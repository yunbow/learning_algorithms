// Kotlin
// グラフの連結成分: DFS

class GraphData {
    // 隣接ノードとその辺の重みを格納します。
    // キーは頂点、値はその頂点に隣接する頂点と重みのリストです。
    private val _data: MutableMap<String, MutableList<Pair<String, Int>>> = mutableMapOf()

    fun get(): Map<String, List<Pair<String, Int>>> {
        // グラフの内部データを取得します。
        return _data
    }

    fun getVertices(): List<String> {
        // グラフの全頂点をリストとして返します。
        return _data.keys.toList()
    }

    fun getEdges(): List<Triple<String, String, Int>> {
        // グラフの全辺をリストとして返します。
        // 無向グラフの場合、(u, v, weight) の形式で返します。
        // 重複を避けるためにセットを使用します。
        val edges = mutableSetOf<Triple<String, String, Int>>()
        for (vertex in _data.keys) {
            for ((neighbor, weight) in _data[vertex]!!) {
                // 辺を正規化してセットに追加 (小さい方の頂点を最初にするなど)
                val sortedVertices = listOf(vertex, neighbor).sorted()
                edges.add(Triple(sortedVertices[0], sortedVertices[1], weight))
            }
        }
        return edges.toList()
    }

    fun getNeighbors(vertex: String): List<Pair<String, Int>>? {
        // 指定された頂点の隣接ノードと辺の重みのリストを返します。
        // 形式: [(隣接頂点, 重み), ...]
        return _data[vertex]
    }

    fun getEdgeWeight(vertex1: String, vertex2: String): Int? {
        // 指定された2つの頂点間の辺の重みを返します。
        // 辺が存在しない場合はnullを返します。
        if (vertex1 in _data && vertex2 in _data) {
            for ((neighbor, weight) in _data[vertex1]!!) {
                if (neighbor == vertex2) {
                    return weight
                }
            }
        }
        return null // 辺が存在しない場合
    }

    fun getVertice(vertex: String): List<Pair<String, Int>>? {
        // 頂点がグラフに存在するか確認する
        return if (vertex in _data) {
            // 存在する場合は、その頂点の隣接リスト（関連する値）を返す
            _data[vertex]
        } else {
            // 存在しない場合はメッセージを表示し、nullを返す
            println("ERROR: ${vertex}は範囲外です")
            null
        }
    }

    fun getEdge(vertex1: String, vertex2: String): Boolean {
        // 指定された2つの頂点間に辺が存在するかを確認する
        // 両方の頂点がグラフに存在する必要がある
        if (vertex1 in _data && vertex2 in _data) {
            // vertex1の隣接リストにvertex2が含まれているかを確認
            return _data[vertex1]!!.any { it.first == vertex2 }
        }
        // どちらかの頂点が存在しない場合は辺も存在しない
        return false
    }

    fun addVertex(vertex: String): Boolean {
        // 新しい頂点をグラフに追加します。
        if (vertex !in _data) {
            _data[vertex] = mutableListOf()
            return true
        }
        // 既に存在する場合は追加しないがTrueを返す（変更なしでも成功とみなす）
        return true
    }

    fun addEdge(vertex1: String, vertex2: String, weight: Int): Boolean {
        // 両頂点間に辺を追加します。重みを指定します。
        // 頂点がグラフに存在しない場合は追加します。
        if (vertex1 !in _data) {
            addVertex(vertex1)
        }
        if (vertex2 !in _data) {
            addVertex(vertex2)
        }

        // 両方向に辺を追加する（無向グラフ）
        // vertex1 -> vertex2 の辺を追加（重み付き）
        var edgeExistsV1V2 = false
        for (i in _data[vertex1]!!.indices) {
            if (_data[vertex1]!![i].first == vertex2) {
                _data[vertex1]!![i] = Pair(vertex2, weight) // 既に存在する場合は重みを更新
                edgeExistsV1V2 = true
                break
            }
        }
        if (!edgeExistsV1V2) {
            _data[vertex1]!!.add(Pair(vertex2, weight))
        }

        // vertex2 -> vertex1 の辺を追加（重み付き）
        var edgeExistsV2V1 = false
        for (i in _data[vertex2]!!.indices) {
            if (_data[vertex2]!![i].first == vertex1) {
                _data[vertex2]!![i] = Pair(vertex1, weight) // 既に存在する場合は重みを更新
                edgeExistsV2V1 = true
                break
            }
        }
        if (!edgeExistsV2V1) {
            _data[vertex2]!!.add(Pair(vertex1, weight))
        }

        return true
    }

    fun removeVertex(vertex: String): Boolean {
        // 頂点とそれに関連する辺を削除します。
        if (vertex in _data) {
            // この頂点への参照を他の頂点の隣接リストから削除する
            for (v in _data.keys) {
                _data[v] = _data[v]!!.filter { it.first != vertex }.toMutableList()
            }
            // 頂点自体を削除する
            _data.remove(vertex)
            return true
        } else {
            println("ERROR: $vertex は範囲外です")
            return false
        }
    }

