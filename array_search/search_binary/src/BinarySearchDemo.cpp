// C++
// 配列の検索: 二分探索 (Binary Search)

#include <iostream>
#include <vector>

class ArrayData {
private:
    std::vector<int> _data;

public:
    ArrayData() = default;

    std::vector<int> get() {
        return _data;
    }

    bool set(const std::vector<int>& data) {
        _data = data;
        return true;
    }

    int search(int target) {
        int left = 0;
        int right = _data.size() - 1;
        
        while (left <= right) {
            int mid = (left + right) / 2;
            
            // 中央の要素が目標値と一致
            if (_data[mid] == target) {
                return mid;
            }
            
            // 中央の要素が目標値より小さい場合、右半分を探索
            else if (_data[mid] < target) {
                left = mid + 1;
            }
            
            // 中央の要素が目標値より大きい場合、左半分を探索
            else {
                right = mid - 1;
            }
        }
        
        // 目標値が見つからない場合
        return -1;
    }
};

int main() {
    std::cout << "BinarySearch TEST -----> start" << std::endl;

    std::cout << "\nnew" << std::endl;
    ArrayData array_data;
    std::vector<int> input = {1, 3, 5, 7, 9, 11, 13, 15, 17, 19};
    array_data.set(input);
    
    std::cout << "  現在のデータ: ";
    for (size_t i = 0; i < array_data.get().size(); ++i) {
        std::cout << array_data.get()[i];
        if (i < array_data.get().size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << std::endl;
    
    std::cout << "\nsearch" << std::endl;
    int searchInput = 7;
    std::cout << "  入力値: " << searchInput << std::endl;
    int output = array_data.search(searchInput);
    std::cout << "  出力値: " << output << std::endl;

    std::cout << "\nsearch" << std::endl;
    searchInput = 30;
    std::cout << "  入力値: " << searchInput << std::endl;
    output = array_data.search(searchInput);
    std::cout << "  出力値: " << output << std::endl;

    std::cout << "\nBinarySearch TEST <----- end" << std::endl;

    return 0;
}