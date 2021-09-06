package com.github.bdusseault.medrhythms_app.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.github.bdusseault.medrhythms_app.R
import com.github.bdusseault.medrhythms_app.data.Playlist

/**
 * Fragment that contains a [RecyclerView] of [Playlist] objects and provides OnClick callbacks
 * through [OnClickListener]
 * @author Bret Dusseault
 * @since 9/6/2021
 */
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

    /**
     * Registers given listener, may allow duplicate registrations
     * @param listener Listener that will be provided a [Playlist] when one is clicked
     */
    fun registerOnItemClickListener(listener: OnClickListener)
    {
        onClickListeners.add(listener)
    }

    /**
     * Iterates through all registered [OnClickListener] and triggers them
     * @param playlist A playlist that has been 'clicked'
     */
    fun triggerOnClickListeners(playlist: Playlist)
    {
        onClickListeners.forEach { it.OnClick(playlist) }
    }
}