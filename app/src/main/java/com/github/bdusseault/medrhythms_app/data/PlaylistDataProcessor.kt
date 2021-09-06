package com.github.bdusseault.medrhythms_app.data

import kotlin.math.round

/**
 * Helper object for processing data in [Playlist] objects
 * @author Bret Dusseault
 * @since 9/6/2021
 */
object PlaylistDataProcessor
{
    private fun round(input: Float, places: Int): Float
    {
        var multiplier = 1.0f
        repeat(places) {multiplier *= 10}
        return round(input * multiplier) / multiplier
    }

    /**
     * Rounds all floats in [Playlist.Track.TrackAnalysis]'s beatmap to millisecond precision
     * and updates FirstStrongBeatSec and LastStrongBeatSec to the appropriate values
     * @param track An individual track from a playlist
     */
    fun cleanBeatMap(track: Playlist.Track)
    {
        track.TrackAnalysis.BeatMap = track.TrackAnalysis.BeatMap.map{ round(it, 3) }.toTypedArray()
        track.TrackAnalysis.FirstStrongBeatSec = track.TrackAnalysis.BeatMap[0]
        track.TrackAnalysis.LastStrongBeatSec = track.TrackAnalysis.BeatMap[track.TrackAnalysis.BeatMap.size-1]
    }
}