// Rust
// 最大公約数: ユークリッドの互除法 (Euclidean)

fn gcd(mut a: u32, mut b: u32) -> u32 {
    // bが0になるまで繰り返し計算する
    while b != 0 {
        let temp = b;
        b = a % b;
        a = temp;
    }
    a
}

fn main() {
    println!("Euclidean TEST -----> start");
    let input = (48, 18);
    println!("\n  入力値: {:?}", input);
    let output = gcd(input.0, input.1);
    println!("\n  出力値: {}", output);
    println!("\nEuclidean TEST <----- end");
}
