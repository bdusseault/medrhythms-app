package com.github.bdusseault.medrhythms_app.web

import com.github.bdusseault.medrhythms_app.data.Playlist
import com.github.bdusseault.medrhythms_app.helpers.PlaylistJSONHelper
import java.io.*
import java.net.URL
import java.util.*

/**
 * Web requester implementation of [IPlaylistRequester] that is unused because there is no
 * valid endpoint to use for this demo
 * @author Bret Dusseault
 * @since 9/6/2021
 */
class PlaylistRequester(private val PlaylistUrl: String) : IPlaylistRequester
{

    override fun getPlaylist(uuid: UUID): Optional<Playlist>
    {
        val connection = URL("${PlaylistUrl}${uuid}").openConnection()
        connection.doOutput = false
        connection.doInput = true

        try
        {
            connection.connect()
            val dataReader = BufferedReader(InputStreamReader(connection.getInputStream()))
            val rawPlaylist = dataReader.lines().reduce({ carry, nextLine -> carry + nextLine })
            dataReader.close()
            if (rawPlaylist.isPresent)
            {
                return Optional.of(PlaylistJSONHelper.createPlaylist(rawPlaylist.get()))
            }
        }
        catch (e: IOException)
        {
            println(e.localizedMessage)
        }

        return Optional.empty()
    }

    override fun updatePlaylist(playlist: Playlist): Boolean
    {
        val connection = URL("${PlaylistUrl}${playlist.UUID}").openConnection()
        connection.doOutput = true
        connection.doInput = false

        try
        {
            connection.connect()
            val jsonPlaylist = PlaylistJSONHelper.convertPlaylistToJSON(playlist)
            val outStream = BufferedWriter(OutputStreamWriter (connection.getOutputStream()))
            outStream.write(jsonPlaylist.toString())
            outStream.flush()
            outStream.close()

            return true
        }
        catch (e: IOException)
        {
            println(e.localizedMessage)
        }

        return false
    }
}