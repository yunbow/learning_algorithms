// Go
// 配列の並び替え: マージソート (Merge Sort)

package main

import (
	"fmt"
)

// ArrayData は配列データとそれに対する操作を管理する構造体
type ArrayData struct {
	data []int
}

// Get は内部データを返します
func (a *ArrayData) Get() []int {
	return a.data
}

// Set は内部データを設定します
func (a *ArrayData) Set(data []int) bool {
	a.data = data
	return true
}

// mergeSort は配列を再帰的にソートします
func (a *ArrayData) mergeSort(target []int) []int {
	// 配列の長さが1以下の場合はそのまま返す（基本ケース）
	if len(target) <= 1 {
		return target
	}

	// 配列を半分に分割
	mid := len(target) / 2
	leftHalf := make([]int, mid)
	rightHalf := make([]int, len(target)-mid)

	copy(leftHalf, target[:mid])
	copy(rightHalf, target[mid:])

	// 左右の半分を再帰的にソート
	leftHalf = a.mergeSort(leftHalf)
	rightHalf = a.mergeSort(rightHalf)

	// ソート済みの半分同士をマージ
	return a.merge(leftHalf, rightHalf)
}

// merge は2つのソート済み配列をマージします
func (a *ArrayData) merge(left, right []int) []int {
	result := make([]int, 0, len(left)+len(right))
	i, j := 0, 0

	// 左右の配列を比較しながらマージ
	for i < len(left) && j < len(right) {
		if left[i] <= right[j] {
			result = append(result, left[i])
			i++
		} else {
			result = append(result, right[j])
			j++
		}
	}

	// 残った要素を追加
	result = append(result, left[i:]...)
	result = append(result, right[j:]...)

	return result
}

// Sort は内部データをソートします
func (a *ArrayData) Sort() bool {
	a.data = a.mergeSort(a.data)
	return true
}

func main() {
	fmt.Println("MergeSort TEST -----> start")

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

	fmt.Println("\nMergeSort TEST <----- end")
}