// Kotlin
// 配列の検索: 二分探索 (Binary Search)

class ArrayData {
    private var _data: List<Int> = listOf()

    fun get(): List<Int> {
        return _data
    }

    fun set(data: List<Int>): Boolean {
        _data = data
        return true
    }

    fun search(target: Int): Int {
        var left = 0
        var right = _data.size - 1
        
        while (left <= right) {
            val mid = (left + right) / 2
            
            // 中央の要素が目標値と一致
            if (_data[mid] == target) {
                return mid
            }
            
            // 中央の要素が目標値より小さい場合、右半分を探索
            else if (_data[mid] < target) {
                left = mid + 1
            }
            
            // 中央の要素が目標値より大きい場合、左半分を探索
            else {
                right = mid - 1
            }
        }
        
        // 目標値が見つからない場合
        return -1
    }
}

fun main() {
    println("BinarySearch TEST -----> start")

    println("\nnew")
    val arrayData = ArrayData()
    val input = listOf(1, 3, 5, 7, 9, 11, 13, 15, 17, 19)
    arrayData.set(input)
    println("  現在のデータ: ${arrayData.get()}")
    
    println("\nsearch")
    val searchValue1 = 7
    println("  入力値: $searchValue1")
    val output1 = arrayData.search(searchValue1)
    println("  出力値: $output1")

    println("\nsearch")
    val searchValue2 = 30
    println("  入力値: $searchValue2")
    val output2 = arrayData.search(searchValue2)
    println("  出力値: $output2")

    println("\nBinarySearch TEST <----- end")
}