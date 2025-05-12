// Go
// データ構造: スタック (Stack)

package main

import (
	"fmt"
)

type StackData struct {
	data []int
}

func (s *StackData) Get() []int {
	return s.data
}

func (s *StackData) GetIndex(item int) int {
	for i, v := range s.data {
		if v == item {
			return i
		}
	}
	fmt.Printf("ERROR: %d は範囲外です\n", item)
	return -1
}

func (s *StackData) GetValue(index int) *int {
	if 0 <= index && index < len(s.data) {
		return &s.data[index]
	}
	fmt.Printf("ERROR: %d は範囲外です\n", index)
	return nil
}

func (s *StackData) Push(item int) bool {
	s.data = append(s.data, item)
	return true
}

func (s *StackData) Pop() bool {
	if !s.IsEmpty() {
		s.data = s.data[:len(s.data)-1]
		return true
	}
	fmt.Println("ERROR: 空です")
	return false
}

func (s *StackData) Peek() *int {
	if !s.IsEmpty() {
		return &s.data[len(s.data)-1]
	}
	return nil
}

func (s *StackData) IsEmpty() bool {
	return len(s.data) == 0
}

func (s *StackData) Size() int {
	return len(s.data)
}

func (s *StackData) Clear() bool {
	s.data = []int{}
	return true
}

func main() {
	fmt.Println("Stack TEST -----> start")

	fmt.Println("\nnew")
	stackData := StackData{}
	fmt.Printf("  現在のデータ: %v\n", stackData.Get())

	fmt.Println("\nis_empty")
	output := stackData.IsEmpty()
	fmt.Printf("  出力値: %v\n", output)

	fmt.Println("\nsize")
	output2 := stackData.Size()
	fmt.Printf("  出力値: %v\n", output2)

	fmt.Println("\npush")
	itemsToPush := []int{10, 20, 30, 40}
	for _, item := range itemsToPush {
		fmt.Printf("  入力値: %v\n", item)
		output3 := stackData.Push(item)
		fmt.Printf("  出力値: %v\n", output3)
		fmt.Printf("  現在のデータ: %v\n", stackData.Get())
	}

	fmt.Println("\nsize")
	output4 := stackData.Size()
	fmt.Printf("  出力値: %v\n", output4)

	fmt.Println("\nis_empty")
	output5 := stackData.IsEmpty()
	fmt.Printf("  出力値: %v\n", output5)

	fmt.Println("\npeek")
	output6 := stackData.Peek()
	if output6 != nil {
		fmt.Printf("  出力値: %v\n", *output6)
	} else {
		fmt.Printf("  出力値: %v\n", output6)
	}

	fmt.Println("\nget_index")
	input1 := 30
	fmt.Printf("  入力値: %v\n", input1)
	output7 := stackData.GetIndex(input1)
	fmt.Printf("  出力値: %v\n", output7)

	fmt.Println("\nget_index")
	input2 := 50
	fmt.Printf("  入力値: %v\n", input2)
	output8 := stackData.GetIndex(input2)
	fmt.Printf("  出力値: %v\n", output8)

	fmt.Println("\npop")
	for !stackData.IsEmpty() {
		output9 := stackData.Pop()
		fmt.Printf("  出力値: %v\n", output9)
		fmt.Printf("  現在のデータ: %v\n", stackData.Get())
	}

	fmt.Println("\nis_empty")
	output10 := stackData.IsEmpty()
	fmt.Printf("  出力値: %v\n", output10)

	fmt.Println("\nsize")
	output11 := stackData.Size()
	fmt.Printf("  出力値: %v\n", output11)

	fmt.Println("\npop")
	output12 := stackData.Pop()
	fmt.Printf("  出力値: %v\n", output12)

	fmt.Println("\npeek")
	output13 := stackData.Peek()
	if output13 != nil {
		fmt.Printf("  出力値: %v\n", *output13)
	} else {
		fmt.Printf("  出力値: %v\n", output13)
	}

	fmt.Println("\nStack TEST <----- end")
}