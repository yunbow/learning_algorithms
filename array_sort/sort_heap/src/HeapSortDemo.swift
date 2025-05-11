// Swift
// 配列の並び替え: ヒープソート (Heap Sort)

class HeapData<T: Comparable> {
    private var _data: [T] = []
    // 最小ヒープか最大ヒープかを設定
    var isMinHeap: Bool
    
    init(isMinHeap: Bool = true) {
        self.isMinHeap = isMinHeap
    }
    
    private func _getParentIdx(_ idx: Int) -> Int {
        // 親ノードのインデックスを計算
        if idx <= 0 {
            return -1 // 根ノードには親がない
        }
        return (idx - 1) / 2
    }
    
    private func _getLeftChildIdx(_ idx: Int) -> Int {
        // 左の子ノードのインデックスを計算
        return 2 * idx + 1
    }
    
    private func _getRightChildIdx(_ idx: Int) -> Int {
        // 右の子ノードのインデックスを計算
        return 2 * idx + 2
    }
    
    private func _hasParent(_ idx: Int) -> Bool {
        // 親ノードが存在するか確認
        return _getParentIdx(idx) >= 0 && _getParentIdx(idx) < _data.count
    }
    
    private func _hasLeftChild(_ idx: Int) -> Bool {
        // 左の子ノードが存在するか確認
        return _getLeftChildIdx(idx) < _data.count
    }
    
    private func _hasRightChild(_ idx: Int) -> Bool {
        // 右の子ノードが存在するか確認
        return _getRightChildIdx(idx) < _data.count
    }
    
    private func _swap(_ idx1: Int, _ idx2: Int) {
        // 2つのノードの値を交換
        if 0 <= idx1 && idx1 < _data.count && 0 <= idx2 && idx2 < _data.count {
            _data.swapAt(idx1, idx2)
        } else {
            print("Warning: Swap indices out of bounds: \(idx1), \(idx2)")
        }
    }
    
    private func _shouldSwap(_ idx1: Int, _ idx2: Int) -> Bool {
        // 最小ヒープでは idx1(親) が idx2(子) より大きい場合に交換
        // 最大ヒープでは idx1(親) が idx2(子) より小さい場合に交換
        if isMinHeap {
            return _data[idx1] > _data[idx2]
        } else { // Max heap
            return _data[idx1] < _data[idx2]
        }
    }
    
    private func _heapifyDown(_ idx: Int) {
        // ノードを下方向に移動させてヒープ条件を満たす (isMinHeap に従う)
        var smallestOrLargest = idx
        
        let leftChildIdx = _getLeftChildIdx(idx)
        let rightChildIdx = _getRightChildIdx(idx)
        
        // 左の子ノードと比較 (存在する場合)
        if _hasLeftChild(idx) {
            if isMinHeap { // Min-heap: compare with smallest child
                if _data[leftChildIdx] < _data[smallestOrLargest] {
                    smallestOrLargest = leftChildIdx
                }
            } else { // Max-heap: compare with largest child
                if _data[leftChildIdx] > _data[smallestOrLargest] {
                    smallestOrLargest = leftChildIdx
                }
            }
        }
        
        // 右の子ノードと比較 (存在する場合)
        if _hasRightChild(idx) {
            if isMinHeap { // Min-heap: compare with smallest child
                if _data[rightChildIdx] < _data[smallestOrLargest] {
                    smallestOrLargest = rightChildIdx
                }
            } else { // Max-heap: compare with largest child
                if _data[rightChildIdx] > _data[smallestOrLargest] {
                    smallestOrLargest = rightChildIdx
                }
            }
        }
        
        // インデックスが変わっていたら交換して再帰的に処理
        if smallestOrLargest != idx {
            _swap(idx, smallestOrLargest)
            _heapifyDown(smallestOrLargest)
        }
    }
    
