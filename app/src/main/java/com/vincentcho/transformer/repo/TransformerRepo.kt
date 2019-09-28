package com.vincentcho.transformer.repo

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vincentcho.transformer.api.TransformerService
import com.vincentcho.transformer.vo.Transformer
import com.google.gson.Gson
import com.vincentcho.transformer.api.TransformerResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.gson.reflect.TypeToken
import com.vincentcho.transformer.api.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody

class TransformerRepo(val apiService: TransformerService, val context: Context) {

    companion object {
        private const val PREF_NAME = "transformer_cache"
        private const val KEY_JSON_TRANSFORMER_LIST = "transformer_list"
        private const val KEY_JSON_ALLSPARK = "transformer_allspark"

        private const val TRANSFORMER_CACHE_FILENAME = "transformerRepo.json"
    }
    private val defaultScope = CoroutineScope(Dispatchers.Default)
    private var isInit = false
    private var allspark: String = ""
    private var gson: Gson
    var actionResult = SingleLiveEvent<ActionResult>()

    private lateinit var _transformerList : MutableList<Transformer>

    // mutable LiveData
    private var _transformers: MutableLiveData<List<Transformer>> = MutableLiveData()

    // LiveData to return to viewmodel
    private var transformers: LiveData<List<Transformer>> = _transformers

    fun loadTransformers() : LiveData<List<Transformer>> = transformers

    init {
        gson = Gson()
        loadLocalListFromDb()
        defaultScope.launch {
            getTransformers()
        }
    }

    fun getTransformers() {
        val call = apiService.getTransformers("Bearer $allspark")
        call.enqueue(object : Callback<TransformerResponse> {
            override fun onResponse(call: Call<TransformerResponse>, response: Response<TransformerResponse>) {
                if (response.isSuccessful) {
                    val tr = response.body() as TransformerResponse
                    _transformerList = tr.transformer.toMutableList()
                    _transformers.value = _transformerList
                } else {
                    actionResult.value = ActionResult(false, response.message())
                }
            }
            override fun onFailure(call: Call<TransformerResponse>, t: Throwable) {
                actionResult.value = ActionResult(false, t.message ?: "Delete Fail")
            }
        })
    }

    fun deleteTransformer(transformer: Transformer){
        val call = apiService.deleteTransformer("Bearer $allspark",transformer.id)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    actionResult.value = ActionResult(true, "Delete Successfully")
                    _transformerList.remove(transformer)
                } else {
                    actionResult.value = ActionResult(false, response.message())
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                actionResult.value = ActionResult(false, t.message ?: "Delete Fail")
            }
        })
    }

    fun updateTransformer(transformer: Transformer) {
        val body = transformer.partialJsonForUpdate().toRequestBody(
            "application/json; charset=utf-8".toMediaTypeOrNull()
        )
        val call = apiService.updateTransformers("Bearer $allspark", body)
        call.enqueue(object : Callback<Transformer> {
            override fun onResponse(call: Call<Transformer>, response: Response<Transformer>) {
                if (response.isSuccessful) {
                    var idx = _transformerList.indexOf(transformer)
                    _transformerList.remove(transformer)
                    _transformerList.add(idx, response.body() as Transformer)
                    actionResult.value = ActionResult(true, "Update Successfully")
                } else {
                    actionResult.value = ActionResult(false, response.message())
                }
            }
            override fun onFailure(call: Call<Transformer>, t: Throwable) {
                actionResult.value = ActionResult(false, t.message ?: "Update Fail")
            }
        })
    }

    fun createTransformer(transformer: Transformer){
        val body = transformer.partialJson().toRequestBody(
            "application/json; charset=utf-8".toMediaTypeOrNull()
        )
        val call = apiService.createTransformers("Bearer $allspark", body)
        call.enqueue(object : Callback<Transformer> {
            override fun onResponse(call: Call<Transformer>, response: Response<Transformer>) {
                if (response.isSuccessful) {
                    _transformerList.add(response.body() as Transformer)
                    actionResult.value = ActionResult(true, "Create Successfully")
                } else {
                    actionResult.value = ActionResult(false, response.message())
                }
            }
            override fun onFailure(call: Call<Transformer>, t: Throwable) {
                actionResult.value = ActionResult(false, t.message ?: "Create Fail")
            }
        })
    }

    fun connect(): Boolean {
        if (allspark.length > 0) { return true}

        val call = apiService.allspark()

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    allspark = response.body()!!
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
            }
        })
        return true
    }

    private fun getPreferences(): SharedPreferences {
        return context.getSharedPreferences(TransformerRepo.PREF_NAME, Context.MODE_PRIVATE)
    }

    fun loadLocalListFromDb() {
        allspark = getPreferences().getString(TransformerRepo.KEY_JSON_ALLSPARK,"") ?: ""

        val tlist = getPreferences()
            .getString(TransformerRepo.KEY_JSON_TRANSFORMER_LIST, "") ?: ""
        val tType = object : TypeToken<MutableList<Transformer>>() {}.type
        if (tlist.length > 0) {
            _transformerList = gson.fromJson(tlist, tType)
        } else {
            _transformerList = mutableListOf<Transformer>()
        }
        _transformers.value = _transformerList
    }

    fun saveLocalListToDb() {
        if (_transformerList.count() > 0) {
            var tlist = gson.toJson(_transformerList)
            getPreferences().edit().putString(TransformerRepo.KEY_JSON_TRANSFORMER_LIST, tlist).apply()
        }
        getPreferences().edit().putString(TransformerRepo.KEY_JSON_ALLSPARK,allspark).apply()
    }

    data class ActionResult(val result: Boolean, val message: String)
}