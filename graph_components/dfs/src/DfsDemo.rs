// Rust
// グラフの連結成分: DFS

use std::collections::{HashMap, HashSet};

struct GraphData {
    // 隣接ノードとその辺の重みを格納
    // キーは頂点、値はその頂点に隣接する頂点と重みのベクター
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
        // グラフの全頂点をベクトルとして返す
        self.data.keys().cloned().collect()
    }

    fn add_vertex(&mut self, vertex: &str) -> bool {
        // 新しい頂点をグラフに追加
        self.data.entry(vertex.to_string()).or_insert_with(Vec::new);
        true
    }
    
    fn add_edge(&mut self, vertex1: &str, vertex2: &str, weight: i32) -> bool {
        // 両頂点間に辺を追加。重みを指定
        // 頂点がグラフに存在しない場合は追加
        
        // 頂点を確実に追加
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
        
    fn clear(&mut self) -> bool {
        // グラフを空にする
        self.data.clear();
        true
    }
    
    fn _dfs(&self, vertex: &str, visited: &mut HashSet<String>, current_component: &mut Vec<String>) {
        // 現在の頂点を訪問済みにマークし、現在の成分に追加
        visited.insert(vertex.to_string());
        current_component.push(vertex.to_string());
        
        // 隣接する頂点を探索
        if let Some(neighbors) = self.data.get(vertex) {
            for (neighbor_vertex, _) in neighbors {
                // まだ訪問していない隣接頂点に対して再帰的にDFSを呼び出す
                if !visited.contains(neighbor_vertex) {
                    self._dfs(neighbor_vertex, visited, current_component);
                }
            }
        }
    }
    
    fn get_connected_components(&self) -> Vec<Vec<String>> {
        let mut visited = HashSet::new(); // 訪問済み頂点を記録するセット
        let mut connected_components = Vec::new(); // 連結成分を格納するベクトル
        
        // グラフの全頂点を順にチェック
        for vertex in self.get_vertices() {
            // まだ訪問していない頂点からDFSを開始
            if !visited.contains(&vertex) {
                let mut current_component = Vec::new(); // 現在の連結成分を格納するベクトル
                // DFSヘルパー関数を呼び出し、現在の連結成分を探索
                self._dfs(&vertex, &mut visited, &mut current_component);
                // 探索で見つかった連結成分を結果ベクトルに追加
                connected_components.push(current_component);
            }
        }
        
        connected_components
    }
}

fn main() {
    println!("Dfs TEST -----> start");
    
    println!("\nnew");
    let mut graph_data = GraphData::new();
    println!("  現在のデータ: {:?}", graph_data.get());
    
    println!("\nadd_edge");
    graph_data.clear();
    let input_list = vec![
        ("A", "B", 4), ("B", "C", 3), ("B", "D", 2), 
        ("D", "A", 1), ("A", "C", 2), ("B", "D", 2)
    ];
    for input in input_list {
        println!("  入力値: {:?}", input);
        let output = graph_data.add_edge(input.0, input.1, input.2);
        println!("  出力値: {}", output);
    }
    println!("  現在のデータ: {:?}", graph_data.get());
    println!("\nget_connected_components");
    let output = graph_data.get_connected_components();
    println!("  連結成分: {:?}", output);
    
    println!("\nadd_edge");
    graph_data.clear();
    let input_list = vec![
        ("A", "B", 4), ("C", "D", 4), ("E", "F", 1), ("F", "G", 1)
    ];
    for input in input_list {
        println!("  入力値: {:?}", input);
        let output = graph_data.add_edge(input.0, input.1, input.2);
        println!("  出力値: {}", output);
    }
    println!("  現在のデータ: {:?}", graph_data.get());
    println!("\nget_connected_components");
    let output = graph_data.get_connected_components();
    println!("  連結成分: {:?}", output);
    
    println!("\nadd_edge");
    graph_data.clear();        
    let input_list = vec![("A", "B", 4), ("B", "C", 3), ("D", "E", 5)];
    for input in input_list {
        println!("  入力値: {:?}", input);
        let output = graph_data.add_edge(input.0, input.1, input.2);
        println!("  出力値: {}", output);
    }
    println!("  現在のデータ: {:?}", graph_data.get());
    println!("\nget_connected_components");
    let output = graph_data.get_connected_components();
    println!("  連結成分: {:?}", output);
    
    println!("\nadd_edge");
    graph_data.clear();
    let input_list: Vec<(&str, &str, i32)> = vec![];
    for input in input_list {
        println!("  入力値: {:?}", input);
        let output = graph_data.add_edge(input.0, input.1, input.2);
        println!("  出力値: {}", output);
    }
    println!("  現在のデータ: {:?}", graph_data.get());
    println!("\nget_connected_components");
    let output = graph_data.get_connected_components();
    println!("  連結成分: {:?}", output);
    
    println!("Dfs TEST <----- end");
}