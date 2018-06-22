package di.kotlin.kotlindicomparison.kodein

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import di.kotlin.kotlindicomparison.R
import di.kotlin.kotlindicomparison.shared.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.*
import org.kodein.di.generic.*
import org.kodein.di.weakReference

const val applicationRepository = "applicationRepository"
const val activityRepository = "activityRepository"
const val applicationState = "applicationState"
const val stringFactory = "stringFactory"
const val stringProvider = "stringProvider"


val applicationKodeinModule = Kodein.Module("repository") {
    bind<Repository>(applicationRepository) with singleton { RepositoryImpl() }
    bind(applicationState) from singleton(ref = weakReference) { State() }
}

val activityKodeinModule = Kodein.Module("viewModel") {

    bind(activityRepository) from scoped(AndroidComponentsWeakScope<Activity>()).singleton { RepositoryImpl() as Repository }

    bind() from scoped(ActivityRetainedScope).singleton { TodoViewModel(instance()) }

    bind() from scoped(AndroidLifecycleScope<Activity>()).singleton { Navigator(context) }
}

val exampleKodeinModel = Kodein.Module("example") {

    bind() from provider { }

    bind() from factory { int: Int -> int.toString() }
}

class KodeinActivity : AppCompatActivity(), KodeinAware {

    private val closestKodein by closestKodein()

    override val kodein by retainedKodein {
        extend(closestKodein)
        import(activityKodeinModule)
    }

    private val model by instance<TodoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}