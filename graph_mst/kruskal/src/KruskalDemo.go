// Go
// グラフの最小全域木: クラスカル法 (Kruskal)

package main

import (
	"fmt"
	"sort"
)

// DSU (Disjoint Set Union) はUnion-Find木の実装
type DSU struct {
	parent map[string]string
	rank   map[string]int
}

// NewDSU は新しいDSU構造体を初期化します
func NewDSU(vertices []string) *DSU {
	parent := make(map[string]string)
	rank := make(map[string]int)

	for _, v := range vertices {
		parent[v] = v
		rank[v] = 0
	}

	return &DSU{
		parent: parent,
		rank:   rank,
	}
}

// Find は頂点が属する集合の代表元（根）を見つけます
func (d *DSU) Find(i string) string {
	if d.parent[i] == i {
		return i
	}
	// パス圧縮
	d.parent[i] = d.Find(d.parent[i])
	return d.parent[i]
}

// Union は二つの集合を結合します
func (d *DSU) Union(i, j string) bool {
	rootI := d.Find(i)
	rootJ := d.Find(j)

	if rootI != rootJ {
		// ランクによる最適化
		if d.rank[rootI] < d.rank[rootJ] {
			d.parent[rootI] = rootJ
		} else if d.rank[rootI] > d.rank[rootJ] {
			d.parent[rootJ] = rootI
		} else {
			d.parent[rootJ] = rootI
			d.rank[rootI]++
		}
		return true
	}
	return false
}

// Edge は辺を表す構造体
type Edge struct {
	Vertex1 string
	Vertex2 string
	Weight  int
}

// GraphData は重み付きグラフを表す構造体
type GraphData struct {
	data map[string][]struct {
		Neighbor string
		Weight   int
	}
}

// NewGraphData は新しいGraphData構造体を初期化します
func NewGraphData() *GraphData {
	return &GraphData{
		data: make(map[string][]struct {
			Neighbor string
			Weight   int
		}),
	}
}

// Get はグラフの内部データを返します
func (g *GraphData) Get() map[string][]struct {
	Neighbor string
	Weight   int
} {
	return g.data
}

// GetVertices はグラフの全頂点を返します
func (g *GraphData) GetVertices() []string {
	vertices := make([]string, 0, len(g.data))
	for vertex := range g.data {
		vertices = append(vertices, vertex)
	}
	return vertices
}

// GetEdges はグラフの全辺を返します
func (g *GraphData) GetEdges() []Edge {
	edges := make(map[string]Edge)

	for vertex, neighbors := range g.data {
		for _, neighborInfo := range neighbors {
			// 無向グラフなので辺を正規化（小さい頂点を先に）
			v1, v2 := vertex, neighborInfo.Neighbor
			if v1 > v2 {
				v1, v2 = v2, v1
			}
			edgeKey := v1 + ":" + v2
			edges[edgeKey] = Edge{
				Vertex1: v1,
				Vertex2: v2,
				Weight:  neighborInfo.Weight,
			}
		}
	}

	// マップからスライスに変換
	result := make([]Edge, 0, len(edges))
	for _, edge := range edges {
		result = append(result, edge)
	}
	return result
}

// AddVertex は新しい頂点をグラフに追加します
func (g *GraphData) AddVertex(vertex string) bool {
	if _, exists := g.data[vertex]; !exists {
		g.data[vertex] = []struct {
			Neighbor string
			Weight   int
		}{}
	}
	return true
}

// AddEdge は両頂点間に辺を追加します
func (g *GraphData) AddEdge(vertex1, vertex2 string, weight int) bool {
	// 頂点が存在しない場合は追加
	g.AddVertex(vertex1)
	g.AddVertex(vertex2)

	// vertex1 -> vertex2 の辺を追加または更新
	edgeExistsV1V2 := false
	for i, neighborInfo := range g.data[vertex1] {
		if neighborInfo.Neighbor == vertex2 {
			g.data[vertex1][i].Weight = weight
			edgeExistsV1V2 = true
			break
		}
	}
	if !edgeExistsV1V2 {
		g.data[vertex1] = append(g.data[vertex1], struct {
			Neighbor string
			Weight   int
		}{vertex2, weight})
	}

	// vertex2 -> vertex1 の辺を追加または更新
	edgeExistsV2V1 := false
	for i, neighborInfo := range g.data[vertex2] {
		if neighborInfo.Neighbor == vertex1 {
			g.data[vertex2][i].Weight = weight
			edgeExistsV2V1 = true
			break
		}
	}
	if !edgeExistsV2V1 {
		g.data[vertex2] = append(g.data[vertex2], struct {
			Neighbor string
			Weight   int
		}{vertex1, weight})
	}

	return true
}

