// Swift
// 配列の検索: ハッシュ探索 (Hash Search)

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
        // ハッシュテーブルの作成
        var hashTable: [Int: Int] = [:]
        
        // 配列の要素をハッシュテーブルに格納
        // キーを要素の値、値をインデックスとする
        for (index, value) in _data.enumerated() {
            hashTable[value] = index
        }
        
        // ハッシュテーブルを使って検索
        return hashTable[target] ?? -1
    }
}

func main() {
    print("HashSearch TEST -----> start")
    
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
    
    print("\nHashSearch TEST <----- end")
}

main()