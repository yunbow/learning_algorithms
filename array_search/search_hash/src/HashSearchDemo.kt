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
        // ハッシュテーブルの作成
        val hashTable = mutableMapOf<Int, Int>()
        
        // 配列の要素をハッシュテーブルに格納
        // キーを要素の値、値をインデックスとする
        _data.forEachIndexed { index, value ->
            hashTable[value] = index
        }
        
        // ハッシュテーブルを使って検索
        return hashTable[target] ?: -1
    }
}

fun main() {
    println("HashSearch TEST -----> start")

    println("\nnew")
    val arrayData = ArrayData()
    val input = listOf(1, 3, 5, 7, 9, 11, 13, 15, 17, 19)
    arrayData.set(input)
    println("  現在のデータ: ${arrayData.get()}")    

    println("\nsearch")
    val searchInput1 = 7
    println("  入力値: $searchInput1")
    val output1 = arrayData.search(searchInput1)
    println("  出力値: $output1")

    println("\nsearch")
    val searchInput2 = 30
    println("  入力値: $searchInput2")
    val output2 = arrayData.search(searchInput2)
    println("  出力値: $output2")

    println("\nHashSearch TEST <----- end")
}