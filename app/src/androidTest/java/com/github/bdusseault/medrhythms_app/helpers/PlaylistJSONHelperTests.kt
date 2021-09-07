package com.github.bdusseault.medrhythms_app.helpers

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.bdusseault.medrhythms_app.data.Playlist
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

@RunWith(AndroidJUnit4::class)
class PlaylistJSONHelperTests
{
    private val playlistJSON = """
            {
                "playlist":
                {
                    "uuid": "6778140e-5e48-4ed5-aa03-a811b836d5d5",
                    "name": "test_playlist",
                    "tracks": [
                    {
                        "track":
                        {
                            "source": "track1.mp4",
                            "source_track_ext_id": "track1_ext_id",
                            "artist": "myself",
                            "track_analysis":
                            {
                                "analysis_version": "1.000",
                                "analysis_time": "10.0",
                                "sample_count": 10,
                                "bpm": "90.0",
                                "first_strong_beat_sec": "0.2923",
                                "last_strong_beat_sec": "0.9333",
                                "beat_map": ["0.2902", "0.555", "0.9333"],
                                "duration_sec": "1.3"
                            },
                            "title": "track1",
                            "album": null,
                            "genre": null,
                            "uuid": "6663ee60-a934-4e27-a751-53e0e4860c86",
                            "file_encoding":
                            {
                                "type": "mp4",
                                "bitrate": "256kbps",
                                "channels": 2,
                                "encoding": "mp4a.40.2, AAC LC",
                                "sample_rate": 44100
                            }
                        },
                        "order": 1
                    }],
                    "created_at": "2018-02-07T21:42:38.717697Z",
                    "updated_at": "2018-07-02T19:33:08.763123Z"
                }
            }
        """.trimIndent()

    private val knownPlaylist = Playlist(UUID.fromString("6778140e-5e48-4ed5-aa03-a811b836d5d5"), "test_playlist", ArrayList(),
        ZonedDateTime.parse("2018-02-07T21:42:38.717697Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
        ZonedDateTime.parse("2018-07-02T19:33:08.763123Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME))

    init
    {
        knownPlaylist.Tracks.add(Playlist.TrackEntry(Playlist.Track("track1.mp4", "track1_ext_id", Optional.of("myself"),
            Playlist.Track.Analysis("1.000", 10f, 10, 90f, 0.29f, 0.933f,
                arrayOf(0.290f, 0.555f, 0.933f), 1.3f),
            "track1", Optional.empty(), Optional.empty(), UUID.fromString("6663ee60-a934-4e27-a751-53e0e4860c86"),
            Playlist.Track.FileEncoding("mp4", "256kbps", 2, "mp4a.40.2, AAC LC", 44100)), 1))
    }

    //Tests that parsed playlists are the same as its 'known' constructed version
    @Test
    fun createPlaylist_verify()
    {
        val parsedPlaylist = PlaylistJSONHelper.createPlaylist(playlistJSON)

        assertEquals(knownPlaylist, parsedPlaylist)
    }

    //Tests that converting the playlist back into JSON, then re-parsing that into a playlist yields
    //an object with identical data
    @Test
    fun convertPlaylistToJSON_verify()
    {
        val jsonifiedPlaylist = PlaylistJSONHelper.convertPlaylistToJSON(knownPlaylist)
        val unjsonifiedPlaylist = PlaylistJSONHelper.createPlaylist(jsonifiedPlaylist.toString())

        assertEquals(knownPlaylist, unjsonifiedPlaylist)
    }
}