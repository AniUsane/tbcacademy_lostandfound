package com.example.lostfound.presentation.lost

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lostfound.data.model.LostFoundItem
import com.example.lostfound.databinding.ItemRecyclerBinding

//checks item ids for updates
class ItemDiffUtil: DiffUtil.ItemCallback<LostFoundItem>(){
    override fun areItemsTheSame(oldItem: LostFoundItem, newItem: LostFoundItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: LostFoundItem, newItem: LostFoundItem): Boolean {
        return oldItem == newItem
    }

}

//adapter for displaying items
class ItemAdapter: ListAdapter<LostFoundItem, ItemAdapter.ItemViewHolder>(ItemDiffUtil()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemAdapter.ItemViewHolder {
        return ItemViewHolder(
            ItemRecyclerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemAdapter.ItemViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    inner class ItemViewHolder(private val binding: ItemRecyclerBinding):
            RecyclerView.ViewHolder(binding.root){
                fun onBind(item: LostFoundItem) {
                    binding.textViewTitle.text = item.title
                    binding.textViewDescription.text = item.description

                    binding.textViewDate.text = java.text.SimpleDateFormat("dd MMM yyyy").format(item.date)
                }
            }
}