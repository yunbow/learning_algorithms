// C++
// 素数判定: フェルマーテスト (Fermat Test)

#include <iostream>
#include <vector>
#include <random>
#include <cmath>

// 高速な冪剰余計算 (a^b % m)
long long pow_mod(long long a, long long b, long long m) {
    long long result = 1;
    a %= m;
    while (b > 0) {
        if (b & 1) {
            result = (result * a) % m;
        }
        a = (a * a) % m;
        b >>= 1;
    }
    return result;
}

bool is_prime(int target, int k = 5) {
    if (target == 2 || target == 3) {
        return true;
    }

    // 2未満または偶数は素数でない（2を除く）
    if (target < 2 || target % 2 == 0) {
        return false;
    }

    std::random_device rd;
    std::mt19937 gen(rd());
    std::uniform_int_distribution<int> dist(2, target - 2);

    for (int i = 0; i < k; i++) {
        // 1 < a < target の範囲からランダムに数を選ぶ
        int a = dist(gen);

        // フェルマーの小定理: a^(target-1) ≡ 1 (mod target) が成り立つか検証
        if (pow_mod(a, target - 1, target) != 1) {
            return false;
        }
    }

    return true;
}

int main() {
    std::cout << "FermatTest TEST -----> start" << std::endl;

    std::cout << "\nis_prime" << std::endl;
    std::vector<int> input_list = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 97, 100, 101, 997};
    for (int input : input_list) {
        bool output = is_prime(input);
        std::cout << "  " << input << ": " << (output ? "true" : "false") << std::endl;
    }

    std::cout << "\nFermatTest TEST <----- end" << std::endl;

    return 0;
}