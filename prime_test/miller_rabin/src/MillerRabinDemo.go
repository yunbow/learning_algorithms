// Go
// 素数判定: ミラーラビン (Miller-Rabin)

package main

import (
	"fmt"
	"math/big"
	"math/rand"
	"time"
)

func isPrime(target int, k int) bool {
	// 特殊なケースを処理
	if target <= 1 {
		return false
	}
	if target <= 3 {
		return true
	}
	if target%2 == 0 {
		return false
	}

	// n-1 = 2^r * d の形に分解する（dは奇数）
	r, d := 0, target-1
	for d%2 == 0 {
		r++
		d /= 2
	}

	// 乱数生成器を初期化
	rnd := rand.New(rand.NewSource(time.Now().UnixNano()))

	// k回の試行を実行
	targetBig := big.NewInt(int64(target))
	for i := 0; i < k; i++ {
		// 2からtarget-2の範囲でランダムな数値aを選択
		a := big.NewInt(0)
		a.Rand(rnd, big.NewInt(int64(target-3)))
		a.Add(a, big.NewInt(2))

		x := big.NewInt(0)
		x.Exp(a, big.NewInt(int64(d)), targetBig) // a^d mod n を計算

		if x.Cmp(big.NewInt(1)) == 0 || x.Cmp(big.NewInt(int64(target-1))) == 0 {
			continue
		}

		testPassed := false
		for j := 0; j < r-1; j++ {
			// x = x^2 mod target
			x.Exp(x, big.NewInt(2), targetBig)
			if x.Cmp(big.NewInt(int64(target-1))) == 0 {
				testPassed = true
				break
			}
		}

		if !testPassed {
			return false // 合成数と確定
		}
	}

	return true // 素数である可能性が高い
}

func main() {
	fmt.Println("MillerRabin TEST -----> start")

	fmt.Println("\nis_prime")
	inputList := []int{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 97, 100, 101, 997}
	for _, input := range inputList {
		output := isPrime(input, 40)
		fmt.Printf("  %d: %t\n", input, output)
	}

	fmt.Println("\nMillerRabin TEST <----- end")
}
