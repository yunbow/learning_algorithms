// C++
// グラフの最短経路: A-star

#include <iostream>
#include <vector>
#include <unordered_map>
#include <queue>
#include <set>
#include <limits>
#include <algorithm>
#include <functional>

class GraphData {
private:
    // 隣接ノードとその辺の重みを格納します。
    // キーは頂点、値はその頂点に隣接する頂点と重みのペアのベクター
    std::unordered_map<std::string, std::vector<std::pair<std::string, int>>> _data;

public:
    GraphData() {}

    // グラフの内部データを取得します。
    const std::unordered_map<std::string, std::vector<std::pair<std::string, int>>>& get() const {
        return _data;
    }

    // グラフの全頂点をベクターとして返します。
    std::vector<std::string> get_vertices() const {
        std::vector<std::string> vertices;
        for (const auto& pair : _data) {
            vertices.push_back(pair.first);
        }
        return vertices;
    }

    // グラフの全辺をベクターとして返します。
    // 無向グラフの場合、(u, v, weight) の形式で返します。
    std::vector<std::tuple<std::string, std::string, int>> get_edges() const {
        std::set<std::tuple<std::string, std::string, int>> edges;
        for (const auto& vertex_pair : _data) {
            const std::string& vertex = vertex_pair.first;
            for (const auto& neighbor_weight : vertex_pair.second) {
                const std::string& neighbor = neighbor_weight.first;
                int weight = neighbor_weight.second;
                
                // 辺を正規化してセットに追加（小さい方の頂点を最初にする）
                std::string first = vertex;
                std::string second = neighbor;
                if (vertex > neighbor) {
                    std::swap(first, second);
                }
                edges.insert(std::make_tuple(first, second, weight));
            }
        }
        return std::vector<std::tuple<std::string, std::string, int>>(edges.begin(), edges.end());
    }

    // 指定された頂点の隣接ノードと辺の重みのベクターを返します。
    const std::vector<std::pair<std::string, int>>* get_neighbors(const std::string& vertex) const {
        auto it = _data.find(vertex);
        if (it != _data.end()) {
            return &(it->second);
        }
        return nullptr; // 頂点が存在しない場合はnullptrを返す
    }

    // 新しい頂点をグラフに追加します。
    bool add_vertex(const std::string& vertex) {
        if (_data.find(vertex) == _data.end()) {
            _data[vertex] = std::vector<std::pair<std::string, int>>();
        }
        return true;
    }

    // 両頂点間に辺を追加します。重みを指定します。
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
        for (auto& neighbor_weight : _data[vertex1]) {
            if (neighbor_weight.first == vertex2) {
                neighbor_weight.second = weight; // 既に存在する場合は重みを更新
                edge_exists_v1v2 = true;
                break;
            }
        }
        if (!edge_exists_v1v2) {
            _data[vertex1].push_back(std::make_pair(vertex2, weight));
        }

        // vertex2 -> vertex1 の辺を追加（重み付き）
        bool edge_exists_v2v1 = false;
        for (auto& neighbor_weight : _data[vertex2]) {
            if (neighbor_weight.first == vertex1) {
                neighbor_weight.second = weight; // 既に存在する場合は重みを更新
                edge_exists_v2v1 = true;
                break;
            }
        }
        if (!edge_exists_v2v1) {
            _data[vertex2].push_back(std::make_pair(vertex1, weight));
        }

        return true;
    }

    // グラフを空にします。
    bool clear() {
        _data.clear();
        return true;
    }

    // A*アルゴリズムを使用して最短経路を見つけます。
    std::pair<std::vector<std::string>, int> get_shortest_path(
        const std::string& start_vertex, 
        const std::string& end_vertex,
        std::function<int(const std::string&, const std::string&)> heuristic
    ) {
        if (_data.find(start_vertex) == _data.end() || _data.find(end_vertex) == _data.end()) {
            std::cout << "ERROR: 開始頂点または終了頂点がグラフに存在しません。" << std::endl;
            return {std::vector<std::string>(), std::numeric_limits<int>::max()};
        }

        if (start_vertex == end_vertex) {
            return {std::vector<std::string>{start_vertex}, 0};
        }

        // g_costs: 開始ノードから各ノードまでの既知の最短コスト
        std::unordered_map<std::string, int> g_costs;
        for (const auto& vertex_pair : _data) {
            g_costs[vertex_pair.first] = std::numeric_limits<int>::max();
        }
        g_costs[start_vertex] = 0;

        // f_costs: g_costs + ヒューリスティックコスト（推定合計コスト）
        std::unordered_map<std::string, int> f_costs;
        for (const auto& vertex_pair : _data) {
            f_costs[vertex_pair.first] = std::numeric_limits<int>::max();
        }
        f_costs[start_vertex] = heuristic(start_vertex, end_vertex);

        // came_from: 最短経路で各ノードの直前のノードを記録
        std::unordered_map<std::string, std::string> came_from;

        // 優先度キューを使用して、f_costが最小のノードを効率的に取得
        // pair: (f_cost, vertex)
        using PQElement = std::pair<int, std::string>;
        std::priority_queue<PQElement, std::vector<PQElement>, std::greater<PQElement>> open_set;
        open_set.push({f_costs[start_vertex], start_vertex});

        while (!open_set.empty()) {
            // open_setから最もf_costが低いノードを取り出す
            auto [current_f_cost, current_vertex] = open_set.top();
            open_set.pop();

            // 取り出したノードのf_costが記録されているf_costsより大きい場合は古い情報なので無視
            if (current_f_cost > f_costs[current_vertex]) {
                continue;
            }

            // 目標ノードに到達した場合、経路を再構築して返す
            if (current_vertex == end_vertex) {
                return {reconstruct_path(came_from, end_vertex), g_costs[end_vertex]};
            }

            // 現在のノードの隣接ノードを調べる
            const auto* neighbors = get_neighbors(current_vertex);
            if (neighbors == nullptr) { // 孤立したノードの場合など
                continue;
            }

            for (const auto& [neighbor, weight] : *neighbors) {
                // 現在のノードを経由した場合の隣接ノードへの新しいg_cost
                int tentative_g_cost = g_costs[current_vertex] + weight;

                // 新しいg_costが現在記録されている隣接ノードへのg_costよりも小さい場合
                if (tentative_g_cost < g_costs[neighbor]) {
                    // 経路情報を更新
                    came_from[neighbor] = current_vertex;
                    g_costs[neighbor] = tentative_g_cost;
                    f_costs[neighbor] = g_costs[neighbor] + heuristic(neighbor, end_vertex);

                    // 隣接ノードをopen_setに追加（または優先度を更新）
                    open_set.push({f_costs[neighbor], neighbor});
                }
            }
        }

        // open_setが空になっても目標ノードに到達しなかった場合、経路は存在しない
        return {std::vector<std::string>(), std::numeric_limits<int>::max()};
    }

