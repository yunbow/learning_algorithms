// Kotlin
// データ構造: 配列 (Array)

class ArrayData<T> {
    private var _data = mutableListOf<T>()
    
    fun get(): List<T> {
        // 要素を取得
        return _data
    }

    fun getIndex(item: T): Int {
        // 配列内で指定された値を検索し、最初に見つかったインデックスを返す
        val index = _data.indexOf(item)
        if (index == -1) {
            println("ERROR: $item は範囲外です")
        }
        return index
    }

    fun getValue(index: Int): T? {
        // 指定されたインデックスの要素を取得する
        return if (index in 0 until _data.size) {
            _data[index]
        } else {
            println("ERROR: $index は範囲外です")
            null
        }
    }

    fun add(item: T): Boolean {
        // 配列の末尾に要素を追加する
        _data.add(item)
        return true
    }
    
    fun remove(index: Int): Boolean {
        // 指定されたインデックスの要素を削除する
        return if (index in 0 until _data.size) {
            _data.removeAt(index)
            true
        } else {
            println("ERROR: $index は範囲外です")
            false
        }
    }
    
    fun update(index: Int, newValue: T): Boolean {
        // 指定されたインデックスの要素を新しい値に更新する
        return if (index in 0 until _data.size) {
            _data[index] = newValue
            true
        } else {
            println("ERROR: $index は範囲外です")
            false
        }
    }

    fun reverse(): List<T> {
        // 配列の要素を逆順にする
        _data.reverse()
        return _data
    }
    
    fun sort(descending: Boolean = false): List<T> {
        // 配列の要素をソートする (比較可能な型に対してのみ動作)
        @Suppress("UNCHECKED_CAST")
        when {
            _data.isEmpty() -> return _data
            _data[0] is Comparable<*> -> {
                _data.sortWith(compareBy { it as Comparable<Any> })
                if (descending) {
                    _data.reverse()
                }
            }
        }
        return _data
    }
        
    fun isEmpty(): Boolean {
        // 配列が空かどうか
        return _data.isEmpty()
    }
    
    fun size(): Int {
        // 配列のサイズ（要素数）を返す
        return _data.size
    }
    
    fun clear(): Boolean {
        // 配列の全要素を削除する
        _data.clear()
        return true
    }
}

fun main() {
    println("Array TEST -----> start")

    println("\nnew")
    val arrayData = ArrayData<Int>()
    println("  現在のデータ: ${arrayData.get()}")

    println("\nadd")
    val input = listOf(10, 20, 30, 10, 40)
    for (item in input) {
        println("  入力値: $item")
        val output = arrayData.add(item)
        println("  出力値: $output")
        println("  現在のデータ: ${arrayData.get()}")
    }

    println("\nsize")
    val sizeOutput = arrayData.size()
    println("  出力値: $sizeOutput")

    println("\nis_empty")
    val isEmptyOutput = arrayData.isEmpty()
    println("  出力値: $isEmptyOutput")

    println("\nget_value")
    val getValueInput1 = 2
    println("  入力値: $getValueInput1")
    val getValueOutput1 = arrayData.getValue(getValueInput1)
    println("  出力値: $getValueOutput1")

    println("\nget_value")
    val getValueInput2 = 10
    println("  入力値: $getValueInput2")
    val getValueOutput2 = arrayData.getValue(getValueInput2)
    println("  出力値: $getValueOutput2")

    println("\nupdate")
    val updateInput1 = Pair(1, 25)
    println("  入力値: $updateInput1")
    val updateOutput1 = arrayData.update(updateInput1.first, updateInput1.second)
    println("  出力値: $updateOutput1")
    println("  現在のデータ: ${arrayData.get()}")

    println("\nupdate")
    val updateInput2 = Pair(15, 25)
    println("  入力値: $updateInput2")
    val updateOutput2 = arrayData.update(updateInput2.first, updateInput2.second)
    println("  出力値: $updateOutput2") 
    println("  現在のデータ: ${arrayData.get()}") 

    println("\nget_index")
    val getIndexInput1 = 10
    println("  入力値: $getIndexInput1")
    val getIndexOutput1 = arrayData.getIndex(getIndexInput1)
    println("  出力値: $getIndexOutput1")

    println("\nget_index")
    val getIndexInput2 = 99
    println("  入力値: $getIndexInput2")
    val getIndexOutput2 = arrayData.getIndex(getIndexInput2)
    println("  出力値: $getIndexOutput2")

    println("\nremove")
    val removeInput1 = 3
    println("  入力値: $removeInput1")
    val removeOutput1 = arrayData.remove(removeInput1)
    println("  出力値: $removeOutput1")
    println("  現在のデータ: ${arrayData.get()}")

    println("\nremove")
    val removeInput2 = 8
    println("  入力値: $removeInput2")
    val removeOutput2 = arrayData.remove(removeInput2)
    println("  出力値: $removeOutput2")
    println("  現在のデータ: ${arrayData.get()}")

    println("\nreverse")
    val reverseOutput = arrayData.reverse()
    println("  出力値: $reverseOutput")

    println("\nsort")
    println("  入力値: descending=false")
    val sortOutput1 = arrayData.sort(false)
    println("  出力値: $sortOutput1")

    println("\nsort")
    println("  入力値: descending=true")
    val sortOutput2 = arrayData.sort(true)
    println("  出力値: $sortOutput2")

    println("\nclear")
    val clearOutput = arrayData.clear()
    println("  出力値: $clearOutput")
    println("  現在のデータ: ${arrayData.get()}")

    println("\nis_empty")
    val isEmptyOutput2 = arrayData.isEmpty()
    println("  出力値: $isEmptyOutput2")

    println("\nArray TEST <----- end")
}