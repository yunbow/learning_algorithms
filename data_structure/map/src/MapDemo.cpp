// C++
// データ構造: マップ (Map)

#include <iostream>
#include <map>
#include <vector>
#include <string>
#include <optional>

class MapData {
private:
    std::map<std::string, int> _data;

public:
    std::vector<std::pair<std::string, int>> get() const {
        return std::vector<std::pair<std::string, int>>(_data.begin(), _data.end());
    }
    
    std::vector<std::string> get_keys() const {
        std::vector<std::string> keys;
        for (const auto& pair : _data) {
            keys.push_back(pair.first);
        }
        return keys;
    }
    
    std::vector<int> get_values() const {
        std::vector<int> values;
        for (const auto& pair : _data) {
            values.push_back(pair.second);
        }
        return values;
    }
    
    std::optional<std::string> get_key(int value) const {
        for (const auto& pair : _data) {
            if (pair.second == value) {
                return pair.first;
            }
        }
        std::cout << "ERROR: " << value << " は範囲外です" << std::endl;
        return std::nullopt;
    }
    
    std::optional<int> get_value(const std::string& key) const {
        auto it = _data.find(key);
        if (it != _data.end()) {
            return it->second;
        }
        std::cout << "ERROR: " << key << " は範囲外です" << std::endl;
        return std::nullopt;
    }

    bool add(const std::string& key, int value) {
        if (_data.find(key) != _data.end()) {
            std::cout << "ERROR: " << key << " は重複です" << std::endl;
            return false;
        }
        _data[key] = value;
        return true;
    }
    
    bool remove(const std::string& key) {
        auto it = _data.find(key);
        if (it != _data.end()) {
            _data.erase(it);
            return true;
        }
        std::cout << "ERROR: " << key << " は範囲外です" << std::endl;
        return false;
    }
    
    bool update(const std::string& key, int value) {
        auto it = _data.find(key);
        if (it != _data.end()) {
            it->second = value;
            return true;
        }
        std::cout << "ERROR: " << key << " は範囲外です" << std::endl;
        return false;
    }
    
    bool is_empty() const {
        return _data.empty();
    }
    
    size_t size() const {
        return _data.size();
    }
    
    bool clear() {
        _data.clear();
        return true;
    }
};

void print_vector(const std::vector<std::pair<std::string, int>>& vec) {
    std::cout << "[";
    for (size_t i = 0; i < vec.size(); ++i) {
        std::cout << "(" << vec[i].first << ", " << vec[i].second << ")";
        if (i < vec.size() - 1) std::cout << ", ";
    }
    std::cout << "]" << std::endl;
}

void print_string_vector(const std::vector<std::string>& vec) {
    std::cout << "[";
    for (size_t i = 0; i < vec.size(); ++i) {
        std::cout << vec[i];
        if (i < vec.size() - 1) std::cout << ", ";
    }
    std::cout << "]" << std::endl;
}

void print_int_vector(const std::vector<int>& vec) {
    std::cout << "[";
    for (size_t i = 0; i < vec.size(); ++i) {
        std::cout << vec[i];
        if (i < vec.size() - 1) std::cout << ", ";
    }
    std::cout << "]" << std::endl;
}

