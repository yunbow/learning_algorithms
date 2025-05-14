// Rust
// グラフの最短経路: ダイクストラ法 (dijkstra)

use std::cmp::Ordering;
use std::collections::{BinaryHeap, HashMap};

// BinaryHeapでの優先度付きキューの要素のための構造体
#[derive(Copy, Clone, Eq, PartialEq)]
struct State {
    cost: u32,
    vertex: String,
}

// BinaryHeapは最大ヒープなので、逆順で比較する必要がある
impl Ord for State {
    fn cmp(&self, other: &Self) -> Ordering {
        // コストが小さい方を優先するため、otherと自分を比較
        other.cost.cmp(&self.cost)
            .then_with(|| self.vertex.cmp(&other.vertex))
    }
}

impl PartialOrd for State {
    fn partial_cmp(&self, other: &Self) -> Option<Ordering> {
        Some(self.cmp(other))
    }
}

struct GraphData {
    // 隣接ノードとその辺の重みを格納します
    // キーは頂点、値はその頂点に隣接する頂点と重みのベクター
    data: HashMap<String, Vec<(String, u32)>>,
}

impl GraphData {
    fn new() -> Self {
        GraphData {
            data: HashMap::new(),
        }
    }

    fn get(&self) -> &HashMap<String, Vec<(String, u32)>> {
        &self.data
    }

    fn get_vertices(&self) -> Vec<String> {
        self.data.keys().cloned().collect()
    }

    fn get_edges(&self) -> Vec<(String, String, u32)> {
        let mut edges = Vec::new();
        let mut seen = std::collections::HashSet::new();

        for (vertex, neighbors) in &self.data {
            for (neighbor, weight) in neighbors {
                // 辺を正規化して重複を避ける
                let (v1, v2) = if vertex < neighbor {
                    (vertex.clone(), neighbor.clone())
                } else {
                    (neighbor.clone(), vertex.clone())
                };

                let edge_key = format!("{}-{}", v1, v2);
                if !seen.contains(&edge_key) {
                    edges.push((v1, v2, *weight));
                    seen.insert(edge_key);
                }
            }
        }
        edges
    }

    fn get_neighbors(&self, vertex: &str) -> Option<&Vec<(String, u32)>> {
        self.data.get(vertex)
    }

    fn add_vertex(&mut self, vertex: &str) -> bool {
        self.data.entry(vertex.to_string()).or_insert_with(Vec::new);
        true
    }

