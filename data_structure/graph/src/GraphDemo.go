// Go
// データ構造: グラフ (Graph)

package main

import (
	"fmt"
)

type Edge struct {
	neighbor string
	weight   int
}

type GraphData struct {
	data map[string][]Edge
}

func NewGraphData() *GraphData {
	return &GraphData{
		data: make(map[string][]Edge),
	}
}

func (g *GraphData) Get() map[string][]Edge {
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
	u, v string
	weight int
} {
	edges := make(map[string]struct{})
	var uniqueEdges []struct {
		u, v string
		weight int
	}

	for vertex, neighbors := range g.data {
		for _, edge := range neighbors {
			// Create a sorted edge representation
			var u, v string
			if vertex < edge.neighbor {
				u, v = vertex, edge.neighbor
			} else {
				u, v = edge.neighbor, vertex
			}

			key := fmt.Sprintf("%s-%s", u, v)
			if _, exists := edges[key]; !exists {
				edges[key] = struct{}{}
				uniqueEdges = append(uniqueEdges, struct {
					u, v string
					weight int
				}{u, v, edge.weight})
			}
		}
	}

	return uniqueEdges
}

func (g *GraphData) GetNeighbors(vertex string) []Edge {
	if neighbors, exists := g.data[vertex]; exists {
		return neighbors
	}
	return nil
}

func (g *GraphData) GetEdgeWeight(vertex1, vertex2 string) (int, bool) {
	if neighbors, exists := g.data[vertex1]; exists {
		for _, edge := range neighbors {
			if edge.neighbor == vertex2 {
				return edge.weight, true
			}
		}
	}
	return 0, false
}

func (g *GraphData) GetVertice(vertex string) []Edge {
	if neighbors, exists := g.data[vertex]; exists {
		return neighbors
	}
	fmt.Printf("ERROR: %s is out of range\n", vertex)
	return nil
}

func (g *GraphData) GetEdge(vertex1, vertex2 string) bool {
	if neighbors, exists := g.data[vertex1]; exists {
		for _, edge := range neighbors {
			if edge.neighbor == vertex2 {
				return true
			}
		}
	}
	return false
}

func (g *GraphData) AddVertex(vertex string) bool {
	if _, exists := g.data[vertex]; !exists {
		g.data[vertex] = []Edge{}
	}
	return true
}

func (g *GraphData) AddEdge(vertex1, vertex2 string, weight int) bool {
	// Ensure vertices exist
	g.AddVertex(vertex1)
	g.AddVertex(vertex2)

	// Add edge for vertex1 to vertex2
	found1 := false
	for i, edge := range g.data[vertex1] {
		if edge.neighbor == vertex2 {
			g.data[vertex1][i] = Edge{neighbor: vertex2, weight: weight}
			found1 = true
			break
		}
	}
	if !found1 {
		g.data[vertex1] = append(g.data[vertex1], Edge{neighbor: vertex2, weight: weight})
	}

	// Add edge for vertex2 to vertex1
	found2 := false
	for i, edge := range g.data[vertex2] {
		if edge.neighbor == vertex1 {
			g.data[vertex2][i] = Edge{neighbor: vertex1, weight: weight}
			found2 = true
			break
		}
	}
	if !found2 {
		g.data[vertex2] = append(g.data[vertex2], Edge{neighbor: vertex1, weight: weight})
	}

	return true
}

