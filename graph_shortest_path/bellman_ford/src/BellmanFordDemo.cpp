// C++
// グラフの最短経路: ベルマンフォード法 (Bellman Ford)

#include <iostream>
#include <vector>
#include <map>
#include <unordered_map>
#include <set>
#include <limits>
#include <algorithm>
#include <tuple>
#include <utility>
#include <string>

class GraphData {
private:
    // キーは頂点、値はその頂点に隣接する頂点と重みのリストです
    std::unordered_map<std::string, std::vector<std::pair<std::string, int>>> _data;

public:
    GraphData() {}

    std::unordered_map<std::string, std::vector<std::pair<std::string, int>>> get() {
        // グラフの内部データを取得します
        return _data;
    }

    std::vector<std::string> get_vertices() {
        // グラフの全頂点をリストとして返します
        std::vector<std::string> vertices;
        for (const auto& pair : _data) {
            vertices.push_back(pair.first);
        }
        return vertices;
    }

    std::vector<std::tuple<std::string, std::string, int>> get_edges() {
        // グラフの全辺をリストとして返します
        // 各辺は (出発頂点, 到着頂点, 重み) のタプルになります
        std::vector<std::tuple<std::string, std::string, int>> edges;
        for (const auto& pair : _data) {
            const std::string& u = pair.first;
            for (const auto& neighbor_weight : pair.second) {
                const std::string& v = neighbor_weight.first;
                int weight = neighbor_weight.second;
                edges.push_back(std::make_tuple(u, v, weight));
            }
        }
        return edges;
    }

    bool add_vertex(const std::string& vertex) {
        // 新しい頂点をグラフに追加します
        if (_data.find(vertex) == _data.end()) {
            _data[vertex] = {};
            return true;
        }
        return true; // 既に存在する場合は追加しないがTrueを返す
    }

    bool add_edge(const std::string& vertex1, const std::string& vertex2, int weight) {
        // 両頂点間に辺を追加します。重みを指定します
        // 頂点がグラフに存在しない場合は追加します
        add_vertex(vertex1);
        add_vertex(vertex2);

        // vertex1 -> vertex2 の辺を追加（重み付き）
        // 既に同じ頂点間の辺が存在する場合は重みを更新
        bool edge_updated_v1v2 = false;
        for (auto& neighbor_weight : _data[vertex1]) {
            if (neighbor_weight.first == vertex2) {
                neighbor_weight.second = weight;
                edge_updated_v1v2 = true;
                break;
            }
        }
        if (!edge_updated_v1v2) {
            _data[vertex1].push_back(std::make_pair(vertex2, weight));
        }

        // vertex2 -> vertex1 の辺を追加（重み付き）
        // 既に同じ頂点間の辺が存在する場合は重みを更新
        bool edge_updated_v2v1 = false;
        for (auto& neighbor_weight : _data[vertex2]) {
            if (neighbor_weight.first == vertex1) {
                neighbor_weight.second = weight;
                edge_updated_v2v1 = true;
                break;
            }
        }
        if (!edge_updated_v2v1) {
            _data[vertex2].push_back(std::make_pair(vertex1, weight));
        }

        return true;
    }

    bool is_empty() {
        // グラフが空かどうかを返します
        return _data.empty();
    }

    bool clear() {
        // グラフを空にします
        _data.clear();
        return true;
    }

