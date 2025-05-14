// Swift
// グラフの最小全域木: プリム法 (Prim)

// 優先度キューの代わりにヒープを使用するための実装
struct PriorityQueue<Element> {
    private var elements: [Element]
    private let hasHigherPriority: (Element, Element) -> Bool
    
    init(hasHigherPriority: @escaping (Element, Element) -> Bool) {
        self.elements = []
        self.hasHigherPriority = hasHigherPriority
    }
    
    var isEmpty: Bool {
        return elements.isEmpty
    }
    
    mutating func enqueue(_ element: Element) {
        elements.append(element)
        siftUp(from: elements.count - 1)
    }
    
    mutating func dequeue() -> Element? {
        guard !elements.isEmpty else { return nil }
        
        elements.swapAt(0, elements.count - 1)
        let element = elements.removeLast()
        if !elements.isEmpty {
            siftDown(from: 0)
        }
        return element
    }
    
    private mutating func siftUp(from index: Int) {
        var childIndex = index
        let child = elements[childIndex]
        var parentIndex = (childIndex - 1) / 2
        
        while childIndex > 0 && hasHigherPriority(child, elements[parentIndex]) {
            elements[childIndex] = elements[parentIndex]
            childIndex = parentIndex
            parentIndex = (childIndex - 1) / 2
        }
        
        elements[childIndex] = child
    }
    
    private mutating func siftDown(from index: Int) {
        let count = elements.count
        let parent = elements[index]
        var parentIndex = index
        
        while true {
            let leftChildIndex = 2 * parentIndex + 1
            let rightChildIndex = leftChildIndex + 1
            
            var bestChildIndex = parentIndex
            
            if leftChildIndex < count && hasHigherPriority(elements[leftChildIndex], elements[bestChildIndex]) {
                bestChildIndex = leftChildIndex
            }
            
            if rightChildIndex < count && hasHigherPriority(elements[rightChildIndex], elements[bestChildIndex]) {
                bestChildIndex = rightChildIndex
            }
            
            if bestChildIndex == parentIndex {
                break
            }
            
            elements[parentIndex] = elements[bestChildIndex]
            parentIndex = bestChildIndex
        }
        
        elements[parentIndex] = parent
    }
}

class GraphData {
    // 隣接リストとしてグラフデータを格納します。
    // キーは頂点、値はその頂点に隣接する頂点とその辺の重みのタプルのリストです。
    private var data: [String: [(String, Int)]] = [:]
    
    func get() -> [String: [(String, Int)]] {
        // グラフの内部データを取得します。
        return data
    }
    
    func getVertices() -> [String] {
        // グラフの全頂点をリストとして返します。
        return Array(data.keys)
    }
    
    func getEdges() -> [(String, String, Int)] {
        // グラフの全辺をリストとして返します。
        // 無向グラフの場合、(u, v, weight) の形式で返します。
        // 重複を避けるためにセットを使用します。
        var edges = Set<String>()
        var result: [(String, String, Int)] = []
        
        for (vertex, neighbors) in data {
            for (neighbor, weight) in neighbors {
                // 辺を正規化してセットに追加 (小さい方の頂点を最初にするなど)
                let edge = [vertex, neighbor].sorted().joined()
                if !edges.contains(edge) {
                    edges.insert(edge)
                    result.append(([vertex, neighbor].sorted()[0], [vertex, neighbor].sorted()[1], weight))
                }
            }
        }
        
        return result
    }
    
    func getNeighbors(vertex: String) -> [(String, Int)]? {
        // 指定された頂点の隣接ノードと辺の重みのリストを返します。
        // 形式: [(隣接頂点, 重み), ...]
        return data[vertex]
    }
    
    func getEdgeWeight(vertex1: String, vertex2: String) -> Int? {
        // 指定された2つの頂点間の辺の重みを返します。
        // 辺が存在しない場合はnilを返します。
        if let neighbors = data[vertex1] {
            for (neighbor, weight) in neighbors {
                if neighbor == vertex2 {
                    return weight
                }
            }
        }
        return nil
    }
    
