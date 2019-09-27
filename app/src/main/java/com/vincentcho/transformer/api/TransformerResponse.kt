package com.vincentcho.transformer.api

import com.google.gson.annotations.SerializedName
import com.vincentcho.transformer.vo.Transformer

data class TransformerResponse(@SerializedName("transformers") val _transformers: List<Transformer>? = null){
    val transformer
        get() = _transformers ?: emptyList()
}