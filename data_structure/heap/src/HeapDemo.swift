// Swift
// データ構造: ヒープ (Heap)

import Foundation

class HeapData {
    private var data: [Int]
    private let isMinHeap: Bool
    
    init(isMinHeap: Bool = true) {
        self.data = []
        self.isMinHeap = isMinHeap
    }
    
    private func getParentIndex(_ index: Int) -> Int {
        return (index - 1) / 2
    }
    
    private func getLeftChildIndex(_ index: Int) -> Int {
        return 2 * index + 1
    }
    
    private func getRightChildIndex(_ index: Int) -> Int {
        return 2 * index + 2
    }
    
    private func hasParent(_ index: Int) -> Bool {
        return getParentIndex(index) >= 0
    }
    
    private func hasLeftChild(_ index: Int) -> Bool {
        return getLeftChildIndex(index) < data.count
    }
    
    private func hasRightChild(_ index: Int) -> Bool {
        return getRightChildIndex(index) < data.count
    }
    
    private func shouldSwap(_ index1: Int, _ index2: Int) -> Bool {
        if isMinHeap {
            return data[index1] > data[index2]
        } else {
            return data[index1] < data[index2]
        }
    }
    
    private func swap(_ index1: Int, _ index2: Int) {
        data.swapAt(index1, index2)
    }
    
    private func heapifyDown(_ index: Int) {
        var currentIndex = index
        var smallestOrLargest = currentIndex
        
        if hasLeftChild(currentIndex) && shouldSwap(smallestOrLargest, getLeftChildIndex(currentIndex)) {
            smallestOrLargest = getLeftChildIndex(currentIndex)
        }
        
        if hasRightChild(currentIndex) && shouldSwap(smallestOrLargest, getRightChildIndex(currentIndex)) {
            smallestOrLargest = getRightChildIndex(currentIndex)
        }
        
        if smallestOrLargest != currentIndex {
            swap(currentIndex, smallestOrLargest)
            heapifyDown(smallestOrLargest)
        }
    }
    
    private func heapifyUp(_ index: Int) {
        var currentIndex = index
        while hasParent(currentIndex) && shouldSwap(getParentIndex(currentIndex), currentIndex) {
            let parentIndex = getParentIndex(currentIndex)
            swap(parentIndex, currentIndex)
            currentIndex = parentIndex
        }
    }
    
    func get() -> [Int] {
        return data
    }
    
    func getIndex(of item: Int) -> Int {
        return data.firstIndex(of: item) ?? -1
    }
    
    func getValue(at index: Int) -> Int? {
        guard 0 <= index, index < data.count else {
            return nil
        }
        return data[index]
    }
    
    @discardableResult
    func heapify(_ array: [Int]) -> Bool {
        data = array
        for i in stride(from: data.count / 2 - 1, through: 0, by: -1) {
            heapifyDown(i)
        }
        return true
    }
    
    @discardableResult
    func push(_ value: Int) -> Bool {
        data.append(value)
        heapifyUp(data.count - 1)
        return true
    }
    
    @discardableResult
    func pop() -> Bool {
        guard !data.isEmpty else { return false }
        
        let lastElement = data.removeLast()
        
        if !data.isEmpty {
            data[0] = lastElement
            heapifyDown(0)
        }
        
        return true
    }
    
    func peek() -> Int? {
        return data.first
    }
    
    func isEmpty() -> Bool {
        return data.isEmpty
    }
    
    func size() -> Int {
        return data.count
    }
    
    @discardableResult
    func clear() -> Bool {
        data.removeAll()
        return true
    }
}

