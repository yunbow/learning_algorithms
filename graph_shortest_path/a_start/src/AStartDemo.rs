// Rust
// グラフの最短経路: A-star

use std::collections::{BinaryHeap, HashMap};
use std::cmp::Ordering;

// 優先度キューのための比較可能な構造体
#[derive(Copy, Clone, Eq, PartialEq)]
struct Node {
    f_cost: u32,
    vertex: usize,
}

// 優先度キューでの比較のため、Ord と PartialOrd を実装
impl Ord for Node {
    fn cmp(&self, other: &Self) -> Ordering {
        // f_costが小さい方が優先度が高い（BinaryHeapはmaxヒープなので逆順に）
        other.f_cost.cmp(&self.f_cost)
    }
}

impl PartialOrd for Node {
    fn partial_cmp(&self, other: &Self) -> Option<Ordering> {
        Some(self.cmp(other))
    }
}

struct GraphData {
    // 隣接リスト表現: キーは頂点のインデックス、値は(隣接頂点, 重み)のベクタ
    data: HashMap<usize, Vec<(usize, u32)>>,
    // 頂点名とインデックスのマッピング
    vertex_indices: HashMap<String, usize>,
    // インデックスと頂点名のマッピング
    index_vertices: HashMap<usize, String>,
    next_index: usize,
}

impl GraphData {
    fn new() -> Self {
        GraphData {
            data: HashMap::new(),
            vertex_indices: HashMap::new(),
            index_vertices: HashMap::new(),
            next_index: 0,
        }
    }

    // 頂点名からインデックスを取得（存在しない場合は作成）
    fn get_vertex_index(&mut self, vertex: &str) -> usize {
        if let Some(&index) = self.vertex_indices.get(vertex) {
            return index;
        }
        
        let index = self.next_index;
        self.vertex_indices.insert(vertex.to_string(), index);
        self.index_vertices.insert(index, vertex.to_string());
        self.next_index += 1;
        index
    }

    // インデックスから頂点名を取得
    fn get_vertex_name(&self, index: usize) -> Option<&String> {
        self.index_vertices.get(&index)
    }

    fn get(&self) -> &HashMap<usize, Vec<(usize, u32)>> {
        &self.data
    }

    fn get_vertices(&self) -> Vec<String> {
        self.vertex_indices.keys().cloned().collect()
    }

    fn get_edges(&self) -> Vec<(String, String, u32)> {
        let mut edges = Vec::new();
        for (&vertex, neighbors) in &self.data {
            let vertex_name = self.get_vertex_name(vertex).unwrap();
            for &(neighbor, weight) in neighbors {
                let neighbor_name = self.get_vertex_name(neighbor).unwrap();
                if vertex < neighbor {  // 無向グラフなので重複を避ける
                    edges.push((vertex_name.clone(), neighbor_name.clone(), weight));
                }
            }
        }
        edges
    }

    fn get_neighbors(&self, vertex_index: usize) -> Option<&Vec<(usize, u32)>> {
        self.data.get(&vertex_index)
    }

    fn add_vertex(&mut self, vertex: &str) -> bool {
        let index = self.get_vertex_index(vertex);
        self.data.entry(index).or_insert_with(Vec::new);
        true
    }

    fn add_edge(&mut self, vertex1: &str, vertex2: &str, weight: u32) -> bool {
        let v1_index = self.get_vertex_index(vertex1);
        let v2_index = self.get_vertex_index(vertex2);
        
        // データ構造に頂点が存在することを確認
        self.data.entry(v1_index).or_insert_with(Vec::new);
        self.data.entry(v2_index).or_insert_with(Vec::new);
        
        // v1 -> v2 の辺を追加/更新
        let mut edge_exists_v1v2 = false;
        if let Some(neighbors) = self.data.get_mut(&v1_index) {
            for i in 0..neighbors.len() {
                if neighbors[i].0 == v2_index {
                    neighbors[i].1 = weight;
                    edge_exists_v1v2 = true;
                    break;
                }
            }
            if !edge_exists_v1v2 {
                neighbors.push((v2_index, weight));
            }
        }
        
        // v2 -> v1 の辺を追加/更新
        let mut edge_exists_v2v1 = false;
        if let Some(neighbors) = self.data.get_mut(&v2_index) {
            for i in 0..neighbors.len() {
                if neighbors[i].0 == v1_index {
                    neighbors[i].1 = weight;
                    edge_exists_v2v1 = true;
                    break;
                }
            }
            if !edge_exists_v2v1 {
                neighbors.push((v1_index, weight));
            }
        }
        
        true
    }

