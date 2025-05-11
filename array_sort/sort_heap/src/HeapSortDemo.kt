// Kotlin
// 配列の並び替え: ヒープソート (Heap Sort)

class HeapData(private var isMinHeap: Boolean = true) {
    private var data = mutableListOf<Int>()

    private fun getParentIdx(idx: Int): Int {
        // 親ノードのインデックスを計算
        if (idx <= 0) {
            return -1 // 根ノードには親がない
        }
        return (idx - 1) / 2
    }

    private fun getLeftChildIdx(idx: Int): Int {
        // 左の子ノードのインデックスを計算
        return 2 * idx + 1
    }

    private fun getRightChildIdx(idx: Int): Int {
        // 右の子ノードのインデックスを計算
        return 2 * idx + 2
    }

    private fun hasParent(idx: Int): Boolean {
        // 親ノードが存在するか確認
        return getParentIdx(idx) >= 0 && getParentIdx(idx) < data.size
    }

    private fun hasLeftChild(idx: Int): Boolean {
        // 左の子ノードが存在するか確認
        return getLeftChildIdx(idx) < data.size
    }

    private fun hasRightChild(idx: Int): Boolean {
        // 右の子ノードが存在するか確認
        return getRightChildIdx(idx) < data.size
    }

    private fun swap(idx1: Int, idx2: Int) {
        // 2つのノードの値を交換
        if (idx1 in 0 until data.size && idx2 in 0 until data.size) {
            val temp = data[idx1]
            data[idx1] = data[idx2]
            data[idx2] = temp
        } else {
            println("Warning: Swap indices out of bounds: $idx1, $idx2")
        }
    }

    private fun shouldSwap(idx1: Int, idx2: Int): Boolean {
        // 最小ヒープでは idx1(親) が idx2(子) より大きい場合に交換
        // 最大ヒープでは idx1(親) が idx2(子) より小さい場合に交換
        return if (isMinHeap) {
            data[idx1] > data[idx2]
        } else { // Max heap
            data[idx1] < data[idx2]
        }
    }

    private fun heapifyDown(idx: Int) {
        // ノードを下方向に移動させてヒープ条件を満たす (isMinHeap に従う)
        var smallestOrLargest = idx

        val leftChildIdx = getLeftChildIdx(idx)
        val rightChildIdx = getRightChildIdx(idx)

        // 左の子ノードと比較 (存在する場合)
        if (hasLeftChild(idx)) {
            if (isMinHeap) { // Min-heap: compare with smallest child
                if (data[leftChildIdx] < data[smallestOrLargest]) {
                    smallestOrLargest = leftChildIdx
                }
            } else { // Max-heap: compare with largest child
                if (data[leftChildIdx] > data[smallestOrLargest]) {
                    smallestOrLargest = leftChildIdx
                }
            }
        }

        // 右の子ノードと比較 (存在する場合)
        if (hasRightChild(idx)) {
            if (isMinHeap) { // Min-heap: compare with smallest child
                if (data[rightChildIdx] < data[smallestOrLargest]) {
                    smallestOrLargest = rightChildIdx
                }
            } else { // Max-heap: compare with largest child
                if (data[rightChildIdx] > data[smallestOrLargest]) {
                    smallestOrLargest = rightChildIdx
                }
            }
        }

        // インデックスが変わっていたら交換して再帰的に処理
        if (smallestOrLargest != idx) {
            swap(idx, smallestOrLargest)
            heapifyDown(smallestOrLargest)
        }
    }

    private fun maxHeapifyDown(idx: Int, heapSize: Int) {
        // 指定されたヒープサイズ内で、idx から下方向に最大ヒープ条件を満たすように調整する。
        var largest = idx // 最大ヒープでは、親と子の間で最大のものを探す

        val leftChildIdx = getLeftChildIdx(idx)
        val rightChildIdx = getRightChildIdx(idx)

        // 左の子ノードと比較 (ヒープサイズ内に存在する場合)
        if (leftChildIdx < heapSize && data[leftChildIdx] > data[largest]) {
            largest = leftChildIdx
        }

        // 右の子ノードと比較 (ヒープサイズ内に存在する場合)
        if (rightChildIdx < heapSize && data[rightChildIdx] > data[largest]) {
            largest = rightChildIdx
        }

        // largest が現在のノード idx と異なる場合、交換して再帰的に処理
        if (largest != idx) {
            swap(idx, largest)
            maxHeapifyDown(largest, heapSize)
        }
    }

    private fun heapifyUp(idx: Int) {
        // ノードを上方向に移動させてヒープ条件を満たす (isMinHeap に従う)
        var currentIdx = idx
        while (hasParent(currentIdx)) {
            val parentIdx = getParentIdx(currentIdx)
            if (shouldSwap(parentIdx, currentIdx)) {
                swap(parentIdx, currentIdx)
                currentIdx = parentIdx
            } else {
                break // Heap property satisfied
            }
        }
    }

    fun get(): List<Int> {
        // 要素を取得 (現在の internal data を返す)
        return data.toList()
    }

    fun heapify(array: List<Int>): Boolean {
        data = array.toMutableList()
        val n = array.size
        // 最後の非葉ノードから根に向かって、各部分木をヒープ化
        for (i in n / 2 - 1 downTo 0) {
            heapifyDown(i) // Use the regular heapifyDown
        }
        return true
    }

    fun sort(): Boolean {
        val n = data.size

        // 配列を最大ヒープに変換する
        for (i in n / 2 - 1 downTo 0) {
            maxHeapifyDown(i, n)
        }

        // 最大ヒープから要素を一つずつ取り出してソート済みの位置に移動
        for (i in n - 1 downTo 1) {
            // 現在の根 (最大値) をヒープの最後の要素と交換
            swap(0, i)

            // 交換された要素 (元の最大値) は正しい位置に置かれたので、
            // 次のヒープ調整からは除外する。ヒープサイズは i に減少。
            // 新しい根 (元のヒープの最後の要素だったもの) から
            // maxHeapifyDown を使って残りの要素を最大ヒープに調整
            maxHeapifyDown(0, i)
        }
        
        return true
    }
}

fun main() {
    println("HeapSort TEST -----> start")

    val heapData = HeapData()

    // ランダムな整数の配列
    println("\nsort")
    val input1 = listOf(64, 34, 25, 12, 22, 11, 90)
    println("  ソート前: $input1")
    heapData.heapify(input1)
    heapData.sort()
    println("  ソート後: ${heapData.get()}")
    
    // 既にソートされている配列
    println("\nsort")
    val input2 = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    println("  ソート前: $input2")
    heapData.heapify(input2)
    heapData.sort()
    println("  ソート後: ${heapData.get()}")
    
    // 逆順の配列
    println("\nsort")
    val input3 = listOf(10, 9, 8, 7, 6, 5, 4, 3, 2, 1)
    println("  ソート前: $input3")
    heapData.heapify(input3)
    heapData.sort()
    println("  ソート後: ${heapData.get()}")
    
    // 重複要素を含む配列
    println("\nsort")
    val input4 = listOf(10, 9, 8, 7, 6, 10, 9, 8, 7, 6)
    println("  ソート前: $input4")
    heapData.heapify(input4)
    heapData.sort()
    println("  ソート後: ${heapData.get()}")
    
    // 空の配列
    println("\nsort")
    val input5 = listOf<Int>()
    println("  ソート前: $input5")
    heapData.heapify(input5)
    heapData.sort()
    println("  ソート後: ${heapData.get()}")

    println("\nHeapSort TEST <----- end")
}