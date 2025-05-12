// Swift
// データ構造: グラフ (Graph)

import Foundation

class GraphData {
    // Internal data structure to store graph vertices and their edges
    private var _data: [String: [(vertex: String, weight: Int)]] = [:]
    
    // Get the entire graph data
    func get() -> [String: [(vertex: String, weight: Int)]] {
        return _data
    }
    
    // Get all vertices in the graph
    func getVertices() -> [String] {
        return Array(_data.keys)
    }
    
    // Get all edges in the graph
    func getEdges() -> [(String, String, Int)] {
        var edges = Set<(String, String, Int)>()
        for vertex in _data.keys {
            for (neighbor, weight) in _data[vertex] ?? [] {
                let sortedVertices = [vertex, neighbor].sorted()
                edges.insert((sortedVertices[0], sortedVertices[1], weight))
            }
        }
        return Array(edges)
    }
    
    // Get neighbors of a specific vertex
    func getNeighbors(_ vertex: String) -> [(vertex: String, weight: Int)]? {
        return _data[vertex]
    }
    
    // Get edge weight between two vertices
    func getEdgeWeight(vertex1: String, vertex2: String) -> Int? {
        guard let neighbors = _data[vertex1] else { return nil }
        return neighbors.first(where: { $0.vertex == vertex2 })?.weight
    }
    
    // Get a specific vertex's data
    func getVertice(_ vertex: String) -> [(vertex: String, weight: Int)]? {
        guard let vertexData = _data[vertex] else {
            print("ERROR: \(vertex) is out of range")
            return nil
        }
        return vertexData
    }
    
    // Check if an edge exists between two vertices
    func getEdge(vertex1: String, vertex2: String) -> Bool {
        guard let neighbors = _data[vertex1] else { return false }
        return neighbors.contains(where: { $0.vertex == vertex2 })
    }
    
    // Add a new vertex to the graph
    @discardableResult
    func addVertex(_ vertex: String) -> Bool {
        if _data[vertex] == nil {
            _data[vertex] = []
        }
        return true
    }
    
    // Add an edge between two vertices
    @discardableResult
    func addEdge(vertex1: String, vertex2: String, weight: Int) -> Bool {
        // Ensure vertices exist
        if _data[vertex1] == nil {
            addVertex(vertex1)
        }
        if _data[vertex2] == nil {
            addVertex(vertex2)
        }
        
        // Add edge from vertex1 to vertex2
        if let index = _data[vertex1]?.firstIndex(where: { $0.vertex == vertex2 }) {
            _data[vertex1]?[index] = (vertex2, weight)
        } else {
            _data[vertex1]?.append((vertex2, weight))
        }
        
        // Add edge from vertex2 to vertex1 (undirected graph)
        if let index = _data[vertex2]?.firstIndex(where: { $0.vertex == vertex1 }) {
            _data[vertex2]?[index] = (vertex1, weight)
        } else {
            _data[vertex2]?.append((vertex1, weight))
        }
        
        return true
    }
    
    // Remove a vertex from the graph
    @discardableResult
    func removeVertex(_ vertex: String) -> Bool {
        guard _data[vertex] != nil else {
            print("ERROR: \(vertex) is out of range")
            return false
        }
        
        // Remove references to this vertex from other vertices
        for (v, neighbors) in _data {
            _data[v] = neighbors.filter { $0.vertex != vertex }
        }
        
        // Remove the vertex itself
        _data.removeValue(forKey: vertex)
        return true
    }
    
    // Remove an edge between two vertices
    @discardableResult
    func removeEdge(vertex1: String, vertex2: String) -> Bool {
        guard _data[vertex1] != nil && _data[vertex2] != nil else {
            print("ERROR: \(vertex1) or \(vertex2) is out of range")
            return false
        }
        
        var removed = false
        
        // Remove edge from vertex1 to vertex2
        let originalLenV1 = _data[vertex1]?.count ?? 0
        _data[vertex1] = _data[vertex1]?.filter { $0.vertex != vertex2 }
        if (_data[vertex1]?.count ?? 0) < originalLenV1 {
            removed = true
        }
        
        // Remove edge from vertex2 to vertex1
        let originalLenV2 = _data[vertex2]?.count ?? 0
        _data[vertex2] = _data[vertex2]?.filter { $0.vertex != vertex1 }
        if (_data[vertex2]?.count ?? 0) < originalLenV2 {
            removed = true
        }
        
        return removed
    }
    
    // Check if the graph is empty
    func isEmpty() -> Bool {
        return _data.isEmpty
    }
    
    // Get the number of vertices
    func size() -> Int {
        return _data.count
    }
    
    // Clear the entire graph
    @discardableResult
    func clear() -> Bool {
        _data.removeAll()
        return true
    }
}

