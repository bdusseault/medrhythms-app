package com.github.bdusseault.medrhythms_app.web

import com.github.bdusseault.medrhythms_app.data.Playlist
import java.net.URL
import java.util.*

class PlaylistRequester(val PlaylistUrl: URL) : IPlaylistRequester
{

    override fun GetPlaylist(uuid: UUID): Playlist
    {
        TODO("Not yet implemented")
    }

    override fun UpdatePlaylist(playlist: Playlist): Boolean
    {
        TODO("Not yet implemented")
        return false
    }
}