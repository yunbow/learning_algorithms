// Rust
// 素数判定: フェルマーテスト (Fermat Test)

use rand::Rng;

fn is_prime(target: u64, k: u32) -> bool {
    if target == 2 || target == 3 {
        return true;
    }

    // 2未満または偶数は素数でない（2を除く）
    if target < 2 || target % 2 == 0 {
        return false;
    }

    let mut rng = rand::thread_rng();
    
    for _ in 0..k {
        // 1 < a < target の範囲からランダムに数を選ぶ
        let a = rng.gen_range(2..=target - 2);

        // フェルマーの小定理: a^(target-1) ≡ 1 (mod target) が成り立つか検証
        if mod_pow(a, target - 1, target) != 1 {
            return false;
        }
    }

    true
}

// 高速冪剰余算
fn mod_pow(base: u64, exponent: u64, modulus: u64) -> u64 {
    if modulus == 1 {
        return 0;
    }
    
    let mut result = 1;
    let mut base = base % modulus;
    let mut exp = exponent;
    
    while exp > 0 {
        if exp % 2 == 1 {
            result = (result * base) % modulus;
        }
        exp >>= 1;
        base = (base * base) % modulus;
    }
    
    result
}

fn main() {
    println!("FermatTest TEST -----> start");

    println!("\nis_prime");
    let input_list = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 97, 100, 101, 997];
    for &input in &input_list {
        let output = is_prime(input, 5);
        println!("  {}: {}", input, output);
    }

    println!("\nFermatTest TEST <----- end");
}
