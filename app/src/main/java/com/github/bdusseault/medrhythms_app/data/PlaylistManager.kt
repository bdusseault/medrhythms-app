package com.github.bdusseault.medrhythms_app.data

import java.util.*
import kotlin.collections.ArrayList

object PlaylistManager
{
    private val Playlists: ArrayList<Playlist> = ArrayList()
    private var CurrentPlaylistUUID: Optional<UUID> = Optional.empty()

    fun GetCurrentPlaylist(): Optional<Playlist>
    {
        if(CurrentPlaylistUUID.isPresent)
        {
            val filtered = Playlists.filter { it.UUID == CurrentPlaylistUUID.get() }
            if(filtered.isNotEmpty())
            {
                return Optional.of(filtered.component1())
            }
        }

        return Optional.empty()
    }

    fun SetCurrentPlaylist(uuid: UUID)
    {
        CurrentPlaylistUUID = Optional.ofNullable(uuid)
    }

    fun GetPlaylists(): List<Playlist>
    {
        return Playlists
    }

    fun AddPlaylist(playlist: Playlist)
    {
        Playlists.add(playlist)
    }
}