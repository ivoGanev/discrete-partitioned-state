import java.lang.IndexOutOfBoundsException
import java.util.*
import kotlin.jvm.Throws

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
        if(rows.isEmpty())
            throw IndexOutOfBoundsException("ERR: Trying to move through an empty row.")

        var c = col
        var r = row
        c--
        if (c < 0) {
            while (r > 0) {
                r--
                if(r==0)
                    throw IndexOutOfBoundsException("ERR: Trying to move out of left bounds.")

                if (rows[r].size > 1) {
                    col = rows[r].size - 2
                    row = r
                    break
                }
            }
        }
        else { col-- }

        return rows[row][col]
    }

    /**
     * Moves the pointer to the right retrieving the item
     * */
    fun right(): T {
        if(rows.isEmpty())
            throw IndexOutOfBoundsException("ERR: Trying to move through an empty row.")

        var c = col
        var r = row
        c++
        if(c>rows[row].size-1){
            while(r<rows.size-1){
                r++
                if(r==rows.size-1)
                    throw IndexOutOfBoundsException("ERR: Trying to move out of right bounds.")
                if(rows[r].size > 1) {
                    col = 1
                    row = r
                    break
                }
            }
        }else { col ++ }


        return rows[row][col]
    }

    fun clear() {
        row = 0
        col = 0
        rows.clear()
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

        for (r in 0 until rows.size) {
            for (c in 0 until rows[r].size) {
                var elementText = ""

                if (c == 0 && r > 0) {
                    for (space in 0..measured)
                        appendAndMeasure(" ")
                }

                val e = rows[r][c]
                if (row == r && col == c)
                    elementText += "*"
                elementText += e

                if (c < rows[r].size - 2) {
                    appendAndMeasure(elementText)
                    appendAndMeasure(" <--> ")
                } else {
                    append(elementText)
                    if (c == rows[r].size - 1) {
                        append(" --> [X]")
                    } else
                        append(" <--> ")
                }
            }

            if (r < rows.size - 1) {
                appendLine()
                if (undoNode > 0) {
                    for (space in 0..undoNode)
                        append(" ")
                    if (rows[r].size == 1) {
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