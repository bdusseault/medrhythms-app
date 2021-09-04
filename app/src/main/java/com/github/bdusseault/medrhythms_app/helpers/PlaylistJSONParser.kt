package com.github.bdusseault.medrhythms_app.helpers

import com.github.bdusseault.medrhythms_app.data.Playlist
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

object PlaylistJSONParser
{
    fun createPlaylist(input: String): Playlist
    {
        if(input.isBlank())
        {
            throw PlaylistException("Empty json input object for parsing into playlist provided. Check assets!")
        }

        val baseObj = JSONObject(input)
        val playlistObj = baseObj.getJSONObject("playlist")
        val playlistUUID = UUID.fromString(playlistObj.getString("uuid"))
        val playlistName = playlistObj.getString("name")
        val playlistCreateDate = LocalDateTime.parse(playlistObj.getString("created_at"), DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val playlistUpdateDate = LocalDateTime.parse(playlistObj.getString("updated_at"), DateTimeFormatter.ISO_OFFSET_DATE_TIME)

        val playlistTracksArr = playlistObj.getJSONArray("tracks")
        val playlistTracks: ArrayList<Playlist.TrackEntry> = ArrayList()
        for(i in 0 until playlistTracksArr.length())
        {
            playlistTracks.add(createTrack(playlistTracksArr.getJSONObject(i)))
        }

        return Playlist(playlistUUID, playlistName, playlistTracks, playlistCreateDate, playlistUpdateDate)
    }

    private fun createOptString(inString: String): Optional<String>
    {
        return if (inString.isBlank()) Optional.empty() else Optional.of(inString)
    }

    private fun createTrack(inputJson: JSONObject): Playlist.TrackEntry
    {
        val trackOrder = inputJson.getInt("order")

        val trackObj = inputJson.getJSONObject("track")
        val trackSource = trackObj.getString("source")
        val trackSourceExtId = trackObj.getString("source_track_ext_id")
        val trackArtist = createOptString(trackObj.getString("artist"))
        val trackTitle = trackObj.getString("title")
        val trackAlbum = createOptString(trackObj.optString("album", ""))
        val trackGenre = createOptString(trackObj.optString("genre", ""))
        val trackUUID = UUID.fromString(trackObj.getString("uuid"))

        val trackAnalysisObj = trackObj.getJSONObject("track_analysis")
        val trackAnalysisVer = trackAnalysisObj.getString("analysis_version")
        val trackAnalysisTime = trackAnalysisObj.getDouble("analysis_time").toFloat()
        val trackAnalysisSampleCount = trackAnalysisObj.getInt("sample_count")
        val trackAnalysisBPM = trackAnalysisObj.getDouble("bpm").toFloat()
        val trackAnalysisFirstStrongBeatSec = trackAnalysisObj.getDouble("first_strong_beat_sec").toFloat()
        val trackAnalysisLastStrongBeatSec = trackAnalysisObj.getDouble("last_strong_beat_sec").toFloat()
        val trackAnalysisDuration = trackAnalysisObj.getDouble("duration_sec").toFloat()
        val trackAnalysisBeatMapArr = trackAnalysisObj.getJSONArray("beat_map")
        val trackAnalysisBeatMap = Array(trackAnalysisBeatMapArr.length()) {
            trackAnalysisBeatMapArr.getDouble(it).toFloat()
        }

        val trackFileEncodingObj = trackObj.getJSONObject("file_encoding")
        val trackFileEncodingType = trackFileEncodingObj.getString("type")
        val trackFileEncodingBitrate = trackFileEncodingObj.getString("type")
        val trackFileEncodingChannels = trackFileEncodingObj.getInt("channels")
        val trackFileEncodingEncoding = trackFileEncodingObj.getString("encoding")
        val trackFileEncodingSampleRate = trackFileEncodingObj.getInt("sample_rate")

        return Playlist.TrackEntry(
            Playlist.Track(
                trackSource, trackSourceExtId, trackArtist,
                Playlist.Track.Analysis(trackAnalysisVer, trackAnalysisTime, trackAnalysisSampleCount, trackAnalysisBPM,
                    trackAnalysisFirstStrongBeatSec, trackAnalysisLastStrongBeatSec, trackAnalysisBeatMap, trackAnalysisDuration),
                trackTitle, trackAlbum, trackGenre, trackUUID,
                Playlist.Track.FileEncoding(trackFileEncodingType, trackFileEncodingBitrate, trackFileEncodingChannels,
                    trackFileEncodingEncoding, trackFileEncodingSampleRate)),
            trackOrder)
    }
}