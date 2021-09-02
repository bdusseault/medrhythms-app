package com.github.bdusseault.medrhythms_app.web

import com.github.bdusseault.medrhythms_app.data.Playlist
import java.util.*

interface IPlaylistRequester
{

    fun GetPlaylist(uuid: UUID): Playlist

    fun UpdatePlaylist(playlist: Playlist): Boolean
}