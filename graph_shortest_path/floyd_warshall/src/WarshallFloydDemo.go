// Go
// グラフの最短経路: ワーシャルフロイド法 (Warshall Floyd)

package main

import (
	"fmt"
	"math"
)

// Edge represents an edge in the graph with source, destination and weight
type Edge struct {
	Source      string
	Destination string
	Weight      float64
}

// GraphData represents a graph with vertices and edges
type GraphData struct {
	data map[string][]struct {
		Neighbor string
		Weight   float64
	}
}

// NewGraphData creates a new empty graph
func NewGraphData() *GraphData {
	return &GraphData{
		data: make(map[string][]struct {
			Neighbor string
			Weight   float64
		}),
	}
}

// Get returns the internal graph data
func (g *GraphData) Get() map[string][]struct {
	Neighbor string
	Weight   float64
} {
	return g.data
}

// GetVertices returns all vertices in the graph
func (g *GraphData) GetVertices() []string {
	vertices := make([]string, 0, len(g.data))
	for vertex := range g.data {
		vertices = append(vertices, vertex)
	}
	return vertices
}

// GetEdges returns all edges in the graph
func (g *GraphData) GetEdges() []Edge {
	edgeMap := make(map[string]Edge)
	
	for vertex, neighbors := range g.data {
		for _, neighbor := range neighbors {
			// Create a unique key for each edge to avoid duplicates
			var key string
			if vertex < neighbor.Neighbor {
				key = vertex + neighbor.Neighbor
			} else {
				key = neighbor.Neighbor + vertex
			}
			
			edgeMap[key] = Edge{
				Source:      vertex,
				Destination: neighbor.Neighbor,
				Weight:      neighbor.Weight,
			}
		}
	}
	
	edges := make([]Edge, 0, len(edgeMap))
	for _, edge := range edgeMap {
		edges = append(edges, edge)
	}
	
	return edges
}

// GetNeighbors returns all neighbors of a vertex
func (g *GraphData) GetNeighbors(vertex string) []struct {
	Neighbor string
	Weight   float64
} {
	if neighbors, exists := g.data[vertex]; exists {
		return neighbors
	}
	return nil
}

// AddVertex adds a new vertex to the graph
func (g *GraphData) AddVertex(vertex string) bool {
	if _, exists := g.data[vertex]; !exists {
		g.data[vertex] = []struct {
			Neighbor string
			Weight   float64
		}{}
	}
	return true
}

// AddEdge adds an edge between two vertices with a specified weight
func (g *GraphData) AddEdge(vertex1, vertex2 string, weight float64) bool {
	// Add vertices if they don't exist
	g.AddVertex(vertex1)
	g.AddVertex(vertex2)
	
	// Check if edge vertex1 -> vertex2 already exists and update it
	edgeExists12 := false
	for i, neighbor := range g.data[vertex1] {
		if neighbor.Neighbor == vertex2 {
			g.data[vertex1][i].Weight = weight
			edgeExists12 = true
			break
		}
	}
	if !edgeExists12 {
		g.data[vertex1] = append(g.data[vertex1], struct {
			Neighbor string
			Weight   float64
		}{
			Neighbor: vertex2,
			Weight:   weight,
		})
	}
	
	// Check if edge vertex2 -> vertex1 already exists and update it
	edgeExists21 := false
	for i, neighbor := range g.data[vertex2] {
		if neighbor.Neighbor == vertex1 {
			g.data[vertex2][i].Weight = weight
			edgeExists21 = true
			break
		}
	}
	if !edgeExists21 {
		g.data[vertex2] = append(g.data[vertex2], struct {
			Neighbor string
			Weight   float64
		}{
			Neighbor: vertex1,
			Weight:   weight,
		})
	}
	
	return true
}

// Clear empties the graph
func (g *GraphData) Clear() bool {
	g.data = make(map[string][]struct {
		Neighbor string
		Weight   float64
	})
	return true
}