func main() {
    print("Heap TEST -----> start")
    
    print("\nmin heap: new")
    let minHeap = HeapData(isMinHeap: true)
    print("  現在のデータ: \(minHeap.get())")
    
    print("\nmin heap: heapify")
    let input = [4, 10, 3, 5, 1]
    print("  入力値: \(input)")
    let output = minHeap.heapify(input)
    print("  出力値: \(output)")
    print("  現在のデータ: \(minHeap.get())")
    
    print("\nmin heap: push")
    let input1 = 2
    print("  入力値: \(input1)")
    let output1 = minHeap.push(input1)
    print("  出力値: \(output1)")
    print("  現在のデータ: \(minHeap.get())")
    
    print("\nmin heap: push")
    let input2 = 15
    print("  入力値: \(input2)")
    let output2 = minHeap.push(input2)
    print("  出力値: \(output2)")
    print("  現在のデータ: \(minHeap.get())")
    
    print("\nmin heap: peek")
    let output3 = minHeap.peek()
    print("  出力値: \(String(describing: output3))")
    
    print("\nmin heap: pop")
    let output4 = minHeap.pop()
    print("  出力値: \(output4)")
    print("  現在のデータ: \(minHeap.get())")
    
    print("\nmin heap: pop")
    let output5 = minHeap.pop()
    print("  出力値: \(output5)")
    print("  現在のデータ: \(minHeap.get())")
    
    print("\nmin heap: get_index")
    let input3 = 3
    print("  入力値: \(input3)")
    let output6 = minHeap.getIndex(of: input3)
    print("  出力値: \(output6)")
    
    print("\nmin heap: get_index")
    let input4 = 100
    print("  入力値: \(input4)")
    let output7 = minHeap.getIndex(of: input4)
    print("  出力値: \(output7)")
    
    print("\nmin heap: is_empty")
    let output8 = minHeap.isEmpty()
    print("  出力値: \(output8)")
    
    print("\nmin heap: size")
    let output9 = minHeap.size()
    print("  出力値: \(output9)")
    
    print("\nmin heap: clear")
    let output10 = minHeap.clear()
    print("  出力値: \(output10)")
    print("  現在のデータ: \(minHeap.get())")
    
    print("\nmin heap: is_empty")
    let output11 = minHeap.isEmpty()
    print("  出力値: \(output11)")
    
    print("\nmax heap: new")
    let maxHeap = HeapData(isMinHeap: false)
    print("  現在のデータ: \(maxHeap.get())")
    
    print("\nmax heap: heapify")
    let input5 = [4, 10, 3, 5, 1]
    print("  入力値: \(input5)")
    let output12 = maxHeap.heapify(input5)
    print("  出力値: \(output12)")
    print("  現在のデータ: \(maxHeap.get())")
    
    print("\nmax heap: push")
    let input6 = 12
    print("  入力値: \(input6)")
    let output13 = maxHeap.push(input6)
    print("  出力値: \(output13)")
    print("  現在のデータ: \(maxHeap.get())")
    
    print("\nmax heap: push")
    let input7 = 0
    print("  入力値: \(input7)")
    let output14 = maxHeap.push(input7)
    print("  出力値: \(output14)")
    print("  現在のデータ: \(maxHeap.get())")
    
    print("\nmax heap: peek")
    let output15 = maxHeap.peek()
    print("  出力値: \(String(describing: output15))")
    
    print("\nmax heap: pop")
    let output16 = maxHeap.pop()
    print("  出力値: \(output16)")
    print("  現在のデータ: \(maxHeap.get())")
    
    print("\nmax heap: pop")
    let output17 = maxHeap.pop()
    print("  出力値: \(output17)")
    print("  現在のデータ: \(maxHeap.get())")
    
    print("\nmax heap: get_index")
    let input8 = 5
    print("  入力値: \(input8)")
    let output18 = maxHeap.getIndex(of: input8)
    print("  出力値: \(output18)")
    
    print("\nmax heap: get_index")
    let input9 = -10
    print("  入力値: \(input9)")
    let output19 = maxHeap.getIndex(of: input9)
    print("  出力値: \(output19)")
    
    print("\nmax heap: is_empty")
    let output20 = maxHeap.isEmpty()
    print("  出力値: \(output20)")
    
    print("\nmax heap: size")
    let output21 = maxHeap.size()
    print("  出力値: \(output21)")
    
    print("\nmax heap: clear")
    let output22 = maxHeap.clear()
    print("  出力値: \(output22)")
    print("  現在のデータ: \(maxHeap.get())")
    
    print("\nmax heap: is_empty")
    let output23 = maxHeap.isEmpty()
    print("  出力値: \(output23)")
    
    print("\nHeap TEST <----- end")
}

main()