// Rust
// 素数判定: ミラーラビン (Miller-Rabin)

use rand::Rng;

fn is_prime(target: u64, k: u32) -> bool {
    // 特殊なケースを処理
    if target <= 1 {
        return false;
    }
    if target <= 3 {
        return true;
    }
    if target % 2 == 0 {
        return false;
    }
    
    // n-1 = 2^r * d の形に分解する（dは奇数）
    let mut r = 0;
    let mut d = target - 1;
    while d % 2 == 0 {
        r += 1;
        d /= 2;
    }
    
    // k回の試行を実行
    let mut rng = rand::thread_rng();
    for _ in 0..k {
        let a = rng.gen_range(2..target-1);
        let mut x = mod_pow(a, d, target);
        
        if x == 1 || x == target - 1 {
            continue;
        }
        
        let mut i = 0;
        while i < r - 1 {
            x = mod_pow(x, 2, target);
            if x == target - 1 {
                break;
            }
            i += 1;
        }
        
        if i == r - 1 {
            return false;  // 合成数と確定
        }
    }
    
    return true;  // 素数である可能性が高い
}

// モジュラーべき乗の計算（Rust 1.69.0以降ではu64::pow_modが使えるが、互換性のために実装）
fn mod_pow(mut base: u64, mut exp: u64, modulus: u64) -> u64 {
    if modulus == 1 {
        return 0;
    }
    
    let mut result = 1;
    base %= modulus;
    
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
    println!("MillerRabin TEST -----> start");

    println!("\nis_prime");
    let input_list = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 97, 100, 101, 997];
    for &input in &input_list {
        let output = is_prime(input, 40);
        println!("  {}: {}", input, output);
    }
    
    println!("\nMillerRabin TEST <----- end");
}
