// Go
// データ構造: ヒープ (Heap)

package main

import (
	"fmt"
)

// HeapData は最小ヒープまたは最大ヒープを実装する構造体
type HeapData struct {
	data      []int
	isMinHeap bool
}

// NewHeapData は新しいHeapDataインスタンスを作成する
func NewHeapData(isMinHeap bool) *HeapData {
	return &HeapData{
		data:      []int{},
		isMinHeap: isMinHeap,
	}
}

// getParentIdx は親ノードのインデックスを計算する
func (h *HeapData) getParentIdx(idx int) int {
	return (idx - 1) / 2
}

// getLeftChildIdx は左の子ノードのインデックスを計算する
func (h *HeapData) getLeftChildIdx(idx int) int {
	return 2*idx + 1
}

// getRightChildIdx は右の子ノードのインデックスを計算する
func (h *HeapData) getRightChildIdx(idx int) int {
	return 2*idx + 2
}

// hasParent は親ノードが存在するか確認する
func (h *HeapData) hasParent(idx int) bool {
	return h.getParentIdx(idx) >= 0
}

// hasLeftChild は左の子ノードが存在するか確認する
func (h *HeapData) hasLeftChild(idx int) bool {
	return h.getLeftChildIdx(idx) < len(h.data)
}

// hasRightChild は右の子ノードが存在するか確認する
func (h *HeapData) hasRightChild(idx int) bool {
	return h.getRightChildIdx(idx) < len(h.data)
}

// getParent は親ノードの値を取得する
func (h *HeapData) getParent(idx int) int {
	return h.data[h.getParentIdx(idx)]
}

// getLeftChild は左の子ノードの値を取得する
func (h *HeapData) getLeftChild(idx int) int {
	return h.data[h.getLeftChildIdx(idx)]
}

// getRightChild は右の子ノードの値を取得する
func (h *HeapData) getRightChild(idx int) int {
	return h.data[h.getRightChildIdx(idx)]
}

// swap は2つのノードの値を交換する
func (h *HeapData) swap(idx1, idx2 int) {
	h.data[idx1], h.data[idx2] = h.data[idx2], h.data[idx1]
}

// shouldSwap は2つのノードが交換すべきかを判断する
func (h *HeapData) shouldSwap(idx1, idx2 int) bool {
	if h.isMinHeap {
		return h.data[idx1] > h.data[idx2]
	}
	return h.data[idx1] < h.data[idx2]
}

// heapifyDown はノードを下方向に移動させてヒープ条件を満たす
func (h *HeapData) heapifyDown(idx int) {
	smallestOrLargest := idx

	// 左の子ノードと比較
	if h.hasLeftChild(idx) && h.shouldSwap(smallestOrLargest, h.getLeftChildIdx(idx)) {
		smallestOrLargest = h.getLeftChildIdx(idx)
	}

	// 右の子ノードと比較
	if h.hasRightChild(idx) && h.shouldSwap(smallestOrLargest, h.getRightChildIdx(idx)) {
		smallestOrLargest = h.getRightChildIdx(idx)
	}

	// インデックスが変わっていたら交換して再帰的に処理
	if smallestOrLargest != idx {
		h.swap(idx, smallestOrLargest)
		h.heapifyDown(smallestOrLargest)
	}
}

// heapifyUp はノードを上方向に移動させてヒープ条件を満たす
func (h *HeapData) heapifyUp(idx int) {
	// 親がある限り、親と比較して必要なら交換
	for h.hasParent(idx) && h.shouldSwap(h.getParentIdx(idx), idx) {
		parentIdx := h.getParentIdx(idx)
		h.swap(parentIdx, idx)
		idx = parentIdx
	}
}

// Get はヒープの要素を取得する
func (h *HeapData) Get() []int {
	return h.data
}

// GetIndex は配列内で指定された値を検索し、最初に見つかったインデックスを返す
func (h *HeapData) GetIndex(item int) int {
	for i, v := range h.data {
		if v == item {
			return i
		}
	}
	fmt.Printf("ERROR: %d は範囲外です\n", item)
	return -1
}

