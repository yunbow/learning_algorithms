// C#
// 配列の検索: ハッシュ探索 (Hash Search)

#include <iostream>
#include <vector>
#include <unordered_map>

class ArrayData {
private:
    std::vector<int> _data;

public:
    ArrayData() {}

    std::vector<int> get() {
        return _data;
    }

    bool set(const std::vector<int>& data) {
        _data = data;
        return true;
    }

    int search(int target) {
        // ハッシュテーブルの作成
        std::unordered_map<int, int> hash_table;
        
        // 配列の要素をハッシュテーブルに格納
        // キーを要素の値、値をインデックスとする
        for (int i = 0; i < _data.size(); i++) {
            hash_table[_data[i]] = i;
        }
        
        // ハッシュテーブルを使って検索
        auto it = hash_table.find(target);
        if (it != hash_table.end()) {
            return it->second;
        } else {
            return -1;
        }
    }
};

int main() {
    std::cout << "HashSearch TEST -----> start" << std::endl;

    std::cout << "\nnew" << std::endl;
    ArrayData array_data;
    std::vector<int> input = {1, 3, 5, 7, 9, 11, 13, 15, 17, 19};
    array_data.set(input);
    
    std::cout << "  現在のデータ: ";
    for (const auto& value : array_data.get()) {
        std::cout << value << " ";
    }
    std::cout << std::endl;

    std::cout << "\nsearch" << std::endl;
    int search_input = 7;
    std::cout << "  入力値: " << search_input << std::endl;
    int output = array_data.search(search_input);
    std::cout << "  出力値: " << output << std::endl;

    std::cout << "\nsearch" << std::endl;
    search_input = 30;
    std::cout << "  入力値: " << search_input << std::endl;
    output = array_data.search(search_input);
    std::cout << "  出力値: " << output << std::endl;

    std::cout << "\nHashSearch TEST <----- end" << std::endl;
    
    return 0;
}
