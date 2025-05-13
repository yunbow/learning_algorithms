// Go
// グラフの連結成分: DFS

package main

import (
	"fmt"
)

type EdgeInfo struct {
	Vertex string
	Weight int
}

type GraphData struct {
	data map[string][]EdgeInfo
}

func NewGraphData() *GraphData {
	return &GraphData{
		data: make(map[string][]EdgeInfo),
	}
}

func (g *GraphData) Get() map[string][]EdgeInfo {
	return g.data
}

func (g *GraphData) GetVertices() []string {
	vertices := make([]string, 0, len(g.data))
	for vertex := range g.data {
		vertices = append(vertices, vertex)
	}
	return vertices
}

func (g *GraphData) GetEdges() []struct {
	Vertex1 string
	Vertex2 string
	Weight  int
} {
	edges := make(map[string]struct {
		Vertex1 string
		Vertex2 string
		Weight  int
	})
	for vertex, neighbors := range g.data {
		for _, neighbor := range neighbors {
			// Create a sorted edge key to avoid duplicates
			var edge1, edge2 string
			if vertex < neighbor.Vertex {
				edge1 = vertex
				edge2 = neighbor.Vertex
			} else {
				edge1 = neighbor.Vertex
				edge2 = vertex
			}
			edgeKey := edge1 + "-" + edge2

			if _, exists := edges[edgeKey]; !exists {
				edges[edgeKey] = struct {
					Vertex1 string
					Vertex2 string
					Weight  int
				}{
					Vertex1: edge1,
					Vertex2: edge2,
					Weight:  neighbor.Weight,
				}
			}
		}
	}

	result := make([]struct {
		Vertex1 string
		Vertex2 string
		Weight  int
	}, 0, len(edges))
	for _, edge := range edges {
		result = append(result, edge)
	}
	return result
}

func (g *GraphData) GetNeighbors(vertex string) []EdgeInfo {
	return g.data[vertex]
}

func (g *GraphData) GetEdgeWeight(vertex1, vertex2 string) (int, bool) {
	for _, neighbor := range g.data[vertex1] {
		if neighbor.Vertex == vertex2 {
			return neighbor.Weight, true
		}
	}
	return 0, false
}

func (g *GraphData) GetVertice(vertex string) []EdgeInfo {
	if neighbors, exists := g.data[vertex]; exists {
		return neighbors
	}
	fmt.Printf("ERROR: %s is out of range\n", vertex)
	return nil
}

func (g *GraphData) GetEdge(vertex1, vertex2 string) bool {
	for _, neighbor := range g.data[vertex1] {
		if neighbor.Vertex == vertex2 {
			return true
		}
	}
	return false
}

func (g *GraphData) AddVertex(vertex string) bool {
	if _, exists := g.data[vertex]; !exists {
		g.data[vertex] = []EdgeInfo{}
	}
	return true
}

func (g *GraphData) AddEdge(vertex1, vertex2 string, weight int) bool {
	g.AddVertex(vertex1)
	g.AddVertex(vertex2)

	// Add or update edge from vertex1 to vertex2
	found1 := false
	for i, neighbor := range g.data[vertex1] {
		if neighbor.Vertex == vertex2 {
			g.data[vertex1][i].Weight = weight
			found1 = true
			break
		}
	}
	if !found1 {
		g.data[vertex1] = append(g.data[vertex1], EdgeInfo{Vertex: vertex2, Weight: weight})
	}

	// Add or update edge from vertex2 to vertex1
	found2 := false
	for i, neighbor := range g.data[vertex2] {
		if neighbor.Vertex == vertex1 {
			g.data[vertex2][i].Weight = weight
			found2 = true
			break
		}
	}
	if !found2 {
		g.data[vertex2] = append(g.data[vertex2], EdgeInfo{Vertex: vertex1, Weight: weight})
	}

	return true
}

func (g *GraphData) RemoveVertex(vertex string) bool {
	if _, exists := g.data[vertex]; exists {
		// Remove references to this vertex from other vertices
		for v := range g.data {
			if v != vertex {
				newNeighbors := []EdgeInfo{}
				for _, neighbor := range g.data[v] {
					if neighbor.Vertex != vertex {
						newNeighbors = append(newNeighbors, neighbor)
					}
				}
				g.data[v] = newNeighbors
			}
		}
		// Remove the vertex itself
		delete(g.data, vertex)
		return true
	}
	fmt.Printf("ERROR: %s is out of range\n", vertex)
	return false
}

