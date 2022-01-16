package com.dzenis_ska.kvachmach.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dzenis_ska.kvachmach.LocalModel.LocalModel

class GameViewModelFactory(val localModel: LocalModel) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GameViewModel(localModel) as T
    }
}