// Rust
// グラフの最小全域木: クラスカル法 (Kruskal)

use std::collections::{HashMap, HashSet};
use std::hash::Hash;
use std::fmt::Debug;

// DSU (Disjoint Set Union)実装
struct DSU<T: Hash + Eq + Copy + Debug> {
    parent: HashMap<T, T>,
    rank: HashMap<T, usize>,
}

impl<T: Hash + Eq + Copy + Debug> DSU<T> {
    fn new(vertices: &[T]) -> Self {
        let mut parent = HashMap::new();
        let mut rank = HashMap::new();
        
        for &v in vertices {
            parent.insert(v, v);
            rank.insert(v, 0);
        }
        
        DSU { parent, rank }
    }
    
    fn find(&mut self, i: T) -> T {
        if self.parent[&i] == i {
            return i;
        }
        
        // パス圧縮
        let parent_i = self.parent[&i];
        let root = self.find(parent_i);
        self.parent.insert(i, root);
        root
    }
    
    fn union(&mut self, i: T, j: T) -> bool {
        let root_i = self.find(i);
        let root_j = self.find(j);
        
        if root_i != root_j {
            // ランクによるunion
            if self.rank[&root_i] < self.rank[&root_j] {
                self.parent.insert(root_i, root_j);
            } else if self.rank[&root_i] > self.rank[&root_j] {
                self.parent.insert(root_j, root_i);
            } else {
                self.parent.insert(root_j, root_i);
                *self.rank.entry(root_i).or_insert(0) += 1;
            }
            true
        } else {
            false
        }
    }
}

// GraphData構造体の実装
#[derive(Default)]
struct GraphData<T: Hash + Eq + Copy + Ord + Debug> {
    data: HashMap<T, Vec<(T, i32)>>,
}

impl<T: Hash + Eq + Copy + Ord + Debug> GraphData<T> {
    fn new() -> Self {
        GraphData {
            data: HashMap::new(),
        }
    }
    
    fn get(&self) -> &HashMap<T, Vec<(T, i32)>> {
        &self.data
    }
    
    fn get_vertices(&self) -> Vec<T> {
        self.data.keys().copied().collect()
    }
    
    fn get_edges(&self) -> Vec<(T, T, i32)> {
        let mut edges = HashSet::new();
        
        for (&vertex, neighbors) in &self.data {
            for &(neighbor, weight) in neighbors {
                let edge = if vertex < neighbor {
                    (vertex, neighbor, weight)
                } else {
                    (neighbor, vertex, weight)
                };
                edges.insert(edge);
            }
        }
        
        edges.into_iter().collect()
    }
        
    fn add_vertex(&mut self, vertex: T) -> bool {
        self.data.entry(vertex).or_insert_with(Vec::new);
        true
    }
    
