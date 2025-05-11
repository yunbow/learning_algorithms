// Go
// 配列の検索: ハッシュ探索 (Hash Search)

package main
import "fmt"

// ArrayData 配列データを管理する構造体
type ArrayData struct {
	data []int
}

// Get データを取得するメソッド
func (ad *ArrayData) Get() []int {
	return ad.data
}

// Set データをセットするメソッド
func (ad *ArrayData) Set(data []int) bool {
	ad.data = data
	return true
}

// Search ハッシュテーブルを使用して目標値を検索するメソッド
func (ad *ArrayData) Search(target int) int {
	// ハッシュテーブルの作成
	hashTable := make(map[int]int)

	// 配列の要素をハッシュテーブルに格納
	// キーを要素の値、値をインデックスとする
	for i, value := range ad.data {
		hashTable[value] = i
	}

	// ハッシュテーブルを使って検索
	if index, exists := hashTable[target]; exists {
		return index
	}
	return -1
}

func main() {
	fmt.Println("HashSearch TEST -----> start")

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

	fmt.Println("\nHashSearch TEST <----- end")
}