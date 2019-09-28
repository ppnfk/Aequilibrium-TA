package com.vincentcho.transformer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vincentcho.transformer.R
import com.vincentcho.transformer.databinding.ItemTransformerBinding
import androidx.databinding.DataBindingUtil.inflate
import com.bumptech.glide.Glide
import com.vincentcho.transformer.viewmodel.TransformerViewModel
import com.vincentcho.transformer.vo.Transformer

class TransformerDataAdapter(private val transformerViewModel: TransformerViewModel) : RecyclerView.Adapter<TransformerDataAdapter.TransformerViewHolder>() {

    var transformers: List<Transformer> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransformerViewHolder {
        var itemTransformerBinding = inflate<ItemTransformerBinding>(LayoutInflater.from(parent.getContext()),
        R.layout.item_transformer, parent, false)
        return TransformerViewHolder(itemTransformerBinding, transformerViewModel)
    }

    override fun onBindViewHolder(transformerViewHolder: TransformerViewHolder, position: Int) {
        val currentTransfomer = transformers.get(position)
        transformerViewHolder.binding.transformer = currentTransfomer
        transformerViewHolder.bind(currentTransfomer)
    }

    override fun getItemCount(): Int {
        if (transformers != null) {
            return transformers.size;
        } else {
            return 0;
        }
    }

    fun setTransformerList(transformers: List<Transformer>) {
        this.transformers = transformers
        notifyDataSetChanged()
    }

    data class TransformerViewHolder(val binding: ItemTransformerBinding,private val transformerViewModel: TransformerViewModel) : RecyclerView.ViewHolder(binding.root){
        fun bind(transformer: Transformer){
            Glide
                .with(binding.root.context)
                .asBitmap()
                .placeholder(R.drawable.placeholder)
                .fitCenter()
                .load(transformer.teamIcon)
                .into(binding.ivPic)
            itemView.setOnClickListener { transformerViewModel.onItemClicked(transformer) }
        }
    }
}