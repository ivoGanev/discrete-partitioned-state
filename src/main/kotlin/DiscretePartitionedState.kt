import java.lang.IndexOutOfBoundsException
import java.util.*
import kotlin.jvm.Throws

/**
 * The class`s main idea is to be a data structure which stores states in
 * a chained fashion which can be retrieved by moving left and right.
 *
 * Think of a simple text like "Hello how are you?". Imagine that you decide
 * to replace 'how' with 'what' and the text becomes "Hello what are you?"
 * Later on you decide to change 'you' with 'we' and therefore: "Hello what
 * are we?". In order to keep the original state of the text and the latest
 * version naively you could store the entire change in a stack like this:
 * "Hello how are you?", "Hello what are you?", "Hello what are we?
 *
 * We could do better: by saving only the transitions between the words
 * we can easily move through the versions without wasting memory and cpu power.
 *
 * In the example above we can treat each change as a row of states and
 * move through them to restore the correct one without getting a state
 * which is already restored, see the example bellow:
 *
 * Let's save the word states:
 * ```
 * push(how).add(with)
 * push(you).add(we)
 * // 'we' is now the last state
 * ```
 * Now instead the whole text as a state we have:
 * 1. { how, with }
 * 2. { you, ->we }
 *
 * ```
 * var left = left()
 * left = left()
 * ```
 *
 * By moving once with [left] we'll get: 'you' and moving one more time left we get: 'how'.
 * Why 'how' instead of 'with'? Because we assume that 'with' is already inside the text
 * and therefore we don't need to restore it again.
 *
 *
 * So formally: this class is a bi-directional data structure which is composed of rows
 * of elements. Pushing an element with [push] will create a new row and
 * add the element to it while [add] will add an element to the current row.
 * The current position is being tracked by an inner class pointer.
 * Moving through the elements happens with [left] and [right] which moves
 * the pointer to the next position accordingly.
 * */
class DiscretePartitionedState<T> {
    private var row: Int = -1
    private var col: Int = -1

    private val rows = mutableListOf<MutableList<T>>()

    /**
     * Create a new row and adds the element to it.
     * */
    fun push(element: T): DiscretePartitionedState<T> {
        col = 0
        row++
        rows.add(LinkedList(mutableListOf(element)))
        return this
    }

    /**
     * Adds an element to the last created row.
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

    @Throws(IndexOutOfBoundsException::class)
    fun current() : T {
        if(row < 0 || col < 0)
            throw IndexOutOfBoundsException("ERR: The data structure is empty.")
        return rows[row][col]
    }

    /**
     * Moving left starts from the bottom row retrieving the previous element before
     * the one that was added last.
     *
     * If the row has one element it will search further up for a row with more than one element.
     * If a row is found then the element returned will be not the last but the one before it.
     * This is because we assume that the last element is already a chained state therefore it
     * is unneeded when moving left.
     *
     * @throws IndexOutOfBoundsException when trying to move out of left bounds.
     * */
    @Throws(IndexOutOfBoundsException::class)
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
     * Moving right starts from the top row retrieving the next element after
     * the one that was added last.
     *
     * If the row has one element it will search further down for a row with more than one element.
     * If a row is found then the element returned will be not the last but the second in the row.
     * This is because we assume that the first element is already in a restored state therefore it
     * is unneeded when moving right.
     *
     * @throws IndexOutOfBoundsException when trying to move out of right bounds.
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

    /**
     * Clears the entire structure and resets the positions to 0
     * */
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