// GetValue は指定されたインデックスの要素を取得する
func (h *HeapData) GetValue(index int) *int {
	if 0 <= index && index < len(h.data) {
		return &h.data[index]
	}
	fmt.Printf("ERROR: %d は範囲外です\n", index)
	return nil
}

// Heapify は配列をヒープに変換する
func (h *HeapData) Heapify(array []int) bool {
	h.data = make([]int, len(array))
	copy(h.data, array)
	// 最後の親ノードから根に向かって、各部分木をヒープ化
	for i := len(h.data)/2 - 1; i >= 0; i-- {
		h.heapifyDown(i)
	}
	return true
}

// Push はヒープに要素を追加する
func (h *HeapData) Push(value int) bool {
	h.data = append(h.data, value)
	// 最後の要素を適切な位置に移動
	h.heapifyUp(len(h.data) - 1)
	return true
}

// Pop はヒープから最大/最小要素を削除する
func (h *HeapData) Pop() bool {
	// ヒープが空の場合
	if len(h.data) == 0 {
		return false
	}

	// 最後の要素をルートに移動
	lastElement := h.data[len(h.data)-1]
	h.data = h.data[:len(h.data)-1]

	if len(h.data) > 0 {
		h.data[0] = lastElement
		// ルートから下方向にヒープ条件を満たすように調整
		h.heapifyDown(0)
	}

	return true
}

// Peek はヒープの最大/最小要素を返す（取り出さない）
func (h *HeapData) Peek() *int {
	// ヒープが空の場合
	if len(h.data) == 0 {
		return nil
	}
	// ルート要素を返す
	return &h.data[0]
}

// IsEmpty はヒープが空かどうかを確認する
func (h *HeapData) IsEmpty() bool {
	return len(h.data) == 0
}

// Size はヒープのサイズを返す
func (h *HeapData) Size() int {
	return len(h.data)
}

// Clear はヒープをクリアする
func (h *HeapData) Clear() bool {
	h.data = []int{}
	return true
}

