// Go
// グラフの最短経路: ダイクストラ法 (dijkstra)

package main

import (
	"container/heap"
	"fmt"
)

// GraphData はグラフのデータ構造を表します
type GraphData struct {
	data map[string][]Edge
}

// Edge はグラフの辺を表します
type Edge struct {
	Neighbor string
	Weight   float64
}

// NewGraphData は新しいGraphDataを作成します
func NewGraphData() *GraphData {
	return &GraphData{
		data: make(map[string][]Edge),
	}
}

// Get はグラフの内部データを取得します
func (g *GraphData) Get() map[string][]Edge {
	return g.data
}

// GetVertices はグラフの全頂点をリストとして返します
func (g *GraphData) GetVertices() []string {
	vertices := make([]string, 0, len(g.data))
	for vertex := range g.data {
		vertices = append(vertices, vertex)
	}
	return vertices
}

// GetEdges はグラフの全辺をリストとして返します
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
			// 辺を正規化（小さい方の頂点を最初にする）
			v1, v2 := vertex, edge.Neighbor
			if v1 > v2 {
				v1, v2 = v2, v1
			}
			key := v1 + "-" + v2
			edges[key] = struct {
				Vertex1, Vertex2 string
				Weight           float64
			}{v1, v2, edge.Weight}
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

// GetNeighbors は指定された頂点の隣接ノードと辺の重みのリストを返します
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

// AddEdge は両頂点間に辺を追加します
func (g *GraphData) AddEdge(vertex1, vertex2 string, weight float64) bool {
	if _, exists := g.data[vertex1]; !exists {
		g.AddVertex(vertex1)
	}
	if _, exists := g.data[vertex2]; !exists {
		g.AddVertex(vertex2)
	}

	// vertex1 -> vertex2 の辺を追加
	edgeExistsV1V2 := false
	for i, edge := range g.data[vertex1] {
		if edge.Neighbor == vertex2 {
			g.data[vertex1][i].Weight = weight // 既に存在する場合は重みを更新
			edgeExistsV1V2 = true
			break
		}
	}
	if !edgeExistsV1V2 {
		g.data[vertex1] = append(g.data[vertex1], Edge{vertex2, weight})
	}

	// vertex2 -> vertex1 の辺を追加
	edgeExistsV2V1 := false
	for i, edge := range g.data[vertex2] {
		if edge.Neighbor == vertex1 {
			g.data[vertex2][i].Weight = weight // 既に存在する場合は重みを更新
			edgeExistsV2V1 = true
			break
		}
	}
	if !edgeExistsV2V1 {
		g.data[vertex2] = append(g.data[vertex2], Edge{vertex1, weight})
	}

	return true
}

// Clear はグラフを空にします
func (g *GraphData) Clear() bool {
	g.data = make(map[string][]Edge)
	return true
}

// PriorityQueueItem は優先度付きキューのアイテムを表します
type PriorityQueueItem struct {
	vertex   string
	distance float64
	index    int
}

// PriorityQueue は優先度付きキューを実装します
type PriorityQueue []*PriorityQueueItem

func (pq PriorityQueue) Len() int { return len(pq) }

func (pq PriorityQueue) Less(i, j int) bool {
	return pq[i].distance < pq[j].distance
}

func (pq PriorityQueue) Swap(i, j int) {
	pq[i], pq[j] = pq[j], pq[i]
	pq[i].index = i
	pq[j].index = j
}

func (pq *PriorityQueue) Push(x interface{}) {
	n := len(*pq)
	item := x.(*PriorityQueueItem)
	item.index = n
	*pq = append(*pq, item)
}

func (pq *PriorityQueue) Pop() interface{} {
	old := *pq
	n := len(old)
	item := old[n-1]
	old[n-1] = nil  // メモリリークを避けるため
	item.index = -1 // 安全のため
	*pq = old[0 : n-1]
	return item
}

// 無限大を表す定数
const Infinity = float64(int(^uint(0) >> 1))

