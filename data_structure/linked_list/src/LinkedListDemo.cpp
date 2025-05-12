// C++
// データ構造: 連結リスト (Linked List)

#include <iostream>
#include <vector>
#include <stdexcept>

template <typename T>
class NodeData {
private:
    T _data;
    NodeData* _next;

public:
    NodeData(T data) : _data(data), _next(nullptr) {}

    T get() const { return _data; }
    NodeData* get_next() const { return _next; }
    
    void set_data(T data) { _data = data; }
    void set_next(NodeData* next) { _next = next; }
};

template <typename T>
class LinkedListData {
private:
    NodeData<T>* _data;
    size_t _size;

public:
    LinkedListData() : _data(nullptr), _size(0) {}

    ~LinkedListData() {
        clear();
    }

    NodeData<T>* get() const { return _data; }

    int get_position(const T& data) const {
        if (is_empty()) return -1;
        
        NodeData<T>* current = _data;
        int position = 0;
        
        while (current) {
            if (current->get() == data) return position;
            current = current->get_next();
            position++;
        }
        
        return -1;
    }

    T get_value(size_t position) const {
        if (is_empty() || position < 0 || position >= _size) {
            throw std::out_of_range("Position is out of range");
        }
        
        NodeData<T>* current = _data;
        for (size_t i = 0; i < position; i++) {
            current = current->get_next();
        }
        
        return current->get();
    }

    bool add(const T& data, size_t position = -1) {
        NodeData<T>* new_node = new NodeData<T>(data);
        
        if (is_empty()) {
            _data = new_node;
            _size++;
            return true;
        }
        
        if (position == -1 || position >= _size) {
            NodeData<T>* current = _data;
            while (current->get_next()) {
                current = current->get_next();
            }
            current->set_next(new_node);
            _size++;
            return true;
        }
        
        if (position == 0) {
            new_node->set_next(_data);
            _data = new_node;
            _size++;
            return true;
        }
        
        NodeData<T>* current = _data;
        for (size_t i = 0; i < position - 1; i++) {
            current = current->get_next();
        }
        
        new_node->set_next(current->get_next());
        current->set_next(new_node);
        _size++;
        return true;
    }

    bool remove(size_t position = -1, const T* data = nullptr) {
        if (is_empty()) {
            std::cerr << "ERROR: List is empty" << std::endl;
            return false;
        }
        
        if (data != nullptr) {
            if (_data->get() == *data) {
                NodeData<T>* temp = _data;
                _data = _data->get_next();
                delete temp;
                _size--;
                return true;
            }
            
            NodeData<T>* current = _data;
            while (current->get_next() && current->get_next()->get() != *data) {
                current = current->get_next();
            }
            
            if (current->get_next()) {
                NodeData<T>* temp = current->get_next();
                current->set_next(current->get_next()->get_next());
                delete temp;
                _size--;
                return true;
            } else {
                std::cerr << "ERROR: " << *data << " not found" << std::endl;
                return false;
            }
        }
        
        if (position == -1) position = _size - 1;
        
        if (position < 0 || position >= _size) {
            std::cerr << "ERROR: Position " << position << " is out of range" << std::endl;
            return false;
        }
        
        if (position == 0) {
            NodeData<T>* temp = _data;
            _data = _data->get_next();
            delete temp;
            _size--;
            return true;
        }
        
        NodeData<T>* current = _data;
        for (size_t i = 0; i < position - 1; i++) {
            current = current->get_next();
        }
        
        NodeData<T>* temp = current->get_next();
        current->set_next(current->get_next()->get_next());
        delete temp;
        _size--;
        return true;
    }

    bool update(size_t position, const T& data) {
        if (is_empty() || position < 0 || position >= _size) {
            std::cerr << "ERROR: Position " << position << " is out of range" << std::endl;
            return false;
        }
        
        NodeData<T>* current = _data;
        for (size_t i = 0; i < position; i++) {
            current = current->get_next();
        }
        
        current->set_data(data);
        return true;
    }

    bool is_empty() const { return _data == nullptr; }

    size_t size() const { return _size; }

    bool clear() {
        while (_data) {
            NodeData<T>* temp = _data;
            _data = _data->get_next();
            delete temp;
        }
        _size = 0;
        return true;
    }
    
    std::vector<T> display() const {
        std::vector<T> elements;
        NodeData<T>* current = _data;
        while (current) {
            elements.push_back(current->get());
            current = current->get_next();
        }
        return elements;
    }
};

void print_vector(const std::vector<int>& vec) {
    std::cout << "  Current data: [";
    for (size_t i = 0; i < vec.size(); ++i) {
        std::cout << vec[i];
        if (i < vec.size() - 1) std::cout << ", ";
    }
    std::cout << "]" << std::endl;
}

