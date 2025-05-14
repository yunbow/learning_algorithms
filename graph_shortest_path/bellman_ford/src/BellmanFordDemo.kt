// Kotlin
// グラフの最短経路: ベルマンフォード法 (Bellman Ford)

import java.util.*

class GraphData {
    // 隣接ノードとその辺の重みを格納します。
    // キーは頂点、値はその頂点に隣接する頂点と重みのリストです。
    private val data = mutableMapOf<String, MutableList<Pair<String, Int>>>()

    fun get(): Map<String, List<Pair<String, Int>>> {
        return data
    }

    fun getVertices(): List<String> {
        return data.keys.toList()
    }

    fun getEdges(): List<Triple<String, String, Int>> {
        val edges = mutableListOf<Triple<String, String, Int>>()
        for (u in data.keys) {
            for ((v, weight) in data[u] ?: emptyList()) {
                edges.add(Triple(u, v, weight))
            }
        }
        return edges
    }

    fun addVertex(vertex: String): Boolean {
        if (vertex !in data) {
            data[vertex] = mutableListOf()
        }
        return true
    }

    fun addEdge(vertex1: String, vertex2: String, weight: Int): Boolean {
        addVertex(vertex1)
        addVertex(vertex2)

        // vertex1 -> vertex2 の辺を追加（重み付き）
        var edgeUpdatedV1V2 = false
        data[vertex1]?.let { neighbors ->
            for (i in neighbors.indices) {
                val (neighbor, _) = neighbors[i]
                if (neighbor == vertex2) {
                    neighbors[i] = vertex2 to weight
                    edgeUpdatedV1V2 = true
                    break
                }
            }
            if (!edgeUpdatedV1V2) {
                neighbors.add(vertex2 to weight)
            }
        }

        // vertex2 -> vertex1 の辺を追加（重み付き）
        var edgeUpdatedV2V1 = false
        data[vertex2]?.let { neighbors ->
            for (i in neighbors.indices) {
                val (neighbor, _) = neighbors[i]
                if (neighbor == vertex1) {
                    neighbors[i] = vertex1 to weight
                    edgeUpdatedV2V1 = true
                    break
                }
            }
            if (!edgeUpdatedV2V1) {
                neighbors.add(vertex1 to weight)
            }
        }

        return true
    }

    fun isEmpty(): Boolean {
        return data.isEmpty()
    }

    // fun size(): Int {
    //     return data.size
    // }

    fun clear(): Boolean {
        data.clear()
        return true
    }

    fun getShortestPath(startVertex: String, endVertex: String, heuristic: (String, String) -> Int): Pair<List<String>?, Int> {
        val vertices = getVertices()
        val edges = getEdges()
        val numVertices = vertices.size

        // 始点と終点の存在チェック
        if (startVertex !in vertices) {
            println("エラー: 始点 '$startVertex' がグラフに存在しません。")
            return Pair(emptyList(), Int.MAX_VALUE)
        }
        if (endVertex !in vertices) {
            println("エラー: 終点 '$endVertex' がグラフに存在しません。")
            return Pair(emptyList(), Int.MAX_VALUE)
        }

        // 始点と終点が同じ場合
        if (startVertex == endVertex) {
            return Pair(listOf(startVertex), 0)
        }

        // 距離と先行頂点の初期化
        val dist = vertices.associateWith { Int.MAX_VALUE }.toMutableMap()
        val pred = vertices.associateWith { null as String? }.toMutableMap()
        dist[startVertex] = 0 // 始点自身の距離は0

        // |V| - 1 回の緩和ステップを実行
        for (i in 0 until numVertices - 1) {
            var relaxedInThisIteration = false
            for ((u, v, weight) in edges) {
                val distU = dist[u] ?: Int.MAX_VALUE
                val distV = dist[v] ?: Int.MAX_VALUE
                
                // Int.MAX_VALUEの場合はオーバーフローを避けるための特別処理
                if (distU != Int.MAX_VALUE && distU + weight < distV) {
                    dist[v] = distU + weight
                    pred[v] = u
                    relaxedInThisIteration = true
                }
            }
            // このイテレーションで緩和が行われなかった場合はループを抜ける
            if (!relaxedInThisIteration) {
                break
            }
        }

        // 負閉路の検出
        for ((u, v, weight) in edges) {
            val distU = dist[u] ?: Int.MAX_VALUE
            val distV = dist[v] ?: Int.MAX_VALUE
            
            if (distU != Int.MAX_VALUE && distU + weight < distV) {
                println("エラー: グラフに負閉路が存在します。最短経路は定義できません。")
                return Pair(null, Int.MIN_VALUE) // 負の無限大を返す
            }
        }

        // 最短経路の構築
        val path = mutableListOf<String>()
        var current = endVertex

        // 終点まで到達不可能かチェック
        if (dist[endVertex] == Int.MAX_VALUE) {
            return Pair(emptyList(), Int.MAX_VALUE)
        }

        // 終点から先行頂点をたどって経路を逆順に構築
        while (current != null) {
            path.add(current)
            // 始点に到達したらループを終了
            if (current == startVertex) {
                break
            }
            // 次の頂点に進む
            current = pred[current]
        }

        // 経路が始点から始まっていない場合
        if (path.isEmpty() || path.last() != startVertex) {
            return Pair(emptyList(), Int.MAX_VALUE)
        }

        path.reverse() // 経路を始点から終点の順にする

        return Pair(path, dist[endVertex] ?: Int.MAX_VALUE)
    }
}

// ヒューリスティック関数 (ベルマン-フォード法では使用しないが、元のコードに合わせて実装)
fun dummyHeuristic(u: String, v: String): Int {
    // ベルマン-フォード法では使用しないため、常に0を返す
    return 0
}

fun main() {
    println("BellmanFord TEST -----> start")

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

    println("\nBellmanFord TEST <----- end")
}