    fn clear(&mut self) -> bool {
        self.data.clear();
        self.vertex_indices.clear();
        self.index_vertices.clear();
        self.next_index = 0;
        true
    }

    fn get_shortest_path(&self, start_vertex: &str, end_vertex: &str, 
                       heuristic: fn(&str, &str) -> u32) -> (Option<Vec<String>>, u32) {
        // 頂点名からインデックスを取得
        let start_index = match self.vertex_indices.get(start_vertex) {
            Some(&index) => index,
            None => {
                println!("ERROR: 開始頂点がグラフに存在しません。");
                return (None, u32::MAX);
            }
        };
        
        let end_index = match self.vertex_indices.get(end_vertex) {
            Some(&index) => index,
            None => {
                println!("ERROR: 終了頂点がグラフに存在しません。");
                return (None, u32::MAX);
            }
        };
        
        if start_index == end_index {
            return (Some(vec![start_vertex.to_string()]), 0);
        }
        
        // g_costs: 開始ノードから各ノードまでの既知の最短コスト
        let mut g_costs = HashMap::new();
        for &vertex in self.data.keys() {
            g_costs.insert(vertex, u32::MAX);
        }
        g_costs.insert(start_index, 0);
        
        // f_costs: g_costs + ヒューリスティックコスト
        let mut f_costs = HashMap::new();
        for &vertex in self.data.keys() {
            f_costs.insert(vertex, u32::MAX);
        }
        
        let start_name = self.get_vertex_name(start_index).unwrap();
        let end_name = self.get_vertex_name(end_index).unwrap();
        f_costs.insert(start_index, heuristic(start_name, end_name));
        
        // came_from: 最短経路で各ノードの直前のノードを記録
        let mut came_from = HashMap::new();
        
        // open_set: 探索すべきノードの優先度キュー
        let mut open_set = BinaryHeap::new();
        open_set.push(Node { f_cost: f_costs[&start_index], vertex: start_index });
        
        while let Some(Node { f_cost, vertex }) = open_set.pop() {
            // 取り出したノードの f_cost が記録されている f_costs よりも大きい場合、無視
            if f_cost > f_costs[&vertex] {
                continue;
            }
            
            // 目標ノードに到達した場合、経路を再構築して返す
            if vertex == end_index {
                let path = self.reconstruct_path(&came_from, end_index);
                return (Some(path), g_costs[&end_index]);
            }
            
            // 現在のノードの隣接ノードを調べる
            if let Some(neighbors) = self.get_neighbors(vertex) {
                for &(neighbor, weight) in neighbors {
                    // 現在のノードを経由した場合の新しい g_cost
                    let tentative_g_cost = match g_costs[&vertex].checked_add(weight) {
                        Some(cost) => cost,
                        None => continue, // オーバーフロー防止
                    };
                    
                    // 新しい経路が既知の経路より短い場合、情報を更新
                    if tentative_g_cost < g_costs[&neighbor] {
                        came_from.insert(neighbor, vertex);
                        g_costs.insert(neighbor, tentative_g_cost);
                        
                        let neighbor_name = self.get_vertex_name(neighbor).unwrap();
                        let h_cost = heuristic(neighbor_name, end_name);
                        let new_f_cost = match tentative_g_cost.checked_add(h_cost) {
                            Some(cost) => cost,
                            None => u32::MAX, // オーバーフロー防止
                        };
                        
                        f_costs.insert(neighbor, new_f_cost);
                        open_set.push(Node { f_cost: new_f_cost, vertex: neighbor });
                    }
                }
            }
        }
        
        // 経路が見つからなかった場合
        (None, u32::MAX)
    }

