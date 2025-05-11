// Swift
// 配列の並び替え: クイックソート (Quick Sort)

class ArrayData {
    private var _data: [Int] = []
    
    func get() -> [Int] {
        return _data
    }
    
    func set(_ data: [Int]) -> Bool {
        _data = data
        return true
    }
    
    private func _quickSort(_ target: [Int]) -> [Int] {
        // 空の配列または要素が1つの場合はそのまま返す（基底条件）
        if target.count <= 1 {
            return target
        }
        
        // ピボットを選択（この実装では最後の要素を選択）
        let pivot = target.last!
        
        // ピボットより小さい要素と大きい要素に分ける
        var left: [Int] = []
        var right: [Int] = []
        
        // 最後の要素（ピボット）を除いて配列をスキャン
        for i in 0..<target.count - 1 {
            if target[i] <= pivot {
                left.append(target[i])
            } else {
                right.append(target[i])
            }
        }
        
        // 左側の部分配列、ピボット、右側の部分配列を再帰的にソートして結合
        return _quickSort(left) + [pivot] + _quickSort(right)
    }
    
    func sort() -> Bool {
        _data = _quickSort(_data)
        return true
    }
}

func main() {
    print("QuickSort TEST -----> start")
    
    let arrayData = ArrayData()
    
    // ランダムな整数の配列
    print("\nsort")
    let input1 = [64, 34, 25, 12, 22, 11, 90]
    print("  ソート前: \(input1)")
    arrayData.set(input1)
    arrayData.sort()
    print("  ソート後: \(arrayData.get())")
    
    // 既にソートされている配列
    print("\nsort")
    let input2 = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    print("  ソート前: \(input2)")
    arrayData.set(input2)
    arrayData.sort()
    print("  ソート後: \(arrayData.get())")
    
    // 逆順の配列
    print("\nsort")
    let input3 = [10, 9, 8, 7, 6, 5, 4, 3, 2, 1]
    print("  ソート前: \(input3)")
    arrayData.set(input3)
    arrayData.sort()
    print("  ソート後: \(arrayData.get())")
    
    // 重複要素を含む配列
    print("\nsort")
    let input4 = [10, 9, 8, 7, 6, 10, 9, 8, 7, 6]
    print("  ソート前: \(input4)")
    arrayData.set(input4)
    arrayData.sort()
    print("  ソート後: \(arrayData.get())")
    
    // 空の配列
    print("\nsort")
    let input5: [Int] = []
    print("  ソート前: \(input5)")
    arrayData.set(input5)
    arrayData.sort()
    print("  ソート後: \(arrayData.get())")
    
    print("\nQuickSort TEST <----- end")
}

main()
