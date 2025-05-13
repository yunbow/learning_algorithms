// Swift
// グラフの連結成分: Union-Find

class GraphData {
    // 隣接ノードとその辺の重みを格納します。
    // キーは頂点、値はその頂点に隣接する頂点と重みのタプルの配列です。
    private var _data: [String: [(String, Int)]] = [:]
    
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
        
        for (vertex, neighbors) in _data {
            for (neighbor, weight) in neighbors {
                // 辺を正規化してセットに追加 (小さい方の頂点を最初にするなど)
                let edge = [vertex, neighbor].sorted().joined(separator: "-")
                if !edges.contains(edge) {
                    edges.insert(edge)
                    let sortedVertices = [vertex, neighbor].sorted()
                    result.append((sortedVertices[0], sortedVertices[1], weight))
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
        return nil // 辺が存在しない場合
    }
    
    func getVertice(vertex: String) -> [(String, Int)]? {
        // 頂点がグラフに存在するか確認する
        if let neighbors = _data[vertex] {
            // 存在する場合は、その頂点の隣接リスト（関連する値）を返す
            return neighbors
        } else {
            // 存在しない場合はメッセージを表示し、nilを返す
            print("ERROR: \(vertex)は範囲外です")
            return nil
        }
    }
    
    func getEdge(vertex1: String, vertex2: String) -> Bool {
        // 指定された2つの頂点間に辺が存在するかを確認する
        // 両方の頂点がグラフに存在する必要がある
        if let neighbors1 = _data[vertex1], _data[vertex2] != nil {
            // vertex1の隣接リストにvertex2が含まれているかを確認
            return neighbors1.contains { $0.0 == vertex2 }
        } else {
            // どちらかの頂点が存在しない場合は辺も存在しない
            return false
        }
    }
    
    func addVertex(vertex: String) -> Bool {
        // 新しい頂点をグラフに追加します。
        if _data[vertex] == nil {
            _data[vertex] = []
            return true
        }
        // 既に存在する場合は追加しないがTrueを返す（変更なしでも成功とみなす）
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
            for i in 0..<neighbors.count {
                if neighbors[i].0 == vertex2 {
                    neighbors[i] = (vertex2, weight) // 既に存在する場合は重みを更新
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
            for i in 0..<neighbors.count {
                if neighbors[i].0 == vertex1 {
                    neighbors[i] = (vertex1, weight) // 既に存在する場合は重みを更新
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
                if var neighbors = _data[v] {
                    neighbors = neighbors.filter { $0.0 != vertex }
                    _data[v] = neighbors
                }
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
            if var neighbors = _data[vertex1] {
                let originalLenV1 = neighbors.count
                neighbors = neighbors.filter { $0.0 != vertex2 }
                _data[vertex1] = neighbors
                if neighbors.count < originalLenV1 {
                    removed = true
                }
            }
            
            // vertex2 から vertex1 への辺を削除
            if var neighbors = _data[vertex2] {
                let originalLenV2 = neighbors.count
                neighbors = neighbors.filter { $0.0 != vertex1 }
                _data[vertex2] = neighbors
                if neighbors.count < originalLenV2 {
                    removed = true
                }
            }
            
            return removed // 少なくとも片方向が削除されたか
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
        _data.removeAll()
        return true
    }
    
    func getConnectedComponents() -> [[String]] {
        if _data.isEmpty {
            return [] // 空のグラフの場合は空リストを返す
        }
        
        // Union-Findのためのデータ構造を初期化
        // parent[i] は要素 i の親を示す
        // size[i] は要素 i を根とする集合のサイズを示す (Union by Size用)
        var parent: [String: String] = [:]
        var size: [String: Int] = [:]
        
        // 各頂点を初期状態では自分自身の集合に属させ、サイズを1とする
        let vertices = getVertices()
        for vertex in vertices {
            parent[vertex] = vertex
            size[vertex] = 1
        }
        
        // 経路圧縮 (Path Compression) を伴う Find 操作
        func find(_ v: String) -> String {
            // vの親がv自身でなければ、根を探しにいく
            if parent[v] != v {
                // 見つけた根をvの直接の親として記録 (経路圧縮)
                parent[v] = find(parent[v]!)
            }
            return parent[v]! // 最終的に根を返す
        }
        
        // Union by Size を伴う Union 操作
        func union(_ u: String, _ v: String) -> Bool {
            let rootU = find(u)
            let rootV = find(v)
            
            // 根が同じ場合は、すでに同じ集合に属しているので何もしない
            if rootU != rootV {
                // より小さいサイズの木を大きいサイズの木に結合する
                if size[rootU]! < size[rootV]! {
                    parent[rootU] = rootV
                    size[rootV]! += size[rootU]!
                } else {
                    parent[rootV] = rootU
                    size[rootU]! += size[rootV]!
                }
                return true // 結合が行われた
            }
            return false // 結合は行われなかった
        }
        
        // グラフの全ての辺に対してUnion操作を行い、連結成分をマージする
        for edge in getEdges() {
            let (u, v, _) = edge  // タプルから u と v だけを取り出し、重み (_) は無視する
            _ = union(u, v)
        }
        
        // 連結成分をグループ化する
        // 根をキーとして、その根に属する頂点のリストを値とする辞書を作成
        var components: [String: [String]] = [:]
        for vertex in vertices {
            let root = find(vertex) // 各頂点の最終的な根を見つける
            if components[root] == nil {
                components[root] = []
            }
            components[root]?.append(vertex)
        }
        
        // 連結成分のリスト（値の部分）を返す
        return Array(components.values)
    }
}

func main() {
    print("UnionFind TEST -----> start")
    
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
    
    print("\nUnionFind TEST <----- end")
}

main()