// Go
// グラフの最小全域木: プリム法 (Prim)

package main

import (
	"container/heap"
	"fmt"
)

// Edge は辺を表す構造体
type Edge struct {
	vertex string
	weight int
}

// MinHeapItem はヒープ内の項目を表す構造体
type MinHeapItem struct {
	cost     int
	vertex   string
	fromEdge string
	index    int
}

// MinHeap は優先度付きキューの実装
type MinHeap []*MinHeapItem

// Len はヒープの長さを返す
func (h MinHeap) Len() int { return len(h) }

// Less は小さい方が優先されるように比較する
func (h MinHeap) Less(i, j int) bool { return h[i].cost < h[j].cost }

// Swap はヒープ内の要素を交換する
func (h MinHeap) Swap(i, j int) {
	h[i], h[j] = h[j], h[i]
	h[i].index = i
	h[j].index = j
}

// Push はヒープに要素を追加する
func (h *MinHeap) Push(x interface{}) {
	n := len(*h)
	item := x.(*MinHeapItem)
	item.index = n
	*h = append(*h, item)
}

// Pop はヒープから最小要素を取り出す
func (h *MinHeap) Pop() interface{} {
	old := *h
	n := len(old)
	item := old[n-1]
	old[n-1] = nil  // メモリリーク防止
	item.index = -1 // 安全のため
	*h = old[0 : n-1]
	return item
}

// GraphData はグラフを表す構造体
type GraphData struct {
	data map[string][]Edge
}

// NewGraphData は新しいGraphDataインスタンスを作成する
func NewGraphData() *GraphData {
	return &GraphData{
		data: make(map[string][]Edge),
	}
}

// Get はグラフの内部データを取得する
func (g *GraphData) Get() map[string][]Edge {
	return g.data
}

// GetVertices はグラフの全頂点をスライスとして返す
func (g *GraphData) GetVertices() []string {
	vertices := make([]string, 0, len(g.data))
	for v := range g.data {
		vertices = append(vertices, v)
	}
	return vertices
}

// GetEdges はグラフの全辺をスライスとして返す
func (g *GraphData) GetEdges() [][3]interface{} {
	edgeMap := make(map[string][3]interface{})
	
	for vertex, edges := range g.data {
		for _, edge := range edges {
			// 辺を正規化（小さい方の頂点を最初にする）
			v1, v2 := vertex, edge.vertex
			if v1 > v2 {
				v1, v2 = v2, v1
			}
			
			// 辺のキーを作成
			key := v1 + "_" + v2
			
			// 既に追加されていなければ追加
			if _, exists := edgeMap[key]; !exists {
				edgeMap[key] = [3]interface{}{v1, v2, edge.weight}
			}
		}
	}
	
	// マップからスライスに変換
	edges := make([][3]interface{}, 0, len(edgeMap))
	for _, edge := range edgeMap {
		edges = append(edges, edge)
	}
	
	return edges
}

// GetNeighbors は指定された頂点の隣接ノードと辺の重みのスライスを返す
func (g *GraphData) GetNeighbors(vertex string) []Edge {
	if neighbors, exists := g.data[vertex]; exists {
		return neighbors
	}
	return nil
}

// GetEdgeWeight は指定された2つの頂点間の辺の重みを返す
func (g *GraphData) GetEdgeWeight(vertex1, vertex2 string) *int {
	if neighbors, exists := g.data[vertex1]; exists {
		for _, edge := range neighbors {
			if edge.vertex == vertex2 {
				return &edge.weight
			}
		}
	}
	return nil
}

// AddVertex は新しい頂点をグラフに追加する
func (g *GraphData) AddVertex(vertex string) bool {
	if _, exists := g.data[vertex]; !exists {
		g.data[vertex] = []Edge{}
	}
	return true
}

