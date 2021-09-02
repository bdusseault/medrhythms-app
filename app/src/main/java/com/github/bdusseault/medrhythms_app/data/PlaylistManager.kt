package com.github.bdusseault.medrhythms_app.data

object PlaylistManager
{
    private val Playlists: ArrayList<Playlist> = ArrayList()

    fun GetPlaylists(): List<Playlist>
    {
        return Playlists
    }

    fun AddPlaylist(playlist: Playlist)
    {
        Playlists.add(playlist)
    }
}