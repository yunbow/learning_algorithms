// Swift
// 配列の並び替え: バブルソート (Bubble Sort)

class ArrayData {
    private var _data: [Int] = []
    
    func get() -> [Int] {
        return _data
    }
    
    func set(_ data: [Int]) -> Bool {
        _data = data
        return true
    }
    
    func sort() -> Bool {
        let n = _data.count
        
        // 外側のループ: n-1回の走査が必要
        for i in 0..<n {
            // 最適化: 一度の走査で交換がなければソート完了
            var swapped = false
            
            // 内側のループ: まだソートされていない部分を走査
            // 各走査後に最大の要素が末尾に移動するため、i回分を除外
            for j in 0..<(n-i-1) {
                // 隣接する要素を比較し、必要に応じて交換
                if _data[j] > _data[j+1] {
                    _data.swapAt(j, j+1)
                    swapped = true
                }
            }
            
            // 交換が発生しなければソート完了
            if !swapped {
                break
            }
        }
        return true
    }
}

func main() {
    print("BubbleSort TEST -----> start")
    
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
    
    print("\nBubbleSort TEST <----- end")
}

main()