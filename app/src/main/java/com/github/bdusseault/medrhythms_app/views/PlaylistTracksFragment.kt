package com.github.bdusseault.medrhythms_app.views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.github.bdusseault.medrhythms_app.R
import com.github.bdusseault.medrhythms_app.data.Playlist

class PlaylistTracksFragment : Fragment(R.layout.playlist_tracks_fragment)
{
    private var tracksView: RecyclerView? = null

    var tracks: List<Playlist.TrackEntry> = ArrayList()
        set(value)
        {
            field = value
            tracksView?.adapter = PlaylistTracksAdapter(field)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        tracksView = view.findViewById(R.id.playlist_tracks_recycler)
        tracksView!!.adapter = PlaylistTracksAdapter(tracks)
    }
}