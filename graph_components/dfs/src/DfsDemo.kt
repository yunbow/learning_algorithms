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