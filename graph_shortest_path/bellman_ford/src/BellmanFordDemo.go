// Go
// グラフの最短経路: ベルマンフォード法 (Bellman Ford)

package main

import (
	"fmt"
	"math"
)

// Edge はグラフの辺を表す
type Edge struct {
	from   string
	to     string
	weight float64
}

// GraphData はグラフのデータ構造
type GraphData struct {
	data map[string][]struct {
		vertex string
		weight float64
	}
}

// NewGraphData は新しいGraphDataインスタンスを初期化する
func NewGraphData() *GraphData {
	return &GraphData{
		data: make(map[string][]struct {
			vertex string
			weight float64
		}),
	}
}

// Get はグラフの内部データを取得する
func (g *GraphData) Get() map[string][]struct {
	vertex string
	weight float64
} {
	return g.data
}

// GetVertices はグラフの全頂点をリストとして返す
func (g *GraphData) GetVertices() []string {
	vertices := make([]string, 0, len(g.data))
	for vertex := range g.data {
		vertices = append(vertices, vertex)
	}
	return vertices
}

// GetEdges はグラフの全辺をリストとして返す
func (g *GraphData) GetEdges() []Edge {
	edges := []Edge{}
	for u, neighbors := range g.data {
		for _, neighbor := range neighbors {
			edges = append(edges, Edge{
				from:   u,
				to:     neighbor.vertex,
				weight: neighbor.weight,
			})
		}
	}
	return edges
}

// AddVertex は新しい頂点をグラフに追加する
func (g *GraphData) AddVertex(vertex string) bool {
	if _, exists := g.data[vertex]; !exists {
		g.data[vertex] = []struct {
			vertex string
			weight float64
		}{}
	}
	return true
}

// AddEdge は両頂点間に辺を追加する
func (g *GraphData) AddEdge(vertex1, vertex2 string, weight float64) bool {
	g.AddVertex(vertex1)
	g.AddVertex(vertex2)

	// vertex1 -> vertex2 の辺を追加または更新
	edgeUpdatedV1V2 := false
	for i, neighbor := range g.data[vertex1] {
		if neighbor.vertex == vertex2 {
			g.data[vertex1][i].weight = weight
			edgeUpdatedV1V2 = true
			break
		}
	}
	if !edgeUpdatedV1V2 {
		g.data[vertex1] = append(g.data[vertex1], struct {
			vertex string
			weight float64
		}{vertex: vertex2, weight: weight})
	}

	// vertex2 -> vertex1 の辺を追加または更新
	edgeUpdatedV2V1 := false
	for i, neighbor := range g.data[vertex2] {
		if neighbor.vertex == vertex1 {
			g.data[vertex2][i].weight = weight
			edgeUpdatedV2V1 = true
			break
		}
	}
	if !edgeUpdatedV2V1 {
		g.data[vertex2] = append(g.data[vertex2], struct {
			vertex string
			weight float64
		}{vertex: vertex1, weight: weight})
	}

	return true
}

// IsEmpty はグラフが空かどうかを返す
func (g *GraphData) IsEmpty() bool {
	return len(g.data) == 0
}

// Clear はグラフを空にする
func (g *GraphData) Clear() bool {
	g.data = make(map[string][]struct {
		vertex string
		weight float64
	})
	return true
}

