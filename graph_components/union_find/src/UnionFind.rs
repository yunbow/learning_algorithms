// Rust
// グラフの連結成分: Union-Find

use std::collections::{HashMap, HashSet};

struct GraphData<T> 
where 
    T: std::hash::Hash + Eq + Clone + Ord + std::fmt::Debug
{
    // 隣接ノードとその辺の重みを格納します。
    // キーは頂点、値はその頂点に隣接する頂点と重みのベクターです。
    data: HashMap<T, Vec<(T, i32)>>,
}

impl<T> GraphData<T> 
where 
    T: std::hash::Hash + Eq + Clone + Ord + std::fmt::Debug
{
    fn new() -> Self {
        GraphData {
            data: HashMap::new(),
        }
    }

    fn get(&self) -> &HashMap<T, Vec<(T, i32)>> {
        // グラフの内部データを取得します。
        &self.data
    }

    fn get_vertices(&self) -> Vec<T> {
        // グラフの全頂点をベクターとして返します。
        self.data.keys().cloned().collect()
    }

    fn get_edges(&self) -> Vec<(T, T, i32)> {
        // グラフの全辺をベクターとして返します。
        // 無向グラフの場合、(u, v, weight) の形式で返します。
        // 重複を避けるためにセットを使用します。
        let mut edges = HashSet::new();
        
        for (vertex, neighbors) in &self.data {
            for (neighbor, weight) in neighbors {
                // 辺を正規化してセットに追加 (小さい方の頂点を最初にするなど)
                let mut vertices = vec![vertex.clone(), neighbor.clone()];
                vertices.sort();
                edges.insert((vertices[0].clone(), vertices[1].clone(), *weight));
            }
        }
        
        edges.into_iter().collect()
    }

    fn add_vertex(&mut self, vertex: T) -> bool {
        // 新しい頂点をグラフに追加します。
        self.data.entry(vertex).or_insert_with(Vec::new);
        true
    }

    fn add_edge(&mut self, vertex1: T, vertex2: T, weight: i32) -> bool {
        // 両頂点間に辺を追加します。重みを指定します。
        // 頂点がグラフに存在しない場合は追加します。
        self.add_vertex(vertex1.clone());
        self.add_vertex(vertex2.clone());
        
        // vertex1 -> vertex2 の辺を追加（重み付き）
        let mut edge_exists_v1v2 = false;
        if let Some(neighbors) = self.data.get_mut(&vertex1) {
            for i in 0..neighbors.len() {
                if &neighbors[i].0 == &vertex2 {
                    neighbors[i] = (vertex2.clone(), weight);
                    edge_exists_v1v2 = true;
                    break;
                }
            }
            if !edge_exists_v1v2 {
                neighbors.push((vertex2.clone(), weight));
            }
        }

        // vertex2 -> vertex1 の辺を追加（重み付き）
        let mut edge_exists_v2v1 = false;
        if let Some(neighbors) = self.data.get_mut(&vertex2) {
            for i in 0..neighbors.len() {
                if &neighbors[i].0 == &vertex1 {
                    neighbors[i] = (vertex1.clone(), weight);
                    edge_exists_v2v1 = true;
                    break;
                }
            }
            if !edge_exists_v2v1 {
                neighbors.push((vertex1, weight));
            }
        }
        
        true
    }

    fn is_empty(&self) -> bool {
        // グラフが空かどうか
        self.data.is_empty()
    }
    
    fn clear(&mut self) -> bool {
        // グラフを空にする
        self.data.clear();
        true
    }

    fn get_connected_components(&self) -> Vec<Vec<T>> {
        if self.data.is_empty() {
            return vec![]; // 空のグラフの場合は空ベクターを返す
        }

        // Union-Findのためのデータ構造を初期化
        let mut parent: HashMap<T, T> = HashMap::new();
        let mut size: HashMap<T, usize> = HashMap::new();

        // 各頂点を初期状態では自分自身の集合に属させ、サイズを1とする
        let vertices = self.get_vertices();
        for vertex in &vertices {
            parent.insert(vertex.clone(), vertex.clone());
            size.insert(vertex.clone(), 1);
        }

        // findメソッドの定義 (Union-Find の find操作)
        fn find<T: std::hash::Hash + Eq + Clone>(parent: &mut HashMap<T, T>, v: &T) -> T {
            let parent_v = parent.get(v).unwrap().clone();
            // 経路圧縮 (Path Compression) を伴う Find 操作
            // vの親がv自身でなければ、根を探しにいく
            if &parent_v != v {
                // 見つけた根をvの直接の親として記録 (経路圧縮)
                let root = find(parent, &parent_v);
                parent.insert(v.clone(), root.clone());
                return root;
            }
            parent_v // 最終的に根を返す
        }

        // unionメソッドの定義 (Union-Find の union操作)
        fn union<T: std::hash::Hash + Eq + Clone>(parent: &mut HashMap<T, T>, size: &mut HashMap<T, usize>, u: &T, v: &T) -> bool {
            let root_u = find(parent, u);
            let root_v = find(parent, v);

            // 根が同じ場合は、すでに同じ集合に属しているので何もしない
            if root_u != root_v {
                // より小さいサイズの木を大きいサイズの木に結合する
                let size_u = *size.get(&root_u).unwrap();
                let size_v = *size.get(&root_v).unwrap();
                
                if size_u < size_v {
                    parent.insert(root_u.clone(), root_v.clone());
                    size.insert(root_v, size_u + size_v);
                } else {
                    parent.insert(root_v.clone(), root_u.clone());
                    size.insert(root_u, size_u + size_v);
                }
                return true; // 結合が行われた
            }
            false // 結合は行われなかった
        }

        // グラフの全ての辺に対してUnion操作を行い、連結成分をマージする
        for (u, v, _) in self.get_edges() {
            union(&mut parent, &mut size, &u, &v);
        }

        // 連結成分をグループ化する
        // 根をキーとして、その根に属する頂点のベクターを値とする辞書を作成
        let mut components: HashMap<T, Vec<T>> = HashMap::new();
        for vertex in &vertices {
            let root = find(&mut parent, vertex);
            components.entry(root).or_insert_with(Vec::new).push(vertex.clone());
        }

        // 連結成分のベクター（値の部分）を返す
        components.into_values().collect()
    }
}

