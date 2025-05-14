// Swift
// グラフの最短経路: ダイクストラ法 (dijkstra)

import Foundation

class GraphData {
    // 隣接ノードとその辺の重みを格納します。
    // キーは頂点、値はその頂点に隣接する頂点と重みの配列です。
    private var _data: [String: [(neighbor: String, weight: Int)]] = [:]
    
    func get() -> [String: [(neighbor: String, weight: Int)]] {
        // グラフの内部データを取得します。
        return _data
    }
    
    func getVertices() -> [String] {
        // グラフの全頂点をリストとして返します。
        return Array(_data.keys)
    }
    
    func getEdges() -> [(String, String, Int)] {
        // グラフの全辺をリストとして返します。
        // 無向グラフの場合、(u, v, weight) の形式で返します。
        // 重複を避けるためにセットを使用します。
        var edges = Set<String>()
        var result: [(String, String, Int)] = []
        
        for (vertex, neighbors) in _data {
            for (neighbor, weight) in neighbors {
                // 辺を正規化してセットに追加 (小さい方の頂点を最初にするなど)
                let sortedEdge = [vertex, neighbor].sorted()
                let edgeKey = "\(sortedEdge[0])|\(sortedEdge[1])|\(weight)"
                
                if !edges.contains(edgeKey) {
                    edges.insert(edgeKey)
                    result.append((sortedEdge[0], sortedEdge[1], weight))
                }
            }
        }
        
        return result
    }
    
    func getNeighbors(_ vertex: String) -> [(String, Int)]? {
        // 指定された頂点の隣接ノードと辺の重みのリストを返します。
        // 形式: [(隣接頂点, 重み), ...]
        if let neighbors = _data[vertex] {
            return neighbors
        } else {
            return nil // 頂点が存在しない場合はnilを返す
        }
    }
        
    func addVertex(_ vertex: String) -> Bool {
        // 新しい頂点をグラフに追加します。
        if _data[vertex] == nil {
            _data[vertex] = []
            return true
        }
        // 既に存在する場合は追加しないがtrueを返す（変更なしでも成功とみなす）
        return true
    }
    
    func addEdge(vertex1: String, vertex2: String, weight: Int) -> Bool {
        // 両頂点間に辺を追加します。重みを指定します。
        // 頂点がグラフに存在しない場合は追加します。
        if _data[vertex1] == nil {
            _ = addVertex(vertex1)
        }
        if _data[vertex2] == nil {
            _ = addVertex(vertex2)
        }
        
        // vertex1 -> vertex2 の辺を追加（重み付き）
        var edgeExistsV1V2 = false
        if var neighbors = _data[vertex1] {
            for i in 0..<neighbors.count {
                if neighbors[i].neighbor == vertex2 {
                    neighbors[i] = (vertex2, weight) // 既存の辺の重みを更新
                    edgeExistsV1V2 = true
                    break
                }
            }
            if !edgeExistsV1V2 {
                neighbors.append((vertex2, weight))
            }
            _data[vertex1] = neighbors
        }
        
        // vertex2 -> vertex1 の辺を追加（重み付き）
        var edgeExistsV2V1 = false
        if var neighbors = _data[vertex2] {
            for i in 0..<neighbors.count {
                if neighbors[i].neighbor == vertex1 {
                    neighbors[i] = (vertex1, weight) // 既存の辺の重みを更新
                    edgeExistsV2V1 = true
                    break
                }
            }
            if !edgeExistsV2V1 {
                neighbors.append((vertex1, weight))
            }
            _data[vertex2] = neighbors
        }
        
        return true
    }
        
    func clear() -> Bool {
        // グラフを空にします。
        _data = [:]
        return true
    }
    
