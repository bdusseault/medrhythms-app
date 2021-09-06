package com.github.bdusseault.medrhythms_app.views

import androidx.recyclerview.widget.RecyclerView


interface DragNDropHelperContract<VH : RecyclerView.ViewHolder>
{
    fun onRowMoved(fromPos: Int, toPos: Int)
    fun onRowSelected(viewHolder: VH)
    fun onRowClear(viewHolder: VH)
}