// Swift
// 素数判定: ミラーラビン (Miller-Rabin)

import Foundation

func isPrime(_ target: Int, k: Int = 40) -> Bool {
    // 特殊なケースを処理
    if target <= 1 {
        return false
    }
    if target <= 3 {
        return true
    }
    if target % 2 == 0 {
        return false
    }
    
    // n-1 = 2^r * d の形に分解する（dは奇数）
    var r = 0
    var d = target - 1
    while d % 2 == 0 {
        r += 1
        d /= 2
    }
    
    // k回の試行を実行
    for _ in 0..<k {
        let a = Int.random(in: 2...(target - 2))
        var x = Int(pow(Double(a), Double(d)).truncatingRemainder(dividingBy: Double(target)))
        
        if x == 1 || x == target - 1 {
            continue
        }
        
        var continueOuterLoop = false
        for _ in 0..<(r - 1) {
            x = Int(pow(Double(x), 2).truncatingRemainder(dividingBy: Double(target)))
            if x == target - 1 {
                continueOuterLoop = true
                break
            }
        }
        
        if continueOuterLoop {
            continue
        }
        
        return false  // 合成数と確定
    }
    
    return true  // 素数である可能性が高い
}

func main() {
    print("MillerRabin TEST -----> start")
    
    print("\nis_prime")
    let inputList = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 97, 100, 101, 997]
    for input in inputList {
        let output = isPrime(input)
        print("  \(input): \(output)")
    }
    
    print("\nMillerRabin TEST <----- end")
}

main()
