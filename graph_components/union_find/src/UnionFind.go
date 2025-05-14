// Go
// グラフの連結成分: Union-Find

package main

import (
	"fmt"
)

// GraphData represents an undirected weighted graph
type GraphData struct {
	// データは頂点から隣接ノードとその辺の重みへのマッピング
	data map[string][]Edge
}

// Edge represents an edge with destination vertex and weight
type Edge struct {
	Neighbor string
	Weight   int
}

// NewGraphData creates a new empty graph
func NewGraphData() *GraphData {
	return &GraphData{
		data: make(map[string][]Edge),
	}
}

// Get returns the internal data structure of the graph
func (g *GraphData) Get() map[string][]Edge {
	return g.data
}

// GetVertices returns a list of all vertices in the graph
func (g *GraphData) GetVertices() []string {
	vertices := make([]string, 0, len(g.data))
	for vertex := range g.data {
		vertices = append(vertices, vertex)
	}
	return vertices
}

// GetEdges returns a list of all edges in the graph as (u, v, weight) tuples
func (g *GraphData) GetEdges() [][3]interface{} {
	edges := make(map[[2]string]int)
	
	for vertex, neighbors := range g.data {
		for _, edge := range neighbors {
			// 辺を正規化 (小さい方の頂点を最初にする)
			var key [2]string
			if vertex < edge.Neighbor {
				key = [2]string{vertex, edge.Neighbor}
			} else {
				key = [2]string{edge.Neighbor, vertex}
			}
			edges[key] = edge.Weight
		}
	}
	
	result := make([][3]interface{}, 0, len(edges))
	for key, weight := range edges {
		result = append(result, [3]interface{}{key[0], key[1], weight})
	}
	
	return result
}

// AddVertex adds a vertex to the graph
func (g *GraphData) AddVertex(vertex string) bool {
	if _, exists := g.data[vertex]; !exists {
		g.data[vertex] = []Edge{}
	}
	return true
}

// AddEdge adds an edge between two vertices with a given weight
func (g *GraphData) AddEdge(vertex1, vertex2 string, weight int) bool {
	// 必要に応じて頂点を追加
	g.AddVertex(vertex1)
	g.AddVertex(vertex2)
	
	// vertex1 -> vertex2 の辺を追加または更新
	edgeExistsV1V2 := false
	for i, edge := range g.data[vertex1] {
		if edge.Neighbor == vertex2 {
			g.data[vertex1][i].Weight = weight
			edgeExistsV1V2 = true
			break
		}
	}
	if !edgeExistsV1V2 {
		g.data[vertex1] = append(g.data[vertex1], Edge{Neighbor: vertex2, Weight: weight})
	}
	
	// vertex2 -> vertex1 の辺を追加または更新
	edgeExistsV2V1 := false
	for i, edge := range g.data[vertex2] {
		if edge.Neighbor == vertex1 {
			g.data[vertex2][i].Weight = weight
			edgeExistsV2V1 = true
			break
		}
	}
	if !edgeExistsV2V1 {
		g.data[vertex2] = append(g.data[vertex2], Edge{Neighbor: vertex1, Weight: weight})
	}
	
	return true
}

// IsEmpty checks if the graph is empty
func (g *GraphData) IsEmpty() bool {
	return len(g.data) == 0
}

// Clear empties the graph
func (g *GraphData) Clear() bool {
	g.data = make(map[string][]Edge)
	return true
}

