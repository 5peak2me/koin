package org.koin.test.android

import android.arch.lifecycle.Lifecycle
import org.junit.Assert
import org.junit.Assert.fail
import org.junit.Test
import org.koin.android.scope.ScopeObserver
import org.koin.core.error.ScopeNotCreatedException
import org.koin.core.logger.Level
import org.koin.core.standalone.startKoin
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get

class AndroidObserverTest : AutoCloseKoinTest() {

    class MyService

    @Test
    fun `should close scoped definition on ON_DESTROY`() {
        startKoin {
            logger(Level.DEBUG)
            modules(org.koin.dsl.module {
                scope("session") { MyService() }
            })
        }

        val session = getKoin().createScope("session")
        val service = get<MyService>()
        Assert.assertNotNull(service)

        val observer = ScopeObserver(Lifecycle.Event.ON_DESTROY, "testClass", session)
        observer.onDestroy()

        try {
            get<MyService>()
            fail("no resolution of closed scope dependency")
        } catch (e: ScopeNotCreatedException) {
            e.printStackTrace()
        }
    }

    @Test
    fun `should not close scoped definition`() {
        startKoin {
            logger(Level.DEBUG)
            modules(org.koin.dsl.module {
                scope("session") { MyService() }
            })
        }

        val session = getKoin().createScope("session")
        val service = get<MyService>()
        Assert.assertNotNull(service)

        val observer = ScopeObserver(Lifecycle.Event.ON_DESTROY, "testClass", session)
        observer.onStop()

        get<MyService>()
    }

    @Test
    fun `should close scoped definition on ON_STOP`() {
        startKoin {
            logger(Level.DEBUG)
            modules(org.koin.dsl.module {
                scope("session") { MyService() }
            })
        }

        val session = getKoin().createScope("session")
        val service = get<MyService>()
        Assert.assertNotNull(service)

        val observer = ScopeObserver(Lifecycle.Event.ON_STOP, "testClass", session)
        observer.onStop()

        try {
            get<MyService>()
            fail("no resolution of closed scope dependency")
        } catch (e: ScopeNotCreatedException) {
            e.printStackTrace()
        }
    }
}