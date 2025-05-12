// C++
// データ構造: スタック (Stack)

#include <iostream>
#include <vector>

class StackData {
private:
    std::vector<int> _data;

public:
    StackData() {}

    std::vector<int> get() {
        return _data;
    }

    int get_index(int item) {
        auto it = std::find(_data.begin(), _data.end(), item);
        if (it != _data.end()) {
            return it - _data.begin();
        } else {
            std::cout << "ERROR: " << item << " は範囲外です" << std::endl;
            return -1;
        }
    }

    int get_value(int index) {
        if (0 <= index && index < _data.size()) {
            return _data[index];
        } else {
            std::cout << "ERROR: " << index << " は範囲外です" << std::endl;
            return -1;
        }
    }

    bool push(int item) {
        _data.push_back(item);
        return true;
    }

    bool pop() {
        if (!is_empty()) {
            _data.pop_back();
            return true;
        } else {
            std::cout << "ERROR: 空です" << std::endl;
            return false;
        }
    }

    int peek() {
        if (!is_empty()) {
            return _data.back();
        } else {
            return -1;
        }
    }

    bool is_empty() {
        return _data.empty();
    }

    int size() {
        return _data.size();
    }

    bool clear() {
        _data.clear();
        return true;
    }
};

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
    std::cout << "Stack TEST -----> start" << std::endl;

    std::cout << "\nnew" << std::endl;
    StackData stack_data;
    print_vector(stack_data.get());

    std::cout << "\nis_empty" << std::endl;
    bool output_bool = stack_data.is_empty();
    std::cout << "  出力値: " << std::boolalpha << output_bool << std::endl;

    std::cout << "\nsize" << std::endl;
    int output_int = stack_data.size();
    std::cout << "  出力値: " << output_int << std::endl;

    std::cout << "\npush" << std::endl;
    std::vector<int> items_to_push = {10, 20, 30, 40};
    for (int item : items_to_push) {
        std::cout << "  入力値: " << item << std::endl;
        bool output = stack_data.push(item);
        std::cout << "  出力値: " << std::boolalpha << output << std::endl;
        print_vector(stack_data.get());
    }

    std::cout << "\nsize" << std::endl;
    output_int = stack_data.size();
    std::cout << "  出力値: " << output_int << std::endl;

    std::cout << "\nis_empty" << std::endl;
    output_bool = stack_data.is_empty();
    std::cout << "  出力値: " << std::boolalpha << output_bool << std::endl;

    std::cout << "\npeek" << std::endl;
    output_int = stack_data.peek();
    std::cout << "  出力値: " << output_int << std::endl;

    std::cout << "\nget_index" << std::endl;
    int input = 30;
    std::cout << "  入力値: " << input << std::endl;
    output_int = stack_data.get_index(input);
    std::cout << "  出力値: " << output_int << std::endl;

    std::cout << "\nget_index" << std::endl;
    input = 50;
    std::cout << "  入力値: " << input << std::endl;
    output_int = stack_data.get_index(input);
    std::cout << "  出力値: " << output_int << std::endl;

    std::cout << "\npop" << std::endl;
    while (!stack_data.is_empty()) {
        output_bool = stack_data.pop();
        std::cout << "  出力値: " << std::boolalpha << output_bool << std::endl;
        print_vector(stack_data.get());
    }

    std::cout << "\nis_empty" << std::endl;
    output_bool = stack_data.is_empty();
    std::cout << "  出力値: " << std::boolalpha << output_bool << std::endl;

    std::cout << "\nsize" << std::endl;
    output_int = stack_data.size();
    std::cout << "  出力値: " << output_int << std::endl;

    std::cout << "\npop" << std::endl;
    output_bool = stack_data.pop();
    std::cout << "  出力値: " << std::boolalpha << output_bool << std::endl;

    std::cout << "\npeek" << std::endl;
    output_int = stack_data.peek();
    std::cout << "  出力値: " << output_int << std::endl;

    std::cout << "\nStack TEST <----- end" << std::endl;
    
    return 0;
}