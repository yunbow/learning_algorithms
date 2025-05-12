// Go
// データ構造: 連結リスト (Linked List)

package main

import (
	"fmt"
)

type NodeData struct {
	data interface{}
	next *NodeData
}

type LinkedListData struct {
	data *NodeData
	size int
}

func (n *NodeData) Get() interface{} {
	return n.data
}

func (n *NodeData) GetNext() *NodeData {
	return n.next
}

func (n *NodeData) SetData(data interface{}) {
	n.data = data
}

func (n *NodeData) SetNext(next *NodeData) {
	n.next = next
}

func NewLinkedListData() *LinkedListData {
	return &LinkedListData{
		data: nil,
		size: 0,
	}
}

func (l *LinkedListData) Get() *NodeData {
	return l.data
}

func (l *LinkedListData) GetPosition(data interface{}) int {
	if l.IsEmpty() {
		return -1
	}

	current := l.data
	position := 0

	for current != nil {
		if current.data == data {
			return position
		}
		current = current.next
		position++
	}

	return -1
}

func (l *LinkedListData) GetValue(position int) interface{} {
	if l.IsEmpty() || position < 0 || position >= l.size {
		fmt.Printf("ERROR: %d は範囲外です\n", position)
		return nil
	}

	current := l.data
	for i := 0; i < position; i++ {
		current = current.next
	}

	return current.data
}

func (l *LinkedListData) Add(data interface{}, position ...int) bool {
	newNode := &NodeData{data: data}

	if l.IsEmpty() {
		l.data = newNode
		l.size++
		return true
	}

	pos := -1
	if len(position) > 0 {
		pos = position[0]
	}

	if pos == -1 || pos >= l.size {
		current := l.data
		for current.next != nil {
			current = current.next
		}
		current.next = newNode
		l.size++
		return true
	}

	if pos == 0 {
		newNode.next = l.data
		l.data = newNode
		l.size++
		return true
	}

	current := l.data
	for i := 0; i < pos-1; i++ {
		current = current.next
	}

	newNode.next = current.next
	current.next = newNode
	l.size++
	return true
}

func (l *LinkedListData) Remove(args ...interface{}) bool {
	if l.IsEmpty() {
		fmt.Println("ERROR: リストが空です")
		return false
	}

	if len(args) > 0 {
		switch v := args[0].(type) {
		case int:
			return l.removeByPosition(v)
		default:
			return l.removeByData(v)
		}
	}

	return l.removeByPosition(l.size - 1)
}

func (l *LinkedListData) removeByData(data interface{}) bool {
	if l.data.data == data {
		l.data = l.data.next
		l.size--
		return true
	}

	current := l.data
	for current.next != nil && current.next.data != data {
		current = current.next
	}

	if current.next != nil {
		current.next = current.next.next
		l.size--
		return true
	}

	fmt.Printf("ERROR: %v は範囲外です\n", data)
	return false
}

func (l *LinkedListData) removeByPosition(position int) bool {
	if position < 0 || position >= l.size {
		fmt.Printf("ERROR: %d は範囲外です\n", position)
		return false
	}

	if position == 0 {
		l.data = l.data.next
		l.size--
		return true
	}

	current := l.data
	for i := 0; i < position-1; i++ {
		current = current.next
	}

	current.next = current.next.next
	l.size--
	return true
}

func (l *LinkedListData) Update(position int, data interface{}) bool {
	if l.IsEmpty() || position < 0 || position >= l.size {
		fmt.Printf("ERROR: %d は範囲外です\n", position)
		return false
	}

	current := l.data
	for i := 0; i < position; i++ {
		current = current.next
	}

	current.data = data
	return true
}

func (l *LinkedListData) IsEmpty() bool {
	return l.data == nil
}

func (l *LinkedListData) Size() int {
	return l.size
}

func (l *LinkedListData) Clear() bool {
	l.data = nil
	l.size = 0
	return true
}

func (l *LinkedListData) Display() []interface{} {
	elements := []interface{}{}
	current := l.data
	for current != nil {
		elements = append(elements, current.data)
		current = current.next
	}
	return elements
}

