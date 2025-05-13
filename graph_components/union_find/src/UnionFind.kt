// Kotlin
// グラフの連結成分: Union-Find

class GraphData {
    // 隣接ノードとその辺の重みを格納します。
    // キーは頂点、値はその頂点に隣接する頂点と重みのリストです。
    private val _data = mutableMapOf<String, MutableList<Pair<String, Int>>>()

    fun get(): Map<String, List<Pair<String, Int>>> {
        return _data
    }

    fun getVertices(): List<String> {
        return _data.keys.toList()
    }

    fun getEdges(): List<Triple<String, String, Int>> {
        val edges = mutableSetOf<Triple<String, String, Int>>()
        for (vertex in _data) {
            for ((neighbor, weight) in vertex.value) {
                // 辺を正規化してセットに追加（小さい方の頂点を最初にするなど）
                val sortedVertices = listOf(vertex.key, neighbor).sorted()
                edges.add(Triple(sortedVertices[0], sortedVertices[1], weight))
            }
        }
        return edges.toList()
    }

    fun getNeighbors(vertex: String): List<Pair<String, Int>>? {
        return _data[vertex]
    }

    fun getEdgeWeight(vertex1: String, vertex2: String): Int? {
        if (vertex1 in _data && vertex2 in _data) {
            for ((neighbor, weight) in _data[vertex1]!!) {
                if (neighbor == vertex2) {
                    return weight
                }
            }
        }
        return null
    }

    fun getVertice(vertex: String): List<Pair<String, Int>>? {
        return if (vertex in _data) {
            _data[vertex]
        } else {
            println("ERROR: ${vertex}は範囲外です")
            null
        }
    }

    fun getEdge(vertex1: String, vertex2: String): Boolean {
        if (vertex1 in _data && vertex2 in _data) {
            return _data[vertex1]!!.any { it.first == vertex2 }
        }
        return false
    }

    fun addVertex(vertex: String): Boolean {
        if (vertex !in _data) {
            _data[vertex] = mutableListOf()
        }
        return true
    }

    fun addEdge(vertex1: String, vertex2: String, weight: Int): Boolean {
        if (vertex1 !in _data) {
            addVertex(vertex1)
        }
        if (vertex2 !in _data) {
            addVertex(vertex2)
        }

        // vertex1 -> vertex2 の辺を追加（重み付き）
        val edgeExistsV1V2 = _data[vertex1]!!.indexOfFirst { it.first == vertex2 }
        if (edgeExistsV1V2 != -1) {
            _data[vertex1]!![edgeExistsV1V2] = Pair(vertex2, weight)
        } else {
            _data[vertex1]!!.add(Pair(vertex2, weight))
        }

        // vertex2 -> vertex1 の辺を追加（重み付き）
        val edgeExistsV2V1 = _data[vertex2]!!.indexOfFirst { it.first == vertex1 }
        if (edgeExistsV2V1 != -1) {
            _data[vertex2]!![edgeExistsV2V1] = Pair(vertex1, weight)
        } else {
            _data[vertex2]!!.add(Pair(vertex1, weight))
        }

        return true
    }

    fun removeVertex(vertex: String): Boolean {
        if (vertex in _data) {
            // この頂点への参照を他の頂点の隣接リストから削除する
            for (v in _data.keys) {
                _data[v] = _data[v]!!.filter { it.first != vertex }.toMutableList()
            }
            // 頂点自体を削除する
            _data.remove(vertex)
            return true
        } else {
            println("ERROR: ${vertex}は範囲外です")
            return false
        }
    }

    fun removeEdge(vertex1: String, vertex2: String): Boolean {
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

            return removed
        } else {
            println("ERROR: ${vertex1}または${vertex2}は範囲外です")
            return false
        }
    }

    fun isEmpty(): Boolean {
        return _data.isEmpty()
    }

    fun size(): Int {
        return _data.size
    }

    fun clear(): Boolean {
        _data.clear()
        return true
    }

    fun getConnectedComponents(): List<List<String>> {
        if (_data.isEmpty()) {
            return emptyList()
        }

        val parent = mutableMapOf<String, String>()
        val size = mutableMapOf<String, Int>()

        val vertices = getVertices()
        for (vertex in vertices) {
            parent[vertex] = vertex
            size[vertex] = 1
        }

        fun find(v: String): String {
            if (parent[v] != v) {
                parent[v] = find(parent[v]!!)
            }
            return parent[v]!!
        }

        fun union(u: String, v: String): Boolean {
            val rootU = find(u)
            val rootV = find(v)

            if (rootU != rootV) {
                if ((size[rootU] ?: 0) < (size[rootV] ?: 0)) {
                    parent[rootU] = rootV
                    size[rootV] = (size[rootV] ?: 0) + (size[rootU] ?: 0)
                } else {
                    parent[rootV] = rootU
                    size[rootU] = (size[rootU] ?: 0) + (size[rootV] ?: 0)
                }
                return true
            }
            return false
        }

        for ((u, v, _) in getEdges()) {
            union(u, v)
        }

        val components = mutableMapOf<String, MutableList<String>>()
        for (vertex in vertices) {
            val root = find(vertex)
            if (root !in components) {
                components[root] = mutableListOf()
            }
            components[root]!!.add(vertex)
        }

        return components.values.toList()
    }
}

fun main() {
    println("UnionFind TEST -----> start")

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

    println("\nUnionFind TEST <----- end")
}