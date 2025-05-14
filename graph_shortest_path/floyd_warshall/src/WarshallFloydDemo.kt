// Kotlin
// グラフの最短経路: ワーシャルフロイド法 (Warshall Floyd)

import java.util.*

class GraphData {
    // 隣接ノードとその辺の重みを格納します。
    // キーは頂点、値はその頂点に隣接する頂点と重みのリストです。
    private var data: MutableMap<String, MutableList<Pair<String, Double>>> = mutableMapOf()

    fun get(): Map<String, List<Pair<String, Double>>> {
        // グラフの内部データを取得します。
        return data
    }

    fun getVertices(): List<String> {
        // グラフの全頂点をリストとして返します。
        return data.keys.toList()
    }

    fun getEdges(): List<Triple<String, String, Double>> {
        // グラフの全辺をリストとして返します。
        // 無向グラフの場合、(u, v, weight) の形式で返します。
        // 重複を避けるためにセットを使用します。
        val edges = mutableSetOf<Triple<String, String, Double>>()
        for (vertex in data.keys) {
            for ((neighbor, weight) in data[vertex] ?: emptyList()) {
                // 辺を正規化してセットに追加 (小さい方の頂点を最初にするなど)
                val edge = listOf(vertex, neighbor).sorted()
                edges.add(Triple(edge[0], edge[1], weight)) // (u, v, weight) の形式で格納
            }
        }
        return edges.toList()
    }

    fun getNeighbors(vertex: String): List<Pair<String, Double>>? {
        // 指定された頂点の隣接ノードと辺の重みのリストを返します。
        // 形式: [(隣接頂点, 重み), ...]
        return data[vertex]
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

    fun addEdge(vertex1: String, vertex2: String, weight: Double): Boolean {
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
        for (i in (data[vertex1] ?: mutableListOf()).indices) {
            if (data[vertex1]!![i].first == vertex2) {
                data[vertex1]!![i] = Pair(vertex2, weight) // 既に存在する場合は重みを更新
                edgeExistsV1V2 = true
                break
            }
        }
        if (!edgeExistsV1V2) {
            data[vertex1]?.add(Pair(vertex2, weight))
        }

        // vertex2 -> vertex1 の辺を追加（重み付き）
        var edgeExistsV2V1 = false
        for (i in (data[vertex2] ?: mutableListOf()).indices) {
            if (data[vertex2]!![i].first == vertex1) {
                data[vertex2]!![i] = Pair(vertex1, weight) // 既に存在する場合は重みを更新
                edgeExistsV2V1 = true
                break
            }
        }
        if (!edgeExistsV2V1) {
            data[vertex2]?.add(Pair(vertex1, weight))
        }
        
        return true
    }

    fun clear(): Boolean {
        // グラフを空にします。
        data.clear()
        return true
    }

    fun getShortestPath(startVertex: String, endVertex: String, heuristic: (String, String) -> Double): Pair<List<String>?, Double> {
        val vertices = getVertices()
        val numVertices = vertices.size
        if (numVertices == 0) {
            return Pair(null, Double.POSITIVE_INFINITY)
        }

        // 頂点名をインデックスにマッピング
        val vertexToIndex = vertices.withIndex().associate { (index, vertex) -> vertex to index }
        val indexToVertex = vertices.withIndex().associate { (index, vertex) -> index to vertex }

        // 開始・終了頂点が存在するか確認
        if (startVertex !in vertexToIndex || endVertex !in vertexToIndex) {
            println("ERROR: $startVertex または $endVertex がグラフに存在しません。")
            return Pair(null, Double.POSITIVE_INFINITY)
        }

        val startIndex = vertexToIndex[startVertex]!!
        val endIndex = vertexToIndex[endVertex]!!

        // 距離行列 (dist) と経路復元用行列 (nextNode) を初期化
        val INF = Double.POSITIVE_INFINITY
        val dist = Array(numVertices) { DoubleArray(numVertices) { INF } }
        val nextNode = Array(numVertices) { Array<Int?>(numVertices) { null } }

        // 初期距離と経路復元情報を設定
        for (i in 0 until numVertices) {
            dist[i][i] = 0.0 // 自分自身への距離は0
            for ((neighbor, weight) in getNeighbors(vertices[i]) ?: emptyList()) {
                val j = vertexToIndex[neighbor]!!
                dist[i][j] = weight
                nextNode[i][j] = j // iからjへの直接辺の場合、iの次はj
            }
        }

        // ワーシャル-フロイド法の本体
        // k: 中継点として使用する頂点のインデックス
        for (k in 0 until numVertices) {
            // i: 開始頂点のインデックス
            for (i in 0 until numVertices) {
                // j: 終了頂点のインデックス
                for (j in 0 until numVertices) {
                    // i -> k -> j の経路が i -> j の現在の経路より短い場合
                    if (dist[i][k] != INF && dist[k][j] != INF && dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j]
                        nextNode[i][j] = nextNode[i][k] // iからjへの最短経路で、iの次の頂点はiからkへの最短経路でのiの次の頂点
                    }
                }
            }
        }

        // 指定された開始・終了頂点間の最短経路と重みを取得
        val shortestDistance = dist[startIndex][endIndex]

        // 経路が存在しない場合 (距離がINF)
        if (shortestDistance == INF) {
            return Pair(null, INF)
        }

        // 経路を復元
        val path = mutableListOf<String>()
        var u = startIndex
        // 開始と終了が同じ場合は経路は開始頂点のみ
        if (u == endIndex) {
            path.add(startVertex)
        } else {
            // nextNodeを使って経路をたどる
            while (u != null && u != endIndex) {
                path.add(indexToVertex[u]!!)
                u = nextNode[u][endIndex]
                // 無限ループ防止のための簡易チェック (到達不能なのにnextNodeがNoneでない場合など)
                if (u != null && indexToVertex[u] == path.last()) {
                    // 同じ頂点に戻ってきたなど、異常な経路復元を防ぐ
                    println("WARNING: 経路復元中に異常を検出しました（${indexToVertex[u]}でループ？）。")
                    return Pair(null, INF)
                }
            }
            // 最後のノード (endVertex) を追加
            path.add(endVertex)
        }

        return Pair(path, shortestDistance)
    }
}

