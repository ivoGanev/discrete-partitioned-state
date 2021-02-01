import org.junit.Before
import org.junit.Test
import java.lang.IllegalStateException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlin.test.fail

class DiscretePartitionedStateTest {
    val a = "a"
    val ab = "ab"
    val abc = "abc"
    val b = "b"
    val ba = "ba"
    val bac = "bac"
    val c = "c"
    val ca = "ca"
    val cac = "cac"

    private lateinit var path: DiscretePartitionedState<String>

    @Before
    fun init() {
        path = DiscretePartitionedState()
    }

    @Test
    fun `test full right and left traversal with full graph`() {
        with(path) {
            add(a).add(ab).add(abc)
            push(b).add(ba).add(bac)
            push(c).add(ca).add(cac)

            assertEquals(ca, left())
            assertEquals(c, left())
            assertEquals(ba, left())
            assertEquals(b, left())
            assertEquals(ab, left())
            assertEquals(a, left())

            println(this)
            assertEquals(ab, right())
            println(this)
            assertEquals(abc, right())
            println(this)
            assertEquals(ba, right())
            println(this)
            assertEquals(bac, right())
            println(this)
            assertEquals(ca, right())
            println(this)
            assertEquals(cac, right())
            println(this)

        }
    }

    @Test
    fun `test hello`() {
        assertFailsWith<IllegalStateException> {   path.hello() }

        try {
            path.hello()
            fail()
        } catch (ex: IllegalStateException) {
        }
    }

    @Test
    fun `test left traversal with two single item rows`() {
        with(path) {


            add(a)
            push(b)
            push(c).add(ca).add(cac)

            assertEquals(ca, left())
            println(this)
            assertEquals(c, left())
            println(this)

            assertFailsWith<IllegalStateException> { left() }

            assertEquals(ca, right())
            println(this)
            assertEquals(cac, right())
            println(this)
        }
    }


    @Test
    fun `test left and right traversal with stair-cased elements in rows and columns`() {
        with(path) {
            add(a)
            push(b).add(ba)
            push(c).add(ca).add(cac)

            println(this)
            assertEquals(ca, left())
            println(this)
            assertEquals(c, left())
            println(this)
            assertEquals(b, left())

            assertEquals(ba, right())
            assertEquals(ca, right())
            assertEquals(cac, right())
        }
    }
}
