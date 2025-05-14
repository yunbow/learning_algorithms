// Swift
// グラフの最短経路: ベルマンフォード法 (Bellman Ford)

import Foundation

class GraphData {
    // 隣接ノードとその辺の重みを格納します。
    // キーは頂点、値はその頂点に隣接する頂点と重みのリストです。
    private var _data: [String: [(neighbor: String, weight: Double)]] = [:]
    
    func get() -> [String: [(neighbor: String, weight: Double)]] {
        // グラフの内部データを取得します。
        return _data
    }
    
    func getVertices() -> [String] {
        // グラフの全頂点をリストとして返します。
        return Array(_data.keys)
    }
    
    func getEdges() -> [(String, String, Double)] {
        // グラフの全辺をリストとして返します。
        // ベルマン-フォード法で使用するため、内部データ (_data) から有向辺として抽出します。
        // 各辺は (出発頂点, 到着頂点, 重み) のタプルになります。
        var edges: [(String, String, Double)] = []
        for (u, neighbors) in _data {
            for (v, weight) in neighbors {
                edges.append((u, v, weight))
            }
        }
        return edges
    }
        
    @discardableResult
    func addVertex(_ vertex: String) -> Bool {
        // 新しい頂点をグラフに追加します。
        if _data[vertex] == nil {
            _data[vertex] = []
            return true
        }
        return true // 既に存在する場合は追加しないがTrueを返す
    }
    
    @discardableResult
    func addEdge(_ vertex1: String, _ vertex2: String, _ weight: Double) -> Bool {
        // 両頂点間に辺を追加します。重みを指定します。
        // 頂点がグラフに存在しない場合は追加します。
        addVertex(vertex1)
        addVertex(vertex2)
        
        // vertex1 -> vertex2 の辺を追加（重み付き）
        // 既に同じ頂点間の辺が存在する場合は重みを更新
        var edgeUpdatedV1V2 = false
        if var neighbors = _data[vertex1] {
            for i in 0..<neighbors.count {
                if neighbors[i].neighbor == vertex2 {
                    neighbors[i] = (vertex2, weight)
                    edgeUpdatedV1V2 = true
                    break
                }
            }
            if !edgeUpdatedV1V2 {
                neighbors.append((vertex2, weight))
            }
            _data[vertex1] = neighbors
        }
        
        // vertex2 -> vertex1 の辺を追加（重み付き）
        // 既に同じ頂点間の辺が存在する場合は重みを更新
        var edgeUpdatedV2V1 = false
        if var neighbors = _data[vertex2] {
            for i in 0..<neighbors.count {
                if neighbors[i].neighbor == vertex1 {
                    neighbors[i] = (vertex1, weight)
                    edgeUpdatedV2V1 = true
                    break
                }
            }
            if !edgeUpdatedV2V1 {
                neighbors.append((vertex1, weight))
            }
            _data[vertex2] = neighbors
        }
        
        return true
    }
    
    func isEmpty() -> Bool {
        // グラフが空かどうかを返します。
        return _data.isEmpty
    }
        
    @discardableResult
    func clear() -> Bool {
        // グラフを空にします。
        _data.removeAll()
        return true
    }
    