    std::pair<std::vector<std::string>, int> get_shortest_path(
        const std::string& start_vertex,
        const std::string& end_vertex,
        int (*heuristic)(const std::string&, const std::string&)) {
        
        std::vector<std::string> vertices = get_vertices();
        std::vector<std::tuple<std::string, std::string, int>> edges = get_edges();
        size_t num_vertices = vertices.size();

        // 始点と終点の存在チェック
        if (std::find(vertices.begin(), vertices.end(), start_vertex) == vertices.end()) {
            std::cout << "エラー: 始点 '" << start_vertex << "' がグラフに存在しません。" << std::endl;
            return std::make_pair(std::vector<std::string>(), std::numeric_limits<int>::max());
        }
        if (std::find(vertices.begin(), vertices.end(), end_vertex) == vertices.end()) {
            std::cout << "エラー: 終点 '" << end_vertex << "' がグラフに存在しません。" << std::endl;
            return std::make_pair(std::vector<std::string>(), std::numeric_limits<int>::max());
        }

        // 始点と終点が同じ場合
        if (start_vertex == end_vertex) {
            return std::make_pair(std::vector<std::string>{start_vertex}, 0);
        }

        // 距離と先行頂点の初期化
        std::unordered_map<std::string, int> dist;
        std::unordered_map<std::string, std::string> pred;
        
        for (const auto& vertex : vertices) {
            dist[vertex] = std::numeric_limits<int>::max();
            pred[vertex] = "";
        }
        dist[start_vertex] = 0; // 始点自身の距離は0

        // |V| - 1 回の緩和ステップを実行
        for (size_t i = 0; i < num_vertices - 1; ++i) {
            // 緩和が一度も行われなかった場合にループを中断するためのフラグ
            bool relaxed_in_this_iteration = false;
            
            for (const auto& edge : edges) {
                const std::string& u = std::get<0>(edge);
                const std::string& v = std::get<1>(edge);
                int weight = std::get<2>(edge);
                
                // dist[u] が無限大でない場合のみ緩和を試みる
                if (dist[u] != std::numeric_limits<int>::max() && 
                    dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    pred[v] = u;
                    relaxed_in_this_iteration = true;
                }
            }
            
            // このイテレーションで緩和が行われなかった場合は、ループを抜ける
            if (!relaxed_in_this_iteration) {
                break;
            }
        }

        // 負閉路の検出
        for (const auto& edge : edges) {
            const std::string& u = std::get<0>(edge);
            const std::string& v = std::get<1>(edge);
            int weight = std::get<2>(edge);
            
            if (dist[u] != std::numeric_limits<int>::max() && 
                dist[u] + weight < dist[v]) {
                // 負閉路が存在します
                std::cout << "エラー: グラフに負閉路が存在します。最短経路は定義できません。" << std::endl;
                return std::make_pair(std::vector<std::string>(), -std::numeric_limits<int>::max());
            }
        }

        // 最短経路の構築
        std::vector<std::string> path;
        std::string current = end_vertex;

        // 終点まで到達不可能かチェック
        if (dist[end_vertex] == std::numeric_limits<int>::max()) {
            return std::make_pair(std::vector<std::string>(), std::numeric_limits<int>::max());
        }

        // 終点から先行頂点をたどって経路を逆順に構築
        while (!current.empty()) {
            path.push_back(current);
            // 始点に到達したらループを終了
            if (current == start_vertex) {
                break;
            }
            // 次の頂点に進む
            current = pred[current];
        }

        // 経路が始点から始まっていない場合
        if (path.empty() || path.back() != start_vertex) {
            return std::make_pair(std::vector<std::string>(), std::numeric_limits<int>::max());
        }

        // 経路を始点から終点の順にする
        std::reverse(path.begin(), path.end());

        return std::make_pair(path, dist[end_vertex]);
    }
};

// ヒューリスティック関数 (ベルマン-フォード法では使用しないが、元のコードに合わせた引数のために残す)
int dummy_heuristic(const std::string& u, const std::string& v) {
    // u と v の間に何らかの推定距離を計算する関数
    // ここではベルマン-フォード法では使用しないため、常に0を返す
    return 0;
}

