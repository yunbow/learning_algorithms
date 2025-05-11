// Swift
// 配列の並び替え: マージソート (Merge Sort)

class ArrayData {
    private var _data: [Int] = []
    
    func get() -> [Int] {
        return _data
    }
    
    func set(_ data: [Int]) -> Bool {
        _data = data
        return true
    }
    
    private func _merge_sort(_ target: [Int]) -> [Int] {
        // 配列の長さが1以下の場合はそのまま返す（基本ケース）
        if target.count <= 1 {
            return target
        }
        
        // 配列を半分に分割
        let mid = target.count / 2
        let leftHalf = Array(target[0..<mid])
        let rightHalf = Array(target[mid..<target.count])
        
        // 左右の半分を再帰的にソート
        let sortedLeft = _merge_sort(leftHalf)
        let sortedRight = _merge_sort(rightHalf)
        
        // ソート済みの半分同士をマージ
        return _merge(sortedLeft, sortedRight)
    }
    
    private func _merge(_ left: [Int], _ right: [Int]) -> [Int] {
        var result: [Int] = []
        var i = 0
        var j = 0
        
        // 左右の配列を比較しながらマージ
        while i < left.count && j < right.count {
            if left[i] <= right[j] {
                result.append(left[i])
                i += 1
            } else {
                result.append(right[j])
                j += 1
            }
        }
        
        // 残った要素を追加
        result.append(contentsOf: left[i...])
        result.append(contentsOf: right[j...])
        
        return result
    }
    
    func sort() -> Bool {
        _data = _merge_sort(_data)
        return true
    }
}

func main() {
    print("MergeSort TEST -----> start")

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

    print("\nMergeSort TEST <----- end")
}

main()