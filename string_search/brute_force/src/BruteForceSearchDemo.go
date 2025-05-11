package main

import "fmt"

// search はテキスト内でパターンが出現する位置のスライスを返す
func search(text, pattern string) []int {
	n := len(text)
	m := len(pattern)
	positions := []int{}

	// パターンが空または検索対象より長い場合
	if m == 0 || m > n {
		return positions
	}

	// テキスト内の各位置でパターンとの一致を確認
	for i := 0; i <= n-m; i++ {
		j := 0
		// パターンの各文字を確認
		for j < m && text[i+j] == pattern[j] {
			j++
		}
		// パターンが完全に一致した場合
		if j == m {
			positions = append(positions, i)
		}
	}

	return positions
}

func main() {
	fmt.Println("BruteForceSearch TEST -----> start")

	fmt.Println("\nsearch")
	input := []string{"ABABCABCABABABD", "ABABD"}
	fmt.Printf("  入力値: %v\n", input)
	output := search(input[0], input[1])
	fmt.Printf("  出力値: %v\n", output)

	fmt.Println("\nsearch")
	input = []string{"AAAAAA", "AA"}
	fmt.Printf("  入力値: %v\n", input)
	output = search(input[0], input[1])
	fmt.Printf("  出力値: %v\n", output)

	fmt.Println("\nsearch")
	input = []string{"ABCDEFG", "XYZ"}
	fmt.Printf("  入力値: %v\n", input)
	output = search(input[0], input[1])
	fmt.Printf("  出力値: %v\n", output)

	fmt.Println("\nsearch")
	input = []string{"ABCABC", "ABC"}
	fmt.Printf("  入力値: %v\n", input)
	output = search(input[0], input[1])
	fmt.Printf("  出力値: %v\n", output)

	fmt.Println("\nsearch")
	input = []string{"ABC", ""}
	fmt.Printf("  入力値: %v\n", input)
	output = search(input[0], input[1])
	fmt.Printf("  出力値: %v\n", output)

	fmt.Println("\nBruteForceSearch TEST <----- end")
}