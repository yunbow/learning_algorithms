// C++
// データ構造: 木 (Tree)

#include <iostream>
#include <vector>
#include <queue>
#include <algorithm>
#include <string>

class NodeData {
private:
    std::string _value;
    NodeData* _parent;
    std::vector<NodeData*> _children;

public:
    NodeData(const std::string& value) : _value(value), _parent(nullptr) {}

    std::string get_value() const {
        return _value;
    }

    NodeData* get_parent() const {
        return _parent;
    }

    std::vector<NodeData*> get_children() const {
        return _children;
    }

    bool set_parent(NodeData* parent) {
        _parent = parent;
        return true;
    }

    bool add_child(NodeData* child) {
        child->set_parent(this);
        _children.push_back(child);
        return true;
    }

    bool remove_child(NodeData* child) {
        auto it = std::find(_children.begin(), _children.end(), child);
        if (it != _children.end()) {
            child->set_parent(nullptr);
            _children.erase(it);
            return true;
        } else {
            return false;
        }
    }

    bool is_leaf() const {
        return _children.empty();
    }
};

class TreeData {
private:
    NodeData* _data;

public:
    TreeData() : _data(nullptr) {}

    ~TreeData() {
        clear();
    }

    NodeData* get() const {
        return _data;
    }

    int get_height(NodeData* node = nullptr) const {
        if (node == nullptr) {
            node = _data;
        }
        if (node == nullptr) {
            return 0;
        }
        if (node->is_leaf()) {
            return 1;
        }
        
        int max_height = 0;
        for (auto* child : node->get_children()) {
            max_height = std::max(max_height, get_height(child));
        }
        return 1 + max_height;
    }

    NodeData* get_parent(NodeData* node) const {
        return node->get_parent();
    }

    std::vector<NodeData*> get_children(NodeData* node) const {
        return node->get_children();
    }

    NodeData* get_node(const std::string& value, NodeData* node = nullptr) const {
        if (node == nullptr) {
            node = _data;
        }
        if (node == nullptr) {
            return nullptr;
        }

        if (node->get_value() == value) {
            return node;
        }

        for (auto* child : node->get_children()) {
            NodeData* result = get_node(value, child);
            if (result != nullptr) {
                return result;
            }
        }

        return nullptr;
    }

    bool add(NodeData* parent, const std::string& value) {
        NodeData* new_node = new NodeData(value);
        if (parent == nullptr) {
            if (_data == nullptr) {
                _data = new_node;
                return true;
            } else {
                std::cout << "ERROR: " << value << " 重複です" << std::endl;
                delete new_node;
                return false;
            }
        } else {
            parent->add_child(new_node);
            return true;
        }
    }

    bool remove(NodeData* node) {
        if (node == nullptr) {
            return false;
        }

        if (node == _data) {
            delete _data;
            _data = nullptr;
            return true;
        }

        NodeData* parent = node->get_parent();
        if (parent != nullptr) {
            bool result = parent->remove_child(node);
            delete node;
            return result;
        }

        return false;
    }

    std::vector<std::string> traverse(NodeData* node = nullptr, const std::string& mode = "pre-order") const {
        if (node == nullptr) {
            node = _data;
        }
        if (node == nullptr) {
            return {};
        }

        std::vector<std::string> result;

        if (mode == "pre-order") {
            result.push_back(node->get_value());
            for (auto* child : node->get_children()) {
                std::vector<std::string> child_result = traverse(child, mode);
                result.insert(result.end(), child_result.begin(), child_result.end());
            }
        } else if (mode == "post-order") {
            for (auto* child : node->get_children()) {
                std::vector<std::string> child_result = traverse(child, mode);
                result.insert(result.end(), child_result.begin(), child_result.end());
            }
            result.push_back(node->get_value());
        } else if (mode == "level-order") {
            std::queue<NodeData*> q;
            q.push(node);
            while (!q.empty()) {
                NodeData* current = q.front();
                q.pop();
                result.push_back(current->get_value());
                for (auto* child : current->get_children()) {
                    q.push(child);
                }
            }
        }

        return result;
    }

