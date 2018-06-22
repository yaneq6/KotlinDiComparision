package di.kotlin.kotlindicomparison.koin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import di.kotlin.kotlindicomparison.R
import di.kotlin.kotlindicomparison.shared.Repository
import di.kotlin.kotlindicomparison.shared.RepositoryImpl
import di.kotlin.kotlindicomparison.shared.State
import di.kotlin.kotlindicomparison.shared.TodoViewModel
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.inject
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext

class KoinActivity : AppCompatActivity() {

    val state by inject<State>()

    val model by viewModel<TodoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}

val applicationKoinModule: Module = applicationContext {
    bean { RepositoryImpl() as Repository }
}

val activityKoinModule: Module = applicationContext {
    bean { State() }
    viewModel { TodoViewModel(get()) }
}

val exampleKoinModule: Module = applicationContext {
    bean {  }
    factory("factory") { }

    get { mapOf() }
}