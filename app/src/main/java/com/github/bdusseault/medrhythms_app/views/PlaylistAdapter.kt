package com.github.bdusseault.medrhythms_app.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.bdusseault.medrhythms_app.R
import com.github.bdusseault.medrhythms_app.data.Playlist


class PlaylistAdapter(private val data: List<Playlist>, private val parent: PlaylistFragment) : RecyclerView.Adapter<PlaylistAdapter.ViewHolder>()
{
    inner class ViewHolder(view: View, ) : RecyclerView.ViewHolder(view)
    {
        val textView: TextView = view.findViewById(R.id.recycler_simple_text_row_entry)
        lateinit var playlist: Playlist

        init
        {
            view.setOnClickListener { parent.triggerOnClickListeners(playlist) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_simple_text_row_entry, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.playlist = data[position]
        holder.textView.text = data[position].Name
    }

    override fun getItemCount(): Int = data.size
}