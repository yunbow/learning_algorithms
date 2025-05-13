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

    // グラフの全辺をベクターとして返す
    std::vector<std::tuple<std::string, std::string, int>> get_edges() const {
        std::unordered_set<std::tuple<std::string, std::string, int>> unique_edges;
        for (const auto& vertex : _data) {
            for (const auto& neighbor : vertex.second) {
                // 辺を正規化して重複を避ける
                auto edge = std::make_tuple(
                    std::min(vertex.first, neighbor.first),
                    std::max(vertex.first, neighbor.first),
                    neighbor.second
                );
                unique_edges.insert(edge);
            }
        }
        return std::vector<std::tuple<std::string, std::string, int>>(
            unique_edges.begin(), unique_edges.end()
        );
    }

    // 指定された頂点の隣接ノードと辺の重みのベクターを返す
    const std::vector<std::pair<std::string, int>>* get_neighbors(const std::string& vertex) const {
        auto it = _data.find(vertex);
        return (it != _data.end()) ? &(it->second) : nullptr;
    }

    // 指定された2つの頂点間の辺の重みを返す
    int get_edge_weight(const std::string& vertex1, const std::string& vertex2) const {
        auto it1 = _data.find(vertex1);
        if (it1 != _data.end()) {
            for (const auto& neighbor : it1->second) {
                if (neighbor.first == vertex2) {
                    return neighbor.second;
                }
            }
        }
        return -1; // 辺が存在しない場合
    }

    // 頂点がグラフに存在するか確認する
    bool get_vertice(const std::string& vertex) const {
        return _data.find(vertex) != _data.end();
    }

    // 指定された2つの頂点間に辺が存在するかを確認する
    bool get_edge(const std::string& vertex1, const std::string& vertex2) const {
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

    // 頂点とそれに関連する辺を削除
    bool remove_vertex(const std::string& vertex) {
        if (_data.find(vertex) != _data.end()) {
            // この頂点への参照を他の頂点の隣接リストから削除する
            for (auto& pair : _data) {
                pair.second.erase(
                    std::remove_if(pair.second.begin(), pair.second.end(),
                        [&vertex](const std::pair<std::string, int>& neighbor) {
                            return neighbor.first == vertex;
                        }
                    ),
                    pair.second.end()
                );
            }
            // 頂点自体を削除する
            _data.erase(vertex);
            return true;
        }
        return false;
    }

    // 両頂点間の辺を削除
    bool remove_edge(const std::string& vertex1, const std::string& vertex2) {
        if (_data.find(vertex1) != _data.end() && _data.find(vertex2) != _data.end()) {
            bool removed = false;

            // vertex1 から vertex2 への辺を削除
            auto& neighbors1 = _data[vertex1];
            neighbors1.erase(
                std::remove_if(neighbors1.begin(), neighbors1.end(),
                    [&vertex2](const std::pair<std::string, int>& neighbor) {
                        return neighbor.first == vertex2;
                    }
                ),
                neighbors1.end()
            );

            // vertex2 から vertex1 への辺を削除
            auto& neighbors2 = _data[vertex2];
            neighbors2.erase(
                std::remove_if(neighbors2.begin(), neighbors2.end(),
                    [&vertex1](const std::pair<std::string, int>& neighbor) {
                        return neighbor.first == vertex1;
                    }
                ),
                neighbors2.end()
            );

            return true;
        }
        return false;
    }

    // グラフが空かどうか
    bool is_empty() const {
        return _data.empty();
    }

    // グラフの頂点数を返す
    size_t size() const {
        return _data.size();
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
