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
