// Swift
// データ構造: スタック (Stack)

class StackData {
    private var _data: [Int] = []
    
    func get() -> [Int] {
        return _data
    }
    
    func getIndex(item: Int) -> Int {
        if let index = _data.firstIndex(of: item) {
            return index
        } else {
            print("ERROR: \(item) は範囲外です")
            return -1
        }
    }
    
    func getValue(index: Int) -> Int? {
        if 0 <= index && index < _data.count {
            return _data[index]
        } else {
            print("ERROR: \(index) は範囲外です")
            return nil
        }
    }
    
    func push(item: Int) -> Bool {
        _data.append(item)
        return true
    }
    
    func pop() -> Bool {
        if !isEmpty() {
            _data.removeLast()
            return true
        } else {
            print("ERROR: 空です")
            return false
        }
    }
    
    func peek() -> Int? {
        return isEmpty() ? nil : _data.last
    }
    
    func isEmpty() -> Bool {
        return _data.isEmpty
    }
    
    func size() -> Int {
        return _data.count
    }
    
    func clear() -> Bool {
        _data.removeAll()
        return true
    }
}

func main() {
    print("Stack TEST -----> start")
    
    print("\nnew")
    let stackData = StackData()
    print("  現在のデータ: \(stackData.get())")
    
    print("\nis_empty")
    let output1 = stackData.isEmpty()
    print("  出力値: \(output1)")
    
    print("\nsize")
    let output2 = stackData.size()
    print("  出力値: \(output2)")
    
    print("\npush")
    let itemsToPush = [10, 20, 30, 40]
    for item in itemsToPush {
        print("  入力値: \(item)")
        let output = stackData.push(item: item)
        print("  出力値: \(output)")
        print("  現在のデータ: \(stackData.get())")
    }
    
    print("\nsize")
    let output3 = stackData.size()
    print("  出力値: \(output3)")
    
    print("\nis_empty")
    let output4 = stackData.isEmpty()
    print("  出力値: \(output4)")
    
    print("\npeek")
    let output5 = stackData.peek()
    print("  出力値: \(output5 ?? 0)")
    
    print("\nget_index")
    let input1 = 30
    print("  入力値: \(input1)")
    let output6 = stackData.getIndex(item: input1)
    print("  出力値: \(output6)")
    
    print("\nget_index")
    let input2 = 50
    print("  入力値: \(input2)")
    let output7 = stackData.getIndex(item: input2)
    print("  出力値: \(output7)")
    
    print("\npop")
    while !stackData.isEmpty() {
        let output = stackData.pop()
        print("  出力値: \(output)")
        print("  現在のデータ: \(stackData.get())")
    }
    
    print("\nis_empty")
    let output8 = stackData.isEmpty()
    print("  出力値: \(output8)")
    
    print("\nsize")
    let output9 = stackData.size()
    print("  出力値: \(output9)")
    
    print("\npop")
    let output10 = stackData.pop()
    print("  出力値: \(output10)")
    
    print("\npeek")
    let output11 = stackData.peek()
    print("  出力値: \(String(describing: output11))")
    
    print("\nStack TEST <----- end")
}

main()