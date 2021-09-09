package com.github.bdusseault.medrhythms_app.data

import org.junit.Test
import java.util.*
import org.junit.Assert.*

class PlaylistDataProcessorTests
{
    @Test
    fun cleanBeatMap_truncatesFloats()
    {
        val track = Playlist.Track("none", "none", Optional.of("none"),
         Playlist.Track.Analysis("none", 1f, 10, 120f, 0.2f,
            1.93f, arrayOf(0.2392f, 0.92819f, 1.0222f, 1.1f, 1.83271f), 5f),
        "none", Optional.of("none"), Optional.of("none"), UUID.randomUUID(),
         Playlist.Track.FileEncoding("none", "none", 2, "none", 44100))

        val copyTrack = track.copy()
        PlaylistDataProcessor.cleanBeatMap(copyTrack)

        assertEquals(0.239f, copyTrack.TrackAnalysis.FirstStrongBeatSec)
        assertEquals(1.833f, copyTrack.TrackAnalysis.LastStrongBeatSec)
        assert(copyTrack.TrackAnalysis.BeatMap.contentEquals(arrayOf(0.239f, 0.928f, 1.022f, 1.1f, 1.833f)))
    }

    @Test
    fun cutOffHalfBeats_verify()
    {
        val track = Playlist.Track("none", "none", Optional.of("none"),
            Playlist.Track.Analysis("none", 1f, 10, 120f, 0.2f,
                1.93f, arrayOf(0.2392f, 0.92819f, 1.0222f, 1.1f, 1.83271f), 5f),
            "none", Optional.of("none"), Optional.of("none"), UUID.randomUUID(),
            Playlist.Track.FileEncoding("none", "none", 2, "none", 44100))

        PlaylistDataProcessor.cutOffHalfBeats(track)
        assert(track.TrackAnalysis.BeatMap.contentEquals(arrayOf(0.2392f, 1.0222f, 1.83271f)))
    }
}