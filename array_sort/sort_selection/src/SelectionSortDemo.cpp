// C++
// 配列の並び替え: 選択ソート (Selection Sort)

#include <iostream>
#include <vector>

class ArrayData {
private:
    std::vector<int> _data;

    std::vector<int> _selection_sort(std::vector<int> target) {
        // 配列の長さを取得
        int n = target.size();
        
        // 配列を順番に走査
        for (int i = 0; i < n; i++) {
            // 未ソート部分の最小値のインデックスを見つける
            int min_index = i;
            for (int j = i + 1; j < n; j++) {
                if (target[j] < target[min_index]) {
                    min_index = j;
                }
            }
            
            // 見つかった最小値と現在の位置を交換
            std::swap(target[i], target[min_index]);
        }
        
        return target;
    }

public:
    ArrayData() {}

    std::vector<int> get() {
        return _data;
    }

    bool set(std::vector<int> data) {
        _data = data;
        return true;
    }

    bool sort() {
        _data = _selection_sort(_data);
        return true;
    }
};

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
    std::cout << "SelectionSort TEST -----> start" << std::endl;

    ArrayData array_data;

    // ランダムな整数の配列
    std::cout << "\nsort" << std::endl;
    std::vector<int> input1 = {64, 34, 25, 12, 22, 11, 90};
    std::cout << "  ソート前: ";
    printVector(input1);
    array_data.set(input1);
    array_data.sort();
    std::cout << "  ソート後: ";
    printVector(array_data.get());
    
    // 既にソートされている配列
    std::cout << "\nsort" << std::endl;
    std::vector<int> input2 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    std::cout << "  ソート前: ";
    printVector(input2);
    array_data.set(input2);
    array_data.sort();
    std::cout << "  ソート後: ";
    printVector(array_data.get());
    
    // 逆順の配列
    std::cout << "\nsort" << std::endl;
    std::vector<int> input3 = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
    std::cout << "  ソート前: ";
    printVector(input3);
    array_data.set(input3);
    array_data.sort();
    std::cout << "  ソート後: ";
    printVector(array_data.get());
    
    // 重複要素を含む配列
    std::cout << "\nsort" << std::endl;
    std::vector<int> input4 = {10, 9, 8, 7, 6, 10, 9, 8, 7, 6};
    std::cout << "  ソート前: ";
    printVector(input4);
    array_data.set(input4);
    array_data.sort();
    std::cout << "  ソート後: ";
    printVector(array_data.get());
    
    // 空の配列
    std::cout << "\nsort" << std::endl;
    std::vector<int> input5 = {};
    std::cout << "  ソート前: ";
    printVector(input5);
    array_data.set(input5);
    array_data.sort();
    std::cout << "  ソート後: ";
    printVector(array_data.get());

    std::cout << "\nSelectionSort TEST <----- end" << std::endl;
    
    return 0;
}