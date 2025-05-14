// Go
// グラフの連結成分: BFS

package main

import (
	"container/list"
	"fmt"
)

type GraphData struct {
	data map[string][]neighborEntry
}

type neighborEntry struct {
	vertex string
	weight int
}

func NewGraphData() *GraphData {
	return &GraphData{
		data: make(map[string][]neighborEntry),
	}
}

func (g *GraphData) Get() map[string][]neighborEntry {
	return g.data
}

func (g *GraphData) GetVertices() []string {
	vertices := make([]string, 0, len(g.data))
	for v := range g.data {
		vertices = append(vertices, v)
	}
	return vertices
}

func (g *GraphData) GetNeighbors(vertex string) []neighborEntry {
	if neighbors, exists := g.data[vertex]; exists {
		return neighbors
	}
	return nil
}

func (g *GraphData) GetVertice(vertex string) []neighborEntry {
	if neighbors, exists := g.data[vertex]; exists {
		return neighbors
	}
	fmt.Printf("ERROR: %s is out of range\n", vertex)
	return nil
}

func (g *GraphData) AddVertex(vertex string) bool {
	if _, exists := g.data[vertex]; !exists {
		g.data[vertex] = []neighborEntry{}
	}
	return true
}

func (g *GraphData) AddEdge(vertex1, vertex2 string, weight int) bool {
	g.AddVertex(vertex1)
	g.AddVertex(vertex2)

	// Add or update edge from vertex1 to vertex2
	updatedV1 := false
	for i, neighbor := range g.data[vertex1] {
		if neighbor.vertex == vertex2 {
			g.data[vertex1][i] = neighborEntry{vertex2, weight}
			updatedV1 = true
			break
		}
	}
	if !updatedV1 {
		g.data[vertex1] = append(g.data[vertex1], neighborEntry{vertex2, weight})
	}

	// Add or update edge from vertex2 to vertex1
	updatedV2 := false
	for i, neighbor := range g.data[vertex2] {
		if neighbor.vertex == vertex1 {
			g.data[vertex2][i] = neighborEntry{vertex1, weight}
			updatedV2 = true
			break
		}
	}
	if !updatedV2 {
		g.data[vertex2] = append(g.data[vertex2], neighborEntry{vertex1, weight})
	}

	return true
}

func (g *GraphData) Clear() bool {
	g.data = make(map[string][]neighborEntry)
	return true
}

func (g *GraphData) GetConnectedComponents() [][]string {
	visited := make(map[string]bool)
	var allComponents [][]string

	for vertex := range g.data {
		if !visited[vertex] {
			var currentComponent []string
			queue := list.New()
			queue.PushBack(vertex)
			visited[vertex] = true
			currentComponent = append(currentComponent, vertex)

			for queue.Len() > 0 {
				u := queue.Front().Value.(string)
				queue.Remove(queue.Front())

				neighbors := g.GetNeighbors(u)
				if neighbors != nil {
					for _, neighbor := range neighbors {
						if !visited[neighbor.vertex] {
							visited[neighbor.vertex] = true
							queue.PushBack(neighbor.vertex)
							currentComponent = append(currentComponent, neighbor.vertex)
						}
					}
				}
			}

			allComponents = append(allComponents, currentComponent)
		}
	}

	return allComponents
}

func main() {
	fmt.Println("Bfs TEST -----> start")

	fmt.Println("\nnew")
	graphData := NewGraphData()
	fmt.Printf("  Current data: %v\n", graphData.Get())

	fmt.Println("\nadd_edge")
	graphData.Clear()
	inputList := [][3]interface{}{
		{"A", "B", 4}, {"B", "C", 3}, {"B", "D", 2}, 
		{"D", "A", 1}, {"A", "C", 2}, {"B", "D", 2},
	}
	for _, input := range inputList {
		fmt.Printf("  Input: %v\n", input)
		output := graphData.AddEdge(input[0].(string), input[1].(string), input[2].(int))
		fmt.Printf("  Output: %v\n", output)
	}
	fmt.Printf("  Current data: %v\n", graphData.Get())
	fmt.Println("\nget_connected_components")
	output := graphData.GetConnectedComponents()
	fmt.Printf("  Connected components: %v\n", output)

	fmt.Println("\nadd_edge")
	graphData.Clear()
	inputList = [][3]interface{}{
		{"A", "B", 4}, {"C", "D", 4}, {"E", "F", 1}, {"F", "G", 1},
	}
	for _, input := range inputList {
		fmt.Printf("  Input: %v\n", input)
		output := graphData.AddEdge(input[0].(string), input[1].(string), input[2].(int))
		fmt.Printf("  Output: %v\n", output)
	}
	fmt.Printf("  Current data: %v\n", graphData.Get())
	fmt.Println("\nget_connected_components")
	output = graphData.GetConnectedComponents()
	fmt.Printf("  Connected components: %v\n", output)

	fmt.Println("\nadd_edge")
	graphData.Clear()
	inputList = [][3]interface{}{
		{"A", "B", 4}, {"B", "C", 3}, {"D", "E", 5},
	}
	for _, input := range inputList {
		fmt.Printf("  Input: %v\n", input)
		output := graphData.AddEdge(input[0].(string), input[1].(string), input[2].(int))
		fmt.Printf("  Output: %v\n", output)
	}
	fmt.Printf("  Current data: %v\n", graphData.Get())
	fmt.Println("\nget_connected_components")
	output = graphData.GetConnectedComponents()
	fmt.Printf("  Connected components: %v\n", output)

	fmt.Println("\nadd_edge")
	graphData.Clear()
	inputList = [][3]interface{}{}
	for _, input := range inputList {
		fmt.Printf("  Input: %v\n", input)
		output := graphData.AddEdge(input[0].(string), input[1].(string), input[2].(int))
		fmt.Printf("  Output: %v\n", output)
	}
	fmt.Printf("  Current data: %v\n", graphData.Get())
	fmt.Println("\nget_connected_components")
	output = graphData.GetConnectedComponents()
	fmt.Printf("  Connected components: %v\n", output)

	fmt.Println("Bfs TEST <----- end")
}
