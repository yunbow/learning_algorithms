#include <iostream>
#include <vector>
#include <string>
#include <unordered_map>
#include <algorithm>

// 文字列の検索: ボイヤムーア法 (Boyer Moore Search)

std::vector<int> search(const std::string& text, const std::string& pattern) {
    if (pattern.empty() || text.empty() || pattern.length() > text.length()) {
        return {};
    }
    
    // 結果を格納するベクター
    std::vector<int> occurrences;
    
    // 悪文字ルールのテーブル作成
    std::unordered_map<char, int> bad_char;
    for (int i = 0; i < pattern.length(); i++) {
        bad_char[pattern[i]] = i;
    }
    
    // 良接尾辞ルールのテーブル作成
    // ボーダー配列の計算
    int n = pattern.length();
    std::vector<int> border(n + 1, 0);
    border[n] = n;
    
    // 補助関数：Z配列の計算
    auto z_array = [](const std::string& s) {
        int n = s.length();
        std::vector<int> z(n, 0);
        int l = 0, r = 0;
        for (int i = 1; i < n; i++) {
            if (i <= r) {
                z[i] = std::min(r - i + 1, z[i - l]);
            }
            while (i + z[i] < n && s[z[i]] == s[i + z[i]]) {
                z[i]++;
            }
            if (i + z[i] - 1 > r) {
                l = i;
                r = i + z[i] - 1;
            }
        }
        return z;
    };
    
    // パターンの逆順に対するZ配列
    std::string pattern_rev = pattern;
    std::reverse(pattern_rev.begin(), pattern_rev.end());
    std::vector<int> z = z_array(pattern_rev);
    
    for (int i = 0; i < n; i++) {
        int j = n - z[i];
        border[j] = i;
    }
    
    // 良接尾辞ルールのシフト量計算
    std::vector<int> shift(n, n);
    
    for (int i = 0; i < n; i++) {
        int j = n - border[i];
        shift[n - j - 1] = j;
    }
    
    // ボーダーが存在しない場合の処理
    for (int i = 0; i < n - 1; i++) {
        int j = n - 1 - i;
        if (border[j] == j) {
            for (int k = 0; k < n - j; k++) {
                if (shift[k] == n) {
                    shift[k] = n - j;
                }
            }
        }
    }
    
    // 検索
    int i = pattern.length() - 1;  // テキスト内の位置
    while (i < text.length()) {
        int j = pattern.length() - 1;  // パターン内の位置
        while (j >= 0 && text[i - pattern.length() + 1 + j] == pattern[j]) {
            j--;
        }
        
        if (j < 0) {  // マッチした場合
            occurrences.push_back(i - pattern.length() + 1);
            i += 1;
        } else {  // マッチしなかった場合
            // 悪文字ルールと良接尾辞ルールのシフト量の大きい方を採用
            int bc_shift = std::max(1, j - (bad_char.find(text[i]) != bad_char.end() ? bad_char[text[i]] : -1));
            int gs_shift = shift[j];
            i += std::max(bc_shift, gs_shift);
        }
    }
    
    return occurrences;
}

int main() {
    std::cout << "BoyerMooreSearch TEST -----> start" << std::endl;
    
    std::cout << "\nsearch" << std::endl;
    std::string text = "ABABCABCABABABD";
    std::string pattern = "ABABD";
    std::cout << "  入力値: (" << text << ", " << pattern << ")" << std::endl;
    std::vector<int> output = search(text, pattern);
    std::cout << "  出力値: [";
    for (size_t i = 0; i < output.size(); i++) {
        std::cout << output[i];
        if (i < output.size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]" << std::endl;
    
    std::cout << "\nsearch" << std::endl;
    text = "AAAAAA";
    pattern = "AA";
    std::cout << "  入力値: (" << text << ", " << pattern << ")" << std::endl;
    output = search(text, pattern);
    std::cout << "  出力値: [";
    for (size_t i = 0; i < output.size(); i++) {
        std::cout << output[i];
        if (i < output.size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]" << std::endl;
    
    std::cout << "\nsearch" << std::endl;
    text = "ABCDEFG";
    pattern = "XYZ";
    std::cout << "  入力値: (" << text << ", " << pattern << ")" << std::endl;
    output = search(text, pattern);
    std::cout << "  出力値: [";
    for (size_t i = 0; i < output.size(); i++) {
        std::cout << output[i];
        if (i < output.size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]" << std::endl;
    
    std::cout << "\nsearch" << std::endl;
    text = "ABCABC";
    pattern = "ABC";
    std::cout << "  入力値: (" << text << ", " << pattern << ")" << std::endl;
    output = search(text, pattern);
    std::cout << "  出力値: [";
    for (size_t i = 0; i < output.size(); i++) {
        std::cout << output[i];
        if (i < output.size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]" << std::endl;
    
    std::cout << "\nsearch" << std::endl;
    text = "ABC";
    pattern = "";
    std::cout << "  入力値: (" << text << ", " << pattern << ")" << std::endl;
    output = search(text, pattern);
    std::cout << "  出力値: [";
    for (size_t i = 0; i < output.size(); i++) {
        std::cout << output[i];
        if (i < output.size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]" << std::endl;
    
    std::cout << "\nBoyerMooreSearch TEST <----- end" << std::endl;
    
    return 0;
}