package com.vincentcho.transformer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.RecyclerView
import com.vincentcho.transformer.R
import com.vincentcho.transformer.viewmodel.TransformerViewModel
import com.vincentcho.transformer.vo.Transformer
import kotlinx.android.synthetic.main.item_attribute.view.*

/**
 * TransformerEditViewAdapter
 *
 * This class handles the recycler view of the transformer attributes seekbar of edit/new transformer screen
 *
 * @property context for inflatering view only
 * @property tViewModel this adapter keeps a viewmodel passing from the fragment as a data model to use
 * @constructor Creates an new adapter to handle RecyclerView of transformer's attributes
 */
class TransformerEditViewAdapter(val context: Context, private val tViewModel: TransformerViewModel) : RecyclerView.Adapter<TransformerEditViewAdapter.ViewHolder>() {

    lateinit var _transformer: Transformer

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_attribute, parent, false))
    }

    override fun onBindViewHolder(transformerViewHolder: ViewHolder, position: Int) {
        transformerViewHolder.bind(_transformer, position)
    }

    override fun getItemCount(): Int {
        if (_transformer != null) {
            return Transformer.ATTRIBUTE_SIZE;
        } else {
            return 0;
        }
    }

    fun setTransformer(transformer: Transformer) {
        this._transformer = transformer
        notifyDataSetChanged()
    }

    data class ViewHolder(val v: View) : RecyclerView.ViewHolder(v){
        var attrName = ""
        fun bind(transformer: Transformer, position: Int) {

            attrName = transformer.getAttrName(position)
            v.attribute_name.text = attrName + ": " + transformer.getAttr(position).toString()
            val max = 10
            val min = 1
            val total = max - min
            v.fluidslider.progress = ((transformer.getAttr(position)-1).toFloat()/total * 100).toInt()
            v.fluidslider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                internal var progressChangedValue = 0

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    progressChangedValue = progress
                    var newValue = Math.round((min + (total  * (progress.toDouble()/100)))).toInt()
                    v.attribute_name.text = attrName + " : "+ newValue.toString()
                    transformer.setAttr(attrName, newValue)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                }
            })
        }
    }
}