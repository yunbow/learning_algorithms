// Kotlin
// データ構造: スタック (Stack)

class StackData {
    private var _data = mutableListOf<Int>()

    fun get(): List<Int> {
        return _data
    }

    fun getIndex(item: Int): Int {
        val index = _data.indexOf(item)
        if (index == -1) {
            println("ERROR: $item は範囲外です")
        }
        return index
    }

    fun getValue(index: Int): Int? {
        return if (0 <= index && index < _data.size) {
            _data[index]
        } else {
            println("ERROR: $index は範囲外です")
            null
        }
    }
    
    fun push(item: Int): Boolean {
        _data.add(item)
        return true
    }
    
    fun pop(): Boolean {
        return if (!isEmpty()) {
            _data.removeAt(_data.size - 1)
            true
        } else {
            println("ERROR: 空です")
            false
        }
    }

    fun peek(): Int? {
        return if (!isEmpty()) {
            _data.last()
        } else {
            null
        }
    }
    
    fun isEmpty(): Boolean {
        return _data.isEmpty()
    }
    
    fun size(): Int {
        return _data.size
    }
    
    fun clear(): Boolean {
        _data.clear()
        return true
    }
}

fun main() {
    println("Stack TEST -----> start")

    println("\nnew")
    val stackData = StackData()
    println("  現在のデータ: ${stackData.get()}")

    println("\nis_empty")
    val isEmpty = stackData.isEmpty()
    println("  出力値: $isEmpty")

    println("\nsize")
    var output = stackData.size()
    println("  出力値: $output")

    println("\npush")
    val itemsToPush = listOf(10, 20, 30, 40)
    for (item in itemsToPush) {
        println("  入力値: $item")
        val pushResult = stackData.push(item)
        println("  出力値: $pushResult")
        println("  現在のデータ: ${stackData.get()}")
    }

    println("\nsize")
    output = stackData.size()
    println("  出力値: $output")

    println("\nis_empty")
    val isEmptyAfterPush = stackData.isEmpty()
    println("  出力値: $isEmptyAfterPush")

    println("\npeek")
    val peekResult = stackData.peek()
    println("  出力値: $peekResult")

    println("\nget_index")
    var input = 30
    println("  入力値: $input")
    val getIndexResult = stackData.getIndex(input)
    println("  出力値: $getIndexResult")

    println("\nget_index")
    input = 50
    println("  入力値: $input")
    val getIndexForMissingItem = stackData.getIndex(input)
    println("  出力値: $getIndexForMissingItem")

    println("\npop")
    while (!stackData.isEmpty()) {
        val popResult = stackData.pop()
        println("  出力値: $popResult")
        println("  現在のデータ: ${stackData.get()}")
    }

    println("\nis_empty")
    val isEmptyAfterPop = stackData.isEmpty()
    println("  出力値: $isEmptyAfterPop")

    println("\nsize")
    val sizeAfterPop = stackData.size()
    println("  出力値: $sizeAfterPop")

    println("\npop")
    val popEmptyResult = stackData.pop()
    println("  出力値: $popEmptyResult")

    println("\npeek")
    val peekEmptyResult = stackData.peek()
    println("  出力値: $peekEmptyResult")

    println("\nStack TEST <----- end")
}