// GetShortestPath は開始頂点から終了頂点までの最短経路を返します
func (g *GraphData) GetShortestPath(startVertex, endVertex string, heuristic func(string, string) float64) ([]string, float64) {
	if _, exists := g.data[startVertex]; !exists {
		fmt.Printf("ERROR: 開始頂点 '%s' がグラフに存在しません。\n", startVertex)
		return nil, Infinity
	}
	if _, exists := g.data[endVertex]; !exists {
		fmt.Printf("ERROR: 終了頂点 '%s' がグラフに存在しません。\n", endVertex)
		return nil, Infinity
	}

	// 距離を初期化: 全ての頂点への距離を無限大に設定し、開始頂点のみ0とする
	distances := make(map[string]float64)
	for vertex := range g.data {
		distances[vertex] = Infinity
	}
	distances[startVertex] = 0

	// 経路を記録するための辞書: 各頂点に至る直前の頂点を保持
	predecessors := make(map[string]string)
	for vertex := range g.data {
		predecessors[vertex] = ""
	}

	// 優先度付きキューを初期化
	pq := make(PriorityQueue, 0)
	heap.Init(&pq)
	heap.Push(&pq, &PriorityQueueItem{
		vertex:   startVertex,
		distance: 0,
	})

	for pq.Len() > 0 {
		// 優先度付きキューから最も距離の小さい頂点を取り出す
		item := heap.Pop(&pq).(*PriorityQueueItem)
		currentVertex := item.vertex
		currentDistance := item.distance

		// 取り出した頂点への距離が、すでに記録されている距離より大きい場合は、
		// より短い経路が既に見つかっているためスキップ
		if currentDistance > distances[currentVertex] {
			continue
		}

		// 終了頂点に到達したら探索終了
		if currentVertex == endVertex {
			break // 最短経路が見つかった
		}

		// 現在の頂点から到達可能な隣接頂点を探索
		for _, edge := range g.GetNeighbors(currentVertex) {
			distanceThroughCurrent := distances[currentVertex] + edge.Weight

			// より短い経路が見つかった場合
			if distanceThroughCurrent < distances[edge.Neighbor] {
				distances[edge.Neighbor] = distanceThroughCurrent
				predecessors[edge.Neighbor] = currentVertex
				// 優先度付きキューに隣接頂点を追加または更新
				heap.Push(&pq, &PriorityQueueItem{
					vertex:   edge.Neighbor,
					distance: distanceThroughCurrent,
				})
			}
		}
	}

	// 終了頂点への最短距離が無限大のままなら、到達不可能
	if distances[endVertex] == Infinity {
		fmt.Printf("INFO: 開始頂点 '%s' から 終了頂点 '%s' への経路は存在しません。\n", startVertex, endVertex)
		return nil, Infinity
	}

	// 最短経路を再構築
	path := make([]string, 0)
	current := endVertex
	for current != "" {
		path = append([]string{current}, path...) // 先頭に追加（逆順にする）
		current = predecessors[current]
	}

	// 開始ノードから開始されていることを確認
	if len(path) == 0 || path[0] != startVertex {
		return nil, Infinity
	}

	return path, distances[endVertex]
}

// ヒューリスティック関数 (常に0を返す、ダイクストラ法と同じ)
func dummyHeuristic(u, v string) float64 {
	return 0
}

func main() {
	fmt.Println("Dijkstra -----> start")

	graphData := NewGraphData()

	graphData.Clear()
	inputList := []struct {
		v1, v2 string
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
	fmt.Println("\nグラフの頂点:", graphData.GetVertices())
	fmt.Println("グラフの辺 (重み付き):", graphData.GetEdges())
	input := [2]string{"A", "B"}
	shortestPath, weight := graphData.GetShortestPath(input[0], input[1], dummyHeuristic)
	fmt.Printf("経路%s-%s の最短経路は %v (重み: %v)\n", input[0], input[1], shortestPath, weight)

	graphData.Clear()
	inputList = []struct {
		v1, v2 string
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
	fmt.Println("\nグラフの頂点:", graphData.GetVertices())
	fmt.Println("グラフの辺 (重み付き):", graphData.GetEdges())
	input = [2]string{"A", "B"}
	shortestPath, weight = graphData.GetShortestPath(input[0], input[1], dummyHeuristic)
	fmt.Printf("経路%s-%s の最短経路は %v (重み: %v)\n", input[0], input[1], shortestPath, weight)

	graphData.Clear()
	inputList = []struct {
		v1, v2 string
		weight float64
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
	input = [2]string{"A", "D"}
	shortestPath, weight = graphData.GetShortestPath(input[0], input[1], dummyHeuristic)
	fmt.Printf("経路%s-%s の最短経路は %v (重み: %v)\n", input[0], input[1], shortestPath, weight)

	graphData.Clear()
	inputList = []struct {
		v1, v2 string
		weight float64
	}{}
	for _, input := range inputList {
		graphData.AddEdge(input.v1, input.v2, input.weight)
	}
	fmt.Println("\nグラフの頂点:", graphData.GetVertices())
	fmt.Println("グラフの辺 (重み付き):", graphData.GetEdges())
	input = [2]string{"A", "B"}
	shortestPath, weight = graphData.GetShortestPath(input[0], input[1], dummyHeuristic)
	fmt.Printf("経路%s-%s の最短経路は %v (重み: %v)\n", input[0], input[1], shortestPath, weight)

	fmt.Println("\nDijkstra <----- end")
}