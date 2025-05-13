// C++
// グラフの連結成分: DFS

#include <iostream>
#include <unordered_map>
#include <vector>
#include <set>
#include <algorithm>
#include <stdexcept>

class GraphData {
private:
    // キーは頂点、値は隣接頂点と重みのペアのベクタ
    std::unordered_map<std::string, std::vector<std::pair<std::string, int>>> _data;

    void _dfs(const std::string& vertex, std::set<std::string>& visited, std::vector<std::string>& current_component) {
        visited.insert(vertex);
        current_component.push_back(vertex);

        // 隣接頂点を探索
        for (const auto& neighbor_info : _data[vertex]) {
            const std::string& neighbor_vertex = neighbor_info.first;
            if (visited.find(neighbor_vertex) == visited.end()) {
                _dfs(neighbor_vertex, visited, current_component);
            }
        }
    }

public:
    std::unordered_map<std::string, std::vector<std::pair<std::string, int>>> get() const {
        return _data;
    }

    std::vector<std::string> get_vertices() const {
        std::vector<std::string> vertices;
        for (const auto& pair : _data) {
            vertices.push_back(pair.first);
        }
        return vertices;
    }

    std::vector<std::tuple<std::string, std::string, int>> get_edges() const {
        std::set<std::tuple<std::string, std::string, int>> edges;
        for (const auto& vertex : _data) {
            for (const auto& neighbor : vertex.second) {
                auto edge = std::make_tuple(
                    std::min(vertex.first, neighbor.first),
                    std::max(vertex.first, neighbor.first),
                    neighbor.second
                );
                edges.insert(edge);
            }
        }
        return std::vector<std::tuple<std::string, std::string, int>>(edges.begin(), edges.end());
    }

    std::vector<std::pair<std::string, int>>* get_neighbors(const std::string& vertex) {
        auto it = _data.find(vertex);
        return (it != _data.end()) ? &(it->second) : nullptr;
    }

    int* get_edge_weight(const std::string& vertex1, const std::string& vertex2) {
        auto it1 = _data.find(vertex1);
        if (it1 != _data.end()) {
            for (const auto& neighbor : it1->second) {
                if (neighbor.first == vertex2) {
                    return new int(neighbor.second);
                }
            }
        }
        return nullptr;
    }

    std::vector<std::pair<std::string, int>>* get_vertice(const std::string& vertex) {
        auto it = _data.find(vertex);
        if (it != _data.end()) {
            return &(it->second);
        }
        std::cout << "ERROR: " << vertex << "は範囲外です" << std::endl;
        return nullptr;
    }

    bool get_edge(const std::string& vertex1, const std::string& vertex2) {
        auto it1 = _data.find(vertex1);
        if (it1 != _data.end()) {
            for (const auto& neighbor : it1->second) {
                if (neighbor.first == vertex2) {
                    return true;
                }
            }
        }
        return false;
    }

    bool add_vertex(const std::string& vertex) {
        if (_data.find(vertex) == _data.end()) {
            _data[vertex] = {};
        }
        return true;
    }

    bool add_edge(const std::string& vertex1, const std::string& vertex2, int weight) {
        // 頂点が存在しない場合は追加
        add_vertex(vertex1);
        add_vertex(vertex2);

        // vertex1 -> vertex2の辺を追加
        bool edge_exists_v1v2 = false;
        for (auto& neighbor : _data[vertex1]) {
            if (neighbor.first == vertex2) {
                neighbor.second = weight;
                edge_exists_v1v2 = true;
                break;
            }
        }
        if (!edge_exists_v1v2) {
            _data[vertex1].emplace_back(vertex2, weight);
        }

        // vertex2 -> vertex1の辺を追加
        bool edge_exists_v2v1 = false;
        for (auto& neighbor : _data[vertex2]) {
            if (neighbor.first == vertex1) {
                neighbor.second = weight;
                edge_exists_v2v1 = true;
                break;
            }
        }
        if (!edge_exists_v2v1) {
            _data[vertex2].emplace_back(vertex1, weight);
        }

        return true;
    }

    bool remove_vertex(const std::string& vertex) {
        if (_data.find(vertex) != _data.end()) {
            // 他の頂点の隣接リストから参照を削除
            for (auto& v : _data) {
                v.second.erase(
                    std::remove_if(v.second.begin(), v.second.end(),
                        [&vertex](const std::pair<std::string, int>& p) { return p.first == vertex; }),
                    v.second.end()
                );
            }
            // 頂点を削除
            _data.erase(vertex);
            return true;
        }
        std::cout << "ERROR: " << vertex << " は範囲外です" << std::endl;
        return false;
    }

