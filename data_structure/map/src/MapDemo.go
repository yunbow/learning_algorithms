// Go
// データ構造: マップ (Map)

package main

import (
	"fmt"
)

type MapData struct {
	data map[string]int
}

func NewMapData() *MapData {
	return &MapData{
		data: make(map[string]int),
	}
}

func (m *MapData) Get() [][]interface{} {
	items := [][]interface{}{}
	for k, v := range m.data {
		items = append(items, []interface{}{k, v})
	}
	return items
}

func (m *MapData) GetKeys() []string {
	keys := make([]string, 0, len(m.data))
	for k := range m.data {
		keys = append(keys, k)
	}
	return keys
}

func (m *MapData) GetValues() []int {
	values := make([]int, 0, len(m.data))
	for _, v := range m.data {
		values = append(values, v)
	}
	return values
}

func (m *MapData) GetKey(value int) *string {
	for k, v := range m.data {
		if v == value {
			return &k
		}
	}
	fmt.Printf("ERROR: %d は範囲外です\n", value)
	return nil
}

func (m *MapData) GetValue(key string) (int, bool) {
	value, exists := m.data[key]
	if !exists {
		fmt.Printf("ERROR: %s は範囲外です\n", key)
		return 0, false
	}
	return value, true
}

func (m *MapData) Add(key string, value int) bool {
	if _, exists := m.data[key]; exists {
		fmt.Printf("ERROR: %s は重複です\n", key)
		return false
	}
	m.data[key] = value
	return true
}

func (m *MapData) Remove(key string) bool {
	if _, exists := m.data[key]; !exists {
		fmt.Printf("ERROR: %s は範囲外です\n", key)
		return false
	}
	delete(m.data, key)
	return true
}

func (m *MapData) Update(key string, value int) bool {
	if _, exists := m.data[key]; !exists {
		fmt.Printf("ERROR: %s は範囲外です\n", key)
		return false
	}
	m.data[key] = value
	return true
}

func (m *MapData) IsEmpty() bool {
	return len(m.data) == 0
}

func (m *MapData) Size() int {
	return len(m.data)
}

func (m *MapData) Clear() bool {
	m.data = make(map[string]int)
	return true
}

func main() {
	fmt.Println("Map TEST -----> start")

	fmt.Println("\nnew")
	mapData := NewMapData()
	fmt.Printf("  現在のデータ: %v\n", mapData.Get())

	fmt.Println("\nis_empty")
	output := mapData.IsEmpty()
	fmt.Printf("  出力値: %v\n", output)

	fmt.Println("\nsize")
	output2 := mapData.Size()
	fmt.Printf("  出力値: %d\n", output2)

	fmt.Println("\nadd")
	input := []interface{}{"apple", 100}
	output3 := mapData.Add(input[0].(string), input[1].(int))
	fmt.Printf("  入力値: %v\n", input)
	fmt.Printf("  出力値: %v\n", output3)
	fmt.Printf("  現在のデータ: %v\n", mapData.Get())

	fmt.Println("\nadd")
	input = []interface{}{"banana", 150}
	output3 = mapData.Add(input[0].(string), input[1].(int))
	fmt.Printf("  入力値: %v\n", input)
	fmt.Printf("  出力値: %v\n", output3)
	fmt.Printf("  現在のデータ: %v\n", mapData.Get())

	fmt.Println("\nadd")
	input = []interface{}{"apple", 200}
	output3 = mapData.Add(input[0].(string), input[1].(int))
	fmt.Printf("  入力値: %v\n", input)
	fmt.Printf("  出力値: %v\n", output3)
	fmt.Printf("  現在のデータ: %v\n", mapData.Get())

	fmt.Println("\nsize")
	output2 = mapData.Size()
	fmt.Printf("  出力値: %d\n", output2)

	fmt.Println("\nget")
	input2 := "apple"
	output4, _ := mapData.GetValue(input2)
	fmt.Printf("  入力値: %v\n", input2)
	fmt.Printf("  出力値: %d\n", output4)

	fmt.Println("\nget")
	input2 = "orange"
	output4, _ = mapData.GetValue(input2)
	fmt.Printf("  入力値: %v\n", input2)
	fmt.Printf("  出力値: %v\n", output4)

	fmt.Println("\nupdate")
	input = []interface{}{"banana", 180}
	output3 = mapData.Update(input[0].(string), input[1].(int))
	fmt.Printf("  入力値: %v\n", input)
	fmt.Printf("  出力値: %v\n", output3)
	fmt.Printf("  現在のデータ: %v\n", mapData.Get())

	fmt.Println("\nupdate")
	input = []interface{}{"orange", 250}
	output3 = mapData.Update(input[0].(string), input[1].(int))
	fmt.Printf("  入力値: %v\n", input)
	fmt.Printf("  出力値: %v\n", output3)
	fmt.Printf("  現在のデータ: %v\n", mapData.Get())

	fmt.Println("\nget")
	input2 = "banana"
	output4, _ = mapData.GetValue(input2)
	fmt.Printf("  出力値: %d\n", output4)

	fmt.Println("\nget_keys")
	output5 := mapData.GetKeys()
	fmt.Printf("  出力値: %v\n", output5)

	fmt.Println("\nvalues")
	output6 := mapData.GetValues()
	fmt.Printf("  出力値: %v\n", output6)

	fmt.Println("\nget_key")
	input3 := 180
	output7 := mapData.GetKey(input3)
	fmt.Printf("  入力値: %d\n", input3)
	fmt.Printf("  出力値: %v\n", output7)

	fmt.Println("\nget_key")
	input3 = 500
	output7 = mapData.GetKey(input3)
	fmt.Printf("  入力値: %d\n", input3)
	fmt.Printf("  出力値: %v\n", output7)

	fmt.Println("\nremove")
	input2 = "apple"
	output8 := mapData.Remove(input2)
	fmt.Printf("  入力値: %v\n", input2)
	fmt.Printf("  出力値: %v\n", output8)
	fmt.Printf("  現在のデータ: %v\n", mapData.Get())

	fmt.Println("\nremove")
	input2 = "orange"
	output8 = mapData.Remove(input2)
	fmt.Printf("  入力値: %v\n", input2)
	fmt.Printf("  出力値: %v\n", output8)
	fmt.Printf("  現在のデータ: %v\n", mapData.Get())

	fmt.Println("\nsize")
	output2 = mapData.Size()
	fmt.Printf("  出力値: %d\n", output2)

	fmt.Println("\nget_keys")
	output5 = mapData.GetKeys()
	fmt.Printf("  出力値: %v\n", output5)

	fmt.Println("\nclear")
	output9 := mapData.Clear()
	fmt.Printf("  出力値: %v\n", output9)
	fmt.Printf("  現在のデータ: %v\n", mapData.Get())

	fmt.Println("\nsize")
	output2 = mapData.Size()
	fmt.Printf("  出力値: %d\n", output2)

	fmt.Println("\nis_empty")
	output = mapData.IsEmpty()
	fmt.Printf("  出力値: %v\n", output)

	fmt.Println("\nMap TEST <----- end")
}
