#include <iostream>
#include <vector>
#include <string>

// 文字列の検索: KMP (Knuth Morris Pratt)

std::vector<int> compute_lps(const std::string& pattern) {
    int m = pattern.length();
    std::vector<int> lps(m, 0);  // LPSテーブルを初期化
    
    // 最初のインデックスのLPS値は常に0
    int length = 0;
    int i = 1;
    
    // LPSテーブルの残りの値を計算
    while (i < m) {
        if (pattern[i] == pattern[length]) {
            // 文字が一致する場合、lengthをインクリメントしてlps[i]に保存
            length++;
            lps[i] = length;
            i++;
        } else {
            // 文字が一致しない場合
            if (length != 0) {
                // 一致した部分文字列の前の位置のLPS値を使用
                length = lps[length - 1];
                // iはインクリメントしない
            } else {
                // lenght = 0の場合、lps[i] = 0として次に進む
                lps[i] = 0;
                i++;
            }
        }
    }
    
    return lps;
}

std::vector<int> search(const std::string& text, const std::string& pattern) {
    if (pattern.empty() || text.empty()) {
        return {};
    }
    
    int n = text.length();
    int m = pattern.length();
    
    // パターン長がテキスト長より大きい場合、マッチングは不可能
    if (m > n) {
        return {};
    }
    
    // LPSテーブルを計算
    std::vector<int> lps = compute_lps(pattern);
    
    std::vector<int> result;  // マッチした位置のリスト
    int i = 0;  // テキストのインデックス
    int j = 0;  // パターンのインデックス
    
    while (i < n) {
        // 現在の文字が一致する場合
        if (pattern[j] == text[i]) {
            i++;
            j++;
        }
        
        // パターン全体がマッチした場合
        if (j == m) {
            // パターンの開始位置をresultに追加
            result.push_back(i - j);
            // 次の可能性のある一致を探すために、jをLPS[j-1]に設定
            j = lps[j - 1];
        } 
        // 文字が一致しない場合
        else if (i < n && pattern[j] != text[i]) {
            // jが0でない場合、LPSテーブルを使って次の位置を決定
            if (j != 0) {
                j = lps[j - 1];
            } else {
                // jが0の場合、単純にテキストの次の文字に進む
                i++;
            }
        }
    }
    
    return result;
}

int main() {
    std::cout << "Kmp TEST -----> start" << std::endl;

    std::cout << "\nsearch" << std::endl;
    std::string text = "ABABCABCABABABD";
    std::string pattern = "ABABD";
    std::cout << "  入力値: (" << text << ", " << pattern << ")" << std::endl;
    std::vector<int> output = search(text, pattern);
    std::cout << "  出力値: [";
    for (size_t i = 0; i < output.size(); ++i) {
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
    for (size_t i = 0; i < output.size(); ++i) {
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
    for (size_t i = 0; i < output.size(); ++i) {
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
    for (size_t i = 0; i < output.size(); ++i) {
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
    for (size_t i = 0; i < output.size(); ++i) {
        std::cout << output[i];
        if (i < output.size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]" << std::endl;

    std::cout << "\nKmp TEST <----- end" << std::endl;
    
    return 0;
}