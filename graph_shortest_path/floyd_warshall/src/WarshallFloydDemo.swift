// Swift
// グラフの最短経路: ワーシャルフロイド法 (Warshall Floyd)

import Foundation

class GraphData {
    // 隣接ノードとその辺の重みを格納します。
    // キーは頂点、値はその頂点に隣接する頂点と重みの配列です。
    private var _data: [String: [(neighbor: String, weight: Double)]] = [:]
    
    func get() -> [String: [(neighbor: String, weight: Double)]] {
        // グラフの内部データを取得します。
        return _data
    }
    
    func getVertices() -> [String] {
        // グラフの全頂点を配列として返します。
        return Array(_data.keys)
    }
    
    func getEdges() -> [(String, String, Double)] {
        // グラフの全辺を配列として返します。
        // 無向グラフの場合、(u, v, weight) の形式で返します。
        // 重複を避けるためにセットを使用します。
        var edges = Set<String>()
        var result: [(String, String, Double)] = []
        
        for (vertex, neighbors) in _data {
            for (neighbor, weight) in neighbors {
                // 辺を正規化してセットに追加 (小さい方の頂点を最初にするなど)
                let edgeKey = [vertex, neighbor].sorted().joined(separator: "-")
                if !edges.contains(edgeKey) {
                    edges.insert(edgeKey)
                    result.append((vertex, neighbor, weight))
                }
            }
        }
        return result
    }
    
    func getNeighbors(_ vertex: String) -> [(String, Double)]? {
        // 指定された頂点の隣接ノードと辺の重みの配列を返します。
        // 形式: [(隣接頂点, 重み), ...]
        return _data[vertex]
    }
    
    func addVertex(_ vertex: String) -> Bool {
        // 新しい頂点をグラフに追加します。
        if _data[vertex] == nil {
            _data[vertex] = []
            return true
        }
        // 既に存在する場合は追加しないがTrueを返す（変更なしでも成功とみなす）
        return true
    }
    
