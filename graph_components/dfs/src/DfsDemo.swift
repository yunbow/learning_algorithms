// Swift
// グラフの連結成分: DFS

class GraphData {
    // 隣接ノードとその辺の重みを格納します。
    // キーは頂点、値はその頂点に隣接する頂点と重みの配列です。
    private var _data: [String: [(String, Int)]] = [:]
    
    init() {}
    
    func get() -> [String: [(String, Int)]] {
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
        
        for vertex in _data.keys {
            for (neighbor, weight) in _data[vertex]! {
                // 辺を正規化してセットに追加 (小さい方の頂点を最初にするなど)
                let edge = [vertex, neighbor].sorted().joined(separator: "-")
                if !edges.contains(edge) {
                    edges.insert(edge)
                    let sortedPair = [vertex, neighbor].sorted()
                    result.append((sortedPair[0], sortedPair[1], weight))
                }
            }
        }
        return result
    }
    
    func getNeighbors(vertex: String) -> [(String, Int)]? {
        // 指定された頂点の隣接ノードと辺の重みのリストを返します。
        // 形式: [(隣接頂点, 重み), ...]
        return _data[vertex]
    }
    
    func getEdgeWeight(vertex1: String, vertex2: String) -> Int? {
        // 指定された2つの頂点間の辺の重みを返します。
        // 辺が存在しない場合はnilを返します。
        if let neighbors = _data[vertex1] {
            for (neighbor, weight) in neighbors {
                if neighbor == vertex2 {
                    return weight
                }
            }
        }
        return nil
    }
    
    func getVertice(vertex: String) -> [(String, Int)]? {
        // 頂点がグラフに存在するか確認する
        if let neighbors = _data[vertex] {
            return neighbors
        } else {
            print("ERROR: \(vertex)は範囲外です")
            return nil
        }
    }
    
    func getEdge(vertex1: String, vertex2: String) -> Bool {
        // 指定された2つの頂点間に辺が存在するかを確認する
        // 両方の頂点がグラフに存在する必要がある
        if let neighbors1 = _data[vertex1], _data[vertex2] != nil {
            return neighbors1.contains(where: { $0.0 == vertex2 })
        }
        return false
    }
    
    func addVertex(vertex: String) -> Bool {
        // 新しい頂点をグラフに追加します。
        if _data[vertex] == nil {
            _data[vertex] = []
        }
        return true
    }
    
    func addEdge(vertex1: String, vertex2: String, weight: Int) -> Bool {
        // 両頂点間に辺を追加します。重みを指定します。
        // 頂点がグラフに存在しない場合は追加します。
        if _data[vertex1] == nil {
            _ = addVertex(vertex: vertex1)
        }
        if _data[vertex2] == nil {
            _ = addVertex(vertex: vertex2)
        }
        
        // vertex1 -> vertex2 の辺を追加（重み付き）
        var edgeExistsV1V2 = false
        if var neighbors = _data[vertex1] {
            for (index, (neighbor, _)) in neighbors.enumerated() {
                if neighbor == vertex2 {
                    neighbors[index] = (vertex2, weight) // 既に存在する場合は重みを更新
                    _data[vertex1] = neighbors
                    edgeExistsV1V2 = true
                    break
                }
            }
            if !edgeExistsV1V2 {
                _data[vertex1]?.append((vertex2, weight))
            }
        }
        
        // vertex2 -> vertex1 の辺を追加（重み付き）
        var edgeExistsV2V1 = false
        if var neighbors = _data[vertex2] {
            for (index, (neighbor, _)) in neighbors.enumerated() {
                if neighbor == vertex1 {
                    neighbors[index] = (vertex1, weight) // 既に存在する場合は重みを更新
                    _data[vertex2] = neighbors
                    edgeExistsV2V1 = true
                    break
                }
            }
            if !edgeExistsV2V1 {
                _data[vertex2]?.append((vertex1, weight))
            }
        }
        
        return true
    }
    
    func removeVertex(vertex: String) -> Bool {
        // 頂点とそれに関連する辺を削除します。
        if _data[vertex] != nil {
            // この頂点への参照を他の頂点の隣接リストから削除する
            for v in _data.keys {
                _data[v] = _data[v]?.filter { $0.0 != vertex }
            }
            // 頂点自体を削除する
            _data.removeValue(forKey: vertex)
            return true
        } else {
            print("ERROR: \(vertex) は範囲外です")
            return false
        }
    }
    
