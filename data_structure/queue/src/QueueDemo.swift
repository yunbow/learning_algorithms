// Swift
// データ構造: キュー (Queue)

import Foundation

class QueueData<T> {
    private var data: [T] = []
    
    func get() -> [T] {
        return data
    }
    
    func getIndex(_ item: T) -> Int where T: Equatable {
        guard let index = data.firstIndex(of: item) else {
            print("ERROR: \(item) は範囲外です")
            return -1
        }
        return index
    }
    
    func getValue(at index: Int) -> T? {
        guard index >= 0 && index < data.count else {
            print("Error: インデックス \(index) は範囲外です")
            return nil
        }
        return data[index]
    }
    
    func enqueue(_ item: T) -> Bool {
        data.append(item)
        return true
    }
    
    func dequeue() -> Bool {
        guard !isEmpty() else {
            print("ERROR: キューが空です")
            return false
        }
        data.removeFirst()
        return true
    }
    
    func peek() -> T? {
        guard !isEmpty() else {
            print("ERROR: キューが空です")
            return nil
        }
        return data.first
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
    print("Queue TEST -----> start")
    
    print("\nnew")
    let queueData = QueueData<Int>()
    print("  現在のデータ: \(queueData.get())")
    
    print("\nis_empty")
    let output1 = queueData.isEmpty()
    print("  出力値: \(output1)")
    
    print("\nenqueue")
    let input = [10, 20, 30]
    for item in input {
        print("  入力値: \(item)")
        let output = queueData.enqueue(item)
        print("  出力値: \(output)")
        print("  現在のデータ: \(queueData.get())")
    }
    
    print("\nsize")
    let output2 = queueData.size()
    print("  出力値: \(output2)")
    
    print("\npeek")
    let output3 = queueData.peek()
    print("  出力値: \(output3 ?? "nil")")
    
    print("\nget_index")
    let input1 = 20
    print("  入力値: \(input1)")
    let output4 = queueData.getIndex(input1)
    print("  出力値: \(output4)")
    
    print("\nget_index")
    let input2 = 50
    print("  入力値: \(input2)")
    let output5 = queueData.getIndex(input2)
    print("  出力値: \(output5)")
    
    print("\ndequeue")
    let output6 = queueData.dequeue()
    print("  出力値: \(output6)")
    print("  現在のデータ: \(queueData.get())")
    
    print("\ndequeue")
    let output7 = queueData.dequeue()
    print("  出力値: \(output7)")
    print("  現在のデータ: \(queueData.get())")
    
    print("\nsize")
    let output8 = queueData.size()
    print("  出力値: \(output8)")
    
    print("\ndequeue")
    let output9 = queueData.dequeue()
    print("  出力値: \(output9)")
    print("  現在のデータ: \(queueData.get())")
    
    print("\ndequeue")
    let output10 = queueData.dequeue()
    print("  出力値: \(output10)")
    print("  現在のデータ: \(queueData.get())")
    
    print("\nis_empty")
    let output11 = queueData.isEmpty()
    print("  出力値: \(output11)")
    
    print("\nclear")
    let output12 = queueData.clear()
    print("  出力値: \(output12)")
    print("  現在のデータ: \(queueData.get())")
    
    print("\nsize")
    let output13 = queueData.size()
    print("  出力値: \(output13)")
    
    print("\nQueue TEST <----- end")
}

main()