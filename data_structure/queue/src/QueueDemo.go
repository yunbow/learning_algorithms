// Go
// データ構造: キュー (Queue)

package main

import (
	"fmt"
)

// QueueData はキューのデータ構造を表す
type QueueData struct {
	data []interface{}
}

// Get はキューの全データを取得する
func (q *QueueData) Get() []interface{} {
	return q.data
}

// GetIndex は指定した要素のインデックスを返す
func (q *QueueData) GetIndex(item interface{}) int {
	for i, v := range q.data {
		if v == item {
			return i
		}
	}
	fmt.Printf("ERROR: %v は範囲外です\n", item)
	return -1
}

// GetValue は指定したインデックスの要素を取得する
func (q *QueueData) GetValue(index int) interface{} {
	if 0 <= index && index < len(q.data) {
		return q.data[index]
	}
	fmt.Printf("Error: インデックス %d は範囲外です\n", index)
	return nil
}

// Enqueue はキューの末尾に要素を追加する
func (q *QueueData) Enqueue(item interface{}) bool {
	q.data = append(q.data, item)
	return true
}

// Dequeue はキューの先頭から要素を取り出す
func (q *QueueData) Dequeue() bool {
	if !q.IsEmpty() {
		q.data = q.data[1:]
		return true
	}
	fmt.Println("ERROR: キューが空です")
	return false
}

// Peek はキューの先頭要素を参照する
func (q *QueueData) Peek() interface{} {
	if !q.IsEmpty() {
		return q.data[0]
	}
	fmt.Println("ERROR: キューが空です")
	return nil
}

// IsEmpty はキューが空かどうかを確認する
func (q *QueueData) IsEmpty() bool {
	return len(q.data) == 0
}

// Size はキューの要素数を返す
func (q *QueueData) Size() int {
	return len(q.data)
}

// Clear はキューをクリアする
func (q *QueueData) Clear() bool {
	q.data = []interface{}{}
	return true
}

func main() {
	fmt.Println("Queue TEST -----> start")

	fmt.Println("\nnew")
	queueData := QueueData{}
	fmt.Printf("  現在のデータ: %v\n", queueData.Get())

	fmt.Println("\nis_empty")
	output := queueData.IsEmpty()
	fmt.Printf("  出力値: %v\n", output)

	fmt.Println("\nenqueue")
	input := []int{10, 20, 30}
	for _, item := range input {
		fmt.Printf("  入力値: %v\n", item)
		output := queueData.Enqueue(item)
		fmt.Printf("  出力値: %v\n", output)
		fmt.Printf("  現在のデータ: %v\n", queueData.Get())
	}

	fmt.Println("\nsize")
	output = queueData.Size()
	fmt.Printf("  出力値: %v\n", output)

	fmt.Println("\npeek")
	peekOutput := queueData.Peek()
	fmt.Printf("  出力値: %v\n", peekOutput)

	fmt.Println("\nget_index")
	indexInput := 20
	fmt.Printf("  入力値: %v\n", indexInput)
	indexOutput := queueData.GetIndex(indexInput)
	fmt.Printf("  出力値: %v\n", indexOutput)

	fmt.Println("\nget_index")
	indexInput = 50
	fmt.Printf("  入力値: %v\n", indexInput)
	indexOutput = queueData.GetIndex(indexInput)
	fmt.Printf("  出力値: %v\n", indexOutput)

	fmt.Println("\ndequeue")
	dequeueOutput := queueData.Dequeue()
	fmt.Printf("  出力値: %v\n", dequeueOutput)
	fmt.Printf("  現在のデータ: %v\n", queueData.Get())

	fmt.Println("\ndequeue")
	dequeueOutput = queueData.Dequeue()
	fmt.Printf("  出力値: %v\n", dequeueOutput)
	fmt.Printf("  現在のデータ: %v\n", queueData.Get())

	fmt.Println("\nsize")
	sizeOutput := queueData.Size()
	fmt.Printf("  出力値: %v\n", sizeOutput)

	fmt.Println("\ndequeue")
	dequeueOutput = queueData.Dequeue()
	fmt.Printf("  出力値: %v\n", dequeueOutput)
	fmt.Printf("  現在のデータ: %v\n", queueData.Get())

	fmt.Println("\ndequeue")
	dequeueOutput = queueData.Dequeue()
	fmt.Printf("  出力値: %v\n", dequeueOutput)
	fmt.Printf("  現在のデータ: %v\n", queueData.Get())

	fmt.Println("\nis_empty")
	emptyOutput := queueData.IsEmpty()
	fmt.Printf("  出力値: %v\n", emptyOutput)

	fmt.Println("\nclear")
	clearOutput := queueData.Clear()
	fmt.Printf("  出力値: %v\n", clearOutput)
	fmt.Printf("  現在のデータ: %v\n", queueData.Get())

	fmt.Println("\nsize")
	sizeOutput = queueData.Size()
	fmt.Printf("  出力値: %v\n", sizeOutput)

	fmt.Println("\nQueue TEST <----- end")
}