// AddEdge は両頂点間に辺を追加する
func (g *GraphData) AddEdge(vertex1, vertex2 string, weight int) bool {
	// 頂点が存在しなければ追加
	if _, exists := g.data[vertex1]; !exists {
		g.AddVertex(vertex1)
	}
	if _, exists := g.data[vertex2]; !exists {
		g.AddVertex(vertex2)
	}
	
	// vertex1 -> vertex2 の辺を追加/更新
	edgeExistsV1V2 := false
	for i, edge := range g.data[vertex1] {
		if edge.vertex == vertex2 {
			g.data[vertex1][i].weight = weight
			edgeExistsV1V2 = true
			break
		}
	}
	if !edgeExistsV1V2 {
		g.data[vertex1] = append(g.data[vertex1], Edge{vertex: vertex2, weight: weight})
	}
	
	// vertex2 -> vertex1 の辺を追加/更新
	edgeExistsV2V1 := false
	for i, edge := range g.data[vertex2] {
		if edge.vertex == vertex1 {
			g.data[vertex2][i].weight = weight
			edgeExistsV2V1 = true
			break
		}
	}
	if !edgeExistsV2V1 {
		g.data[vertex2] = append(g.data[vertex2], Edge{vertex: vertex1, weight: weight})
	}
	
	return true
}

// IsEmpty はグラフが空かどうかを返す
func (g *GraphData) IsEmpty() bool {
	return len(g.data) == 0
}

// Clear はグラフを空にする
func (g *GraphData) Clear() bool {
	g.data = make(map[string][]Edge)
	return true
}

// GetMST は最小全域木を求める
func (g *GraphData) GetMST(startVertex string) [][3]interface{} {
	vertices := g.GetVertices()
	if len(vertices) == 0 {
		return [][3]interface{}{} // グラフが空
	}
	
	if startVertex == "" && len(vertices) > 0 {
		startVertex = vertices[0]
	} else if _, exists := g.data[startVertex]; !exists {
		fmt.Printf("ERROR: 開始頂点 %s はグラフに存在しません。\n", startVertex)
		return nil
	}
	
	// MSTに含まれる頂点のセット
	inMST := make(map[string]bool)
	// 優先度付きキュー
	minHeap := &MinHeap{}
	heap.Init(minHeap)
	// MSTを構成する辺のリスト
	mstEdges := [][3]interface{}{}
	// 各頂点への最小コストとその遷移元の頂点を記録
	minCost := make(map[string]int)
	parent := make(map[string]string)
	
	// 初期化
	for _, v := range vertices {
		minCost[v] = int(^uint(0) >> 1) // max int
		parent[v] = ""
	}
	
	// 開始頂点の処理
	minCost[startVertex] = 0
	heap.Push(minHeap, &MinHeapItem{
		cost:     0,
		vertex:   startVertex,
		fromEdge: "",
		index:    0,
	})
	
	for minHeap.Len() > 0 {
		// 最小コストの辺を持つ頂点を取り出す
		item := heap.Pop(minHeap).(*MinHeapItem)
		currentVertex := item.vertex
		fromVertex := item.fromEdge
		
		// 既にMSTに含まれている頂点であればスキップ
		if inMST[currentVertex] {
			continue
		}
		
		// 現在の頂点をMSTに追加
		inMST[currentVertex] = true
		
		// MSTに追加された辺を記録 (開始頂点以外)
		if fromVertex != "" {
			// fromVertex から currentVertex への辺の重みを取得
			weight := g.GetEdgeWeight(fromVertex, currentVertex)
			if weight != nil {
				// 辺を正規化して追加
				v1, v2 := fromVertex, currentVertex
				if v1 > v2 {
					v1, v2 = v2, v1
				}
				mstEdges = append(mstEdges, [3]interface{}{v1, v2, *weight})
			}
		}
		
		// 現在の頂点に隣接する頂点を調べ、MSTへの追加コストを更新
		neighbors := g.GetNeighbors(currentVertex)
		if neighbors != nil {
			for _, edge := range neighbors {
				neighbor := edge.vertex
				weight := edge.weight
				
				// 隣接頂点がまだMSTに含まれておらず、現在のコストよりも小さい場合
				if !inMST[neighbor] && weight < minCost[neighbor] {
					minCost[neighbor] = weight
					parent[neighbor] = currentVertex
					heap.Push(minHeap, &MinHeapItem{
						cost:     weight,
						vertex:   neighbor,
						fromEdge: currentVertex,
					})
				}
			}
		}
	}
	
	return mstEdges
}

