// Kotlin
// 素数判定: ミラーラビン (Miller-Rabin)

import kotlin.random.Random

fun isPrime(target: Long, k: Int = 40): Boolean {
    // 特殊なケースを処理
    if (target <= 1) {
        return false
    }
    if (target <= 3) {
        return true
    }
    if (target % 2 == 0L) {
        return false
    }
    
    // n-1 = 2^r * d の形に分解する（dは奇数）
    var r = 0
    var d = target - 1
    while (d % 2 == 0L) {
        r++
        d /= 2
    }
    
    // k回の試行を実行
    repeat(k) {
        val a = Random.nextLong(2, target - 1)
        var x = modPow(a, d, target)  // a^d mod n を計算
        
        if (x == 1L || x == target - 1) {
            return@repeat
        }
        
        for (j in 0 until r - 1) {
            x = modPow(x, 2, target)
            if (x == target - 1) {
                return@repeat
            }
        }
        
        return false  // 合成数と確定
    }
    
    return true  // 素数である可能性が高い
}

// モジュラーべき乗計算（a^b mod m）
fun modPow(base: Long, exponent: Long, modulus: Long): Long {
    if (modulus == 1L) return 0
    
    var result = 1L
    var b = base % modulus
    var e = exponent
    
    while (e > 0) {
        if (e % 2 == 1L) {
            result = (result * b) % modulus
        }
        e = e shr 1
        b = (b * b) % modulus
    }
    
    return result
}

fun main() {
    println("MillerRabin TEST -----> start")

    println("\nis_prime")
    val inputList = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 97, 100, 101, 997)
    for (input in inputList) {
        val output = isPrime(input.toLong())
        println("  $input: $output")
    }
    
    println("\nMillerRabin TEST <----- end")
}