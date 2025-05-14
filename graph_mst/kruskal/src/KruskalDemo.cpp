// C++
// グラフの最小全域木: クラスカル法 (Kruskal)

#include <iostream>
#include <vector>
#include <unordered_map>
#include <map>
#include <set>
#include <algorithm>
#include <utility>
#include <string>

class DSU {
private:
    std::unordered_map<std::string, std::string> parent;
    std::unordered_map<std::string, int> rank;

public:
    DSU(const std::vector<std::string>& vertices) {
        // 各頂点の親を格納します。最初は各頂点自身が親です。
        for (const auto& v : vertices) {
            parent[v] = v;
            rank[v] = 0;
        }
    }

    // 頂点 i が属する集合の代表元（根）を見つけます。
    // パス圧縮により効率化されます。
    std::string find(const std::string& i) {
        if (parent[i] == i) {
            return i;
        }
        // パス圧縮: 探索中に encountered したノードを根に直接つなぎ直します。
        parent[i] = find(parent[i]);
        return parent[i];
    }

    // 頂点 i と 頂点 j を含む二つの集合を結合します。
    // ランクによるunionにより効率化されます。
    bool union_sets(const std::string& i, const std::string& j) {
        std::string root_i = find(i);
        std::string root_j = find(j);

        // 根が異なる場合のみ結合します（同じ集合に属している場合は何もしません）。
        if (root_i != root_j) {
            // ランクが小さい方の木を、ランクが大きい方の木の根に付けます。
            if (rank[root_i] < rank[root_j]) {
                parent[root_i] = root_j;
            }
            else if (rank[root_i] > rank[root_j]) {
                parent[root_j] = root_i;
            }
            else {
                // ランクが同じ場合はどちらかを親とし、その親のランクを増やします。
                parent[root_j] = root_i;
                rank[root_i] += 1;
            }
            return true; // 集合が結合された
        }
        return false; // 既に同じ集合に属していた
    }
};

// 重みを扱えるように改変された GraphData クラス
class GraphData {
private:
    // 隣接ノードとその辺の重みを格納します。
    std::unordered_map<std::string, std::vector<std::pair<std::string, int>>> _data;

public:
    std::unordered_map<std::string, std::vector<std::pair<std::string, int>>> get() {
        // グラフの内部データを取得します。
        return _data;
    }

    std::vector<std::string> get_vertices() {
        // グラフの全頂点をリストとして返します。
        std::vector<std::string> vertices;
        for (const auto& entry : _data) {
            vertices.push_back(entry.first);
        }
        return vertices;
    }

    std::vector<std::tuple<std::string, std::string, int>> get_edges() {
        // グラフの全辺をリストとして返します。
        // 無向グラフの場合、(u, v, weight) の形式で返します。
        // 重複を避けるためにセットを使用します。
        std::set<std::tuple<std::string, std::string, int>> edges;
        for (const auto& vertex_entry : _data) {
            const std::string& vertex = vertex_entry.first;
            for (const auto& neighbor_entry : vertex_entry.second) {
                const std::string& neighbor = neighbor_entry.first;
                int weight = neighbor_entry.second;
                
                // 辺を正規化してセットに追加 (小さい方の頂点を最初にするなど)
                std::string u = vertex;
                std::string v = neighbor;
                if (u > v) std::swap(u, v);
                
                edges.insert(std::make_tuple(u, v, weight)); // (u, v, weight) の形式で格納
            }
        }
        return std::vector<std::tuple<std::string, std::string, int>>(edges.begin(), edges.end());
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
            _data[vertex1].push_back(std::make_pair(vertex2, weight));
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
            _data[vertex2].push_back(std::make_pair(vertex1, weight));
        }
        
        return true;
    }

    bool clear() {
        // グラフを空にします。
        _data.clear();
        return true;
    }

    std::vector<std::tuple<std::string, std::string, int>> get_mst() {
        // 1. 全ての辺を取得し、重みでソートします。
        // get_edges() は (u, v, weight) のリストを返します。
        auto edges = get_edges();
        // 重み (タプルの3番目の要素) をキーとして辺をソート
        std::sort(edges.begin(), edges.end(), 
            [](const std::tuple<std::string, std::string, int>& a, const std::tuple<std::string, std::string, int>& b) {
                return std::get<2>(a) < std::get<2>(b);
            });

        // 2. Union-Findデータ構造を初期化します。
        // 各頂点が自身の集合に属するようにします。
        auto vertices = get_vertices();
        DSU dsu(vertices);

        // 3. MSTを構築します。
        // 結果として得られるMSTの辺を格納するリスト
        std::vector<std::tuple<std::string, std::string, int>> mst_edges;
        // MSTに追加された辺の数 (頂点数-1 になればMSTが完成)
        size_t edges_count = 0;

        // ソートされた辺を順番に調べます。
        for (const auto& edge : edges) {
            const std::string& u = std::get<0>(edge);
            const std::string& v = std::get<1>(edge);
            int weight = std::get<2>(edge);
            
            // 辺 (u, v) の両端点が属する集合の代表元（根）を見つけます。
            std::string root_u = dsu.find(u);
            std::string root_v = dsu.find(v);

            // 両端点が異なる集合に属する場合、その辺をMSTに追加してもサイクルは形成されません。
            if (root_u != root_v) {
                // 辺をMSTに追加します。
                mst_edges.push_back(edge);
                // 辺を追加したので、両端点の集合を結合します。
                dsu.union_sets(u, v);
                // MSTに追加した辺の数を増やします。
                edges_count++;

                // 頂点数から1を引いた数の辺がMSTに追加されたら終了です。
                // これはグラフが連結である場合に限り、完全なMSTが得られたことを意味します。
                // 連結でない場合は、全ての辺を調べ終えるまで続行し、最小全域森を得ます。
                if (edges_count == vertices.size() - 1 && !vertices.empty()) {
                    break;
                }
            }
        }

        // MST (または最小全域森) の辺のリストを返します。
        return mst_edges;
    }
};

