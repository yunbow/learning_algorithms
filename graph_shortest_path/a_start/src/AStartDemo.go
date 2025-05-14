// Go
// グラフの最短経路: A-star

package main

import (
	"container/heap"
	"fmt"
	"math"
)

// GraphData はグラフのデータ構造を表します
type GraphData struct {
	// 隣接ノードとその辺の重みを格納します
	// キーは頂点、値はその頂点に隣接する頂点と重みのスライスです
	data map[string][]Edge
}

// Edge は頂点と重みのペアを表します
type Edge struct {
	Neighbor string
	Weight   float64
}

// NewGraphData はGraphDataの新しいインスタンスを作成します
func NewGraphData() *GraphData {
	return &GraphData{
		data: make(map[string][]Edge),
	}
}

// Get はグラフの内部データを取得します
func (g *GraphData) Get() map[string][]Edge {
	return g.data
}

// GetVertices はグラフの全頂点をスライスとして返します
func (g *GraphData) GetVertices() []string {
	vertices := make([]string, 0, len(g.data))
	for vertex := range g.data {
		vertices = append(vertices, vertex)
	}
	return vertices
}

// GetEdges はグラフの全辺をスライスとして返します
// 無向グラフの場合、(u, v, weight) の形式で返します
func (g *GraphData) GetEdges() []struct {
	Vertex1, Vertex2 string
	Weight           float64
} {
	edges := make(map[string]struct {
		Vertex1, Vertex2 string
		Weight           float64
	})

	for vertex, neighbors := range g.data {
		for _, edge := range neighbors {
			// 辺を正規化して（小さい方の頂点を最初にするなど）
			v1, v2 := vertex, edge.Neighbor
			if v1 > v2 {
				v1, v2 = v2, v1
			}
			key := v1 + ":" + v2
			edges[key] = struct {
				Vertex1, Vertex2 string
				Weight           float64
			}{
				Vertex1: v1,
				Vertex2: v2,
				Weight:  edge.Weight,
			}
		}
	}

	result := make([]struct {
		Vertex1, Vertex2 string
		Weight           float64
	}, 0, len(edges))

	for _, edge := range edges {
		result = append(result, edge)
	}

	return result
}

// GetNeighbors は指定された頂点の隣接ノードと辺の重みのスライスを返します
func (g *GraphData) GetNeighbors(vertex string) []Edge {
	if neighbors, exists := g.data[vertex]; exists {
		return neighbors
	}
	return nil
}

// AddVertex は新しい頂点をグラフに追加します
func (g *GraphData) AddVertex(vertex string) bool {
	if _, exists := g.data[vertex]; !exists {
		g.data[vertex] = []Edge{}
	}
	return true
}

// AddEdge は両頂点間に辺を追加します。重みを指定します
func (g *GraphData) AddEdge(vertex1, vertex2 string, weight float64) bool {
	if _, exists := g.data[vertex1]; !exists {
		g.AddVertex(vertex1)
	}
	if _, exists := g.data[vertex2]; !exists {
		g.AddVertex(vertex2)
	}

	// vertex1 -> vertex2 の辺を追加（重み付き）
	edgeExistsV1V2 := false
	for i, edge := range g.data[vertex1] {
		if edge.Neighbor == vertex2 {
			g.data[vertex1][i] = Edge{Neighbor: vertex2, Weight: weight} // 既に存在する場合は重みを更新
			edgeExistsV1V2 = true
			break
		}
	}
	if !edgeExistsV1V2 {
		g.data[vertex1] = append(g.data[vertex1], Edge{Neighbor: vertex2, Weight: weight})
	}

	// vertex2 -> vertex1 の辺を追加（重み付き）
	edgeExistsV2V1 := false
	for i, edge := range g.data[vertex2] {
		if edge.Neighbor == vertex1 {
			g.data[vertex2][i] = Edge{Neighbor: vertex1, Weight: weight} // 既に存在する場合は重みを更新
			edgeExistsV2V1 = true
			break
		}
	}
	if !edgeExistsV2V1 {
		g.data[vertex2] = append(g.data[vertex2], Edge{Neighbor: vertex1, Weight: weight})
	}

	return true
}

// Clear はグラフを空にします
func (g *GraphData) Clear() bool {
	g.data = make(map[string][]Edge)
	return true
}

// HeuristicFunc は2つの頂点間の推定距離を計算する関数型
type HeuristicFunc func(string, string) float64

