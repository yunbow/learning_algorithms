// Kotlin
// データ構造: ヒープ (Heap)

class HeapData(private val isMinHeap: Boolean = true) {
    private val data = mutableListOf<Int>()

    private fun getParentIdx(idx: Int) = (idx - 1) / 2
    private fun getLeftChildIdx(idx: Int) = 2 * idx + 1
    private fun getRightChildIdx(idx: Int) = 2 * idx + 2

    private fun hasParent(idx: Int) = getParentIdx(idx) >= 0
    private fun hasLeftChild(idx: Int) = getLeftChildIdx(idx) < data.size
    private fun hasRightChild(idx: Int) = getRightChildIdx(idx) < data.size

    private fun getParent(idx: Int) = data[getParentIdx(idx)]
    private fun getLeftChild(idx: Int) = data[getLeftChildIdx(idx)]
    private fun getRightChild(idx: Int) = data[getRightChildIdx(idx)]

    private fun swap(idx1: Int, idx2: Int) {
        val temp = data[idx1]
        data[idx1] = data[idx2]
        data[idx2] = temp
    }

    private fun shouldSwap(idx1: Int, idx2: Int): Boolean {
        return if (isMinHeap) {
            data[idx1] > data[idx2]
        } else {
            data[idx1] < data[idx2]
        }
    }

    private fun heapifyDown(idx: Int) {
        var smallestOrLargest = idx

        if (hasLeftChild(idx) && shouldSwap(smallestOrLargest, getLeftChildIdx(idx))) {
            smallestOrLargest = getLeftChildIdx(idx)
        }

        if (hasRightChild(idx) && shouldSwap(smallestOrLargest, getRightChildIdx(idx))) {
            smallestOrLargest = getRightChildIdx(idx)
        }

        if (smallestOrLargest != idx) {
            swap(idx, smallestOrLargest)
            heapifyDown(smallestOrLargest)
        }
    }

    private fun heapifyUp(idx: Int) {
        var currentIdx = idx
        while (hasParent(currentIdx) && shouldSwap(getParentIdx(currentIdx), currentIdx)) {
            val parentIdx = getParentIdx(currentIdx)
            swap(parentIdx, currentIdx)
            currentIdx = parentIdx
        }
    }

    fun get(): List<Int> = data.toList()

    fun getIndex(item: Int): Int {
        val index = data.indexOf(item)
        return if (index != -1) index else {
            println("ERROR: $item は範囲外です")
            -1
        }
    }

    fun getValue(index: Int): Int? {
        return if (index in data.indices) {
            data[index]
        } else {
            println("ERROR: $index は範囲外です")
            null
        }
    }

    fun heapify(array: List<Int>): Boolean {
        data.clear()
        data.addAll(array)
        for (i in data.size / 2 - 1 downTo 0) {
            heapifyDown(i)
        }
        return true
    }

    fun push(value: Int): Boolean {
        data.add(value)
        heapifyUp(data.size - 1)
        return true
    }

    fun pop(): Boolean {
        if (data.isEmpty()) return false

        val lastElement = data.removeAt(data.lastIndex)
        if (data.isNotEmpty()) {
            data[0] = lastElement
            heapifyDown(0)
        }
        return true
    }

    fun peek(): Int? = if (data.isNotEmpty()) data[0] else null

    fun isEmpty(): Boolean = data.isEmpty()

    fun size(): Int = data.size

    fun clear(): Boolean {
        data.clear()
        return true
    }
}