// GetConnectedComponents returns the connected components of the graph using Union-Find
func (g *GraphData) GetConnectedComponents() [][]string {
	if len(g.data) == 0 {
		return [][]string{}
	}
	
	// Union-Find のためのデータ構造を初期化
	parent := make(map[string]string)
	size := make(map[string]int)
	
	// 各頂点を初期化
	vertices := g.GetVertices()
	for _, vertex := range vertices {
		parent[vertex] = vertex
		size[vertex] = 1
	}
	
	// Find 操作（経路圧縮付き）
	var find func(string) string
	find = func(v string) string {
		if parent[v] != v {
			parent[v] = find(parent[v])
		}
		return parent[v]
	}
	
	// Union 操作（Union by Size付き）
	union := func(u, v string) bool {
		rootU := find(u)
		rootV := find(v)
		
		if rootU != rootV {
			if size[rootU] < size[rootV] {
				parent[rootU] = rootV
				size[rootV] += size[rootU]
			} else {
				parent[rootV] = rootU
				size[rootU] += size[rootV]
			}
			return true
		}
		return false
	}
	
	// グラフの全ての辺に対してUnion操作を実行
	for _, edge := range g.GetEdges() {
		u := edge[0].(string)
		v := edge[1].(string)
		union(u, v)
	}
	
	// 連結成分をグループ化
	components := make(map[string][]string)
	for _, vertex := range vertices {
		root := find(vertex)
		components[root] = append(components[root], vertex)
	}
	
	// 連結成分のリストを返す
	result := make([][]string, 0, len(components))
	for _, comp := range components {
		result = append(result, comp)
	}
	
	return result
}

func main() {
	fmt.Println("UnionFind TEST -----> start")
	
	fmt.Println("\nnew")
	graphData := NewGraphData()
	fmt.Printf("  現在のデータ: %v\n", graphData.Get())
	
	fmt.Println("\nadd_edge")
	graphData.Clear()
	inputList := [][3]interface{}{
		{"A", "B", 4}, {"B", "C", 3}, {"B", "D", 2}, {"D", "A", 1}, {"A", "C", 2}, {"B", "D", 2},
	}
	for _, input := range inputList {
		fmt.Printf("  入力値: %v\n", input)
		output := graphData.AddEdge(input[0].(string), input[1].(string), input[2].(int))
		fmt.Printf("  出力値: %v\n", output)
	}
	fmt.Printf("  現在のデータ: %v\n", graphData.Get())
	fmt.Println("\nget_connected_components")
	output := graphData.GetConnectedComponents()
	fmt.Printf("  連結成分: %v\n", output)
	
	fmt.Println("\nadd_edge")
	graphData.Clear()
	inputList = [][3]interface{}{
		{"A", "B", 4}, {"C", "D", 4}, {"E", "F", 1}, {"F", "G", 1},
	}
	for _, input := range inputList {
		fmt.Printf("  入力値: %v\n", input)
		output := graphData.AddEdge(input[0].(string), input[1].(string), input[2].(int))
		fmt.Printf("  出力値: %v\n", output)
	}
	fmt.Printf("  現在のデータ: %v\n", graphData.Get())
	fmt.Println("\nget_connected_components")
	output = graphData.GetConnectedComponents()
	fmt.Printf("  連結成分: %v\n", output)
	
	fmt.Println("\nadd_edge")
	graphData.Clear()
	inputList = [][3]interface{}{
		{"A", "B", 4}, {"B", "C", 3}, {"D", "E", 5},
	}
	for _, input := range inputList {
		fmt.Printf("  入力値: %v\n", input)
		output := graphData.AddEdge(input[0].(string), input[1].(string), input[2].(int))
		fmt.Printf("  出力値: %v\n", output)
	}
	fmt.Printf("  現在のデータ: %v\n", graphData.Get())
	fmt.Println("\nget_connected_components")
	output = graphData.GetConnectedComponents()
	fmt.Printf("  連結成分: %v\n", output)
	
	fmt.Println("\nadd_edge")
	graphData.Clear()
	var emptyInputList [][3]interface{}
	for _, input := range emptyInputList {
		fmt.Printf("  入力値: %v\n", input)
		output := graphData.AddEdge(input[0].(string), input[1].(string), input[2].(int))
		fmt.Printf("  出力値: %v\n", output)
	}
	fmt.Printf("  現在のデータ: %v\n", graphData.Get())
	fmt.Println("\nget_connected_components")
	output = graphData.GetConnectedComponents()
	fmt.Printf("  連結成分: %v\n", output)
	
	fmt.Println("\nUnionFind TEST <----- end")
}