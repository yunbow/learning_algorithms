// Swift
// データ構造: 配列 (Array)

class ArrayData<T: Equatable> {
    private var _data: [T] = []
    
    func get() -> [T] {
        // 要素を取得
        return _data
    }
    
    func getIndex(of item: T) -> Int {
        // 配列内で指定された値を検索し、最初に見つかったインデックスを返す
        if let index = _data.firstIndex(of: item) {
            return index
        } else {
            print("ERROR: \(item) は範囲外です")
            return -1
        }
    }
    
    func getValue(at index: Int) -> T? {
        // 指定されたインデックスの要素を取得する
        if 0 <= index && index < _data.count {
            return _data[index]
        } else {
            print("ERROR: \(index) は範囲外です")
            return nil
        }
    }
    
    func add(_ item: T) -> Bool {
        // 配列の末尾に要素を追加する
        _data.append(item)
        return true
    }
    
    func remove(at index: Int) -> Bool {
        // 指定されたインデックスの要素を削除する
        if 0 <= index && index < _data.count {
            _data.remove(at: index)
            return true
        } else {
            print("ERROR: \(index) は範囲外です")
            return false
        }
    }
    
    func update(at index: Int, newValue: T) -> Bool {
        // 指定されたインデックスの要素を新しい値に更新する
        if 0 <= index && index < _data.count {
            _data[index] = newValue
            return true
        } else {
            print("ERROR: \(index) は範囲外です")
            return false
        }
    }
    
    func reverse() -> [T] {
        // 配列の要素を逆順にする
        _data.reverse()
        return _data
    }
    
    func sort(descending: Bool = false) -> [T] where T: Comparable {
        // 配列の要素をソートする
        if descending {
            _data.sort(by: >)
        } else {
            _data.sort()
        }
        return _data
    }
    
    func isEmpty() -> Bool {
        // 配列が空かどうか
        return _data.isEmpty
    }
    
    func size() -> Int {
        // 配列のサイズ（要素数）を返す
        return _data.count
    }
    
    func clear() -> Bool {
        // 配列の全要素を削除する
        _data.removeAll()
        return true
    }
}

func main() {
    print("Array TEST -----> start")
    
    print("\nnew")
    let arrayData = ArrayData<Int>()
    print("  現在のデータ: \(arrayData.get())")
    
    print("\nadd")
    let input = [10, 20, 30, 10, 40]
    for item in input {
        print("  入力値: \(item)")
        let output = arrayData.add(item)
        print("  出力値: \(output)")
        print("  現在のデータ: \(arrayData.get())")
    }
    
    print("\nsize")
    let sizeOutput = arrayData.size()
    print("  出力値: \(sizeOutput)")
    
    print("\nis_empty")
    let isEmptyOutput = arrayData.isEmpty()
    print("  出力値: \(isEmptyOutput)")
    
    print("\nget_value")
    let getValueInput1 = 2
    print("  入力値: \(getValueInput1)")
    let getValueOutput1 = arrayData.getValue(at: getValueInput1)
    print("  出力値: \(String(describing: getValueOutput1))")
    
    print("\nget_value")
    let getValueInput2 = 10
    print("  入力値: \(getValueInput2)")
    let getValueOutput2 = arrayData.getValue(at: getValueInput2)
    print("  出力値: \(String(describing: getValueOutput2))")
    
    print("\nupdate")
    let updateInput1 = (1, 25)
    print("  入力値: \(updateInput1)")
    let updateOutput1 = arrayData.update(at: updateInput1.0, newValue: updateInput1.1)
    print("  出力値: \(updateOutput1)")
    print("  現在のデータ: \(arrayData.get())")
    
    print("\nupdate")
    let updateInput2 = (15, 25)
    print("  入力値: \(updateInput2)")
    let updateOutput2 = arrayData.update(at: updateInput2.0, newValue: updateInput2.1)
    print("  出力値: \(updateOutput2)")
    print("  現在のデータ: \(arrayData.get())")
    
    print("\nget_index")
    let getIndexInput1 = 10
    print("  入力値: \(getIndexInput1)")
    let getIndexOutput1 = arrayData.getIndex(of: getIndexInput1)
    print("  出力値: \(getIndexOutput1)")
    
    print("\nget_index")
    let getIndexInput2 = 99
    print("  入力値: \(getIndexInput2)")
    let getIndexOutput2 = arrayData.getIndex(of: getIndexInput2)
    print("  出力値: \(getIndexOutput2)")
    
    print("\nremove")
    let removeInput1 = 3
    print("  入力値: \(removeInput1)")
    let removeOutput1 = arrayData.remove(at: removeInput1)
    print("  出力値: \(removeOutput1)")
    print("  現在のデータ: \(arrayData.get())")
    
    print("\nremove")
    let removeInput2 = 8
    print("  入力値: \(removeInput2)")
    let removeOutput2 = arrayData.remove(at: removeInput2)
    print("  出力値: \(removeOutput2)")
    print("  現在のデータ: \(arrayData.get())")
    
    print("\nreverse")
    let reverseOutput = arrayData.reverse()
    print("  出力値: \(reverseOutput)")
    
    print("\nsort")
    print("  入力値: descending=false")
    let sortOutput1 = arrayData.sort(descending: false)
    print("  出力値: \(sortOutput1)")
    
    print("\nsort")
    print("  入力値: descending=true")
    let sortOutput2 = arrayData.sort(descending: true)
    print("  出力値: \(sortOutput2)")
    
    print("\nclear")
    let clearOutput = arrayData.clear()
    print("  出力値: \(clearOutput)")
    print("  現在のデータ: \(arrayData.get())")
    
    print("\nis_empty")
    let isEmptyOutput2 = arrayData.isEmpty()
    print("  出力値: \(isEmptyOutput2)")
    
    print("\nArray TEST <----- end")
}

main()