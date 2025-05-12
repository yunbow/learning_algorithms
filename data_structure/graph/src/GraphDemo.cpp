// C++
// データ構造: グラフ (Graph)

#include <iostream>
#include <vector>
#include <map>
#include <set>
#include <tuple>
#include <algorithm>

// データ構造: グラフ (Graph)
class GraphData {
private:
    // 隣接ノードとその辺の重みを格納します。
    // キーは頂点、値はその頂点に隣接する頂点と重みのベクトルです。
    std::map<std::string, std::vector<std::pair<std::string, int>>> _data;

public:
    GraphData() {}

    const std::map<std::string, std::vector<std::pair<std::string, int>>>& get() const {
        // グラフの内部データを取得します。
        return _data;
    }

    std::vector<std::string> get_vertices() const {
        // グラフの全頂点をベクトルとして返します。
        std::vector<std::string> vertices;
        for (const auto& entry : _data) {
            vertices.push_back(entry.first);
        }
        return vertices;
    }

    std::vector<std::tuple<std::string, std::string, int>> get_edges() const {
        // グラフの全辺をベクトルとして返します。
        // 無向グラフの場合、(u, v, weight) の形式で返します。
        // 重複を避けるためにセットを使用します。
        std::set<std::tuple<std::string, std::string, int>> edges;
        for (const auto& vertex_entry : _data) {
            for (const auto& neighbor_entry : vertex_entry.second) {
                // 辺を正規化してセットに追加（小さい方の頂点を最初にするなど）
                std::string v1 = vertex_entry.first;
                std::string v2 = neighbor_entry.first;
                int weight = neighbor_entry.second;
                
                // 頂点名でソートして正規化
                if (v1 > v2) {
                    std::swap(v1, v2);
                }
                edges.insert(std::make_tuple(v1, v2, weight));
            }
        }
        
        // セットをベクトルに変換
        return std::vector<std::tuple<std::string, std::string, int>>(edges.begin(), edges.end());
    }

    std::vector<std::pair<std::string, int>> get_neighbors(const std::string& vertex) const {
        // 指定された頂点の隣接ノードと辺の重みのリストを返します。
        // 形式: [(隣接頂点, 重み), ...]
        auto it = _data.find(vertex);
        if (it != _data.end()) {
            return it->second;
        }
        return {}; // 頂点が存在しない場合は空のベクトルを返す
    }

    int get_edge_weight(const std::string& vertex1, const std::string& vertex2) const {
        // 指定された2つの頂点間の辺の重みを返します。
        // 辺が存在しない場合は-1を返します（C++ではNull相当として-1を使用）
        auto it = _data.find(vertex1);
        if (it != _data.end()) {
            for (const auto& neighbor : it->second) {
                if (neighbor.first == vertex2) {
                    return neighbor.second;
                }
            }
        }
        return -1; // 辺が存在しない場合
    }

    std::vector<std::pair<std::string, int>> get_vertice(const std::string& vertex) const {
        // 頂点がグラフに存在するか確認する
        auto it = _data.find(vertex);
        if (it != _data.end()) {
            // 存在する場合は、その頂点の隣接リスト（関連する値）を返す
            return it->second;
        } else {
            // 存在しない場合はメッセージを表示し、空のベクトルを返す
            std::cout << "ERROR: " << vertex << "は範囲外です" << std::endl;
            return {};
        }
    }

    bool get_edge(const std::string& vertex1, const std::string& vertex2) const {
        // 指定された2つの頂点間に辺が存在するかを確認する
        // 両方の頂点がグラフに存在する必要がある
        auto it1 = _data.find(vertex1);
        auto it2 = _data.find(vertex2);
        
        if (it1 != _data.end() && it2 != _data.end()) {
            // vertex1の隣接リストにvertex2が含まれているかを確認
            for (const auto& neighbor : it1->second) {
                if (neighbor.first == vertex2) {
                    return true;
                }
            }
        }
        // どちらかの頂点が存在しないか、辺が存在しない
        return false;
    }

    bool add_vertex(const std::string& vertex) {
        // 新しい頂点をグラフに追加します。
        if (_data.find(vertex) == _data.end()) {
            _data[vertex] = {};
            return true;
        }
        // 既に存在する場合は追加しないがTrueを返す（変更なしでも成功とみなす）
        return true;
    }

