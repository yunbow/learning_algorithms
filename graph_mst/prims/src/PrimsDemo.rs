// Rust
// グラフの最小全域木: プリム法 (Prim)

use std::collections::{BinaryHeap, HashMap, HashSet};
use std::cmp::Reverse;

#[derive(Debug)]
struct GraphData {
    // 隣接リストとしてグラフデータを格納
    // キーは頂点、値はその頂点に隣接する頂点とその辺の重みのタプルのベクター
    data: HashMap<String, Vec<(String, i32)>>,
}

impl GraphData {
    fn new() -> Self {
        GraphData {
            data: HashMap::new(),
        }
    }

    fn get(&self) -> &HashMap<String, Vec<(String, i32)>> {
        // グラフの内部データを取得
        &self.data
    }

    fn get_vertices(&self) -> Vec<String> {
        // グラフの全頂点をベクターとして返す
        self.data.keys().cloned().collect()
    }

    fn get_edges(&self) -> Vec<(String, String, i32)> {
        // グラフの全辺をベクターとして返す
        // 無向グラフの場合、(u, v, weight) の形式で返す
        // 重複を避けるためにハッシュセットを使用
        let mut edges = HashSet::new();
        
        for (vertex, neighbors) in &self.data {
            for (neighbor, weight) in neighbors {
                // 辺を正規化して追加（小さい方の頂点を最初にする）
                let mut edge = vec![vertex.clone(), neighbor.clone()];
                edge.sort();
                edges.insert((edge[0].clone(), edge[1].clone(), *weight));
            }
        }
        
        edges.into_iter().collect()
    }

    fn get_neighbors(&self, vertex: &str) -> Option<&Vec<(String, i32)>> {
        // 指定された頂点の隣接ノードと辺の重みのリストを返す
        self.data.get(vertex)
    }

    fn get_edge_weight(&self, vertex1: &str, vertex2: &str) -> Option<i32> {
        // 指定された2つの頂点間の辺の重みを返す
        if let Some(neighbors) = self.data.get(vertex1) {
            for (neighbor, weight) in neighbors {
                if neighbor == vertex2 {
                    return Some(*weight);
                }
            }
        }
        None
    }

    fn add_vertex(&mut self, vertex: &str) -> bool {
        // 新しい頂点をグラフに追加
        self.data.entry(vertex.to_string()).or_insert_with(Vec::new);
        true
    }

