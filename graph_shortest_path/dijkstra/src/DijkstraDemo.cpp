// C++
// グラフの最短経路: ダイクストラ法 (dijkstra)

#include <iostream>
#include <vector>
#include <map>
#include <queue>
#include <set>
#include <limits>
#include <algorithm>
#include <string>

class GraphData {
private:
    // 隣接ノードとその辺の重みを格納します。
    // キーは頂点、値はその頂点に隣接する頂点と重みのペアのベクターです。
    std::map<std::string, std::vector<std::pair<std::string, int>>> _data;

public:
    GraphData() {}

    // グラフの内部データを取得します。
    std::map<std::string, std::vector<std::pair<std::string, int>>> get() {
        return _data;
    }

    // グラフの全頂点をベクターとして返します。
    std::vector<std::string> get_vertices() {
        std::vector<std::string> vertices;
        for (const auto& pair : _data) {
            vertices.push_back(pair.first);
        }
        return vertices;
    }

    // グラフの全辺をベクターとして返します。
    // 無向グラフの場合、(u, v, weight) の形式で返します。
    std::vector<std::tuple<std::string, std::string, int>> get_edges() {
        std::set<std::tuple<std::string, std::string, int>> edges;
        for (const auto& vertex_pair : _data) {
            const std::string& vertex = vertex_pair.first;
            for (const auto& neighbor_pair : vertex_pair.second) {
                const std::string& neighbor = neighbor_pair.first;
                int weight = neighbor_pair.second;
                
                // 辺を正規化して追加（小さい方の頂点を最初にする）
                std::string edge_first = vertex;
                std::string edge_second = neighbor;
                if (edge_first > edge_second) {
                    std::swap(edge_first, edge_second);
                }
                edges.insert(std::make_tuple(edge_first, edge_second, weight));
            }
        }
        
        return std::vector<std::tuple<std::string, std::string, int>>(edges.begin(), edges.end());
    }

    // 指定された頂点の隣接ノードと辺の重みのベクターを返します。
    std::vector<std::pair<std::string, int>> get_neighbors(const std::string& vertex) {
        if (_data.find(vertex) != _data.end()) {
            return _data[vertex];
        } else {
            return {}; // 頂点が存在しない場合は空のベクターを返す
        }
    }

    // 新しい頂点をグラフに追加します。
    bool add_vertex(const std::string& vertex) {
        if (_data.find(vertex) == _data.end()) {
            _data[vertex] = {};
        }
        return true;
    }

