package com.vincentcho.transformer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vincentcho.transformer.repo.TransformerRepo

/**
 * TransformerViewModelFactory
 *
 * viewModelFactory for TransformerViewModel in order to pass a transformerRepo into it.
 *
 * @property transformerRepo a repository stores transformer data which is got from server;
 * @constructor Creates an new TransformerViewModelFactory instance
 */
class TransformerViewModelFactory(private val transformerRepo: TransformerRepo) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransformerViewModel::class.java)) {
            return TransformerViewModel(transformerRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
