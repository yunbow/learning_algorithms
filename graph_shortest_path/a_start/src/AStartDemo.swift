// Swift
// グラフの最短経路: A-star

import Foundation

class GraphData {
    // 隣接ノードとその辺の重みを格納します。
    // キーは頂点、値はその頂点に隣接する頂点と重みの配列です。
    private var _data: [String: [(neighbor: String, weight: Int)]] = [:]
    
    func get() -> [String: [(neighbor: String, weight: Int)]] {
        return _data
    }
    
    func getVertices() -> [String] {
        return Array(_data.keys)
    }
    
    func getEdges() -> [(u: String, v: String, weight: Int)] {
        var edges = Set<String>()
        var result: [(u: String, v: String, weight: Int)] = []
        
        for (vertex, neighbors) in _data {
            for (neighbor, weight) in neighbors {
                let edge = [vertex, neighbor].sorted().joined()
                if !edges.contains(edge) {
                    edges.insert(edge)
                    result.append((u: [vertex, neighbor].sorted()[0], 
                                  v: [vertex, neighbor].sorted()[1], 
                                  weight: weight))
                }
            }
        }
        
        return result
    }
    
    func getNeighbors(_ vertex: String) -> [(neighbor: String, weight: Int)]? {
        return _data[vertex]
    }
    
    @discardableResult
    func addVertex(_ vertex: String) -> Bool {
        if _data[vertex] == nil {
            _data[vertex] = []
        }
        return true
    }
    
    @discardableResult
    func addEdge(vertex1: String, vertex2: String, weight: Int) -> Bool {
        if _data[vertex1] == nil {
            addVertex(vertex1)
        }
        if _data[vertex2] == nil {
            addVertex(vertex2)
        }
        
        // vertex1 -> vertex2 の辺を追加
        var edgeExistsV1V2 = false
        if var neighbors = _data[vertex1] {
            for i in 0..<neighbors.count {
                if neighbors[i].neighbor == vertex2 {
                    neighbors[i] = (vertex2, weight)
                    edgeExistsV1V2 = true
                    _data[vertex1] = neighbors
                    break
                }
            }
            if !edgeExistsV1V2 {
                _data[vertex1]?.append((vertex2, weight))
            }
        }
        
        // vertex2 -> vertex1 の辺を追加
        var edgeExistsV2V1 = false
        if var neighbors = _data[vertex2] {
            for i in 0..<neighbors.count {
                if neighbors[i].neighbor == vertex1 {
                    neighbors[i] = (vertex1, weight)
                    edgeExistsV2V1 = true
                    _data[vertex2] = neighbors
                    break
                }
            }
            if !edgeExistsV2V1 {
                _data[vertex2]?.append((vertex1, weight))
            }
        }
        
        return true
    }
        
    @discardableResult
    func clear() -> Bool {
        _data.removeAll()
        return true
    }
    
    func getShortestPath(startVertex: String, endVertex: String, heuristic: (String, String) -> Int) -> ([String]?, Int) {
        if _data[startVertex] == nil || _data[endVertex] == nil {
            print("ERROR: 開始頂点または終了頂点がグラフに存在しません。")
            return (nil, Int.max)
        }
        
        if startVertex == endVertex {
            return ([startVertex], 0)
        }
        
        // g_costs: 開始ノードから各ノードまでの既知の最短コスト
        var gCosts = [String: Int]()
        for vertex in _data.keys {
            gCosts[vertex] = Int.max
        }
        gCosts[startVertex] = 0
        
        // f_costs: g_costs + ヒューリスティックコスト (推定合計コスト)
        var fCosts = [String: Int]()
        for vertex in _data.keys {
            fCosts[vertex] = Int.max
        }
        fCosts[startVertex] = heuristic(startVertex, endVertex)
        
        // came_from: 最短経路で各ノードの直前のノードを記録
        var cameFrom = [String: String]()
        
        // Swift標準ライブラリにはpriority queueが無いので、ヒープとして配列を使用
        // タプル (fコスト, 頂点) とする
        var openSet = [(fCost: Int, vertex: String)]()
        openSet.append((fCosts[startVertex]!, startVertex))
        
        // 優先度付きキューの代わりにする関数
        func popMinFCostVertex() -> (fCost: Int, vertex: String)? {
            if openSet.isEmpty { return nil }
            
            var minIndex = 0
            var minFCost = openSet[0].fCost
            
            for i in 1..<openSet.count {
                if openSet[i].fCost < minFCost {
                    minFCost = openSet[i].fCost
                    minIndex = i
                }
            }
            
            let result = openSet[minIndex]
            openSet.remove(at: minIndex)
            return result
        }
        
        while !openSet.isEmpty {
            // open_setから最もf_costが低いノードを取り出す
            guard let current = popMinFCostVertex() else { break }
            let currentFCost = current.fCost
            let currentVertex = current.vertex
            
            // 取り出したノードのf_costが、記録されているfCosts[currentVertex]より大きい場合は古い情報なので無視
            if currentFCost > fCosts[currentVertex]! {
                continue
            }
            
            // 目標ノードに到達した場合、経路を再構築して返す
            if currentVertex == endVertex {
                return (reconstructPath(cameFrom: cameFrom, currentVertex: endVertex), gCosts[endVertex]!)
            }
            
            // 現在のノードの隣接ノードを調べる
            guard let neighbors = getNeighbors(currentVertex) else { continue }
            
            for (neighbor, weight) in neighbors {
                // 現在のノードを経由した場合の隣接ノードへの新しいg_cost
                let tentativeGCost = gCosts[currentVertex]! + weight
                
                // 新しいg_costが、現在記録されている隣接ノードへのg_costよりも小さい場合
                if tentativeGCost < gCosts[neighbor]! {
                    // 経路情報を更新
                    cameFrom[neighbor] = currentVertex
                    gCosts[neighbor] = tentativeGCost
                    fCosts[neighbor] = gCosts[neighbor]! + heuristic(neighbor, endVertex)
                    
                    // 隣接ノードをopen_setに追加
                    openSet.append((fCosts[neighbor]!, neighbor))
                }
            }
        }
        
        // open_setが空になっても目標ノードに到達しなかった場合、経路は存在しない
        return (nil, Int.max)
    }
    