fun main() {
    println("Heap TEST -----> start")

    println("\nmin heap: new")
    val minHeap = HeapData(isMinHeap = true)
    println("  現在のデータ: ${minHeap.get()}")

    println("\nmin heap: heapify")
    val input = listOf(4, 10, 3, 5, 1)
    println("  入力値: $input")
    val output = minHeap.heapify(input)
    println("  出力値: $output")
    println("  現在のデータ: ${minHeap.get()}")

    println("\nmin heap: push")
    val input1 = 2
    println("  入力値: $input1")
    val output1 = minHeap.push(input1)
    println("  出力値: $output1")
    println("  現在のデータ: ${minHeap.get()}")

    println("\nmin heap: push")
    val input2 = 15
    println("  入力値: $input2")
    val output2 = minHeap.push(input2)
    println("  出力値: $output2")
    println("  現在のデータ: ${minHeap.get()}")

    println("\nmin heap: peek")
    val output3 = minHeap.peek()
    println("  出力値: $output3")

    println("\nmin heap: pop")
    val output4 = minHeap.pop()
    println("  出力値: $output4")
    println("  現在のデータ: ${minHeap.get()}")

    println("\nmin heap: pop")
    val output5 = minHeap.pop()
    println("  出力値: $output5")
    println("  現在のデータ: ${minHeap.get()}")

    println("\nmin heap: get_index")
    val input3 = 3
    println("  入力値: $input3")
    val output6 = minHeap.getIndex(input3)
    println("  出力値: $output6")

    println("\nmin heap: get_index")
    val input4 = 100
    println("  入力値: $input4")
    val output7 = minHeap.getIndex(input4)
    println("  出力値: $output7")

    println("\nmin heap: is_empty")
    val output8 = minHeap.isEmpty()
    println("  出力値: $output8")

    println("\nmin heap: size")
    val output9 = minHeap.size()
    println("  出力値: $output9")

    println("\nmin heap: clear")
    val output10 = minHeap.clear()
    println("  出力値: $output10")
    println("  現在のデータ: ${minHeap.get()}")

    println("\nmin heap: is_empty")
    val output11 = minHeap.isEmpty()
    println("  出力値: $output11")

    println("\nmax heap: new")
    val maxHeap = HeapData(isMinHeap = false)
    println("  現在のデータ: ${maxHeap.get()}")

    println("\nmax heap: heapify")
    val input5 = listOf(4, 10, 3, 5, 1)
    println("  入力値: $input5")
    val output12 = maxHeap.heapify(input5)
    println("  出力値: $output12")
    println("  現在のデータ: ${maxHeap.get()}")

    println("\nmax heap: push")
    val input6 = 12
    println("  入力値: $input6")
    val output13 = maxHeap.push(input6)
    println("  出力値: $output13")
    println("  現在のデータ: ${maxHeap.get()}")

    println("\nmax heap: push")
    val input7 = 0
    println("  入力値: $input7")
    val output14 = maxHeap.push(input7)
    println("  出力値: $output14")
    println("  現在のデータ: ${maxHeap.get()}")

    println("\nmax heap: peek")
    val output15 = maxHeap.peek()
    println("  出力値: $output15")

    println("\nmax heap: pop")
    val output16 = maxHeap.pop()
    println("  出力値: $output16")
    println("  現在のデータ: ${maxHeap.get()}")

    println("\nmax heap: pop")
    val output17 = maxHeap.pop()
    println("  出力値: $output17")
    println("  現在のデータ: ${maxHeap.get()}")

    println("\nmax heap: get_index")
    val input8 = 5
    println("  入力値: $input8")
    val output18 = maxHeap.getIndex(input8)
    println("  出力値: $output18")

    println("\nmax heap: get_index")
    val input9 = -10
    println("  入力値: $input9")
    val output19 = maxHeap.getIndex(input9)
    println("  出力値: $output19")

    println("\nmax heap: is_empty")
    val output20 = maxHeap.isEmpty()
    println("  出力値: $output20")

    println("\nmax heap: size")
    val output21 = maxHeap.size()
    println("  出力値: $output21")

    println("\nmax heap: clear")
    val output22 = maxHeap.clear()
    println("  出力値: $output22")
    println("  現在のデータ: ${maxHeap.get()}")

    println("\nmax heap: is_empty")
    val output23 = maxHeap.isEmpty()
    println("  出力値: $output23")

    println("\nHeap TEST <----- end")
}

// Run the main function
fun runMainFunction() {
    main()
}
