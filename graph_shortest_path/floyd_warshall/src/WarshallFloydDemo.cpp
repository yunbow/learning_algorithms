// C++
// グラフの最短経路: ワーシャルフロイド法 (Warshall Floyd)

#include <iostream>
#include <vector>
#include <unordered_map>
#include <set>
#include <limits>
#include <algorithm>
#include <utility>
#include <string>

class GraphData {
private:
    // 隣接ノードとその辺の重みを格納します。
    // キーは頂点、値はその頂点に隣接する頂点と重みのペアのベクターです。
    std::unordered_map<std::string, std::vector<std::pair<std::string, double>>> _data;

public:
    GraphData() {}

    const std::unordered_map<std::string, std::vector<std::pair<std::string, double>>>& get() const {
        // グラフの内部データを取得します。
        return _data;
    }

    std::vector<std::string> get_vertices() const {
        // グラフの全頂点をベクターとして返します。
        std::vector<std::string> vertices;
        for (const auto& pair : _data) {
            vertices.push_back(pair.first);
        }
        return vertices;
    }

    std::vector<std::tuple<std::string, std::string, double>> get_edges() const {
        // グラフの全辺をベクターとして返します。
        // 無向グラフの場合、(u, v, weight) の形式で返します。
        // 重複を避けるためにセットを使用します。
        std::set<std::tuple<std::string, std::string, double>> edges;
        for (const auto& vertex_pair : _data) {
            const std::string& vertex = vertex_pair.first;
            for (const auto& neighbor_weight : vertex_pair.second) {
                const std::string& neighbor = neighbor_weight.first;
                double weight = neighbor_weight.second;
                
                // 辺を正規化してセットに追加 (小さい方の頂点を最初にするなど)
                std::string v1 = vertex;
                std::string v2 = neighbor;
                if (v1 > v2) {
                    std::swap(v1, v2);
                }
                edges.insert(std::make_tuple(v1, v2, weight));
            }
        }
        return std::vector<std::tuple<std::string, std::string, double>>(edges.begin(), edges.end());
    }

