// Kotlin
// 配列の並び替え: 選択ソート (Selection Sort)

class ArrayData {
    private var _data: MutableList<Int> = mutableListOf()

    fun get(): List<Int> {
        return _data
    }

    fun set(data: List<Int>): Boolean {
        _data = data.toMutableList()
        return true
    }

    private fun _selectionSort(target: MutableList<Int>): List<Int> {
        // 配列の長さを取得
        val n = target.size
        
        // 配列を順番に走査
        for (i in 0 until n) {
            // 未ソート部分の最小値のインデックスを見つける
            var minIndex = i
            for (j in i + 1 until n) {
                if (target[j] < target[minIndex]) {
                    minIndex = j
                }
            }
            
            // 見つかった最小値と現在の位置を交換
            val temp = target[i]
            target[i] = target[minIndex]
            target[minIndex] = temp
        }
        
        return target
    }

    fun sort(): Boolean {
        _selectionSort(_data)
        return true
    }
}

fun main() {
    println("SelectionSort TEST -----> start")

    val arrayData = ArrayData()

    // ランダムな整数の配列
    println("\nsort")
    val input1 = listOf(64, 34, 25, 12, 22, 11, 90)
    println("  ソート前: $input1")
    arrayData.set(input1)
    arrayData.sort()
    println("  ソート後: ${arrayData.get()}")
    
    // 既にソートされている配列
    println("\nsort")
    val input2 = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    println("  ソート前: $input2")
    arrayData.set(input2)
    arrayData.sort()
    println("  ソート後: ${arrayData.get()}")
    
    // 逆順の配列
    println("\nsort")
    val input3 = listOf(10, 9, 8, 7, 6, 5, 4, 3, 2, 1)
    println("  ソート前: $input3")
    arrayData.set(input3)
    arrayData.sort()
    println("  ソート後: ${arrayData.get()}")
    
    // 重複要素を含む配列
    println("\nsort")
    val input4 = listOf(10, 9, 8, 7, 6, 10, 9, 8, 7, 6)
    println("  ソート前: $input4")
    arrayData.set(input4)
    arrayData.sort()
    println("  ソート後: ${arrayData.get()}")
    
    // 空の配列
    println("\nsort")
    val input5 = listOf<Int>()
    println("  ソート前: $input5")
    arrayData.set(input5)
    arrayData.sort()
    println("  ソート後: ${arrayData.get()}")

    println("\nSelectionSort TEST <----- end")
}