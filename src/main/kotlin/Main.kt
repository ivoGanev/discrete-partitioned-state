import java.lang.IllegalArgumentException
import java.lang.IndexOutOfBoundsException
import java.util.*

class Main {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val d = DiscretePartitionedState<String>()
            try {
                d.left()
            }
            catch(ex: IndexOutOfBoundsException) {
                println(ex)
                print(d)
            }
        }
    }
}

