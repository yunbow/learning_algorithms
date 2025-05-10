// Swift
// 配列の検索: 二分探索 (Binary Search)

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
        var left = 0
        var right = _data.count - 1
        
        while left <= right {
            let mid = (left + right) / 2
            
            // 中央の要素が目標値と一致
            if _data[mid] == target {
                return mid
            }
            
            // 中央の要素が目標値より小さい場合、右半分を探索
            else if _data[mid] < target {
                left = mid + 1
            }
            
            // 中央の要素が目標値より大きい場合、左半分を探索
            else {
                right = mid - 1
            }
        }
        
        // 目標値が見つからない場合
        return -1
    }
}

func main() {
    print("BinarySearch TEST -----> start")
    
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
    
    print("\nBinarySearch TEST <----- end")
}

main()