// Swift
// 最大公約数: ユークリッドの互除法 (Euclidean)

func gcd(_ a: Int, _ b: Int) -> Int {
    // bが0になるまで繰り返し計算する
    var a = a
    var b = b
    while b != 0 {
        let temp = a
        a = b
        b = temp % b
    }
    return a
}

func main() {
    print("Euclidean TEST -----> start")
    let input = (48, 18)
    print("\n  入力値: \(input)")
    let output = gcd(input.0, input.1)
    print("\n  出力値: \(output)")
    print("\nEuclidean TEST <----- end")
}

main()
