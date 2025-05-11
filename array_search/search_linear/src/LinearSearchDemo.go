// Go
// 配列の検索: 線形探索 (Linear Search)

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

func (a *ArrayData) Search(target int) int {
	// 配列の要素を順番に確認
	for i := 0; i < len(a.data); i++ {
		// 目的の値が見つかった場合、そのインデックスを返す
		if a.data[i] == target {
			return i
		}
	}
	
	// 見つからなかった場合は -1 を返す
	return -1
}

func main() {
	fmt.Println("LinearSearch TEST -----> start")

	fmt.Println("\nnew")
	arrayData := ArrayData{}
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

	fmt.Println("\nLinearSearch TEST <----- end")
}