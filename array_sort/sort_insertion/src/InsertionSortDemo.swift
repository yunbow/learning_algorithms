// Swift
// 配列の並び替え: 挿入ソート (Insertion Sort)

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
        // 配列の長さを取得
        let n = _data.count
        
        // 2番目の要素から始める（最初の要素は既にソート済みと見なす）
        for i in 1..<n {
            // 現在の要素を取得
            let key = _data[i]
            
            // ソート済み部分の最後の要素のインデックス
            var j = i - 1
            
            // keyより大きい要素をすべて右にシフト
            while j >= 0 && _data[j] > key {
                _data[j + 1] = _data[j]
                j -= 1
            }
            
            // 適切な位置にkeyを挿入
            _data[j + 1] = key
        }
        
        return true
    }
}

func main() {
    print("InsertionSort TEST -----> start")
    
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
    
    print("\nInsertionSort TEST <----- end")
}

main()