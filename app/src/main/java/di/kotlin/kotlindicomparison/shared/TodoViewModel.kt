package di.kotlin.kotlindicomparison.shared

import android.arch.lifecycle.ViewModel

class TodoViewModel(
    private val repository: Repository
) : ViewModel()