    // 両頂点間に辺を追加します。重みを指定します。
    bool add_edge(const std::string& vertex1, const std::string& vertex2, int weight) {
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

    // グラフを空にします。
    bool clear() {
        _data.clear();
        return true;
    }

    // 最短経路を取得します。
    std::pair<std::vector<std::string>, double> get_shortest_path(
        const std::string& start_vertex, 
        const std::string& end_vertex, 
        double (*heuristic)(const std::string&, const std::string&)
    ) {
        if (_data.find(start_vertex) == _data.end() || _data.find(end_vertex) == _data.end()) {
            std::cout << "ERROR: 開始頂点 '" << start_vertex << "' または 終了頂点 '" 
                      << end_vertex << "' がグラフに存在しません。" << std::endl;
            return std::make_pair(std::vector<std::string>(), std::numeric_limits<double>::infinity());
        }

        // 距離を初期化: 全ての頂点への距離を無限大に設定し、開始頂点のみ0とする
        std::map<std::string, double> distances;
        for (const auto& vertex_pair : _data) {
            distances[vertex_pair.first] = std::numeric_limits<double>::infinity();
        }
        distances[start_vertex] = 0;

        // 経路を記録するための辞書: 各頂点に至る直前の頂点を保持
        std::map<std::string, std::string> predecessors;
        for (const auto& vertex_pair : _data) {
            predecessors[vertex_pair.first] = "";
        }

        // 優先度付きキュー: (距離, 頂点) のペアを格納し、距離が小さい順に取り出す
        // C++の優先度付きキューはデフォルトで最大ヒープなので、距離にマイナスを付けるか比較関数を反転する
        typedef std::pair<double, std::string> PQElement;
        std::priority_queue<PQElement, std::vector<PQElement>, std::greater<PQElement>> priority_queue;
        
        priority_queue.push(std::make_pair(0.0, start_vertex));

        while (!priority_queue.empty()) {
            // 優先度付きキューから最も距離の小さい頂点を取り出す
            double current_distance = priority_queue.top().first;
            std::string current_vertex = priority_queue.top().second;
            priority_queue.pop();

            // 取り出した頂点への距離が、すでに記録されている距離より大きい場合は、
            // より短い経路が既に見つかっているためスキップ
            if (current_distance > distances[current_vertex]) {
                continue;
            }

            // 終了頂点に到達したら探索終了
            if (current_vertex == end_vertex) {
                break; // 最短経路が見つかった
            }

            // 現在の頂点から到達可能な隣接頂点を探索
            for (const auto& neighbor_pair : get_neighbors(current_vertex)) {
                const std::string& neighbor = neighbor_pair.first;
                int weight = neighbor_pair.second;
                double distance_through_current = distances[current_vertex] + weight;

                // より短い経路が見つかった場合
                if (distance_through_current < distances[neighbor]) {
                    distances[neighbor] = distance_through_current;
                    predecessors[neighbor] = current_vertex;
                    // 優先度付きキューに隣接頂点を追加または更新
                    // ダイクストラ法では heuristic は使用しない (または h=0)
                    priority_queue.push(std::make_pair(distance_through_current, neighbor));
                }
            }
        }

        // 終了頂点への最短距離が無限大のままなら、到達不可能
        if (distances[end_vertex] == std::numeric_limits<double>::infinity()) {
            std::cout << "INFO: 開始頂点 '" << start_vertex << "' から 終了頂点 '" 
                      << end_vertex << "' への経路は存在しません。" << std::endl;
            return std::make_pair(std::vector<std::string>(), std::numeric_limits<double>::infinity());
        }

        // 最短経路を再構築
        std::vector<std::string> path;
        std::string current = end_vertex;
        while (!current.empty()) {
            path.push_back(current);
            current = predecessors[current];
        }
        std::reverse(path.begin(), path.end()); // 経路は逆順に構築されたので反転

        // 開始ノードから開始されていることを確認
        if (path[0] != start_vertex) {
            return std::make_pair(std::vector<std::string>(), std::numeric_limits<double>::infinity());
        }

        return std::make_pair(path, distances[end_vertex]);
    }
};

// ヒューリスティック関数 (この例では常に0、ダイクストラ法と同じ)
double dummy_heuristic(const std::string& u, const std::string& v) {
    // u と v の間に何らかの推定距離を計算する関数
    // ここではダミーとして常に0を返す
    return 0.0;
}

// ベクターの内容を表示する関数
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

// 辺リストを表示する関数
void print_edges(const std::vector<std::tuple<std::string, std::string, int>>& edges) {
    std::cout << "[";
    for (size_t i = 0; i < edges.size(); ++i) {
        const auto& edge = edges[i];
        std::cout << "('" << std::get<0>(edge) << "', '" << std::get<1>(edge) << "', " 
                  << std::get<2>(edge) << ")";
        if (i < edges.size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]";
}

int main() {
    std::cout << "Dijkstra -----> start" << std::endl;

    GraphData graph_data;

    graph_data.clear();
    std::vector<std::tuple<std::string, std::string, int>> inputList = {
        std::make_tuple("A", "B", 4), 
        std::make_tuple("B", "C", 3), 
        std::make_tuple("B", "D", 2), 
        std::make_tuple("D", "A", 1), 
        std::make_tuple("A", "C", 2), 
        std::make_tuple("B", "D", 2)
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
    
    std::pair<std::string, std::string> input = std::make_pair("A", "B");
    auto shortest_path = graph_data.get_shortest_path(input.first, input.second, dummy_heuristic);
    std::cout << "経路" << input.first << "-" << input.second << " の最短経路は ";
    print_vector(shortest_path.first);
    std::cout << " (重み: " << shortest_path.second << ")" << std::endl;

    graph_data.clear();
    inputList = {
        std::make_tuple("A", "B", 4), 
        std::make_tuple("C", "D", 4), 
        std::make_tuple("E", "F", 1), 
        std::make_tuple("F", "G", 1)
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
    
    input = std::make_pair("A", "B");
    shortest_path = graph_data.get_shortest_path(input.first, input.second, dummy_heuristic);
    std::cout << "経路" << input.first << "-" << input.second << " の最短経路は ";
    print_vector(shortest_path.first);
    std::cout << " (重み: " << shortest_path.second << ")" << std::endl;

    graph_data.clear();
    inputList = {
        std::make_tuple("A", "B", 4), 
        std::make_tuple("B", "C", 3), 
        std::make_tuple("D", "E", 5)
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
    
    input = std::make_pair("A", "D");
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
    
    input = std::make_pair("A", "B");
    shortest_path = graph_data.get_shortest_path(input.first, input.second, dummy_heuristic);
    std::cout << "経路" << input.first << "-" << input.second << " の最短経路は ";
    print_vector(shortest_path.first);
    std::cout << " (重み: " << shortest_path.second << ")" << std::endl;

    std::cout << "\nDijkstra <----- end" << std::endl;

    return 0;
}