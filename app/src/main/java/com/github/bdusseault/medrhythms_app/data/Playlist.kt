package com.github.bdusseault.medrhythms_app.data

import java.time.LocalDateTime
import java.util.*

/**
 * Data class for Playlists. Implements [Playlist.hashCode] and [Playlist.equals], including
 * all member types and data classes
 * @author Bret Dusseault
 * @since 9/6/2021
 */
data class Playlist(
    val UUID: UUID, val Name: String, var Tracks: ArrayList<TrackEntry>,
    val CreationDate: LocalDateTime, var UpdateDate: LocalDateTime
)
{
    data class Track(
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
        data class Analysis(
            var Version: String, var Time: Float, var SampleCount: Int, var BPM: Float,
            var FirstStrongBeatSec: Float, var LastStrongBeatSec: Float, var BeatMap: Array<Float>,
            var Duration: Float
        )
        {
            override fun equals(other: Any?): Boolean
            {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as Analysis

                if (Version != other.Version) return false
                if (Time != other.Time) return false
                if (SampleCount != other.SampleCount) return false
                if (BPM != other.BPM) return false
                if (FirstStrongBeatSec != other.FirstStrongBeatSec) return false
                if (LastStrongBeatSec != other.LastStrongBeatSec) return false
                if (!BeatMap.contentEquals(other.BeatMap)) return false
                if (Duration != other.Duration) return false

                return true
            }

            override fun hashCode(): Int
            {
                var result = Version.hashCode()
                result = 31 * result + Time.hashCode()
                result = 31 * result + SampleCount
                result = 31 * result + BPM.hashCode()
                result = 31 * result + FirstStrongBeatSec.hashCode()
                result = 31 * result + LastStrongBeatSec.hashCode()
                result = 31 * result + BeatMap.contentHashCode()
                result = 31 * result + Duration.hashCode()
                return result
            }
        }

        data class FileEncoding(
            val Type: String, val Bitrate: String, val Channels: Int,
            val Encoding: String, val SampleRate: Int
        )
    }

    data class TrackEntry(val Track: Track, var Ordering: Int)
}