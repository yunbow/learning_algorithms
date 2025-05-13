// Swift
// グラフの連結成分: BFS

import Foundation

class GraphData {
    // 隣接ノードとその辺の重みを格納します。
    // キーは頂点、値はその頂点に隣接する頂点と重みの配列です。
    // 例: ["A": [("B", 1), ("C", 4)], "B": [("A", 1), ("C", 2), ("D", 5)], ... ]
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
                    let sortedEdge = [vertex, neighbor].sorted()
                    result.append((sortedEdge[0], sortedEdge[1], weight))
                }
            }
        }
        return result
    }
    
    func getNeighbors(_ vertex: String) -> [(String, Int)]? {
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
    
    func getVertice(_ vertex: String) -> [(String, Int)]? {
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
        if let neighbors = _data[vertex1], _data[vertex2] != nil {
            // vertex1の隣接リストにvertex2が含まれているかを確認
            return neighbors.contains { neighbor, _ in
                neighbor == vertex2
            }
        } else {
            // どちらかの頂点が存在しない場合は辺も存在しない
            return false
        }
    }
    
    @discardableResult
    func addVertex(_ vertex: String) -> Bool {
        // 新しい頂点をグラフに追加します。
        if _data[vertex] == nil {
            _data[vertex] = []
            return true
        }
        // 既に存在する場合は追加しないがTrueを返す（変更なしでも成功とみなす）
        return true
    }
    
    @discardableResult
    func addEdge(vertex1: String, vertex2: String, weight: Int) -> Bool {
        // 両頂点間に辺を追加します。重みを指定します。
        // 頂点がグラフに存在しない場合は追加します。
        if _data[vertex1] == nil {
            addVertex(vertex1)
        }
        if _data[vertex2] == nil {
            addVertex(vertex2)
        }
        
        // vertex1 -> vertex2 の辺を追加（重み付き）
        var edgeExistsV1V2 = false
        if var neighbors = _data[vertex1] {
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
            _data[vertex1] = neighbors
        }
        
        // vertex2 -> vertex1 の辺を追加（重み付き）
        var edgeExistsV2V1 = false
        if var neighbors = _data[vertex2] {
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
            _data[vertex2] = neighbors
        }
        
        return true
    }
    
    @discardableResult
    func removeVertex(_ vertex: String) -> Bool {
        // 頂点とそれに関連する辺を削除します。
        if _data[vertex] != nil {
            // この頂点への参照を他の頂点の隣接リストから削除する
            for v in _data.keys {
                if var neighbors = _data[v] {
                    neighbors = neighbors.filter { neighbor, _ in
                        neighbor != vertex
                    }
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
    
    @discardableResult
    func removeEdge(vertex1: String, vertex2: String) -> Bool {
        // 両頂点間の辺を削除します。
        if _data[vertex1] != nil && _data[vertex2] != nil {
            var removed = false
            
            // vertex1 から vertex2 への辺を削除
            if var neighbors = _data[vertex1] {
                let originalLenV1 = neighbors.count
                neighbors = neighbors.filter { neighbor, _ in
                    neighbor != vertex2
                }
                if neighbors.count < originalLenV1 {
                    removed = true
                }
                _data[vertex1] = neighbors
            }
            
            // vertex2 から vertex1 への辺を削除
            if var neighbors = _data[vertex2] {
                let originalLenV2 = neighbors.count
                neighbors = neighbors.filter { neighbor, _ in
                    neighbor != vertex1
                }
                if neighbors.count < originalLenV2 {
                    removed = true
                }
                _data[vertex2] = neighbors
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
    
    @discardableResult
    func clear() -> Bool {
        // グラフを空にする
        _data.removeAll()
        return true
    }
    
    func getConnectedComponents() -> [[String]] {
        /**
         グラフの連結成分をBFSを使用して見つけます。
         
         Returns:
            [[String]]: 連結成分のリスト。各要素は連結成分を構成する頂点のリストです。
         */
        var visited = Set<String>()  // 全体の訪問済み頂点を記録するセット
        var allComponents: [[String]] = [] // 見つかった連結成分のリスト
        
        // グラフのすべての頂点を取得
        let vertices = getVertices()
        
        // すべての頂点を順番にチェック
        for vertex in vertices {
            // もしその頂点がまだ訪問されていなければ、新しい連結成分の開始点
            if !visited.contains(vertex) {
                var currentComponent: [String] = [] // 現在探索中の連結成分
                var queue = [vertex] // BFS用のキュー
                visited.insert(vertex) // 開始点を訪問済みにマーク
                currentComponent.append(vertex) // 開始点を現在の成分に追加
                
                // BFSを開始
                var queueIndex = 0
                while queueIndex < queue.count {
                    let u = queue[queueIndex] // キューから頂点を取り出す
                    queueIndex += 1
                    
                    // 取り出した頂点の隣接リストを取得 (重み情報を含む)
                    if let neighborsWithWeight = getNeighbors(u) {
                        // 隣接ノードだけを取り出してループ
                        for (neighbor, _) in neighborsWithWeight {
                            // 隣接する頂点がまだ訪問されていなければ
                            if !visited.contains(neighbor) {
                                visited.insert(neighbor) // 頂点そのものを訪問済みにマーク
                                queue.append(neighbor) // 頂点そのものをキューに追加
                                currentComponent.append(neighbor) // 頂点そのものを現在の成分に追加
                            }
                        }
                    }
                }
                
                // BFSが終了したら、1つの連結成分が見つかった
                allComponents.append(currentComponent)
            }
        }
        
        return allComponents
    }
}

func main() {
    print("Bfs TEST -----> start")
    
    print("\nnew")
    let graphData = GraphData()
    print("  現在のデータ: \(graphData.get())")
    
    print("\nadd_edge")
    graphData.clear()
    let inputList1 = [("A", "B", 4), ("B", "C", 3), ("B", "D", 2), ("D", "A", 1), ("A", "C", 2), ("B", "D", 2)]
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
    graphData.clear()
    let inputList2 = [("A", "B", 4), ("C", "D", 4), ("E", "F", 1), ("F", "G", 1)]
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
    graphData.clear()
    let inputList3 = [("A", "B", 4), ("B", "C", 3), ("D", "E", 5)]
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
    graphData.clear()
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
    
    print("Bfs TEST <----- end")
}

main()