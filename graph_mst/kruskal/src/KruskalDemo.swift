// Swift
// グラフの最小全域木: クラスカル法 (Kruskal)

// Union-Find データ構造 (Disjoint Set Union)
class DSU {
    private var parent: [String: String]
    private var rank: [String: Int]
    
    init(vertices: [String]) {
        // 各頂点の親を格納します。最初は各頂点自身が親です。
        parent = [:]
        // ランク（木の高さまたはサイズ）を格納し、union操作を最適化します。
        rank = [:]
        
        for vertex in vertices {
            parent[vertex] = vertex
            rank[vertex] = 0
        }
    }
    
    // 頂点 i が属する集合の代表元（根）を見つけます。
    // パス圧縮により効率化されます。
    func find(_ i: String) -> String {
        if parent[i] == i {
            return i
        }
        // パス圧縮: 探索中に encountered したノードを根に直接つなぎ直します。
        parent[i] = find(parent[i]!)
        return parent[i]!
    }
    
    // 頂点 i と 頂点 j を含む二つの集合を結合します。
    // ランクによるunionにより効率化されます。
    func union(_ i: String, _ j: String) -> Bool {
        let rootI = find(i)
        let rootJ = find(j)
        
        // 根が異なる場合のみ結合します（同じ集合に属している場合は何もしません）。
        if rootI != rootJ {
            // ランクが小さい方の木を、ランクが大きい方の木の根に付けます。
            if rank[rootI]! < rank[rootJ]! {
                parent[rootI] = rootJ
            } else if rank[rootI]! > rank[rootJ]! {
                parent[rootJ] = rootI
            } else {
                // ランクが同じ場合はどちらかを親とし、その親のランクを増やします。
                parent[rootJ] = rootI
                rank[rootI]! += 1
            }
            return true // 集合が結合された
        }
        return false // 既に同じ集合に属していた
    }
}

// 重みを扱えるように改変された GraphData クラス
class GraphData {
    // (隣接頂点, 重み) のタプルを格納する辞書
    private var data: [String: [(String, Int)]]
    
    init() {
        data = [:]
    }
    
    func get() -> [String: [(String, Int)]] {
        return data
    }
    
    func getVertices() -> [String] {
        return Array(data.keys)
    }
    
    func getEdges() -> [(String, String, Int)] {
        var edges = Set<String>()
        var result: [(String, String, Int)] = []
        
        for (vertex, neighbors) in data {
            for (neighbor, weight) in neighbors {
                // 辺を正規化して重複を避ける
                let edge = [vertex, neighbor].sorted().joined(separator: ",")
                if !edges.contains(edge) {
                    edges.insert(edge)
                    let sortedVertices = [vertex, neighbor].sorted()
                    result.append((sortedVertices[0], sortedVertices[1], weight))
                }
            }
        }
        return result
    }
    
    func addVertex(_ vertex: String) -> Bool {
        if data[vertex] == nil {
            data[vertex] = []
        }
        return true
    }
    
    func addEdge(_ vertex1: String, _ vertex2: String, _ weight: Int) -> Bool {
        // 頂点がグラフに存在しない場合は追加
        if data[vertex1] == nil {
            addVertex(vertex1)
        }
        if data[vertex2] == nil {
            addVertex(vertex2)
        }
        
        // vertex1 -> vertex2 の辺を追加（重み付き）
        var edgeExistsV1V2 = false
        if var neighbors = data[vertex1] {
            for i in 0..<neighbors.count {
                if neighbors[i].0 == vertex2 {
                    neighbors[i] = (vertex2, weight) // 既に存在する場合は重みを更新
                    edgeExistsV1V2 = true
                    break
                }
            }
            if !edgeExistsV1V2 {
                neighbors.append((vertex2, weight))
            }
            data[vertex1] = neighbors
        }
        
        // vertex2 -> vertex1 の辺を追加（重み付き）
        var edgeExistsV2V1 = false
        if var neighbors = data[vertex2] {
            for i in 0..<neighbors.count {
                if neighbors[i].0 == vertex1 {
                    neighbors[i] = (vertex1, weight) // 既に存在する場合は重みを更新
                    edgeExistsV2V1 = true
                    break
                }
            }
            if !edgeExistsV2V1 {
                neighbors.append((vertex1, weight))
            }
            data[vertex2] = neighbors
        }
        
        return true
    }
        
