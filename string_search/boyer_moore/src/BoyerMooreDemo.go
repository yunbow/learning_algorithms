package main

import (
	"fmt"
)

// search は与えられたテキスト内でパターンを検索し、出現位置のインデックスのスライスを返します
func search(text, pattern string) []int {
	if pattern == "" || text == "" || len(pattern) > len(text) {
		return []int{}
	}

	// 結果を格納するスライス
	occurrences := []int{}

	// 悪文字ルールのテーブル作成
	badChar := badCharacterTable(pattern)

	// 良接尾辞ルールのテーブル作成
	goodSuffix := goodSuffixTable(pattern)

	// 検索
	i := len(pattern) - 1 // テキスト内の位置
	for i < len(text) {
		j := len(pattern) - 1 // パターン内の位置
		for j >= 0 && text[i-len(pattern)+1+j] == pattern[j] {
			j--
		}

		if j < 0 { // マッチした場合
			occurrences = append(occurrences, i-len(pattern)+1)
			i++
		} else { // マッチしなかった場合
			// 悪文字ルールと良接尾辞ルールのシフト量の大きい方を採用
			bcShift := j + 1 // デフォルトのシフト量
			if idx, exists := badChar[text[i]]; exists {
				bcShift = max(1, j-idx)
			}
			gsShift := goodSuffix[j]
			i += max(bcShift, gsShift)
		}
	}

	return occurrences
}

// badCharacterTable はパターン内の各文字の最後の出現位置を記録したマップを返します
func badCharacterTable(pattern string) map[byte]int {
	badChar := make(map[byte]int)
	for i := 0; i < len(pattern); i++ {
		badChar[pattern[i]] = i
	}
	return badChar
}

// goodSuffixTable は良接尾辞ルールのシフト量テーブルを計算します
func goodSuffixTable(pattern string) []int {
	n := len(pattern)
	// ボーダー配列の計算
	border := make([]int, n+1)
	border[n] = n

	// パターンの逆順に対するZ配列の計算
	patternRev := reverse(pattern)
	z := zArray(patternRev)

	for i := 0; i < n; i++ {
		j := n - z[i]
		border[j] = i
	}

	// 良接尾辞ルールのシフト量計算
	shift := make([]int, n)
	for i := 0; i < n; i++ {
		shift[i] = n
	}

	for i := 0; i < n; i++ {
		j := n - border[i]
		shift[n-j-1] = j
	}

	// ボーダーが存在しない場合の処理
	for i := 0; i < n-1; i++ {
		j := n - 1 - i
		if border[j] == j {
			for k := 0; k < n-j; k++ {
				if shift[k] == n {
					shift[k] = n - j
				}
			}
		}
	}

	return shift
}

// zArray はZ配列を計算します
func zArray(pattern string) []int {
	n := len(pattern)
	z := make([]int, n)
	l, r := 0, 0
	for i := 1; i < n; i++ {
		if i <= r {
			z[i] = min(r-i+1, z[i-l])
		}
		for i+z[i] < n && pattern[z[i]] == pattern[i+z[i]] {
			z[i]++
		}
		if i+z[i]-1 > r {
			l, r = i, i+z[i]-1
		}
	}
	return z
}

// reverse は文字列を反転します
func reverse(s string) string {
	runes := []rune(s)
	for i, j := 0, len(runes)-1; i < j; i, j = i+1, j-1 {
		runes[i], runes[j] = runes[j], runes[i]
	}
	return string(runes)
}

// max は2つの整数の大きい方を返します
func max(a, b int) int {
	if a > b {
		return a
	}
	return b
}

// min は2つの整数の小さい方を返します
func min(a, b int) int {
	if a < b {
		return a
	}
	return b
}

func main() {
	fmt.Println("BoyerMooreSearch TEST -----> start")

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

	fmt.Println("\nBoyerMooreSearch TEST <----- end")
}