func main() {
	fmt.Println("Prims TEST -----> start")
	graphData := NewGraphData()
	
	graphData.Clear()
	inputList := [][3]interface{}{
		{"A", "B", 4},
		{"B", "C", 3},
		{"B", "D", 2},
		{"D", "A", 1},
		{"A", "C", 2},
		{"B", "D", 2},
	}
	for _, input := range inputList {
		graphData.AddEdge(input[0].(string), input[1].(string), input[2].(int))
	}
	fmt.Println("\nグラフの頂点:", graphData.GetVertices())
	fmt.Println("グラフの辺 (重み付き):", graphData.GetEdges())
	outputMst := graphData.GetMST("")
	for _, edge := range outputMst {
		fmt.Printf("Edge: %v - %v, Weight: %v\n", edge[0], edge[1], edge[2])
	}
	totalWeight := 0
	for _, edge := range outputMst {
		totalWeight += edge[2].(int)
	}
	fmt.Printf("最小全域木の合計重み: %d\n", totalWeight)
	
	graphData.Clear()
	inputList = [][3]interface{}{
		{"A", "B", 4},
		{"C", "D", 4},
		{"E", "F", 1},
		{"F", "G", 1},
	}
	for _, input := range inputList {
		graphData.AddEdge(input[0].(string), input[1].(string), input[2].(int))
	}
	fmt.Println("\nグラフの頂点:", graphData.GetVertices())
	fmt.Println("グラフの辺 (重み付き):", graphData.GetEdges())
	outputMst = graphData.GetMST("")
	for _, edge := range outputMst {
		fmt.Printf("Edge: %v - %v, Weight: %v\n", edge[0], edge[1], edge[2])
	}
	totalWeight = 0
	for _, edge := range outputMst {
		totalWeight += edge[2].(int)
	}
	fmt.Printf("最小全域木の合計重み: %d\n", totalWeight)
	
	graphData.Clear()
	inputList = [][3]interface{}{
		{"A", "B", 4},
		{"B", "C", 3},
		{"D", "E", 5},
	}
	for _, input := range inputList {
		graphData.AddEdge(input[0].(string), input[1].(string), input[2].(int))
	}
	fmt.Println("\nグラフの頂点:", graphData.GetVertices())
	fmt.Println("グラフの辺 (重み付き):", graphData.GetEdges())
	outputMst = graphData.GetMST("")
	for _, edge := range outputMst {
		fmt.Printf("Edge: %v - %v, Weight: %v\n", edge[0], edge[1], edge[2])
	}
	totalWeight = 0
	for _, edge := range outputMst {
		totalWeight += edge[2].(int)
	}
	fmt.Printf("最小全域木の合計重み: %d\n", totalWeight)
	
	graphData.Clear()
	inputList = [][3]interface{}{}
	for _, input := range inputList {
		graphData.AddEdge(input[0].(string), input[1].(string), input[2].(int))
	}
	fmt.Println("\nグラフの頂点:", graphData.GetVertices())
	fmt.Println("グラフの辺 (重み付き):", graphData.GetEdges())
	outputMst = graphData.GetMST("")
	for _, edge := range outputMst {
		fmt.Printf("Edge: %v - %v, Weight: %v\n", edge[0], edge[1], edge[2])
	}
	totalWeight = 0
	for _, edge := range outputMst {
		totalWeight += edge[2].(int)
	}
	fmt.Printf("最小全域木の合計重み: %d\n", totalWeight)
	
	fmt.Println("\nPrims TEST <----- end")
}
