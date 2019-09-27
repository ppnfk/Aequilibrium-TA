package com.vincentcho.transformer

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.ramotion.fluidslider.FluidSlider
import com.vincentcho.transformer.adapter.TransformerDataAdapter
import com.vincentcho.transformer.adapter.TransformerEditViewAdapter
import com.vincentcho.transformer.api.RestClient
import com.vincentcho.transformer.api.TransformerResponse
import com.vincentcho.transformer.api.TransformerService
import com.vincentcho.transformer.databinding.FragmentTransformerEditBinding
import com.vincentcho.transformer.databinding.FragmentTransformerListBinding
import com.vincentcho.transformer.repo.TransformerRepo
import com.vincentcho.transformer.viewmodel.TransformerViewModel
import com.vincentcho.transformer.viewmodel.TransformerViewModelFactory
import com.vincentcho.transformer.vo.Transformer
import kotlinx.android.synthetic.main.fragment_transformer_edit.*
import kotlinx.android.synthetic.main.fragment_transformer_list.*

class TransformerNewFragment: Fragment() , RadioGroup.OnCheckedChangeListener {

    private lateinit var transformerEditViewAdapter: TransformerEditViewAdapter
    private lateinit var transformerViewModel: TransformerViewModel
    private lateinit var newTransformer: Transformer
    private var editType: Boolean = false
    var gson = Gson()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apiService = RestClient("https://transformers-api.firebaseapp.com/").create(TransformerService::class.java)
        val factory = TransformerViewModelFactory(TransformerRepo(apiService, activity!!))

        transformerViewModel = activity?.run {
            ViewModelProviders.of(this, factory).get(TransformerViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        arguments?.getBoolean("edit_type")?.let {
            editType = it
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val binding = FragmentTransformerEditBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var transformerJson = arguments?.getString("transformer")


        if (transformerJson.isNullOrEmpty())
        {
            transformerViewModel?.run {
                newTransformer = newTransformer()
            }
        } else {
            newTransformer = gson.fromJson(transformerJson, Transformer::class.java)
            editName.setText(newTransformer.name)
            when (newTransformer.team) {
                "A" -> {radio_autobot.isChecked = true}
                "D" -> {radio_deception.isChecked = true}
            }
        }

        var linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        transformerEditViewAdapter = TransformerEditViewAdapter(context!!, transformerViewModel)

        transformer_attribute_list.apply {
            adapter = transformerEditViewAdapter
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
        }

        transformerEditViewAdapter.setTransformer(newTransformer)
        radioGroup.apply {
            setOnCheckedChangeListener(this@TransformerNewFragment)
        }
        if (editType == true) {
            button_delete.visibility = View.VISIBLE
            button_delete.setOnClickListener {
                    transformerViewModel.deleteTransformer(newTransformer)
            }

        } else {
            button_delete.visibility = View.GONE
        }

        button_submit.setOnClickListener {
            if (editType==true) {
                transformerViewModel.updateTransformer(newTransformer)
            }
            else {
                transformerViewModel.createTransformer()
            }
        }

        editName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                newTransformer.name = "$s"
            }
        })

        transformerViewModel.actionResultObservable.observe(this, Observer { event ->
            val actionResult = event
            if (actionResult.result == false) {
                Snackbar.make(view, actionResult.message, Snackbar.LENGTH_LONG).setAction("Action", null).show()
            } else {
                activity!!.onBackPressed()
            }
        })
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        if (radio_autobot.isChecked) {
            newTransformer.team = "A"
        } else if (radio_deception.isChecked) {
            newTransformer.team = "D"
        }
    }
}