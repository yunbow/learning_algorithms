// C++
// 素数判定: ミラーラビン (Miller-Rabin)

#include <iostream>
#include <vector>
#include <random>
#include <cstdint>

// 素数判定: ミラーラビン (Miller-Rabin)
bool is_prime(int64_t target, int k = 40) {
    // 特殊なケースを処理
    if (target <= 1) {
        return false;
    }
    if (target <= 3) {
        return true;
    }
    if (target % 2 == 0) {
        return false;
    }
    
    // n-1 = 2^r * d の形に分解する（dは奇数）
    int r = 0;
    int64_t d = target - 1;
    while (d % 2 == 0) {
        r++;
        d /= 2;
    }
    
    // 乱数生成器を初期化
    std::random_device rd;
    std::mt19937_64 gen(rd());
    std::uniform_int_distribution<int64_t> dis(2, target - 2);
    
    // k回の試行を実行
    for (int i = 0; i < k; i++) {
        int64_t a = dis(gen);
        int64_t x = 1;
        
        // a^d mod targetを計算（繰り返し二乗法）
        int64_t base = a;
        int64_t exp = d;
        while (exp > 0) {
            if (exp % 2 == 1) {
                x = (x * base) % target;
            }
            base = (base * base) % target;
            exp /= 2;
        }
        
        if (x == 1 || x == target - 1) {
            continue;
        }
        
        bool composite = true;
        for (int j = 0; j < r - 1; j++) {
            x = (x * x) % target;
            if (x == target - 1) {
                composite = false;
                break;
            }
        }
        
        if (composite) {
            return false;  // 合成数と確定
        }
    }
    
    return true;  // 素数である可能性が高い
}

int main() {
    std::cout << "MillerRabin TEST -----> start" << std::endl;

    std::cout << "\nis_prime" << std::endl;
    std::vector<int> input_list = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 97, 100, 101, 997};
    
    for (const auto& input : input_list) {
        bool output = is_prime(input);
        std::cout << "  " << input << ": " << (output ? "true" : "false") << std::endl;
    }
    
    std::cout << "\nMillerRabin TEST <----- end" << std::endl;
    
    return 0;
}
