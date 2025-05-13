// Kotlin
// グラフの連結成分: BFS

import java.util.*

class GraphData {
    private val _data: MutableMap<String, MutableList<Pair<String, Int>>> = mutableMapOf()

    fun get(): Map<String, List<Pair<String, Int>>> = _data

    fun getVertices(): List<String> = _data.keys.toList()
    
    fun getEdges(): List<Triple<String, String, Int>> {
        val edges = mutableSetOf<Triple<String, String, Int>>()
        for (vertex in _data) {
            for ((neighbor, weight) in vertex.value) {
                val sortedEdge = listOf(vertex.key, neighbor).sorted()
                edges.add(Triple(sortedEdge[0], sortedEdge[1], weight))
            }
        }
        return edges.toList()
    }
    
    fun getNeighbors(vertex: String): List<Pair<String, Int>>? {
        return _data[vertex]
    }

    fun getEdgeWeight(vertex1: String, vertex2: String): Int? {
        return _data[vertex1]?.find { it.first == vertex2 }?.second
    }

    fun getVertice(vertex: String): List<Pair<String, Int>>? {
        return if (vertex in _data) {
            _data[vertex]
        } else {
            println("ERROR: $vertex は範囲外です")
            null
        }
    }

    fun getEdge(vertex1: String, vertex2: String): Boolean {
        return vertex1 in _data && vertex2 in _data && 
               _data[vertex1]?.any { it.first == vertex2 } == true
    }

    fun addVertex(vertex: String): Boolean {
        if (vertex !in _data) {
            _data[vertex] = mutableListOf()
            return true
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
        
        val v1Edges = _data[vertex1]!!
        val v2Edges = _data[vertex2]!!
        
        val existsV1V2 = v1Edges.indexOfFirst { it.first == vertex2 }.let { index ->
            if (index != -1) {
                v1Edges[index] = Pair(vertex2, weight)
                true
            } else {
                v1Edges.add(Pair(vertex2, weight))
                false
            }
        }
        
        val existsV2V1 = v2Edges.indexOfFirst { it.first == vertex1 }.let { index ->
            if (index != -1) {
                v2Edges[index] = Pair(vertex1, weight)
                true
            } else {
                v2Edges.add(Pair(vertex1, weight))
                false
            }
        }
        
        return true
    }
    
    fun removeVertex(vertex: String): Boolean {
        return if (vertex in _data) {
            _data.forEach { (_, edges) ->
                edges.removeIf { it.first == vertex }
            }
            _data.remove(vertex)
            true
        } else {
            println("ERROR: $vertex は範囲外です")
            false
        }
    }

    fun removeEdge(vertex1: String, vertex2: String): Boolean {
        return if (vertex1 in _data && vertex2 in _data) {
            val v1Removed = _data[vertex1]!!.removeIf { it.first == vertex2 }
            val v2Removed = _data[vertex2]!!.removeIf { it.first == vertex1 }
            v1Removed || v2Removed
        } else {
            println("ERROR: $vertex1 または $vertex2 は範囲外です")
            false
        }
    }

    fun isEmpty(): Boolean = _data.isEmpty()
    
    fun size(): Int = _data.size
    
    fun clear(): Boolean {
        _data.clear()
        return true
    }
    
    fun getConnectedComponents(): List<List<String>> {
        val visited = mutableSetOf<String>()
        val allComponents = mutableListOf<List<String>>()

        val vertices = getVertices()

        for (vertex in vertices) {
            if (vertex !in visited) {
                val currentComponent = mutableListOf<String>()
                val queue: Queue<String> = LinkedList()
                queue.add(vertex)
                visited.add(vertex)
                currentComponent.add(vertex)

                while (queue.isNotEmpty()) {
                    val u = queue.poll()

                    val neighborsWithWeight = getNeighbors(u)

                    neighborsWithWeight?.forEach { (neighbor, _) ->
                        if (neighbor !in visited) {
                            visited.add(neighbor)
                            queue.add(neighbor)
                            currentComponent.add(neighbor)
                        }
                    }
                }

                allComponents.add(currentComponent)
            }
        }

        return allComponents
    }
}

fun main() {
    println("Bfs TEST -----> start")

    println("\nnew")
    val graphData = GraphData()
    println("  現在のデータ: ${graphData.get()}")

    println("\nadd_edge")
    graphData.clear()
    val inputList1 = listOf(
        Triple("A", "B", 4), 
        Triple("B", "C", 3), 
        Triple("B", "D", 2), 
        Triple("D", "A", 1), 
        Triple("A", "C", 2), 
        Triple("B", "D", 2)
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
        Triple("A", "B", 4), 
        Triple("C", "D", 4), 
        Triple("E", "F", 1), 
        Triple("F", "G", 1)
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
        Triple("A", "B", 4), 
        Triple("B", "C", 3), 
        Triple("D", "E", 5)
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

    println("Bfs TEST <----- end")
}

// プログラムのエントリーポイント
fun main(args: Array<String>) {
    main()
}
