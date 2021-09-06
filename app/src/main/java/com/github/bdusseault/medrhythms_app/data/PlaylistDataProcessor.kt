package com.github.bdusseault.medrhythms_app.data

import kotlin.math.round

object PlaylistDataProcessor
{
    // Found at https://discuss.kotlinlang.org/t/how-do-you-round-a-number-to-n-decimal-places/8843
    // while trying to figure out if there's a function in kotlin that already does this :/
    private fun round(input: Float, decimals: Int): Float
    {
        var multiplier = 1.0f
        repeat(decimals) {multiplier *= 10}
        return round(input * multiplier) / multiplier
    }

    fun CleanBeatMap(track: Playlist.Track)
    {
        track.TrackAnalysis.BeatMap = track.TrackAnalysis.BeatMap.map{ round(it, 3) }.toTypedArray()
        track.TrackAnalysis.FirstStrongBeatSec = track.TrackAnalysis.BeatMap[0]
        track.TrackAnalysis.LastStrongBeatSec = track.TrackAnalysis.BeatMap[track.TrackAnalysis.BeatMap.size-1]
    }
}