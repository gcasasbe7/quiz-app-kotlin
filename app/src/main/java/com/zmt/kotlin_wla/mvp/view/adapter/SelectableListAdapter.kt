package com.zmt.kotlin_wla.mvp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zmt.kotlin_wla.R
import com.zmt.kotlin_wla.mvp.helper.Utils
import com.zmt.kotlin_wla.mvp.presenter.IGameParameterListener

/**
 * Custom RecyclerView Adapter to represent text items within our Selectable List
 */
class SelectableListAdapter(private val mDataset: ArrayList<String>, private var mListener: IGameParameterListener) : RecyclerView.Adapter<SelectableListAdapter.ViewHolder>() {

    /**
     Inner class to declare an element of the list as a RecyclerView Holder.
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitle : TextView = itemView.findViewById(R.id.txt_row_item_title)
    }

    /**
     * Item View Holder creation. Bind the custom view to our custom text item
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.text_row_item, parent, false)

        return ViewHolder(view)
    }

    /**
     * Define the binding methodology for each listed element
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Fetch the value from the dataset
        val textValue = mDataset[position]
        // Set the value to the row title
        holder.txtTitle.text = textValue
        // Bind the callback action
        holder.itemView.setOnClickListener {
            if (Utils.isValidEntity(textValue)) {
                mListener.onOptionSelected(textValue)
            }
        }
    }

    /**
     * Overridden method to provide the adapter dataset size
     */
    override fun getItemCount(): Int { return mDataset.size }
}