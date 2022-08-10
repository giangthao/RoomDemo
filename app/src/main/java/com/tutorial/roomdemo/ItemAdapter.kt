package com.tutorial.roomdemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tutorial.roomdemo.databinding.ItemsRowsBinding
/**
 * We have the @param [items] to represent the list that populates the adapter
 * The @param [updateListener] to listen to the edit icon an get the positions id
 * The @param [deleteListener] to listen to the delete icon and get the positions id
 **/

class ItemAdapter(private val items: ArrayList<EmployeeEntity>,
                    private val updateListener:(id: Int) -> Unit,
                  private val deleteListener:(id: Int) -> Unit

): RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    inner class ViewHolder(binding: ItemsRowsBinding): RecyclerView.ViewHolder(binding.root) {
        // Holds the TextView that will add each item to
        val llMain = binding.llMain
        val tvName = binding.tvName
        val tvEmail = binding.tvEmail
        val tvEdit = binding.ivEdit
        val ivDelete = binding.ivDelete
    }
    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemsRowsBinding.inflate(LayoutInflater.from(parent.context)
        ,parent,false
        ))
    }
    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val context = holder.itemView.context
    val item = items[position]
        holder.tvName.text = item.name
        holder.tvEmail.text = item.email
        if (position%2==0){
            holder.llMain.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    androidx.core.R.color.androidx_core_ripple_material_light
                )
            )
        } else {
            holder.llMain.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    androidx.core.R.color.androidx_core_secondary_text_default_material_light
                )
            )
        }
        holder.tvEdit.setOnClickListener{
            updateListener.invoke(item.id)
        }
        holder.ivDelete.setOnClickListener {
            updateListener.invoke(item.id   )
        }

    }
    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return items.size
}
}