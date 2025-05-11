// Kotlin
// 配列の並び替え: クイックソート (Quick Sort)

class ArrayData {
    private var _data: MutableList<Int> = mutableListOf()

    fun get(): List<Int> {
        return _data
    }

    fun set(data: List<Int>): Boolean {
        _data = data.toMutableList()
        return true
    }

    private fun _quickSort(target: List<Int>): List<Int> {
        // 空の配列または要素が1つの場合はそのまま返す（基底条件）
        if (target.size <= 1) {
            return target
        }
        
        // ピボットを選択（この実装では最後の要素を選択）
        val pivot = target.last()
        
        // ピボットより小さい要素と大きい要素に分ける
        val left = mutableListOf<Int>()
        val right = mutableListOf<Int>()
        
        // 最後の要素（ピボット）を除いて配列をスキャン
        for (i in 0 until target.size - 1) {
            if (target[i] <= pivot) {
                left.add(target[i])
            } else {
                right.add(target[i])
            }
        }
        
        // 左側の部分配列、ピボット、右側の部分配列を再帰的にソートして結合
        return _quickSort(left) + listOf(pivot) + _quickSort(right)
    }

    fun sort(): Boolean {
        _data = _quickSort(_data).toMutableList()
        return true
    }
}

fun main() {
    println("QuickSort TEST -----> start")

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

    println("\nQuickSort TEST <----- end")
}