    bool is_leaf(NodeData* node) const {
        return node != nullptr && node->is_leaf();
    }

    bool is_empty() const {
        return _data == nullptr;
    }

    int size(NodeData* node = nullptr) const {
        if (node == nullptr) {
            node = _data;
        }
        if (node == nullptr) {
            return 0;
        }

        int count = 1;
        for (auto* child : node->get_children()) {
            count += size(child);
        }

        return count;
    }

    bool clear() {
        if (_data != nullptr) {
            clear_recursive(_data);
            _data = nullptr;
        }
        return true;
    }

    void clear_recursive(NodeData* node) {
        if (node == nullptr) return;
        
        for (auto* child : node->get_children()) {
            clear_recursive(child);
        }
        delete node;
    }

    std::vector<std::string> display() const {
        if (_data == nullptr) {
            return {};
        }

        return traverse(_data, "level-order");
    }
};

int main() {
    std::cout << "Tree TEST -----> start" << std::endl;

    std::cout << "\nnew" << std::endl;
    TreeData tree_data;
    std::cout << "  現在のデータ: ";
    for (const auto& val : tree_data.display()) {
        std::cout << val << " ";
    }
    std::cout << std::endl;

    std::cout << "\nis_empty" << std::endl;
    bool output_empty = tree_data.is_empty();
    std::cout << "  出力値: " << (output_empty ? "true" : "false") << std::endl;

    std::cout << "\nsize" << std::endl;
    int output_size = tree_data.size();
    std::cout << "  出力値: " << output_size << std::endl;

    std::cout << "\nadd" << std::endl;
    std::cout << "  入力値: (nullptr, Root)" << std::endl;
    bool output_add = tree_data.add(nullptr, "Root");
    std::cout << "  出力値: " << (output_add ? "true" : "false") << std::endl;
    std::cout << "  現在のデータ: ";
    for (const auto& val : tree_data.display()) {
        std::cout << val << " ";
    }
    std::cout << std::endl;

    NodeData* root_node = tree_data.get();

    std::cout << "\nadd" << std::endl;
    std::cout << "  入力値: (root_node, Child1)" << std::endl;
    output_add = tree_data.add(root_node, "Child1");
    std::cout << "  出力値: " << (output_add ? "true" : "false") << std::endl;
    std::cout << "  現在のデータ: ";
    for (const auto& val : tree_data.display()) {
        std::cout << val << " ";
    }
    std::cout << std::endl;

    std::cout << "\nadd" << std::endl;
    std::cout << "  入力値: (root_node, Child2)" << std::endl;
    output_add = tree_data.add(root_node, "Child2");
    std::cout << "  出力値: " << (output_add ? "true" : "false") << std::endl;
    std::cout << "  現在のデータ: ";
    for (const auto& val : tree_data.display()) {
        std::cout << val << " ";
    }
    std::cout << std::endl;

    std::cout << "\nget_node" << std::endl;
    std::string input_value = "Child1";
    std::cout << "  入力値: " << input_value << std::endl;
    NodeData* output_node = tree_data.get_node(input_value);
    std::cout << "  出力値: " << output_node << std::endl;
    std::cout << "  現在のデータ: ";
    for (const auto& val : tree_data.display()) {
        std::cout << val << " ";
    }
    std::cout << std::endl;

    std::cout << "\ntraverse" << std::endl;
    std::string input_mode = "pre-order";
    std::cout << "  入力値: " << input_mode << std::endl;
    std::vector<std::string> output_traverse = tree_data.traverse(nullptr, input_mode);
    std::cout << "  出力値: ";
    for (const auto& val : output_traverse) {
        std::cout << val << " ";
    }
    std::cout << std::endl;
    std::cout << "  現在のデータ: ";
    for (const auto& val : tree_data.display()) {
        std::cout << val << " ";
    }
    std::cout << std::endl;

    std::cout << "\nTree TEST <----- end" << std::endl;

    return 0;
}