// C++
// データ構造: ヒープ (Heap)

#include <iostream>
#include <vector>
#include <algorithm>

class HeapData {
private:
    std::vector<int> _data;
    bool is_min_heap;

    int _get_parent_idx(int idx) {
        // 親ノードのインデックスを計算
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
        return _get_parent_idx(idx) >= 0;
    }

    bool _has_left_child(int idx) {
        // 左の子ノードが存在するか確認
        return _get_left_child_idx(idx) < _data.size();
    }

    bool _has_right_child(int idx) {
        // 右の子ノードが存在するか確認
        return _get_right_child_idx(idx) < _data.size();
    }

    int _get_parent(int idx) {
        // 親ノードの値を取得
        return _data[_get_parent_idx(idx)];
    }

    int _get_left_child(int idx) {
        // 左の子ノードの値を取得
        return _data[_get_left_child_idx(idx)];
    }

    int _get_right_child(int idx) {
        // 右の子ノードの値を取得
        return _data[_get_right_child_idx(idx)];
    }

    void _swap(int idx1, int idx2) {
        // 2つのノードの値を交換
        std::swap(_data[idx1], _data[idx2]);
    }

    bool _should_swap(int idx1, int idx2) {
        // 最小ヒープでは親が子より大きい場合に交換
        // 最大ヒープでは親が子より小さい場合に交換
        if (is_min_heap) {
            return _data[idx1] > _data[idx2];
        } else {
            return _data[idx1] < _data[idx2];
        }
    }

    void _heapify_down(int idx) {
        // ノードを下方向に移動させてヒープ条件を満たす
        int smallest_or_largest = idx;
        
        // 左の子ノードと比較
        if (_has_left_child(idx) && _should_swap(smallest_or_largest, _get_left_child_idx(idx))) {
            smallest_or_largest = _get_left_child_idx(idx);
        }
        
        // 右の子ノードと比較
        if (_has_right_child(idx) && _should_swap(smallest_or_largest, _get_right_child_idx(idx))) {
            smallest_or_largest = _get_right_child_idx(idx);
        }
        
        // インデックスが変わっていたら交換して再帰的に処理
        if (smallest_or_largest != idx) {
            _swap(idx, smallest_or_largest);
            _heapify_down(smallest_or_largest);
        }
    }

    void _heapify_up(int idx) {
        // ノードを上方向に移動させてヒープ条件を満たす
        // 親がある限り、親と比較して必要なら交換
        while (_has_parent(idx) && _should_swap(_get_parent_idx(idx), idx)) {
            int parent_idx = _get_parent_idx(idx);
            _swap(parent_idx, idx);
            idx = parent_idx;
        }
    }

public:
    HeapData(bool is_min_heap = true) : is_min_heap(is_min_heap) {}

    std::vector<int> get() {
        // 要素を取得
        return _data;
    }

    int get_index(int item) {
        // 配列内で指定された値を検索し、最初に見つかったインデックスを返す
        for (int i = 0; i < _data.size(); i++) {
            if (_data[i] == item) {
                return i;
            }
        }
        std::cout << "ERROR: " << item << " は範囲外です" << std::endl;
        return -1;
    }

    int get_value(int index) {
        // 指定されたインデックスの要素を取得する
        if (0 <= index && index < _data.size()) {
            return _data[index];
        } else {
            std::cout << "ERROR: " << index << " は範囲外です" << std::endl;
            return -1;
        }
    }

    bool heapify(const std::vector<int>& array) {
        // 配列をヒープに変換
        _data = array;
        // 最後の親ノードから根に向かって、各部分木をヒープ化
        for (int i = _data.size() / 2 - 1; i >= 0; i--) {
            _heapify_down(i);
        }
        return true;
    }

    bool push(int value) {
        // ヒープに要素を追加
        _data.push_back(value);
        // 最後の要素を適切な位置に移動
        _heapify_up(_data.size() - 1);
        return true;
    }

