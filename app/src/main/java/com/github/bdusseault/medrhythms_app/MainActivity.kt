package com.github.bdusseault.medrhythms_app

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.github.bdusseault.medrhythms_app.web.IPlaylistRequester
import com.github.bdusseault.medrhythms_app.web.LocalPlaylistRequester
import com.github.bdusseault.medrhythms_app.web.PlaylistRequester
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity()
{
    //Playlist data
    private val endpoint = "https://medrhythms.com/api/v1/playlists/"
    private val playlistUUIDs: ArrayList<UUID> = ArrayList();
    private var currentPlaylistUUID: UUID = UUID(0, 0)
//    private val playlistRequester: IPlaylistRequester = PlaylistRequester(URL(endpoint))
    private val playlistRequester: IPlaylistRequester = LocalPlaylistRequester(assets)

    //States
    private val LAST_PLAYLIST_SELECTED = "LAST_PLAYLIST_SELECTED"

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        BuildPlaylistUUIDs()

        if (savedInstanceState != null)
        {
            currentPlaylistUUID =
                UUID.fromString(savedInstanceState.getString(LAST_PLAYLIST_SELECTED))
        }

//        val playlist_nav: RecyclerView = findViewById(R.id.playlist_view)
//        playlist_nav.adapter =
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle)
    {
        outState.putString(LAST_PLAYLIST_SELECTED, currentPlaylistUUID.toString())
        super.onSaveInstanceState(outState, outPersistentState)
    }

    private fun BuildPlaylistUUIDs()
    {
        playlistUUIDs.add(UUID.fromString("6778140e-5e48-4ed5-aa03-a811b836d5d5"))
    }
}