    fun removeEdge(vertex1: String, vertex2: String): Boolean {
        // 両頂点間の辺を削除します。
        if (vertex1 in _data && vertex2 in _data) {
            var removed = false
            // vertex1 から vertex2 への辺を削除
            val originalLenV1 = _data[vertex1]!!.size
            _data[vertex1] = _data[vertex1]!!.filter { it.first != vertex2 }.toMutableList()
            if (_data[vertex1]!!.size < originalLenV1) {
                removed = true
            }

            // vertex2 から vertex1 への辺を削除
            val originalLenV2 = _data[vertex2]!!.size
            _data[vertex2] = _data[vertex2]!!.filter { it.first != vertex1 }.toMutableList()
            if (_data[vertex2]!!.size < originalLenV2) {
                removed = true
            }

            return removed // 少なくとも片方向が削除されたか
        } else {
            println("ERROR: $vertex1 または $vertex2 は範囲外です")
            return false
        }
    }

    fun isEmpty(): Boolean {
        // グラフが空かどうか
        return _data.isEmpty()
    }

    fun size(): Int {
        // グラフの頂点数を返す
        return _data.size
    }

    fun clear(): Boolean {
        // グラフを空にする
        _data.clear()
        return true
    }

    private fun _dfs(vertex: String, visited: MutableSet<String>, currentComponent: MutableList<String>) {
        // 現在の頂点を訪問済みにマークし、現在の成分に追加
        visited.add(vertex)
        currentComponent.add(vertex)

        // 隣接する頂点を探索
        // _data[vertex] ?: emptyList() は、vertexが存在しない場合でも
        // エラーにならず空リストを返す安全な方法
        for (neighborInfo in _data[vertex] ?: emptyList()) {
            // neighborInfo は (neighbor, weight) のペアなので、
            // ペアの1番目の要素（隣接頂点そのもの）を取得します。
            val neighborVertex = neighborInfo.first

            // まだ訪問していない隣接頂点に対して再帰的にDFSを呼び出す
            if (neighborVertex !in visited) {
                _dfs(neighborVertex, visited, currentComponent)
            }
        }
    }

    fun getConnectedComponents(): List<List<String>> {
        val visited = mutableSetOf<String>() // 訪問済み頂点を記録するセット
        val connectedComponents = mutableListOf<List<String>>() // 連結成分を格納するリスト

        // グラフの全頂点を順にチェック
        for (vertex in getVertices()) {
            // まだ訪問していない頂点からDFSを開始
            if (vertex !in visited) {
                val currentComponent = mutableListOf<String>() // 現在の連結成分を格納するリスト
                // DFSヘルパー関数を呼び出し、現在の連結成分を探索
                _dfs(vertex, visited, currentComponent)
                // 探索で見つかった連結成分を結果リストに追加
                connectedComponents.add(currentComponent)
            }
        }

        return connectedComponents
    }
}

fun main() {
    println("Dfs TEST -----> start")

    println("\nnew")
    val graphData = GraphData()
    println("  現在のデータ: ${graphData.get()}")

    println("\nadd_edge")
    graphData.clear()
    val inputList1 = listOf(
        Triple("A", "B", 4), Triple("B", "C", 3), Triple("B", "D", 2),
        Triple("D", "A", 1), Triple("A", "C", 2), Triple("B", "D", 2)
    )
    for (input in inputList1) {
        println("  入力値: $input")
        val output = graphData.addEdge(input.first, input.second, input.third)
        println("  出力値: $output")
    }
    println("  現在のデータ: ${graphData.get()}")
    println("\nget_connected_components")
    val output1 = graphData.getConnectedComponents()
    println("  連結成分: $output1")

    println("\nadd_edge")
    graphData.clear()
    val inputList2 = listOf(
        Triple("A", "B", 4), Triple("C", "D", 4),
        Triple("E", "F", 1), Triple("F", "G", 1)
    )
    for (input in inputList2) {
        println("  入力値: $input")
        val output = graphData.addEdge(input.first, input.second, input.third)
        println("  出力値: $output")
    }
    println("  現在のデータ: ${graphData.get()}")
    println("\nget_connected_components")
    val output2 = graphData.getConnectedComponents()
    println("  連結成分: $output2")

    println("\nadd_edge")
    graphData.clear()
    val inputList3 = listOf(
        Triple("A", "B", 4), Triple("B", "C", 3), Triple("D", "E", 5)
    )
    for (input in inputList3) {
        println("  入力値: $input")
        val output = graphData.addEdge(input.first, input.second, input.third)
        println("  出力値: $output")
    }
    println("  現在のデータ: ${graphData.get()}")
    println("\nget_connected_components")
    val output3 = graphData.getConnectedComponents()
    println("  連結成分: $output3")

    println("\nadd_edge")
    graphData.clear()
    val inputList4 = listOf<Triple<String, String, Int>>()
    for (input in inputList4) {
        println("  入力値: $input")
        val output = graphData.addEdge(input.first, input.second, input.third)
        println("  出力値: $output")
    }
    println("  現在のデータ: ${graphData.get()}")
    println("\nget_connected_components")
    val output4 = graphData.getConnectedComponents()
    println("  連結成分: $output4")

    println("Dfs TEST <----- end")
}