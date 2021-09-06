package com.github.bdusseault.medrhythms_app

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.github.bdusseault.medrhythms_app.data.PlaylistManager
import com.github.bdusseault.medrhythms_app.helpers.PlaylistException
import com.github.bdusseault.medrhythms_app.views.PlaylistAdapter
import com.github.bdusseault.medrhythms_app.views.PlaylistFragment
import com.github.bdusseault.medrhythms_app.views.PlaylistTracksFragment
import com.github.bdusseault.medrhythms_app.web.IPlaylistRequester
import com.github.bdusseault.medrhythms_app.web.LocalPlaylistRequester
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity()
{
    //Playlist data
    private val endpoint = "https://medrhythms.com/api/v1/playlists/"
    private val playlistUUIDs: ArrayList<UUID> = ArrayList();
//    private val playlistRequester: IPlaylistRequester = PlaylistRequester(URL(endpoint))
    private lateinit var playlistRequester: IPlaylistRequester
    private lateinit var playlistFragment: PlaylistFragment
    private lateinit var playlistTracksFragment: PlaylistTracksFragment

    //States
    private val LAST_PLAYLIST_SELECTED = "LAST_PLAYLIST_SELECTED"
    private val BACKSTACK_PLAYLIST = "playlist_fragment"

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
                PlaylistManager.SetCurrentPlaylist(UUID.fromString(uuidStr))
            }
            else if(playlistUUIDs.isNotEmpty())
            {
                PlaylistManager.SetCurrentPlaylist(playlistUUIDs.component1())
            }
        }
        else
        {
            if(playlistUUIDs.isNotEmpty())
            {
                PlaylistManager.SetCurrentPlaylist(playlistUUIDs.component1())
            }
        }

        playlistFragment = PlaylistFragment(PlaylistManager.GetPlaylists())
        playlistTracksFragment = PlaylistTracksFragment()
        val curPlaylist = PlaylistManager.GetCurrentPlaylist()
        if(curPlaylist.isPresent)
        {
            playlistTracksFragment.tracks = curPlaylist.get().Tracks
        }

        playlistFragment.registerOnItemClickListener {
            val currPlaylist = PlaylistManager.GetCurrentPlaylist()
            if (!currPlaylist.isPresent)
            {
                PlaylistManager.SetCurrentPlaylist(it.UUID)
                playlistTracksFragment.tracks = it.Tracks
            }
            else if(currPlaylist.get().UUID != it.UUID)
            {
                currPlaylist.get().Tracks = ArrayList(playlistTracksFragment.tracks)
                PlaylistManager.SetCurrentPlaylist(it.UUID)
                playlistTracksFragment.tracks = it.Tracks
            }

            supportFragmentManager.commit {
                replace(R.id.fragment_layout_container, playlistTracksFragment)
                addToBackStack(BACKSTACK_PLAYLIST)
            }
        }

        supportFragmentManager.commit {
            replace(R.id.fragment_layout_container, playlistFragment)
        }

        findViewById<Button>(R.id.test_button_frag1).setOnClickListener {
            supportFragmentManager.commit {
                replace(R.id.fragment_layout_container, playlistFragment)
            }
        }

        findViewById<Button>(R.id.test_button_frag2).setOnClickListener {
            supportFragmentManager.commit {
                replace(R.id.fragment_layout_container, playlistTracksFragment)
                addToBackStack(BACKSTACK_PLAYLIST)
            }
        }

//        val playlistNav: RecyclerView = findViewById(R.id.playlist_view)
//        playlistNav.adapter = PlaylistAdapter(PlaylistManager.GetPlaylists())
//        playlistNav.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener
//        {
//            val gestureDetector = GestureDetector(applicationContext, object: GestureDetector.SimpleOnGestureListener()
//            {
//                override fun onSingleTapUp(e: MotionEvent?): Boolean
//                {
//                    return true
//                }
//
//                override fun onContextClick(e: MotionEvent?): Boolean
//                {
//                    return true
//                }
//
//                override fun onDown(e: MotionEvent?): Boolean
//                {
//                    return true
//                }
//            })
//
//            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean
//            {
//                return gestureDetector.onTouchEvent(e)
//            }
//
//            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent)
//            {
//                val childView = rv.findChildViewUnder(e.x, e.y)
//                if(childView != null)
//                {
//                    println("Test message!")
//                }
//            }
//
//            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) { }
//        })
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle)
    {
        val curPlaylist = PlaylistManager.GetCurrentPlaylist()
        if(curPlaylist.isPresent)
        {
            outState.putString(
                LAST_PLAYLIST_SELECTED,
                curPlaylist.get().UUID.toString()
            )
        }
        super.onSaveInstanceState(outState, outPersistentState)
    }

    private fun buildPlaylistUUIDs()
    {
        playlistUUIDs.add(UUID.fromString("6778140e-5e48-4ed5-aa03-a811b836d5d5"))
        playlistUUIDs.add(UUID.fromString("bda16db7-33ce-4070-a6cf-ca76f0cf088a"))
        playlistUUIDs.add(UUID.fromString("da0e9d6b-209e-47d4-8b58-e19363e4b7e3"))
    }

    private fun populatePlaylistMenu()
    {
        playlistUUIDs.forEach {
            PlaylistManager.AddPlaylist(playlistRequester.GetPlaylist(it).orElseThrow({PlaylistException("Could not build playlist!")}))
        }
    }
}