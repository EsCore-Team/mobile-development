package com.dicoding.escore.view.bottombar.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HistoryViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is history Fragment"
    }
    val text: LiveData<String> = _text

//    val cardColor = when (item.score) {
//        1, 2 -> context.getColor(R.color.red) // Merah
//        3 -> context.getColor(R.color.yellow) // Kuning
//        4, 5 -> context.getColor(R.color.blue) // Biru
//        6 -> context.getColor(R.color.green) // Hijau
//        else -> context.getColor(R.color.gray) // Default (jika nilai tidak sesuai)
//    }
}