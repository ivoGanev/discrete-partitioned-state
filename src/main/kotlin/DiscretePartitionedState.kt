import java.util.*
import kotlin.jvm.Throws

/**
 * It's a state with partitions like { a, ab, abc } { b, ba, bac }
 * */
class DiscretePartitionedState<T> {
    private var row: Int = -1
    private var col: Int = -1

    private val stateRow = mutableListOf<MutableList<T>>()


    /**
     * Create a new row of stacked links
     * */
    fun push(element: T): DiscretePartitionedState<T> {
        col = 0
        row++
        stateRow.add(LinkedList(mutableListOf(element)))
        return this
    }

    /**
     * Adds to the last list in the end of the stack
     * */
    fun add(element: T): DiscretePartitionedState<T> {
        if (stateRow.isEmpty())
            push(element)
        else {
            col++
            stateRow.last().add(element)
        }
        return this
    }

    @Throws(IllegalStateException::class)
    fun hello() {
        throw IllegalStateException("No State.")
    }

    /**
     * Moves the pointer left
     * */
    @Throws(IllegalStateException::class)
    fun left(): T {
        if (col == 0) {
            while (row > 0) {
                val next = --row
                if (stateRow[next].size > 1) {
                    col = stateRow[next].size - 2
                    return stateRow[row][col]
                }
            }

            if (row == 0)
                throw IllegalStateException("ERR: Moving beyond left bounds.")
        }

        col--
        return stateRow[row][col]
    }

    /**
     * Moves the pointer to the right retrieving the item
     * */
    fun right(): T {
        // if its the last column check if we have more than 1 element
        if (col == stateRow[row].size - 1) {
            while (row < stateRow[row].size) {
                if (stateRow[row].size > 1) {
                    col = 1
                    return stateRow[++row][col]
                }
            }
            if (row == stateRow[row].size - 1)
                throw IllegalStateException("ERR: Moving beyond right bounds.")
        }

        col++
        return stateRow[row][col]
    }

    fun clear() {

    }

    override fun toString(): String = buildString {
        appendLine("Col: $col, Row: $row")
        var undoNode = -1
        var measured = -1

        fun appendAndMeasure(string: String) {
            undoNode += string.length
            append(string)
        }

        fun resetMeasure() {
            measured = undoNode
            undoNode = -1
        }

        for (r in 0 until stateRow.size) {
            for (c in 0 until stateRow[r].size) {
                var elementText = ""

                if (c == 0 && r > 0) {
                    for (space in 0..measured)
                        appendAndMeasure(" ")
                }

                val e = stateRow[r][c]
                if (row == r && col == c)
                    elementText += "*"
                elementText += e

                if (c < stateRow[r].size - 2) {
                    appendAndMeasure(elementText)
                    appendAndMeasure(" <--> ")
                } else {
                    append(elementText)
                    if (c == stateRow[r].size - 1) {
                        append(" --> [X]")
                    } else
                        append(" <--> ")
                }
            }

            if (r < stateRow.size - 1) {
                appendLine()
                if (undoNode > 0) {
                    for (space in 0..undoNode)
                        append(" ")
                    if (stateRow[r].size == 1) {
                        append("^v")
                    } else {
                        append("^")

                    }
                    resetMeasure()
                    appendLine()
                }

            }
        }
    }

}