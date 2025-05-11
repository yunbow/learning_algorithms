// Go
// 配列の並び替え: ヒープソート (Heap Sort)
package main

import "fmt"

// HeapData はヒープデータ構造を表します
type HeapData struct {
	data       []int
	isMinHeap bool
}

// NewHeapData は新しいHeapDataインスタンスを作成します
func NewHeapData(isMinHeap bool) *HeapData {
	return &HeapData{
		data:       []int{},
		isMinHeap:  isMinHeap,
	}
}

// getParentIdx は親ノードのインデックスを計算します
func (h *HeapData) getParentIdx(idx int) int {
	if idx <= 0 {
		return -1 // 根ノードには親がない
	}
	return (idx - 1) / 2
}

// getLeftChildIdx は左の子ノードのインデックスを計算します
func (h *HeapData) getLeftChildIdx(idx int) int {
	return 2*idx + 1
}

// getRightChildIdx は右の子ノードのインデックスを計算します
func (h *HeapData) getRightChildIdx(idx int) int {
	return 2*idx + 2
}

// hasParent は親ノードが存在するか確認します
func (h *HeapData) hasParent(idx int) bool {
	parentIdx := h.getParentIdx(idx)
	return parentIdx >= 0 && parentIdx < len(h.data)
}

// hasLeftChild は左の子ノードが存在するか確認します
func (h *HeapData) hasLeftChild(idx int) bool {
	return h.getLeftChildIdx(idx) < len(h.data)
}

// hasRightChild は右の子ノードが存在するか確認します
func (h *HeapData) hasRightChild(idx int) bool {
	return h.getRightChildIdx(idx) < len(h.data)
}

// swap は2つのノードの値を交換します
func (h *HeapData) swap(idx1, idx2 int) {
	if 0 <= idx1 && idx1 < len(h.data) && 0 <= idx2 && idx2 < len(h.data) {
		h.data[idx1], h.data[idx2] = h.data[idx2], h.data[idx1]
	} else {
		fmt.Printf("Warning: Swap indices out of bounds: %d, %d\n", idx1, idx2)
	}
}

// shouldSwap は交換が必要かどうかを判断します
func (h *HeapData) shouldSwap(idx1, idx2 int) bool {
	if h.isMinHeap {
		return h.data[idx1] > h.data[idx2]
	}
	return h.data[idx1] < h.data[idx2]
}

// heapifyDown はノードを下方向に移動させてヒープ条件を満たします
func (h *HeapData) heapifyDown(idx int) {
	smallestOrLargest := idx

	leftChildIdx := h.getLeftChildIdx(idx)
	rightChildIdx := h.getRightChildIdx(idx)

	// 左の子ノードと比較（存在する場合）
	if h.hasLeftChild(idx) {
		if h.isMinHeap { // 最小ヒープ: 最小の子と比較
			if h.data[leftChildIdx] < h.data[smallestOrLargest] {
				smallestOrLargest = leftChildIdx
			}
		} else { // 最大ヒープ: 最大の子と比較
			if h.data[leftChildIdx] > h.data[smallestOrLargest] {
				smallestOrLargest = leftChildIdx
			}
		}
	}

	// 右の子ノードと比較（存在する場合）
	if h.hasRightChild(idx) {
		if h.isMinHeap { // 最小ヒープ: 最小の子と比較
			if h.data[rightChildIdx] < h.data[smallestOrLargest] {
				smallestOrLargest = rightChildIdx
			}
		} else { // 最大ヒープ: 最大の子と比較
			if h.data[rightChildIdx] > h.data[smallestOrLargest] {
				smallestOrLargest = rightChildIdx
			}
		}
	}

	// インデックスが変わっていたら交換して再帰的に処理
	if smallestOrLargest != idx {
		h.swap(idx, smallestOrLargest)
		h.heapifyDown(smallestOrLargest)
	}
}