func (g *GraphData) RemoveVertex(vertex string) bool {
	if _, exists := g.data[vertex]; exists {
		// Remove references to this vertex from other vertices
		for v := range g.data {
			if v != vertex {
				g.RemoveEdge(v, vertex)
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
	newNeighbors1 := []Edge{}
	for _, edge := range g.data[vertex1] {
		if edge.neighbor != vertex2 {
			newNeighbors1 = append(newNeighbors1, edge)
		} else {
			removed = true
		}
	}
	g.data[vertex1] = newNeighbors1

	// Remove edge from vertex2 to vertex1
	newNeighbors2 := []Edge{}
	for _, edge := range g.data[vertex2] {
		if edge.neighbor != vertex1 {
			newNeighbors2 = append(newNeighbors2, edge)
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
	g.data = make(map[string][]Edge)
	return true
}

func main() {
	fmt.Println("Graph TEST -----> start")

	fmt.Println("\nnew")
	graphData := NewGraphData()
	fmt.Printf("  Current data: %v\n", graphData.Get())

	fmt.Println("\nis_empty")
	output := graphData.IsEmpty()
	fmt.Printf("  Output value: %v\n", output)

	fmt.Println("\nsize")
	output2 := graphData.Size()
	fmt.Printf("  Output value: %d\n", output2)

	inputList := []string{"A", "B", "C"}
	for _, input := range inputList {
		fmt.Println("\nadd_vertex")
		fmt.Printf("  Input value: %s\n", input)
		output3 := graphData.AddVertex(input)
		fmt.Printf("  Output value: %v\n", output3)
	}
	fmt.Printf("  Current data: %v\n", graphData.Get())

	fmt.Println("\nget_vertices")
	outputVertices := graphData.GetVertices()
	fmt.Printf("  Output value: %v\n", outputVertices)

	fmt.Println("\nsize")
	output2 = graphData.Size()
	fmt.Printf("  Output value: %d\n", output2)

	fmt.Println("\nadd_edge")
	inputList2 := [][3]interface{}{
		{"A", "B", 4},
		{"B", "C", 2},
		{"C", "A", 3},
	}
	for _, input := range inputList2 {
		fmt.Printf("  Input value: %v\n", input)
		output4 := graphData.AddEdge(input[0].(string), input[1].(string), input[2].(int))
		fmt.Printf("  Output value: %v\n", output4)
	}
	fmt.Printf("  Current data: %v\n", graphData.Get())

	fmt.Println("\nget_vertices")
	outputVertices = graphData.GetVertices()
	fmt.Printf("  Output value: %v\n", outputVertices)

	fmt.Println("\nget_edges")
	outputEdges := graphData.GetEdges()
	fmt.Printf("  Output value: %v\n", outputEdges)

	fmt.Println("\nsize")
	output2 = graphData.Size()
	fmt.Printf("  Output value: %d\n", output2)

	fmt.Println("\nget_vertice")
	input := "B"
	fmt.Printf("  Input value: '%s'\n", input)
	outputVertice := graphData.GetVertice(input)
	fmt.Printf("  Output value: %v\n", outputVertice)

	fmt.Println("\nget_vertice")
	input = "E"
	fmt.Printf("  Input value: '%s'\n", input)
	outputVertice = graphData.GetVertice(input)
	fmt.Printf("  Output value: %v\n", outputVertice)

	fmt.Println("\nremove_edge")
	input1 := "A"
	input2 := "B"
	fmt.Printf("  Input value: (%s, %s)\n", input1, input2)
	outputRemoveEdge := graphData.RemoveEdge(input1, input2)
	fmt.Printf("  Output value: %v\n", outputRemoveEdge)
	fmt.Printf("  Current data: %v\n", graphData.Get())

	fmt.Println("\nremove_edge")
	input1 = "A"
	input2 = "C"
	fmt.Printf("  Input value: (%s, %s)\n", input1, input2)
	outputRemoveEdge = graphData.RemoveEdge(input1, input2)
	fmt.Printf("  Output value: %v\n", outputRemoveEdge)
	fmt.Printf("  Current data: %v\n", graphData.Get())

	fmt.Println("\nget_edges")
	outputEdges = graphData.GetEdges()
	fmt.Printf("  Output value: %v\n", outputEdges)

	fmt.Println("\nremove_vertex")
	input = "B"
	fmt.Printf("  Input value: %s\n", input)
	outputRemoveVertex := graphData.RemoveVertex(input)
	fmt.Printf("  Output value: %v\n", outputRemoveVertex)
	fmt.Printf("  Current data: %v\n", graphData.Get())

	fmt.Println("\nremove_vertex")
	input = "Z"
	fmt.Printf("  Input value: %s\n", input)
	outputRemoveVertex = graphData.RemoveVertex(input)
	fmt.Printf("  Output value: %v\n", outputRemoveVertex)
	fmt.Printf("  Current data: %v\n", graphData.Get())

	fmt.Println("\nget_vertices")
	outputVertices = graphData.GetVertices()
	fmt.Printf("  Output value: %v\n", outputVertices)

	fmt.Println("\nget_edges")
	outputEdges = graphData.GetEdges()
	fmt.Printf("  Output value: %v\n", outputEdges)

	fmt.Println("\nsize")
	output2 = graphData.Size()
	fmt.Printf("  Output value: %d\n", output2)

	fmt.Println("\nget_vertice")
	input = "B"
	fmt.Printf("  Input value: %s\n", input)
	outputVertice = graphData.GetVertice(input)
	fmt.Printf("  Output value: %v\n", outputVertice)

	fmt.Println("\nclear")
	outputClear := graphData.Clear()
	fmt.Printf("  Output value: %v\n", outputClear)

	fmt.Println("\nis_empty")
	output = graphData.IsEmpty()
	fmt.Printf("  Output value: %v\n", output)

	fmt.Println("\nsize")
	output2 = graphData.Size()
	fmt.Printf("  Output value: %d\n", output2)

	fmt.Println("\nget_vertices")
	outputVertices = graphData.GetVertices()
	fmt.Printf("  Output value: %v\n", outputVertices)

	fmt.Println("\nget_edges")
	outputEdges = graphData.GetEdges()
	fmt.Printf("  Output value: %v\n", outputEdges)

	fmt.Println("\nGraph TEST <----- end")
}
