// Rust
// グラフの最短経路: ワーシャルフロイド法 (Warshall Floyd)

use std::collections::HashMap;
use std::collections::HashSet;
use std::f64::INFINITY;

struct GraphData {
    // 隣接ノードとその辺の重みを格納します。
    // キーは頂点、値はその頂点に隣接する頂点と重みのベクターです。
    data: HashMap<String, Vec<(String, f64)>>,
}

impl GraphData {
    fn new() -> Self {
        GraphData {
            data: HashMap::new(),
        }
    }

    fn get(&self) -> &HashMap<String, Vec<(String, f64)>> {
        &self.data
    }

    fn get_vertices(&self) -> Vec<String> {
        self.data.keys().cloned().collect()
    }

    fn get_edges(&self) -> Vec<(String, String, f64)> {
        let mut edges = HashSet::new();
        for (vertex, neighbors) in &self.data {
            for (neighbor, weight) in neighbors {
                let mut vertices = vec![vertex.clone(), neighbor.clone()];
                vertices.sort();
                edges.insert((vertices[0].clone(), vertices[1].clone(), *weight));
            }
        }
        edges.into_iter().collect()
    }

    fn get_neighbors(&self, vertex: &str) -> Option<&Vec<(String, f64)>> {
        self.data.get(vertex)
    }

    fn add_vertex(&mut self, vertex: &str) -> bool {
        if !self.data.contains_key(vertex) {
            self.data.insert(vertex.to_string(), Vec::new());
        }
        true
    }

    fn add_edge(&mut self, vertex1: &str, vertex2: &str, weight: f64) -> bool {
        if !self.data.contains_key(vertex1) {
            self.add_vertex(vertex1);
        }
        if !self.data.contains_key(vertex2) {
            self.add_vertex(vertex2);
        }

        // vertex1 -> vertex2 の辺を追加（重み付き）
        let mut edge_exists_v1v2 = false;
        if let Some(neighbors) = self.data.get_mut(vertex1) {
            for i in 0..neighbors.len() {
                if neighbors[i].0 == vertex2 {
                    neighbors[i] = (vertex2.to_string(), weight);
                    edge_exists_v1v2 = true;
                    break;
                }
            }
            if !edge_exists_v1v2 {
                neighbors.push((vertex2.to_string(), weight));
            }
        }

        // vertex2 -> vertex1 の辺を追加（重み付き）
        let mut edge_exists_v2v1 = false;
        if let Some(neighbors) = self.data.get_mut(vertex2) {
            for i in 0..neighbors.len() {
                if neighbors[i].0 == vertex1 {
                    neighbors[i] = (vertex1.to_string(), weight);
                    edge_exists_v2v1 = true;
                    break;
                }
            }
            if !edge_exists_v2v1 {
                neighbors.push((vertex1.to_string(), weight));
            }
        }

        true
    }

    fn clear(&mut self) -> bool {
        self.data.clear();
        true
    }

    fn get_shortest_path(&self, start_vertex: &str, end_vertex: &str, _heuristic: fn(&str, &str) -> f64) -> (Option<Vec<String>>, f64) {
        let vertices = self.get_vertices();
        let num_vertices = vertices.len();
        if num_vertices == 0 {
            return (None, INFINITY);
        }

        // 頂点名をインデックスにマッピング
        let mut vertex_to_index: HashMap<&str, usize> = HashMap::new();
        let mut index_to_vertex: HashMap<usize, &str> = HashMap::new();

        for (index, vertex) in vertices.iter().enumerate() {
            vertex_to_index.insert(vertex, index);
            index_to_vertex.insert(index, vertex);
        }

        // 開始・終了頂点が存在するか確認
        if !vertex_to_index.contains_key(start_vertex) || !vertex_to_index.contains_key(end_vertex) {
            println!("ERROR: {} または {} がグラフに存在しません。", start_vertex, end_vertex);
            return (None, INFINITY);
        }

        let start_index = *vertex_to_index.get(start_vertex).unwrap();
        let end_index = *vertex_to_index.get(end_vertex).unwrap();

        // 距離行列 (dist) と経路復元用行列 (next_node) を初期化
        let mut dist = vec![vec![INFINITY; num_vertices]; num_vertices];
        let mut next_node = vec![vec![None as Option<usize>; num_vertices]; num_vertices];

        // 初期距離と経路復元情報を設定
        for i in 0..num_vertices {
            dist[i][i] = 0.0; // 自分自身への距離は0
            if let Some(neighbors) = self.get_neighbors(vertices[i].as_str()) {
                for (neighbor, weight) in neighbors {
                    if let Some(&j) = vertex_to_index.get(neighbor.as_str()) {
                        dist[i][j] = *weight;
                        next_node[i][j] = Some(j); // iからjへの直接辺の場合、iの次はj
                    }
                }
            }
        }

        // ワーシャル-フロイド法の本体
        for k in 0..num_vertices {
            for i in 0..num_vertices {
                for j in 0..num_vertices {
                    if dist[i][k] != INFINITY && dist[k][j] != INFINITY && dist[i][k] + dist[k][j] < dist[i][j] {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        next_node[i][j] = next_node[i][k]; // iからjへの最短経路で、iの次の頂点はiからkへの最短経路でのiの次の頂点
                    }
                }
            }
        }

        // 指定された開始・終了頂点間の最短経路と重みを取得
        let shortest_distance = dist[start_index][end_index];

        // 経路が存在しない場合 (距離がINF)
        if shortest_distance == INFINITY {
            return (None, INFINITY);
        }

        // 経路を復元
        let mut path = Vec::new();
        let mut u = start_index;
        
        // 開始と終了が同じ場合は経路は開始頂点のみ
        if u == end_index {
            path.push(start_vertex.to_string());
        } else {
            // next_nodeを使って経路をたどる
            while u != end_index {
                path.push(index_to_vertex[&u].to_string());
                if let Some(next) = next_node[u][end_index] {
                    u = next;
                    // 無限ループ防止のための簡易チェック
                    if !path.is_empty() && index_to_vertex[&u] == path.last().unwrap() {
                        println!("WARNING: 経路復元中に異常を検出しました（{}でループ？）。", index_to_vertex[&u]);
                        return (None, INFINITY);
                    }
                } else {
                    // next_node が None の場合は到達不能
                    return (None, INFINITY);
                }
            }
            // 最後のノード (end_vertex) を追加
            path.push(end_vertex.to_string());
        }

        (Some(path), shortest_distance)
    }
}

