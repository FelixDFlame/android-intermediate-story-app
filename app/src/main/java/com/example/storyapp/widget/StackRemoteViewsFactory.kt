package com.example.storyapp.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.database.StoryDao
import com.example.storyapp.database.StoryItem
import com.example.storyapp.database.StoryRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

internal class StackRemoteViewsFactory(private val mContext: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private var mWidgetItems = listOf<StoryItem>()
    private lateinit var dao: StoryDao

    override fun onCreate() {
        dao = StoryRoomDatabase.getDatabase(mContext.applicationContext).storyDao()
        fetchDataDB()
    }

    override fun onDataSetChanged() {
        fetchDataDB()
    }

    override fun onDestroy() {
    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)

        try {
            val bitmap: Bitmap = Glide.with(mContext.applicationContext)
                .asBitmap()
                .load(mWidgetItems[position].photoUrl)
                .submit()
                .get()
            rv.setImageViewBitmap(R.id.imageView, bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val extras = bundleOf(
            StoryWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false

    private fun fetchDataDB() {
        runBlocking(Dispatchers.IO) {
            try {
                val identityToken = Binder.clearCallingIdentity()
                mWidgetItems = dao.getAllStoryNoPaging()
                Binder.restoreCallingIdentity(identityToken)
            }catch(e:Exception){
                e.printStackTrace()
            }
        }

    }
}

