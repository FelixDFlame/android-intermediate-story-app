package com.example.storyapp.ui.storyadd

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.storyapp.databinding.ActivityStoryAddBinding
import com.example.storyapp.model.DicodingStoryBasicResponse
import com.example.storyapp.model.Result
import com.example.storyapp.ui.camera.CameraActivity
import com.example.storyapp.ui.main.MainActivity
import com.example.storyapp.utils.PrefUtils
import com.example.storyapp.utils.reduceFileImage
import com.example.storyapp.utils.rotateBitmap
import com.example.storyapp.utils.uriToFile
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.button.MaterialButton
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException
import java.util.*

class StoryAddActivity : AppCompatActivity() {
    private var _binding: ActivityStoryAddBinding? = null
    private val binding get() = _binding!!

    private lateinit var storyAddViewModel: StoryAddViewModel

    private var getFile: File? = null

    private lateinit var imageView: ImageView
    private lateinit var btnCamera: MaterialButton
    private lateinit var btnGallery: MaterialButton
    private lateinit var btnUpload: MaterialButton
    private lateinit var btnRefreshLocation: MaterialButton
    private lateinit var etDescription: EditText

    private var locationLat: Double? = null
    private var locationLon: Double? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityStoryAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: ViewModelFactory = ViewModelFactory.getInstance()
        val vm: StoryAddViewModel by viewModels {
            factory
        }

        storyAddViewModel = vm

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setupUI()
        requestPermission()
        getMyLastLocation()

        setButtonListener()
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getAddressName(lat: Double, lon: Double): String? {
        var addressName: String? = null
        val geocoder = Geocoder(this@StoryAddActivity, Locale.getDefault())
        try {
            val list = geocoder.getFromLocation(lat, lon, 1)
            if (list != null && list.size != 0) {
                addressName = list[0].getAddressLine(0)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return addressName
    }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    locationLat = location.latitude
                    locationLon = location.longitude
                    val addressName = getAddressName(location.latitude, location.longitude)
                    if (addressName != null) {
                        binding.tvLocation.text = "Location: " + addressName
                        toast("Location is found. Address: " + addressName)
                    } else {
                        binding.tvLocation.text = "Location: Address not found"
                        toast("Location is found. Address not found")
                    }
                } else {
                    binding.tvLocation.text = "Location: -"
                    toast("Location is not found. Try Again")
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile

            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )

            binding.imageView.setImageBitmap(result)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                toast("Permission not granted.")
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun setupUI() {
        imageView = binding.imageView
        btnCamera = binding.btnCamera
        btnGallery = binding.btnGallery
        btnUpload = binding.btnUpload
        btnRefreshLocation = binding.btnRefreshLocation
        etDescription = binding.etDescription
    }

    private fun requestPermission() {
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLastLocation()
                }
                else -> {
                    // No location access granted.
                }
            }
        }

    private fun setButtonListener() {
        btnCamera.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            launcherIntentCameraX.launch(intent)
        }
        btnGallery.setOnClickListener {
            startGallery()
        }
        btnUpload.setOnClickListener {
            uploadImage()
        }
        btnRefreshLocation.setOnClickListener {
            getMyLastLocation()
        }
    }

    private fun uploadImage() {
        if (isUploadValid()) {
            val file = reduceFileImage(getFile as File)

            val prefUtils = PrefUtils(this)
            val token = prefUtils.getString(PrefUtils.PREF_TOKEN)

            val description =
                etDescription.text.toString().toRequestBody("text/plain".toMediaType())

            val lat = if (locationLat == null) null else locationLat.toString()
                .toRequestBody("text/plain".toMediaType())
            val lon = if (locationLon == null) null else locationLon.toString()
                .toRequestBody("text/plain".toMediaType())

            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
            storyAddViewModel.doUpload(token, imageMultipart, description, lat, lon)
                .observe(this) { result ->
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Success -> {
                            showLoading(false)
                            setupAddedStory(result.data)
                        }
                        is Result.Error -> {
                            showLoading(false)
                            toast(result.error)
                        }
                    }
                }
        }
    }

    private fun isUploadValid(): Boolean {
        if (getFile == null) {
            toast("Image required.")
            return false
        }

        if (etDescription.text.isNullOrEmpty()) {
            toast("Description required.")
            etDescription.requestFocus()
            return false
        }

        return true
    }

    private fun setupAddedStory(dicodingStoryBasicResponse: DicodingStoryBasicResponse) {
        val message = dicodingStoryBasicResponse.message
        val error = dicodingStoryBasicResponse.error
        toast(message)
        if (!error) {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun toast(it: String?) {
        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@StoryAddActivity)
            getFile = myFile
            binding.imageView.setImageURI(selectedImg)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}