    bool remove_edge(const std::string& vertex1, const std::string& vertex2) {
        if (_data.find(vertex1) != _data.end() && _data.find(vertex2) != _data.end()) {
            bool removed = false;

            // vertex1 から vertex2 への辺を削除
            size_t orig_size_v1 = _data[vertex1].size();
            _data[vertex1].erase(
                std::remove_if(_data[vertex1].begin(), _data[vertex1].end(),
                    [&vertex2](const std::pair<std::string, int>& p) { return p.first == vertex2; }),
                _data[vertex1].end()
            );
            if (_data[vertex1].size() < orig_size_v1) {
                removed = true;
            }

            // vertex2 から vertex1 への辺を削除
            size_t orig_size_v2 = _data[vertex2].size();
            _data[vertex2].erase(
                std::remove_if(_data[vertex2].begin(), _data[vertex2].end(),
                    [&vertex1](const std::pair<std::string, int>& p) { return p.first == vertex1; }),
                _data[vertex2].end()
            );
            if (_data[vertex2].size() < orig_size_v2) {
                removed = true;
            }

            return removed;
        }
        std::cout << "ERROR: " << vertex1 << " または " << vertex2 << " は範囲外です" << std::endl;
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

    std::vector<std::vector<std::string>> get_connected_components() {
        std::set<std::string> visited;
        std::vector<std::vector<std::string>> connected_components;

        for (const auto& vertex : get_vertices()) {
            if (visited.find(vertex) == visited.end()) {
                std::vector<std::string> current_component;
                _dfs(vertex, visited, current_component);
                connected_components.push_back(current_component);
            }
        }

        return connected_components;
    }
};

void print_graph_data(const GraphData& graph_data) {
    std::cout << "現在のデータ: {";
    for (const auto& vertex : graph_data.get()) {
        std::cout << vertex.first << ": [";
        for (const auto& neighbor : vertex.second) {
            std::cout << "(" << neighbor.first << ", " << neighbor.second << ") ";
        }
        std::cout << "], ";
    }
    std::cout << "}" << std::endl;
}

int main() {
    std::cout << "Dfs TEST -----> start" << std::endl;

    std::cout << "\nnew" << std::endl;
    GraphData graph_data;
    print_graph_data(graph_data);

    std::cout << "\nadd_edge" << std::endl;
    graph_data.clear();
    std::vector<std::tuple<std::string, std::string, int>> inputList = {
        {"A", "B", 4}, {"B", "C", 3}, {"B", "D", 2}, 
        {"D", "A", 1}, {"A", "C", 2}, {"B", "D", 2}
    };
    for (const auto& input : inputList) {
        std::cout << "  入力値: (" 
                  << std::get<0>(input) << ", " 
                  << std::get<1>(input) << ", " 
                  << std::get<2>(input) << ")" << std::endl;
        bool output = graph_data.add_edge(std::get<0>(input), std::get<1>(input), std::get<2>(input));
        std::cout << "  出力値: " << std::boolalpha << output << std::endl;
    }
    print_graph_data(graph_data);
    std::cout << "\nget_connected_components" << std::endl;
    auto output = graph_data.get_connected_components();
    std::cout << "  連結成分: ";
    for (const auto& component : output) {
        std::cout << "[ ";
        for (const auto& vertex : component) {
            std::cout << vertex << " ";
        }
        std::cout << "] ";
    }
    std::cout << std::endl;

    std::cout << "\nadd_edge" << std::endl;
    graph_data.clear();
    inputList = {{"A", "B", 4}, {"C", "D", 4}, {"E", "F", 1}, {"F", "G", 1}};
    for (const auto& input : inputList) {
        std::cout << "  入力値: (" 
                  << std::get<0>(input) << ", " 
                  << std::get<1>(input) << ", " 
                  << std::get<2>(input) << ")" << std::endl;
        bool output = graph_data.add_edge(std::get<0>(input), std::get<1>(input), std::get<2>(input));
        std::cout << "  出力値: " << std::boolalpha << output << std::endl;
    }
    print_graph_data(graph_data);
    std::cout << "\nget_connected_components" << std::endl;
    output = graph_data.get_connected_components();
    std::cout << "  連結成分: ";
    for (const auto& component : output) {
        std::cout << "[ ";
        for (const auto& vertex : component) {
            std::cout << vertex << " ";
        }
        std::cout << "] ";
    }
    std::cout << std::endl;

    std::cout << "\nadd_edge" << std::endl;
    graph_data.clear();
    inputList = {{"A", "B", 4}, {"B", "C", 3}, {"D", "E", 5}};
    for (const auto& input : inputList) {
        std::cout << "  入力値: (" 
                  << std::get<0>(input) << ", " 
                  << std::get<1>(input) << ", " 
                  << std::get<2>(input) << ")" << std::endl;
        bool output = graph_data.add_edge(std::get<0>(input), std::get<1>(input), std::get<2>(input));
        std::cout << "  出力値: " << std::boolalpha << output << std::endl;
    }
    print_graph_data(graph_data);
    std::cout << "\nget_connected_components" << std::endl;
    output = graph_data.get_connected_components();
    std::cout << "  連結成分: ";
    for (const auto& component : output) {
        std::cout << "[ ";
        for (const auto& vertex : component) {
            std::cout << vertex << " ";
        }
        std::cout << "] ";
    }
    std::cout << std::endl;

    std::cout << "\nadd_edge" << std::endl;
    graph_data.clear();
    inputList = {};
    for (const auto& input : inputList) {
        std::cout << "  入力値: (" 
                  << std::get<0>(input) << ", " 
                  << std::get<1>(input) << ", " 
                  << std::get<2>(input) << ")" << std::endl;
        bool output = graph_data.add_edge(std::get<0>(input), std::get<1>(input), std::get<2>(input));
        std::cout << "  出力値: " << std::boolalpha << output << std::endl;
    }
    print_graph_data(graph_data);
    std::cout << "\nget_connected_components" << std::endl;
    output = graph_data.get_connected_components();
    std::cout << "  連結成分: ";
    for (const auto& component : output) {
        std::cout << "[ ";
        for (const auto& vertex : component) {
            std::cout << vertex << " ";
        }
        std::cout << "] ";
    }
    std::cout << std::endl;

    std::cout << "Dfs TEST <----- end" << std::endl;

    return 0;
}