    bool pop() {
        // ヒープが空の場合
        if (_data.empty()) {
            return false;
        }
        
        // 最後の要素をルートに移動
        int last_element = _data.back();
        _data.pop_back();
        
        if (!_data.empty()) {
            _data[0] = last_element;
            // ルートから下方向にヒープ条件を満たすように調整
            _heapify_down(0);
        }
        
        return true;
    }

    int peek() {
        // ヒープが空の場合
        if (_data.empty()) {
            return -1; // C++では明示的にnullの代わりに-1を返す
        }
        // ルート要素を返す（取り出さない）
        return _data[0];
    }

    bool is_empty() {
        // ヒープが空かどうかを確認
        return _data.empty();
    }

    int size() {
        // ヒープのサイズを返す
        return _data.size();
    }

    bool clear() {
        // ヒープをクリア
        _data.clear();
        return true;
    }
};

// ベクトルを表示するユーティリティ関数
void print_vector(const std::vector<int>& vec) {
    std::cout << "[";
    for (size_t i = 0; i < vec.size(); i++) {
        std::cout << vec[i];
        if (i < vec.size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]";
}

int main() {
    std::cout << "Heap TEST -----> start" << std::endl;

    std::cout << "\nmin heap: new" << std::endl;
    HeapData min_heap(true);
    std::cout << "  現在のデータ: ";
    print_vector(min_heap.get());
    std::cout << std::endl;

    std::cout << "\nmin heap: heapify" << std::endl;
    std::vector<int> input = {4, 10, 3, 5, 1};
    std::cout << "  入力値: ";
    print_vector(input);
    std::cout << std::endl;
    bool output = min_heap.heapify(input);
    std::cout << "  出力値: " << (output ? "true" : "false") << std::endl;
    std::cout << "  現在のデータ: ";
    print_vector(min_heap.get());
    std::cout << std::endl;

    std::cout << "\nmin heap: push" << std::endl;
    int input_value = 2;
    std::cout << "  入力値: " << input_value << std::endl;
    output = min_heap.push(input_value);
    std::cout << "  出力値: " << (output ? "true" : "false") << std::endl;
    std::cout << "  現在のデータ: ";
    print_vector(min_heap.get());
    std::cout << std::endl;

    std::cout << "\nmin heap: push" << std::endl;
    input_value = 15;
    std::cout << "  入力値: " << input_value << std::endl;
    output = min_heap.push(input_value);
    std::cout << "  出力値: " << (output ? "true" : "false") << std::endl;
    std::cout << "  現在のデータ: ";
    print_vector(min_heap.get());
    std::cout << std::endl;

    std::cout << "\nmin heap: peek" << std::endl;
    int peek_result = min_heap.peek();
    std::cout << "  出力値: " << peek_result << std::endl;

    std::cout << "\nmin heap: pop" << std::endl;
    output = min_heap.pop();
    std::cout << "  出力値: " << (output ? "true" : "false") << std::endl;
    std::cout << "  現在のデータ: ";
    print_vector(min_heap.get());
    std::cout << std::endl;

    std::cout << "\nmin heap: pop" << std::endl;
    output = min_heap.pop();
    std::cout << "  出力値: " << (output ? "true" : "false") << std::endl;
    std::cout << "  現在のデータ: ";
    print_vector(min_heap.get());
    std::cout << std::endl;

    std::cout << "\nmin heap: get_index" << std::endl;
    input_value = 3;
    std::cout << "  入力値: " << input_value << std::endl;
    int index_result = min_heap.get_index(input_value);
    std::cout << "  出力値: " << index_result << std::endl;

    std::cout << "\nmin heap: get_index" << std::endl;
    input_value = 100;
    std::cout << "  入力値: " << input_value << std::endl;
    index_result = min_heap.get_index(input_value);
    std::cout << "  出力値: " << index_result << std::endl;

    std::cout << "\nmin heap: is_empty" << std::endl;
    bool empty_result = min_heap.is_empty();
    std::cout << "  出力値: " << (empty_result ? "true" : "false") << std::endl;

    std::cout << "\nmin heap: size" << std::endl;
    int size_result = min_heap.size();
    std::cout << "  出力値: " << size_result << std::endl;

    std::cout << "\nmin heap: clear" << std::endl;
    output = min_heap.clear();
    std::cout << "  出力値: " << (output ? "true" : "false") << std::endl;
    std::cout << "  現在のデータ: ";
    print_vector(min_heap.get());
    std::cout << std::endl;

    std::cout << "\nmin heap: is_empty" << std::endl;
    empty_result = min_heap.is_empty();
    std::cout << "  出力値: " << (empty_result ? "true" : "false") << std::endl;

    std::cout << "\nmax heap: new" << std::endl;
    HeapData max_heap(false);
    std::cout << "  現在のデータ: ";
    print_vector(max_heap.get());
    std::cout << std::endl;

    std::cout << "\nmax heap: heapify" << std::endl;
    input = {4, 10, 3, 5, 1};
    std::cout << "  入力値: ";
    print_vector(input);
    std::cout << std::endl;
    output = max_heap.heapify(input);
    std::cout << "  出力値: " << (output ? "true" : "false") << std::endl;
    std::cout << "  現在のデータ: ";
    print_vector(max_heap.get());
    std::cout << std::endl;

    std::cout << "\nmax heap: push" << std::endl;
    input_value = 12;
    std::cout << "  入力値: " << input_value << std::endl;
    output = max_heap.push(input_value);
    std::cout << "  出力値: " << (output ? "true" : "false") << std::endl;
    std::cout << "  現在のデータ: ";
    print_vector(max_heap.get());
    std::cout << std::endl;

    std::cout << "\nmax heap: push" << std::endl;
    input_value = 0;
    std::cout << "  入力値: " << input_value << std::endl;
    output = max_heap.push(input_value);
    std::cout << "  出力値: " << (output ? "true" : "false") << std::endl;
    std::cout << "  現在のデータ: ";
    print_vector(max_heap.get());
    std::cout << std::endl;

    std::cout << "\nmax heap: peek" << std::endl;
    peek_result = max_heap.peek();
    std::cout << "  出力値: " << peek_result << std::endl;

    std::cout << "\nmax heap: pop" << std::endl;
    output = max_heap.pop();
    std::cout << "  出力値: " << (output ? "true" : "false") << std::endl;
    std::cout << "  現在のデータ: ";
    print_vector(max_heap.get());
    std::cout << std::endl;

    std::cout << "\nmax heap: pop" << std::endl;
    output = max_heap.pop();
    std::cout << "  出力値: " << (output ? "true" : "false") << std::endl;
    std::cout << "  現在のデータ: ";
    print_vector(max_heap.get());
    std::cout << std::endl;

    std::cout << "\nmax heap: get_index" << std::endl;
    input_value = 5;
    std::cout << "  入力値: " << input_value << std::endl;
    index_result = max_heap.get_index(input_value);
    std::cout << "  出力値: " << index_result << std::endl;

    std::cout << "\nmax heap: get_index" << std::endl;
    input_value = -10;
    std::cout << "  入力値: " << input_value << std::endl;
    index_result = max_heap.get_index(input_value);
    std::cout << "  出力値: " << index_result << std::endl;

    std::cout << "\nmax heap: is_empty" << std::endl;
    empty_result = max_heap.is_empty();
    std::cout << "  出力値: " << (empty_result ? "true" : "false") << std::endl;

    std::cout << "\nmax heap: size" << std::endl;
    size_result = max_heap.size();
    std::cout << "  出力値: " << size_result << std::endl;

    std::cout << "\nmax heap: clear" << std::endl;
    output = max_heap.clear();
    std::cout << "  出力値: " << (output ? "true" : "false") << std::endl;
    std::cout << "  現在のデータ: ";
    print_vector(max_heap.get());
    std::cout << std::endl;

    std::cout << "\nmax heap: is_empty" << std::endl;
    empty_result = max_heap.is_empty();
    std::cout << "  出力値: " << (empty_result ? "true" : "false") << std::endl;

    std::cout << "\nHeap TEST <----- end" << std::endl;

    return 0;
}