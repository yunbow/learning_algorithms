// C++
// 配列の並び替え: マージソート (Merge Sort)

#include <iostream>
#include <vector>

class ArrayData {
private:
    std::vector<int> _data;

    std::vector<int> _merge_sort(const std::vector<int>& target) {
        // 配列の長さが1以下の場合はそのまま返す（基本ケース）
        if (target.size() <= 1) {
            return target;
        }
        
        // 配列を半分に分割
        size_t mid = target.size() / 2;
        std::vector<int> left_half(target.begin(), target.begin() + mid);
        std::vector<int> right_half(target.begin() + mid, target.end());
        
        // 左右の半分を再帰的にソート
        left_half = _merge_sort(left_half);
        right_half = _merge_sort(right_half);
        
        // ソート済みの半分同士をマージ
        return _merge(left_half, right_half);
    }

    std::vector<int> _merge(const std::vector<int>& left, const std::vector<int>& right) {
        std::vector<int> result;
        size_t i = 0, j = 0;
        
        // 左右の配列を比較しながらマージ
        while (i < left.size() && j < right.size()) {
            if (left[i] <= right[j]) {
                result.push_back(left[i]);
                i++;
            } else {
                result.push_back(right[j]);
                j++;
            }
        }
        
        // 残った要素を追加
        while (i < left.size()) {
            result.push_back(left[i]);
            i++;
        }
        
        while (j < right.size()) {
            result.push_back(right[j]);
            j++;
        }
        
        return result;
    }

public:
    std::vector<int> get() {
        return _data;
    }

    bool set(const std::vector<int>& data) {
        _data = data;
        return true;
    }

    bool sort() {
        _data = _merge_sort(_data);
        return true;
    }
};

void printVector(const std::vector<int>& vec) {
    std::cout << "  [";
    for (size_t i = 0; i < vec.size(); ++i) {
        std::cout << vec[i];
        if (i < vec.size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]" << std::endl;
}

int main() {
    std::cout << "MergeSort TEST -----> start" << std::endl;

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

    std::cout << "\nMergeSort TEST <----- end" << std::endl;
    
    return 0;
}