// GetShortestPath finds the shortest path between two vertices using the Warshall-Floyd algorithm
func (g *GraphData) GetShortestPath(startVertex, endVertex string, heuristic func(string, string) float64) ([]string, float64) {
	vertices := g.GetVertices()
	numVertices := len(vertices)
	
	if numVertices == 0 {
		return nil, math.Inf(1)
	}
	
	// Map vertex names to indices
	vertexToIndex := make(map[string]int)
	indexToVertex := make(map[int]string)
	
	for i, vertex := range vertices {
		vertexToIndex[vertex] = i
		indexToVertex[i] = vertex
	}
	
	// Check if start and end vertices exist
	startIndex, startExists := vertexToIndex[startVertex]
	endIndex, endExists := vertexToIndex[endVertex]
	
	if !startExists || !endExists {
		fmt.Printf("ERROR: %s または %s がグラフに存在しません。\n", startVertex, endVertex)
		return nil, math.Inf(1)
	}
	
	// Initialize distance and next node matrices
	dist := make([][]float64, numVertices)
	nextNode := make([][]int, numVertices)
	
	for i := 0; i < numVertices; i++ {
		dist[i] = make([]float64, numVertices)
		nextNode[i] = make([]int, numVertices)
		
		for j := 0; j < numVertices; j++ {
			dist[i][j] = math.Inf(1)
			nextNode[i][j] = -1 // Using -1 instead of nil for Go
		}
	}
	
	// Set initial distances and paths
	for i := 0; i < numVertices; i++ {
		dist[i][i] = 0 // Distance to self is 0
		
		neighbors := g.GetNeighbors(vertices[i])
		if neighbors != nil {
			for _, neighbor := range neighbors {
				j := vertexToIndex[neighbor.Neighbor]
				dist[i][j] = neighbor.Weight
				nextNode[i][j] = j // Next node from i to j is j itself for direct edges
			}
		}
	}
	
	// Warshall-Floyd algorithm
	for k := 0; k < numVertices; k++ {
		for i := 0; i < numVertices; i++ {
			for j := 0; j < numVertices; j++ {
				if dist[i][k] != math.Inf(1) && dist[k][j] != math.Inf(1) && dist[i][k]+dist[k][j] < dist[i][j] {
					dist[i][j] = dist[i][k] + dist[k][j]
					nextNode[i][j] = nextNode[i][k]
				}
			}
		}
	}
	
	// Get shortest path and distance
	shortestDistance := dist[startIndex][endIndex]
	
	// If no path exists
	if shortestDistance == math.Inf(1) {
		return nil, math.Inf(1)
	}
	
	// Reconstruct the path
	path := []string{}
	u := startIndex
	
	// If start and end are the same
	if u == endIndex {
		path = []string{startVertex}
	} else {
		// Traverse the path using nextNode
		for u != -1 && u != endIndex {
			path = append(path, indexToVertex[u])
			u = nextNode[u][endIndex]
			
			// Simple check to prevent infinite loops
			if u != -1 && len(path) > 0 && indexToVertex[u] == path[len(path)-1] {
				fmt.Printf("WARNING: 経路復元中に異常を検出しました（%sでループ？）。\n", indexToVertex[u])
				return nil, math.Inf(1)
			}
		}
		
		// Add the end vertex
		if len(path) > 0 {
			path = append(path, endVertex)
		}
	}
	
	return path, shortestDistance
}

// DummyHeuristic is a placeholder heuristic function that always returns 0
func DummyHeuristic(u, v string) float64 {
	return 0
}

func main() {
	fmt.Println("WarshallFloyd -----> start")
	
	graphData := NewGraphData()
	
	graphData.Clear()
	inputList := []struct {
		v1     string
		v2     string
		weight float64
	}{
		{"A", "B", 4},
		{"B", "C", 3},
		{"B", "D", 2},
		{"D", "A", 1},
		{"A", "C", 2},
		{"B", "D", 2},
	}
	
	for _, input := range inputList {
		graphData.AddEdge(input.v1, input.v2, input.weight)
	}
	
	fmt.Printf("\nグラフの頂点: %v\n", graphData.GetVertices())
	fmt.Printf("グラフの辺 (重み付き): %v\n", graphData.GetEdges())
	
	input := struct {
		start string
		end   string
	}{"A", "B"}
	
	shortestPath, weight := graphData.GetShortestPath(input.start, input.end, DummyHeuristic)
	fmt.Printf("経路%s-%s の最短経路は %v (重み: %v)\n", input.start, input.end, shortestPath, weight)
	
	graphData.Clear()
	inputList = []struct {
		v1     string
		v2     string
		weight float64
	}{
		{"A", "B", 4},
		{"C", "D", 4},
		{"E", "F", 1},
		{"F", "G", 1},
	}
	
	for _, input := range inputList {
		graphData.AddEdge(input.v1, input.v2, input.weight)
	}
	
	fmt.Printf("\nグラフの頂点: %v\n", graphData.GetVertices())
	fmt.Printf("グラフの辺 (重み付き): %v\n", graphData.GetEdges())
	
	input = struct {
		start string
		end   string
	}{"A", "B"}
	
	shortestPath, weight = graphData.GetShortestPath(input.start, input.end, DummyHeuristic)
	fmt.Printf("経路%s-%s の最短経路は %v (重み: %v)\n", input.start, input.end, shortestPath, weight)
	
	graphData.Clear()
	inputList = []struct {
		v1     string
		v2     string
		weight float64
	}{
		{"A", "B", 4},
		{"B", "C", 3},
		{"D", "E", 5},
	}
	
	for _, input := range inputList {
		graphData.AddEdge(input.v1, input.v2, input.weight)
	}
	
	fmt.Printf("\nグラフの頂点: %v\n", graphData.GetVertices())
	fmt.Printf("グラフの辺 (重み付き): %v\n", graphData.GetEdges())
	
	input = struct {
		start string
		end   string
	}{"A", "D"}
	
	shortestPath, weight = graphData.GetShortestPath(input.start, input.end, DummyHeuristic)
	fmt.Printf("経路%s-%s の最短経路は %v (重み: %v)\n", input.start, input.end, shortestPath, weight)
	
	graphData.Clear()
	inputList = []struct {
		v1     string
		v2     string
		weight float64
	}{}
	
	for _, input := range inputList {
		graphData.AddEdge(input.v1, input.v2, input.weight)
	}
	
	fmt.Printf("\nグラフの頂点: %v\n", graphData.GetVertices())
	fmt.Printf("グラフの辺 (重み付き): %v\n", graphData.GetEdges())
	
	input = struct {
		start string
		end   string
	}{"A", "B"}
	
	shortestPath, weight = graphData.GetShortestPath(input.start, input.end, DummyHeuristic)
	fmt.Printf("経路%s-%s の最短経路は %v (重み: %v)\n", input.start, input.end, shortestPath, weight)
	
	fmt.Println("\nWarshallFloyd <----- end")
}