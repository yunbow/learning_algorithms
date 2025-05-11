#include <iostream>
#include <string>
#include <vector>

// 文字列の検索: ブルートフォース法 (Brute Force Search)
std::vector<int> search(const std::string& text, const std::string& pattern) {
    int n = text.length();
    int m = pattern.length();
    std::vector<int> positions;
    
    // パターンが空または検索対象より長い場合
    if (m == 0 || m > n) {
        return positions;
    }
    
    // テキスト内の各位置でパターンとの一致を確認
    for (int i = 0; i <= n - m; ++i) {
        int j = 0;
        // パターンの各文字を確認
        while (j < m && text[i + j] == pattern[j]) {
            j++;
        }
        // パターンが完全に一致した場合
        if (j == m) {
            positions.push_back(i);
        }
    }
    
    return positions;
}

void printResults(const std::vector<int>& results) {
    std::cout << "  出力値: [";
    for (size_t i = 0; i < results.size(); ++i) {
        std::cout << results[i];
        if (i < results.size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]" << std::endl;
}

int main() {
    std::cout << "BruteForceSearch TEST -----> start" << std::endl;

    std::cout << "\nsearch" << std::endl;
    std::string text1 = "ABABCABCABABABD";
    std::string pattern1 = "ABABD";
    std::cout << "  入力値: (" << text1 << ", " << pattern1 << ")" << std::endl;
    auto output1 = search(text1, pattern1);
    printResults(output1);

    std::cout << "\nsearch" << std::endl;
    std::string text2 = "AAAAAA";
    std::string pattern2 = "AA";
    std::cout << "  入力値: (" << text2 << ", " << pattern2 << ")" << std::endl;
    auto output2 = search(text2, pattern2);
    printResults(output2);

    std::cout << "\nsearch" << std::endl;
    std::string text3 = "ABCDEFG";
    std::string pattern3 = "XYZ";
    std::cout << "  入力値: (" << text3 << ", " << pattern3 << ")" << std::endl;
    auto output3 = search(text3, pattern3);
    printResults(output3);

    std::cout << "\nsearch" << std::endl;
    std::string text4 = "ABCABC";
    std::string pattern4 = "ABC";
    std::cout << "  入力値: (" << text4 << ", " << pattern4 << ")" << std::endl;
    auto output4 = search(text4, pattern4);
    printResults(output4);

    std::cout << "\nsearch" << std::endl;
    std::string text5 = "ABC";
    std::string pattern5 = "";
    std::cout << "  入力値: (" << text5 << ", " << pattern5 << ")" << std::endl;
    auto output5 = search(text5, pattern5);
    printResults(output5);

    std::cout << "\nBruteForceSearch TEST <----- end" << std::endl;
    
    return 0;
}