// Example usage demonstrating the graph functionality
func main() {
    print("Graph TEST -----> start")

    print("\nnew")
    let graphData = GraphData()
    print("  Current data: \(graphData.get())")

    print("\nis_empty")
    let isEmptyOutput = graphData.isEmpty()
    print("  Output value: \(isEmptyOutput)")

    print("\nsize")
    let sizeOutput = graphData.size()
    print("  Output value: \(sizeOutput)")

    let inputList = ["A", "B", "C"]
    for input in inputList {
        print("\nadd_vertex")
        print("  Input value: \(input)")
        let output = graphData.addVertex(input)
        print("  Output value: \(output)")
    }
    print("  Current data: \(graphData.get())")

    print("\nget_vertices")
    let verticesOutput = graphData.getVertices()
    print("  Output value: \(verticesOutput)")

    print("\nsize")
    let newSizeOutput = graphData.size()
    print("  Output value: \(newSizeOutput)")

    print("\nadd_edge")
    let edgeInputList = [("A", "B", 4), ("B", "C", 2), ("C", "A", 3)]
    for (v1, v2, weight) in edgeInputList {
        print("  Input value: (\(v1), \(v2), \(weight))")
        let output = graphData.addEdge(vertex1: v1, vertex2: v2, weight: weight)
        print("  Output value: \(output)")
    }
    print("  Current data: \(graphData.get())")

    print("\nget_vertices")
    let newVerticesOutput = graphData.getVertices()
    print("  Output value: \(newVerticesOutput)")

    print("\nget_edges")
    let edgesOutput = graphData.getEdges()
    print("  Output value: \(edgesOutput)")

    print("\nsize")
    let currentSizeOutput = graphData.size()
    print("  Output value: \(currentSizeOutput)")

    print("\nget_vertice")
    let verticeInput = "B"
    print("  Input value: '\(verticeInput)'")
    let verticeOutput = graphData.getVertice(verticeInput)
    print("  Output value: \(String(describing: verticeOutput))")

    print("\nget_vertice")
    let verticeInput2 = "E"
    print("  Input value: '\(verticeInput2)'")
    let verticeOutput2 = graphData.getVertice(verticeInput2)
    print("  Output value: \(String(describing: verticeOutput2))")

    print("\nremove_edge")
    let removeEdgeInput = ("A", "B")
    print("  Input value: \(removeEdgeInput)")
    let removeEdgeOutput = graphData.removeEdge(vertex1: removeEdgeInput.0, vertex2: removeEdgeInput.1)
    print("  Output value: \(removeEdgeOutput)")
    print("  Current data: \(graphData.get())")

    print("\nremove_edge")
    let removeEdgeInput2 = ("A", "C")
    print("  Input value: \(removeEdgeInput2)")
    let removeEdgeOutput2 = graphData.removeEdge(vertex1: removeEdgeInput2.0, vertex2: removeEdgeInput2.1)
    print("  Output value: \(removeEdgeOutput2)")
    print("  Current data: \(graphData.get())")

    print("\nget_edges")
    let finalEdgesOutput = graphData.getEdges()
    print("  Output value: \(finalEdgesOutput)")

    print("\nremove_vertex")
    let removeVertexInput = "B"
    print("  Input value: \(removeVertexInput)")
    let removeVertexOutput = graphData.removeVertex(removeVertexInput)
    print("  Output value: \(removeVertexOutput)")
    print("  Current data: \(graphData.get())")

    print("\nremove_vertex")
    let removeVertexInput2 = "Z"
    print("  Input value: \(removeVertexInput2)")
    let removeVertexOutput2 = graphData.removeVertex(removeVertexInput2)
    print("  Output value: \(removeVertexOutput2)")
    print("  Current data: \(graphData.get())")

    print("\nget_vertices")
    let finalVerticesOutput = graphData.getVertices()
    print("  Output value: \(finalVerticesOutput)")

    print("\nget_edges")
    let finalEdgesOutput2 = graphData.getEdges()
    print("  Output value: \(finalEdgesOutput2)")

    print("\nsize")
    let finalSizeOutput = graphData.size()
    print("  Output value: \(finalSizeOutput)")

    print("\nget_vertice")
    let finalVerticeInput = "B"
    print("  Input value: \(finalVerticeInput)")
    let finalVerticeOutput = graphData.getVertice(finalVerticeInput)
    print("  Output value: \(String(describing: finalVerticeOutput))")

    print("\nclear")
    let clearOutput = graphData.clear()
    print("  Output value: \(clearOutput)")

    print("\nis_empty")
    let finalIsEmptyOutput = graphData.isEmpty()
    print("  Output value: \(finalIsEmptyOutput)")

    print("\nsize")
    let finalSizeOutput2 = graphData.size()
    print("  Output value: \(finalSizeOutput2)")

    print("\nget_vertices")
    let finalVerticesOutput2 = graphData.getVertices()
    print("  Output value: \(finalVerticesOutput2)")

    print("\nget_edges")
    let finalEdgesOutput3 = graphData.getEdges()
    print("  Output value: \(finalEdgesOutput3)")

    print("\nGraph TEST <----- end")
}

// Uncomment the following line to run the main function
// main()