private:
    // 経路を再構築する補助関数
    std::vector<std::string> reconstruct_path(
        const std::unordered_map<std::string, std::string>& came_from,
        std::string current_vertex
    ) const {
        std::vector<std::string> path;
        path.push_back(current_vertex);
        
        while (came_from.find(current_vertex) != came_from.end()) {
            current_vertex = came_from.at(current_vertex);
            path.push_back(current_vertex);
        }
        
        // 経路を逆順にする（開始 -> 目標）
        std::reverse(path.begin(), path.end());
        return path;
    }
};

// ヒューリスティック関数（この例では常に0、ダイクストラ法と同じ）
int dummy_heuristic(const std::string& u, const std::string& v) {
    // u と v の間に何らかの推定距離を計算する関数
    // ここではダミーとして常に0を返す
    return 0;
}

// ベクターの内容を表示する補助関数
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

// タプルの内容を表示する補助関数
void print_edges(const std::vector<std::tuple<std::string, std::string, int>>& edges) {
    std::cout << "[";
    for (size_t i = 0; i < edges.size(); ++i) {
        const auto& [u, v, w] = edges[i];
        std::cout << "(" << u << ", " << v << ", " << w << ")";
        if (i < edges.size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]";
}

int main() {
    std::cout << "A-start TEST -----> start" << std::endl;

    GraphData graph_data;

    graph_data.clear();
    std::vector<std::tuple<std::string, std::string, int>> inputList = {
        {"A", "B", 4}, {"B", "C", 3}, {"B", "D", 2}, {"D", "A", 1}, {"A", "C", 2}, {"B", "D", 2}
    };
    for (const auto& [v1, v2, w] : inputList) {
        graph_data.add_edge(v1, v2, w);
    }
    std::cout << "\nグラフの頂点: ";
    print_vector(graph_data.get_vertices());
    std::cout << std::endl;
    std::cout << "グラフの辺 (重み付き): ";
    print_edges(graph_data.get_edges());
    std::cout << std::endl;
    
    std::pair<std::string, std::string> input = {"A", "B"};
    auto [path, weight] = graph_data.get_shortest_path(input.first, input.second, dummy_heuristic);
    std::cout << "経路" << input.first << "-" << input.second << " の最短経路は ";
    print_vector(path);
    std::cout << " (重み: " << weight << ")" << std::endl;

    graph_data.clear();
    inputList = {
        {"A", "B", 4}, {"C", "D", 4}, {"E", "F", 1}, {"F", "G", 1}
    };
    for (const auto& [v1, v2, w] : inputList) {
        graph_data.add_edge(v1, v2, w);
    }
    std::cout << "\nグラフの頂点: ";
    print_vector(graph_data.get_vertices());
    std::cout << std::endl;
    std::cout << "グラフの辺 (重み付き): ";
    print_edges(graph_data.get_edges());
    std::cout << std::endl;
    
    input = {"A", "B"};
    auto shortest_path = graph_data.get_shortest_path(input.first, input.second, dummy_heuristic);
    std::cout << "経路" << input.first << "-" << input.second << " の最短経路は ";
    print_vector(shortest_path.first);
    std::cout << " (重み: " << shortest_path.second << ")" << std::endl;

    graph_data.clear();
    inputList = {
        {"A", "B", 4}, {"B", "C", 3}, {"D", "E", 5}
    };
    for (const auto& [v1, v2, w] : inputList) {
        graph_data.add_edge(v1, v2, w);
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
    for (const auto& [v1, v2, w] : inputList) {
        graph_data.add_edge(v1, v2, w);
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

    std::cout << "\nA-start TEST <----- end" << std::endl;

    return 0;
}