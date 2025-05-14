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
        
    func isEmpty() -> Bool {
        // グラフが空かどうか
        return _data.isEmpty
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