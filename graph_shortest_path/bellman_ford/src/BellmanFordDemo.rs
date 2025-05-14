// Rust
// グラフの最短経路: ベルマンフォード法 (Bellman Ford)

use std::collections::{HashMap, HashSet};
use std::fmt::Debug;
use std::hash::Hash;

#[derive(Debug)]
struct GraphData<T: Eq + Hash + Clone + Debug> {
    // 隣接ノードとその辺の重みを格納します
    data: HashMap<T, Vec<(T, i32)>>,
}

impl<T: Eq + Hash + Clone + Debug> GraphData<T> {
    fn new() -> Self {
        GraphData {
            data: HashMap::new(),
        }
    }

    fn get(&self) -> &HashMap<T, Vec<(T, i32)>> {
        &self.data
    }

    fn get_vertices(&self) -> Vec<T> {
        self.data.keys().cloned().collect()
    }

    fn get_edges(&self) -> Vec<(T, T, i32)> {
        let mut edges = Vec::new();
        for (u, neighbors) in &self.data {
            for (v, weight) in neighbors {
                edges.push((u.clone(), v.clone(), *weight));
            }
        }
        edges
    }

    fn add_vertex(&mut self, vertex: T) -> bool {
        if !self.data.contains_key(&vertex) {
            self.data.insert(vertex, Vec::new());
            return true;
        }
        true
    }

    fn add_edge(&mut self, vertex1: T, vertex2: T, weight: i32) -> bool {
        self.add_vertex(vertex1.clone());
        self.add_vertex(vertex2.clone());

        // vertex1 -> vertex2 の辺を追加
        let mut edge_updated_v1v2 = false;
        if let Some(neighbors) = self.data.get_mut(&vertex1) {
            for i in 0..neighbors.len() {
                if &neighbors[i].0 == &vertex2 {
                    neighbors[i] = (vertex2.clone(), weight);
                    edge_updated_v1v2 = true;
                    break;
                }
            }
            if !edge_updated_v1v2 {
                neighbors.push((vertex2.clone(), weight));
            }
        }

        // vertex2 -> vertex1 の辺を追加
        let mut edge_updated_v2v1 = false;
        if let Some(neighbors) = self.data.get_mut(&vertex2) {
            for i in 0..neighbors.len() {
                if &neighbors[i].0 == &vertex1 {
                    neighbors[i] = (vertex1.clone(), weight);
                    edge_updated_v2v1 = true;
                    break;
                }
            }
            if !edge_updated_v2v1 {
                neighbors.push((vertex1, weight));
            }
        }

        true
    }

    fn is_empty(&self) -> bool {
        self.data.is_empty()
    }

    fn clear(&mut self) -> bool {
        self.data.clear();
        true
    }

    fn get_shortest_path<F>(&self, start_vertex: &T, end_vertex: &T, _heuristic: F) -> (Option<Vec<T>>, f64)
    where
        F: Fn(&T, &T) -> f64,
    {
        let vertices = self.get_vertices();
        let edges = self.get_edges();
        let num_vertices = vertices.len();

        // 始点と終点の存在チェック
        if !self.data.contains_key(start_vertex) {
            println!("エラー: 始点 '{:?}' がグラフに存在しません。", start_vertex);
            return (None, f64::INFINITY);
        }
        if !self.data.contains_key(end_vertex) {
            println!("エラー: 終点 '{:?}' がグラフに存在しません。", end_vertex);
            return (None, f64::INFINITY);
        }

        // 始点と終点が同じ場合
        if start_vertex == end_vertex {
            return (Some(vec![start_vertex.clone()]), 0.0);
        }

        // 距離と先行頂点の初期化
        let mut dist: HashMap<T, f64> = HashMap::new();
        let mut pred: HashMap<T, Option<T>> = HashMap::new();

        for vertex in &vertices {
            dist.insert(vertex.clone(), f64::INFINITY);
            pred.insert(vertex.clone(), None);
        }
        dist.insert(start_vertex.clone(), 0.0);

        // |V| - 1 回の緩和ステップを実行
        for _ in 0..num_vertices - 1 {
            let mut relaxed_in_this_iteration = false;
            for (u, v, weight) in &edges {
                let weight_f64 = *weight as f64;
                if let Some(dist_u) = dist.get(u) {
                    if *dist_u != f64::INFINITY && *dist_u + weight_f64 < *dist.get(v).unwrap() {
                        dist.insert(v.clone(), *dist_u + weight_f64);
                        pred.insert(v.clone(), Some(u.clone()));
                        relaxed_in_this_iteration = true;
                    }
                }
            }
            if !relaxed_in_this_iteration {
                break;
            }
        }

        // 負閉路の検出
        for (u, v, weight) in &edges {
            let weight_f64 = *weight as f64;
            if let Some(dist_u) = dist.get(u) {
                if *dist_u != f64::INFINITY && *dist_u + weight_f64 < *dist.get(v).unwrap() {
                    // 負閉路が存在します
                    println!("エラー: グラフに負閉路が存在します。最短経路は定義できません。");
                    return (None, f64::NEG_INFINITY);
                }
            }
        }

        // 最短経路の構築
        let mut path = Vec::new();
        let mut current = Some(end_vertex.clone());

        // 終点まで到達不可能かチェック
        if *dist.get(end_vertex).unwrap() == f64::INFINITY {
            return (None, f64::INFINITY);
        }

        // 終点から先行頂点をたどって経路を逆順に構築
        while let Some(vertex) = current {
            path.push(vertex.clone());
            // 始点に到達したらループを終了
            if &vertex == start_vertex {
                break;
            }
            // 次の頂点に進む
            current = pred.get(&vertex).unwrap().clone();
        }

        // 経路が始点から始まっていない場合
        if path.is_empty() || &path[path.len() - 1] != start_vertex {
            return (None, f64::INFINITY);
        }

        path.reverse(); // 経路を始点から終点の順にする

        (Some(path), *dist.get(end_vertex).unwrap())
    }
}