// ヒューリスティック関数 (この例では常に0、ダイクストラ法と同じ)
// 実際のA*では、問題に応じた適切な推定関数を使用する必要があります。
fun dummyHeuristic(u: String, v: String): Double {
    // u と v の間に何らかの推定距離を計算する関数
    // ここではダミーとして常に0を返す
    return 0.0
}

fun main() {
    println("WarshallFloyd -----> start")

    val graphData = GraphData()

    graphData.clear()
    val inputList1 = listOf(
        Triple("A", "B", 4.0), 
        Triple("B", "C", 3.0), 
        Triple("B", "D", 2.0), 
        Triple("D", "A", 1.0), 
        Triple("A", "C", 2.0), 
        Triple("B", "D", 2.0)
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
        Triple("A", "B", 4.0), 
        Triple("C", "D", 4.0), 
        Triple("E", "F", 1.0), 
        Triple("F", "G", 1.0)
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
        Triple("A", "B", 4.0), 
        Triple("B", "C", 3.0), 
        Triple("D", "E", 5.0)
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
    val inputList4 = emptyList<Triple<String, String, Double>>()
    for ((v1, v2, weight) in inputList4) {
        graphData.addEdge(v1, v2, weight)
    }
    println("\nグラフの頂点: ${graphData.getVertices()}")
    println("グラフの辺 (重み付き): ${graphData.getEdges()}")
    val input4 = Pair("A", "B")
    val shortestPath4 = graphData.getShortestPath(input4.first, input4.second, ::dummyHeuristic)
    println("経路${input4.first}-${input4.second} の最短経路は ${shortestPath4.first} (重み: ${shortestPath4.second})")

    println("\nWarshallFloyd <----- end")
}