import org.junit.Test

class DiscreteUndoTest {
    @Test
    fun `test adding and pushing`() {
        val path = DiscreteUndo<String>()
        with(path) {
            push("a")
            push("b")
            push("c")
            add("ca.")
            add("cab.")
            add("cabc.")
        }
        println(path.toString())
    }
}