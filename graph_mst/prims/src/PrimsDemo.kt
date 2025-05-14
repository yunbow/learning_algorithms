// Kotlin
// グラフの最小全域木: プリム法 (Prim)

import java.util.*

class GraphData {
    // 隣接リストとしてグラフデータを格納します。
    // キーは頂点、値はその頂点に隣接する頂点とその辺の重みのペアのリストです。
    private val data = mutableMapOf<String, MutableList<Pair<String, Int>>>()

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

    fun addVertex(vertex: String): Boolean {
        // 新しい頂点をグラフに追加します。
        if (vertex !in data) {
            data[vertex] = mutableListOf()
            return true
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
        // vertex1 -> vertex2 の辺を追加（重み付き）
        var edgeExistsV1V2 = false
        for (i in data[vertex1]!!.indices) {
            val (neighbor, _) = data[vertex1]!![i]
            if (neighbor == vertex2) {
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
            val (neighbor, _) = data[vertex2]!![i]
            if (neighbor == vertex1) {
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

    fun isEmpty(): Boolean {
        // グラフが空かどうかを返します。
        return data.isEmpty()
    }

    fun clear(): Boolean {
        // グラフを空にします。
        data.clear()
        return true
    }

    fun getMst(startVertex: String? = null): List<Triple<String, String, Int>> {
        val vertices = getVertices()
        if (vertices.isEmpty()) {
            return emptyList() // グラフが空
        }

        val actualStartVertex = startVertex ?: vertices[0]
        if (actualStartVertex !in data) {
            println("ERROR: 開始頂点 $actualStartVertex はグラフに存在しません。")
            return emptyList()
        }

        // MSTに含まれる頂点のセット
        val inMst = mutableSetOf<String>()
        // 優先度付きキュー (重み, 現在の頂点, 遷移元の頂点)
        // PriorityQueueは最小ヒープなので、重みが小さい辺から取り出される
        val minHeap = PriorityQueue<Triple<Int, String, String?>> { a, b -> a.first.compareTo(b.first) }
        // MSTを構成する辺のリスト
        val mstEdges = mutableListOf<Triple<String, String, Int>>()
        // 各頂点への最小コスト（MSTに追加する際の辺の重み）と、その遷移元の頂点を記録
        val minCost = vertices.associateWith { Double.POSITIVE_INFINITY }.toMutableMap()
        val parent = vertices.associateWith { null as String? }.toMutableMap()

        // 開始頂点の処理
        minCost[actualStartVertex] = 0.0
        minHeap.add(Triple(0, actualStartVertex, null)) // (コスト, 現在の頂点, 遷移元の頂点)

        while (minHeap.isNotEmpty()) {
            // 最小コストの辺を持つ頂点を取り出す
            val (cost, currentVertex, fromVertex) = minHeap.poll()

            // 既にMSTに含まれている頂点であればスキップ
            if (currentVertex in inMst) {
                continue
            }

            // 現在の頂点をMSTに追加
            inMst.add(currentVertex)

            // MSTに追加された辺を記録 (開始頂点以外)
            if (fromVertex != null) {
                // fromVertex から currentVertex への辺の重みを取得
                val weight = getEdgeWeight(fromVertex, currentVertex)
                if (weight != null) {
                    val sortedVertices = listOf(fromVertex, currentVertex).sorted()
                    mstEdges.add(Triple(sortedVertices[0], sortedVertices[1], weight)) // 辺を正規化して追加
                }
            }

            // 現在の頂点に隣接する頂点を調べ、MSTへの追加コストを更新
            val neighborsWithWeight = getNeighbors(currentVertex)
            if (neighborsWithWeight != null) { // 隣接する頂点がある場合
                for ((neighbor, weight) in neighborsWithWeight) {
                    // 隣接頂点がまだMSTに含まれておらず、現在のコストよりも小さい場合
                    if (neighbor !in inMst && weight < minCost[neighbor]!!) {
                        minCost[neighbor] = weight.toDouble()
                        parent[neighbor] = currentVertex
                        minHeap.add(Triple(weight, neighbor, currentVertex))
                    }
                }
            }
        }

        return mstEdges
    }
}

fun main() {
    println("Prims TEST -----> start")
    val graphData = GraphData()

    graphData.clear()
    val inputList1 = arrayOf(
        Triple("A", "B", 4),
        Triple("B", "C", 3),
        Triple("B", "D", 2),
        Triple("D", "A", 1),
        Triple("A", "C", 2),
        Triple("B", "D", 2)
    )
    for (input in inputList1) {
        graphData.addEdge(input.first, input.second, input.third)
    }
    println("\nグラフの頂点: ${graphData.getVertices()}")
    println("グラフの辺 (重み付き): ${graphData.getEdges()}")
    val outputMst1 = graphData.getMst()
    for (edge in outputMst1) {
        println("Edge: ${edge.first} - ${edge.second}, Weight: ${edge.third}")
    }
    val totalWeight1 = outputMst1.sumOf { it.third }
    println("最小全域木の合計重み: $totalWeight1")

    graphData.clear()
    val inputList2 = arrayOf(
        Triple("A", "B", 4),
        Triple("C", "D", 4),
        Triple("E", "F", 1),
        Triple("F", "G", 1)
    )
    for (input in inputList2) {
        graphData.addEdge(input.first, input.second, input.third)
    }
    println("\nグラフの頂点: ${graphData.getVertices()}")
    println("グラフの辺 (重み付き): ${graphData.getEdges()}")
    val outputMst2 = graphData.getMst()
    for (edge in outputMst2) {
        println("Edge: ${edge.first} - ${edge.second}, Weight: ${edge.third}")
    }
    val totalWeight2 = outputMst2.sumOf { it.third }
    println("最小全域木の合計重み: $totalWeight2")

    graphData.clear()
    val inputList3 = arrayOf(
        Triple("A", "B", 4),
        Triple("B", "C", 3),
        Triple("D", "E", 5)
    )
    for (input in inputList3) {
        graphData.addEdge(input.first, input.second, input.third)
    }
    println("\nグラフの頂点: ${graphData.getVertices()}")
    println("グラフの辺 (重み付き): ${graphData.getEdges()}")
    val outputMst3 = graphData.getMst()
    for (edge in outputMst3) {
        println("Edge: ${edge.first} - ${edge.second}, Weight: ${edge.third}")
    }
    val totalWeight3 = outputMst3.sumOf { it.third }
    println("最小全域木の合計重み: $totalWeight3")

    graphData.clear()
    val inputList4 = arrayOf<Triple<String, String, Int>>()
    for (input in inputList4) {
        graphData.addEdge(input.first, input.second, input.third)
    }
    println("\nグラフの頂点: ${graphData.getVertices()}")
    println("グラフの辺 (重み付き): ${graphData.getEdges()}")
    val outputMst4 = graphData.getMst()
    for (edge in outputMst4) {
        println("Edge: ${edge.first} - ${edge.second}, Weight: ${edge.third}")
    }
    val totalWeight4 = outputMst4.sumOf { it.third }
    println("最小全域木の合計重み: $totalWeight4")

    println("\nPrims TEST <----- end")
}