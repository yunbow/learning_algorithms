// Kotlin
// グラフの最小全域木: クラスカル法 (Kruskal)

class DSU(vertices: List<String>) {
    // 各頂点の親を格納します。最初は各頂点自身が親です。
    private val parent = vertices.associateWith { it }.toMutableMap()
    // ランク（木の高さまたはサイズ）を格納し、union操作を最適化します。
    private val rank = vertices.associateWith { 0 }.toMutableMap()

    // 頂点 i が属する集合の代表元（根）を見つけます。
    // パス圧縮により効率化されます。
    fun find(i: String): String {
        if (parent[i] == i) {
            return i
        }
        // パス圧縮: 探索中に encountered したノードを根に直接つなぎ直します。
        parent[i] = find(parent[i]!!)
        return parent[i]!!
    }

    // 頂点 i と 頂点 j を含む二つの集合を結合します。
    // ランクによるunionにより効率化されます。
    fun union(i: String, j: String): Boolean {
        val rootI = find(i)
        val rootJ = find(j)

        // 根が異なる場合のみ結合します（同じ集合に属している場合は何もしません）。
        if (rootI != rootJ) {
            // ランクが小さい方の木を、ランクが大きい方の木の根に付けます。
            when {
                rank[rootI]!! < rank[rootJ]!! -> parent[rootI] = rootJ
                rank[rootI]!! > rank[rootJ]!! -> parent[rootJ] = rootI
                else -> {
                    // ランクが同じ場合はどちらかを親とし、その親のランクを増やします。
                    parent[rootJ] = rootI
                    rank[rootI] = rank[rootI]!! + 1
                }
            }
            return true // 集合が結合された
        }
        return false // 既に同じ集合に属していた
    }
}

// 重みを扱えるように改変された GraphData クラス
class GraphData {
    // 隣接ノードとその辺の重みを格納します。
    // キーは頂点、値はその頂点に隣接する頂点と重みのリストです。
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
                val normalizedVertices = listOf(vertex, neighbor).sorted()
                edges.add(Triple(normalizedVertices[0], normalizedVertices[1], weight))
            }
        }
        return edges.toList()
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

    fun clear(): Boolean {
        // グラフを空にします。
        data.clear()
        return true
    }

    fun getMst(): List<Triple<String, String, Int>> {
        // 1. 全ての辺を取得し、重みでソートします。
        // getEdges() は (u, v, weight) のリストを返します。
        val edges = getEdges()
        // 重み (タプルの3番目の要素) をキーとして辺をソート
        val sortedEdges = edges.sortedBy { it.third }

        // 2. Union-Findデータ構造を初期化します。
        // 各頂点が自身の集合に属するようにします。
        val vertices = getVertices()
        val dsu = DSU(vertices)

        // 3. MSTを構築します。
        // 結果として得られるMSTの辺を格納するリスト
        val mstEdges = mutableListOf<Triple<String, String, Int>>()
        // MSTに追加された辺の数 (頂点数-1 になればMSTが完成)
        var edgesCount = 0

        // ソートされた辺を順番に調べます。
        for ((u, v, weight) in sortedEdges) {
            // 辺 (u, v) の両端点が属する集合の代表元（根）を見つけます。
            val rootU = dsu.find(u)
            val rootV = dsu.find(v)

            // 両端点が異なる集合に属する場合、その辺をMSTに追加してもサイクルは形成されません。
            if (rootU != rootV) {
                // 辺をMSTに追加します。
                mstEdges.add(Triple(u, v, weight))
                // 辺を追加したので、両端点の集合を結合します。
                dsu.union(u, v)
                // MSTに追加した辺の数を増やします。
                edgesCount++

                // 頂点数から1を引いた数の辺がMSTに追加されたら終了です。
                // これはグラフが連結である場合に限り、完全なMSTが得られたことを意味します。
                // 連結でない場合は、全ての辺を調べ終えるまで続行し、最小全域森を得ます。
                if (edgesCount == vertices.size - 1) {
                    break
                }
            }
        }

        // MST (または最小全域森) の辺のリストを返します。
        return mstEdges
    }
}

fun main() {
    println("Kruskal TEST -----> start")
    val graphData = GraphData()

    graphData.clear()
    val inputList1 = listOf(
        Triple("A", "B", 4), Triple("B", "C", 3), Triple("B", "D", 2), 
        Triple("D", "A", 1), Triple("A", "C", 2), Triple("B", "D", 2)
    )
    for ((v1, v2, weight) in inputList1) {
        graphData.addEdge(v1, v2, weight)
    }
    println("\nグラフの頂点: ${graphData.getVertices()}")
    println("グラフの辺 (重み付き): ${graphData.getEdges()}")
    val outputMst1 = graphData.getMst()
    for ((v1, v2, weight) in outputMst1) {
        println("Edge: $v1 - $v2, Weight: $weight")
    }
    val totalWeight1 = outputMst1.sumOf { it.third }
    println("最小全域木の合計重み: $totalWeight1")

    graphData.clear()
    val inputList2 = listOf(
        Triple("A", "B", 4), Triple("C", "D", 4), 
        Triple("E", "F", 1), Triple("F", "G", 1)
    )
    for ((v1, v2, weight) in inputList2) {
        graphData.addEdge(v1, v2, weight)
    }
    println("\nグラフの頂点: ${graphData.getVertices()}")
    println("グラフの辺 (重み付き): ${graphData.getEdges()}")
    val outputMst2 = graphData.getMst()
    for ((v1, v2, weight) in outputMst2) {
        println("Edge: $v1 - $v2, Weight: $weight")
    }
    val totalWeight2 = outputMst2.sumOf { it.third }
    println("最小全域木の合計重み: $totalWeight2")

    graphData.clear()
    val inputList3 = listOf(
        Triple("A", "B", 4), Triple("B", "C", 3), Triple("D", "E", 5)
    )
    for ((v1, v2, weight) in inputList3) {
        graphData.addEdge(v1, v2, weight)
    }
    println("\nグラフの頂点: ${graphData.getVertices()}")
    println("グラフの辺 (重み付き): ${graphData.getEdges()}")
    val outputMst3 = graphData.getMst()
    for ((v1, v2, weight) in outputMst3) {
        println("Edge: $v1 - $v2, Weight: $weight")
    }
    val totalWeight3 = outputMst3.sumOf { it.third }
    println("最小全域木の合計重み: $totalWeight3")

    graphData.clear()
    val inputList4 = listOf<Triple<String, String, Int>>()
    for ((v1, v2, weight) in inputList4) {
        graphData.addEdge(v1, v2, weight)
    }
    println("\nグラフの頂点: ${graphData.getVertices()}")
    println("グラフの辺 (重み付き): ${graphData.getEdges()}")
    val outputMst4 = graphData.getMst()
    for ((v1, v2, weight) in outputMst4) {
        println("Edge: $v1 - $v2, Weight: $weight")
    }
    val totalWeight4 = outputMst4.sumBy { it.third }
    println("最小全域木の合計重み: $totalWeight4")

    println("\nKruskal TEST <----- end")
}