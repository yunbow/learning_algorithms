// Swift
// データ構造: 連結リスト (Linked List)

import Foundation

class NodeData<T> {
    var data: T
    var next: NodeData<T>?
    
    init(data: T) {
        self.data = data
    }
}

class LinkedListData<T: Equatable> {
    private var head: NodeData<T>?
    private(set) var size: Int = 0
    
    func isEmpty() -> Bool {
        return head == nil
    }
    
    func getPosition(data: T) -> Int? {
        guard !isEmpty() else { return nil }
        
        var current = head
        var position = 0
        
        while let node = current {
            if node.data == data {
                return position
            }
            current = node.next
            position += 1
        }
        
        return nil
    }
    
    func getValue(position: Int) -> T? {
        guard !isEmpty(), position >= 0, position < size else {
            print("ERROR: \(position) is out of range")
            return nil
        }
        
        var current = head
        for _ in 0..<position {
            current = current?.next
        }
        
        return current?.data
    }
    
    @discardableResult
    func add(_ data: T, at position: Int? = nil) -> Bool {
        let newNode = NodeData(data: data)
        
        if isEmpty() {
            head = newNode
            size += 1
            return true
        }
        
        guard let position = position else {
            // Add to end
            var current = head
            while current?.next != nil {
                current = current?.next
            }
            current?.next = newNode
            size += 1
            return true
        }
        
        if position == 0 {
            newNode.next = head
            head = newNode
            size += 1
            return true
        }
        
        guard position > 0, position <= size else {
            print("ERROR: \(position) is out of range")
            return false
        }
        
        var current = head
        for _ in 0..<(position - 1) {
            current = current?.next
        }
        
        newNode.next = current?.next
        current?.next = newNode
        size += 1
        return true
    }
    
    @discardableResult
    func remove(at position: Int? = nil, data: T? = nil) -> Bool {
        guard !isEmpty() else {
            print("ERROR: List is empty")
            return false
        }
        
        if let data = data {
            if head?.data == data {
                head = head?.next
                size -= 1
                return true
            }
            
            var current = head
            while let next = current?.next, next.data != data {
                current = next
            }
            
            if current?.next != nil {
                current?.next = current?.next?.next
                size -= 1
                return true
            } else {
                print("ERROR: \(data) not found")
                return false
            }
        }
        
        let targetPosition = position ?? (size - 1)
        
        guard targetPosition >= 0, targetPosition < size else {
            print("ERROR: \(targetPosition) is out of range")
            return false
        }
        
        if targetPosition == 0 {
            head = head?.next
            size -= 1
            return true
        }
        
        var current = head
        for _ in 0..<(targetPosition - 1) {
            current = current?.next
        }
        
        current?.next = current?.next?.next
        size -= 1
        return true
    }
    
    @discardableResult
    func update(at position: Int, with data: T) -> Bool {
        guard !isEmpty(), position >= 0, position < size else {
            print("ERROR: \(position) is out of range")
            return false
        }
        
        var current = head
        for _ in 0..<position {
            current = current?.next
        }
        
        current?.data = data
        return true
    }
    
    func clear() {
        head = nil
        size = 0
    }
    
    func display() -> [T] {
        var elements: [T] = []
        var current = head
        
        while let node = current {
            elements.append(node.data)
            current = node.next
        }
        
        return elements
    }
}

func main() {
    print("LinkedList TEST -----> start")
    
    print("\nnew")
    let linkedListData = LinkedListData<Int>()
    print("  現在のデータ: \(linkedListData.display())")
    
    print("\nis_empty")
    let outputEmpty = linkedListData.isEmpty()
    print("  出力値: \(outputEmpty)")
    
    print("\nsize")
    let outputSize = linkedListData.size
    print("  出力値: \(outputSize)")
    
    print("\nadd")
    let input1 = 10
    print("  入力値: \(input1)")
    let output1 = linkedListData.add(input1)
    print("  出力値: \(output1)")
    print("  現在のデータ: \(linkedListData.display())")
    
    print("\nadd")
    let input2 = 20
    print("  入力値: \(input2)")
    let output2 = linkedListData.add(input2)
    print("  出力値: \(output2)")
    print("  現在のデータ: \(linkedListData.display())")
    
    print("\nadd")
    print("  入力値: (5, 0)")
    let output3 = linkedListData.add(5, at: 0)
    print("  出力値: \(output3)")
    print("  現在のデータ: \(linkedListData.display())")
    
    print("\nadd")
    print("  入力値: (15, 2)")
    let output4 = linkedListData.add(15, at: 2)
    print("  出力値: \(output4)")
    print("  現在のデータ: \(linkedListData.display())")
    
    print("\nget_position")
    let input5 = 1
    print("  入力値: \(input5)")
    let output5 = linkedListData.getPosition(data: input5)
    print("  出力値: \(output5 ?? -1)")
    
    print("\nget_position")
    let input6 = 10
    print("  入力値: \(input6)")
    let output6 = linkedListData.getPosition(data: input6)
    print("  出力値: \(output6 ?? -1)")
    
    print("\nupdate")
    print("  入力値: (1, 99)")
    let output7 = linkedListData.update(at: 1, with: 99)
    print("  出力値: \(output7)")
    print("  現在のデータ: \(linkedListData.display())")
    
    print("\nget_value")
    let input8 = 2
    print("  入力値: \(input8)")
    let output8 = linkedListData.getValue(position: input8)
    print("  出力値: \(output8 ?? -1)")
    
    print("\nget_value")
    let input9 = 100
    print("  入力値: \(input9)")
    let output9 = linkedListData.getValue(position: input9)
    print("  出力値: \(output9 ?? -1)")
    
    print("\nremove")
    let input10 = 15
    print("  入力値: data=\(input10)")
    let output10 = linkedListData.remove(data: input10)
    print("  出力値: \(output10)")
    print("  現在のデータ: \(linkedListData.display())")
    
    print("\nremove")
    print("  入力値: position=0")
    let output11 = linkedListData.remove(at: 0)
    print("  出力値: \(output11)")
    print("  現在のデータ: \(linkedListData.display())")
    
    print("\nremove")
    let output12 = linkedListData.remove()
    print("  出力値: \(output12)")
    print("  現在のデータ: \(linkedListData.display())")
    
    print("\nremove")
    let input13 = 5
    print("  入力値: position=\(input13)")
    let output13 = linkedListData.remove(at: input13)
    print("  出力値: \(output13)")
    print("  現在のデータ: \(linkedListData.display())")
    
    print("\nclear")
    linkedListData.clear()
    print("  出力値: true")
    print("  現在のデータ: \(linkedListData.display())")
    
    print("\nis_empty")
    let output14 = linkedListData.isEmpty()
    print("  出力値: \(output14)")
    
    print("\nsize")
    let output15 = linkedListData.size
    print("出力値: \(output15)")
    
    print("\nremove")
    let output16 = linkedListData.remove()
    print("  出力値: \(output16)")
    print("  現在のデータ: \(linkedListData.display())")
    
    print("\nLinkedList TEST <----- end")
}

main()
