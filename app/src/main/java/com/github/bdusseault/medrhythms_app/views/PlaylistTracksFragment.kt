package com.github.bdusseault.medrhythms_app.views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.github.bdusseault.medrhythms_app.R
import com.github.bdusseault.medrhythms_app.data.Playlist

/**
 * Fragment that has a [RecyclerView] to display a list of [Playlist.TrackEntry] and implement
 * drag & drop behaviors
 * @author Bret Dusseault
 * @since 9/6/2021
 */
class PlaylistTracksFragment : Fragment(R.layout.playlist_tracks_fragment)
{
    private var tracksView: RecyclerView? = null
    private var itemTouchHelper: ItemTouchHelper? = null

    /**
     * List reference to a playlist's tracks. Will be modified by drag & drops.
     */
    var tracks: MutableList<Playlist.TrackEntry> = ArrayList()
        set(value)
        {
            field = value
            if(tracksView != null)
            {
                // Destroy previous drag/drop helper
                itemTouchHelper!!.attachToRecyclerView(null)

                val adapter = PlaylistTracksAdapter(field)
                val dragCallback = PlaylistTracksAdapter.DragNDropCallback(adapter)
                itemTouchHelper = ItemTouchHelper(dragCallback)
                itemTouchHelper!!.attachToRecyclerView(tracksView)
                tracksView!!.adapter = adapter
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        tracksView = view.findViewById(R.id.playlist_tracks_recycler)
        val adapter = PlaylistTracksAdapter(tracks)
        val dragCallback = PlaylistTracksAdapter.DragNDropCallback(adapter)
        itemTouchHelper = ItemTouchHelper(dragCallback)
        itemTouchHelper!!.attachToRecyclerView(tracksView)
        tracksView!!.adapter = adapter
    }
}