package com.github.bdusseault.medrhythms_app.data

import java.time.LocalDateTime
import java.util.*

class Playlist(
    val UUID: UUID, val Name: String, val Tracks: ArrayList<TrackEntry>,
    val CreationDate: LocalDateTime, var UpdateDate: LocalDateTime
)
{
    class Track(
        val Source: String,
        val SourceExtID: String,
        val Artist: Optional<String>,
        val TrackAnalysis: Analysis,
        val Title: String,
        val Album: Optional<String>,
        val Genre: Optional<String>,
        val UUID: UUID,
        val Encoding: FileEncoding
    )
    {
        class Analysis(
            var Version: String, var Time: Float, var SampleCount: Int, var BPM: Float,
            var FirstStrongBeatSec: Float, var LastStrongBeatSec: Float, var BeatMap: Array<Float>,
            var Duration: Float
        )

        class FileEncoding(
            val Type: String, val Bitrate: String, val Channels: Int,
            val Encoding: String, val SampleRate: Int
        )
    }

    class TrackEntry(val Track: Track, var Ordering: Int)
}