    fn add_edge(&mut self, vertex1: &str, vertex2: &str, weight: u32) -> bool {
        // 頂点がグラフに存在しない場合は追加
        if !self.data.contains_key(vertex1) {
            self.add_vertex(vertex1);
        }
        if !self.data.contains_key(vertex2) {
            self.add_vertex(vertex2);
        }
        
        // vertex1 -> vertex2 の辺を追加または更新
        let mut edge_exists_v1v2 = false;
        if let Some(neighbors) = self.data.get_mut(vertex1) {
            for i in 0..neighbors.len() {
                if neighbors[i].0 == vertex2 {
                    neighbors[i].1 = weight;
                    edge_exists_v1v2 = true;
                    break;
                }
            }
            if !edge_exists_v1v2 {
                neighbors.push((vertex2.to_string(), weight));
            }
        }

        // vertex2 -> vertex1 の辺を追加または更新
        let mut edge_exists_v2v1 = false;
        if let Some(neighbors) = self.data.get_mut(vertex2) {
            for i in 0..neighbors.len() {
                if neighbors[i].0 == vertex1 {
                    neighbors[i].1 = weight;
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

    fn get_shortest_path(&self, start_vertex: &str, end_vertex: &str, _heuristic: fn(&str, &str) -> u32) -> (Option<Vec<String>>, u32) {
        if !self.data.contains_key(start_vertex) || !self.data.contains_key(end_vertex) {
            println!("ERROR: 開始頂点 '{}' または 終了頂点 '{}' がグラフに存在しません。", start_vertex, end_vertex);
            return (None, std::u32::MAX);
        }

        // 距離を初期化: 全ての頂点への距離を無限大に設定し、開始頂点のみ0とする
        let mut distances: HashMap<String, u32> = self.data.keys()
            .map(|vertex| (vertex.clone(), std::u32::MAX))
            .collect();
        distances.insert(start_vertex.to_string(), 0);

        // 経路を記録するための辞書: 各頂点に至る直前の頂点を保持
        let mut predecessors: HashMap<String, Option<String>> = self.data.keys()
            .map(|vertex| (vertex.clone(), None))
            .collect();

        // 優先度付きキュー: (距離, 頂点) のタプルを格納し、距離が小さい順に取り出す
        let mut priority_queue = BinaryHeap::new();
        priority_queue.push(State {
            cost: 0,
            vertex: start_vertex.to_string(),
        });

        while let Some(State { cost, vertex }) = priority_queue.pop() {
            // 取り出した頂点への距離が、すでに記録されている距離より大きい場合はスキップ
            if cost > distances[&vertex] {
                continue;
            }

            // 終了頂点に到達したら探索終了
            if vertex == end_vertex {
                break;
            }

            // 現在の頂点から到達可能な隣接頂点を探索
            if let Some(neighbors) = self.get_neighbors(&vertex) {
                for (neighbor, weight) in neighbors {
                    let distance_through_current = match distances[&vertex].checked_add(*weight) {
                        Some(val) => val,
                        None => continue, // オーバーフロー防止
                    };

                    // より短い経路が見つかった場合
                    if distance_through_current < distances[neighbor] {
                        distances.insert(neighbor.clone(), distance_through_current);
                        predecessors.insert(neighbor.clone(), Some(vertex.clone()));
                        // 優先度付きキューに隣接頂点を追加または更新
                        priority_queue.push(State {
                            cost: distance_through_current,
                            vertex: neighbor.clone(),
                        });
                    }
                }
            }
        }

        // 終了頂点への最短距離が無限大のままなら、到達不可能
        if distances[end_vertex] == std::u32::MAX {
            println!("INFO: 開始頂点 '{}' から 終了頂点 '{}' への経路は存在しません。", start_vertex, end_vertex);
            return (None, std::u32::MAX);
        }

        // 最短経路を再構築
        let mut path = Vec::new();
        let mut current = end_vertex.to_string();
        while current != start_vertex {
            path.push(current.clone());
            if let Some(Some(prev)) = predecessors.get(&current) {
                current = prev.clone();
            } else {
                // これは起こらないはずだが、安全のため
                return (None, std::u32::MAX);
            }
        }
        path.push(start_vertex.to_string());
        path.reverse(); // 経路は逆順に構築されたので反転

        (Some(path), distances[end_vertex])
    }
}

// ヒューリスティック関数 (この例では常に0、ダイクストラ法と同じ)
fn dummy_heuristic(_u: &str, _v: &str) -> u32 {
    0
}

fn main() {
    println!("Dijkstra -----> start");

    let mut graph_data = GraphData::new();

    graph_data.clear();
    let input_list = vec![
        ("A", "B", 4), ("B", "C", 3), ("B", "D", 2), 
        ("D", "A", 1), ("A", "C", 2), ("B", "D", 2)
    ];
    for &(v1, v2, weight) in &input_list {
        graph_data.add_edge(v1, v2, weight);
    }
    println!("\nグラフの頂点: {:?}", graph_data.get_vertices());
    println!("グラフの辺 (重み付き): {:?}", graph_data.get_edges());
    let input = ("A", "B");
    let shortest_path = graph_data.get_shortest_path(input.0, input.1, dummy_heuristic);
    println!("経路{}-{} の最短経路は {:?} (重み: {})", 
             input.0, input.1, shortest_path.0, 
             if shortest_path.1 == std::u32::MAX { "無限大".to_string() } else { shortest_path.1.to_string() });

    graph_data.clear();
    let input_list = vec![
        ("A", "B", 4), ("C", "D", 4), ("E", "F", 1), ("F", "G", 1)
    ];
    for &(v1, v2, weight) in &input_list {
        graph_data.add_edge(v1, v2, weight);
    }
    println!("\nグラフの頂点: {:?}", graph_data.get_vertices());
    println!("グラフの辺 (重み付き): {:?}", graph_data.get_edges());
    let input = ("A", "B");
    let shortest_path = graph_data.get_shortest_path(input.0, input.1, dummy_heuristic);
    println!("経路{}-{} の最短経路は {:?} (重み: {})", 
             input.0, input.1, shortest_path.0, 
             if shortest_path.1 == std::u32::MAX { "無限大".to_string() } else { shortest_path.1.to_string() });

    graph_data.clear();
    let input_list = vec![
        ("A", "B", 4), ("B", "C", 3), ("D", "E", 5)
    ];
    for &(v1, v2, weight) in &input_list {
        graph_data.add_edge(v1, v2, weight);
    }
    println!("\nグラフの頂点: {:?}", graph_data.get_vertices());
    println!("グラフの辺 (重み付き): {:?}", graph_data.get_edges());
    let input = ("A", "D");
    let shortest_path = graph_data.get_shortest_path(input.0, input.1, dummy_heuristic);
    println!("経路{}-{} の最短経路は {:?} (重み: {})", 
             input.0, input.1, shortest_path.0, 
             if shortest_path.1 == std::u32::MAX { "無限大".to_string() } else { shortest_path.1.to_string() });

    graph_data.clear();
    let input_list: Vec<(&str, &str, u32)> = vec![];
    for &(v1, v2, weight) in &input_list {
        graph_data.add_edge(v1, v2, weight);
    }
    println!("\nグラフの頂点: {:?}", graph_data.get_vertices());
    println!("グラフの辺 (重み付き): {:?}", graph_data.get_edges());
    let input = ("A", "B");
    let shortest_path = graph_data.get_shortest_path(input.0, input.1, dummy_heuristic);
    println!("経路{}-{} の最短経路は {:?} (重み: {})", 
             input.0, input.1, shortest_path.0, 
             if shortest_path.1 == std::u32::MAX { "無限大".to_string() } else { shortest_path.1.to_string() });

    println!("\nDijkstra <----- end");
}