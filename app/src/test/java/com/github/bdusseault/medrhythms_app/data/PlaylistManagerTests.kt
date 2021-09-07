package com.github.bdusseault.medrhythms_app.data

import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class PlaylistManagerTests
{
    @Test
    fun AddPlaylist_noDuplicates()
    {
        val dummyPlaylist = Playlist(UUID.randomUUID(), "", ArrayList(), LocalDateTime.now(), LocalDateTime.now())

        PlaylistManager.AddPlaylist(dummyPlaylist)
        PlaylistManager.AddPlaylist(dummyPlaylist)

        assertEquals(1, PlaylistManager.GetPlaylists().size)
    }
}