// GetShortestPath は始点から終点までの最短経路を返す
func (g *GraphData) GetShortestPath(startVertex, endVertex string, heuristic func(string, string) float64) ([]string, float64) {
	vertices := g.GetVertices()
	edges := g.GetEdges()
	numVertices := len(vertices)

	// 始点と終点の存在チェック
	startExists := false
	endExists := false
	for _, v := range vertices {
		if v == startVertex {
			startExists = true
		}
		if v == endVertex {
			endExists = true
		}
	}

	if !startExists {
		fmt.Printf("エラー: 始点 '%s' がグラフに存在しません。\n", startVertex)
		return []string{}, math.Inf(1)
	}
	if !endExists {
		fmt.Printf("エラー: 終点 '%s' がグラフに存在しません。\n", endVertex)
		return []string{}, math.Inf(1)
	}

	// 始点と終点が同じ場合
	if startVertex == endVertex {
		return []string{startVertex}, 0
	}

	// 距離と先行頂点の初期化
	dist := make(map[string]float64)
	pred := make(map[string]string)
	for _, vertex := range vertices {
		dist[vertex] = math.Inf(1)
		pred[vertex] = ""
	}
	dist[startVertex] = 0 // 始点自身の距離は0

	// |V| - 1 回の緩和ステップを実行
	for i := 0; i < numVertices-1; i++ {
		relaxedInThisIteration := false
		for _, edge := range edges {
			u, v, weight := edge.from, edge.to, edge.weight
			if dist[u] != math.Inf(1) && dist[u]+weight < dist[v] {
				dist[v] = dist[u] + weight
				pred[v] = u
				relaxedInThisIteration = true
			}
		}
		// このイテレーションで緩和が行われなかった場合はループを抜ける
		if !relaxedInThisIteration {
			break
		}
	}

	// 負閉路の検出
	for _, edge := range edges {
		u, v, weight := edge.from, edge.to, edge.weight
		if dist[u] != math.Inf(1) && dist[u]+weight < dist[v] {
			fmt.Println("エラー: グラフに負閉路が存在します。最短経路は定義できません。")
			return nil, math.Inf(-1)
		}
	}

	// 最短経路の構築
	path := []string{}
	current := endVertex

	// 終点まで到達不可能かチェック
	if dist[endVertex] == math.Inf(1) {
		return []string{}, math.Inf(1)
	}

	// 終点から先行頂点をたどって経路を逆順に構築
	for current != "" {
		path = append(path, current)
		// 始点に到達したらループを終了
		if current == startVertex {
			break
		}
		// 次の頂点に進む
		current = pred[current]
	}

	// 経路が始点から始まっていない場合
	if len(path) == 0 || path[len(path)-1] != startVertex {
		return []string{}, math.Inf(1)
	}

	// 経路を始点から終点の順にする
	for i, j := 0, len(path)-1; i < j; i, j = i+1, j-1 {
		path[i], path[j] = path[j], path[i]
	}

	return path, dist[endVertex]
}

// DummyHeuristic はヒューリスティック関数（ベルマン-フォード法では使用しない）
func DummyHeuristic(u, v string) float64 {
	return 0
}

func main() {
	fmt.Println("BellmanFord TEST -----> start")

	graphData := NewGraphData()

	graphData.Clear()
	inputList := [][3]interface{}{
		{"A", "B", 4.0},
		{"B", "C", 3.0},
		{"B", "D", 2.0},
		{"D", "A", 1.0},
		{"A", "C", 2.0},
		{"B", "D", 2.0},
	}
	for _, input := range inputList {
		graphData.AddEdge(input[0].(string), input[1].(string), input[2].(float64))
	}
	fmt.Printf("\nグラフの頂点: %v\n", graphData.GetVertices())
	fmt.Printf("グラフの辺 (重み付き): %v\n", graphData.GetEdges())
	input := [2]string{"A", "B"}
	shortestPath, weight := graphData.GetShortestPath(input[0], input[1], DummyHeuristic)
	fmt.Printf("経路%s-%s の最短経路は %v (重み: %v)\n", input[0], input[1], shortestPath, weight)

	graphData.Clear()
	inputList = [][3]interface{}{
		{"A", "B", 4.0},
		{"C", "D", 4.0},
		{"E", "F", 1.0},
		{"F", "G", 1.0},
	}
	for _, input := range inputList {
		graphData.AddEdge(input[0].(string), input[1].(string), input[2].(float64))
	}
	fmt.Printf("\nグラフの頂点: %v\n", graphData.GetVertices())
	fmt.Printf("グラフの辺 (重み付き): %v\n", graphData.GetEdges())
	input = [2]string{"A", "B"}
	shortestPath, weight = graphData.GetShortestPath(input[0], input[1], DummyHeuristic)
	fmt.Printf("経路%s-%s の最短経路は %v (重み: %v)\n", input[0], input[1], shortestPath, weight)

	graphData.Clear()
	inputList = [][3]interface{}{
		{"A", "B", 4.0},
		{"B", "C", 3.0},
		{"D", "E", 5.0},
	}
	for _, input := range inputList {
		graphData.AddEdge(input[0].(string), input[1].(string), input[2].(float64))
	}
	fmt.Printf("\nグラフの頂点: %v\n", graphData.GetVertices())
	fmt.Printf("グラフの辺 (重み付き): %v\n", graphData.GetEdges())
	input = [2]string{"A", "D"}
	shortestPath, weight = graphData.GetShortestPath(input[0], input[1], DummyHeuristic)
	fmt.Printf("経路%s-%s の最短経路は %v (重み: %v)\n", input[0], input[1], shortestPath, weight)

	graphData.Clear()
	fmt.Printf("\nグラフの頂点: %v\n", graphData.GetVertices())
	fmt.Printf("グラフの辺 (重み付き): %v\n", graphData.GetEdges())
	input = [2]string{"A", "B"}
	shortestPath, weight = graphData.GetShortestPath(input[0], input[1], DummyHeuristic)
	fmt.Printf("経路%s-%s の最短経路は %v (重み: %v)\n", input[0], input[1], shortestPath, weight)

	fmt.Println("\nBellmanFord TEST <----- end")
}