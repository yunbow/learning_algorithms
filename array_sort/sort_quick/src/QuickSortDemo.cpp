// C++
// 配列の並び替え: クイックソート (Quick Sort)

#include <iostream>
#include <vector>

class ArrayData {
private:
    std::vector<int> _data;

    std::vector<int> _quick_sort(const std::vector<int>& target) {
        // 空の配列または要素が1つの場合はそのまま返す（基底条件）
        if (target.size() <= 1) {
            return target;
        }
        
        // ピボットを選択（この実装では最後の要素を選択）
        int pivot = target.back();
        
        // ピボットより小さい要素と大きい要素に分ける
        std::vector<int> left;
        std::vector<int> right;
        
        // 最後の要素（ピボット）を除いて配列をスキャン
        for (size_t i = 0; i < target.size() - 1; i++) {
            if (target[i] <= pivot) {
                left.push_back(target[i]);
            } else {
                right.push_back(target[i]);
            }
        }
        
        // 左側の部分配列をソート
        std::vector<int> sorted_left = _quick_sort(left);
        // 右側の部分配列をソート
        std::vector<int> sorted_right = _quick_sort(right);
        
        // 左側の部分配列、ピボット、右側の部分配列を結合
        std::vector<int> result;
        result.insert(result.end(), sorted_left.begin(), sorted_left.end());
        result.push_back(pivot);
        result.insert(result.end(), sorted_right.begin(), sorted_right.end());
        
        return result;
    }

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
        _data = _quick_sort(_data);
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
    std::cout << "QuickSort TEST -----> start" << std::endl;

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

    std::cout << "\nQuickSort TEST <----- end" << std::endl;

    return 0;
}