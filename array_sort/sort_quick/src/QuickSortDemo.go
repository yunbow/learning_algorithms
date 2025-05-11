// Go
// 配列の並び替え: クイックソート (Quick Sort)

package main

import "fmt"

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

func (a *ArrayData) quickSort(target []int) []int {
	// 空の配列または要素が1つの場合はそのまま返す（基底条件）
	if len(target) <= 1 {
		return target
	}

	// ピボットを選択（この実装では最後の要素を選択）
	pivot := target[len(target)-1]

	// ピボットより小さい要素と大きい要素に分ける
	var left []int
	var right []int

	// 最後の要素（ピボット）を除いて配列をスキャン
	for i := 0; i < len(target)-1; i++ {
		if target[i] <= pivot {
			left = append(left, target[i])
		} else {
			right = append(right, target[i])
		}
	}

	// 左側の部分配列、ピボット、右側の部分配列を再帰的にソートして結合
	result := append(a.quickSort(left), pivot)
	return append(result, a.quickSort(right)...)
}

func (a *ArrayData) Sort() bool {
	a.data = a.quickSort(a.data)
	return true
}

func main() {
	fmt.Println("QuickSort TEST -----> start")

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

	fmt.Println("\nQuickSort TEST <----- end")
}