func (g *GraphData) RemoveEdge(vertex1, vertex2 string) bool {
	if _, exists1 := g.data[vertex1]; !exists1 {
		fmt.Printf("ERROR: %s is out of range\n", vertex1)
		return false
	}
	if _, exists2 := g.data[vertex2]; !exists2 {
		fmt.Printf("ERROR: %s is out of range\n", vertex2)
		return false
	}

	removed := false
	// Remove edge from vertex1 to vertex2
	newNeighbors1 := []EdgeInfo{}
	for _, neighbor := range g.data[vertex1] {
		if neighbor.Vertex != vertex2 {
			newNeighbors1 = append(newNeighbors1, neighbor)
		} else {
			removed = true
		}
	}
	g.data[vertex1] = newNeighbors1

	// Remove edge from vertex2 to vertex1
	newNeighbors2 := []EdgeInfo{}
	for _, neighbor := range g.data[vertex2] {
		if neighbor.Vertex != vertex1 {
			newNeighbors2 = append(newNeighbors2, neighbor)
		} else {
			removed = true
		}
	}
	g.data[vertex2] = newNeighbors2

	return removed
}

func (g *GraphData) IsEmpty() bool {
	return len(g.data) == 0
}

func (g *GraphData) Size() int {
	return len(g.data)
}

func (g *GraphData) Clear() bool {
	g.data = make(map[string][]EdgeInfo)
	return true
}

func (g *GraphData) dfs(vertex string, visited map[string]bool, currentComponent *[]string) {
	visited[vertex] = true
	*currentComponent = append(*currentComponent, vertex)

	for _, neighborInfo := range g.data[vertex] {
		neighborVertex := neighborInfo.Vertex
		if !visited[neighborVertex] {
			g.dfs(neighborVertex, visited, currentComponent)
		}
	}
}

func (g *GraphData) GetConnectedComponents() [][]string {
	visited := make(map[string]bool)
	var connectedComponents [][]string

	for _, vertex := range g.GetVertices() {
		if !visited[vertex] {
			currentComponent := []string{}
			g.dfs(vertex, visited, &currentComponent)
			connectedComponents = append(connectedComponents, currentComponent)
		}
	}

	return connectedComponents
}

func main() {
	fmt.Println("Dfs TEST -----> start")

	fmt.Println("\nnew")
	graphData := NewGraphData()
	fmt.Printf("  Current data: %v\n", graphData.Get())

	fmt.Println("\nadd_edge")
	graphData.Clear()
	inputList := []struct {
		Vertex1 string
		Vertex2 string
		Weight  int
	}{
		{"A", "B", 4}, {"B", "C", 3}, {"B", "D", 2}, {"D", "A", 1}, {"A", "C", 2}, {"B", "D", 2},
	}
	for _, input := range inputList {
		fmt.Printf("  Input: (%s, %s, %d)\n", input.Vertex1, input.Vertex2, input.Weight)
		output := graphData.AddEdge(input.Vertex1, input.Vertex2, input.Weight)
		fmt.Printf("  Output: %v\n", output)
	}
	fmt.Printf("  Current data: %v\n", graphData.Get())
	fmt.Println("\nget_connected_components")
	output := graphData.GetConnectedComponents()
	fmt.Printf("  Connected components: %v\n", output)

	fmt.Println("\nadd_edge")
	graphData.Clear()
	inputList = []struct {
		Vertex1 string
		Vertex2 string
		Weight  int
	}{
		{"A", "B", 4}, {"C", "D", 4}, {"E", "F", 1}, {"F", "G", 1},
	}
	for _, input := range inputList {
		fmt.Printf("  Input: (%s, %s, %d)\n", input.Vertex1, input.Vertex2, input.Weight)
		output := graphData.AddEdge(input.Vertex1, input.Vertex2, input.Weight)
		fmt.Printf("  Output: %v\n", output)
	}
	fmt.Printf("  Current data: %v\n", graphData.Get())
	fmt.Println("\nget_connected_components")
	output = graphData.GetConnectedComponents()
	fmt.Printf("  Connected components: %v\n", output)

	fmt.Println("\nadd_edge")
	graphData.Clear()
	inputList = []struct {
		Vertex1 string
		Vertex2 string
		Weight  int
	}{
		{"A", "B", 4}, {"B", "C", 3}, {"D", "E", 5},
	}
	for _, input := range inputList {
		fmt.Printf("  Input: (%s, %s, %d)\n", input.Vertex1, input.Vertex2, input.Weight)
		output := graphData.AddEdge(input.Vertex1, input.Vertex2, input.Weight)
		fmt.Printf("  Output: %v\n", output)
	}
	fmt.Printf("  Current data: %v\n", graphData.Get())
	fmt.Println("\nget_connected_components")
	output = graphData.GetConnectedComponents()
	fmt.Printf("  Connected components: %v\n", output)

	fmt.Println("\nadd_edge")
	graphData.Clear()
	inputList = []struct {
		Vertex1 string
		Vertex2 string
		Weight  int
	}{}
	for _, input := range inputList {
		fmt.Printf("  Input: (%s, %s, %d)\n", input.Vertex1, input.Vertex2, input.Weight)
		output := graphData.AddEdge(input.Vertex1, input.Vertex2, input.Weight)
		fmt.Printf("  Output: %v\n", output)
	}
	fmt.Printf("  Current data: %v\n", graphData.Get())
	fmt.Println("\nget_connected_components")
	output = graphData.GetConnectedComponents()
	fmt.Printf("  Connected components: %v\n", output)

	fmt.Println("Dfs TEST <----- end")
}
