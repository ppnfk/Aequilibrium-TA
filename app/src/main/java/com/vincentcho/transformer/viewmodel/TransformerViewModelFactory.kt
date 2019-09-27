package com.vincentcho.transformer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vincentcho.transformer.api.TransformerService
import com.vincentcho.transformer.repo.TransformerRepo


class TransformerViewModelFactory(private val transformerRepo: TransformerRepo) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransformerViewModel::class.java)) {
            return TransformerViewModel(transformerRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}