// ベクターの内容を表示するヘルパー関数
template<typename T>
void print_vector(const std::vector<T>& vec) {
    std::cout << "[";
    for (size_t i = 0; i < vec.size(); ++i) {
        std::cout << vec[i];
        if (i < vec.size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]";
}

// エッジのリストを表示するヘルパー関数
void print_edges(const std::vector<std::tuple<std::string, std::string, int>>& edges) {
    std::cout << "[";
    for (size_t i = 0; i < edges.size(); ++i) {
        std::cout << "('" << std::get<0>(edges[i]) << "', '" 
                << std::get<1>(edges[i]) << "', " 
                << std::get<2>(edges[i]) << ")";
        if (i < edges.size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]";
}

int main() {
    std::cout << "BellmanFord TEST -----> start" << std::endl;

    GraphData graph_data;

    graph_data.clear();
    std::vector<std::tuple<std::string, std::string, int>> inputList = {
        {"A", "B", 4}, {"B", "C", 3}, {"B", "D", 2}, {"D", "A", 1}, {"A", "C", 2}, {"B", "D", 2}
    };
    for (const auto& input : inputList) {
        graph_data.add_edge(std::get<0>(input), std::get<1>(input), std::get<2>(input));
    }
    std::cout << "\nグラフの頂点: ";
    print_vector(graph_data.get_vertices());
    std::cout << std::endl;
    
    std::cout << "グラフの辺 (重み付き): ";
    print_edges(graph_data.get_edges());
    std::cout << std::endl;
    
    std::pair<std::string, std::string> input = {"A", "B"};
    auto shortest_path = graph_data.get_shortest_path(input.first, input.second, dummy_heuristic);
    std::cout << "経路" << input.first << "-" << input.second << " の最短経路は ";
    print_vector(shortest_path.first);
    std::cout << " (重み: " << shortest_path.second << ")" << std::endl;

    graph_data.clear();
    inputList = {
        {"A", "B", 4}, {"C", "D", 4}, {"E", "F", 1}, {"F", "G", 1}
    };
    for (const auto& input : inputList) {
        graph_data.add_edge(std::get<0>(input), std::get<1>(input), std::get<2>(input));
    }
    std::cout << "\nグラフの頂点: ";
    print_vector(graph_data.get_vertices());
    std::cout << std::endl;
    
    std::cout << "グラフの辺 (重み付き): ";
    print_edges(graph_data.get_edges());
    std::cout << std::endl;
    
    input = {"A", "B"};
    shortest_path = graph_data.get_shortest_path(input.first, input.second, dummy_heuristic);
    std::cout << "経路" << input.first << "-" << input.second << " の最短経路は ";
    print_vector(shortest_path.first);
    std::cout << " (重み: " << shortest_path.second << ")" << std::endl;

    graph_data.clear();
    inputList = {
        {"A", "B", 4}, {"B", "C", 3}, {"D", "E", 5}
    };
    for (const auto& input : inputList) {
        graph_data.add_edge(std::get<0>(input), std::get<1>(input), std::get<2>(input));
    }
    std::cout << "\nグラフの頂点: ";
    print_vector(graph_data.get_vertices());
    std::cout << std::endl;
    
    std::cout << "グラフの辺 (重み付き): ";
    print_edges(graph_data.get_edges());
    std::cout << std::endl;
    
    input = {"A", "D"};
    shortest_path = graph_data.get_shortest_path(input.first, input.second, dummy_heuristic);
    std::cout << "経路" << input.first << "-" << input.second << " の最短経路は ";
    print_vector(shortest_path.first);
    std::cout << " (重み: " << shortest_path.second << ")" << std::endl;

    graph_data.clear();
    inputList = {};
    for (const auto& input : inputList) {
        graph_data.add_edge(std::get<0>(input), std::get<1>(input), std::get<2>(input));
    }
    std::cout << "\nグラフの頂点: ";
    print_vector(graph_data.get_vertices());
    std::cout << std::endl;
    
    std::cout << "グラフの辺 (重み付き): ";
    print_edges(graph_data.get_edges());
    std::cout << std::endl;
    
    input = {"A", "B"};
    shortest_path = graph_data.get_shortest_path(input.first, input.second, dummy_heuristic);
    std::cout << "経路" << input.first << "-" << input.second << " の最短経路は ";
    print_vector(shortest_path.first);
    std::cout << " (重み: " << shortest_path.second << ")" << std::endl;

    std::cout << "\nBellmanFord TEST <----- end" << std::endl;

    return 0;
}
