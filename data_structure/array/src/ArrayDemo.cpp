// C++
// データ構造: 配列 (Array)

#include <iostream>
#include <vector>
#include <algorithm>

class ArrayData {
private:
    std::vector<int> _data;

public:
    ArrayData() = default;

    std::vector<int> get() {
        // 要素を取得
        return _data;
    }

    int get_index(int item) {
        // 配列内で指定された値を検索し、最初に見つかったインデックスを返す
        auto it = std::find(_data.begin(), _data.end(), item);
        if (it != _data.end()) {
            return std::distance(_data.begin(), it);
        } else {
            std::cout << "ERROR: " << item << " は範囲外です" << std::endl;
            return -1;
        }
    }

    int get_value(int index) {
        // 指定されたインデックスの要素を取得する
        if (0 <= index && index < _data.size()) {
            return _data[index];
        } else {
            std::cout << "ERROR: " << index << " は範囲外です" << std::endl;
            return -1; // C++ではNoneの代わりに-1を返す
        }
    }

    bool add(int item) {
        // 配列の末尾に要素を追加する
        _data.push_back(item);
        return true;
    }

    bool remove(int index) {
        // 指定されたインデックスの要素を削除する
        if (0 <= index && index < _data.size()) {
            _data.erase(_data.begin() + index);
            return true;
        } else {
            std::cout << "ERROR: " << index << " は範囲外です" << std::endl;
            return false;
        }
    }

    bool update(int index, int new_value) {
        // 指定されたインデックスの要素を新しい値に更新する
        if (0 <= index && index < _data.size()) {
            _data[index] = new_value;
            return true;
        } else {
            std::cout << "ERROR: " << index << " は範囲外です" << std::endl;
            return false;
        }
    }

    std::vector<int> reverse() {
        // 配列の要素を逆順にする
        std::reverse(_data.begin(), _data.end());
        return _data;
    }

    std::vector<int> sort(bool descending = false) {
        // 配列の要素をソートする
        if (descending) {
            std::sort(_data.begin(), _data.end(), std::greater<int>());
        } else {
            std::sort(_data.begin(), _data.end());
        }
        return _data;
    }

    bool is_empty() {
        // 配列が空かどうか
        return _data.empty();
    }

    size_t size() {
        // 配列のサイズ（要素数）を返す
        return _data.size();
    }

    bool clear() {
        // 配列の全要素を削除する
        _data.clear();
        return true;
    }
};

// ベクターを表示するヘルパー関数
void print_vector(const std::vector<int>& vec) {
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
    std::cout << "Array TEST -----> start" << std::endl;

    std::cout << "\nnew" << std::endl;
    ArrayData array_data;
    std::cout << "  現在のデータ: ";
    print_vector(array_data.get());
    
    std::cout << "\nadd" << std::endl;
    std::vector<int> input = {10, 20, 30, 10, 40};
    for (int item : input) {
        std::cout << "  入力値: " << item << std::endl;
        bool output = array_data.add(item);
        std::cout << "  出力値: " << std::boolalpha << output << std::endl;
        std::cout << "  現在のデータ: ";
        print_vector(array_data.get());
    }

    std::cout << "\nsize" << std::endl;
    size_t size_output = array_data.size();
    std::cout << "  出力値: " << size_output << std::endl;

    std::cout << "\nis_empty" << std::endl;
    bool empty_output = array_data.is_empty();
    std::cout << "  出力値: " << std::boolalpha << empty_output << std::endl;

    std::cout << "\nget_value" << std::endl;
    int get_index = 2;
    std::cout << "  入力値: " << get_index << std::endl;
    int get_value_output = array_data.get_value(get_index);
    std::cout << "  出力値: " << get_value_output << std::endl;

    std::cout << "\nget_value" << std::endl;
    get_index = 10;
    std::cout << "  入力値: " << get_index << std::endl;
    get_value_output = array_data.get_value(get_index);
    std::cout << "  出力値: " << get_value_output << std::endl;

    std::cout << "\nupdate" << std::endl;
    int update_index = 1;
    int new_value = 25;
    std::cout << "  入力値: (" << update_index << ", " << new_value << ")" << std::endl;
    bool update_output = array_data.update(update_index, new_value);
    std::cout << "  出力値: " << std::boolalpha << update_output << std::endl;
    std::cout << "  現在のデータ: ";
    print_vector(array_data.get());

    std::cout << "\nupdate" << std::endl;
    update_index = 15;
    new_value = 25;
    std::cout << "  入力値: (" << update_index << ", " << new_value << ")" << std::endl;
    update_output = array_data.update(update_index, new_value);
    std::cout << "  出力値: " << std::boolalpha << update_output << std::endl;
    std::cout << "  現在のデータ: ";
    print_vector(array_data.get());

    std::cout << "\nget_index" << std::endl;
    int find_value = 10;
    std::cout << "  入力値: " << find_value << std::endl;
    int index_output = array_data.get_index(find_value);
    std::cout << "  出力値: " << index_output << std::endl;

    std::cout << "\nget_index" << std::endl;
    find_value = 99;
    std::cout << "  入力値: " << find_value << std::endl;
    index_output = array_data.get_index(find_value);
    std::cout << "  出力値: " << index_output << std::endl;

    std::cout << "\nremove" << std::endl;
    int remove_index = 3;
    std::cout << "  入力値: " << remove_index << std::endl;
    bool remove_output = array_data.remove(remove_index);
    std::cout << "  出力値: " << std::boolalpha << remove_output << std::endl;
    std::cout << "  現在のデータ: ";
    print_vector(array_data.get());

    std::cout << "\nremove" << std::endl;
    remove_index = 8;
    std::cout << "  入力値: " << remove_index << std::endl;
    remove_output = array_data.remove(remove_index);
    std::cout << "  出力値: " << std::boolalpha << remove_output << std::endl;
    std::cout << "  現在のデータ: ";
    print_vector(array_data.get());

    std::cout << "\nreverse" << std::endl;
    std::vector<int> reverse_output = array_data.reverse();
    std::cout << "  出力値: ";
    print_vector(reverse_output);

    std::cout << "\nsort" << std::endl;
    std::cout << "  入力値: descending=false" << std::endl;
    std::vector<int> sort_output = array_data.sort(false);
    std::cout << "  出力値: ";
    print_vector(sort_output);

    std::cout << "\nsort" << std::endl;
    std::cout << "  入力値: descending=true" << std::endl;
    sort_output = array_data.sort(true);
    std::cout << "  出力値: ";
    print_vector(sort_output);

    std::cout << "\nclear" << std::endl;
    bool clear_output = array_data.clear();
    std::cout << "  出力値: " << std::boolalpha << clear_output << std::endl;
    std::cout << "  現在のデータ: ";
    print_vector(array_data.get());

    std::cout << "\nis_empty" << std::endl;
    empty_output = array_data.is_empty();
    std::cout << "  出力値: " << std::boolalpha << empty_output << std::endl;

    std::cout << "\nArray TEST <----- end" << std::endl;

    return 0;
}