    private func _maxHeapifyDown(_ idx: Int, _ heapSize: Int) {
        // 指定されたヒープサイズ内で、idx から下方向に最大ヒープ条件を満たすように調整する。
        var largest = idx // 最大ヒープでは、親と子の間で最大のものを探す
        
        let leftChildIdx = _getLeftChildIdx(idx)
        let rightChildIdx = _getRightChildIdx(idx)
        
        // 左の子ノードと比較 (ヒープサイズ内に存在する場合)
        if leftChildIdx < heapSize && _data[leftChildIdx] > _data[largest] {
            largest = leftChildIdx
        }
        
        // 右の子ノードと比較 (ヒープサイズ内に存在する場合)
        if rightChildIdx < heapSize && _data[rightChildIdx] > _data[largest] {
            largest = rightChildIdx
        }
        
        // largest が現在のノード idx と異なる場合、交換して再帰的に処理
        if largest != idx {
            _swap(idx, largest)
            _maxHeapifyDown(largest, heapSize)
        }
    }
    
    private func _heapifyUp(_ idx: Int) {
        // ノードを上方向に移動させてヒープ条件を満たす (isMinHeap に従う)
        var currentIdx = idx
        while _hasParent(currentIdx) {
            let parentIdx = _getParentIdx(currentIdx)
            if _shouldSwap(parentIdx, currentIdx) {
                _swap(parentIdx, currentIdx)
                currentIdx = parentIdx
            } else {
                break // Heap property satisfied
            }
        }
    }
    
    func get() -> [T] {
        // 要素を取得 (現在の internal data を返す)
        return _data
    }
    
    @discardableResult
    func heapify(_ array: [T]) -> Bool {
        _data = array
        let n = array.count
        // 最後の非葉ノードから根に向かって、各部分木をヒープ化
        for i in stride(from: n / 2 - 1, through: 0, by: -1) {
            _heapifyDown(i) // Use the regular heapify_down
        }
        return true
    }
    
    @discardableResult
    func sort() -> Bool {
        let n = _data.count
        
        // 配列を最大ヒープに変換する
        for i in stride(from: n / 2 - 1, through: 0, by: -1) {
            _maxHeapifyDown(i, n)
        }
        
        // 最大ヒープから要素を一つずつ取り出してソート済みの位置に移動
        for i in stride(from: n - 1, through: 1, by: -1) {
            // 現在の根 (最大値) をヒープの最後の要素と交換
            _swap(0, i)
            
            // 交換された要素 (元の最大値) は正しい位置に置かれたので、
            // 次のヒープ調整からは除外する。ヒープサイズは i に減少。
            // 新しい根 (元のヒープの最後の要素だったもの) から
            // _maxHeapifyDown を使って残りの要素を最大ヒープに調整
            _maxHeapifyDown(0, i)
        }
        
        return true
    }
}

func main() {
    print("HeapSort TEST -----> start")
    
    let heapData = HeapData<Int>()
    
    // ランダムな整数の配列
    print("\nsort")
    let input1 = [64, 34, 25, 12, 22, 11, 90]
    print("  ソート前: \(input1)")
    heapData.heapify(input1)
    heapData.sort()
    print("  ソート後: \(heapData.get())")
    
    // 既にソートされている配列
    print("\nsort")
    let input2 = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    print("  ソート前: \(input2)")
    heapData.heapify(input2)
    heapData.sort()
    print("  ソート後: \(heapData.get())")
    
    // 逆順の配列
    print("\nsort")
    let input3 = [10, 9, 8, 7, 6, 5, 4, 3, 2, 1]
    print("  ソート前: \(input3)")
    heapData.heapify(input3)
    heapData.sort()
    print("  ソート後: \(heapData.get())")
    
    // 重複要素を含む配列
    print("\nsort")
    let input4 = [10, 9, 8, 7, 6, 10, 9, 8, 7, 6]
    print("  ソート前: \(input4)")
    heapData.heapify(input4)
    heapData.sort()
    print("  ソート後: \(heapData.get())")
    
    // 空の配列
    print("\nsort")
    let input5 = [Int]()
    print("  ソート前: \(input5)")
    heapData.heapify(input5)
    heapData.sort()
    print("  ソート後: \(heapData.get())")
    
    print("\nHeapSort TEST <----- end")
}

main()