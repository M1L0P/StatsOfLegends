package ch.noseryoung.statsoflegends.net

import dagger.Module
import dagger.Provides

@Module
class NetModule {

    @Provides
    fun apiManager() : APIManager {
        return APIManager()
    }

    @Provides
    fun httpManager() : HTTPManager {
        return HTTPManager()
    }
}