package com.vincentcho.transformer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.vincentcho.transformer.adapter.TransformerDataAdapter
import com.vincentcho.transformer.viewmodel.TransformerViewModel
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.vincentcho.transformer.databinding.FragmentTransformerListBinding
import kotlinx.android.synthetic.main.fragment_transformer_list.*
import com.vincentcho.transformer.api.RestClient
import com.vincentcho.transformer.api.TransformerService
import com.vincentcho.transformer.repo.TransformerRepo
import com.vincentcho.transformer.viewmodel.TransformerViewModelFactory
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

class TransformerListFragment: Fragment(){
    private lateinit var transformerViewModel: TransformerViewModel
    private var transformerDataAdapter: TransformerDataAdapter? = null
    var gson = Gson()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apiService = RestClient("https://transformers-api.firebaseapp.com/").create(TransformerService::class.java)

        val factory = TransformerViewModelFactory(TransformerRepo(apiService, context!!))

        transformerViewModel = activity?.run {
            ViewModelProviders.of(this, factory).get(TransformerViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        transformerDataAdapter = TransformerDataAdapter(transformerViewModel)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val binding = FragmentTransformerListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        transformerList.adapter = transformerDataAdapter
        transformerList.layoutManager = linearLayoutManager

        transformerList.setHasFixedSize(true)

        transformerViewModel!!.getAllTransformers().observe(this, Observer {
            transformerDataAdapter!!.setTransformerList(it)
        })

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            view.findNavController().navigate(R.id.action_transformerListFragment_to_transformerNewFragment)
        }

        fight.setOnClickListener{ view ->
            var msg = transformerViewModel.goToWar()
            val alertDialog = AlertDialog.Builder(activity!!).create()
            alertDialog.setTitle("War!!")
            alertDialog.setMessage(msg)
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
            alertDialog.show()
        }
        transformerViewModel.event.observe(this, Observer { event ->
            when (event) {
                is TransformerViewModel.UserAction.Edit -> {
                    val edit: TransformerViewModel.UserAction.Edit = event
                    var bundle = bundleOf("edit_type" to true ,"transformer" to gson.toJson(edit.transformer))
                    this.findNavController()
                        .navigate(R.id.action_transformerListFragment_to_transformerNewFragment, bundle)
                }
            }
        })
    }
}