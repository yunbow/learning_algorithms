// Go
// 最大公約数: ユークリッドの互除法 (Euclidean)

package main

import "fmt"

func gcd(a, b int) int {
	// bが0になるまで繰り返し計算する
	for b != 0 {
		a, b = b, a%b
	}
	return a
}

func main() {
	fmt.Println("Euclidean TEST -----> start")
	input := [2]int{48, 18}
	fmt.Printf("\n  入力値: %v\n", input)
	output := gcd(input[0], input[1])
	fmt.Printf("\n  出力値: %d\n", output)
	fmt.Println("\nEuclidean TEST <----- end")
}
