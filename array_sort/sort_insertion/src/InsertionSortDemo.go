// Go
// 配列の並び替え: 挿入ソート (Insertion Sort)

package main
import "fmt"

type ArrayData struct {
	data []int
}

// Get は内部データを返す
func (a *ArrayData) Get() []int {
	return a.data
}

// Set は内部データを設定する
func (a *ArrayData) Set(data []int) bool {
	a.data = data
	return true
}

// Sort は挿入ソートを実行する
func (a *ArrayData) Sort() bool {
	// 配列の長さを取得
	n := len(a.data)

	// 2番目の要素から始める（最初の要素は既にソート済みと見なす）
	for i := 1; i < n; i++ {
		// 現在の要素を取得
		key := a.data[i]

		// ソート済み部分の最後の要素のインデックス
		j := i - 1

		// keyより大きい要素をすべて右にシフト
		for j >= 0 && a.data[j] > key {
			a.data[j+1] = a.data[j]
			j--
		}

		// 適切な位置にkeyを挿入
		a.data[j+1] = key
	}

	return true
}

func main() {
	fmt.Println("InsertionSort TEST -----> start")

	arrayData := ArrayData{}

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

	fmt.Println("\nInsertionSort TEST <----- end")
}