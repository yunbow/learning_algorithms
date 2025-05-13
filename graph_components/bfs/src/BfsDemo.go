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

func (g *GraphData) GetEdges() []struct {
	vertex1 string
	vertex2 string
	weight  int
} {
	edges := make(map[string]struct{})
	var result []struct {
		vertex1 string
		vertex2 string
		weight  int
	}

	for vertex, neighbors := range g.data {
		for _, neighbor := range neighbors {
			var key string
			if vertex < neighbor.vertex {
				key = vertex + "," + neighbor.vertex
			} else {
				key = neighbor.vertex + "," + vertex
			}

			if _, exists := edges[key]; !exists {
				edges[key] = struct{}{}
				result = append(result, struct {
					vertex1 string
					vertex2 string
					weight  int
				}{
					vertex1: vertex,
					vertex2: neighbor.vertex,
					weight:  neighbor.weight,
				})
			}
		}
	}
	return result
}

func (g *GraphData) GetNeighbors(vertex string) []neighborEntry {
	if neighbors, exists := g.data[vertex]; exists {
		return neighbors
	}
	return nil
}

func (g *GraphData) GetEdgeWeight(vertex1, vertex2 string) *int {
	if neighbors, exists := g.data[vertex1]; exists {
		for _, neighbor := range neighbors {
			if neighbor.vertex == vertex2 {
				return &neighbor.weight
			}
		}
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

func (g *GraphData) GetEdge(vertex1, vertex2 string) bool {
	if neighbors, exists := g.data[vertex1]; exists {
		for _, neighbor := range neighbors {
			if neighbor.vertex == vertex2 {
				return true
			}
		}
	}
	return false
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

func (g *GraphData) RemoveVertex(vertex string) bool {
	if _, exists := g.data[vertex]; exists {
		// Remove references to this vertex from other vertices
		for v := range g.data {
			var newNeighbors []neighborEntry
			for _, neighbor := range g.data[v] {
				if neighbor.vertex != vertex {
					newNeighbors = append(newNeighbors, neighbor)
				}
			}
			g.data[v] = newNeighbors
		}

		// Remove the vertex itself
		delete(g.data, vertex)
		return true
	}
	fmt.Printf("ERROR: %s is out of range\n", vertex)
	return false
}

func (g *GraphData) RemoveEdge(vertex1, vertex2 string) bool {
	if _, exists1 := g.data[vertex1]; exists1 {
		if _, exists2 := g.data[vertex2]; exists2 {
			removed := false

			// Remove edge from vertex1 to vertex2
			var newNeighborsV1 []neighborEntry
			for _, neighbor := range g.data[vertex1] {
				if neighbor.vertex != vertex2 {
					newNeighborsV1 = append(newNeighborsV1, neighbor)
				}
			}
			if len(newNeighborsV1) < len(g.data[vertex1]) {
				removed = true
			}
			g.data[vertex1] = newNeighborsV1

			// Remove edge from vertex2 to vertex1
			var newNeighborsV2 []neighborEntry
			for _, neighbor := range g.data[vertex2] {
				if neighbor.vertex != vertex1 {
					newNeighborsV2 = append(newNeighborsV2, neighbor)
				}
			}
			if len(newNeighborsV2) < len(g.data[vertex2]) {
				removed = true
			}
			g.data[vertex2] = newNeighborsV2

			return removed
		}
	}
	fmt.Printf("ERROR: %s or %s is out of range\n", vertex1, vertex2)
	return false
}

func (g *GraphData) IsEmpty() bool {
	return len(g.data) == 0
}

func (g *GraphData) Size() int {
	return len(g.data)
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
