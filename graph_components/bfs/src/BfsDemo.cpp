// C++
// グラフの連結成分: BFS

#include <iostream>
#include <vector>
#include <unordered_map>
#include <unordered_set>
#include <queue>
#include <algorithm>
#include <tuple>

class GraphData {
private:
    // 隣接リストの型定義
    // キーは頂点、値は<隣接頂点, 重み>のペアのベクター
    std::unordered_map<std::string, std::vector<std::pair<std::string, int>>> _data;

public:
    // グラフの内部データを取得
    const std::unordered_map<std::string, std::vector<std::pair<std::string, int>>>& get() const {
        return _data;
    }

    // グラフの全頂点をベクターとして返す
    std::vector<std::string> get_vertices() const {
        std::vector<std::string> vertices;
        for (const auto& pair : _data) {
            vertices.push_back(pair.first);
        }
        return vertices;
    }

    // 指定された頂点の隣接ノードと辺の重みのベクターを返す
    const std::vector<std::pair<std::string, int>>* get_neighbors(const std::string& vertex) const {
        auto it = _data.find(vertex);
        return (it != _data.end()) ? &(it->second) : nullptr;
    }

    // 頂点がグラフに存在するか確認する
    bool get_vertice(const std::string& vertex) const {
        return _data.find(vertex) != _data.end();
    }

    // 新しい頂点をグラフに追加
    bool add_vertex(const std::string& vertex) {
        if (_data.find(vertex) == _data.end()) {
            _data[vertex] = std::vector<std::pair<std::string, int>>();
        }
        return true;
    }

    // 両頂点間に辺を追加
    bool add_edge(const std::string& vertex1, const std::string& vertex2, int weight) {
        // 頂点を追加（存在しない場合）
        add_vertex(vertex1);
        add_vertex(vertex2);

        // vertex1 -> vertex2 の辺を追加
        bool edge_exists_v1v2 = false;
        for (auto& neighbor : _data[vertex1]) {
            if (neighbor.first == vertex2) {
                neighbor.second = weight;
                edge_exists_v1v2 = true;
                break;
            }
        }
        if (!edge_exists_v1v2) {
            _data[vertex1].push_back({vertex2, weight});
        }

        // vertex2 -> vertex1 の辺を追加
        bool edge_exists_v2v1 = false;
        for (auto& neighbor : _data[vertex2]) {
            if (neighbor.first == vertex1) {
                neighbor.second = weight;
                edge_exists_v2v1 = true;
                break;
            }
        }
        if (!edge_exists_v2v1) {
            _data[vertex2].push_back({vertex1, weight});
        }

        return true;
    }

    // グラフを空にする
    bool clear() {
        _data.clear();
        return true;
    }

    // グラフの連結成分をBFSを使用して見つける
    std::vector<std::vector<std::string>> get_connected_components() {
        std::unordered_set<std::string> visited;
        std::vector<std::vector<std::string>> all_components;

        // グラフのすべての頂点を取得
        std::vector<std::string> vertices = get_vertices();

        // すべての頂点を順番にチェック
        for (const std::string& vertex : vertices) {
            // もしその頂点がまだ訪問されていなければ、新しい連結成分の開始点
            if (visited.find(vertex) == visited.end()) {
                std::vector<std::string> current_component;
                std::queue<std::string> queue;
                queue.push(vertex);
                visited.insert(vertex);
                current_component.push_back(vertex);

                // BFSを開始
                while (!queue.empty()) {
                    std::string u = queue.front();
                    queue.pop();

                    // 取り出した頂点の隣接リストを取得
                    const auto* neighbors_with_weight = get_neighbors(u);

                    // 頂点が存在し、隣接ノードがある場合
                    if (neighbors_with_weight != nullptr) {
                        for (const auto& neighbor_pair : *neighbors_with_weight) {
                            // 隣接する頂点がまだ訪問されていなければ
                            if (visited.find(neighbor_pair.first) == visited.end()) {
                                visited.insert(neighbor_pair.first);
                                queue.push(neighbor_pair.first);
                                current_component.push_back(neighbor_pair.first);
                            }
                        }
                    }
                }

                // BFSが終了したら、1つの連結成分が見つかった
                all_components.push_back(current_component);
            }
        }

        return all_components;
    }
};

void print_vector(const std::vector<std::string>& vec) {
    std::cout << "[";
    for (size_t i = 0; i < vec.size(); ++i) {
        std::cout << vec[i];
        if (i < vec.size() - 1) std::cout << ", ";
    }
    std::cout << "]";
}

void print_connected_components(const std::vector<std::vector<std::string>>& components) {
    std::cout << "[";
    for (size_t i = 0; i < components.size(); ++i) {
        print_vector(components[i]);
        if (i < components.size() - 1) std::cout << ", ";
    }
    std::cout << "]";
}

int main() {
    std::cout << "Bfs TEST -----> start" << std::endl;

    std::cout << "\nnew" << std::endl;
    GraphData graph_data;
    std::cout << "  現在のデータ: {}" << std::endl;

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
        std::cout << "  出力値: " << (output ? "true" : "false") << std::endl;
    }
    std::cout << "  現在のデータ: { ... }" << std::endl;
    std::cout << "\nget_connected_components" << std::endl;
    auto output = graph_data.get_connected_components();
    std::cout << "  連結成分: ";
    print_connected_components(output);
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
        std::cout << "  出力値: " << (output ? "true" : "false") << std::endl;
    }
    std::cout << "  現在のデータ: { ... }" << std::endl;
    std::cout << "\nget_connected_components" << std::endl;
    output = graph_data.get_connected_components();
    std::cout << "  連結成分: ";
    print_connected_components(output);
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
        std::cout << "  出力値: " << (output ? "true" : "false") << std::endl;
    }
    std::cout << "  現在のデータ: { ... }" << std::endl;
    std::cout << "\nget_connected_components" << std::endl;
    output = graph_data.get_connected_components();
    std::cout << "  連結成分: ";
    print_connected_components(output);
    std::cout << std::endl;

    std::cout << "\nadd_edge" << std::endl;
    graph_data.clear();
    inputList = {};
    std::cout << "  現在のデータ: { ... }" << std::endl;
    std::cout << "\nget_connected_components" << std::endl;
    output = graph_data.get_connected_components();
    std::cout << "  連結成分: ";
    print_connected_components(output);
    std::cout << std::endl;

    std::cout << "Bfs TEST <----- end" << std::endl;

    return 0;
}
