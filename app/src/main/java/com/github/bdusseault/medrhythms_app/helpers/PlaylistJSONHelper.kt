package com.github.bdusseault.medrhythms_app.helpers

import com.github.bdusseault.medrhythms_app.data.Playlist
import com.github.bdusseault.medrhythms_app.data.PlaylistDataProcessor
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

/**
 * Helper class for parsing [Playlist] to or from JSON
 * @author Bret Dusseault
 * @since 9/6/2021
 */
object PlaylistJSONHelper
{
    /**
     * Expects a string-ified JSON body to create a playlist
     * @param input JSON body
     */
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
            val track = createTrack(playlistTracksArr.getJSONObject(i))
            PlaylistDataProcessor.cleanBeatMap(track.Track)
            playlistTracks.add(track)
        }

        return Playlist(playlistUUID, playlistName, playlistTracks, playlistCreateDate, playlistUpdateDate)
    }

    private fun createOptString(inString: String): Optional<String>
    {
        return if (inString.isEmpty()) Optional.empty() else Optional.of(inString)
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

    /**
     * Converts an existing playlist to its JSON representation
     * @param playlist A playlist with any number of elements
     */
    fun convertPlaylistToJSON(playlist: Playlist): JSONObject
    {
        val rootObj = JSONObject()

        val playlistObj = JSONObject()
        playlistObj.put("uuid", playlist.UUID.toString())
        playlistObj.put("name", playlist.Name)
        playlistObj.put("created_at", DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(playlist.CreationDate))
        playlistObj.put("updated_at", DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(playlist.UpdateDate))

        val playlistTrackArr = JSONArray()
        for (entry: Playlist.TrackEntry in playlist.Tracks)
        {
            val trackEntryObj = JSONObject()
            trackEntryObj.put("order", entry.Ordering)

            val trackObj = JSONObject()
            trackObj.put("source", entry.Track.Source)
            trackObj.put("source_track_ext_id", entry.Track.SourceExtID)
            trackObj.putOpt("artist", entry.Track.Artist.orElse(null))
            trackObj.put("title", entry.Track.Title)
            trackObj.putOpt("album", entry.Track.Album.orElse(null))
            trackObj.putOpt("genre", entry.Track.Genre.orElse(null))
            trackObj.put("uuid", entry.Track.UUID.toString())

            val trackEncodingObj = JSONObject()
            trackEncodingObj.put("type", entry.Track.Encoding.Type)
            trackEncodingObj.put("bitrate", entry.Track.Encoding.Bitrate)
            trackEncodingObj.put("channels", entry.Track.Encoding.Channels)
            trackEncodingObj.put("encoding", entry.Track.Encoding.Encoding)
            trackEncodingObj.put("sample_rate", entry.Track.Encoding.SampleRate)
            trackObj.put("file_encoding", trackEncodingObj)

            val trackAnalysisObj = JSONObject()
            trackAnalysisObj.put("analysis_version", entry.Track.TrackAnalysis.Version)
            trackAnalysisObj.put("analysis_time", entry.Track.TrackAnalysis.Time)
            trackAnalysisObj.put("sample_count", entry.Track.TrackAnalysis.SampleCount)
            trackAnalysisObj.put("bpm", entry.Track.TrackAnalysis.BPM)
            trackAnalysisObj.put("first_strong_beat_sec", entry.Track.TrackAnalysis.FirstStrongBeatSec)
            trackAnalysisObj.put("last_strong_beat_sec", entry.Track.TrackAnalysis.LastStrongBeatSec)
            trackAnalysisObj.put("duration_sec", entry.Track.TrackAnalysis.Duration)
            val trackAnalysisBeatmapArr = JSONArray()
            for(f: Float in entry.Track.TrackAnalysis.BeatMap)
            {
                trackAnalysisBeatmapArr.put(f.toDouble())
            }
            trackAnalysisObj.put("beat_map", trackAnalysisBeatmapArr)
            trackObj.put("track_analysis", trackAnalysisObj)

            trackEntryObj.put("track", trackObj)
            playlistTrackArr.put(trackEntryObj)
        }
        playlistObj.put("tracks", playlistTrackArr)

        rootObj.put("playlist", playlistObj)

        return rootObj
    }
}