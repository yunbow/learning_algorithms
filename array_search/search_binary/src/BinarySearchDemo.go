// Go
// 配列の検索: 二分探索 (Binary Search)

package main
import "fmt"

// ArrayData 配列のデータを扱う構造体
type ArrayData struct {
	data []int
}

// Get データを取得するメソッド
func (a *ArrayData) Get() []int {
	return a.data
}

// Set データをセットするメソッド
func (a *ArrayData) Set(data []int) bool {
	a.data = data
	return true
}

// Search 二分探索で値を検索するメソッド
func (a *ArrayData) Search(target int) int {
	left := 0
	right := len(a.data) - 1

	for left <= right {
		mid := (left + right) / 2

		// 中央の要素が目標値と一致
		if a.data[mid] == target {
			return mid
		}

		// 中央の要素が目標値より小さい場合、右半分を探索
		if a.data[mid] < target {
			left = mid + 1
		} else {
			// 中央の要素が目標値より大きい場合、左半分を探索
			right = mid - 1
		}
	}

	// 目標値が見つからない場合
	return -1
}

func main() {
	fmt.Println("BinarySearch TEST -----> start")

	fmt.Println("\nnew")
	arrayData := &ArrayData{}
	input := []int{1, 3, 5, 7, 9, 11, 13, 15, 17, 19}
	arrayData.Set(input)
	fmt.Printf("  現在のデータ: %v\n", arrayData.Get())

	fmt.Println("\nsearch")
	searchInput := 7
	fmt.Printf("  入力値: %d\n", searchInput)
	output := arrayData.Search(searchInput)
	fmt.Printf("  出力値: %d\n", output)

	fmt.Println("\nsearch")
	searchInput = 30
	fmt.Printf("  入力値: %d\n", searchInput)
	output = arrayData.Search(searchInput)
	fmt.Printf("  出力値: %d\n", output)

	fmt.Println("\nBinarySearch TEST <----- end")
}