// Go
// 配列の並び替え: 選択ソート (Selection Sort)
package main

import (
	"fmt"
)

type ArrayData struct {
	data []int
}

func NewArrayData() *ArrayData {
	return &ArrayData{
		data: []int{},
	}
}

func (a *ArrayData) Get() []int {
	return a.data
}

func (a *ArrayData) Set(data []int) bool {
	a.data = data
	return true
}

func (a *ArrayData) selectionSort(target []int) []int {
	// 配列の長さを取得
	n := len(target)

	// 配列を順番に走査
	for i := 0; i < n; i++ {
		// 未ソート部分の最小値のインデックスを見つける
		minIndex := i
		for j := i + 1; j < n; j++ {
			if target[j] < target[minIndex] {
				minIndex = j
			}
		}

		// 見つかった最小値と現在の位置を交換
		target[i], target[minIndex] = target[minIndex], target[i]
	}

	return target
}

func (a *ArrayData) Sort() bool {
	a.selectionSort(a.data)
	return true
}

func main() {
	fmt.Println("SelectionSort TEST -----> start")

	arrayData := NewArrayData()

	// ランダムな整数の配列
	fmt.Println("\nsort")
	input := []int{64, 34, 25, 12, 22, 11, 90}
	fmt.Printf("  ソート前: %v\n", input)
	arrayData.Set(input)
	arrayData.Sort()
	fmt.Printf("  ソート後: %v\n", arrayData.Get())

	// 既にソートされている配列
	fmt.Println("\nsort")
	input = []int{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}
	fmt.Printf("  ソート前: %v\n", input)
	arrayData.Set(input)
	arrayData.Sort()
	fmt.Printf("  ソート後: %v\n", arrayData.Get())

	// 逆順の配列
	fmt.Println("\nsort")
	input = []int{10, 9, 8, 7, 6, 5, 4, 3, 2, 1}
	fmt.Printf("  ソート前: %v\n", input)
	arrayData.Set(input)
	arrayData.Sort()
	fmt.Printf("  ソート後: %v\n", arrayData.Get())

	// 重複要素を含む配列
	fmt.Println("\nsort")
	input = []int{10, 9, 8, 7, 6, 10, 9, 8, 7, 6}
	fmt.Printf("  ソート前: %v\n", input)
	arrayData.Set(input)
	arrayData.Sort()
	fmt.Printf("  ソート後: %v\n", arrayData.Get())

	// 空の配列
	fmt.Println("\nsort")
	input = []int{}
	fmt.Printf("  ソート前: %v\n", input)
	arrayData.Set(input)
	arrayData.Sort()
	fmt.Printf("  ソート後: %v\n", arrayData.Get())

	fmt.Println("\nSelectionSort TEST <----- end")
}