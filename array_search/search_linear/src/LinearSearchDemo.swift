// Swift
// 配列の検索: 線形探索 (Linear Search)

class ArrayData {
    private var _data: [Int] = []
    
    func get() -> [Int] {
        return _data
    }
    
    func set(_ data: [Int]) -> Bool {
        _data = data
        return true
    }
    
    func search(_ target: Int) -> Int {
        // 配列の要素を順番に確認
        for i in 0..<_data.count {
            // 目的の値が見つかった場合、そのインデックスを返す
            if _data[i] == target {
                return i
            }
        }
        
        // 見つからなかった場合は -1 を返す
        return -1
    }
}

func main() {
    print("LinearSearch TEST -----> start")
    
    print("\nnew")
    let arrayData = ArrayData()
    let input = [1, 3, 5, 7, 9, 11, 13, 15, 17, 19]
    _ = arrayData.set(input)
    print("  現在のデータ: \(arrayData.get())")
    
    print("\nsearch")
    let searchInput1 = 7
    print("  入力値: \(searchInput1)")
    let output1 = arrayData.search(searchInput1)
    print("  出力値: \(output1)")
    
    print("\nsearch")
    let searchInput2 = 30
    print("  入力値: \(searchInput2)")
    let output2 = arrayData.search(searchInput2)
    print("  出力値: \(output2)")
    
    print("\nLinearSearch TEST <----- end")
}

main()