    bool add_edge(const std::string& vertex1, const std::string& vertex2, int weight) {
        // 両頂点間に辺を追加します。重みを指定します。
        // 頂点がグラフに存在しない場合は追加します。
        if (_data.find(vertex1) == _data.end()) {
            add_vertex(vertex1);
        }
        if (_data.find(vertex2) == _data.end()) {
            add_vertex(vertex2);
        }

        // 両方向に辺を追加する（無向グラフ）
        // 既に同じ辺が存在する場合は重みを更新する

        // vertex1 -> vertex2 の辺を追加（重み付き）
        bool edge_exists_v1v2 = false;
        for (auto& neighbor : _data[vertex1]) {
            if (neighbor.first == vertex2) {
                neighbor.second = weight; // 既に存在する場合は重みを更新
                edge_exists_v1v2 = true;
                break;
            }
        }
        if (!edge_exists_v1v2) {
            _data[vertex1].push_back(std::make_pair(vertex2, weight));
        }

        // vertex2 -> vertex1 の辺を追加（重み付き）
        bool edge_exists_v2v1 = false;
        for (auto& neighbor : _data[vertex2]) {
            if (neighbor.first == vertex1) {
                neighbor.second = weight; // 既に存在する場合は重みを更新
                edge_exists_v2v1 = true;
                break;
            }
        }
        if (!edge_exists_v2v1) {
            _data[vertex2].push_back(std::make_pair(vertex1, weight));
        }

        return true;
    }

    bool remove_vertex(const std::string& vertex) {
        // 頂点とそれに関連する辺を削除する
        auto it = _data.find(vertex);
        if (it != _data.end()) {
            // この頂点を削除
            _data.erase(it);
            
            // この頂点への参照を他の頂点の隣接リストから削除する
            for (auto& entry : _data) {
                auto& neighbors = entry.second;
                neighbors.erase(
                    std::remove_if(neighbors.begin(), neighbors.end(),
                        [&vertex](const std::pair<std::string, int>& neighbor) {
                            return neighbor.first == vertex;
                        }),
                    neighbors.end()
                );
            }
            return true;
        } else {
            std::cout << "ERROR: " << vertex << " は範囲外です" << std::endl;
            return false;
        }
    }

    bool remove_edge(const std::string& vertex1, const std::string& vertex2) {
        // 両頂点間の辺を削除します。
        auto it1 = _data.find(vertex1);
        auto it2 = _data.find(vertex2);
        
        if (it1 != _data.end() && it2 != _data.end()) {
            bool removed = false;
            
            // vertex1 から vertex2 への辺を削除
            size_t original_len_v1 = it1->second.size();
            it1->second.erase(
                std::remove_if(it1->second.begin(), it1->second.end(),
                    [&vertex2](const std::pair<std::string, int>& neighbor) {
                        return neighbor.first == vertex2;
                    }),
                it1->second.end()
            );
            if (it1->second.size() < original_len_v1) {
                removed = true;
            }
            
            // vertex2 から vertex1 への辺を削除
            size_t original_len_v2 = it2->second.size();
            it2->second.erase(
                std::remove_if(it2->second.begin(), it2->second.end(),
                    [&vertex1](const std::pair<std::string, int>& neighbor) {
                        return neighbor.first == vertex1;
                    }),
                it2->second.end()
            );
            if (it2->second.size() < original_len_v2) {
                removed = true;
            }
            
            return removed; // 少なくとも片方向が削除されたか
        } else {
            std::cout << "ERROR: " << vertex1 << " または " << vertex2 << " は範囲外です" << std::endl;
            return false;
        }
    }

    bool is_empty() const {
        // グラフが空かどうか
        return _data.empty();
    }

    size_t size() const {
        // グラフの頂点数を返す
        return _data.size();
    }

    bool clear() {
        // グラフを空にする
        _data.clear();
        return true;
    }
};

// データを出力するためのヘルパー関数
void print_graph_data(const std::map<std::string, std::vector<std::pair<std::string, int>>>& data) {
    std::cout << "  現在のデータ: { ";
    bool first_vertex = true;
    
    for (const auto& vertex_entry : data) {
        if (!first_vertex) {
            std::cout << ", ";
        }
        first_vertex = false;
        
        std::cout << "'" << vertex_entry.first << "': [";
        bool first_neighbor = true;
        
        for (const auto& neighbor : vertex_entry.second) {
            if (!first_neighbor) {
                std::cout << ", ";
            }
            first_neighbor = false;
            
            std::cout << "('" << neighbor.first << "', " << neighbor.second << ")";
        }
        
        std::cout << "]";
    }
    
    std::cout << " }" << std::endl;
}

