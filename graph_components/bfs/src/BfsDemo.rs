// Rust
// グラフの連結成分: BFS

use std::collections::{HashMap, HashSet, VecDeque};

/// グラフデータを表す構造体
/// 隣接ノードとその辺の重みを格納します
struct GraphData {
    // キーは頂点、値はその頂点に隣接する頂点と重みのベクタです
    data: HashMap<String, Vec<(String, i32)>>,
}

impl GraphData {
    /// 新しいグラフデータを作成します
    fn new() -> Self {
        GraphData {
            data: HashMap::new(),
        }
    }

    /// グラフの内部データを取得します
    fn get(&self) -> &HashMap<String, Vec<(String, i32)>> {
        &self.data
    }

    /// グラフの全頂点をベクタとして返します
    fn get_vertices(&self) -> Vec<String> {
        self.data.keys().cloned().collect()
    }

    /// グラフの全辺をベクタとして返します
    fn get_edges(&self) -> Vec<(String, String, i32)> {
        let mut edges = HashSet::new();
        for (vertex, neighbors) in &self.data {
            for (neighbor, weight) in neighbors {
                // 辺を正規化（小さい方の頂点を最初に）
                let mut vertices = vec![vertex.clone(), neighbor.clone()];
                vertices.sort();
                edges.insert((vertices[0].clone(), vertices[1].clone(), *weight));
            }
        }
        edges.into_iter().collect()
    }

    /// 指定された頂点の隣接ノードと辺の重みのベクタを返します
    fn get_neighbors(&self, vertex: &str) -> Option<&Vec<(String, i32)>> {
        self.data.get(vertex)
    }

    /// 指定された2つの頂点間の辺の重みを返します
    fn get_edge_weight(&self, vertex1: &str, vertex2: &str) -> Option<i32> {
        if let Some(neighbors) = self.data.get(vertex1) {
            for (neighbor, weight) in neighbors {
                if neighbor == vertex2 {
                    return Some(*weight);
                }
            }
        }
        None
    }

    /// 頂点がグラフに存在するか確認し、存在する場合はその隣接リストを返します
    fn get_vertice(&self, vertex: &str) -> Option<&Vec<(String, i32)>> {
        match self.data.get(vertex) {
            Some(neighbors) => Some(neighbors),
            None => {
                println!("ERROR: {}は範囲外です", vertex);
                None
            }
        }
    }

    /// 指定された2つの頂点間に辺が存在するかを確認します
    fn get_edge(&self, vertex1: &str, vertex2: &str) -> bool {
        if let Some(neighbors) = self.data.get(vertex1) {
            for (neighbor, _) in neighbors {
                if neighbor == vertex2 {
                    return true;
                }
            }
        }
        false
    }

    /// 新しい頂点をグラフに追加します
    fn add_vertex(&mut self, vertex: &str) -> bool {
        if !self.data.contains_key(vertex) {
            self.data.insert(vertex.to_string(), Vec::new());
        }
        true
    }