    private func reconstructPath(cameFrom: [String: String], currentVertex: String) -> [String] {
        var path = [String]()
        var current = currentVertex
        
        path.append(current)
        
        while cameFrom[current] != nil {
            current = cameFrom[current]!
            path.append(current)
        }
        
        return path.reversed()
    }
}

// ヒューリスティック関数 (この例では常に0、ダイクストラ法と同じ)
func dummyHeuristic(_ u: String, _ v: String) -> Int {
    return 0
}

func main() {
    print("A-start TEST -----> start")
    
    let graphData = GraphData()
    
    graphData.clear()
    let inputList1 = [("A", "B", 4), ("B", "C", 3), ("B", "D", 2), ("D", "A", 1), ("A", "C", 2), ("B", "D", 2)]
    for input in inputList1 {
        graphData.addEdge(vertex1: input.0, vertex2: input.1, weight: input.2)
    }
    print("\nグラフの頂点:", graphData.getVertices())
    print("グラフの辺 (重み付き):", graphData.getEdges())
    let input1 = ("A", "B")
    let shortestPath1 = graphData.getShortestPath(startVertex: input1.0, endVertex: input1.1, heuristic: dummyHeuristic)
    print("経路\(input1.0)-\(input1.1) の最短経路は \(String(describing: shortestPath1.0)) (重み: \(shortestPath1.1))")
    
    graphData.clear()
    let inputList2 = [("A", "B", 4), ("C", "D", 4), ("E", "F", 1), ("F", "G", 1)]
    for input in inputList2 {
        graphData.addEdge(vertex1: input.0, vertex2: input.1, weight: input.2)
    }
    print("\nグラフの頂点:", graphData.getVertices())
    print("グラフの辺 (重み付き):", graphData.getEdges())
    let input2 = ("A", "B")
    let shortestPath2 = graphData.getShortestPath(startVertex: input2.0, endVertex: input2.1, heuristic: dummyHeuristic)
    print("経路\(input2.0)-\(input2.1) の最短経路は \(String(describing: shortestPath2.0)) (重み: \(shortestPath2.1))")
    
    graphData.clear()
    let inputList3 = [("A", "B", 4), ("B", "C", 3), ("D", "E", 5)]
    for input in inputList3 {
        graphData.addEdge(vertex1: input.0, vertex2: input.1, weight: input.2)
    }
    print("\nグラフの頂点:", graphData.getVertices())
    print("グラフの辺 (重み付き):", graphData.getEdges())
    let input3 = ("A", "D")
    let shortestPath3 = graphData.getShortestPath(startVertex: input3.0, endVertex: input3.1, heuristic: dummyHeuristic)
    print("経路\(input3.0)-\(input3.1) の最短経路は \(String(describing: shortestPath3.0)) (重み: \(shortestPath3.1))")
    
    graphData.clear()
    let inputList4: [(String, String, Int)] = []
    for input in inputList4 {
        graphData.addEdge(vertex1: input.0, vertex2: input.1, weight: input.2)
    }
    print("\nグラフの頂点:", graphData.getVertices())
    print("グラフの辺 (重み付き):", graphData.getEdges())
    let input4 = ("A", "B")
    let shortestPath4 = graphData.getShortestPath(startVertex: input4.0, endVertex: input4.1, heuristic: dummyHeuristic)
    print("経路\(input4.0)-\(input4.1) の最短経路は \(String(describing: shortestPath4.0)) (重み: \(shortestPath4.1))")
    
    print("\nA-start TEST <----- end")
}

main()