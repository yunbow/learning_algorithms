// Swift
// 配列の並び替え: 選択ソート (Selection Sort)

class ArrayData {
    private var _data: [Int] = []
    
    func get() -> [Int] {
        return _data
    }
    
    func set(_ data: [Int]) -> Bool {
        _data = data
        return true
    }
    
    private func _selection_sort(_ target: [Int]) -> [Int] {
        // 配列の長さを取得
        let n = target.count
        var result = target
        
        // 配列を順番に走査
        for i in 0..<n {
            // 未ソート部分の最小値のインデックスを見つける
            var minIndex = i
            for j in (i + 1)..<n {
                if result[j] < result[minIndex] {
                    minIndex = j
                }
            }
            
            // 見つかった最小値と現在の位置を交換
            if i != minIndex {
                result.swapAt(i, minIndex)
            }
        }
        
        return result
    }
    
    func sort() -> Bool {
        _data = _selection_sort(_data)
        return true
    }
}

func main() {
    print("SelectionSort TEST -----> start")
    
    let arrayData = ArrayData()
    
    // ランダムな整数の配列
    print("\nsort")
    let input1 = [64, 34, 25, 12, 22, 11, 90]
    print("  ソート前: \(input1)")
    _ = arrayData.set(input1)
    _ = arrayData.sort()
    print("  ソート後: \(arrayData.get())")
    
    // 既にソートされている配列
    print("\nsort")
    let input2 = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    print("  ソート前: \(input2)")
    _ = arrayData.set(input2)
    _ = arrayData.sort()
    print("  ソート後: \(arrayData.get())")
    
    // 逆順の配列
    print("\nsort")
    let input3 = [10, 9, 8, 7, 6, 5, 4, 3, 2, 1]
    print("  ソート前: \(input3)")
    _ = arrayData.set(input3)
    _ = arrayData.sort()
    print("  ソート後: \(arrayData.get())")
    
    // 重複要素を含む配列
    print("\nsort")
    let input4 = [10, 9, 8, 7, 6, 10, 9, 8, 7, 6]
    print("  ソート前: \(input4)")
    _ = arrayData.set(input4)
    _ = arrayData.sort()
    print("  ソート後: \(arrayData.get())")
    
    // 空の配列
    print("\nsort")
    let input5: [Int] = []
    print("  ソート前: \(input5)")
    _ = arrayData.set(input5)
    _ = arrayData.sort()
    print("  ソート後: \(arrayData.get())")
    
    print("\nSelectionSort TEST <----- end")
}

main()