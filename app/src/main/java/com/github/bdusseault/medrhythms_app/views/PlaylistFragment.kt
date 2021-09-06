package com.github.bdusseault.medrhythms_app.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.github.bdusseault.medrhythms_app.R
import com.github.bdusseault.medrhythms_app.data.Playlist

class PlaylistFragment(private val playlistData: List<Playlist>) : Fragment(R.layout.playlist_fragment)
{
    fun interface OnClickListener
    {
        fun OnClick(playlist: Playlist)
    }

    private val onClickListeners: ArrayList<OnClickListener> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.playlist_recycler)
        //There'd be no sorting in a full implementation of this sort of app
        recyclerView.adapter = PlaylistAdapter(playlistData.sortedBy { it.Name }, this)
    }

    fun registerOnItemClickListener(listener: OnClickListener)
    {
        onClickListeners.add(listener)
    }

    fun triggerOnClickListeners(playlist: Playlist)
    {
        onClickListeners.forEach { it.OnClick(playlist) }
    }
}