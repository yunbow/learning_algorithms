// Kotlin
// データ構造: セット (Set)

class SetData {
    private val _data = mutableSetOf<Int>()

    fun get(): Set<Int> = _data.toSet()

    fun getIndex(item: Int): Int {
        val index = _data.indexOf(item)
        return if (index != -1) index else run {
            println("ERROR: $item は範囲外です")
            -1
        }
    }

    fun getValue(index: Int): Int? {
        return if (index in 0 until _data.size) {
            _data.elementAt(index)
        } else {
            println("ERROR: $index は範囲外です")
            null
        }
    }

    fun add(item: Int): Boolean {
        return if (_data.add(item)) {
            true
        } else {
            println("ERROR: $item は重複です")
            false
        }
    }

    fun remove(item: Int): Boolean {
        return if (_data.remove(item)) {
            true
        } else {
            println("ERROR: $item は範囲外です")
            false
        }
    }

    fun isEmpty(): Boolean = _data.isEmpty()

    fun size(): Int = _data.size

    fun clear(): Boolean {
        _data.clear()
        return true
    }
}

fun main() {
    println("Set TEST -----> start")

    println("\nnew")
    val setData = SetData()
    println("  現在のデータ: ${setData.get()}")

    println("\nadd")
    val input = listOf(10, 20, 30, 20, 40)
    input.forEach { item ->
        println("  入力値: $item")
        val output = setData.add(item)
        println("  出力値: $output")
        println("  現在のデータ: ${setData.get()}")
    }

    println("\nsize")
    val output = setData.size()
    println("  出力値: $output")

    println("\nis_empty")
    val emptyOutput = setData.isEmpty()
    println("  出力値: $emptyOutput")

    println("\nget_value")
    val indexInput = listOf(0, 2, 5)
    indexInput.forEach { index ->
        println("  入力値: $index")
        val valueOutput = setData.getValue(index)
        println("  出力値: $valueOutput")
    }

    println("\nget_index")
    val itemInput = listOf(30, 99)
    itemInput.forEach { item ->
        println("  入力値: $item")
        val indexOutput = setData.getIndex(item)
        println("  出力値: $indexOutput")
    }

    println("\nremove")
    val removeInput = listOf(20, 50, 10)
    removeInput.forEach { item ->
        println("  入力値: $item")
        val removeOutput = setData.remove(item)
        println("  出力値: $removeOutput")
        println("  現在のデータ: ${setData.get()}")
    }

    println("\nsize")
    val sizeOutput = setData.size()
    println("  出力値: $sizeOutput")

    println("\nclear")
    val clearOutput = setData.clear()
    println("  出力値: $clearOutput")
    println("  現在のデータ: ${setData.get()}")

    println("\nis_empty")
    val finalEmptyOutput = setData.isEmpty()
    println("  出力値: $finalEmptyOutput")

    println("\nSet TEST <----- end")
}

fun main() {
    main()
}