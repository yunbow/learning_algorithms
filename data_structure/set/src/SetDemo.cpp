// C++
// データ構造: セット (Set)

#include <iostream>
#include <vector>
#include <algorithm>

class SetData {
private:
    std::vector<int> _data;

public:
    std::vector<int> get() {
        // 要素を取得
        return _data;
    }

    int get_index(int item) {
        // 指定された要素がセット内に存在するかどうかをチェックする。
        auto it = std::find(_data.begin(), _data.end(), item);
        if (it != _data.end()) {
            return std::distance(_data.begin(), it);
        } else {
            std::cout << "ERROR: " << item << " は範囲外です" << std::endl;
            return -1;
        }
    }

    int get_value(int index) {
        // 指定されたインデックスの要素を取得する。
        if (0 <= index && index < _data.size()) {
            return _data[index];
        } else {
            std::cout << "ERROR: " << index << " は範囲外です" << std::endl;
            return -1;
        }
    }

    bool add(int item) {
        // 要素をセットに追加する。
        if (std::find(_data.begin(), _data.end(), item) == _data.end()) {
            _data.push_back(item);
            return true;
        } else {
            std::cout << "ERROR: " << item << " は重複です" << std::endl;
            return false;
        }
    }

    bool remove(int item) {
        // 指定された要素をセットから削除する。
        auto it = std::find(_data.begin(), _data.end(), item);
        if (it != _data.end()) {
            _data.erase(it);
            return true;
        } else {
            std::cout << "ERROR: " << item << " は範囲外です" << std::endl;
            return false;
        }
    }

    bool is_empty() {
        // 空かどうかをチェックする
        return _data.empty();
    }

    size_t size() {
        // 要素数を返す
        return _data.size();
    }

    bool clear() {
        // 空にする
        _data.clear();
        return true;
    }
};

// ベクトルの内容を表示するためのヘルパー関数
void print_vector(const std::vector<int>& vec) {
    std::cout << "  現在のデータ: [";
    for (size_t i = 0; i < vec.size(); ++i) {
        std::cout << vec[i];
        if (i < vec.size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]" << std::endl;
}

int main() {
    std::cout << "Set TEST -----> start" << std::endl;

    std::cout << "\nnew" << std::endl;
    SetData set_data;
    print_vector(set_data.get());

    std::cout << "\nadd" << std::endl;
    std::vector<int> input = {10, 20, 30, 20, 40};
    for (int item : input) {
        std::cout << "  入力値: " << item << std::endl;
        bool output = set_data.add(item);
        std::cout << "  出力値: " << std::boolalpha << output << std::endl;
        print_vector(set_data.get());
    }

    std::cout << "\nsize" << std::endl;
    size_t size_output = set_data.size();
    std::cout << "  出力値: " << size_output << std::endl;

    std::cout << "\nis_empty" << std::endl;
    bool empty_output = set_data.is_empty();
    std::cout << "  出力値: " << std::boolalpha << empty_output << std::endl;

    std::cout << "\nget_value" << std::endl;
    std::vector<int> index_input = {0, 2, 5};
    for (int index : index_input) {
        std::cout << "  入力値: " << index << std::endl;
        int value_output = set_data.get_value(index);
        if (value_output != -1) {
            std::cout << "  出力値: " << value_output << std::endl;
        }
    }

    std::cout << "\nget_index" << std::endl;
    std::vector<int> item_input = {30, 99};
    for (int item : item_input) {
        std::cout << "  入力値: " << item << std::endl;
        int index_output = set_data.get_index(item);
        if (index_output != -1) {
            std::cout << "  出力値: " << index_output << std::endl;
        }
    }

    std::cout << "\nremove" << std::endl;
    std::vector<int> remove_input = {20, 50, 10};
    for (int item : remove_input) {
        std::cout << "  入力値: " << item << std::endl;
        bool remove_output = set_data.remove(item);
        std::cout << "  出力値: " << std::boolalpha << remove_output << std::endl;
        print_vector(set_data.get());
    }

    std::cout << "\nsize" << std::endl;
    size_output = set_data.size();
    std::cout << "  出力値: " << size_output << std::endl;

    std::cout << "\nclear" << std::endl;
    bool clear_output = set_data.clear();
    std::cout << "  出力値: " << std::boolalpha << clear_output << std::endl;
    print_vector(set_data.get());

    std::cout << "\nis_empty" << std::endl;
    empty_output = set_data.is_empty();
    std::cout << "  出力値: " << std::boolalpha << empty_output << std::endl;

    std::cout << "\nSet TEST <----- end" << std::endl;

    return 0;
}