    func addEdge(_ vertex1: String, _ vertex2: String, _ weight: Double) -> Bool {
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
                if neighbors[i].neighbor == vertex1 {
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
    
    func clear() -> Bool {
        // グラフを空にします。
        _data = [:]
        return true
    }
    
    func getShortestPath(_ startVertex: String, _ endVertex: String, _ heuristic: (String, String) -> Double) -> ([String]?, Double) {
        let vertices = getVertices()
        let numVertices = vertices.count
        if numVertices == 0 {
            return (nil, Double.infinity)
        }
        
        // 頂点名をインデックスにマッピング
        var vertexToIndex: [String: Int] = [:]
        var indexToVertex: [Int: String] = [:]
        
        for (index, vertex) in vertices.enumerated() {
            vertexToIndex[vertex] = index
            indexToVertex[index] = vertex
        }
        
        // 開始・終了頂点が存在するか確認
        guard let startIndex = vertexToIndex[startVertex], let endIndex = vertexToIndex[endVertex] else {
            print("ERROR: \(startVertex) または \(endVertex) がグラフに存在しません。")
            return (nil, Double.infinity)
        }
        
        // 距離行列 (dist) と経路復元用行列 (nextNode) を初期化
        let INF = Double.infinity
        var dist = Array(repeating: Array(repeating: INF, count: numVertices), count: numVertices)
        var nextNode = Array(repeating: Array(repeating: nil as Int?, count: numVertices), count: numVertices)
        
        // 初期距離と経路復元情報を設定
        for i in 0..<numVertices {
            dist[i][i] = 0 // 自分自身への距離は0
            if let neighbors = getNeighbors(vertices[i]) {
                for (neighbor, weight) in neighbors {
                    if let j = vertexToIndex[neighbor] {
                        dist[i][j] = weight
                        nextNode[i][j] = j // iからjへの直接辺の場合、iの次はj
                    }
                }
            }
        }
        
        // ワーシャル-フロイド法の本体
        // k: 中継点として使用する頂点のインデックス
        for k in 0..<numVertices {
            // i: 開始頂点のインデックス
            for i in 0..<numVertices {
                // j: 終了頂点のインデックス
                for j in 0..<numVertices {
                    // i -> k -> j の経路が i -> j の現在の経路より短い場合
                    if dist[i][k] != INF && dist[k][j] != INF && dist[i][k] + dist[k][j] < dist[i][j] {
                        dist[i][j] = dist[i][k] + dist[k][j]
                        nextNode[i][j] = nextNode[i][k] // iからjへの最短経路で、iの次の頂点はiからkへの最短経路でのiの次の頂点
                    }
                }
            }
        }
        
        // 指定された開始・終了頂点間の最短経路と重みを取得
        let shortestDistance = dist[startIndex][endIndex]
        
        // 経路が存在しない場合 (距離がINF)
        if shortestDistance == INF {
            return (nil, INF)
        }
        
        // 経路を復元
        var path: [String] = []
        var u = startIndex
        // 開始と終了が同じ場合は経路は開始頂点のみ
        if u == endIndex {
            path = [startVertex]
        } else {
            // nextNodeを使って経路をたどる
            while u != nil && u != endIndex {
                if let vertex = indexToVertex[u] {
                    path.append(vertex)
                }
                
                guard let nextIndex = nextNode[u][endIndex] else {
                    break
                }
                
                // 無限ループ防止のための簡易チェック
                if let lastVertex = path.last, lastVertex == indexToVertex[nextIndex] {
                    print("WARNING: 経路復元中に異常を検出しました（\(indexToVertex[nextIndex] ?? "unknown")でループ？）。")
                    return (nil, INF)
                }
                
                u = nextIndex
            }
            
            // 最後のノード (endVertex) を追加
            path.append(endVertex)
        }
        
        return (path, shortestDistance)
    }
}

// ヒューリスティック関数 (この例では常に0、ダイクストラ法と同じ)
func dummyHeuristic(_ u: String, _ v: String) -> Double {
    // u と v の間に何らかの推定距離を計算する関数
    // ここではダミーとして常に0を返す
    return 0
}

func main() {
    print("WarshallFloyd -----> start")
    
    let graphData = GraphData()
    
    graphData.clear()
    let inputList1: [(String, String, Double)] = [("A", "B", 4), ("B", "C", 3), ("B", "D", 2), ("D", "A", 1), ("A", "C", 2), ("B", "D", 2)]
    for input in inputList1 {
        _ = graphData.addEdge(input.0, input.1, input.2)
    }
    print("\nグラフの頂点:", graphData.getVertices())
    print("グラフの辺 (重み付き):", graphData.getEdges())
    let input1 = ("A", "B")
    let shortestPath1 = graphData.getShortestPath(input1.0, input1.1, dummyHeuristic)
    print("経路\(input1.0)-\(input1.1) の最短経路は \(String(describing: shortestPath1.0)) (重み: \(shortestPath1.1))")
    
    graphData.clear()
    let inputList2: [(String, String, Double)] = [("A", "B", 4), ("C", "D", 4), ("E", "F", 1), ("F", "G", 1)]
    for input in inputList2 {
        _ = graphData.addEdge(input.0, input.1, input.2)
    }
    print("\nグラフの頂点:", graphData.getVertices())
    print("グラフの辺 (重み付き):", graphData.getEdges())
    let input2 = ("A", "B")
    let shortestPath2 = graphData.getShortestPath(input2.0, input2.1, dummyHeuristic)
    print("経路\(input2.0)-\(input2.1) の最短経路は \(String(describing: shortestPath2.0)) (重み: \(shortestPath2.1))")
    
    graphData.clear()
    let inputList3: [(String, String, Double)] = [("A", "B", 4), ("B", "C", 3), ("D", "E", 5)]
    for input in inputList3 {
        _ = graphData.addEdge(input.0, input.1, input.2)
    }
    print("\nグラフの頂点:", graphData.getVertices())
    print("グラフの辺 (重み付き):", graphData.getEdges())
    let input3 = ("A", "D")
    let shortestPath3 = graphData.getShortestPath(input3.0, input3.1, dummyHeuristic)
    print("経路\(input3.0)-\(input3.1) の最短経路は \(String(describing: shortestPath3.0)) (重み: \(shortestPath3.1))")
    
    graphData.clear()
    let inputList4: [(String, String, Double)] = []
    for input in inputList4 {
        _ = graphData.addEdge(input.0, input.1, input.2)
    }
    print("\nグラフの頂点:", graphData.getVertices())
    print("グラフの辺 (重み付き):", graphData.getEdges())
    let input4 = ("A", "B")
    let shortestPath4 = graphData.getShortestPath(input4.0, input4.1, dummyHeuristic)
    print("経路\(input4.0)-\(input4.1) の最短経路は \(String(describing: shortestPath4.0)) (重み: \(shortestPath4.1))")
    
    print("\nWarshallFloyd <----- end")
}

main()