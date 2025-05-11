// C++
// 配列の並び替え: バブルソート (Bubble Sort)

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

    bool sort() {
        int n = _data.size();
        
        // 外側のループ: n-1回の走査が必要
        for (int i = 0; i < n; i++) {
            // 最適化: 一度の走査で交換がなければソート完了
            bool swapped = false;
            
            // 内側のループ: まだソートされていない部分を走査
            // 各走査後に最大の要素が末尾に移動するため、i回分を除外
            for (int j = 0; j < n - i - 1; j++) {
                // 隣接する要素を比較し、必要に応じて交換
                if (_data[j] > _data[j + 1]) {
                    std::swap(_data[j], _data[j + 1]);
                    swapped = true;
                }
            }
            
            // 交換が発生しなければソート完了
            if (!swapped) {
                break;
            }
        }
        return true;
    }
};

// 配列表示用のヘルパー関数
void printVector(const std::vector<int>& vec) {
    std::cout << "  [";
    for (size_t i = 0; i < vec.size(); i++) {
        std::cout << vec[i];
        if (i < vec.size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]" << std::endl;
}

int main() {
    std::cout << "BubbleSort TEST -----> start" << std::endl;

    ArrayData array_data;
    std::vector<int> input;

    // ランダムな整数の配列
    std::cout << "\nsort" << std::endl;
    input = {64, 34, 25, 12, 22, 11, 90};
    std::cout << "  ソート前: ";
    printVector(input);
    array_data.set(input);
    array_data.sort();
    std::cout << "  ソート後: ";
    printVector(array_data.get());
    
    // 既にソートされている配列
    std::cout << "\nsort" << std::endl;
    input = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    std::cout << "  ソート前: ";
    printVector(input);
    array_data.set(input);
    array_data.sort();
    std::cout << "  ソート後: ";
    printVector(array_data.get());
    
    // 逆順の配列
    std::cout << "\nsort" << std::endl;
    input = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
    std::cout << "  ソート前: ";
    printVector(input);
    array_data.set(input);
    array_data.sort();
    std::cout << "  ソート後: ";
    printVector(array_data.get());
    
    // 重複要素を含む配列
    std::cout << "\nsort" << std::endl;
    input = {10, 9, 8, 7, 6, 10, 9, 8, 7, 6};
    std::cout << "  ソート前: ";
    printVector(input);
    array_data.set(input);
    array_data.sort();
    std::cout << "  ソート後: ";
    printVector(array_data.get());
    
    // 空の配列
    std::cout << "\nsort" << std::endl;
    input = {};
    std::cout << "  ソート前: ";
    printVector(input);
    array_data.set(input);
    array_data.sort();
    std::cout << "  ソート後: ";
    printVector(array_data.get());

    std::cout << "\nBubbleSort TEST <----- end" << std::endl;
    
    return 0;
}