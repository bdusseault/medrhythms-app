package com.github.bdusseault.medrhythms_app

import android.os.Bundle
import android.os.PersistableBundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.github.bdusseault.medrhythms_app.data.PlaylistManager
import com.github.bdusseault.medrhythms_app.views.PlaylistMenuAdapter
import com.github.bdusseault.medrhythms_app.helpers.PlaylistException
import com.github.bdusseault.medrhythms_app.web.IPlaylistRequester
import com.github.bdusseault.medrhythms_app.web.LocalPlaylistRequester
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity()
{
    //Playlist data
    private val endpoint = "https://medrhythms.com/api/v1/playlists/"
    private val playlistUUIDs: ArrayList<UUID> = ArrayList();
    private var currentPlaylistUUID: UUID = UUID(0, 0)
//    private val playlistRequester: IPlaylistRequester = PlaylistRequester(URL(endpoint))
    private lateinit var playlistRequester: IPlaylistRequester

    //States
    private val LAST_PLAYLIST_SELECTED = "LAST_PLAYLIST_SELECTED"

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        playlistRequester = LocalPlaylistRequester(assets)

        buildPlaylistUUIDs()
        populatePlaylistMenu()

        if (savedInstanceState != null)
        {
            val uuidStr = savedInstanceState.getString(LAST_PLAYLIST_SELECTED)
            if(uuidStr != null)
            {
                currentPlaylistUUID =
                    UUID.fromString(uuidStr)
            }
        }

        val playlistNav: RecyclerView = findViewById(R.id.playlist_view)
        playlistNav.adapter = PlaylistMenuAdapter(PlaylistManager.GetPlaylists())
        playlistNav.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener
        {
            val gestureDetector = GestureDetector(applicationContext, object: GestureDetector.SimpleOnGestureListener()
            {
                override fun onSingleTapUp(e: MotionEvent?): Boolean
                {
                    return true
                }

                override fun onContextClick(e: MotionEvent?): Boolean
                {
                    return true
                }

                override fun onDown(e: MotionEvent?): Boolean
                {
                    return true
                }
            })

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean
            {
                return gestureDetector.onTouchEvent(e)
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent)
            {
                val childView = rv.findChildViewUnder(e.x, e.y)
                if(childView != null)
                {
                    println("Test message!")
                }
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) { }
        })
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle)
    {
        outState.putString(LAST_PLAYLIST_SELECTED, currentPlaylistUUID.toString())
        super.onSaveInstanceState(outState, outPersistentState)
    }

    private fun buildPlaylistUUIDs()
    {
        playlistUUIDs.add(UUID.fromString("6778140e-5e48-4ed5-aa03-a811b836d5d5"))
    }

    private fun populatePlaylistMenu()
    {
        playlistUUIDs.forEach {
            PlaylistManager.AddPlaylist(playlistRequester.GetPlaylist(it).orElseThrow({PlaylistException("Could not build playlist!")}))
        }
    }
}