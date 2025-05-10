// C++
// 最大公約数: ユークリッドの互除法 (Euclidean)

#include <iostream>
#include <utility>

int gcd(int a, int b) {
    // bが0になるまで繰り返し計算する
    while (b) {
        std::tie(a, b) = std::make_pair(b, a % b);
    }
    return a;
}

int main() {
    std::cout << "Euclidean TEST -----> start" << std::endl;
    
    std::pair<int, int> input = {48, 18};
    std::cout << "\n  入力値: (" << input.first << ", " << input.second << ")" << std::endl;
    
    int output = gcd(input.first, input.second);
    std::cout << "\n  出力値: " << output << std::endl;
    
    std::cout << "\nEuclidean TEST <----- end" << std::endl;
    
    return 0;
}