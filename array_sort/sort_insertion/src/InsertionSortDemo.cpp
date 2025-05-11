// C++
// 配列の並び替え: 挿入ソート (Insertion Sort)

#include <iostream>
#include <vector>

class ArrayData {
private:
    std::vector<int> _data;

public:
    ArrayData() {}

    const std::vector<int>& get() {
        return _data;
    }

    bool set(const std::vector<int>& data) {
        _data = data;
        return true;
    }

    bool sort() {
        // 配列の長さを取得
        int n = _data.size();
        
        // 2番目の要素から始める（最初の要素は既にソート済みと見なす）
        for (int i = 1; i < n; i++) {
            // 現在の要素を取得
            int key = _data[i];
            
            // ソート済み部分の最後の要素のインデックス
            int j = i - 1;
            
            // keyより大きい要素をすべて右にシフト
            while (j >= 0 && _data[j] > key) {
                _data[j + 1] = _data[j];
                j--;
            }
            
            // 適切な位置にkeyを挿入
            _data[j + 1] = key;
        }
        
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
    std::cout << "InsertionSort TEST -----> start" << std::endl;

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

    std::cout << "\nInsertionSort TEST <----- end" << std::endl;
    
    return 0;
}