    std::vector<std::pair<std::string, double>> get_neighbors(const std::string& vertex) const {
        // 指定された頂点の隣接ノードと辺の重みのベクターを返します。
        // 形式: [(隣接頂点, 重み), ...]
        auto it = _data.find(vertex);
        if (it != _data.end()) {
            return it->second;
        } else {
            return {}; // 頂点が存在しない場合は空のベクターを返す
        }
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

    bool add_edge(const std::string& vertex1, const std::string& vertex2, double weight) {
        // 両頂点間に辺を追加します。重みを指定します。
        // 頂点がグラフに存在しない場合は追加します。
        if (_data.find(vertex1) == _data.end()) {
            add_vertex(vertex1);
        }
        if (_data.find(vertex2) == _data.end()) {
            add_vertex(vertex2);
        }
        
        // vertex1 -> vertex2 の辺を追加（重み付き）
        bool edge_exists_v1v2 = false;
        for (auto& pair : _data[vertex1]) {
            if (pair.first == vertex2) {
                pair.second = weight; // 既に存在する場合は重みを更新
                edge_exists_v1v2 = true;
                break;
            }
        }
        if (!edge_exists_v1v2) {
            _data[vertex1].push_back({vertex2, weight});
        }

        // vertex2 -> vertex1 の辺を追加（重み付き）
        bool edge_exists_v2v1 = false;
        for (auto& pair : _data[vertex2]) {
            if (pair.first == vertex1) {
                pair.second = weight; // 既に存在する場合は重みを更新
                edge_exists_v2v1 = true;
                break;
            }
        }
        if (!edge_exists_v2v1) {
            _data[vertex2].push_back({vertex1, weight});
        }
        
        return true;
    }

    bool clear() {
        // グラフを空にします。
        _data.clear();
        return true;
    }

    std::pair<std::vector<std::string>, double> get_shortest_path(
        const std::string& start_vertex, 
        const std::string& end_vertex, 
        double (*heuristic)(const std::string&, const std::string&)) {
        
        std::vector<std::string> vertices = get_vertices();
        size_t num_vertices = vertices.size();
        if (num_vertices == 0) {
            return {std::vector<std::string>(), std::numeric_limits<double>::infinity()};
        }

        // 頂点名をインデックスにマッピング
        std::unordered_map<std::string, size_t> vertex_to_index;
        std::unordered_map<size_t, std::string> index_to_vertex;
        for (size_t i = 0; i < num_vertices; ++i) {
            vertex_to_index[vertices[i]] = i;
            index_to_vertex[i] = vertices[i];
        }

        // 開始・終了頂点が存在するか確認
        if (vertex_to_index.find(start_vertex) == vertex_to_index.end() || 
            vertex_to_index.find(end_vertex) == vertex_to_index.end()) {
            std::cout << "ERROR: " << start_vertex << " または " << end_vertex << " がグラフに存在しません。" << std::endl;
            return {std::vector<std::string>(), std::numeric_limits<double>::infinity()};
        }

        size_t start_index = vertex_to_index[start_vertex];
        size_t end_index = vertex_to_index[end_vertex];

        // 距離行列 (dist) と経路復元用行列 (next_node) を初期化
        const double INF = std::numeric_limits<double>::infinity();
        std::vector<std::vector<double>> dist(num_vertices, std::vector<double>(num_vertices, INF));
        std::vector<std::vector<size_t>> next_node(num_vertices, std::vector<size_t>(num_vertices));
        // nextを初期化（インデックスとして有効な値以外で初期化）
        for (size_t i = 0; i < num_vertices; ++i) {
            for (size_t j = 0; j < num_vertices; ++j) {
                next_node[i][j] = num_vertices; // 無効な値で初期化
            }
        }

        // 初期距離と経路復元情報を設定
        for (size_t i = 0; i < num_vertices; ++i) {
            dist[i][i] = 0; // 自分自身への距離は0
            const std::string& vertex_i = vertices[i];
            for (const auto& neighbor_weight : get_neighbors(vertex_i)) {
                const std::string& neighbor = neighbor_weight.first;
                double weight = neighbor_weight.second;
                size_t j = vertex_to_index[neighbor];
                dist[i][j] = weight;
                next_node[i][j] = j; // iからjへの直接辺の場合、iの次はj
            }
        }

        // ワーシャル-フロイド法の本体
        // k: 中継点として使用する頂点のインデックス
        for (size_t k = 0; k < num_vertices; ++k) {
            // i: 開始頂点のインデックス
            for (size_t i = 0; i < num_vertices; ++i) {
                // j: 終了頂点のインデックス
                for (size_t j = 0; j < num_vertices; ++j) {
                    // i -> k -> j の経路が i -> j の現在の経路より短い場合
                    if (dist[i][k] != INF && dist[k][j] != INF && dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        next_node[i][j] = next_node[i][k]; // iからjへの最短経路で、iの次の頂点はiからkへの最短経路でのiの次の頂点
                    }
                }
            }
        }

        // 指定された開始・終了頂点間の最短経路と重みを取得
        double shortest_distance = dist[start_index][end_index];

        // 経路が存在しない場合 (距離がINF)
        if (shortest_distance == INF) {
            return {std::vector<std::string>(), INF};
        }

        // 経路を復元
        std::vector<std::string> path;
        size_t u = start_index;
        // 開始と終了が同じ場合は経路は開始頂点のみ
        if (u == end_index) {
            path = {start_vertex};
        } else {
            // next_nodeを使って経路をたどる
            while (u != num_vertices && u != end_index) {
                path.push_back(index_to_vertex[u]);
                u = next_node[u][end_index];
                // 無限ループ防止のための簡易チェック
                if (u != num_vertices && !path.empty() && index_to_vertex[u] == path.back()) {
                    // 同じ頂点に戻ってきたなど、異常な経路復元を防ぐ
                    std::cout << "WARNING: 経路復元中に異常を検出しました（" << index_to_vertex[u] << "でループ？）。" << std::endl;
                    return {std::vector<std::string>(), INF};
                }
            }
            // 最後のノード (end_vertex) を追加
            path.push_back(end_vertex);
        }

        return {path, shortest_distance};
    }
};

// ヒューリスティック関数 (この例では常に0、ダイクストラ法と同じ)
double dummy_heuristic(const std::string& u, const std::string& v) {
    // u と v の間に何らかの推定距離を計算する関数
    // ここではダミーとして常に0を返す
    return 0;
}

// 経路を文字列として整形する関数
std::string path_to_string(const std::vector<std::string>& path) {
    if (path.empty()) {
        return "None";
    }
    
    std::string result = "[";
    for (size_t i = 0; i < path.size(); ++i) {
        result += "'" + path[i] + "'";
        if (i < path.size() - 1) {
            result += ", ";
        }
    }
    result += "]";
    return result;
}

int main() {
    std::cout << "WarshallFloyd -----> start" << std::endl;

    GraphData graph_data;

    graph_data.clear();
    std::vector<std::tuple<std::string, std::string, double>> inputList = {
        {"A", "B", 4}, {"B", "C", 3}, {"B", "D", 2}, {"D", "A", 1}, {"A", "C", 2}, {"B", "D", 2}
    };
    for (const auto& input : inputList) {
        graph_data.add_edge(std::get<0>(input), std::get<1>(input), std::get<2>(input));
    }
    std::cout << "\nグラフの頂点: ";
    for (const auto& vertex : graph_data.get_vertices()) {
        std::cout << vertex << " ";
    }
    std::cout << std::endl;
    
    std::cout << "グラフの辺 (重み付き): ";
    for (const auto& edge : graph_data.get_edges()) {
        std::cout << "(" << std::get<0>(edge) << ", " << std::get<1>(edge) << ", " << std::get<2>(edge) << ") ";
    }
    std::cout << std::endl;
    
    std::pair<std::string, std::string> input = {"A", "B"};
    auto shortest_path = graph_data.get_shortest_path(input.first, input.second, dummy_heuristic);
    std::cout << "経路" << input.first << "-" << input.second << " の最短経路は " 
              << path_to_string(shortest_path.first) << " (重み: " << shortest_path.second << ")" << std::endl;

    graph_data.clear();
    inputList = {
        {"A", "B", 4}, {"C", "D", 4}, {"E", "F", 1}, {"F", "G", 1}
    };
    for (const auto& input : inputList) {
        graph_data.add_edge(std::get<0>(input), std::get<1>(input), std::get<2>(input));
    }
    std::cout << "\nグラフの頂点: ";
    for (const auto& vertex : graph_data.get_vertices()) {
        std::cout << vertex << " ";
    }
    std::cout << std::endl;
    
    std::cout << "グラフの辺 (重み付き): ";
    for (const auto& edge : graph_data.get_edges()) {
        std::cout << "(" << std::get<0>(edge) << ", " << std::get<1>(edge) << ", " << std::get<2>(edge) << ") ";
    }
    std::cout << std::endl;
    
    input = {"A", "B"};
    shortest_path = graph_data.get_shortest_path(input.first, input.second, dummy_heuristic);
    std::cout << "経路" << input.first << "-" << input.second << " の最短経路は " 
              << path_to_string(shortest_path.first) << " (重み: " << shortest_path.second << ")" << std::endl;

    graph_data.clear();
    inputList = {
        {"A", "B", 4}, {"B", "C", 3}, {"D", "E", 5}
    };
    for (const auto& input : inputList) {
        graph_data.add_edge(std::get<0>(input), std::get<1>(input), std::get<2>(input));
    }
    std::cout << "\nグラフの頂点: ";
    for (const auto& vertex : graph_data.get_vertices()) {
        std::cout << vertex << " ";
    }
    std::cout << std::endl;
    
    std::cout << "グラフの辺 (重み付き): ";
    for (const auto& edge : graph_data.get_edges()) {
        std::cout << "(" << std::get<0>(edge) << ", " << std::get<1>(edge) << ", " << std::get<2>(edge) << ") ";
    }
    std::cout << std::endl;
    
    input = {"A", "D"};
    shortest_path = graph_data.get_shortest_path(input.first, input.second, dummy_heuristic);
    std::cout << "経路" << input.first << "-" << input.second << " の最短経路は " 
              << path_to_string(shortest_path.first) << " (重み: " << shortest_path.second << ")" << std::endl;

    graph_data.clear();
    inputList = {};
    for (const auto& input : inputList) {
        graph_data.add_edge(std::get<0>(input), std::get<1>(input), std::get<2>(input));
    }
    std::cout << "\nグラフの頂点: ";
    for (const auto& vertex : graph_data.get_vertices()) {
        std::cout << vertex << " ";
    }
    std::cout << std::endl;
    
    std::cout << "グラフの辺 (重み付き): ";
    for (const auto& edge : graph_data.get_edges()) {
        std::cout << "(" << std::get<0>(edge) << ", " << std::get<1>(edge) << ", " << std::get<2>(edge) << ") ";
    }
    std::cout << std::endl;
    
    input = {"A", "B"};
    shortest_path = graph_data.get_shortest_path(input.first, input.second, dummy_heuristic);
    std::cout << "経路" << input.first << "-" << input.second << " の最短経路は " 
              << path_to_string(shortest_path.first) << " (重み: " << shortest_path.second << ")" << std::endl;

    std::cout << "\nWarshallFloyd <----- end" << std::endl;
    
    return 0;
}