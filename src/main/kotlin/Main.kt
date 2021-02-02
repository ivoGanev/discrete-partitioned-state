import java.lang.IllegalArgumentException
import java.lang.IndexOutOfBoundsException
import java.util.*

class Main {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val d = DiscretePartitionedState<String>()

            d.push("Little").add("Small").add("Miniature")
            d.push("Big").add("Huge").add("Enormous")
            d.left()

            d.left()
            d.left()

             d.left()
             d.left()
            println(d)
        }
    }
}