func main() {
	fmt.Println("LinkedList TEST -----> start")

	fmt.Println("\nnew")
	linkedListData := NewLinkedListData()
	fmt.Printf("  現在のデータ: %v\n", linkedListData.Display())

	fmt.Println("\nis_empty")
	output := linkedListData.IsEmpty()
	fmt.Printf("  出力値: %v\n", output)

	fmt.Println("\nsize")
	sizeOutput := linkedListData.Size()
	fmt.Printf("  出力値: %d\n", sizeOutput)

	fmt.Println("\nadd")
	input := 10
	fmt.Printf("  入力値: %d\n", input)
	addOutput := linkedListData.Add(input)
	fmt.Printf("  出力値: %v\n", addOutput)
	fmt.Printf("  現在のデータ: %v\n", linkedListData.Display())

	fmt.Println("\nadd")
	input = 20
	fmt.Printf("  入力値: %d\n", input)
	addOutput = linkedListData.Add(input)
	fmt.Printf("  出力値: %v\n", addOutput)
	fmt.Printf("  現在のデータ: %v\n", linkedListData.Display())

	fmt.Println("\nadd")
	input = 5
	inputPos := 0
	fmt.Printf("  入力値: (%d, %d)\n", input, inputPos)
	addOutput = linkedListData.Add(input, inputPos)
	fmt.Printf("  出力値: %v\n", addOutput)
	fmt.Printf("  現在のデータ: %v\n", linkedListData.Display())

	fmt.Println("\nadd")
	input = 15
	inputPos = 2
	fmt.Printf("  入力値: (%d, %d)\n", input, inputPos)
	addOutput = linkedListData.Add(input, inputPos)
	fmt.Printf("  出力値: %v\n", addOutput)
	fmt.Printf("  現在のデータ: %v\n", linkedListData.Display())

	fmt.Println("\nget_value")
	input = 1
	fmt.Printf("  入力値: %d\n", input)
	outputPos := linkedListData.GetPosition(input)
	fmt.Printf("  出力値: %d\n", outputPos)

	fmt.Println("\nget_value")
	input = 10
	fmt.Printf("  入力値: %d\n", input)
	outputPos = linkedListData.GetPosition(input)
	fmt.Printf("  出力値: %d\n", outputPos)

	fmt.Println("\nupdate")
	inputPos = 1
	input = 99
	fmt.Printf("  入力値: (%d, %d)\n", inputPos, input)
	updateOutput := linkedListData.Update(inputPos, input)
	fmt.Printf("  出力値: %v\n", updateOutput)
	fmt.Printf("  現在のデータ: %v\n", linkedListData.Display())

	fmt.Println("\nget_value")
	input = 15
	fmt.Printf("  入力値: %d\n", input)
	getValueOutput := linkedListData.GetValue(input)
	fmt.Printf("  出力値: %v\n", getValueOutput)

	fmt.Println("\nget_valuefind")
	input = 100
	fmt.Printf("  入力値: %d\n", input)
	getValueOutput = linkedListData.GetValue(input)
	fmt.Printf("  出力値: %v\n", getValueOutput)

	fmt.Println("\nremove")
	input = 15
	fmt.Printf("  入力値: data=%d\n", input)
	removeOutput := linkedListData.Remove(input)
	fmt.Printf("  出力値: %v\n", removeOutput)
	fmt.Printf("  現在のデータ: %v\n", linkedListData.Display())

	fmt.Println("\nremove")
	inputPos = 0
	fmt.Printf("  入力値: position=%d\n", inputPos)
	removeOutput = linkedListData.Remove(inputPos)
	fmt.Printf("  出力値: %v\n", removeOutput)
	fmt.Printf("  現在のデータ: %v\n", linkedListData.Display())

	fmt.Println("\nremove")
	removeOutput = linkedListData.Remove()
	fmt.Printf("  出力値: %v\n", removeOutput)
	fmt.Printf("  現在のデータ: %v\n", linkedListData.Display())

	fmt.Println("\nremove")
	inputPos = 5
	fmt.Printf("  入力値: position=%d\n", inputPos)
	removeOutput = linkedListData.Remove(inputPos)
	fmt.Printf("  出力値: %v\n", removeOutput)
	fmt.Printf("  現在のデータ: %v\n", linkedListData.Display())

	fmt.Println("\nclear")
	clearOutput := linkedListData.Clear()
	fmt.Printf("  出力値: %v\n", clearOutput)
	fmt.Printf("  現在のデータ: %v\n", linkedListData.Display())

	fmt.Println("\nis_empty")
	output = linkedListData.IsEmpty()
	fmt.Printf("  出力値: %v\n", output)

	fmt.Println("\nsize")
	sizeOutput = linkedListData.Size()
	fmt.Printf("出力値: %d\n", sizeOutput)

	fmt.Println("\nremove")
	removeOutput = linkedListData.Remove()
	fmt.Printf("  出力値: %v\n", removeOutput)
	fmt.Printf("  現在のデータ: %v\n", linkedListData.Display())

	fmt.Println("\nLinkedList TEST <----- end")
}