// Clear はグラフを空にします
func (g *GraphData) Clear() bool {
	g.data = make(map[string][]struct {
		Neighbor string
		Weight   int
	})
	return true
}

// GetMST はクラスカルアルゴリズムを使って最小全域木を計算します
func (g *GraphData) GetMST() []Edge {
	// 辺を重みでソート
	edges := g.GetEdges()
	sort.Slice(edges, func(i, j int) bool {
		return edges[i].Weight < edges[j].Weight
	})

	// DSUを初期化
	vertices := g.GetVertices()
	dsu := NewDSU(vertices)

	// MSTを構築
	mstEdges := []Edge{}
	edgesCount := 0

	for _, edge := range edges {
		rootU := dsu.Find(edge.Vertex1)
		rootV := dsu.Find(edge.Vertex2)

		if rootU != rootV {
			mstEdges = append(mstEdges, edge)
			dsu.Union(edge.Vertex1, edge.Vertex2)
			edgesCount++

			if edgesCount == len(vertices)-1 {
				break
			}
		}
	}

	return mstEdges
}

func main() {
	fmt.Println("Kruskal TEST -----> start")
	graphData := NewGraphData()

	graphData.Clear()
	inputList := []struct {
		v1     string
		v2     string
		weight int
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
	fmt.Println("\nグラフの頂点:", graphData.GetVertices())
	fmt.Println("グラフの辺 (重み付き):", graphData.GetEdges())
	outputMst := graphData.GetMST()
	for _, edge := range outputMst {
		fmt.Printf("Edge: %s - %s, Weight: %d\n", edge.Vertex1, edge.Vertex2, edge.Weight)
	}
	totalWeight := 0
	for _, edge := range outputMst {
		totalWeight += edge.Weight
	}
	fmt.Printf("最小全域木の合計重み: %d\n", totalWeight)

	graphData.Clear()
	inputList = []struct {
		v1     string
		v2     string
		weight int
	}{
		{"A", "B", 4},
		{"C", "D", 4},
		{"E", "F", 1},
		{"F", "G", 1},
	}
	for _, input := range inputList {
		graphData.AddEdge(input.v1, input.v2, input.weight)
	}
	fmt.Println("\nグラフの頂点:", graphData.GetVertices())
	fmt.Println("グラフの辺 (重み付き):", graphData.GetEdges())
	outputMst = graphData.GetMST()
	for _, edge := range outputMst {
		fmt.Printf("Edge: %s - %s, Weight: %d\n", edge.Vertex1, edge.Vertex2, edge.Weight)
	}
	totalWeight = 0
	for _, edge := range outputMst {
		totalWeight += edge.Weight
	}
	fmt.Printf("最小全域木の合計重み: %d\n", totalWeight)

	graphData.Clear()
	inputList = []struct {
		v1     string
		v2     string
		weight int
	}{
		{"A", "B", 4},
		{"B", "C", 3},
		{"D", "E", 5},
	}
	for _, input := range inputList {
		graphData.AddEdge(input.v1, input.v2, input.weight)
	}
	fmt.Println("\nグラフの頂点:", graphData.GetVertices())
	fmt.Println("グラフの辺 (重み付き):", graphData.GetEdges())
	outputMst = graphData.GetMST()
	for _, edge := range outputMst {
		fmt.Printf("Edge: %s - %s, Weight: %d\n", edge.Vertex1, edge.Vertex2, edge.Weight)
	}
	totalWeight = 0
	for _, edge := range outputMst {
		totalWeight += edge.Weight
	}
	fmt.Printf("最小全域木の合計重み: %d\n", totalWeight)

	graphData.Clear()
	fmt.Println("\nグラフの頂点:", graphData.GetVertices())
	fmt.Println("グラフの辺 (重み付き):", graphData.GetEdges())
	outputMst = graphData.GetMST()
	for _, edge := range outputMst {
		fmt.Printf("Edge: %s - %s, Weight: %d\n", edge.Vertex1, edge.Vertex2, edge.Weight)
	}
	totalWeight = 0
	for _, edge := range outputMst {
		totalWeight += edge.Weight
	}
	fmt.Printf("最小全域木の合計重み: %d\n", totalWeight)

	fmt.Println("\nKruskal TEST <----- end")
}