int main() {
    std::cout << "LinkedList TEST -----> start" << std::endl;

    std::cout << "\nnew" << std::endl;
    LinkedListData<int> linked_list_data;
    print_vector(linked_list_data.display());

    std::cout << "\nis_empty" << std::endl;
    std::cout << "  Output value: " << std::boolalpha << linked_list_data.is_empty() << std::endl;

    std::cout << "\nsize" << std::endl;
    std::cout << "  Output value: " << linked_list_data.size() << std::endl;

    std::cout << "\nadd" << std::endl;
    int input = 10;
    std::cout << "  Input value: " << input << std::endl;
    bool output = linked_list_data.add(input);
    std::cout << "  Output value: " << std::boolalpha << output << std::endl;
    print_vector(linked_list_data.display());

    std::cout << "\nadd" << std::endl;
    input = 20;
    std::cout << "  Input value: " << input << std::endl;
    output = linked_list_data.add(input);
    std::cout << "  Output value: " << std::boolalpha << output << std::endl;
    print_vector(linked_list_data.display());

    std::cout << "\nadd" << std::endl;
    output = linked_list_data.add(5, 0);
    std::cout << "  Input value: (5, 0)" << std::endl;
    std::cout << "  Output value: " << std::boolalpha << output << std::endl;
    print_vector(linked_list_data.display());

    std::cout << "\nadd" << std::endl;
    output = linked_list_data.add(15, 2);
    std::cout << "  Input value: (15, 2)" << std::endl;
    std::cout << "  Output value: " << std::boolalpha << output << std::endl;
    print_vector(linked_list_data.display());

    std::cout << "\nget_position" << std::endl;
    input = 1;
    std::cout << "  Input value: " << input << std::endl;
    int pos_output = linked_list_data.get_position(input);
    std::cout << "  Output value: " << pos_output << std::endl;

    std::cout << "\nget_position" << std::endl;
    input = 10;
    std::cout << "  Input value: " << input << std::endl;
    pos_output = linked_list_data.get_position(input);
    std::cout << "  Output value: " << pos_output << std::endl;

    std::cout << "\nupdate" << std::endl;
    output = linked_list_data.update(1, 99);
    std::cout << "  Input value: (1, 99)" << std::endl;
    std::cout << "  Output value: " << std::boolalpha << output << std::endl;
    print_vector(linked_list_data.display());

    std::cout << "\nget_value" << std::endl;
    try {
        int val_output = linked_list_data.get_value(2);
        std::cout << "  Input value: 2" << std::endl;
        std::cout << "  Output value: " << val_output << std::endl;
    } catch (const std::out_of_range& e) {
        std::cout << "  Error: " << e.what() << std::endl;
    }

    std::cout << "\nget_value" << std::endl;
    try {
        int val_output = linked_list_data.get_value(100);
        std::cout << "  Input value: 100" << std::endl;
        std::cout << "  Output value: " << val_output << std::endl;
    } catch (const std::out_of_range& e) {
        std::cout << "  Error: " << e.what() << std::endl;
    }

    std::cout << "\nremove" << std::endl;
    int remove_data = 15;
    output = linked_list_data.remove(-1, &remove_data);
    std::cout << "  Input value: data=" << remove_data << std::endl;
    std::cout << "  Output value: " << std::boolalpha << output << std::endl;
    print_vector(linked_list_data.display());

    std::cout << "\nremove" << std::endl;
    output = linked_list_data.remove(0);
    std::cout << "  Input value: position=0" << std::endl;
    std::cout << "  Output value: " << std::boolalpha << output << std::endl;
    print_vector(linked_list_data.display());

    std::cout << "\nremove" << std::endl;
    output = linked_list_data.remove();
    std::cout << "  Output value: " << std::boolalpha << output << std::endl;
    print_vector(linked_list_data.display());

    std::cout << "\nremove" << std::endl;
    try {
        output = linked_list_data.remove(5);
        std::cout << "  Input value: position=5" << std::endl;
        std::cout << "  Output value: " << std::boolalpha << output << std::endl;
        print_vector(linked_list_data.display());
    } catch (const std::out_of_range& e) {
        std::cout << "  Error: " << e.what() << std::endl;
    }

    std::cout << "\nclear" << std::endl;
    output = linked_list_data.clear();
    std::cout << "  Output value: " << std::boolalpha << output << std::endl;
    print_vector(linked_list_data.display());

    std::cout << "\nis_empty" << std::endl;
    std::cout << "  Output value: " << std::boolalpha << linked_list_data.is_empty() << std::endl;

    std::cout << "\nsize" << std::endl;
    std::cout << "  Output value: " << linked_list_data.size() << std::endl;

    std::cout << "\nremove" << std::endl;
    output = linked_list_data.remove();
    std::cout << "  Output value: " << std::boolalpha << output << std::endl;
    print_vector(linked_list_data.display());

    std::cout << "\nLinkedList TEST <----- end" << std::endl;

    return 0;
}