fn main() {
    println!("UnionFind TEST -----> start");

    println!("\nnew");
    let mut graph_data = GraphData::<String>::new();
    println!("  現在のデータ: {:?}", graph_data.get());

    println!("\nadd_edge");
    graph_data.clear();
    let input_list = [
        ("A".to_string(), "B".to_string(), 4),
        ("B".to_string(), "C".to_string(), 3),
        ("B".to_string(), "D".to_string(), 2),
        ("D".to_string(), "A".to_string(), 1),
        ("A".to_string(), "C".to_string(), 2),
        ("B".to_string(), "D".to_string(), 2)
    ];
    for input in &input_list {
        println!("  入力値: {:?}", input);
        let output = graph_data.add_edge(input.0.clone(), input.1.clone(), input.2);
        println!("  出力値: {}", output);
    }
    println!("  現在のデータ: {:?}", graph_data.get());
    println!("\nget_connected_components");
    let output = graph_data.get_connected_components();
    println!("  連結成分: {:?}", output);

    println!("\nadd_edge");
    graph_data.clear();
    let input_list = [
        ("A".to_string(), "B".to_string(), 4),
        ("C".to_string(), "D".to_string(), 4),
        ("E".to_string(), "F".to_string(), 1),
        ("F".to_string(), "G".to_string(), 1)
    ];
    for input in &input_list {
        println!("  入力値: {:?}", input);
        let output = graph_data.add_edge(input.0.clone(), input.1.clone(), input.2);
        println!("  出力値: {}", output);
    }
    println!("  現在のデータ: {:?}", graph_data.get());
    println!("\nget_connected_components");
    let output = graph_data.get_connected_components();
    println!("  連結成分: {:?}", output);

    println!("\nadd_edge");
    graph_data.clear();        
    let input_list = [
        ("A".to_string(), "B".to_string(), 4),
        ("B".to_string(), "C".to_string(), 3),
        ("D".to_string(), "E".to_string(), 5)
    ];
    for input in &input_list {
        println!("  入力値: {:?}", input);
        let output = graph_data.add_edge(input.0.clone(), input.1.clone(), input.2);
        println!("  出力値: {}", output);
    }
    println!("  現在のデータ: {:?}", graph_data.get());
    println!("\nget_connected_components");
    let output = graph_data.get_connected_components();
    println!("  連結成分: {:?}", output);

    println!("\nadd_edge");
    graph_data.clear();
    let input_list: [(String, String, i32); 0] = [];
    for input in &input_list {
        println!("  入力値: {:?}", input);
        let output = graph_data.add_edge(input.0.clone(), input.1.clone(), input.2);
        println!("  出力値: {}", output);
    }
    println!("  現在のデータ: {:?}", graph_data.get());
    println!("\nget_connected_components");
    let output = graph_data.get_connected_components();
    println!("  連結成分: {:?}", output);

    println!("\nUnionFind TEST <----- end");
}