    /// 両頂点間に辺を追加します。重みを指定します
    fn add_edge(&mut self, vertex1: &str, vertex2: &str, weight: i32) -> bool {
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

    /// 頂点とそれに関連する辺を削除します
    fn remove_vertex(&mut self, vertex: &str) -> bool {
        if self.data.contains_key(vertex) {
            // この頂点への参照を他の頂点の隣接リストから削除
            let vertices = self.get_vertices();
            for v in vertices {
                if v != vertex {
                    if let Some(neighbors) = self.data.get_mut(&v) {
                        neighbors.retain(|(neighbor, _)| neighbor != vertex);
                    }
                }
            }
            // 頂点自体を削除
            self.data.remove(vertex);
            true
        } else {
            println!("ERROR: {} は範囲外です", vertex);
            false
        }
    }

    /// 両頂点間の辺を削除します
    fn remove_edge(&mut self, vertex1: &str, vertex2: &str) -> bool {
        let mut removed = false;

        if self.data.contains_key(vertex1) && self.data.contains_key(vertex2) {
            // vertex1 から vertex2 への辺を削除
            if let Some(neighbors) = self.data.get_mut(vertex1) {
                let original_len = neighbors.len();
                neighbors.retain(|(neighbor, _)| neighbor != vertex2);
                if neighbors.len() < original_len {
                    removed = true;
                }
            }

            // vertex2 から vertex1 への辺を削除
            if let Some(neighbors) = self.data.get_mut(vertex2) {
                let original_len = neighbors.len();
                neighbors.retain(|(neighbor, _)| neighbor != vertex1);
                if neighbors.len() < original_len {
                    removed = true;
                }
            }

            removed
        } else {
            println!("ERROR: {} または {} は範囲外です", vertex1, vertex2);
            false
        }
    }

    /// グラフが空かどうか確認します
    fn is_empty(&self) -> bool {
        self.data.is_empty()
    }

    /// グラフの頂点数を返します
    fn size(&self) -> usize {
        self.data.len()
    }

    /// グラフを空にします
    fn clear(&mut self) -> bool {
        self.data.clear();
        true
    }

    /// グラフの連結成分をBFSを使用して見つけます
    fn get_connected_components(&self) -> Vec<Vec<String>> {
        let mut visited = HashSet::new();  // 全体の訪問済み頂点を記録するセット
        let mut all_components = Vec::new(); // 見つかった連結成分のリスト

        // グラフのすべての頂点を取得
        let vertices = self.get_vertices();

        // すべての頂点を順番にチェック
        for vertex in &vertices {
            // もしその頂点がまだ訪問されていなければ、新しい連結成分の開始点
            if !visited.contains(vertex) {
                let mut current_component = Vec::new(); // 現在探索中の連結成分
                let mut queue = VecDeque::new(); // BFS用のキュー
                queue.push_back(vertex.clone());
                visited.insert(vertex.clone()); // 開始点を訪問済みにマーク
                current_component.push(vertex.clone()); // 開始点を現在の成分に追加

                // BFSを開始
                while let Some(u) = queue.pop_front() {
                    // 取り出した頂点の隣接リストを取得
                    if let Some(neighbors_with_weight) = self.get_neighbors(&u) {
                        // 隣接ノードだけを取り出してループ
                        for (neighbor, _) in neighbors_with_weight {
                            // 隣接する頂点がまだ訪問されていなければ
                            if !visited.contains(neighbor) {
                                visited.insert(neighbor.clone()); // 頂点を訪問済みにマーク
                                queue.push_back(neighbor.clone()); // 頂点をキューに追加
                                current_component.push(neighbor.clone()); // 頂点を現在の成分に追加
                            }
                        }
                    }
                }

                // BFSが終了したら、1つの連結成分が見つかった
                all_components.push(current_component);
            }
        }

        all_components
    }
}

fn main() {
    println!("Bfs TEST -----> start");

    println!("\nnew");
    let mut graph_data = GraphData::new();
    println!("  現在のデータ: {:?}", graph_data.get());

    println!("\nadd_edge");
    graph_data.clear();
    let input_list = vec![
        ("A", "B", 4), ("B", "C", 3), ("B", "D", 2), ("D", "A", 1), ("A", "C", 2), ("B", "D", 2)
    ];
    for &(v1, v2, weight) in &input_list {
        println!("  入力値: ({}, {}, {})", v1, v2, weight);
        let output = graph_data.add_edge(v1, v2, weight);
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
    for &(v1, v2, weight) in &input_list {
        println!("  入力値: ({}, {}, {})", v1, v2, weight);
        let output = graph_data.add_edge(v1, v2, weight);
        println!("  出力値: {}", output);
    }
    println!("  現在のデータ: {:?}", graph_data.get());
    println!("\nget_connected_components");
    let output = graph_data.get_connected_components();
    println!("  連結成分: {:?}", output);

    println!("\nadd_edge");
    graph_data.clear();
    let input_list = vec![
        ("A", "B", 4), ("B", "C", 3), ("D", "E", 5)
    ];
    for &(v1, v2, weight) in &input_list {
        println!("  入力値: ({}, {}, {})", v1, v2, weight);
        let output = graph_data.add_edge(v1, v2, weight);
        println!("  出力値: {}", output);
    }
    println!("  現在のデータ: {:?}", graph_data.get());
    println!("\nget_connected_components");
    let output = graph_data.get_connected_components();
    println!("  連結成分: {:?}", output);

    println!("\nadd_edge");
    graph_data.clear();
    let input_list: Vec<(&str, &str, i32)> = vec![];
    for &(v1, v2, weight) in &input_list {
        println!("  入力値: ({}, {}, {})", v1, v2, weight);
        let output = graph_data.add_edge(v1, v2, weight);
        println!("  出力値: {}", output);
    }
    println!("  現在のデータ: {:?}", graph_data.get());
    println!("\nget_connected_components");
    let output = graph_data.get_connected_components();
    println!("  連結成分: {:?}", output);

    println!("Bfs TEST <----- end");
}