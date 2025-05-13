// C++
// グラフの連結成分: Union-Find

#include <iostream>
#include <vector>
#include <unordered_map>
#include <map>
#include <set>
#include <utility>
#include <algorithm>
#include <string>
#include <tuple>

class GraphData {
private:
    // 隣接ノードとその辺の重みを格納します
    // キーは頂点、値はその頂点に隣接する頂点と重みのペアのベクター
    std::unordered_map<std::string, std::vector<std::pair<std::string, int>>> _data;

public:
    GraphData() {}

    // グラフの内部データを取得
    std::unordered_map<std::string, std::vector<std::pair<std::string, int>>>& get() {
        return _data;
    }

    // グラフの全頂点をベクターとして返す
    std::vector<std::string> get_vertices() {
        std::vector<std::string> vertices;
        for (const auto& pair : _data) {
            vertices.push_back(pair.first);
        }
        return vertices;
    }

    // グラフの全辺をベクターとして返す
    std::vector<std::tuple<std::string, std::string, int>> get_edges() {
        std::set<std::tuple<std::string, std::string, int>> edges;
        for (const auto& vertex_pair : _data) {
            const std::string& vertex = vertex_pair.first;
            for (const auto& neighbor_pair : vertex_pair.second) {
                const std::string& neighbor = neighbor_pair.first;
                int weight = neighbor_pair.second;
                
                // 辺を正規化して（小さい方の頂点を最初にする）セットに追加
                std::string v1 = vertex;
                std::string v2 = neighbor;
                if (v1 > v2) {
                    std::swap(v1, v2);
                }
                edges.insert(std::make_tuple(v1, v2, weight));
            }
        }
        return std::vector<std::tuple<std::string, std::string, int>>(edges.begin(), edges.end());
    }

    // 指定された頂点の隣接ノードと辺の重みのベクターを返す
    std::vector<std::pair<std::string, int>>* get_neighbors(const std::string& vertex) {
        if (_data.find(vertex) != _data.end()) {
            return &_data[vertex];
        }
        return nullptr; // 頂点が存在しない場合はnullptrを返す
    }

    // 指定された2つの頂点間の辺の重みを返す
    int* get_edge_weight(const std::string& vertex1, const std::string& vertex2) {
        if (_data.find(vertex1) != _data.end() && _data.find(vertex2) != _data.end()) {
            for (auto& neighbor_pair : _data[vertex1]) {
                if (neighbor_pair.first == vertex2) {
                    return &neighbor_pair.second;
                }
            }
        }
        return nullptr; // 辺が存在しない場合はnullptrを返す
    }

    // 頂点がグラフに存在するか確認する
    std::vector<std::pair<std::string, int>>* get_vertice(const std::string& vertex) {
        if (_data.find(vertex) != _data.end()) {
            return &_data[vertex];
        } else {
            std::cout << "ERROR: " << vertex << "は範囲外です" << std::endl;
            return nullptr;
        }
    }

