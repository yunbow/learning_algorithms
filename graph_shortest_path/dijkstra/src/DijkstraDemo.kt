// Kotlin
// グラフの最短経路: ダイクストラ法 (dijkstra)

import java.util.*

class GraphData {
    // 隣接ノードとその辺の重みを格納
    // キーは頂点、値はその頂点に隣接する頂点と重みのペアのリスト
    private val data = mutableMapOf<String, MutableList<Pair<String, Int>>>()

    fun get(): Map<String, List<Pair<String, Int>>> {
        return data
    }

    fun getVertices(): List<String> {
        // グラフの全頂点をリストとして返す
        return data.keys.toList()
    }

    fun getEdges(): List<Triple<String, String, Int>> {
        // グラフの全辺をリストとして返す
        // 無向グラフの場合、(u, v, weight) の形式で返す
        // 重複を避けるためにセットを使用
        val edges = mutableSetOf<Triple<String, String, Int>>()
        for (vertex in data.keys) {
            for ((neighbor, weight) in data[vertex] ?: emptyList()) {
                // 辺を正規化してセットに追加 (小さい方の頂点を最初にするなど)
                val normalizedEdge = if (vertex < neighbor) {
                    Triple(vertex, neighbor, weight)
                } else {
                    Triple(neighbor, vertex, weight)
                }
                edges.add(normalizedEdge)
            }
        }
        return edges.toList()
    }

    fun getNeighbors(vertex: String): List<Pair<String, Int>>? {
        // 指定された頂点の隣接ノードと辺の重みのリストを返す
        // 形式: [(隣接頂点, 重み), ...]
        return data[vertex]
    }

    fun addVertex(vertex: String): Boolean {
        // 新しい頂点をグラフに追加
        if (vertex !in data) {
            data[vertex] = mutableListOf()
        }
        // 既に存在する場合も追加しないがtrueを返す（変更なしでも成功とみなす）
        return true
    }

