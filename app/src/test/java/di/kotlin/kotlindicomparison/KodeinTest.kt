package di.kotlin.kotlindicomparison

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertSame
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotSame
import org.junit.Test
import org.kodein.di.Kodein
import org.kodein.di.bindings.MultiItemScopeRegistry
import org.kodein.di.bindings.subTypes
import org.kodein.di.generic.*
import org.kodein.di.jvmType
import org.kodein.di.weakReference

const val tag1 = "tag1"
const val tag2 = "tag2"

class KodeinTest {

    @Test
    fun `should bind constant`(): Unit = Kodein {

        constant("c") with 1
    }.run {

        val constant by instance<Int>("c")

        assertEquals(1, constant)
    }

    @Test
    fun `should bind with instance`(): Unit = Kodein {

        bind<TestInterface>() with instance(test)
    }.run {

        val instance by instance<TestInterface>()

        assertEquals(test, instance)
        assertSame(instance, instance)
    }

    @Test
    fun `should bind with singleton`(): Unit = Kodein {

        bind<TestInterface>() with singleton { test }
    }.run {

        val instance1 by instance<TestInterface>()
        val instance2 by instance<TestInterface>()

        assertSame(instance1, instance2)
    }

    @Test
    fun `should bind with provider`(): Unit = Kodein {

        bind<TestInterface>() with provider { test }
    }.run {

        val provide by provider<TestInterface>()

        assertEquals(provide(), provide())
        assertNotSame(provide(), provide())
    }

    @Test
    fun `should bind with factory`(): Unit = Kodein {

        bind<TestInterface>() with factory { c: TestInterface -> c }
    }.run {

        val create by factory<TestInterface, TestInterface>()

        assertEquals(test, create(test))
    }


    @Test
    fun `should bind with multiton`(): Unit = Kodein {

        bind<TestInterface>() with multiton { _: Int -> test }
    }.run {

        val create by factory<Int, TestInterface>()

        assertEquals(test, create(0))
        assertEquals(test, create(1))

        assertEquals(create(0), create(0))
        assertEquals(create(0), create(1))

        assertSame(create(0), create(0))
        assertNotSame(create(0), create(1))
    }

    @Test
    fun `should bind same reference`(): Unit = Kodein.direct {
        bind<TestInterface>() with singleton(ref = weakReference) { test }
    }.run {

        val instance = instance<TestInterface>()

        val name1 = instance<TestInterface>()
        val name2 = instance<TestInterface>()

        assertSame(name1, name2)
    }

    @Test
    fun `should bind different reference`(): Unit = Kodein.direct {
        bind<Thread>() with singleton(ref = weakReference) { Thread() }
    }.run {

        val name1 = instance<Thread>().toString()
        System.gc()
        val name2 = instance<Thread>().toString()

        assertNotEquals(name1, name2)
    }

    @Test
    fun `should bind with tag`(): Unit = Kodein.direct {

        bind(tag1) from instance(tag1)
        bind(tag2) from instance(tag2)
    }.run {

        assertEquals(tag1, instance(tag1))
        assertEquals(tag2, instance(tag2))

        assertNotEquals(
            instance<String>(tag1),
            instance<String>(tag2)
        )
    }

    @Test
    fun `should bind subtypes`(): Unit = Kodein.direct {
        bind<TestInterface>().subTypes() with { type ->
            when {
                type.jvmType === TestClass::class.java -> singleton { TestClass() }
                else -> provider { TestClass2() }
            }
        }
    }.run {

        instance<TestClass>()
        instance<TestClass2>()
    }

    @Test
    fun `should retrieval all instances`(): Unit = Kodein.direct {
        bind<TestClass>() with singleton { TestClass() }
        bind<TestClass2>() with singleton { TestClass2() }
    }.run {

        val instances = allInstances<TestInterface>()

        assertEquals(2, instances.size)
    }

    @Test
    fun `should bind with scope`(): Unit = Kodein.direct {

    }.run {

    }
}

val test get() = TestClass()

data class TestClass(val v: String = "") : TestInterface
data class TestClass2(val v2: String = "") : TestInterface

interface TestInterface