    fn add_edge(&mut self, vertex1: T, vertex2: T, weight: i32) -> bool {
        // 頂点が存在しない場合は追加
        self.add_vertex(vertex1);
        self.add_vertex(vertex2);
        
        // vertex1 -> vertex2の辺を追加
        let mut edge_exists_v1v2 = false;
        if let Some(neighbors) = self.data.get_mut(&vertex1) {
            for i in 0..neighbors.len() {
                if neighbors[i].0 == vertex2 {
                    neighbors[i].1 = weight;
                    edge_exists_v1v2 = true;
                    break;
                }
            }
            if !edge_exists_v1v2 {
                neighbors.push((vertex2, weight));
            }
        }
        
        // vertex2 -> vertex1の辺を追加
        let mut edge_exists_v2v1 = false;
        if let Some(neighbors) = self.data.get_mut(&vertex2) {
            for i in 0..neighbors.len() {
                if neighbors[i].0 == vertex1 {
                    neighbors[i].1 = weight;
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
    
    fn clear(&mut self) -> bool {
        self.data.clear();
        true
    }
    
    fn get_mst(&mut self) -> Vec<(T, T, i32)> {
        // 1. 全ての辺を取得し、重みでソート
        let mut edges = self.get_edges();
        edges.sort_by_key(|&(_, _, weight)| weight);
        
        // 2. DSU (Disjoint Set Union) を初期化
        let vertices = self.get_vertices();
        let mut dsu = DSU::new(&vertices);
        
        // 3. MST構築
        let mut mst_edges = Vec::new();
        let mut edges_count = 0;
        
        // ソートされた辺を順番に調べる
        for (u, v, weight) in edges {
            let root_u = dsu.find(u);
            let root_v = dsu.find(v);
            
            // 両端点が異なる集合に属する場合、辺をMSTに追加
            if root_u != root_v {
                mst_edges.push((u, v, weight));
                dsu.union(u, v);
                edges_count += 1;
                
                // 頂点数-1の辺が追加されたらMSTが完成
                if edges_count == vertices.len() - 1 {
                    break;
                }
            }
        }
        
        mst_edges
    }
}

fn main() {
    println!("Kruskal TEST -----> start");
    let mut graph_data: GraphData<&str> = GraphData::new();
    
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
    
    let output_mst = graph_data.get_mst();
    for (v1, v2, weight) in &output_mst {
        println!("Edge: {} - {}, Weight: {}", v1, v2, weight);
    }
    
    let total_weight: i32 = output_mst.iter().map(|&(_, _, weight)| weight).sum();
    println!("最小全域木の合計重み: {}", total_weight);
    
    graph_data.clear();
    let input_list = vec![
        ("A", "B", 4), ("C", "D", 4), ("E", "F", 1), ("F", "G", 1)
    ];
    
    for &(v1, v2, weight) in &input_list {
        graph_data.add_edge(v1, v2, weight);
    }
    
    println!("\nグラフの頂点: {:?}", graph_data.get_vertices());
    println!("グラフの辺 (重み付き): {:?}", graph_data.get_edges());
    
    let output_mst = graph_data.get_mst();
    for (v1, v2, weight) in &output_mst {
        println!("Edge: {} - {}, Weight: {}", v1, v2, weight);
    }
    
    let total_weight: i32 = output_mst.iter().map(|&(_, _, weight)| weight).sum();
    println!("最小全域木の合計重み: {}", total_weight);
    
    graph_data.clear();
    let input_list = vec![
        ("A", "B", 4), ("B", "C", 3), ("D", "E", 5)
    ];
    
    for &(v1, v2, weight) in &input_list {
        graph_data.add_edge(v1, v2, weight);
    }
    
    println!("\nグラフの頂点: {:?}", graph_data.get_vertices());
    println!("グラフの辺 (重み付き): {:?}", graph_data.get_edges());
    
    let output_mst = graph_data.get_mst();
    for (v1, v2, weight) in &output_mst {
        println!("Edge: {} - {}, Weight: {}", v1, v2, weight);
    }
    
    let total_weight: i32 = output_mst.iter().map(|&(_, _, weight)| weight).sum();
    println!("最小全域木の合計重み: {}", total_weight);
    
    graph_data.clear();
    let input_list: Vec<(&str, &str, i32)> = vec![];
    
    for &(v1, v2, weight) in &input_list {
        graph_data.add_edge(v1, v2, weight);
    }
    
    println!("\nグラフの頂点: {:?}", graph_data.get_vertices());
    println!("グラフの辺 (重み付き): {:?}", graph_data.get_edges());
    
    let output_mst = graph_data.get_mst();
    for (v1, v2, weight) in &output_mst {
        println!("Edge: {} - {}, Weight: {}", v1, v2, weight);
    }
    
    let total_weight: i32 = output_mst.iter().map(|&(_, _, weight)| weight).sum();
    println!("最小全域木の合計重み: {}", total_weight);
    
    println!("\nKruskal TEST <----- end");
}