    fun addEdge(vertex1: String, vertex2: String, weight: Int): Boolean {
        // 両頂点間に辺を追加し、重みを指定
        // 頂点がグラフに存在しない場合は追加
        if (vertex1 !in data) {
            addVertex(vertex1)
        }
        if (vertex2 !in data) {
            addVertex(vertex2)
        }

        // 両方向に辺を追加（無向グラフ）
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

    fun clear(): Boolean {
        // グラフを空にする
        data.clear()
        return true
    }

    fun getShortestPath(
        startVertex: String,
        endVertex: String,
        heuristic: (String, String) -> Int
    ): Pair<List<String>?, Int> {
        if (startVertex !in data || endVertex !in data) {
            println("ERROR: 開始頂点 '$startVertex' または 終了頂点 '$endVertex' がグラフに存在しません。")
            return Pair(null, Int.MAX_VALUE)
        }

        // 距離を初期化: 全ての頂点への距離を無限大に設定し、開始頂点のみ0とする
        val distances = data.keys.associateWith { Int.MAX_VALUE }.toMutableMap()
        distances[startVertex] = 0

        // 経路を記録するための辞書: 各頂点に至る直前の頂点を保持
        val predecessors = data.keys.associateWith { null as String? }.toMutableMap()

        // 優先度付きキュー: (距離, 頂点) のペアを格納し、距離が小さい順に取り出す
        val priorityQueue = PriorityQueue<Pair<Int, String>>(compareBy { it.first })
        priorityQueue.add(Pair(0, startVertex)) // (開始頂点への距離, 開始頂点)

        while (priorityQueue.isNotEmpty()) {
            // 優先度付きキューから最も距離の小さい頂点を取り出す
            val (currentDistance, currentVertex) = priorityQueue.poll()

            // 取り出した頂点への距離が、すでに記録されている距離より大きい場合は、
            // より短い経路が既に見つかっているためスキップ
            if (currentDistance > distances[currentVertex]!!) {
                continue
            }

            // 終了頂点に到達したら探索終了
            if (currentVertex == endVertex) {
                break // 最短経路が見つかった
            }

            // 現在の頂点から到達可能な隣接頂点を探索
            for ((neighbor, weight) in getNeighbors(currentVertex) ?: emptyList()) {
                // 整数オーバーフローを避けるためのチェック
                val distanceThroughCurrent = if (distances[currentVertex]!! == Int.MAX_VALUE) {
                    Int.MAX_VALUE
                } else {
                    distances[currentVertex]!! + weight
                }

                // より短い経路が見つかった場合
                if (distanceThroughCurrent < distances[neighbor]!!) {
                    distances[neighbor] = distanceThroughCurrent
                    predecessors[neighbor] = currentVertex
                    // 優先度付きキューに隣接頂点を追加または更新
                    // ダイクストラ法では heuristic は使用しない (または h=0)
                    priorityQueue.add(Pair(distanceThroughCurrent, neighbor))
                }
            }
        }

        // 終了頂点への最短距離が無限大のままなら、到達不可能
        if (distances[endVertex] == Int.MAX_VALUE) {
            println("INFO: 開始頂点 '$startVertex' から 終了頂点 '$endVertex' への経路は存在しません。")
            return Pair(null, Int.MAX_VALUE)
        }

        // 最短経路を再構築
        val path = mutableListOf<String>()
        var current: String? = endVertex
        while (current != null) {
            path.add(current)
            current = predecessors[current]
        }
        path.reverse() // 経路は逆順に構築されたので反転

        // 開始ノードから開始されていることを確認
        if (path.firstOrNull() != startVertex) {
            // これが発生するのは、開始頂点が存在しないか、到達不能な場合だが、
            // 前段のチェックで大部分はカバーされているはず。
            // ここに来る場合は、特殊なケース（例えば孤立した開始点と終了点）が考えられる。
            return Pair(null, Int.MAX_VALUE)
        }

        return Pair(path, distances[endVertex]!!)
    }
}

// ヒューリスティック関数 (この例では常に0、ダイクストラ法と同じ)
// 実際のA*では、問題に応じた適切な推定関数を使用する必要がある
fun dummyHeuristic(u: String, v: String): Int {
    // u と v の間に何らかの推定距離を計算する関数
    // ここではダミーとして常に0を返す
    return 0
}

fun main() {
    println("Dijkstra -----> start")

    val graphData = GraphData()

    graphData.clear()
    val inputList1 = listOf(
        Triple("A", "B", 4),
        Triple("B", "C", 3),
        Triple("B", "D", 2),
        Triple("D", "A", 1),
        Triple("A", "C", 2),
        Triple("B", "D", 2)
    )
    for ((v1, v2, weight) in inputList1) {
        graphData.addEdge(v1, v2, weight)
    }
    println("\nグラフの頂点: ${graphData.getVertices()}")
    println("グラフの辺 (重み付き): ${graphData.getEdges()}")
    val input1 = Pair("A", "B")
    val shortestPath1 = graphData.getShortestPath(input1.first, input1.second, ::dummyHeuristic)
    println("経路${input1.first}-${input1.second} の最短経路は ${shortestPath1.first} (重み: ${shortestPath1.second})")

    graphData.clear()
    val inputList2 = listOf(
        Triple("A", "B", 4),
        Triple("C", "D", 4),
        Triple("E", "F", 1),
        Triple("F", "G", 1)
    )
    for ((v1, v2, weight) in inputList2) {
        graphData.addEdge(v1, v2, weight)
    }
    println("\nグラフの頂点: ${graphData.getVertices()}")
    println("グラフの辺 (重み付き): ${graphData.getEdges()}")
    val input2 = Pair("A", "B")
    val shortestPath2 = graphData.getShortestPath(input2.first, input2.second, ::dummyHeuristic)
    println("経路${input2.first}-${input2.second} の最短経路は ${shortestPath2.first} (重み: ${shortestPath2.second})")

    graphData.clear()
    val inputList3 = listOf(
        Triple("A", "B", 4),
        Triple("B", "C", 3),
        Triple("D", "E", 5)
    )
    for ((v1, v2, weight) in inputList3) {
        graphData.addEdge(v1, v2, weight)
    }
    println("\nグラフの頂点: ${graphData.getVertices()}")
    println("グラフの辺 (重み付き): ${graphData.getEdges()}")
    val input3 = Pair("A", "D")
    val shortestPath3 = graphData.getShortestPath(input3.first, input3.second, ::dummyHeuristic)
    println("経路${input3.first}-${input3.second} の最短経路は ${shortestPath3.first} (重み: ${shortestPath3.second})")

    graphData.clear()
    val inputList4 = listOf<Triple<String, String, Int>>()
    for ((v1, v2, weight) in inputList4) {
        graphData.addEdge(v1, v2, weight)
    }
    println("\nグラフの頂点: ${graphData.getVertices()}")
    println("グラフの辺 (重み付き): ${graphData.getEdges()}")
    val input4 = Pair("A", "B")
    val shortestPath4 = graphData.getShortestPath(input4.first, input4.second, ::dummyHeuristic)
    println("経路${input4.first}-${input4.second} の最短経路は ${shortestPath4.first} (重み: ${shortestPath4.second})")

    println("\nDijkstra <----- end")
}