int main() {
    std::cout << "Map TEST -----> start" << std::endl;

    std::cout << "\nnew" << std::endl;
    MapData map_data;
    std::cout << "  現在のデータ: ";
    print_vector(map_data.get());

    std::cout << "\nis_empty" << std::endl;
    bool output_bool = map_data.is_empty();
    std::cout << "  出力値: " << std::boolalpha << output_bool << std::endl;

    std::cout << "\nsize" << std::endl;
    size_t output_size = map_data.size();
    std::cout << "  出力値: " << output_size << std::endl;

    std::cout << "\nadd" << std::endl;
    std::pair<std::string, int> input = {"apple", 100};
    std::cout << "  入力値: (" << input.first << ", " << input.second << ")" << std::endl;
    bool output = map_data.add(input.first, input.second);
    std::cout << "  出力値: " << std::boolalpha << output << std::endl;
    std::cout << "  現在のデータ: ";
    print_vector(map_data.get());

    std::cout << "\nadd" << std::endl;
    input = {"banana", 150};
    std::cout << "  入力値: (" << input.first << ", " << input.second << ")" << std::endl;
    output = map_data.add(input.first, input.second);
    std::cout << "  出力値: " << std::boolalpha << output << std::endl;
    std::cout << "  現在のデータ: ";
    print_vector(map_data.get());

    std::cout << "\nadd" << std::endl;
    input = {"apple", 200};
    std::cout << "  入力値: (" << input.first << ", " << input.second << ")" << std::endl;
    output = map_data.add(input.first, input.second);
    std::cout << "  出力値: " << std::boolalpha << output << std::endl;
    std::cout << "  現在のデータ: ";
    print_vector(map_data.get());

    std::cout << "\nsize" << std::endl;
    output_size = map_data.size();
    std::cout << "  出力値: " << output_size << std::endl;

    std::cout << "\nget" << std::endl;
    std::string input_str = "apple";
    std::cout << "  入力値: " << input_str << std::endl;
    auto output_opt = map_data.get_value(input_str);
    std::cout << "  出力値: " << (output_opt ? std::to_string(*output_opt) : "nullopt") << std::endl;

    std::cout << "\nget" << std::endl;
    input_str = "orange";
    std::cout << "  入力値: " << input_str << std::endl;
    output_opt = map_data.get_value(input_str);
    std::cout << "  出力値: " << (output_opt ? std::to_string(*output_opt) : "nullopt") << std::endl;

    std::cout << "\nupdate" << std::endl;
    input = {"banana", 180};
    std::cout << "  入力値: (" << input.first << ", " << input.second << ")" << std::endl;
    output = map_data.update(input.first, input.second);
    std::cout << "  出力値: " << std::boolalpha << output << std::endl;
    std::cout << "  現在のデータ: ";
    print_vector(map_data.get());

    std::cout << "\nupdate" << std::endl;
    input = {"orange", 250};
    std::cout << "  入力値: (" << input.first << ", " << input.second << ")" << std::endl;
    output = map_data.update(input.first, input.second);
    std::cout << "  出力値: " << std::boolalpha << output << std::endl;
    std::cout << "  現在のデータ: ";
    print_vector(map_data.get());

    std::cout << "\nget" << std::endl;
    input_str = "banana";
    output_opt = map_data.get_value(input_str);
    std::cout << "  出力値: " << (output_opt ? std::to_string(*output_opt) : "nullopt") << std::endl;

    std::cout << "\nget_keys" << std::endl;
    auto output_keys = map_data.get_keys();
    std::cout << "  出力値: ";
    print_string_vector(output_keys);

    std::cout << "\nvalues" << std::endl;
    auto output_values = map_data.get_values();
    std::cout << "  出力値: ";
    print_int_vector(output_values);

    std::cout << "\nget_key" << std::endl;
    int input_int = 180;
    std::cout << "  入力値: " << input_int << std::endl;
    auto output_key = map_data.get_key(input_int);
    std::cout << "  出力値: " << (output_key ? *output_key : "nullopt") << std::endl;

    std::cout << "\nget_key" << std::endl;
    input_int = 500;
    std::cout << "  入力値: " << input_int << std::endl;
    output_key = map_data.get_key(input_int);
    std::cout << "  出力値: " << (output_key ? *output_key : "nullopt") << std::endl;

    std::cout << "\nremove" << std::endl;
    input_str = "apple";
    std::cout << "  入力値: " << input_str << std::endl;
    output = map_data.remove(input_str);
    std::cout << "  出力値: " << std::boolalpha << output << std::endl;
    std::cout << "  現在のデータ: ";
    print_vector(map_data.get());

    std::cout << "\nremove" << std::endl;
    input_str = "orange";
    std::cout << "  入力値: " << input_str << std::endl;
    output = map_data.remove(input_str);
    std::cout << "  出力値: " << std::boolalpha << output << std::endl;
    std::cout << "  現在のデータ: ";
    print_vector(map_data.get());

    std::cout << "\nsize" << std::endl;
    output_size = map_data.size();
    std::cout << "  出力値: " << output_size << std::endl;

    std::cout << "\nget_keys" << std::endl;
    output_keys = map_data.get_keys();
    std::cout << "  出力値: ";
    print_string_vector(output_keys);

    std::cout << "\nclear" << std::endl;
    output = map_data.clear();
    std::cout << "  出力値: " << std::boolalpha << output << std::endl;
    std::cout << "  現在のデータ: ";
    print_vector(map_data.get());

    std::cout << "\nsize" << std::endl;
    output_size = map_data.size();
    std::cout << "  出力値: " << output_size << std::endl;

    std::cout << "\nis_empty" << std::endl;
    output_bool = map_data.is_empty();
    std::cout << "  出力値: " << std::boolalpha << output_bool << std::endl;

    std::cout << "\nMap TEST <----- end" << std::endl;

    return 0;
}
