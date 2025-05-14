// Swift
// 素数判定: フェルマーテスト (Fermat Test)

import Foundation

func isPrime(_ target: Int, k: Int = 5) -> Bool {
    if target == 2 || target == 3 {
        return true
    }
    
    // 2未満または偶数は素数でない（2を除く）
    if target < 2 || target % 2 == 0 {
        return false
    }
    
    for _ in 0..<k {
        // 1 < a < target の範囲からランダムに数を選ぶ
        let a = Int.random(in: 2...(target - 2))
        
        // フェルマーの小定理: a^(target-1) ≡ 1 (mod target) が成り立つか検証
        if modPow(a, target - 1, target) != 1 {
            return false
        }
    }
    
    return true
}

// 効率的なべき乗剰余計算関数 (a^b mod m)
func modPow(_ base: Int, _ exponent: Int, _ modulus: Int) -> Int {
    // Swift のビルトイン関数として pow と組み合わせる代わりに
    // 繰り返し二乗法を実装
    var result = 1
    var base = base % modulus
    var exp = exponent
    
    while exp > 0 {
        if exp % 2 == 1 {
            result = (result * base) % modulus
        }
        base = (base * base) % modulus
        exp /= 2
    }
    
    return result
}

func main() {
    print("FermatTest TEST -----> start")
    
    print("\nisPrime")
    let inputList = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 97, 100, 101, 997]
    for input in inputList {
        let output = isPrime(input)
        print("  \(input): \(output)")
    }
    
    print("\nFermatTest TEST <----- end")
}

main()