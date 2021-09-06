package com.github.bdusseault.medrhythms_app.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.bdusseault.medrhythms_app.R
import com.github.bdusseault.medrhythms_app.data.Playlist
import kotlin.math.ceil

class PlaylistTracksAdapter(private val tracks: List<Playlist.TrackEntry>) : RecyclerView.Adapter<PlaylistTracksAdapter.ViewHolder>()
{
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val trackName: TextView = view.findViewById(R.id.playlist_item_track_name)
        val trackArtist: TextView = view.findViewById(R.id.playlist_item_track_artist)
        val trackDuration: TextView = view.findViewById(R.id.playlist_item_track_duration)

    }

    private val durationFormat = "%d:%02d"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_playlist_item_row_entry, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val track = tracks[position].Track
        holder.trackName.text = track.Title

        if(track.Artist.isPresent)
        {
            holder.trackArtist.text = track.Artist.get()
        }

        val duration = ceil(track.TrackAnalysis.Duration.toDouble()).toInt()
        holder.trackDuration.text = durationFormat.format(duration / 60, duration % 60)
    }

    override fun getItemCount(): Int
    {
        return tracks.size
    }
}