    fn reconstruct_path(&self, came_from: &HashMap<usize, usize>, current_vertex: usize) -> Vec<String> {
        let mut path = Vec::new();
        let mut current = current_vertex;
        
        loop {
            let vertex_name = self.get_vertex_name(current).unwrap().clone();
            path.push(vertex_name);
            
            if let Some(&prev) = came_from.get(&current) {
                current = prev;
            } else {
                break;
            }
        }
        
        path.reverse(); // 経路を逆順にする (開始 -> 目標)
        path
    }
}

// ダミーのヒューリスティック関数（常に0を返す、ダイクストラ法と同じ）
fn dummy_heuristic(_u: &str, _v: &str) -> u32 {
    0
}

fn main() {
    println!("A-start TEST -----> start");
    
    let mut graph_data = GraphData::new();
    
    graph_data.clear();
    let input_list = vec![("A", "B", 4), ("B", "C", 3), ("B", "D", 2), ("D", "A", 1), ("A", "C", 2), ("B", "D", 2)];
    for &(v1, v2, weight) in &input_list {
        graph_data.add_edge(v1, v2, weight);
    }
    println!("\nグラフの頂点: {:?}", graph_data.get_vertices());
    println!("グラフの辺 (重み付き): {:?}", graph_data.get_edges());
    let (start_vertex, end_vertex) = ("A", "B");
    let (shortest_path, weight) = graph_data.get_shortest_path(start_vertex, end_vertex, dummy_heuristic);
    println!("経路{}-{} の最短経路は {:?} (重み: {})", 
             start_vertex, end_vertex, shortest_path, weight);
    
    graph_data.clear();
    let input_list = vec![("A", "B", 4), ("C", "D", 4), ("E", "F", 1), ("F", "G", 1)];
    for &(v1, v2, weight) in &input_list {
        graph_data.add_edge(v1, v2, weight);
    }
    println!("\nグラフの頂点: {:?}", graph_data.get_vertices());
    println!("グラフの辺 (重み付き): {:?}", graph_data.get_edges());
    let (start_vertex, end_vertex) = ("A", "B");
    let (shortest_path, weight) = graph_data.get_shortest_path(start_vertex, end_vertex, dummy_heuristic);
    println!("経路{}-{} の最短経路は {:?} (重み: {})", 
             start_vertex, end_vertex, shortest_path, weight);
    
    graph_data.clear();
    let input_list = vec![("A", "B", 4), ("B", "C", 3), ("D", "E", 5)];
    for &(v1, v2, weight) in &input_list {
        graph_data.add_edge(v1, v2, weight);
    }
    println!("\nグラフの頂点: {:?}", graph_data.get_vertices());
    println!("グラフの辺 (重み付き): {:?}", graph_data.get_edges());
    let (start_vertex, end_vertex) = ("A", "D");
    let (shortest_path, weight) = graph_data.get_shortest_path(start_vertex, end_vertex, dummy_heuristic);
    println!("経路{}-{} の最短経路は {:?} (重み: {})", 
             start_vertex, end_vertex, shortest_path, weight);
    
    graph_data.clear();
    let input_list: Vec<(&str, &str, u32)> = vec![];
    for &(v1, v2, weight) in &input_list {
        graph_data.add_edge(v1, v2, weight);
    }
    println!("\nグラフの頂点: {:?}", graph_data.get_vertices());
    println!("グラフの辺 (重み付き): {:?}", graph_data.get_edges());
    let (start_vertex, end_vertex) = ("A", "B");
    let (shortest_path, weight) = graph_data.get_shortest_path(start_vertex, end_vertex, dummy_heuristic);
    println!("経路{}-{} の最短経路は {:?} (重み: {})", 
             start_vertex, end_vertex, shortest_path, weight);
    
    println!("\nA-start TEST <----- end");
}