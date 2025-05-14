// Kotlin
// 素数判定: フェルマーテスト (Fermat Test)

import kotlin.random.Random

fun isPrime(target: Int, k: Int = 5): Boolean {
    if (target == 2 || target == 3) {
        return true
    }

    // 2未満または偶数は素数でない（2を除く）
    if (target < 2 || target % 2 == 0) {
        return false
    }

    repeat(k) {
        // 1 < a < target の範囲からランダムに数を選ぶ
        val a = Random.nextInt(2, target - 1)

        // フェルマーの小定理: a^(target-1) ≡ 1 (mod target) が成り立つか検証
        if (modPow(a.toBigInteger(), (target - 1).toBigInteger(), target.toBigInteger()) != 1.toBigInteger()) {
            return false
        }
    }

    return true
}

// modPow関数の実装（a^b mod m を計算）
fun modPow(base: java.math.BigInteger, exponent: java.math.BigInteger, modulus: java.math.BigInteger): java.math.BigInteger {
    return base.modPow(exponent, modulus)
}

fun main() {
    println("FermatTest TEST -----> start")

    println("\nisPrime")
    val inputList = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 97, 100, 101, 997)
    for (input in inputList) {
        val output = isPrime(input)
        println("  $input: $output")
    }

    println("\nFermatTest TEST <----- end")
}