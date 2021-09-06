package com.github.bdusseault.medrhythms_app.views

import androidx.recyclerview.widget.RecyclerView

// Some inspiration drawn from https://www.journaldev.com/23208/android-recyclerview-drag-and-drop
// but modified to have better logical structure/reusability
/**
 * Generic callback for Drag & Drop events
 * @author Bret Dusseault
 * @since 9/6/2021
 */
interface DragNDropHelperContract<VH : RecyclerView.ViewHolder>
{
    /**
     * Called when an item is moved, used to update internal data structure
     * @param fromPos Position the currently moved item originates at
     * @param toPos Position the currently moved item is ending at
     */
    fun onRowMoved(fromPos: Int, toPos: Int)

    /**
     * Called when a Drag & Drop event is started, usually for graphical purposes
     * @param viewHolder The viewholder being moved
     */
    fun onRowSelected(viewHolder: VH)

    /**
     * Called when a Drag & Drop event is ending, or the viewholder being moved clears another item
     * @param viewHolder The viewholder that should have its graphics reset
     */
    fun onRowClear(viewHolder: VH)
}