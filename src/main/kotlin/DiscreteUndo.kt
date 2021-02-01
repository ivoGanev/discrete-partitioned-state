import java.util.*

class DiscreteUndo<T> {
    private var row: Int = -1
    private var col: Int = -1

    private val stackedLinks = mutableListOf<MutableList<T>>()


    /**
     * Create a new row of stacked links
     * */
    fun push(element: T) {
        col = 0
        row++
        stackedLinks.add(LinkedList(mutableListOf(element)))
    }

    /**
     * Adds to the last list in the end of the stack
     * */
    fun add(element: T) {
        if (stackedLinks.isEmpty())
            push(element)
        else {
            col++
            stackedLinks.last().add(element)
        }
    }

    /**
     * Moves the pointer left
     * */
    fun left(): T {
        col--
        return stackedLinks[row][col]
    }

    /**
     * Moves the pointer to the right retrieving the item
     * */
    fun right(): T {
        if (col >= stackedLinks[row].size && stackedLinks.size < row) {
            row++
            col = 0
        } else {
            col++
        }
        return stackedLinks[row][col]
    }

    fun clear() {

    }

    override fun toString(): String = buildString {
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

        for (r in 0 until stackedLinks.size) {
            if (r == 0)
                appendAndMeasure("$r. [X] --> ")
            else
                appendAndMeasure("$r. ")

            for (c in 0 until stackedLinks[r].size) {
                var elementText = ""

                if (c == 0 && r > 0) {
                    val enumeratorSpace = 3
                    for (space in 0..measured-enumeratorSpace)
                        appendAndMeasure(" ")
                }

                val e = stackedLinks[r][c]
                if (row == r && col == c)
                    elementText += "*"
                elementText += e

                if (c < stackedLinks[r].size - 2) {
                    appendAndMeasure(elementText)
                    appendAndMeasure(" <--> ")
                } else {
                    append(elementText)
                    if (c == stackedLinks[r].size - 1) {
                        append(" --> [X]")
                    } else
                        append(" <--> ")
                }
            }

            if (r < stackedLinks.size - 1) {
                appendLine()
                if (undoNode > 0) {
                    for (space in 0..undoNode)
                        append(" ")
                    if (stackedLinks[r].size == 1) {
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