    func getShortestPath(startVertex: String, endVertex: String, heuristic: (String, String) -> Int) -> ([String]?, Int) {
        if _data[startVertex] == nil || _data[endVertex] == nil {
            print("ERROR: 開始頂点 '\(startVertex)' または 終了頂点 '\(endVertex)' がグラフに存在しません。")
            return (nil, .max)
        }
        
        // 距離を初期化: 全ての頂点への距離を無限大に設定し、開始頂点のみ0とする
        var distances: [String: Int] = [:]
        for vertex in _data.keys {
            distances[vertex] = .max
        }
        distances[startVertex] = 0
        
        // 経路を記録するための辞書: 各頂点に至る直前の頂点を保持
        var predecessors: [String: String?] = [:]
        for vertex in _data.keys {
            predecessors[vertex] = nil
        }
        
        // 優先度付きキュー: Swiftには標準のPriorityQueueがないため、配列をソートして使用
        // (距離, 頂点) のタプルを格納し、距離が小さい順に取り出す
        var priorityQueue: [(distance: Int, vertex: String)] = [(0, startVertex)]
        
        while !priorityQueue.isEmpty {
            // 優先度付きキューから最も距離の小さい頂点を取り出す
            priorityQueue.sort { $0.distance < $1.distance }
            let current = priorityQueue.removeFirst()
            let currentDistance = current.distance
            let currentVertex = current.vertex
            
            // 取り出した頂点への距離が、すでに記録されている距離より大きい場合は、
            // より短い経路が既に見つかっているためスキップ
            if let dist = distances[currentVertex], currentDistance > dist {
                continue
            }
            
            // 終了頂点に到達したら探索終了
            if currentVertex == endVertex {
                break // 最短経路が見つかった
            }
            
            // 現在の頂点から到達可能な隣接頂点を探索
            if let neighbors = getNeighbors(currentVertex) {
                for (neighbor, weight) in neighbors {
                    if let dist = distances[currentVertex], dist != .max {
                        let distanceThroughCurrent = dist + weight
                        
                        // より短い経路が見つかった場合
                        if let neighborDist = distances[neighbor], distanceThroughCurrent < neighborDist {
                            distances[neighbor] = distanceThroughCurrent
                            predecessors[neighbor] = currentVertex
                            // 優先度付きキューに隣接頂点を追加または更新
                            priorityQueue.append((distanceThroughCurrent, neighbor))
                        }
                    }
                }
            }
        }
        
        // 終了頂点への最短距離が無限大のままなら、到達不可能
        if let dist = distances[endVertex], dist == .max {
            print("INFO: 開始頂点 '\(startVertex)' から 終了頂点 '\(endVertex)' への経路は存在しません。")
            return (nil, .max)
        }
        
        // 最短経路を再構築
        var path: [String] = []
        var current: String? = endVertex
        while current != nil {
            path.append(current!)
            current = predecessors[current!] ?? nil
        }
        // 経路は逆順に構築されたので反転
        path.reverse()
        
        // 開始ノードから開始されていることを確認
        if !path.isEmpty && path[0] != startVertex {
            // これが発生するのは、開始頂点が存在しないか、到達不能な場合だが、
            // 前段のチェックで大部分はカバーされているはず。
            return (nil, .max)
        }
        
        return (path, distances[endVertex] ?? .max)
    }
}

// ヒューリスティック関数 (この例では常に0、ダイクストラ法と同じ)
func dummyHeuristic(u: String, v: String) -> Int {
    // u と v の間に何らかの推定距離を計算する関数
    // ここではダミーとして常に0を返す
    return 0
}

func main() {
    print("Dijkstra -----> start")
    
    let graphData = GraphData()
    
    graphData.clear()
    let inputList1: [(String, String, Int)] = [("A", "B", 4), ("B", "C", 3), ("B", "D", 2), ("D", "A", 1), ("A", "C", 2), ("B", "D", 2)]
    for input in inputList1 {
        _ = graphData.addEdge(vertex1: input.0, vertex2: input.1, weight: input.2)
    }
    print("\nグラフの頂点:", graphData.getVertices())
    print("グラフの辺 (重み付き):", graphData.getEdges())
    let input1 = ("A", "B")
    let shortestPath1 = graphData.getShortestPath(startVertex: input1.0, endVertex: input1.1, heuristic: dummyHeuristic)
    print("経路\(input1.0)-\(input1.1) の最短経路は \(String(describing: shortestPath1.0)) (重み: \(shortestPath1.1))")
    
    graphData.clear()
    let inputList2: [(String, String, Int)] = [("A", "B", 4), ("C", "D", 4), ("E", "F", 1), ("F", "G", 1)]
    for input in inputList2 {
        _ = graphData.addEdge(vertex1: input.0, vertex2: input.1, weight: input.2)
    }
    print("\nグラフの頂点:", graphData.getVertices())
    print("グラフの辺 (重み付き):", graphData.getEdges())
    let input2 = ("A", "B")
    let shortestPath2 = graphData.getShortestPath(startVertex: input2.0, endVertex: input2.1, heuristic: dummyHeuristic)
    print("経路\(input2.0)-\(input2.1) の最短経路は \(String(describing: shortestPath2.0)) (重み: \(shortestPath2.1))")
    
    graphData.clear()
    let inputList3: [(String, String, Int)] = [("A", "B", 4), ("B", "C", 3), ("D", "E", 5)]
    for input in inputList3 {
        _ = graphData.addEdge(vertex1: input.0, vertex2: input.1, weight: input.2)
    }
    print("\nグラフの頂点:", graphData.getVertices())
    print("グラフの辺 (重み付き):", graphData.getEdges())
    let input3 = ("A", "D")
    let shortestPath3 = graphData.getShortestPath(startVertex: input3.0, endVertex: input3.1, heuristic: dummyHeuristic)
    print("経路\(input3.0)-\(input3.1) の最短経路は \(String(describing: shortestPath3.0)) (重み: \(shortestPath3.1))")
    
    graphData.clear()
    let inputList4: [(String, String, Int)] = []
    for input in inputList4 {
        _ = graphData.addEdge(vertex1: input.0, vertex2: input.1, weight: input.2)
    }
    print("\nグラフの頂点:", graphData.getVertices())
    print("グラフの辺 (重み付き):", graphData.getEdges())
    let input4 = ("A", "B")
    let shortestPath4 = graphData.getShortestPath(startVertex: input4.0, endVertex: input4.1, heuristic: dummyHeuristic)
    print("経路\(input4.0)-\(input4.1) の最短経路は \(String(describing: shortestPath4.0)) (重み: \(shortestPath4.1))")
    
    print("\nDijkstra <----- end")
}

main()