// ヒューリスティック関数 (この例では常に0、ダイクストラ法と同じ)
fn dummy_heuristic(_u: &str, _v: &str) -> f64 {
    0.0
}

fn main() {
    println!("WarshallFloyd -----> start");

    let mut graph_data = GraphData::new();

    graph_data.clear();
    let input_list = vec![
        ("A", "B", 4.0),
        ("B", "C", 3.0),
        ("B", "D", 2.0),
        ("D", "A", 1.0),
        ("A", "C", 2.0),
        ("B", "D", 2.0),
    ];
    for &(v1, v2, weight) in &input_list {
        graph_data.add_edge(v1, v2, weight);
    }
    println!("\nグラフの頂点: {:?}", graph_data.get_vertices());
    println!("グラフの辺 (重み付き): {:?}", graph_data.get_edges());
    let input = ("A", "B");
    let shortest_path = graph_data.get_shortest_path(input.0, input.1, dummy_heuristic);
    println!("経路{}-{} の最短経路は {:?} (重み: {})", input.0, input.1, shortest_path.0, shortest_path.1);

    graph_data.clear();
    let input_list = vec![
        ("A", "B", 4.0),
        ("C", "D", 4.0),
        ("E", "F", 1.0),
        ("F", "G", 1.0),
    ];
    for &(v1, v2, weight) in &input_list {
        graph_data.add_edge(v1, v2, weight);
    }
    println!("\nグラフの頂点: {:?}", graph_data.get_vertices());
    println!("グラフの辺 (重み付き): {:?}", graph_data.get_edges());
    let input = ("A", "B");
    let shortest_path = graph_data.get_shortest_path(input.0, input.1, dummy_heuristic);
    println!("経路{}-{} の最短経路は {:?} (重み: {})", input.0, input.1, shortest_path.0, shortest_path.1);

    graph_data.clear();
    let input_list = vec![
        ("A", "B", 4.0),
        ("B", "C", 3.0),
        ("D", "E", 5.0),
    ];
    for &(v1, v2, weight) in &input_list {
        graph_data.add_edge(v1, v2, weight);
    }
    println!("\nグラフの頂点: {:?}", graph_data.get_vertices());
    println!("グラフの辺 (重み付き): {:?}", graph_data.get_edges());
    let input = ("A", "D");
    let shortest_path = graph_data.get_shortest_path(input.0, input.1, dummy_heuristic);
    println!("経路{}-{} の最短経路は {:?} (重み: {})", input.0, input.1, shortest_path.0, shortest_path.1);

    graph_data.clear();
    let input_list: Vec<(&str, &str, f64)> = vec![];
    for &(v1, v2, weight) in &input_list {
        graph_data.add_edge(v1, v2, weight);
    }
    println!("\nグラフの頂点: {:?}", graph_data.get_vertices());
    println!("グラフの辺 (重み付き): {:?}", graph_data.get_edges());
    let input = ("A", "B");
    let shortest_path = graph_data.get_shortest_path(input.0, input.1, dummy_heuristic);
    println!("経路{}-{} の最短経路は {:?} (重み: {})", input.0, input.1, shortest_path.0, shortest_path.1);

    println!("\nWarshallFloyd <----- end");
}