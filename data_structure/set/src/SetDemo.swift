// Swift
// データ構造: セット (Set)

import Foundation

class SetData<T: Hashable> {
    private var data: Set<T>
    
    init() {
        data = []
    }
    
    func get() -> [T] {
        return Array(data)
    }
    
    func getIndex(_ item: T) -> Int? {
        guard let index = Array(data).firstIndex(of: item) else {
            print("ERROR: \(item) は範囲外です")
            return nil
        }
        return index
    }
    
    func getValue(at index: Int) -> T? {
        let dataArray = Array(data)
        guard index >= 0 && index < dataArray.count else {
            print("ERROR: \(index) は範囲外です")
            return nil
        }
        return dataArray[index]
    }
    
    func add(_ item: T) -> Bool {
        if !data.contains(item) {
            data.insert(item)
            return true
        } else {
            print("ERROR: \(item) は重複です")
            return false
        }
    }
    
    func remove(_ item: T) -> Bool {
        if data.contains(item) {
            data.remove(item)
            return true
        } else {
            print("ERROR: \(item) は範囲外です")
            return false
        }
    }
    
    func isEmpty() -> Bool {
        return data.isEmpty
    }
    
    func size() -> Int {
        return data.count
    }
    
    func clear() -> Bool {
        data.removeAll()
        return true
    }
}

func main() {
    print("Set TEST -----> start")
    
    print("\nnew")
    let setData = SetData<Int>()
    print("  現在のデータ: \(setData.get())")
    
    print("\nadd")
    let input = [10, 20, 30, 20, 40]
    for item in input {
        print("  入力値: \(item)")
        let output = setData.add(item)
        print("  出力値: \(output)")
        print("  現在のデータ: \(setData.get())")
    }
    
    print("\nsize")
    let sizeOutput = setData.size()
    print("  出力値: \(sizeOutput)")
    
    print("\nis_empty")
    let isEmptyOutput = setData.isEmpty()
    print("  出力値: \(isEmptyOutput)")
    
    print("\nget_value")
    let getValueInput = [0, 2, 5]
    for index in getValueInput {
        print("  入力値: \(index)")
        let output = setData.getValue(at: index)
        print("  出力値: \(output ?? "nil")")
    }
    
    print("\nget_index")
    let getIndexInput = [30, 99]
    for item in getIndexInput {
        print("  入力値: \(item)")
        let output = setData.getIndex(item)
        print("  出力値: \(output ?? -1)")
    }
    
    print("\nremove")
    let removeInput = [20, 50, 10]
    for item in removeInput {
        print("  入力値: \(item)")
        let output = setData.remove(item)
        print("  出力値: \(output)")
        print("  現在のデータ: \(setData.get())")
    }
    
    print("\nsize")
    let finalSizeOutput = setData.size()
    print("  出力値: \(finalSizeOutput)")
    
    print("\nclear")
    let clearOutput = setData.clear()
    print("  出力値: \(clearOutput)")
    print("  現在のデータ: \(setData.get())")
    
    print("\nis_empty")
    let finalIsEmptyOutput = setData.isEmpty()
    print("  出力値: \(finalIsEmptyOutput)")
    
    print("\nSet TEST <----- end")
}

main()