    @discardableResult
    func addVertex(vertex: String) -> Bool {
        // 新しい頂点をグラフに追加します。
        if data[vertex] == nil {
            data[vertex] = []
        }
        // 既に存在する場合は追加しないがTrueを返す（変更なしでも成功とみなす）
        return true
    }
    
    @discardableResult
    func addEdge(vertex1: String, vertex2: String, weight: Int) -> Bool {
        // 両頂点間に辺を追加します。重みを指定します。
        // 頂点がグラフに存在しない場合は追加します。
        if data[vertex1] == nil {
            addVertex(vertex: vertex1)
        }
        if data[vertex2] == nil {
            addVertex(vertex: vertex2)
        }
        
        // vertex1 -> vertex2 の辺を追加（重み付き）
        var edgeExistsV1V2 = false
        if var neighbors = data[vertex1] {
            for i in 0..<neighbors.count {
                if neighbors[i].0 == vertex2 {
                    neighbors[i] = (vertex2, weight) // 既に存在する場合は重みを更新
                    data[vertex1] = neighbors
                    edgeExistsV1V2 = true
                    break
                }
            }
            if !edgeExistsV1V2 {
                data[vertex1]?.append((vertex2, weight))
            }
        }
        
        // vertex2 -> vertex1 の辺を追加（重み付き）
        var edgeExistsV2V1 = false
        if var neighbors = data[vertex2] {
            for i in 0..<neighbors.count {
                if neighbors[i].0 == vertex1 {
                    neighbors[i] = (vertex1, weight) // 既に存在する場合は重みを更新
                    data[vertex2] = neighbors
                    edgeExistsV2V1 = true
                    break
                }
            }
            if !edgeExistsV2V1 {
                data[vertex2]?.append((vertex1, weight))
            }
        }
        
        return true
    }    
    
    func isEmpty() -> Bool {
        // グラフが空かどうかを返します。
        return data.isEmpty
    }
        
    @discardableResult
    func clear() -> Bool {
        // グラフを空にします。
        data = [:]
        return true
    }
    
    func getMST(startVertex: String? = nil) -> [(String, String, Int)]? {
        let vertices = getVertices()
        if vertices.isEmpty {
            return [] // グラフが空
        }
        
        let start = startVertex ?? vertices[0]
        guard data[start] != nil else {
            print("ERROR: 開始頂点 \(start) はグラフに存在しません。")
            return nil
        }
        
        // MSTに含まれる頂点のセット
        var inMST = Set<String>()
        // 優先度付きキュー (重み, 現在の頂点, 遷移元の頂点)
        var minHeap = PriorityQueue<(Int, String, String?)> { $0.0 < $1.0 }
        // MSTを構成する辺のリスト
        var mstEdges: [(String, String, Int)] = []
        // 各頂点への最小コスト（MSTに追加する際の辺の重み）と、その遷移元の頂点を記録
        var minCost = [String: Int]()
        var parent = [String: String?]()
        
        for vertex in vertices {
            minCost[vertex] = Int.max
            parent[vertex] = nil
        }
        
        // 開始頂点の処理
        minCost[start] = 0
        minHeap.enqueue((0, start, nil)) // (コスト, 現在の頂点, 遷移元の頂点)
        
        while !minHeap.isEmpty {
            // 最小コストの辺を持つ頂点を取り出す
            guard let (cost, currentVertex, fromVertex) = minHeap.dequeue() else { break }
            
            // 既にMSTに含まれている頂点であればスキップ
            if inMST.contains(currentVertex) {
                continue
            }
            
            // 現在の頂点をMSTに追加
            inMST.insert(currentVertex)
            
            // MSTに追加された辺を記録 (開始頂点以外)
            if let from = fromVertex {
                // from_vertex から current_vertex への辺の重みを取得
                if let weight = getEdgeWeight(vertex1: from, vertex2: currentVertex) {
                    let edge = ([from, currentVertex].sorted(), weight)
                    mstEdges.append((edge.0[0], edge.0[1], edge.1))
                }
            }
            
            // 現在の頂点に隣接する頂点を調べ、MSTへの追加コストを更新
            if let neighborsWithWeight = getNeighbors(vertex: currentVertex) {
                for (neighbor, weight) in neighborsWithWeight {
                    // 隣接頂点がまだMSTに含まれておらず、現在のコストよりも小さい場合
                    if !inMST.contains(neighbor) && weight < minCost[neighbor]! {
                        minCost[neighbor] = weight
                        parent[neighbor] = currentVertex
                        minHeap.enqueue((weight, neighbor, currentVertex))
                    }
                }
            }
        }
        
        return mstEdges
    }
}

