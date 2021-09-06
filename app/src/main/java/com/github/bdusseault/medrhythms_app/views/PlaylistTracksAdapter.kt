package com.github.bdusseault.medrhythms_app.views

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.github.bdusseault.medrhythms_app.R
import com.github.bdusseault.medrhythms_app.data.Playlist
import java.util.*
import kotlin.math.ceil

class PlaylistTracksAdapter(private var tracks: MutableList<Playlist.TrackEntry>) : RecyclerView.Adapter<PlaylistTracksAdapter.ViewHolder>(), DragNDropHelperContract<PlaylistTracksAdapter.ViewHolder>
{
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
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

    override fun onRowMoved(fromPos: Int, toPos: Int)
    {
        if (fromPos < toPos)
        {
            for(i in fromPos until toPos)
            {
                tracks[i].Ordering = i+1
                tracks[i+1].Ordering = i
                Collections.swap(tracks, i, i+1)
            }
        }
        else
        {
            for(i in fromPos until toPos)
            {
                tracks[i].Ordering = i-1
                tracks[i-1].Ordering = i
                Collections.swap(tracks, i, i-1)
            }
        }

        notifyItemMoved(fromPos, toPos)
    }

    override fun onRowSelected(viewHolder: ViewHolder)
    {
        viewHolder.view.setBackgroundColor(Color.GRAY)
    }

    override fun onRowClear(viewHolder: ViewHolder)
    {
        viewHolder.view.setBackgroundColor(Color.WHITE)
    }

    class DragNDropCallback(private val adapter: DragNDropHelperContract<PlaylistTracksAdapter.ViewHolder>) : ItemTouchHelper.Callback()
    {
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int
        {
            val dragFlags = ItemTouchHelper.UP.or(ItemTouchHelper.DOWN)
            return makeMovementFlags(dragFlags, 0)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean
        {
            adapter.onRowMoved(viewHolder.adapterPosition, target.adapterPosition)
            return true
        }

        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int)
        {
            if(actionState != ItemTouchHelper.ACTION_STATE_IDLE)
            {
                if(viewHolder is ViewHolder)
                {
                    adapter.onRowClear(viewHolder)
                }
            }

            super.onSelectedChanged(viewHolder, actionState)
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder)
        {
            super.clearView(recyclerView, viewHolder)

            if (viewHolder is ViewHolder)
            {
                adapter.onRowClear(viewHolder)
            }
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int)
        {
            // Do nothing
        }

        override fun isLongPressDragEnabled(): Boolean
        {
            return true
        }

        override fun isItemViewSwipeEnabled(): Boolean
        {
            return false
        }
    }
}