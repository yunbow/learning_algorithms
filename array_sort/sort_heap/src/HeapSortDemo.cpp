#include <iostream>
#include <vector>

// 配列の並び替え: ヒープソート (Heap Sort)

class HeapData {
private:
    std::vector<int> _data;
    bool is_min_heap;

    int _get_parent_idx(int idx) {
        // 親ノードのインデックスを計算
        if (idx <= 0) {
            return -1; // 根ノードには親がない
        }
        return (idx - 1) / 2;
    }

    int _get_left_child_idx(int idx) {
        // 左の子ノードのインデックスを計算
        return 2 * idx + 1;
    }

    int _get_right_child_idx(int idx) {
        // 右の子ノードのインデックスを計算
        return 2 * idx + 2;
    }

    bool _has_parent(int idx) {
        // 親ノードが存在するか確認
        return _get_parent_idx(idx) >= 0 && _get_parent_idx(idx) < static_cast<int>(_data.size());
    }

    bool _has_left_child(int idx) {
        // 左の子ノードが存在するか確認
        return _get_left_child_idx(idx) < static_cast<int>(_data.size());
    }

    bool _has_right_child(int idx) {
        // 右の子ノードが存在するか確認
        return _get_right_child_idx(idx) < static_cast<int>(_data.size());
    }

    void _swap(int idx1, int idx2) {
        // 2つのノードの値を交換
        if (0 <= idx1 && idx1 < static_cast<int>(_data.size()) && 0 <= idx2 && idx2 < static_cast<int>(_data.size())) {
            std::swap(_data[idx1], _data[idx2]);
        } else {
            std::cout << "Warning: Swap indices out of bounds: " << idx1 << ", " << idx2 << std::endl;
        }
    }

    bool _should_swap(int idx1, int idx2) {
        // 最小ヒープでは idx1(親) が idx2(子) より大きい場合に交換
        // 最大ヒープでは idx1(親) が idx2(子) より小さい場合に交換
        if (is_min_heap) {
            return _data[idx1] > _data[idx2];
        } else { // Max heap
            return _data[idx1] < _data[idx2];
        }
    }

    void _heapify_down(int idx) {
        // ノードを下方向に移動させてヒープ条件を満たす (is_min_heap に従う)
        int smallest_or_largest = idx;

        int left_child_idx = _get_left_child_idx(idx);
        int right_child_idx = _get_right_child_idx(idx);

        // 左の子ノードと比較 (存在する場合)
        if (_has_left_child(idx)) {
            if (is_min_heap) { // Min-heap: compare with smallest child
                if (_data[left_child_idx] < _data[smallest_or_largest]) {
                    smallest_or_largest = left_child_idx;
                }
            } else { // Max-heap: compare with largest child
                if (_data[left_child_idx] > _data[smallest_or_largest]) {
                    smallest_or_largest = left_child_idx;
                }
            }
        }

        // 右の子ノードと比較 (存在する場合)
        if (_has_right_child(idx)) {
            if (is_min_heap) { // Min-heap: compare with smallest child
                if (_data[right_child_idx] < _data[smallest_or_largest]) {
                    smallest_or_largest = right_child_idx;
                }
            } else { // Max-heap: compare with largest child
                if (_data[right_child_idx] > _data[smallest_or_largest]) {
                    smallest_or_largest = right_child_idx;
                }
            }
        }

        // インデックスが変わっていたら交換して再帰的に処理
        if (smallest_or_largest != idx) {
            _swap(idx, smallest_or_largest);
            _heapify_down(smallest_or_largest);
        }
    }

    void _max_heapify_down(int idx, int heap_size) {
        // 指定されたヒープサイズ内で、idx から下方向に最大ヒープ条件を満たすように調整する。
        int largest = idx; // 最大ヒープでは、親と子の間で最大のものを探す

        int left_child_idx = _get_left_child_idx(idx);
        int right_child_idx = _get_right_child_idx(idx);

        // 左の子ノードと比較 (ヒープサイズ内に存在する場合)
        if (left_child_idx < heap_size && _data[left_child_idx] > _data[largest]) {
            largest = left_child_idx;
        }

        // 右の子ノードと比較 (ヒープサイズ内に存在する場合)
        if (right_child_idx < heap_size && _data[right_child_idx] > _data[largest]) {
            largest = right_child_idx;
        }

        // largest が現在のノード idx と異なる場合、交換して再帰的に処理
        if (largest != idx) {
            _swap(idx, largest);
            _max_heapify_down(largest, heap_size);
        }
    }

