// Kotlin
// データ構造: 連結リスト (Linked List)

class NodeData<T>(
    private var data: T,
    private var next: NodeData<T>? = null
) {
    fun get(): T = data
    fun getNext(): NodeData<T>? = next
    fun setData(newData: T) { data = newData }
    fun setNext(newNext: NodeData<T>?) { next = newNext }
}

class LinkedListData<T> {
    private var data: NodeData<T>? = null
    private var size: Int = 0

    fun get(): NodeData<T>? = data

    fun getPosition(searchData: T): Int {
        if (isEmpty()) return -1

        var current = data
        var position = 0

        while (current != null) {
            if (current.get() == searchData) return position
            current = current.getNext()
            position++
        }

        return -1
    }

    fun getValue(position: Int): T? {
        if (isEmpty() || position < 0 || position >= size) {
            println("ERROR: $position は範囲外です")
            return null
        }

        var current = data
        repeat(position) {
            current = current?.getNext()
        }

        return current?.get()
    }

    fun add(data: T, position: Int? = null): Boolean {
        val newNode = NodeData(data)

        if (isEmpty()) {
            this.data = newNode
            size++
            return true
        }

        if (position == null || position >= size) {
            var current = this.data
            while (current?.getNext() != null) {
                current = current.getNext()
            }
            current?.setNext(newNode)
            size++
            return true
        }

        if (position == 0) {
            newNode.setNext(this.data)
            this.data = newNode
            size++
            return true
        }

        var current = this.data
        repeat(position - 1) {
            current = current?.getNext()
        }

        newNode.setNext(current?.getNext())
        current?.setNext(newNode)
        size++
        return true
    }

    fun remove(position: Int? = null, data: T? = null): Boolean {
        if (isEmpty()) {
            println("ERROR: リストが空です")
            return false
        }

        data?.let {
            if (this.data?.get() == it) {
                this.data = this.data?.getNext()
                size--
                return true
            }

            var current = this.data
            while (current?.getNext() != null && current.getNext()?.get() != it) {
                current = current.getNext()
            }

            if (current?.getNext() != null) {
                current.setNext(current.getNext()?.getNext())
                size--
                return true
            } else {
                println("ERROR: $it は範囲外です")
                return false
            }
        }

        val pos = position ?: (size - 1)

        if (pos < 0 || pos >= size) {
            println("ERROR: $pos は範囲外です")
            return false
        }

        if (pos == 0) {
            data = data?.getNext()
            size--
            return true
        }

        var current = data
        repeat(pos - 1) {
            current = current?.getNext()
        }

        current?.setNext(current?.getNext()?.getNext())
        size--
        return true
    }

    fun update(position: Int, data: T): Boolean {
        if (isEmpty() || position < 0 || position >= size) {
            println("ERROR: $position は範囲外です")
            return false
        }

        var current = data
        repeat(position) {
            current = current?.getNext()
        }

        current?.setData(data)
        return true
    }

    fun isEmpty(): Boolean = data == null

    fun size(): Int = size

    fun clear(): Boolean {
        data = null
        size = 0
        return true
    }

    fun display(): List<T> {
        val elements = mutableListOf<T>()
        var current = data
        while (current != null) {
            elements.add(current.get())
            current = current.getNext()
        }
        return elements
    }
}

fun main() {
    println("LinkedList TEST -----> start")

    println("\nnew")
    val linkedListData = LinkedListData<Int>()
    println("  現在のデータ: ${linkedListData.display()}")

    println("\nis_empty")
    val output1 = linkedListData.isEmpty()
    println("  出力値: $output1")

    println("\nsize")
    val output2 = linkedListData.size()
    println("  出力値: $output2")

    println("\nadd")
    val input1 = 10
    println("  入力値: $input1")
    val output3 = linkedListData.add(input1)
    println("  出力値: $output3")
    println("  現在のデータ: ${linkedListData.display()}")

    println("\nadd")
    val input2 = 20
    println("  入力値: $input2")
    val output4 = linkedListData.add(input2)
    println("  出力値: $output4")
    println("  現在のデータ: ${linkedListData.display()}")

    println("\nadd")
    val input3 = Pair(5, 0)
    println("  入力値: $input3")
    val output5 = linkedListData.add(input3.first, input3.second)
    println("  出力値: $output5")
    println("  現在のデータ: ${linkedListData.display()}")

    println("\nadd")
    val input4 = Pair(15, 2)
    println("  入力値: $input4")
    val output6 = linkedListData.add(input4.first, input4.second)
    println("  出力値: $output6")
    println("  現在のデータ: ${linkedListData.display()}")

    println("\nget_value")
    val input5 = 1
    println("  入力値: $input5")
    val output7 = linkedListData.getPosition(input5)
    println("  出力値: $output7")

    println("\nget_value")
    val input6 = 10
    println("  入力値: $input6")
    val output8 = linkedListData.getPosition(input6)
    println("  出力値: $output8")

    println("\nupdate")
    val input7 = Pair(1, 99)
    println("  入力値: $input7")
    val output9 = linkedListData.update(input7.first, input7.second)
    println("  出力値: $output9")
    println("  現在のデータ: ${linkedListData.display()}")

    println("\nget_value")
    val input8 = 15
    println("  入力値: $input8")
    val output10 = linkedListData.getValue(input8)
    println("  出力値: $output10")

    println("\nget_valuefind")
    val input9 = 100
    println("  入力値: $input9")
    val output11 = linkedListData.getValue(input9)
    println("  出力値: $output11")

    println("\nremove")
    val input10 = 15
    println("  入力値: data=$input10")
    val output12 = linkedListData.remove(data = input10)
    println("  出力値: $output12")
    println("  現在のデータ: ${linkedListData.display()}")

    println("\nremove")
    val input11 = 0
    println("  入力値: position=$input11")
    val output13 = linkedListData.remove(position = input11)
    println("  出力値: $output13")
    println("  現在のデータ: ${linkedListData.display()}")

    println("\nremove")
    val output14 = linkedListData.remove()
    println("  出力値: $output14")
    println("  現在のデータ: ${linkedListData.display()}")

    println("\nremove")
    val input12 = 5
    println("  入力値: position=$input12")
    val output15 = linkedListData.remove(position = input12)
    println("  出力値: $output15")
    println("  現在のデータ: ${linkedListData.display()}")

    println("\nclear")
    val output16 = linkedListData.clear()
    println("  出力値: $output16")
    println("  現在のデータ: ${linkedListData.display()}")

    println("\nis_empty")
    val output17 = linkedListData.isEmpty()
    println("  出力値: $output17")

    println("\nsize")
    val output18 = linkedListData.size()
    println("出力値: $output18")

    println("\nremove")
    val output19 = linkedListData.remove()
    println("  出力値: $output19")
    println("  現在のデータ: ${linkedListData.display()}")

    println("\nLinkedList TEST <----- end")
}

// Kotlinでは、このmain関数を直接実行できます
fun main(args: Array<String>) {
    main()
}