// maxHeapifyDown は指定されたヒープサイズ内で、idxから下方向に最大ヒープ条件を満たすように調整します
func (h *HeapData) maxHeapifyDown(idx, heapSize int) {
	largest := idx // 最大ヒープでは、親と子の間で最大のものを探す

	leftChildIdx := h.getLeftChildIdx(idx)
	rightChildIdx := h.getRightChildIdx(idx)

	// 左の子ノードと比較（ヒープサイズ内に存在する場合）
	if leftChildIdx < heapSize && h.data[leftChildIdx] > h.data[largest] {
		largest = leftChildIdx
	}

	// 右の子ノードと比較（ヒープサイズ内に存在する場合）
	if rightChildIdx < heapSize && h.data[rightChildIdx] > h.data[largest] {
		largest = rightChildIdx
	}

	// largestが現在のノードidxと異なる場合、交換して再帰的に処理
	if largest != idx {
		h.swap(idx, largest)
		h.maxHeapifyDown(largest, heapSize)
	}
}

// heapifyUp はノードを上方向に移動させてヒープ条件を満たします
func (h *HeapData) heapifyUp(idx int) {
	// ノードを上方向に移動させてヒープ条件を満たす (isMinHeap に従う)
	for h.hasParent(idx) {
		parentIdx := h.getParentIdx(idx)
		if h.shouldSwap(parentIdx, idx) {
			h.swap(parentIdx, idx)
			idx = parentIdx
		} else {
			break // ヒープ条件を満たした
		}
	}
}

// Get は現在のデータを返します
func (h *HeapData) Get() []int {
	return h.data
}

// Heapify は配列をヒープ化します
func (h *HeapData) Heapify(array []int) bool {
	h.data = array
	n := len(array)
	// 最後の非葉ノードから根に向かって、各部分木をヒープ化
	for i := n/2 - 1; i >= 0; i-- {
		h.heapifyDown(i)
	}
	return true
}

// Sort はヒープソートを実行します
func (h *HeapData) Sort() bool {
	n := len(h.data)

	// 配列を最大ヒープに変換する
	for i := n/2 - 1; i >= 0; i-- {
		h.maxHeapifyDown(i, n)
	}

	// 最大ヒープから要素を一つずつ取り出してソート済みの位置に移動
	for i := n - 1; i > 0; i-- {
		// 現在の根（最大値）をヒープの最後の要素と交換
		h.swap(0, i)

		// 交換された要素（元の最大値）は正しい位置に置かれたので、
		// 次のヒープ調整からは除外する。ヒープサイズはiに減少。
		// 新しい根（元のヒープの最後の要素だったもの）から
		// maxHeapifyDownを使って残りの要素を最大ヒープに調整
		h.maxHeapifyDown(0, i)
	}

	return true
}

func main() {
	fmt.Println("HeapSort TEST -----> start")

	heapData := NewHeapData(true)

	// ランダムな整数の配列
	fmt.Println("\nsort")
	input := []int{64, 34, 25, 12, 22, 11, 90}
	fmt.Printf("  ソート前: %v\n", input)
	heapData.Heapify(input)
	heapData.Sort()
	fmt.Printf("  ソート後: %v\n", heapData.Get())

	// 既にソートされている配列
	fmt.Println("\nsort")
	input = []int{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}
	fmt.Printf("  ソート前: %v\n", input)
	heapData.Heapify(input)
	heapData.Sort()
	fmt.Printf("  ソート後: %v\n", heapData.Get())

	// 逆順の配列
	fmt.Println("\nsort")
	input = []int{10, 9, 8, 7, 6, 5, 4, 3, 2, 1}
	fmt.Printf("  ソート前: %v\n", input)
	heapData.Heapify(input)
	heapData.Sort()
	fmt.Printf("  ソート後: %v\n", heapData.Get())

	// 重複要素を含む配列
	fmt.Println("\nsort")
	input = []int{10, 9, 8, 7, 6, 10, 9, 8, 7, 6}
	fmt.Printf("  ソート前: %v\n", input)
	heapData.Heapify(input)
	heapData.Sort()
	fmt.Printf("  ソート後: %v\n", heapData.Get())

	// 空の配列
	fmt.Println("\nsort")
	input = []int{}
	fmt.Printf("  ソート前: %v\n", input)
	heapData.Heapify(input)
	heapData.Sort()
	fmt.Printf("  ソート後: %v\n", heapData.Get())

	fmt.Println("\nHeapSort TEST <----- end")
}