// C++
// データ構造: キュー (Queue)

#include <iostream>
#include <vector>
#include <algorithm>

class QueueData {
private:
    std::vector<int> _data;

public:
    QueueData() {}

    std::vector<int> get() {
        // 要素を取得
        return _data;
    }

    int get_index(int item) {
        // キュー内に指定した要素があるか検索
        auto it = std::find(_data.begin(), _data.end(), item);
        if (it != _data.end()) {
            return std::distance(_data.begin(), it);
        } else {
            std::cout << "ERROR: " << item << " は範囲外です" << std::endl;
            return -1;
        }
    }

    int get_value(int index) {
        // 指定したインデックスの要素を取得
        if (0 <= index && index < static_cast<int>(_data.size())) {
            return _data[index];
        } else {
            std::cout << "Error: インデックス " << index << " は範囲外です" << std::endl;
            return -1;
        }
    }

    bool enqueue(int item) {
        // キューの末尾に要素を追加
        _data.push_back(item);
        return true;
    }

    bool dequeue() {
        // キューが空でない場合、先頭要素を取り出す
        if (!is_empty()) {
            _data.erase(_data.begin());
            return true;
        } else {
            std::cout << "ERROR: キューが空です" << std::endl;
            return false;
        }
    }

    int peek() {
        // キューが空でない場合、先頭要素を参照
        if (!is_empty()) {
            return _data[0];
        } else {
            std::cout << "ERROR: キューが空です" << std::endl;
            return -1;
        }
    }

    bool is_empty() {
        // キューが空かどうかを確認
        return _data.empty();
    }

    int size() {
        // キューの要素数を返す
        return static_cast<int>(_data.size());
    }

    bool clear() {
        // キューをクリア
        _data.clear();
        return true;
    }
};

void printVector(const std::vector<int>& vec) {
    std::cout << "  現在のデータ: [";
    for (size_t i = 0; i < vec.size(); i++) {
        std::cout << vec[i];
        if (i < vec.size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]" << std::endl;
}

int main() {
    std::cout << "Queue TEST -----> start" << std::endl;

    std::cout << "\nnew" << std::endl;
    QueueData queue_data;
    printVector(queue_data.get());

    std::cout << "\nis_empty" << std::endl;
    bool output = queue_data.is_empty();
    std::cout << "  出力値: " << std::boolalpha << output << std::endl;

    std::cout << "\nenqueue" << std::endl;
    std::vector<int> input = {10, 20, 30};
    for (int item : input) {
        std::cout << "  入力値: " << item << std::endl;
        output = queue_data.enqueue(item);
        std::cout << "  出力値: " << output << std::endl;
        printVector(queue_data.get());
    }

    std::cout << "\nsize" << std::endl;
    int size_output = queue_data.size();
    std::cout << "  出力値: " << size_output << std::endl;

    std::cout << "\npeek" << std::endl;
    int peek_output = queue_data.peek();
    std::cout << "  出力値: " << peek_output << std::endl;

    std::cout << "\nget_index" << std::endl;
    int index_input = 20;
    std::cout << "  入力値: " << index_input << std::endl;
    int index_output = queue_data.get_index(index_input);
    std::cout << "  出力値: " << index_output << std::endl;

    std::cout << "\nget_index" << std::endl;
    index_input = 50;
    std::cout << "  入力値: " << index_input << std::endl;
    index_output = queue_data.get_index(index_input);
    std::cout << "  出力値: " << index_output << std::endl;

    std::cout << "\ndequeue" << std::endl;
    output = queue_data.dequeue();
    std::cout << "  出力値: " << output << std::endl;
    printVector(queue_data.get());

    std::cout << "\ndequeue" << std::endl;
    output = queue_data.dequeue();
    std::cout << "  出力値: " << output << std::endl;
    printVector(queue_data.get());

    std::cout << "\nsize" << std::endl;
    size_output = queue_data.size();
    std::cout << "  出力値: " << size_output << std::endl;

    std::cout << "\ndequeue" << std::endl;
    output = queue_data.dequeue();
    std::cout << "  出力値: " << output << std::endl;
    printVector(queue_data.get());

    std::cout << "\ndequeue" << std::endl;
    output = queue_data.dequeue();
    std::cout << "  出力値: " << output << std::endl;
    printVector(queue_data.get());

    std::cout << "\nis_empty" << std::endl;
    output = queue_data.is_empty();
    std::cout << "  出力値: " << output << std::endl;

    std::cout << "\nclear" << std::endl;
    output = queue_data.clear();
    std::cout << "  出力値: " << output << std::endl;
    printVector(queue_data.get());

    std::cout << "\nsize" << std::endl;
    size_output = queue_data.size();
    std::cout << "  出力値: " << size_output << std::endl;

    std::cout << "\nQueue TEST <----- end" << std::endl;

    return 0;
}