// Kotlin
// データ構造: キュー (Queue)

class QueueData<T> {
    private val _data = mutableListOf<T>()

    fun get(): List<T> = _data.toList()

    fun getIndex(item: T): Int {
        return try {
            _data.indexOf(item)
        } catch (e: Exception) {
            println("ERROR: $item は範囲外です")
            -1
        }
    }

    fun getValue(index: Int): T? {
        return if (index in 0 until _data.size) {
            _data[index]
        } else {
            println("Error: インデックス $index は範囲外です")
            null
        }
    }

    fun enqueue(item: T): Boolean {
        _data.add(item)
        return true
    }

    fun dequeue(): Boolean {
        return if (!isEmpty()) {
            _data.removeAt(0)
            true
        } else {
            println("ERROR: キューが空です")
            false
        }
    }

    fun peek(): T? {
        return if (!isEmpty()) {
            _data.first()
        } else {
            println("ERROR: キューが空です")
            null
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
    println("Queue TEST -----> start")

    println("\nnew")
    val queueData = QueueData<Int>()
    println("  現在のデータ: ${queueData.get()}")

    println("\nis_empty")
    val output1 = queueData.isEmpty()
    println("  出力値: $output1")

    println("\nenqueue")
    val input = listOf(10, 20, 30)
    for (item in input) {
        println("  入力値: $item")
        val output = queueData.enqueue(item)
        println("  出力値: $output")
        println("  現在のデータ: ${queueData.get()}")
    }

    println("\nsize")
    val output2 = queueData.size()
    println("  出力値: $output2")

    println("\npeek")
    val output3 = queueData.peek()
    println("  出力値: $output3")

    println("\nget_index")
    val input1 = 20
    println("  入力値: $input1")
    val output4 = queueData.getIndex(input1)
    println("  出力値: $output4")

    println("\nget_index")
    val input2 = 50
    println("  入力値: $input2")
    val output5 = queueData.getIndex(input2)
    println("  出力値: $output5")

    println("\ndequeue")
    val output6 = queueData.dequeue()
    println("  出力値: $output6")
    println("  現在のデータ: ${queueData.get()}")

    println("\ndequeue")
    val output7 = queueData.dequeue()
    println("  出力値: $output7")
    println("  現在のデータ: ${queueData.get()}")

    println("\nsize")
    val output8 = queueData.size()
    println("  出力値: $output8")

    println("\ndequeue")
    val output9 = queueData.dequeue()
    println("  出力値: $output9")
    println("  現在のデータ: ${queueData.get()}")

    println("\ndequeue")
    val output10 = queueData.dequeue()
    println("  出力値: $output10")
    println("  現在のデータ: ${queueData.get()}")

    println("\nis_empty")
    val output11 = queueData.isEmpty()
    println("  出力値: $output11")

    println("\nclear")
    val output12 = queueData.clear()
    println("  出力値: $output12")
    println("  現在のデータ: ${queueData.get()}")

    println("\nsize")
    val output13 = queueData.size()
    println("  出力値: $output13")

    println("\nQueue TEST <----- end")
}