    fn add_edge(&mut self, vertex1: &str, vertex2: &str, weight: i32) -> bool {
        // 両頂点間に辺を追加、重みを指定
        // 頂点がグラフに存在しない場合は追加
        self.add_vertex(vertex1);
        self.add_vertex(vertex2);

        // vertex1 -> vertex2 の辺を追加（重み付き）
        let mut edge_exists_v1v2 = false;
        if let Some(neighbors) = self.data.get_mut(vertex1) {
            for i in 0..neighbors.len() {
                if neighbors[i].0 == vertex2 {
                    neighbors[i].1 = weight; // 既に存在する場合は重みを更新
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
                    neighbors[i].1 = weight; // 既に存在する場合は重みを更新
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

    fn is_empty(&self) -> bool {
        // グラフが空かどうかを返す
        self.data.is_empty()
    }

    fn clear(&mut self) -> bool {
        // グラフを空にする
        self.data.clear();
        true
    }

    fn get_mst(&self, start_vertex: Option<&str>) -> Vec<(String, String, i32)> {
        let vertices = self.get_vertices();
        if vertices.is_empty() {
            return vec![]; // グラフが空
        }

        let start = match start_vertex {
            Some(v) if self.data.contains_key(v) => v.to_string(),
            _ => vertices[0].clone(), // デフォルトまたは指定された頂点が存在しない場合は最初の頂点を使用
        };

        // MSTに含まれる頂点のセット
        let mut in_mst = HashSet::new();
        // 優先度付きキュー (重み, 現在の頂点, 遷移元の頂点)
        // BinaryHeapは最大ヒープなので、Reverseを使って最小ヒープにする
        let mut min_heap = BinaryHeap::new();
        // MSTを構成する辺のリスト
        let mut mst_edges = Vec::new();
        // 各頂点への最小コストと親頂点を記録
        let mut min_cost = HashMap::new();
        let mut parent = HashMap::new();

        // 初期化
        for v in &vertices {
            min_cost.insert(v.clone(), f64::INFINITY);
            parent.insert(v.clone(), None::<String>);
        }

        // 開始頂点の処理
        min_cost.insert(start.clone(), 0.0);
        min_heap.push(Reverse((0, start.clone(), None::<String>))); // (コスト, 現在の頂点, 遷移元の頂点)

        while let Some(Reverse((cost, current_vertex, from_vertex))) = min_heap.pop() {
            // 既にMSTに含まれている頂点であればスキップ
            if in_mst.contains(&current_vertex) {
                continue;
            }

            // 現在の頂点をMSTに追加
            in_mst.insert(current_vertex.clone());

            // MSTに追加された辺を記録 (開始頂点以外)
            if let Some(from) = from_vertex {
                // from から current_vertex への辺の重みを取得
                if let Some(weight) = self.get_edge_weight(&from, &current_vertex) {
                    let mut edge = vec![from.clone(), current_vertex.clone()];
                    edge.sort();
                    mst_edges.push((edge[0].clone(), edge[1].clone(), weight));
                }
            }

            // 現在の頂点に隣接する頂点を調べ、MSTへの追加コストを更新
            if let Some(neighbors) = self.get_neighbors(&current_vertex) {
                for (neighbor, weight) in neighbors {
                    // 隣接頂点がまだMSTに含まれておらず、現在のコストよりも小さい場合
                    if !in_mst.contains(neighbor) && *weight as f64 < *min_cost.get(neighbor).unwrap() {
                        min_cost.insert(neighbor.clone(), *weight as f64);
                        parent.insert(neighbor.clone(), Some(current_vertex.clone()));
                        min_heap.push(Reverse((*weight, neighbor.clone(), Some(current_vertex.clone()))));
                    }
                }
            }
        }

        mst_edges
    }
}

fn main() {
    println!("Prims TEST -----> start");
    let mut graph_data = GraphData::new();

    graph_data.clear();
    let input_list = vec![
        ("A", "B", 4), ("B", "C", 3), ("B", "D", 2), 
        ("D", "A", 1), ("A", "C", 2), ("B", "D", 2)
    ];
    for (v1, v2, weight) in input_list {
        graph_data.add_edge(v1, v2, weight);
    }
    println!("\nグラフの頂点: {:?}", graph_data.get_vertices());
    println!("グラフの辺 (重み付き): {:?}", graph_data.get_edges());
    let output_mst = graph_data.get_mst(None);
    for (v1, v2, weight) in &output_mst {
        println!("Edge: {} - {}, Weight: {}", v1, v2, weight);
    }
    let total_weight: i32 = output_mst.iter().map(|(_, _, w)| w).sum();
    println!("最小全域木の合計重み: {}", total_weight);

    graph_data.clear();
    let input_list = vec![
        ("A", "B", 4), ("C", "D", 4), ("E", "F", 1), ("F", "G", 1)
    ];
    for (v1, v2, weight) in input_list {
        graph_data.add_edge(v1, v2, weight);
    }
    println!("\nグラフの頂点: {:?}", graph_data.get_vertices());
    println!("グラフの辺 (重み付き): {:?}", graph_data.get_edges());
    let output_mst = graph_data.get_mst(None);
    for (v1, v2, weight) in &output_mst {
        println!("Edge: {} - {}, Weight: {}", v1, v2, weight);
    }
    let total_weight: i32 = output_mst.iter().map(|(_, _, w)| w).sum();
    println!("最小全域木の合計重み: {}", total_weight);

    graph_data.clear();
    let input_list = vec![
        ("A", "B", 4), ("B", "C", 3), ("D", "E", 5)
    ];
    for (v1, v2, weight) in input_list {
        graph_data.add_edge(v1, v2, weight);
    }
    println!("\nグラフの頂点: {:?}", graph_data.get_vertices());
    println!("グラフの辺 (重み付き): {:?}", graph_data.get_edges());
    let output_mst = graph_data.get_mst(None);
    for (v1, v2, weight) in &output_mst {
        println!("Edge: {} - {}, Weight: {}", v1, v2, weight);
    }
    let total_weight: i32 = output_mst.iter().map(|(_, _, w)| w).sum();
    println!("最小全域木の合計重み: {}", total_weight);

    graph_data.clear();
    let input_list: Vec<(&str, &str, i32)> = vec![];
    for (v1, v2, weight) in input_list {
        graph_data.add_edge(v1, v2, weight);
    }
    println!("\nグラフの頂点: {:?}", graph_data.get_vertices());
    println!("グラフの辺 (重み付き): {:?}", graph_data.get_edges());
    let output_mst = graph_data.get_mst(None);
    for (v1, v2, weight) in &output_mst {
        println!("Edge: {} - {}, Weight: {}", v1, v2, weight);
    }
    let total_weight: i32 = output_mst.iter().map(|(_, _, w)| w).sum();
    println!("最小全域木の合計重み: {}", total_weight);

    println!("\nPrims TEST <----- end");
}