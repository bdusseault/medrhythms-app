package com.github.bdusseault.medrhythms_app.web

import android.content.res.AssetManager
import com.github.bdusseault.medrhythms_app.data.Playlist
import com.github.bdusseault.medrhythms_app.helpers.PlaylistJSONParser
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.util.*
import java.util.function.Supplier
import kotlin.collections.HashMap

class LocalPlaylistRequester(private val assetManager: AssetManager) : IPlaylistRequester
{
    private val playlistUUIDMapping: HashMap<UUID, PlaylistFileEntry> = HashMap()
    class PlaylistFileEntry(val fileName: String, var playlist: Playlist)

    init
    {
        assetManager.list("playlists/")?.forEach {
            val inStream = BufferedReader(InputStreamReader(assetManager.open(it, AssetManager.ACCESS_BUFFER)))
            val rawInput = inStream.lines().reduce { carry, nextLine -> carry + nextLine }
            inStream.close()

            val playlist = PlaylistJSONParser.CreatePlaylist(rawInput.orElseThrow(Supplier { Exception("Failed to create Playlist from JSON input") }))
            playlistUUIDMapping[playlist.UUID] = PlaylistFileEntry(it, playlist)
        }
    }

    override fun GetPlaylist(uuid: UUID): Playlist
    {
        return playlistUUIDMapping[uuid]!!.playlist
    }

    override fun UpdatePlaylist(playlist: Playlist): Boolean
    {
        //changes are not persisted between app runs, because it's an asset and not the web API
        if(playlistUUIDMapping.containsKey(playlist.UUID))
        {
            playlistUUIDMapping[playlist.UUID]!!.playlist = playlist
            return true
        }

        return false
    }
}