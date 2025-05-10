// Kotlin
// データ構造: グラフ (Graph)

class GraphData {
    // 隣接ノードとその辺の重みを格納します。
    // キーは頂点、値はその頂点に隣接する頂点と重みのリストです。
    private var data: MutableMap<String, MutableList<Pair<String, Int>>> = mutableMapOf()

    fun get(): Map<String, List<Pair<String, Int>>> {
        // グラフの内部データを取得します。
        return data
    }

    fun getVertices(): List<String> {
        // グラフの全頂点をリストとして返します。
        return data.keys.toList()
    }

    fun getEdges(): List<Triple<String, String, Int>> {
        // グラフの全辺をリストとして返します。
        // 無向グラフの場合、(u, v, weight) の形式で返します。
        // 重複を避けるためにセットを使用します。
        val edges = mutableSetOf<Triple<String, String, Int>>()
        for (vertex in data.keys) {
            for ((neighbor, weight) in data[vertex]!!) {
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
        return data[vertex]
    }

    fun getEdgeWeight(vertex1: String, vertex2: String): Int? {
        // 指定された2つの頂点間の辺の重みを返します。
        // 辺が存在しない場合はnullを返します。
        if (vertex1 in data && vertex2 in data) {
            for ((neighbor, weight) in data[vertex1]!!) {
                if (neighbor == vertex2) {
                    return weight
                }
            }
        }
        return null // 辺が存在しない場合
    }

    fun getVertice(vertex: String): List<Pair<String, Int>>? {
        // 頂点がグラフに存在するか確認する
        return if (vertex in data) {
            // 存在する場合は、その頂点の隣接リスト（関連する値）を返す
            data[vertex]
        } else {
            // 存在しない場合はメッセージを表示し、nullを返す
            println("ERROR: ${vertex}は範囲外です")
            null
        }
    }

    fun getEdge(vertex1: String, vertex2: String): Boolean {
        // 指定された2つの頂点間に辺が存在するかを確認する
        // 両方の頂点がグラフに存在する必要がある
        if (vertex1 in data && vertex2 in data) {
            // vertex1の隣接リストにvertex2が含まれているかを確認
            return data[vertex1]!!.any { it.first == vertex2 }
        }
        return false // どちらかの頂点が存在しない場合は辺も存在しない
    }

    fun addVertex(vertex: String): Boolean {
        // 新しい頂点をグラフに追加します。
        if (vertex !in data) {
            data[vertex] = mutableListOf()
        }
        // 既に存在する場合は追加しないがTrueを返す（変更なしでも成功とみなす）
        return true
    }

    fun addEdge(vertex1: String, vertex2: String, weight: Int): Boolean {
        // 両頂点間に辺を追加します。重みを指定します。
        // 頂点がグラフに存在しない場合は追加します。
        if (vertex1 !in data) {
            addVertex(vertex1)
        }
        if (vertex2 !in data) {
            addVertex(vertex2)
        }

        // 両方向に辺を追加する（無向グラフ）
        // 既に同じ辺が存在する場合は重みを更新する

        // vertex1 -> vertex2 の辺を追加（重み付き）
        var edgeExistsV1V2 = false
        for (i in data[vertex1]!!.indices) {
            if (data[vertex1]!![i].first == vertex2) {
                data[vertex1]!![i] = Pair(vertex2, weight) // 既に存在する場合は重みを更新
                edgeExistsV1V2 = true
                break
            }
        }
        if (!edgeExistsV1V2) {
            data[vertex1]!!.add(Pair(vertex2, weight))
        }

        // vertex2 -> vertex1 の辺を追加（重み付き）
        var edgeExistsV2V1 = false
        for (i in data[vertex2]!!.indices) {
            if (data[vertex2]!![i].first == vertex1) {
                data[vertex2]!![i] = Pair(vertex1, weight) // 既に存在する場合は重みを更新
                edgeExistsV2V1 = true
                break
            }
        }
        if (!edgeExistsV2V1) {
            data[vertex2]!!.add(Pair(vertex1, weight))
        }

        return true
    }

    fun removeVertex(vertex: String): Boolean {
        // 頂点とそれに関連する辺を削除する
        if (vertex in data) {
            // この頂点への参照を他の頂点の隣接リストから削除する
            for (v in data.keys) {
                data[v] = data[v]!!.filter { it.first != vertex }.toMutableList()
            }
            // 頂点自体を削除する
            data.remove(vertex)
            return true
        } else {
            println("ERROR: ${vertex} は範囲外です")
            return false
        }
    }

    fun removeEdge(vertex1: String, vertex2: String): Boolean {
        // 両頂点間の辺を削除します。
        if (vertex1 in data && vertex2 in data) {
            var removed = false
            // vertex1 から vertex2 への辺を削除
            val originalLenV1 = data[vertex1]!!.size
            data[vertex1] = data[vertex1]!!.filter { it.first != vertex2 }.toMutableList()
            if (data[vertex1]!!.size < originalLenV1) {
                removed = true
            }

            // vertex2 から vertex1 への辺を削除
            val originalLenV2 = data[vertex2]!!.size
            data[vertex2] = data[vertex2]!!.filter { it.first != vertex1 }.toMutableList()
            if (data[vertex2]!!.size < originalLenV2) {
                removed = true
            }

            return removed // 少なくとも片方向が削除されたか
        } else {
            println("ERROR: ${vertex1} または ${vertex2} は範囲外です")
            return false
        }
    }

    fun isEmpty(): Boolean {
        // グラフが空かどうか
        return data.isEmpty()
    }

    fun size(): Int {
        // グラフの頂点数を返す
        return data.size
    }

    fun clear(): Boolean {
        // グラフを空にする
        data.clear()
        return true
    }
}

fun main() {
    println("Graph TEST -----> start")

    println("\nnew")
    val graphData = GraphData()
    println("  現在のデータ: ${graphData.get()}")

    println("\nis_empty")
    val emptyOutput = graphData.isEmpty()
    println("  出力値: $emptyOutput")

    println("\nsize")
    val sizeOutput = graphData.size()
    println("  出力値: $sizeOutput")

    val vertexList = listOf("A", "B", "C")
    for (input in vertexList) {
        println("\nadd_vertex")
        println("  入力値: $input")
        val output = graphData.addVertex(input)
        println("  出力値: $output")
    }
    println("  現在のデータ: ${graphData.get()}")

    println("\nget_vertices")
    val verticesOutput = graphData.getVertices()
    println("  出力値: $verticesOutput")

    println("\nsize")
    val newSizeOutput = graphData.size()
    println("  出力値: $newSizeOutput")

    println("\nadd_edge")
    val edgeList = listOf(Triple("A", "B", 4), Triple("B", "C", 2), Triple("C", "A", 3))
    for (input in edgeList) {
        println("  入力値: $input")
        val output = graphData.addEdge(input.first, input.second, input.third)
        println("  出力値: $output")
    }
    println("  現在のデータ: ${graphData.get()}")

    println("\nget_vertices")
    val verticesOutput2 = graphData.getVertices()
    println("  出力値: $verticesOutput2")

    println("\nget_edges")
    val edgesOutput = graphData.getEdges()
    println("  出力値: $edgesOutput")

    println("\nsize")
    val sizeOutput2 = graphData.size()
    println("  出力値: $sizeOutput2")

    println("\nget_vertice")
    val vertexInput = "B"
    println("  入力値: '$vertexInput'")
    val vertexOutput = graphData.getVertice(vertexInput)
    println("  出力値: $vertexOutput")

    println("\nget_vertice")
    val vertexInput2 = "E"
    println("  入力値: '$vertexInput2'")
    val vertexOutput2 = graphData.getVertice(vertexInput2)
    println("  出力値: $vertexOutput2")

    println("\nremove_edge")
    val edgeRemoveInput = Pair("A", "B")
    println("  入力値: $edgeRemoveInput")
    val edgeRemoveOutput = graphData.removeEdge(edgeRemoveInput.first, edgeRemoveInput.second)
    println("  出力値: $edgeRemoveOutput")
    println("  現在のデータ: ${graphData.get()}")

    println("\nremove_edge")
    val edgeRemoveInput2 = Pair("A", "C")
    println("  入力値: $edgeRemoveInput2")
    val edgeRemoveOutput2 = graphData.removeEdge(edgeRemoveInput2.first, edgeRemoveInput2.second)
    println("  出力値: $edgeRemoveOutput2")
    println("  現在のデータ: ${graphData.get()}")

    println("\nget_edges")
    val edgesOutput2 = graphData.getEdges()
    println("  出力値: $edgesOutput2")

    println("\nremove_vertex")
    val vertexRemoveInput = "B"
    println("  入力値: $vertexRemoveInput")
    val vertexRemoveOutput = graphData.removeVertex(vertexRemoveInput)
    println("  出力値: $vertexRemoveOutput")
    println("  現在のデータ: ${graphData.get()}")

    println("\nremove_vertex")
    val vertexRemoveInput2 = "Z"
    println("  入力値: $vertexRemoveInput2")
    val vertexRemoveOutput2 = graphData.removeVertex(vertexRemoveInput2)
    println("  出力値: $vertexRemoveOutput2")
    println("  現在のデータ: ${graphData.get()}")

    println("\nget_vertices")
    val verticesOutput3 = graphData.getVertices()
    println("  出力値: $verticesOutput3")

    println("\nget_edges")
    val edgesOutput3 = graphData.getEdges()
    println("  出力値: $edgesOutput3")

    println("\nsize")
    val sizeOutput3 = graphData.size()
    println("  出力値: $sizeOutput3")

    println("\nget_vertice")
    val vertexInput3 = "B"
    println("  入力値: $vertexInput3")
    val vertexOutput3 = graphData.getVertice(vertexInput3)
    println("  出力値: $vertexOutput3")

    println("\nclear")
    val clearOutput = graphData.clear()
    println("  出力値: $clearOutput")

    println("\nis_empty")
    val isEmptyOutput = graphData.isEmpty()
    println("  出力値: $isEmptyOutput")

    println("\nsize")
    val sizeOutput4 = graphData.size()
    println("  出力値: $sizeOutput4")

    println("\nget_vertices")
    val verticesOutput4 = graphData.getVertices()
    println("  出力値: $verticesOutput4")

    println("\nget_edges")
    val edgesOutput4 = graphData.getEdges()
    println("  出力値: $edgesOutput4")

    println("\nGraph TEST <----- end")
}