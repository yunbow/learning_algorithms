// Kotlin
// 最大公約数: ユークリッドの互除法 (Euclidean)

fun gcd(a: Int, b: Int): Int {
    // bが0になるまで繰り返し計算する
    var x = a
    var y = b
    while (y != 0) {
        val temp = y
        y = x % y
        x = temp
    }
    return x
}

fun main() {
    println("Euclidean TEST -----> start")
    val input = Pair(48, 18)
    println("\n  入力値: $input")
    val output = gcd(input.first, input.second)
    println("\n  出力値: $output")
    println("\nEuclidean TEST <----- end")
}