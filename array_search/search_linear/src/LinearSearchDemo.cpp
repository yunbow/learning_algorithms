// C++
// 配列の検索: 線形探索 (Linear Search)

#include <iostream>
#include <vector>

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
        // 配列の要素を順番に確認
        for (size_t i = 0; i < _data.size(); i++) {
            // 目的の値が見つかった場合、そのインデックスを返す
            if (_data[i] == target) {
                return static_cast<int>(i);
            }
        }
        
        // 見つからなかった場合は -1 を返す
        return -1;
    }
};

void printVector(const std::vector<int>& vec) {
    std::cout << "  現在のデータ: [";
    for (size_t i = 0; i < vec.size(); i++) {
        std::cout << vec[i];
        if (i < vec.size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]" << std::endl;
}

int main() {
    std::cout << "LinearSearch TEST -----> start" << std::endl;

    std::cout << "\nnew" << std::endl;
    ArrayData array_data;
    std::vector<int> input = {1, 3, 5, 7, 9, 11, 13, 15, 17, 19};
    array_data.set(input);
    printVector(array_data.get());
    
    std::cout << "\nsearch" << std::endl;
    int searchValue = 7;
    std::cout << "  入力値: " << searchValue << std::endl;
    int output = array_data.search(searchValue);
    std::cout << "  出力値: " << output << std::endl;

    std::cout << "\nsearch" << std::endl;
    searchValue = 30;
    std::cout << "  入力値: " << searchValue << std::endl;
    output = array_data.search(searchValue);
    std::cout << "  出力値: " << output << std::endl;

    std::cout << "\nLinearSearch TEST <----- end" << std::endl;
    
    return 0;
}