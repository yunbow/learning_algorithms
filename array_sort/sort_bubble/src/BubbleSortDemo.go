// Go
// 配列の並び替え: バブルソート (Bubble Sort)

package main

import "fmt"

type ArrayData struct {
	data []int
}

func (a *ArrayData) Get() []int {
	return a.data
}

func (a *ArrayData) Set(data []int) bool {
	a.data = data
	return true
}

func (a *ArrayData) Sort() bool {
	n := len(a.data)

	// 外側のループ: n-1回の走査が必要
	for i := 0; i < n; i++ {
		// 最適化: 一度の走査で交換がなければソート完了
		swapped := false

		// 内側のループ: まだソートされていない部分を走査
		// 各走査後に最大の要素が末尾に移動するため、i回分を除外
		for j := 0; j < n-i-1; j++ {
			// 隣接する要素を比較し、必要に応じて交換
			if a.data[j] > a.data[j+1] {
				a.data[j], a.data[j+1] = a.data[j+1], a.data[j]
				swapped = true
			}
		}

		// 交換が発生しなければソート完了
		if !swapped {
			break
		}
	}
	return true
}

func main() {
	fmt.Println("BubbleSort TEST -----> start")

	arrayData := &ArrayData{}

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

	fmt.Println("\nBubbleSort TEST <----- end")
}