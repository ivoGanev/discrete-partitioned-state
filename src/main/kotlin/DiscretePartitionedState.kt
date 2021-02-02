import java.lang.IndexOutOfBoundsException
import java.util.*

/**
 * It's a state with partitions like { a, ab, abc } { b, ba, bac }
 * */
class DiscretePartitionedState<T> {
    private var row: Int = -1
    private var col: Int = -1

    private val rows = mutableListOf<MutableList<T>>()

    /**
     * Create a new row of stacked links
     * */
    fun push(element: T): DiscretePartitionedState<T> {
        col = 0
        row++
        rows.add(LinkedList(mutableListOf(element)))
        return this
    }

    /**
     * Adds to the last list in the end of the stack
     * */
    fun add(element: T): DiscretePartitionedState<T> {
        if (rows.isEmpty())
            push(element)
        else {
            col++
            rows.last().add(element)
        }
        return this
    }

    /**
     * Moves the pointer left
     * */
    fun left(): T {
        if (rows.isEmpty())
            throw IndexOutOfBoundsException("ERR: Trying to move through an empty row.")

        var c = col
        var r = row
        c--
        if (c < 0) {
            while (r >= 0) {
                r--
                if (r < 0)
                    throw IndexOutOfBoundsException("ERR: Trying to move out of left bounds.")

                if (rows[r].size > 1) {
                    col = rows[r].size - 2
                    row = r
                    break
                }
            }
        } else {
            col--
        }

        return rows[row][col]
    }

    /**
     * Moves the pointer to the right retrieving the item
     * */
    fun right(): T {
        if (rows.isEmpty())
            throw IndexOutOfBoundsException("ERR: Trying to move through an empty row.")

        var c = col
        var r = row
        c++
        if (c > rows[row].size - 1) {
            while (r <= rows.size - 1) {
                r++
                if (r > rows.size - 1)
                    throw IndexOutOfBoundsException("ERR: Trying to move out of right bounds.")
                if (rows[r].size > 1) {
                    col = 1
                    row = r
                    break
                }
            }
        } else {
            col++
        }


        return rows[row][col]
    }

    fun clear() {
        row = 0
        col = 0
        rows.clear()
    }

    override fun toString(): String = buildString {
        val elements = mutableListOf<MutableList<Pair<Int, Int>>>()
        //TODO: Make a beautiful display for the structure
        var charTrack = 0

        fun printRow(row: Int) {
            charTrack = 0
            if (rows[row].isNotEmpty()) {
                val e = rows[row][0]
                elements.add(
                    mutableListOf(
                        Pair(charTrack, charTrack + e.toString().length)
                    )
                )
                if(this@DiscretePartitionedState.row == row &&
                        this@DiscretePartitionedState.col == 0) {
                    append("*")
                }
                append(e)
                charTrack += e.toString().length
            }

            var i = 1
            while (i <= rows[row].size - 1) {

                val e = rows[row][i++]
                val space = ", "
                append(space + e)
                if(this@DiscretePartitionedState.row == row &&
                    this@DiscretePartitionedState.col == i-1) {
                    append("*")
                }
                elements[row].add(Pair(charTrack + space.length, charTrack + space.length + e.toString().length))
                charTrack += space.length + e.toString().length
            }
            appendLine()
        }
        if (rows.size == 0)
            return "The structure is empty."
        appendLine("*****                    ******")
        appendLine("***** Partitioned States ******")
        appendLine("***** Row: $row; Column: $col; ")
        for (i in 0 until rows.size) {
            append("|*| ")
            printRow(i)
        }
        appendLine("|*| *********************** |*| ")

    }

}