    func removeEdge(vertex1: String, vertex2: String) -> Bool {
        // 両頂点間の辺を削除します。
        if _data[vertex1] != nil && _data[vertex2] != nil {
            var removed = false
            
            // vertex1 から vertex2 への辺を削除
            let originalLenV1 = _data[vertex1]?.count ?? 0
            _data[vertex1] = _data[vertex1]?.filter { $0.0 != vertex2 }
            if let newLenV1 = _data[vertex1]?.count, newLenV1 < originalLenV1 {
                removed = true
            }
            
            // vertex2 から vertex1 への辺を削除
            let originalLenV2 = _data[vertex2]?.count ?? 0
            _data[vertex2] = _data[vertex2]?.filter { $0.0 != vertex1 }
            if let newLenV2 = _data[vertex2]?.count, newLenV2 < originalLenV2 {
                removed = true
            }
            
            return removed
        } else {
            print("ERROR: \(vertex1) または \(vertex2) は範囲外です")
            return false
        }
    }
    
    func isEmpty() -> Bool {
        // グラフが空かどうか
        return _data.isEmpty
    }
    
    func size() -> Int {
        // グラフの頂点数を返す
        return _data.count
    }
    
    func clear() -> Bool {
        // グラフを空にする
        _data = [:]
        return true
    }
    
    private func _dfs(vertex: String, visited: inout Set<String>, currentComponent: inout [String]) {
        // 現在の頂点を訪問済みにマークし、現在の成分に追加
        visited.insert(vertex)
        currentComponent.append(vertex)
        
        // 隣接する頂点を探索
        guard let neighbors = _data[vertex] else { return }
        
        for neighborInfo in neighbors {
            let neighborVertex = neighborInfo.0
            
            // まだ訪問していない隣接頂点に対して再帰的にDFSを呼び出す
            if !visited.contains(neighborVertex) {
                _dfs(vertex: neighborVertex, visited: &visited, currentComponent: &currentComponent)
            }
        }
    }
    
    func getConnectedComponents() -> [[String]] {
        var visited = Set<String>() // 訪問済み頂点を記録するセット
        var connectedComponents: [[String]] = [] // 連結成分を格納するリスト
        
        // グラフの全頂点を順にチェック
        for vertex in getVertices() {
            // まだ訪問していない頂点からDFSを開始
            if !visited.contains(vertex) {
                var currentComponent: [String] = [] // 現在の連結成分を格納するリスト
                // DFSヘルパー関数を呼び出し、現在の連結成分を探索
                _dfs(vertex: vertex, visited: &visited, currentComponent: &currentComponent)
                // 探索で見つかった連結成分を結果リストに追加
                connectedComponents.append(currentComponent)
            }
        }
        
        return connectedComponents
    }
}

func main() {
    print("Dfs TEST -----> start")
    
    print("\nnew")
    let graphData = GraphData()
    print("  現在のデータ: \(graphData.get())")
    
    print("\nadd_edge")
    _ = graphData.clear()
    let inputList1: [(String, String, Int)] = [("A", "B", 4), ("B", "C", 3), ("B", "D", 2), ("D", "A", 1), ("A", "C", 2), ("B", "D", 2)]
    for input in inputList1 {
        print("  入力値: \(input)")
        let output = graphData.addEdge(vertex1: input.0, vertex2: input.1, weight: input.2)
        print("  出力値: \(output)")
    }
    print("  現在のデータ: \(graphData.get())")
    print("\nget_connected_components")
    let output1 = graphData.getConnectedComponents()
    print("  連結成分: \(output1)")
    
    print("\nadd_edge")
    _ = graphData.clear()
    let inputList2: [(String, String, Int)] = [("A", "B", 4), ("C", "D", 4), ("E", "F", 1), ("F", "G", 1)]
    for input in inputList2 {
        print("  入力値: \(input)")
        let output = graphData.addEdge(vertex1: input.0, vertex2: input.1, weight: input.2)
        print("  出力値: \(output)")
    }
    print("  現在のデータ: \(graphData.get())")
    print("\nget_connected_components")
    let output2 = graphData.getConnectedComponents()
    print("  連結成分: \(output2)")
    
    print("\nadd_edge")
    _ = graphData.clear()
    let inputList3: [(String, String, Int)] = [("A", "B", 4), ("B", "C", 3), ("D", "E", 5)]
    for input in inputList3 {
        print("  入力値: \(input)")
        let output = graphData.addEdge(vertex1: input.0, vertex2: input.1, weight: input.2)
        print("  出力値: \(output)")
    }
    print("  現在のデータ: \(graphData.get())")
    print("\nget_connected_components")
    let output3 = graphData.getConnectedComponents()
    print("  連結成分: \(output3)")
    
    print("\nadd_edge")
    _ = graphData.clear()
    let inputList4: [(String, String, Int)] = []
    for input in inputList4 {
        print("  入力値: \(input)")
        let output = graphData.addEdge(vertex1: input.0, vertex2: input.1, weight: input.2)
        print("  出力値: \(output)")
    }
    print("  現在のデータ: \(graphData.get())")
    print("\nget_connected_components")
    let output4 = graphData.getConnectedComponents()
    print("  連結成分: \(output4)")
    
    print("Dfs TEST <----- end")
}

main()
