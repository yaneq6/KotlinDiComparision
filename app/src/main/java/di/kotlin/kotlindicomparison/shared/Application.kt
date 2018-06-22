package di.kotlin.kotlindicomparison.shared

import android.app.Application
import di.kotlin.kotlindicomparison.kodein.applicationKodeinModule
import di.kotlin.kotlindicomparison.koin.activityKoinModule
import di.kotlin.kotlindicomparison.koin.applicationKoinModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.koin.android.ext.android.startKoin

class Application :
    Application(),
    KodeinAware {

    override val kodein = Kodein {
        import(applicationKodeinModule)
    }

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(
            applicationKoinModule,
            activityKoinModule
        ))

    }
}