    func clear() -> Bool {
        data.removeAll()
        return true
    }
    
    func getMST() -> [(String, String, Int)] {
        // 1. 全ての辺を取得し、重みでソート
        let edges = getEdges()
        let sortedEdges = edges.sorted { $0.2 < $1.2 }
        
        // 2. Union-Findデータ構造を初期化
        let vertices = getVertices()
        let dsu = DSU(vertices: vertices)
        
        // 3. MSTを構築
        var mstEdges: [(String, String, Int)] = []
        var edgesCount = 0
        
        // ソートされた辺を順番に調べる
        for (u, v, weight) in sortedEdges {
            // 辺 (u, v) の両端点が属する集合の代表元（根）を見つける
            let rootU = dsu.find(u)
            let rootV = dsu.find(v)
            
            // 両端点が異なる集合に属する場合、その辺をMSTに追加してもサイクルは形成されない
            if rootU != rootV {
                // 辺をMSTに追加
                mstEdges.append((u, v, weight))
                // 辺を追加したので、両端点の集合を結合
                dsu.union(u, v)
                // MSTに追加した辺の数を増やす
                edgesCount += 1
                
                // 頂点数から1を引いた数の辺がMSTに追加されたら終了
                if edgesCount == vertices.count - 1 {
                    break
                }
            }
        }
        
        // MST (または最小全域森) の辺のリストを返す
        return mstEdges
    }
}

func main() {
    print("Kruskal TEST -----> start")
    let graphData = GraphData()
    
    graphData.clear()
    let inputList1: [(String, String, Int)] = [("A", "B", 4), ("B", "C", 3), ("B", "D", 2), ("D", "A", 1), ("A", "C", 2), ("B", "D", 2)]
    for input in inputList1 {
        graphData.addEdge(input.0, input.1, input.2)
    }
    print("\nグラフの頂点:", graphData.getVertices())
    print("グラフの辺 (重み付き):", graphData.getEdges())
    let outputMst = graphData.getMST()
    for edge in outputMst {
        print("Edge: \(edge.0) - \(edge.1), Weight: \(edge.2)")
    }
    let totalWeight = outputMst.reduce(0) { $0 + $1.2 }
    print("最小全域木の合計重み: \(totalWeight)")
    
    graphData.clear()
    let inputList2: [(String, String, Int)] = [("A", "B", 4), ("C", "D", 4), ("E", "F", 1), ("F", "G", 1)]
    for input in inputList2 {
        graphData.addEdge(input.0, input.1, input.2)
    }
    print("\nグラフの頂点:", graphData.getVertices())
    print("グラフの辺 (重み付き):", graphData.getEdges())
    let outputMst2 = graphData.getMST()
    for edge in outputMst2 {
        print("Edge: \(edge.0) - \(edge.1), Weight: \(edge.2)")
    }
    let totalWeight2 = outputMst2.reduce(0) { $0 + $1.2 }
    print("最小全域木の合計重み: \(totalWeight2)")
    
    graphData.clear()
    let inputList3: [(String, String, Int)] = [("A", "B", 4), ("B", "C", 3), ("D", "E", 5)]
    for input in inputList3 {
        graphData.addEdge(input.0, input.1, input.2)
    }
    print("\nグラフの頂点:", graphData.getVertices())
    print("グラフの辺 (重み付き):", graphData.getEdges())
    let outputMst3 = graphData.getMST()
    for edge in outputMst3 {
        print("Edge: \(edge.0) - \(edge.1), Weight: \(edge.2)")
    }
    let totalWeight3 = outputMst3.reduce(0) { $0 + $1.2 }
    print("最小全域木の合計重み: \(totalWeight3)")
    
    graphData.clear()
    let inputList4: [(String, String, Int)] = []
    for input in inputList4 {
        graphData.addEdge(input.0, input.1, input.2)
    }
    print("\nグラフの頂点:", graphData.getVertices())
    print("グラフの辺 (重み付き):", graphData.getEdges())
    let outputMst4 = graphData.getMST()
    for edge in outputMst4 {
        print("Edge: \(edge.0) - \(edge.1), Weight: \(edge.2)")
    }
    let totalWeight4 = outputMst4.reduce(0) { $0 + $1.2 }
    print("最小全域木の合計重み: \(totalWeight4)")
    
    print("\nKruskal TEST <----- end")
}

main()