    // 指定された2つの頂点間に辺が存在するかを確認する
    bool get_edge(const std::string& vertex1, const std::string& vertex2) {
        if (_data.find(vertex1) != _data.end() && _data.find(vertex2) != _data.end()) {
            for (const auto& neighbor_pair : _data[vertex1]) {
                if (neighbor_pair.first == vertex2) {
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
        // 頂点がグラフに存在しない場合は追加
        if (_data.find(vertex1) == _data.end()) {
            add_vertex(vertex1);
        }
        if (_data.find(vertex2) == _data.end()) {
            add_vertex(vertex2);
        }

        // vertex1 -> vertex2 の辺を追加（重み付き）
        bool edge_exists_v1v2 = false;
        for (auto& neighbor_pair : _data[vertex1]) {
            if (neighbor_pair.first == vertex2) {
                neighbor_pair.second = weight; // 既に存在する場合は重みを更新
                edge_exists_v1v2 = true;
                break;
            }
        }
        if (!edge_exists_v1v2) {
            _data[vertex1].push_back(std::make_pair(vertex2, weight));
        }

        // vertex2 -> vertex1 の辺を追加（重み付き）
        bool edge_exists_v2v1 = false;
        for (auto& neighbor_pair : _data[vertex2]) {
            if (neighbor_pair.first == vertex1) {
                neighbor_pair.second = weight; // 既に存在する場合は重みを更新
                edge_exists_v2v1 = true;
                break;
            }
        }
        if (!edge_exists_v2v1) {
            _data[vertex2].push_back(std::make_pair(vertex1, weight));
        }

        return true;
    }

    // 頂点とそれに関連する辺を削除
    bool remove_vertex(const std::string& vertex) {
        if (_data.find(vertex) != _data.end()) {
            // この頂点への参照を他の頂点の隣接リストから削除
            for (auto& v : _data) {
                auto& neighbors = v.second;
                neighbors.erase(
                    std::remove_if(neighbors.begin(), neighbors.end(),
                        [&vertex](const std::pair<std::string, int>& p) { return p.first == vertex; }),
                    neighbors.end()
                );
            }
            // 頂点自体を削除
            _data.erase(vertex);
            return true;
        } else {
            std::cout << "ERROR: " << vertex << " は範囲外です" << std::endl;
            return false;
        }
    }

    // 両頂点間の辺を削除
    bool remove_edge(const std::string& vertex1, const std::string& vertex2) {
        if (_data.find(vertex1) != _data.end() && _data.find(vertex2) != _data.end()) {
            bool removed = false;
            
            // vertex1 から vertex2 への辺を削除
            size_t original_len_v1 = _data[vertex1].size();
            _data[vertex1].erase(
                std::remove_if(_data[vertex1].begin(), _data[vertex1].end(),
                    [&vertex2](const std::pair<std::string, int>& p) { return p.first == vertex2; }),
                _data[vertex1].end()
            );
            if (_data[vertex1].size() < original_len_v1) {
                removed = true;
            }

            // vertex2 から vertex1 への辺を削除
            size_t original_len_v2 = _data[vertex2].size();
            _data[vertex2].erase(
                std::remove_if(_data[vertex2].begin(), _data[vertex2].end(),
                    [&vertex1](const std::pair<std::string, int>& p) { return p.first == vertex1; }),
                _data[vertex2].end()
            );
            if (_data[vertex2].size() < original_len_v2) {
                removed = true;
            }

            return removed; // 少なくとも片方向が削除されたか
        } else {
            std::cout << "ERROR: " << vertex1 << " または " << vertex2 << " は範囲外です" << std::endl;
            return false;
        }
    }

    // グラフが空かどうか
    bool is_empty() {
        return _data.empty();
    }

    // グラフの頂点数を返す
    size_t size() {
        return _data.size();
    }

    // グラフを空にする
    bool clear() {
        _data.clear();
        return true;
    }

    // 連結成分を取得
    std::vector<std::vector<std::string>> get_connected_components() {
        if (_data.empty()) {
            return {}; // 空のグラフの場合は空リストを返す
        }

        // Union-Findのためのデータ構造を初期化
        std::unordered_map<std::string, std::string> parent;
        std::unordered_map<std::string, int> size;

        // 各頂点を初期状態では自分自身の集合に属させ、サイズを1とする
        std::vector<std::string> vertices = get_vertices();
        for (const auto& vertex : vertices) {
            parent[vertex] = vertex;
            size[vertex] = 1;
        }

        // Find操作（経路圧縮を伴う）
        std::function<std::string(const std::string&)> find = [&](const std::string& v) -> std::string {
            if (parent[v] != v) {
                parent[v] = find(parent[v]); // 経路圧縮
            }
            return parent[v];
        };

        // Union操作（Union by Sizeを伴う）
        std::function<bool(const std::string&, const std::string&)> union_sets = [&](const std::string& u, const std::string& v) -> bool {
            std::string root_u = find(u);
            std::string root_v = find(v);

            // 根が同じ場合は、すでに同じ集合に属しているので何もしない
            if (root_u != root_v) {
                // より小さいサイズの木を大きいサイズの木に結合する
                if (size[root_u] < size[root_v]) {
                    parent[root_u] = root_v;
                    size[root_v] += size[root_u];
                } else {
                    parent[root_v] = root_u;
                    size[root_u] += size[root_v];
                }
                return true; // 結合が行われた
            }
            return false; // 結合は行われなかった
        };

        // グラフの全ての辺に対してUnion操作を行い、連結成分をマージする
        for (const auto& edge : get_edges()) {
            const std::string& u = std::get<0>(edge);
            const std::string& v = std::get<1>(edge);
            union_sets(u, v);
        }

        // 連結成分をグループ化する
        std::unordered_map<std::string, std::vector<std::string>> components;
        for (const auto& vertex : vertices) {
            std::string root = find(vertex); // 各頂点の最終的な根を見つける
            if (components.find(root) == components.end()) {
                components[root] = std::vector<std::string>();
            }
            components[root].push_back(vertex);
        }

        // 連結成分のリスト（値の部分）を返す
        std::vector<std::vector<std::string>> result;
        for (const auto& component : components) {
            result.push_back(component.second);
        }
        return result;
    }
};

int main() {
    std::cout << "UnionFind TEST -----> start" << std::endl;

    std::cout << "\nnew" << std::endl;
    GraphData graph_data;
    std::cout << "  現在のデータ: "; // C++ではマップの直接表示が難しいので省略
    
    std::cout << "\nadd_edge" << std::endl;
    graph_data.clear();
    std::vector<std::tuple<std::string, std::string, int>> inputList = {
        {"A", "B", 4}, {"B", "C", 3}, {"B", "D", 2}, {"D", "A", 1}, {"A", "C", 2}, {"B", "D", 2}
    };
    for (const auto& input : inputList) {
        std::cout << "  入力値: (" << std::get<0>(input) << ", " << std::get<1>(input) << ", " << std::get<2>(input) << ")" << std::endl;
        bool output = graph_data.add_edge(std::get<0>(input), std::get<1>(input), std::get<2>(input));
        std::cout << "  出力値: " << (output ? "true" : "false") << std::endl;
    }
    std::cout << "  現在のデータ: "; // C++ではマップの直接表示が難しいので省略
    
    std::cout << "\nget_connected_components" << std::endl;
    auto output1 = graph_data.get_connected_components();
    std::cout << "  連結成分: [";
    for (size_t i = 0; i < output1.size(); ++i) {
        std::cout << "[";
        for (size_t j = 0; j < output1[i].size(); ++j) {
            std::cout << "'" << output1[i][j] << "'";
            if (j < output1[i].size() - 1) std::cout << ", ";
        }
        std::cout << "]";
        if (i < output1.size() - 1) std::cout << ", ";
    }
    std::cout << "]" << std::endl;

    std::cout << "\nadd_edge" << std::endl;
    graph_data.clear();
    std::vector<std::tuple<std::string, std::string, int>> inputList2 = {
        {"A", "B", 4}, {"C", "D", 4}, {"E", "F", 1}, {"F", "G", 1}
    };
    for (const auto& input : inputList2) {
        std::cout << "  入力値: (" << std::get<0>(input) << ", " << std::get<1>(input) << ", " << std::get<2>(input) << ")" << std::endl;
        bool output = graph_data.add_edge(std::get<0>(input), std::get<1>(input), std::get<2>(input));
        std::cout << "  出力値: " << (output ? "true" : "false") << std::endl;
    }
    std::cout << "  現在のデータ: "; // C++ではマップの直接表示が難しいので省略
    
    std::cout << "\nget_connected_components" << std::endl;
    auto output2 = graph_data.get_connected_components();
    std::cout << "  連結成分: [";
    for (size_t i = 0; i < output2.size(); ++i) {
        std::cout << "[";
        for (size_t j = 0; j < output2[i].size(); ++j) {
            std::cout << "'" << output2[i][j] << "'";
            if (j < output2[i].size() - 1) std::cout << ", ";
        }
        std::cout << "]";
        if (i < output2.size() - 1) std::cout << ", ";
    }
    std::cout << "]" << std::endl;

    std::cout << "\nadd_edge" << std::endl;
    graph_data.clear();
    std::vector<std::tuple<std::string, std::string, int>> inputList3 = {
        {"A", "B", 4}, {"B", "C", 3}, {"D", "E", 5}
    };
    for (const auto& input : inputList3) {
        std::cout << "  入力値: (" << std::get<0>(input) << ", " << std::get<1>(input) << ", " << std::get<2>(input) << ")" << std::endl;
        bool output = graph_data.add_edge(std::get<0>(input), std::get<1>(input), std::get<2>(input));
        std::cout << "  出力値: " << (output ? "true" : "false") << std::endl;
    }
    std::cout << "  現在のデータ: "; // C++ではマップの直接表示が難しいので省略
    
    std::cout << "\nget_connected_components" << std::endl;
    auto output3 = graph_data.get_connected_components();
    std::cout << "  連結成分: [";
    for (size_t i = 0; i < output3.size(); ++i) {
        std::cout << "[";
        for (size_t j = 0; j < output3[i].size(); ++j) {
            std::cout << "'" << output3[i][j] << "'";
            if (j < output3[i].size() - 1) std::cout << ", ";
        }
        std::cout << "]";
        if (i < output3.size() - 1) std::cout << ", ";
    }
    std::cout << "]" << std::endl;

    std::cout << "\nadd_edge" << std::endl;
    graph_data.clear();
    std::vector<std::tuple<std::string, std::string, int>> inputList4;
    for (const auto& input : inputList4) {
        std::cout << "  入力値: (" << std::get<0>(input) << ", " << std::get<1>(input) << ", " << std::get<2>(input) << ")" << std::endl;
        bool output = graph_data.add_edge(std::get<0>(input), std::get<1>(input), std::get<2>(input));
        std::cout << "  出力値: " << (output ? "true" : "false") << std::endl;
    }
    std::cout << "  現在のデータ: "; // C++ではマップの直接表示が難しいので省略
    
    std::cout << "\nget_connected_components" << std::endl;
    auto output4 = graph_data.get_connected_components();
    std::cout << "  連結成分: [";
    for (size_t i = 0; i < output4.size(); ++i) {
        std::cout << "[";
        for (size_t j = 0; j < output4[i].size(); ++j) {
            std::cout << "'" << output4[i][j] << "'";
            if (j < output4[i].size() - 1) std::cout << ", ";
        }
        std::cout << "]";
        if (i < output4.size() - 1) std::cout << ", ";
    }
    std::cout << "]" << std::endl;

    std::cout << "\nUnionFind TEST <----- end" << std::endl;
    
    return 0;
}