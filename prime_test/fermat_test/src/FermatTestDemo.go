// Go
// 素数判定: フェルマーテスト (Fermat Test)

package main

import (
	"fmt"
	"math/big"
	"math/rand"
	"time"
)

// is_prime はフェルマーテストを使って素数判定を行います
func is_prime(target int, k int) bool {
	if target == 2 || target == 3 {
		return true
	}

	// 2未満または偶数は素数でない（2を除く）
	if target < 2 || target%2 == 0 {
		return false
	}

	// big.Int型で計算を行うための準備
	targetBig := big.NewInt(int64(target))
	one := big.NewInt(1)
	targetMinusOne := new(big.Int).Sub(targetBig, one)

	// 乱数生成器の初期化
	r := rand.New(rand.NewSource(time.Now().UnixNano()))

	for i := 0; i < k; i++ {
		// 1 < a < target の範囲からランダムに数を選ぶ
		a := new(big.Int)
		// 2 ≤ a ≤ target-2 の範囲で乱数を生成
		a.Rand(r, new(big.Int).Sub(targetBig, big.NewInt(3)))
		a.Add(a, big.NewInt(2))

		// フェルマーの小定理: a^(target-1) ≡ 1 (mod target) が成り立つか検証
		result := new(big.Int).Exp(a, targetMinusOne, targetBig)
		if result.Cmp(one) != 0 {
			return false
		}
	}

	return true
}

func main() {
	fmt.Println("FermatTest TEST -----> start")

	fmt.Println("\nis_prime")
	inputList := []int{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 97, 100, 101, 997}
	for _, input := range inputList {
		output := is_prime(input, 5) // デフォルトkは5
		fmt.Printf("  %d: %t\n", input, output)
	}

	fmt.Println("\nFermatTest TEST <----- end")
}