    void _heapify_up(int idx) {
        // ノードを上方向に移動させてヒープ条件を満たす (is_min_heap に従う)
        while (_has_parent(idx)) {
            int parent_idx = _get_parent_idx(idx);
            if (_should_swap(parent_idx, idx)) {
                _swap(parent_idx, idx);
                idx = parent_idx;
            } else {
                break; // Heap property satisfied
            }
        }
    }

public:
    HeapData(bool is_min_heap = true) : is_min_heap(is_min_heap) {}

    std::vector<int> get() {
        // 要素を取得 (現在の internal data を返す)
        return _data;
    }

    bool heapify(const std::vector<int>& array) {
        _data = array;
        int n = static_cast<int>(_data.size());
        // 最後の非葉ノードから根に向かって、各部分木をヒープ化
        for (int i = n / 2 - 1; i >= 0; i--) {
            _heapify_down(i); // Use the regular heapify_down
        }
        return true;
    }

    bool sort() {
        int n = static_cast<int>(_data.size());

        // 配列を最大ヒープに変換する
        for (int i = n / 2 - 1; i >= 0; i--) {
            _max_heapify_down(i, n);
        }

        // 最大ヒープから要素を一つずつ取り出してソート済みの位置に移動
        for (int i = n - 1; i > 0; i--) {
            // 現在の根 (最大値) をヒープの最後の要素と交換
            _swap(0, i);

            // 交換された要素 (元の最大値) は正しい位置に置かれたので、
            // 次のヒープ調整からは除外する。ヒープサイズは i に減少。
            // 新しい根 (元のヒープの最後の要素だったもの) から
            // _max_heapify_down を使って残りの要素を最大ヒープに調整
            _max_heapify_down(0, i);
        }
        
        return true;
    }
};

// Vectorを出力するためのヘルパー関数
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
    std::cout << "HeapSort TEST -----> start" << std::endl;

    HeapData heap_data;

    // ランダムな整数の配列
    std::cout << "\nsort" << std::endl;
    std::vector<int> input1 = {64, 34, 25, 12, 22, 11, 90};
    std::cout << "  ソート前: ";
    printVector(input1);
    heap_data.heapify(input1);
    heap_data.sort();
    std::cout << "  ソート後: ";
    printVector(heap_data.get());
    
    // 既にソートされている配列
    std::cout << "\nsort" << std::endl;
    std::vector<int> input2 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    std::cout << "  ソート前: ";
    printVector(input2);
    heap_data.heapify(input2);
    heap_data.sort();
    std::cout << "  ソート後: ";
    printVector(heap_data.get());
    
    // 逆順の配列
    std::cout << "\nsort" << std::endl;
    std::vector<int> input3 = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
    std::cout << "  ソート前: ";
    printVector(input3);
    heap_data.heapify(input3);
    heap_data.sort();
    std::cout << "  ソート後: ";
    printVector(heap_data.get());
    
    // 重複要素を含む配列
    std::cout << "\nsort" << std::endl;
    std::vector<int> input4 = {10, 9, 8, 7, 6, 10, 9, 8, 7, 6};
    std::cout << "  ソート前: ";
    printVector(input4);
    heap_data.heapify(input4);
    heap_data.sort();
    std::cout << "  ソート後: ";
    printVector(heap_data.get());
    
    // 空の配列
    std::cout << "\nsort" << std::endl;
    std::vector<int> input5;
    std::cout << "  ソート前: ";
    printVector(input5);
    heap_data.heapify(input5);
    heap_data.sort();
    std::cout << "  ソート後: ";
    printVector(heap_data.get());

    std::cout << "\nHeapSort TEST <----- end" << std::endl;
    
    return 0;
}