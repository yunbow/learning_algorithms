// Kotlin
// 配列の並び替え: 挿入ソート (Insertion Sort)

class ArrayData {
    private var data: MutableList<Int> = mutableListOf()

    fun get(): List<Int> {
        return data
    }

    fun set(newData: List<Int>): Boolean {
        data = newData.toMutableList()
        return true
    }

    fun sort(): Boolean {
        // 配列の長さを取得
        val n = data.size
        
        // 2番目の要素から始める（最初の要素は既にソート済みと見なす）
        for (i in 1 until n) {
            // 現在の要素を取得
            val key = data[i]
            
            // ソート済み部分の最後の要素のインデックス
            var j = i - 1
            
            // keyより大きい要素をすべて右にシフト
            while (j >= 0 && data[j] > key) {
                data[j + 1] = data[j]
                j--
            }
            
            // 適切な位置にkeyを挿入
            data[j + 1] = key
        }
        
        return true
    }
}

fun main() {
    println("InsertionSort TEST -----> start")

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

    println("\nInsertionSort TEST <----- end")
}