// PriorityQueue はA*アルゴリズムで使用する優先度キューを実装します
type PriorityQueue []*Item

// Item は優先度キューの要素です
type Item struct {
	Value    string  // 頂点の名前
	FCost    float64 // 推定の総コスト
	Priority int     // ヒープ内での優先度
	Index    int     // ヒープ内でのインデックス
}

// Len はPriorityQueueのヒープインターフェースを実装します
func (pq PriorityQueue) Len() int { return len(pq) }

// Less はPriorityQueueのヒープインターフェースを実装します
func (pq PriorityQueue) Less(i, j int) bool {
	// FCostが小さい方を優先します
	return pq[i].FCost < pq[j].FCost
}

// Swap はPriorityQueueのヒープインターフェースを実装します
func (pq PriorityQueue) Swap(i, j int) {
	pq[i], pq[j] = pq[j], pq[i]
	pq[i].Index = i
	pq[j].Index = j
}

// Push はPriorityQueueのヒープインターフェースを実装します
func (pq *PriorityQueue) Push(x interface{}) {
	n := len(*pq)
	item := x.(*Item)
	item.Index = n
	*pq = append(*pq, item)
}

// Pop はPriorityQueueのヒープインターフェースを実装します
func (pq *PriorityQueue) Pop() interface{} {
	old := *pq
	n := len(old)
	item := old[n-1]
	old[n-1] = nil  // メモリリークを避けるため
	item.Index = -1 // 安全のため
	*pq = old[0 : n-1]
	return item
}

// GetShortestPath はA*アルゴリズムを使用して最短経路を見つけます
func (g *GraphData) GetShortestPath(startVertex, endVertex string, heuristic HeuristicFunc) ([]string, float64) {
	if _, exists1 := g.data[startVertex]; !exists1 {
		fmt.Println("ERROR: 開始頂点がグラフに存在しません。")
		return nil, math.Inf(1)
	}
	if _, exists2 := g.data[endVertex]; !exists2 {
		fmt.Println("ERROR: 終了頂点がグラフに存在しません。")
		return nil, math.Inf(1)
	}

	if startVertex == endVertex {
		return []string{startVertex}, 0
	}

	// g_costs: 開始ノードから各ノードまでの既知の最短コスト
	gCosts := make(map[string]float64)
	for vertex := range g.data {
		gCosts[vertex] = math.Inf(1)
	}
	gCosts[startVertex] = 0

	// f_costs: g_costs + ヒューリスティックコスト (推定合計コスト)
	fCosts := make(map[string]float64)
	for vertex := range g.data {
		fCosts[vertex] = math.Inf(1)
	}
	fCosts[startVertex] = heuristic(startVertex, endVertex)

	// came_from: 最短経路で各ノードの直前のノードを記録
	cameFrom := make(map[string]string)

	// open_set: 探索すべきノードの優先度キュー
	openSet := &PriorityQueue{}
	heap.Init(openSet)
	heap.Push(openSet, &Item{
		Value: startVertex,
		FCost: fCosts[startVertex],
	})

	visited := make(map[string]bool) // 処理済みのノードを追跡

	for openSet.Len() > 0 {
		// open_set から最も f_cost が低いノードを取り出す
		current := heap.Pop(openSet).(*Item)
		currentVertex := current.Value

		// 取り出したノードの f_cost が現在の記録よりも大きければスキップ
		if current.FCost > fCosts[currentVertex] {
			continue
		}

		// 目標ノードに到達した場合、経路を再構築して返す
		if currentVertex == endVertex {
			return g.reconstructPath(cameFrom, endVertex), gCosts[endVertex]
		}

		// 処理済みとしてマーク
		visited[currentVertex] = true

		// 現在のノードの隣接ノードを調べる
		neighbors := g.GetNeighbors(currentVertex)
		if neighbors == nil {
			continue
		}

		for _, edge := range neighbors {
			neighbor := edge.Neighbor
			weight := edge.Weight

			// すでに処理済みのノードはスキップ
			if visited[neighbor] {
				continue
			}

			// 現在のノードを経由した場合の隣接ノードへの新しい g_cost
			tentativeGCost := gCosts[currentVertex] + weight

			// 新しい g_cost が、現在記録されている隣接ノードへの g_cost よりも小さい場合
			if tentativeGCost < gCosts[neighbor] {
				// 経路情報を更新
				cameFrom[neighbor] = currentVertex
				gCosts[neighbor] = tentativeGCost
				fCosts[neighbor] = gCosts[neighbor] + heuristic(neighbor, endVertex)

				// 隣接ノードを open_set に追加
				heap.Push(openSet, &Item{
					Value: neighbor,
					FCost: fCosts[neighbor],
				})
			}
		}
	}

	// open_set が空になっても目標ノードに到達しなかった場合、経路は存在しない
	return nil, math.Inf(1)
}