// ヒューリスティック関数
fn dummy_heuristic(_u: &str, _v: &str) -> f64 {
    0.0
}

fn main() {
    println!("BellmanFord TEST -----> start");

    let mut graph_data: GraphData<String> = GraphData::new();

    graph_data.clear();
    let input_list = [
        ("A".to_string(), "B".to_string(), 4),
        ("B".to_string(), "C".to_string(), 3),
        ("B".to_string(), "D".to_string(), 2),
        ("D".to_string(), "A".to_string(), 1),
        ("A".to_string(), "C".to_string(), 2),
        ("B".to_string(), "D".to_string(), 2),
    ];
    for (v1, v2, weight) in input_list {
        graph_data.add_edge(v1, v2, weight);
    }
    println!("\nグラフの頂点: {:?}", graph_data.get_vertices());
    println!("グラフの辺 (重み付き): {:?}", graph_data.get_edges());
    let input = ("A".to_string(), "B".to_string());
    let shortest_path = graph_data.get_shortest_path(&input.0, &input.1, dummy_heuristic);
    println!(
        "経路{}-{} の最短経路は {:?} (重み: {})",
        input.0, input.1, shortest_path.0, shortest_path.1
    );

    graph_data.clear();
    let input_list = [
        ("A".to_string(), "B".to_string(), 4),
        ("C".to_string(), "D".to_string(), 4),
        ("E".to_string(), "F".to_string(), 1),
        ("F".to_string(), "G".to_string(), 1),
    ];
    for (v1, v2, weight) in input_list {
        graph_data.add_edge(v1, v2, weight);
    }
    println!("\nグラフの頂点: {:?}", graph_data.get_vertices());
    println!("グラフの辺 (重み付き): {:?}", graph_data.get_edges());
    let input = ("A".to_string(), "B".to_string());
    let shortest_path = graph_data.get_shortest_path(&input.0, &input.1, dummy_heuristic);
    println!(
        "経路{}-{} の最短経路は {:?} (重み: {})",
        input.0, input.1, shortest_path.0, shortest_path.1
    );

    graph_data.clear();
    let input_list = [
        ("A".to_string(), "B".to_string(), 4),
        ("B".to_string(), "C".to_string(), 3),
        ("D".to_string(), "E".to_string(), 5),
    ];
    for (v1, v2, weight) in input_list {
        graph_data.add_edge(v1, v2, weight);
    }
    println!("\nグラフの頂点: {:?}", graph_data.get_vertices());
    println!("グラフの辺 (重み付き): {:?}", graph_data.get_edges());
    let input = ("A".to_string(), "D".to_string());
    let shortest_path = graph_data.get_shortest_path(&input.0, &input.1, dummy_heuristic);
    println!(
        "経路{}-{} の最短経路は {:?} (重み: {})",
        input.0, input.1, shortest_path.0, shortest_path.1
    );

    graph_data.clear();
    println!("\nグラフの頂点: {:?}", graph_data.get_vertices());
    println!("グラフの辺 (重み付き): {:?}", graph_data.get_edges());
    let input = ("A".to_string(), "B".to_string());
    let shortest_path = graph_data.get_shortest_path(&input.0, &input.1, dummy_heuristic);
    println!(
        "経路{}-{} の最短経路は {:?} (重み: {})",
        input.0, input.1, shortest_path.0, shortest_path.1
    );

    println!("\nBellmanFord TEST <----- end");
}
