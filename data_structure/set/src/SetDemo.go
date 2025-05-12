// Go
// データ構造: セット (Set)

package main

import (
	"fmt"
)

type SetData struct {
	data []interface{}
}

func NewSetData() *SetData {
	return &SetData{
		data: []interface{}{},
	}
}

func (s *SetData) Get() []interface{} {
	// 要素を取得
	return s.data
}

func (s *SetData) GetIndex(item interface{}) int {
	// 指定された要素がセット内に存在するかどうかをチェックする。
	for i, v := range s.data {
		if v == item {
			return i
		}
	}
	fmt.Printf("ERROR: %v は範囲外です\n", item)
	return -1
}

func (s *SetData) GetValue(index int) interface{} {
	// 指定されたインデックスの要素を取得する。
	if 0 <= index && index < len(s.data) {
		return s.data[index]
	} else {
		fmt.Printf("ERROR: %v は範囲外です\n", index)
		return nil
	}
}

func (s *SetData) Add(item interface{}) bool {
	// 要素をセットに追加する。
	for _, v := range s.data {
		if v == item {
			fmt.Printf("ERROR: %v は重複です\n", item)
			return false
		}
	}
	s.data = append(s.data, item)
	return true
}

func (s *SetData) Remove(item interface{}) bool {
	// 指定された要素をセットから削除する。
	for i, v := range s.data {
		if v == item {
			// スライスから要素を削除
			s.data = append(s.data[:i], s.data[i+1:]...)
			return true
		}
	}
	fmt.Printf("ERROR: %v は範囲外です\n", item)
	return false
}

func (s *SetData) IsEmpty() bool {
	// 空かどうかをチェックする
	return len(s.data) == 0
}

func (s *SetData) Size() int {
	// 要素数を返す
	return len(s.data)
}

func (s *SetData) Clear() bool {
	// 空にする
	s.data = []interface{}{}
	return true
}

func main() {
	fmt.Println("Set TEST -----> start")

	fmt.Println("\nnew")
	setData := NewSetData()
	fmt.Printf("  現在のデータ: %v\n", setData.Get())

	fmt.Println("\nadd")
	input := []interface{}{10, 20, 30, 20, 40}
	for _, item := range input {
		fmt.Printf("  入力値: %v\n", item)
		output := setData.Add(item)
		fmt.Printf("  出力値: %v\n", output)
		fmt.Printf("  現在のデータ: %v\n", setData.Get())
	}

	fmt.Println("\nsize")
	output := setData.Size()
	fmt.Printf("  出力値: %v\n", output)

	fmt.Println("\nis_empty")
	outputEmpty := setData.IsEmpty()
	fmt.Printf("  出力値: %v\n", outputEmpty)

	fmt.Println("\nget_value")
	inputIndexes := []int{0, 2, 5}
	for _, index := range inputIndexes {
		fmt.Printf("  入力値: %v\n", index)
		outputValue := setData.GetValue(index)
		fmt.Printf("  出力値: %v\n", outputValue)
	}

	fmt.Println("\nget_index")
	inputItems := []interface{}{30, 99}
	for _, item := range inputItems {
		fmt.Printf("  入力値: %v\n", item)
		outputIndex := setData.GetIndex(item)
		fmt.Printf("  出力値: %v\n", outputIndex)
	}

	fmt.Println("\nremove")
	removeItems := []interface{}{20, 50, 10}
	for _, item := range removeItems {
		fmt.Printf("  入力値: %v\n", item)
		outputRemove := setData.Remove(item)
		fmt.Printf("  出力値: %v\n", outputRemove)
		fmt.Printf("  現在のデータ: %v\n", setData.Get())
	}

	fmt.Println("\nsize")
	outputSize := setData.Size()
	fmt.Printf("  出力値: %v\n", outputSize)

	fmt.Println("\nclear")
	outputClear := setData.Clear()
	fmt.Printf("  出力値: %v\n", outputClear)
	fmt.Printf("  現在のデータ: %v\n", setData.Get())

	fmt.Println("\nis_empty")
	outputEmpty = setData.IsEmpty()
	fmt.Printf("  出力値: %v\n", outputEmpty)

	fmt.Println("\nSet TEST <----- end")
}