// reconstructPath は最短経路を再構築します
func (g *GraphData) reconstructPath(cameFrom map[string]string, currentVertex string) []string {
	path := []string{}
	for {
		path = append([]string{currentVertex}, path...) // 先頭に追加
		prev, exists := cameFrom[currentVertex]
		if !exists {
			break
		}
		currentVertex = prev
	}
	return path
}

// DummyHeuristic はダミーのヒューリスティック関数（常に0を返す、ダイクストラ法と同じ）
func DummyHeuristic(u, v string) float64 {
	// u と v の間に何らかの推定距離を計算する関数
	// ここではダミーとして常に0を返す
	return 0
}

func main() {
	fmt.Println("A-start TEST -----> start")

	graphData := NewGraphData()

	graphData.Clear()
	inputList := []struct {
		Vertex1, Vertex2 string
		Weight           float64
	}{
		{"A", "B", 4},
		{"B", "C", 3},
		{"B", "D", 2},
		{"D", "A", 1},
		{"A", "C", 2},
		{"B", "D", 2},
	}
	for _, input := range inputList {
		graphData.AddEdge(input.Vertex1, input.Vertex2, input.Weight)
	}
	fmt.Println("\nグラフの頂点:", graphData.GetVertices())
	fmt.Println("グラフの辺 (重み付き):", graphData.GetEdges())
	input := [2]string{"A", "B"}
	shortestPath, weight := graphData.GetShortestPath(input[0], input[1], DummyHeuristic)
	fmt.Printf("経路%s-%s の最短経路は %v (重み: %v)\n", input[0], input[1], shortestPath, weight)

	graphData.Clear()
	inputList = []struct {
		Vertex1, Vertex2 string
		Weight           float64
	}{
		{"A", "B", 4},
		{"C", "D", 4},
		{"E", "F", 1},
		{"F", "G", 1},
	}
	for _, input := range inputList {
		graphData.AddEdge(input.Vertex1, input.Vertex2, input.Weight)
	}
	fmt.Println("\nグラフの頂点:", graphData.GetVertices())
	fmt.Println("グラフの辺 (重み付き):", graphData.GetEdges())
	input = [2]string{"A", "B"}
	shortestPath, weight = graphData.GetShortestPath(input[0], input[1], DummyHeuristic)
	fmt.Printf("経路%s-%s の最短経路は %v (重み: %v)\n", input[0], input[1], shortestPath, weight)

	graphData.Clear()
	inputList = []struct {
		Vertex1, Vertex2 string
		Weight           float64
	}{
		{"A", "B", 4},
		{"B", "C", 3},
		{"D", "E", 5},
	}
	for _, input := range inputList {
		graphData.AddEdge(input.Vertex1, input.Vertex2, input.Weight)
	}
	fmt.Println("\nグラフの頂点:", graphData.GetVertices())
	fmt.Println("グラフの辺 (重み付き):", graphData.GetEdges())
	input = [2]string{"A", "D"}
	shortestPath, weight = graphData.GetShortestPath(input[0], input[1], DummyHeuristic)
	fmt.Printf("経路%s-%s の最短経路は %v (重み: %v)\n", input[0], input[1], shortestPath, weight)

	graphData.Clear()
	inputList = []struct {
		Vertex1, Vertex2 string
		Weight           float64
	}{}
	for _, input := range inputList {
		graphData.AddEdge(input.Vertex1, input.Vertex2, input.Weight)
	}
	fmt.Println("\nグラフの頂点:", graphData.GetVertices())
	fmt.Println("グラフの辺 (重み付き):", graphData.GetEdges())
	input = [2]string{"A", "B"}
	shortestPath, weight = graphData.GetShortestPath(input[0], input[1], DummyHeuristic)
	fmt.Printf("経路%s-%s の最短経路は %v (重み: %v)\n", input[0], input[1], shortestPath, weight)

	fmt.Println("\nA-start TEST <----- end")
}