int main() {
    std::cout << "Kruskal TEST -----> start" << std::endl;
    GraphData graph_data;

    graph_data.clear();
    std::vector<std::tuple<std::string, std::string, int>> inputList = {
        {"A", "B", 4}, {"B", "C", 3}, {"B", "D", 2}, {"D", "A", 1}, {"A", "C", 2}, {"B", "D", 2}
    };
    for (const auto& input : inputList) {
        graph_data.add_edge(std::get<0>(input), std::get<1>(input), std::get<2>(input));
    }
    std::cout << "\nグラフの頂点:";
    for (const auto& v : graph_data.get_vertices()) {
        std::cout << " " << v;
    }
    std::cout << std::endl;
    
    std::cout << "グラフの辺 (重み付き):";
    for (const auto& e : graph_data.get_edges()) {
        std::cout << " (" << std::get<0>(e) << "," << std::get<1>(e) << "," << std::get<2>(e) << ")";
    }
    std::cout << std::endl;
    
    auto outputMst = graph_data.get_mst();
    for (const auto& edge : outputMst) {
        std::cout << "Edge: " << std::get<0>(edge) << " - " << std::get<1>(edge) << ", Weight: " << std::get<2>(edge) << std::endl;
    }
    int total_weight = 0;
    for (const auto& edge : outputMst) {
        total_weight += std::get<2>(edge);
    }
    std::cout << "最小全域木の合計重み: " << total_weight << std::endl;

    graph_data.clear();
    inputList = {{"A", "B", 4}, {"C", "D", 4}, {"E", "F", 1}, {"F", "G", 1}};
    for (const auto& input : inputList) {
        graph_data.add_edge(std::get<0>(input), std::get<1>(input), std::get<2>(input));
    }
    std::cout << "\nグラフの頂点:";
    for (const auto& v : graph_data.get_vertices()) {
        std::cout << " " << v;
    }
    std::cout << std::endl;
    
    std::cout << "グラフの辺 (重み付き):";
    for (const auto& e : graph_data.get_edges()) {
        std::cout << " (" << std::get<0>(e) << "," << std::get<1>(e) << "," << std::get<2>(e) << ")";
    }
    std::cout << std::endl;
    
    outputMst = graph_data.get_mst();
    for (const auto& edge : outputMst) {
        std::cout << "Edge: " << std::get<0>(edge) << " - " << std::get<1>(edge) << ", Weight: " << std::get<2>(edge) << std::endl;
    }
    total_weight = 0;
    for (const auto& edge : outputMst) {
        total_weight += std::get<2>(edge);
    }
    std::cout << "最小全域木の合計重み: " << total_weight << std::endl;

    graph_data.clear();
    inputList = {{"A", "B", 4}, {"B", "C", 3}, {"D", "E", 5}};
    for (const auto& input : inputList) {
        graph_data.add_edge(std::get<0>(input), std::get<1>(input), std::get<2>(input));
    }
    std::cout << "\nグラフの頂点:";
    for (const auto& v : graph_data.get_vertices()) {
        std::cout << " " << v;
    }
    std::cout << std::endl;
    
    std::cout << "グラフの辺 (重み付き):";
    for (const auto& e : graph_data.get_edges()) {
        std::cout << " (" << std::get<0>(e) << "," << std::get<1>(e) << "," << std::get<2>(e) << ")";
    }
    std::cout << std::endl;
    
    outputMst = graph_data.get_mst();
    for (const auto& edge : outputMst) {
        std::cout << "Edge: " << std::get<0>(edge) << " - " << std::get<1>(edge) << ", Weight: " << std::get<2>(edge) << std::endl;
    }
    total_weight = 0;
    for (const auto& edge : outputMst) {
        total_weight += std::get<2>(edge);
    }
    std::cout << "最小全域木の合計重み: " << total_weight << std::endl;

    graph_data.clear();
    inputList = {};
    for (const auto& input : inputList) {
        graph_data.add_edge(std::get<0>(input), std::get<1>(input), std::get<2>(input));
    }
    std::cout << "\nグラフの頂点:";
    for (const auto& v : graph_data.get_vertices()) {
        std::cout << " " << v;
    }
    std::cout << std::endl;
    
    std::cout << "グラフの辺 (重み付き):";
    for (const auto& e : graph_data.get_edges()) {
        std::cout << " (" << std::get<0>(e) << "," << std::get<1>(e) << "," << std::get<2>(e) << ")";
    }
    std::cout << std::endl;
    
    outputMst = graph_data.get_mst();
    for (const auto& edge : outputMst) {
        std::cout << "Edge: " << std::get<0>(edge) << " - " << std::get<1>(edge) << ", Weight: " << std::get<2>(edge) << std::endl;
    }
    total_weight = 0;
    for (const auto& edge : outputMst) {
        total_weight += std::get<2>(edge);
    }
    std::cout << "最小全域木の合計重み: " << total_weight << std::endl;

    std::cout << "\nKruskal TEST <----- end" << std::endl;
    return 0;
}