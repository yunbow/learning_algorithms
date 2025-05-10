// Go
// データ構造: 配列 (Array)

package main

import (
	"fmt"
	"sort"
)

// ArrayData は配列操作のためのデータ構造
type ArrayData struct {
	data []interface{}
}

// Get は要素を取得する
func (a *ArrayData) Get() []interface{} {
	return a.data
}

// GetIndex は配列内で指定された値を検索し、最初に見つかったインデックスを返す
func (a *ArrayData) GetIndex(item interface{}) int {
	for i, v := range a.data {
		if v == item {
			return i
		}
	}
	fmt.Printf("ERROR: %v は範囲外です\n", item)
	return -1
}

// GetValue は指定されたインデックスの要素を取得する
func (a *ArrayData) GetValue(index int) interface{} {
	if 0 <= index && index < len(a.data) {
		return a.data[index]
	}
	fmt.Printf("ERROR: %d は範囲外です\n", index)
	return nil
}

// Add は配列の末尾に要素を追加する
func (a *ArrayData) Add(item interface{}) bool {
	a.data = append(a.data, item)
	return true
}

// Remove は指定されたインデックスの要素を削除する
func (a *ArrayData) Remove(index int) bool {
	if 0 <= index && index < len(a.data) {
		a.data = append(a.data[:index], a.data[index+1:]...)
		return true
	}
	fmt.Printf("ERROR: %d は範囲外です\n", index)
	return false
}

// Update は指定されたインデックスの要素を新しい値に更新する
func (a *ArrayData) Update(index int, newValue interface{}) bool {
	if 0 <= index && index < len(a.data) {
		a.data[index] = newValue
		return true
	}
	fmt.Printf("ERROR: %d は範囲外です\n", index)
	return false
}

// Reverse は配列の要素を逆順にする
func (a *ArrayData) Reverse() []interface{} {
	for i, j := 0, len(a.data)-1; i < j; i, j = i+1, j-1 {
		a.data[i], a.data[j] = a.data[j], a.data[i]
	}
	return a.data
}

// Sort は配列の要素をソートする
func (a *ArrayData) Sort(descending bool) []interface{} {
	// インターフェース型のスライスを整数型のスライスに変換
	intData := make([]int, 0, len(a.data))
	for _, v := range a.data {
		if intVal, ok := v.(int); ok {
			intData = append(intData, intVal)
		}
	}

	if descending {
		sort.Sort(sort.Reverse(sort.IntSlice(intData)))
	} else {
		sort.Ints(intData)
	}

	// 整数型のスライスをインターフェース型のスライスに戻す
	for i, v := range intData {
		a.data[i] = v
	}

	return a.data
}

// IsEmpty は配列が空かどうかを確認する
func (a *ArrayData) IsEmpty() bool {
	return len(a.data) == 0
}

// Size は配列のサイズ（要素数）を返す
func (a *ArrayData) Size() int {
	return len(a.data)
}

// Clear は配列の全要素を削除する
func (a *ArrayData) Clear() bool {
	a.data = []interface{}{}
	return true
}

func main() {
	fmt.Println("Array TEST -----> start")

	fmt.Println("\nnew")
	arrayData := &ArrayData{}
	fmt.Printf("  現在のデータ: %v\n", arrayData.Get())

	fmt.Println("\nadd")
	input := []int{10, 20, 30, 10, 40}
	for _, item := range input {
		fmt.Printf("  入力値: %d\n", item)
		output := arrayData.Add(item)
		fmt.Printf("  出力値: %v\n", output)
		fmt.Printf("  現在のデータ: %v\n", arrayData.Get())
	}

	fmt.Println("\nsize")
	output := arrayData.Size()
	fmt.Printf("  出力値: %d\n", output)

	fmt.Println("\nis_empty")
	isEmpty := arrayData.IsEmpty()
	fmt.Printf("  出力値: %v\n", isEmpty)

	fmt.Println("\nget_value")
	indexInput := 2
	fmt.Printf("  入力値: %d\n", indexInput)
	valueOutput := arrayData.GetValue(indexInput)
	fmt.Printf("  出力値: %v\n", valueOutput)

	fmt.Println("\nget_value")
	outOfRangeIndex := 10
	fmt.Printf("  入力値: %d\n", outOfRangeIndex)
	outOfRangeOutput := arrayData.GetValue(outOfRangeIndex)
	fmt.Printf("  出力値: %v\n", outOfRangeOutput)

	fmt.Println("\nupdate")
	updateIndex, updateValue := 1, 25
	fmt.Printf("  入力値: (%d, %d)\n", updateIndex, updateValue)
	updateOutput := arrayData.Update(updateIndex, updateValue)
	fmt.Printf("  出力値: %v\n", updateOutput)
	fmt.Printf("  現在のデータ: %v\n", arrayData.Get())

	fmt.Println("\nupdate")
	invalidIndex, updateValue := 15, 25
	fmt.Printf("  入力値: (%d, %d)\n", invalidIndex, updateValue)
	invalidUpdateOutput := arrayData.Update(invalidIndex, updateValue)
	fmt.Printf("  出力値: %v\n", invalidUpdateOutput)
	fmt.Printf("  現在のデータ: %v\n", arrayData.Get())

	fmt.Println("\nget_index")
	findItem := 10
	fmt.Printf("  入力値: %d\n", findItem)
	findOutput := arrayData.GetIndex(findItem)
	fmt.Printf("  出力値: %d\n", findOutput)

	fmt.Println("\nget_index")
	notFoundItem := 99
	fmt.Printf("  入力値: %d\n", notFoundItem)
	notFoundOutput := arrayData.GetIndex(notFoundItem)
	fmt.Printf("  出力値: %d\n", notFoundOutput)

	fmt.Println("\nremove")
	removeIndex := 3
	fmt.Printf("  入力値: %d\n", removeIndex)
	removeOutput := arrayData.Remove(removeIndex)
	fmt.Printf("  出力値: %v\n", removeOutput)
	fmt.Printf("  現在のデータ: %v\n", arrayData.Get())

	fmt.Println("\nremove")
	invalidRemoveIndex := 8
	fmt.Printf("  入力値: %d\n", invalidRemoveIndex)
	invalidRemoveOutput := arrayData.Remove(invalidRemoveIndex)
	fmt.Printf("  出力値: %v\n", invalidRemoveOutput)
	fmt.Printf("  現在のデータ: %v\n", arrayData.Get())

	fmt.Println("\nreverse")
	reverseOutput := arrayData.Reverse()
	fmt.Printf("  出力値: %v\n", reverseOutput)

	fmt.Println("\nsort")
	fmt.Println("  入力値: descending=false")
	sortAscOutput := arrayData.Sort(false)
	fmt.Printf("  出力値: %v\n", sortAscOutput)

	fmt.Println("\nsort")
	fmt.Println("  入力値: descending=true")
	sortDescOutput := arrayData.Sort(true)
	fmt.Printf("  出力値: %v\n", sortDescOutput)

	fmt.Println("\nclear")
	clearOutput := arrayData.Clear()
	fmt.Printf("  出力値: %v\n", clearOutput)
	fmt.Printf("  現在のデータ: %v\n", arrayData.Get())

	fmt.Println("\nis_empty")
	isEmptyAfterClear := arrayData.IsEmpty()
	fmt.Printf("  出力値: %v\n", isEmptyAfterClear)

	fmt.Println("\nArray TEST <----- end")
}