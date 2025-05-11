// Kotlin
// 配列の並び替え: マージソート (Merge Sort)

class ArrayData {
    private var data: MutableList<Int> = mutableListOf()

    fun get(): List<Int> {
        return data
    }

    fun set(newData: List<Int>): Boolean {
        data = newData.toMutableList()
        return true
    }
    
    private fun mergeSort(target: List<Int>): List<Int> {
        // 配列の長さが1以下の場合はそのまま返す（基本ケース）
        if (target.size <= 1) {
            return target
        }
        
        // 配列を半分に分割
        val mid = target.size / 2
        val leftHalf = target.subList(0, mid)
        val rightHalf = target.subList(mid, target.size)
        
        // 左右の半分を再帰的にソート
        val sortedLeft = mergeSort(leftHalf)
        val sortedRight = mergeSort(rightHalf)
        
        // ソート済みの半分同士をマージ
        return merge(sortedLeft, sortedRight)
    }

    private fun merge(left: List<Int>, right: List<Int>): List<Int> {
        val result = mutableListOf<Int>()
        var i = 0
        var j = 0
        
        // 左右の配列を比較しながらマージ
        while (i < left.size && j < right.size) {
            if (left[i] <= right[j]) {
                result.add(left[i])
                i++
            } else {
                result.add(right[j])
                j++
            }
        }
        
        // 残った要素を追加
        while (i < left.size) {
            result.add(left[i])
            i++
        }
        
        while (j < right.size) {
            result.add(right[j])
            j++
        }
        
        return result
    }

    fun sort(): Boolean {
        data = mergeSort(data)
        return true
    }
}

fun main() {
    println("MergeSort TEST -----> start")

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

    println("\nMergeSort TEST <----- end")
}