    func getShortestPath(_ startVertex: String, _ endVertex: String, _ heuristic: (String, String) -> Double) -> ([String], Double) {
        let vertices = getVertices()
        let edges = getEdges() // 有向辺のリストを取得
        let numVertices = vertices.count
        
        // 始点と終点の存在チェック
        guard vertices.contains(startVertex) else {
            print("エラー: 始点 '\(startVertex)' がグラフに存在しません。")
            return ([], Double.infinity)
        }
        
        guard vertices.contains(endVertex) else {
            print("エラー: 終点 '\(endVertex)' がグラフに存在しません。")
            return ([], Double.infinity)
        }
        
        // 始点と終点が同じ場合
        if startVertex == endVertex {
            return ([startVertex], 0)
        }
        
        // 距離と先行頂点の初期化
        // dist: 始点からの最短距離を格納
        // pred: 最短経路における各頂点の先行頂点を格納
        var dist: [String: Double] = [:]
        var pred: [String: String?] = [:]
        
        for vertex in vertices {
            dist[vertex] = Double.infinity
            pred[vertex] = nil
        }
        
        dist[startVertex] = 0 // 始点自身の距離は0
        
        // |V| - 1 回の緩和ステップを実行
        // このループの後、負閉路が存在しない場合は全ての頂点への最短距離が確定している
        for _ in 0..<(numVertices - 1) {
            // 緩和が一度も行われなかった場合にループを中断するためのフラグ
            var relaxedInThisIteration = false
            
            for (u, v, weight) in edges {
                // dist[u] が無限大でない場合のみ緩和を試みる（到達不可能な頂点からの緩和は意味がない）
                if let distU = dist[u], let distV = dist[v], distU != Double.infinity, distU + weight < distV {
                    dist[v] = distU + weight
                    pred[v] = u
                    relaxedInThisIteration = true
                }
            }
            
            // このイテレーションで緩和が行われなかった場合は、それ以上距離が更新されることはないのでループを抜ける
            if !relaxedInThisIteration {
                break
            }
        }
        
        // 負閉路の検出
        // もう一度全ての辺に対して緩和を試みる。
        // ここでさらに距離が更新される辺が存在する場合、その辺は負閉路の一部であるか、
        // 負閉路から到達可能な頂点への辺である。
        // 終点が負閉路から到達可能な場合、終点までの最短距離は無限小になるため定義できない。
        for (u, v, weight) in edges {
            if let distU = dist[u], let distV = dist[v], distU != Double.infinity, distU + weight < distV {
                // 負閉路が存在します。
                // 終点がこの負閉路から到達可能であれば、最短経路は存在しません。
                // 厳密には終点への到達可能性をチェックすべきですが、ベルマン・フォード法では負閉路自体の検出をもって最短経路定義不可とすることが一般的です。
                print("エラー: グラフに負閉路が存在します。最短経路は定義できません。")
                return (nil, -Double.infinity) // 負の無限大を返すことで、距離が無限小になることを示す
            }
        }
        
        // 最短経路の構築
        var path: [String] = []
        var current: String? = endVertex
        
        // 終点まで到達不可能かチェック (距離が初期値のままか)
        if dist[endVertex] == Double.infinity {
            return ([], Double.infinity) // 到達不可能
        }
        
        // 終点から先行頂点をたどって経路を逆順に構築
        while let currentVertex = current {
            path.append(currentVertex)
            // 始点に到達したらループを終了
            if currentVertex == startVertex {
                break
            }
            // 次の頂点に進む
            current = pred[currentVertex] ?? nil
        }
        
        // 経路が始点から始まっていない場合 (通常は到達不可能な場合に含まれるはずだが、念のため)
        // pathリストの最後の要素がstartVertexであることを確認
        if path.isEmpty || path.last != startVertex {
            // このケースは dist[endVertex] == Double.infinity で既に処理されているはずだが、念のため
            // または、負閉路検出後に到達不可能と判断されるケースもありうる
            return ([], Double.infinity)
        }
        
        path.reverse() // 経路を始点から終点の順にする
        
        return (path, dist[endVertex] ?? Double.infinity)
    }
}

// ヒューリスティック関数 (ベルマン-フォード法では使用しないが、元のコードに合わせた引数のために残す)
func dummyHeuristic(_ u: String, _ v: String) -> Double {
    // u と v の間に何らかの推定距離を計算する関数
    // ここではベルマン-フォード法では使用しないため、常に0を返す
    return 0
}

func main() {
    print("BellmanFord TEST -----> start")
    
    let graphData = GraphData()
    
    graphData.clear()
    let inputList1: [(String, String, Double)] = [("A", "B", 4), ("B", "C", 3), ("B", "D", 2), ("D", "A", 1), ("A", "C", 2), ("B", "D", 2)]
    for input in inputList1 {
        graphData.addEdge(input.0, input.1, input.2)
    }
    print("\nグラフの頂点:", graphData.getVertices())
    print("グラフの辺 (重み付き):", graphData.getEdges())
    let input1 = ("A", "B")
    let shortestPath1 = graphData.getShortestPath(input1.0, input1.1, dummyHeuristic)
    print("経路\(input1.0)-\(input1.1) の最短経路は \(shortestPath1.0) (重み: \(shortestPath1.1))")
    
    graphData.clear()
    let inputList2: [(String, String, Double)] = [("A", "B", 4), ("C", "D", 4), ("E", "F", 1), ("F", "G", 1)]
    for input in inputList2 {
        graphData.addEdge(input.0, input.1, input.2)
    }
    print("\nグラフの頂点:", graphData.getVertices())
    print("グラフの辺 (重み付き):", graphData.getEdges())
    let input2 = ("A", "B")
    let shortestPath2 = graphData.getShortestPath(input2.0, input2.1, dummyHeuristic)
    print("経路\(input2.0)-\(input2.1) の最短経路は \(shortestPath2.0) (重み: \(shortestPath2.1))")
    
    graphData.clear()
    let inputList3: [(String, String, Double)] = [("A", "B", 4), ("B", "C", 3), ("D", "E", 5)]
    for input in inputList3 {
        graphData.addEdge(input.0, input.1, input.2)
    }
    print("\nグラフの頂点:", graphData.getVertices())
    print("グラフの辺 (重み付き):", graphData.getEdges())
    let input3 = ("A", "D")
    let shortestPath3 = graphData.getShortestPath(input3.0, input3.1, dummyHeuristic)
    print("経路\(input3.0)-\(input3.1) の最短経路は \(shortestPath3.0) (重み: \(shortestPath3.1))")
    
    graphData.clear()
    let inputList4: [(String, String, Double)] = []
    for input in inputList4 {
        graphData.addEdge(input.0, input.1, input.2)
    }
    print("\nグラフの頂点:", graphData.getVertices())
    print("グラフの辺 (重み付き):", graphData.getEdges())
    let input4 = ("A", "B")
    let shortestPath4 = graphData.getShortestPath(input4.0, input4.1, dummyHeuristic)
    print("経路\(input4.0)-\(input4.1) の最短経路は \(shortestPath4.0) (重み: \(shortestPath4.1))")
    
    print("\nBellmanFord TEST <----- end")
}

main()