func main() {
	fmt.Println("Heap TEST -----> start")

	fmt.Println("\nmin heap: new")
	minHeap := NewHeapData(true)
	fmt.Printf("  現在のデータ: %v\n", minHeap.Get())

	fmt.Println("\nmin heap: heapify")
	input := []int{4, 10, 3, 5, 1}
	fmt.Printf("  入力値: %v\n", input)
	output := minHeap.Heapify(input)
	fmt.Printf("  出力値: %v\n", output)
	fmt.Printf("  現在のデータ: %v\n", minHeap.Get())

	fmt.Println("\nmin heap: push")
	inputVal := 2
	fmt.Printf("  入力値: %v\n", inputVal)
	output = minHeap.Push(inputVal)
	fmt.Printf("  出力値: %v\n", output)
	fmt.Printf("  現在のデータ: %v\n", minHeap.Get())

	fmt.Println("\nmin heap: push")
	inputVal = 15
	fmt.Printf("  入力値: %v\n", inputVal)
	output = minHeap.Push(inputVal)
	fmt.Printf("  出力値: %v\n", output)
	fmt.Printf("  現在のデータ: %v\n", minHeap.Get())

	fmt.Println("\nmin heap: peek")
	peekVal := minHeap.Peek()
	if peekVal != nil {
		fmt.Printf("  出力値: %v\n", *peekVal)
	} else {
		fmt.Printf("  出力値: %v\n", peekVal)
	}

	fmt.Println("\nmin heap: pop")
	output = minHeap.Pop()
	fmt.Printf("  出力値: %v\n", output)
	fmt.Printf("  現在のデータ: %v\n", minHeap.Get())

	fmt.Println("\nmin heap: pop")
	output = minHeap.Pop()
	fmt.Printf("  出力値: %v\n", output)
	fmt.Printf("  現在のデータ: %v\n", minHeap.Get())

	fmt.Println("\nmin heap: get_index")
	inputVal = 3
	fmt.Printf("  入力値: %v\n", inputVal)
	idx := minHeap.GetIndex(inputVal)
	fmt.Printf("  出力値: %v\n", idx)

	fmt.Println("\nmin heap: get_index")
	inputVal = 100
	fmt.Printf("  入力値: %v\n", inputVal)
	idx = minHeap.GetIndex(inputVal)
	fmt.Printf("  出力値: %v\n", idx)

	fmt.Println("\nmin heap: is_empty")
	isEmpty := minHeap.IsEmpty()
	fmt.Printf("  出力値: %v\n", isEmpty)

	fmt.Println("\nmin heap: size")
	size := minHeap.Size()
	fmt.Printf("  出力値: %v\n", size)

	fmt.Println("\nmin heap: clear")
	output = minHeap.Clear()
	fmt.Printf("  出力値: %v\n", output)
	fmt.Printf("  現在のデータ: %v\n", minHeap.Get())

	fmt.Println("\nmin heap: is_empty")
	isEmpty = minHeap.IsEmpty()
	fmt.Printf("  出力値: %v\n", isEmpty)

	fmt.Println("\nmax heap: new")
	maxHeap := NewHeapData(false)
	fmt.Printf("  現在のデータ: %v\n", maxHeap.Get())

	fmt.Println("\nmax heap: heapify")
	fmt.Printf("  入力値: %v\n", input)
	output = maxHeap.Heapify(input)
	fmt.Printf("  出力値: %v\n", output)
	fmt.Printf("  現在のデータ: %v\n", maxHeap.Get())

	fmt.Println("\nmax heap: push")
	inputVal = 12
	fmt.Printf("  入力値: %v\n", inputVal)
	output = maxHeap.Push(inputVal)
	fmt.Printf("  出力値: %v\n", output)
	fmt.Printf("  現在のデータ: %v\n", maxHeap.Get())

	fmt.Println("\nmax heap: push")
	inputVal = 0
	fmt.Printf("  入力値: %v\n", inputVal)
	output = maxHeap.Push(inputVal)
	fmt.Printf("  出力値: %v\n", output)
	fmt.Printf("  現在のデータ: %v\n", maxHeap.Get())

	fmt.Println("\nmax heap: peek")
	peekVal = maxHeap.Peek()
	if peekVal != nil {
		fmt.Printf("  出力値: %v\n", *peekVal)
	} else {
		fmt.Printf("  出力値: %v\n", peekVal)
	}

	fmt.Println("\nmax heap: pop")
	output = maxHeap.Pop()
	fmt.Printf("  出力値: %v\n", output)
	fmt.Printf("  現在のデータ: %v\n", maxHeap.Get())

	fmt.Println("\nmax heap: pop")
	output = maxHeap.Pop()
	fmt.Printf("  出力値: %v\n", output)
	fmt.Printf("  現在のデータ: %v\n", maxHeap.Get())

	fmt.Println("\nmax heap: get_index")
	inputVal = 5
	fmt.Printf("  入力値: %v\n", inputVal)
	idx = maxHeap.GetIndex(inputVal)
	fmt.Printf("  出力値: %v\n", idx)

	fmt.Println("\nmax heap: get_index")
	inputVal = -10
	fmt.Printf("  入力値: %v\n", inputVal)
	idx = maxHeap.GetIndex(inputVal)
	fmt.Printf("  出力値: %v\n", idx)

	fmt.Println("\nmax heap: is_empty")
	isEmpty = maxHeap.IsEmpty()
	fmt.Printf("  出力値: %v\n", isEmpty)

	fmt.Println("\nmax heap: size")
	size = maxHeap.Size()
	fmt.Printf("  出力値: %v\n", size)

	fmt.Println("\nmax heap: clear")
	output = maxHeap.Clear()
	fmt.Printf("  出力値: %v\n", output)
	fmt.Printf("  現在のデータ: %v\n", maxHeap.Get())

	fmt.Println("\nmax heap: is_empty")
	isEmpty = maxHeap.IsEmpty()
	fmt.Printf("  出力値: %v\n", isEmpty)

	fmt.Println("\nHeap TEST <----- end")
}