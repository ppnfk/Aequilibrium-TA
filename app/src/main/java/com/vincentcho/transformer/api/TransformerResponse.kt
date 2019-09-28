package com.vincentcho.transformer.api

import com.google.gson.annotations.SerializedName
import com.vincentcho.transformer.vo.Transformer

/**
 * TransformerResponse
 *
 * a response classs used by TransformerService(Retrofit)
 *
 * @property transformerlist a list of transformers
 * @constructor Creates an new TransformerResponse instance
 */
data class TransformerResponse(@SerializedName("transformers") val _transformers: List<Transformer>? = null){
    val transformerlist
        get() = _transformers ?: emptyList()
}