// ベクトルを出力するヘルパー関数
template<typename T>
void print_vector(const std::vector<T>& vec) {
    std::cout << "  出力値: [";
    for (size_t i = 0; i < vec.size(); ++i) {
        if (i > 0) {
            std::cout << ", ";
        }
        std::cout << "'" << vec[i] << "'";
    }
    std::cout << "]" << std::endl;
}

// エッジを出力するヘルパー関数
void print_edges(const std::vector<std::tuple<std::string, std::string, int>>& edges) {
    std::cout << "  出力値: [";
    for (size_t i = 0; i < edges.size(); ++i) {
        if (i > 0) {
            std::cout << ", ";
        }
        std::cout << "('" << std::get<0>(edges[i]) << "', '" 
                 << std::get<1>(edges[i]) << "', " 
                 << std::get<2>(edges[i]) << ")";
    }
    std::cout << "]" << std::endl;
}

// 隣接リストを出力するヘルパー関数
void print_neighbors(const std::vector<std::pair<std::string, int>>& neighbors) {
    if (neighbors.empty()) {
        std::cout << "  出力値: None" << std::endl;
        return;
    }
    
    std::cout << "  出力値: [";
    for (size_t i = 0; i < neighbors.size(); ++i) {
        if (i > 0) {
            std::cout << ", ";
        }
        std::cout << "('" << neighbors.first << "', " << neighbors.second << ")";
    }
    std::cout << "]" << std::endl;
}