func main() {
    print("Prims TEST -----> start")
    let graphData = GraphData()
    
    graphData.clear()
    let inputList1 = [("A", "B", 4), ("B", "C", 3), ("B", "D", 2), ("D", "A", 1), ("A", "C", 2), ("B", "D", 2)]
    for input in inputList1 {
        graphData.addEdge(vertex1: input.0, vertex2: input.1, weight: input.2)
    }
    print("\nグラフの頂点: \(graphData.getVertices())")
    print("グラフの辺 (重み付き): \(graphData.getEdges())")
    if let outputMst = graphData.getMST() {
        for edge in outputMst {
            print("Edge: \(edge.0) - \(edge.1), Weight: \(edge.2)")
        }
        let totalWeight = outputMst.reduce(0) { $0 + $1.2 }
        print("最小全域木の合計重み: \(totalWeight)")
    }
    
    graphData.clear()
    let inputList2 = [("A", "B", 4), ("C", "D", 4), ("E", "F", 1), ("F", "G", 1)]
    for input in inputList2 {
        graphData.addEdge(vertex1: input.0, vertex2: input.1, weight: input.2)
    }
    print("\nグラフの頂点: \(graphData.getVertices())")
    print("グラフの辺 (重み付き): \(graphData.getEdges())")
    if let outputMst = graphData.getMST() {
        for edge in outputMst {
            print("Edge: \(edge.0) - \(edge.1), Weight: \(edge.2)")
        }
        let totalWeight = outputMst.reduce(0) { $0 + $1.2 }
        print("最小全域木の合計重み: \(totalWeight)")
    }
    
    graphData.clear()
    let inputList3 = [("A", "B", 4), ("B", "C", 3), ("D", "E", 5)]
    for input in inputList3 {
        graphData.addEdge(vertex1: input.0, vertex2: input.1, weight: input.2)
    }
    print("\nグラフの頂点: \(graphData.getVertices())")
    print("グラフの辺 (重み付き): \(graphData.getEdges())")
    if let outputMst = graphData.getMST() {
        for edge in outputMst {
            print("Edge: \(edge.0) - \(edge.1), Weight: \(edge.2)")
        }
        let totalWeight = outputMst.reduce(0) { $0 + $1.2 }
        print("最小全域木の合計重み: \(totalWeight)")
    }
    
    graphData.clear()
    let inputList4: [(String, String, Int)] = []
    for input in inputList4 {
        graphData.addEdge(vertex1: input.0, vertex2: input.1, weight: input.2)
    }
    print("\nグラフの頂点: \(graphData.getVertices())")
    print("グラフの辺 (重み付き): \(graphData.getEdges())")
    if let outputMst = graphData.getMST() {
        for edge in outputMst {
            print("Edge: \(edge.0) - \(edge.1), Weight: \(edge.2)")
        }
        let totalWeight = outputMst.reduce(0) { $0 + $1.2 }
        print("最小全域木の合計重み: \(totalWeight)")
    }
    
    print("\nPrims TEST <----- end")
}

main()