// C++
// グラフの最短経路: Kotlin

import java.util.*

class GraphData {
    // 隣接ノードとその辺の重みを格納
    // キーは頂点、値はその頂点に隣接する頂点と重みのリスト
    private val data = mutableMapOf<String, MutableList<Pair<String, Int>>>()

    fun get(): Map<String, List<Pair<String, Int>>> {
        return data
    }

    fun getVertices(): List<String> {
        return data.keys.toList()
    }

    fun getEdges(): List<Triple<String, String, Int>> {
        val edges = mutableSetOf<Triple<String, String, Int>>()
        for (vertex in data.keys) {
            for ((neighbor, weight) in data[vertex] ?: emptyList()) {
                // 辺を正規化してセットに追加（小さい方の頂点を最初にする）
                val sortedVertices = listOf(vertex, neighbor).sorted()
                edges.add(Triple(sortedVertices[0], sortedVertices[1], weight))
            }
        }
        return edges.toList()
    }

    fun getNeighbors(vertex: String): List<Pair<String, Int>>? {
        return data[vertex]
    }

    fun addVertex(vertex: String): Boolean {
        if (vertex !in data) {
            data[vertex] = mutableListOf()
        }
        return true
    }

    fun addEdge(vertex1: String, vertex2: String, weight: Int): Boolean {
        if (vertex1 !in data) {
            addVertex(vertex1)
        }
        if (vertex2 !in data) {
            addVertex(vertex2)
        }

        // vertex1 -> vertex2 の辺を追加（重み付き）
        var edgeExistsV1V2 = false
        for (i in (data[vertex1] ?: mutableListOf()).indices) {
            val (neighbor, _) = data[vertex1]!![i]
            if (neighbor == vertex2) {
                data[vertex1]!![i] = vertex2 to weight // 既に存在する場合は重みを更新
                edgeExistsV1V2 = true
                break
            }
        }
        if (!edgeExistsV1V2) {
            data[vertex1]?.add(vertex2 to weight)
        }

        // vertex2 -> vertex1 の辺を追加（重み付き）
        var edgeExistsV2V1 = false
        for (i in (data[vertex2] ?: mutableListOf()).indices) {
            val (neighbor, _) = data[vertex2]!![i]
            if (neighbor == vertex1) {
                data[vertex2]!![i] = vertex1 to weight // 既に存在する場合は重みを更新
                edgeExistsV2V1 = true
                break
            }
        }
        if (!edgeExistsV2V1) {
            data[vertex2]?.add(vertex1 to weight)
        }

        return true
    }

    fun clear(): Boolean {
        data.clear()
        return true
    }

    fun getShortestPath(startVertex: String, endVertex: String, heuristic: (String, String) -> Int): Pair<List<String>?, Int> {
        if (startVertex !in data || endVertex !in data) {
            println("ERROR: 開始頂点または終了頂点がグラフに存在しません。")
            return null to Int.MAX_VALUE
        }

        if (startVertex == endVertex) {
            return listOf(startVertex) to 0
        }

        // g_costs: 開始ノードから各ノードまでの既知の最短コスト
        val gCosts = data.keys.associateWith { Int.MAX_VALUE }.toMutableMap()
        gCosts[startVertex] = 0

        // f_costs: g_costs + ヒューリスティックコスト（推定合計コスト）
        val fCosts = data.keys.associateWith { Int.MAX_VALUE }.toMutableMap()
        fCosts[startVertex] = heuristic(startVertex, endVertex)

        // came_from: 最短経路で各ノードの直前のノードを記録
        val cameFrom = mutableMapOf<String, String>()

        // open_set: 探索すべきノードの優先度キュー (f_cost, node)
        val openSet = PriorityQueue<Pair<Int, String>>(compareBy { it.first })
        openSet.add(fCosts[startVertex]!! to startVertex)

        while (openSet.isNotEmpty()) {
            // open_set から最も f_cost が低いノードを取り出す
            val (currentFCost, currentVertex) = openSet.poll()

            // 取り出したノードの f_cost が、記録されている f_costs[currentVertex] より大きい場合、
            // それは古い情報なので無視して次のノードに進む
            if (currentFCost > fCosts[currentVertex]!!) {
                continue
            }

            // 目標ノードに到達した場合、経路を再構築して返す
            if (currentVertex == endVertex) {
                return reconstructPath(cameFrom, endVertex) to gCosts[endVertex]!!
            }

            // 現在のノードの隣接ノードを調べる
            val neighbors = getNeighbors(currentVertex) ?: continue

            for ((neighbor, weight) in neighbors) {
                // 現在のノードを経由した場合の隣接ノードへの新しい g_cost
                val tentativeGCost = gCosts[currentVertex]!! + weight

                // 新しい g_cost が、現在記録されている隣接ノードへの g_cost よりも小さい場合
                if (tentativeGCost < gCosts[neighbor]!!) {
                    // 経路情報を更新
                    cameFrom[neighbor] = currentVertex
                    gCosts[neighbor] = tentativeGCost
                    fCosts[neighbor] = gCosts[neighbor]!! + heuristic(neighbor, endVertex)

                    // 隣接ノードを open_set に追加（または優先度を更新）
                    openSet.add(fCosts[neighbor]!! to neighbor)
                }
            }
        }

        // open_set が空になっても目標ノードに到達しなかった場合、経路は存在しない
        return null to Int.MAX_VALUE
    }