int main() {
    std::cout << "Graph TEST -----> start" << std::endl;

    std::cout << "\nnew" << std::endl;
    GraphData graph_data;
    print_graph_data(graph_data.get());

    std::cout << "\nis_empty" << std::endl;
    bool is_empty_output = graph_data.is_empty();
    std::cout << "  出力値: " << (is_empty_output ? "true" : "false") << std::endl;

    std::cout << "\nsize" << std::endl;
    size_t size_output = graph_data.size();
    std::cout << "  出力値: " << size_output << std::endl;

    std::vector<std::string> inputList = {"A", "B", "C"};
    for (const auto& input : inputList) {
        std::cout << "\nadd_vertex" << std::endl;
        std::cout << "  入力値: " << input << std::endl;
        bool add_vertex_output = graph_data.add_vertex(input);
        std::cout << "  出力値: " << (add_vertex_output ? "true" : "false") << std::endl;
    }
    print_graph_data(graph_data.get());

    std::cout << "\nget_vertices" << std::endl;
    auto vertices = graph_data.get_vertices();
    print_vector(vertices);

    std::cout << "\nsize" << std::endl;
    size_output = graph_data.size();
    std::cout << "  出力値: " << size_output << std::endl;

    std::cout << "\nadd_edge" << std::endl;
    std::vector<std::tuple<std::string, std::string, int>> edge_inputs = {
        std::make_tuple("A", "B", 4),
        std::make_tuple("B", "C", 2),
        std::make_tuple("C", "A", 3)
    };
    
    for (const auto& input : edge_inputs) {
        std::cout << "  入力値: (" << std::get<0>(input) << ", " 
                 << std::get<1>(input) << ", " << std::get<2>(input) << ")" << std::endl;
        bool add_edge_output = graph_data.add_edge(std::get<0>(input), std::get<1>(input), std::get<2>(input));
        std::cout << "  出力値: " << (add_edge_output ? "true" : "false") << std::endl;
    }
    print_graph_data(graph_data.get());

    std::cout << "\nget_vertices" << std::endl;
    vertices = graph_data.get_vertices();
    print_vector(vertices);

    std::cout << "\nget_edges" << std::endl;
    auto edges = graph_data.get_edges();
    print_edges(edges);

    std::cout << "\nsize" << std::endl;
    size_output = graph_data.size();
    std::cout << "  出力値: " << size_output << std::endl;

    std::cout << "\nget_vertice" << std::endl;
    std::string vertex_input = "B";
    std::cout << "  入力値: '" << vertex_input << "'" << std::endl;
    auto neighbors = graph_data.get_vertice(vertex_input);
    if (!neighbors.empty()) {
        std::cout << "  出力値: [";
        for (size_t i = 0; i < neighbors.size(); ++i) {
            if (i > 0) {
                std::cout << ", ";
            }
            std::cout << "('" << neighbors[i].first << "', " << neighbors[i].second << ")";
        }
        std::cout << "]" << std::endl;
    }

    std::cout << "\nget_vertice" << std::endl;
    vertex_input = "E";
    std::cout << "  入力値: '" << vertex_input << "'" << std::endl;
    neighbors = graph_data.get_vertice(vertex_input);
    if (!neighbors.empty()) {
        std::cout << "  出力値: [";
        for (size_t i = 0; i < neighbors.size(); ++i) {
            if (i > 0) {
                std::cout << ", ";
            }
            std::cout << "('" << neighbors[i].first << "', " << neighbors[i].second << ")";
        }
        std::cout << "]" << std::endl;
    } else {
        std::cout << "  出力値: None" << std::endl;
    }

    std::cout << "\nremove_edge" << std::endl;
    std::string remove_edge_v1 = "A";
    std::string remove_edge_v2 = "B";
    std::cout << "  入力値: (" << remove_edge_v1 << ", " << remove_edge_v2 << ")" << std::endl;
    bool remove_edge_output = graph_data.remove_edge(remove_edge_v1, remove_edge_v2);
    std::cout << "  出力値: " << (remove_edge_output ? "true" : "false") << std::endl;
    print_graph_data(graph_data.get());

    std::cout << "\nremove_edge" << std::endl;
    remove_edge_v1 = "A";
    remove_edge_v2 = "C";
    std::cout << "  入力値: (" << remove_edge_v1 << ", " << remove_edge_v2 << ")" << std::endl;
    remove_edge_output = graph_data.remove_edge(remove_edge_v1, remove_edge_v2);
    std::cout << "  出力値: " << (remove_edge_output ? "true" : "false") << std::endl;
    print_graph_data(graph_data.get());

    std::cout << "\nget_edges" << std::endl;
    edges = graph_data.get_edges();
    print_edges(edges);

    std::cout << "\nremove_vertex" << std::endl;
    std::string remove_vertex_input = "B";
    std::cout << "  入力値: " << remove_vertex_input << std::endl;
    bool remove_vertex_output = graph_data.remove_vertex(remove_vertex_input);
    std::cout << "  出力値: " << (remove_vertex_output ? "true" : "false") << std::endl;
    print_graph_data(graph_data.get());

    std::cout << "\nremove_vertex" << std::endl;
    remove_vertex_input = "Z";
    std::cout << "  入力値: " << remove_vertex_input << std::endl;
    remove_vertex_output = graph_data.remove_vertex(remove_vertex_input);
    std::cout << "  出力値: " << (remove_vertex_output ? "true" : "false") << std::endl;
    print_graph_data(graph_data.get());

    std::cout << "\nget_vertices" << std::endl;
    vertices = graph_data.get_vertices();
    print_vector(vertices);

    std::cout << "\nget_edges" << std::endl;
    edges = graph_data.get_edges();
    print_edges(edges);

    std::cout << "\nsize" << std::endl;
    size_output = graph_data.size();
    std::cout << "  出力値: " << size_output << std::endl;

    std::cout << "\nget_vertice" << std::endl;
    vertex_input = "B";
    std::cout << "  入力値: " << vertex_input << std::endl;
    neighbors = graph_data.get_vertice(vertex_input);
    if (!neighbors.empty()) {
        std::cout << "  出力値: [";
        for (size_t i = 0; i < neighbors.size(); ++i) {
            if (i > 0) {
                std::cout << ", ";
            }
            std::cout << "('" << neighbors[i].first << "', " << neighbors[i].second << ")";
        }
        std::cout << "]" << std::endl;
    } else {
        std::cout << "  出力値: None" << std::endl;
    }

    std::cout << "\nclear" << std::endl;
    bool clear_output = graph_data.clear();
    std::cout << "  出力値: " << (clear_output ? "true" : "false") << std::endl;

    std::cout << "\nis_empty" << std::endl;
    is_empty_output = graph_data.is_empty();
    std::cout << "  出力値: " << (is_empty_output ? "true" : "false") << std::endl;

    std::cout << "\nsize" << std::endl;
    size_output = graph_data.size();
    std::cout << "  出力値: " << size_output << std::endl;

    std::cout << "\nget_vertices" << std::endl;
    vertices = graph_data.get_vertices();
    print_vector(vertices);

    std::cout << "\nget_edges" << std::endl;
    edges = graph_data.get_edges();
    print_edges(edges);

    std::cout << "\nGraph TEST <----- end" << std::endl;

    return 0;
}