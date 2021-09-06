package com.github.bdusseault.medrhythms_app.web

import com.github.bdusseault.medrhythms_app.data.Playlist
import java.util.*

/**
 * Generic retrieval interface for [Playlist]
 * @author Bret Dusseault
 * @since 9/6/2021
 */
interface IPlaylistRequester
{
    /**
     * Retrieves a [Playlist] given a UUID
     * @param uuid a valid UUID4 style UUID
     */
    fun getPlaylist(uuid: UUID): Optional<Playlist>

    /**
     * Makes a request with the given [Playlist] to update it
     * @param playlist a playlist with a valid [Playlist.UUID]
     */
    fun updatePlaylist(playlist: Playlist): Boolean
}