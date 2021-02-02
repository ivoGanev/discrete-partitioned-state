# discrete-partitioned-state

The main motivation is to create a data structure which stores states in
a chained fashion that can be retrieved by moving left and right.  

Think of a simple text like "Hello how are you?". Imagine that you decide
to replace 'how' with 'what', and the text becomes "Hello what are you?"
Later on you decide to change 'you' with 'we' and therefore: "Hello what
are we?". In order to keep the original state of the text, and the latest
version naively you could store the entire change in a stack like this:
"Hello how are you?", "Hello what are you?", "Hello what are we?

We could do better: by saving only the transitions between the words
we can easily move through the versions without wasting memory and cpu power.

In the example above we can treat each change as a row of states and
move through them to restore the correct one without getting a state
which is already restored, see the example bellow:

Let's save the word states:
``` kotlin
push(how).add(with)
push(you).add(we)
// 'we' is now the last state
```
Now instead the whole text as a state we have:
1. { how, with }
2. { you, ->we }

Notice '->' is where the last state was added.

``` kotlin
var left = left()
left = left()
```

By moving once with `left()` we'll get: 'you' and moving one more time left we get: 'how'.

Why 'how' instead of 'with'? Because we assume that 'with' is already inside the text
and therefore we don't need to restore it again.

Here is an example of left and right flow of retrieving elements:

- <-> : will move in both directions
- v : can move only with `right()`
- ^ : can move only with `left()`
    
| |col:0| |col:1|
|---|---|---|---|
|row:0| (3) how | <->| (4) with |
| | ^ | |v|
|row:1| (2) you | <-> | (1) we |

1. Starting from 'we' we can move only `left()`.
2. We arrived at 'you' now we can move `right()` to retrieve 'we' or move `left()` again to retrieve how.
3. We decide to move left, and we get 'how'. Notice now that 'with' on col:1 got replaced meaning if we move right we need to restore it again.
4. We can't go left anymore because it will take us out of bounds, so we can move to the right and restore 'with'
5. From there we can restore 'we' again or go back and restore 'how'


So formally this is a bi-directional data structure which is composed of rows
of elements. Pushing an element with `push()` will create a new row and
add the element to it while `add()` will add an element to the current row.
The current position is being tracked by an inner class pointer.
Moving through the elements happens with `left()` and `right()` which moves
the pointer to the next position accordingly.