    private fun reconstructPath(cameFrom: Map<String, String>, currentVertex: String): List<String> {
        val path = mutableListOf<String>()
        var current = currentVertex
        while (current in cameFrom) {
            path.add(current)
            current = cameFrom[current]!!
        }
        path.add(current) // 開始ノードを追加
        path.reverse() // 経路を逆順にする (開始 -> 目標)
        return path
    }
}

// ヒューリスティック関数 (この例では常に0、ダイクストラ法と同じ)
fun dummyHeuristic(u: String, v: String): Int {
    // u と v の間に何らかの推定距離を計算する関数
    // ここではダミーとして常に0を返す
    return 0
}

fun main() {
    println("A-start TEST -----> start")

    val graphData = GraphData()

    graphData.clear()
    val inputList1 = listOf(
        Triple("A", "B", 4), Triple("B", "C", 3), Triple("B", "D", 2),
        Triple("D", "A", 1), Triple("A", "C", 2), Triple("B", "D", 2)
    )
    for ((v1, v2, w) in inputList1) {
        graphData.addEdge(v1, v2, w)
    }
    println("\nグラフの頂点: ${graphData.getVertices()}")
    println("グラフの辺 (重み付き): ${graphData.getEdges()}")
    val input1 = "A" to "B"
    val shortestPath1 = graphData.getShortestPath(input1.first, input1.second, ::dummyHeuristic)
    println("経路${input1.first}-${input1.second} の最短経路は ${shortestPath1.first} (重み: ${shortestPath1.second})")

    graphData.clear()
    val inputList2 = listOf(
        Triple("A", "B", 4), Triple("C", "D", 4), Triple("E", "F", 1), Triple("F", "G", 1)
    )
    for ((v1, v2, w) in inputList2) {
        graphData.addEdge(v1, v2, w)
    }
    println("\nグラフの頂点: ${graphData.getVertices()}")
    println("グラフの辺 (重み付き): ${graphData.getEdges()}")
    val input2 = "A" to "B"
    val shortestPath2 = graphData.getShortestPath(input2.first, input2.second, ::dummyHeuristic)
    println("経路${input2.first}-${input2.second} の最短経路は ${shortestPath2.first} (重み: ${shortestPath2.second})")

    graphData.clear()
    val inputList3 = listOf(
        Triple("A", "B", 4), Triple("B", "C", 3), Triple("D", "E", 5)
    )
    for ((v1, v2, w) in inputList3) {
        graphData.addEdge(v1, v2, w)
    }
    println("\nグラフの頂点: ${graphData.getVertices()}")
    println("グラフの辺 (重み付き): ${graphData.getEdges()}")
    val input3 = "A" to "D"
    val shortestPath3 = graphData.getShortestPath(input3.first, input3.second, ::dummyHeuristic)
    println("経路${input3.first}-${input3.second} の最短経路は ${shortestPath3.first} (重み: ${shortestPath3.second})")

    graphData.clear()
    val inputList4 = listOf<Triple<String, String, Int>>()
    for ((v1, v2, w) in inputList4) {
        graphData.addEdge(v1, v2, w)
    }
    println("\nグラフの頂点: ${graphData.getVertices()}")
    println("グラフの辺 (重み付き): ${graphData.getEdges()}")
    val input4 = "A" to "B"
    val shortestPath4 = graphData.getShortestPath(input4.first, input4.second, ::dummyHeuristic)
    println("経路${input4.first}-${input4.second} の最短経路は ${shortestPath4.first} (重み: ${shortestPath4.second})")

    println("\nA-start TEST <----- end")
}