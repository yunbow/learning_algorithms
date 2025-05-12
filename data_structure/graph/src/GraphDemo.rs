// Rust
// データ構造: グラフ (Graph)

use std::collections::{HashMap, HashSet};

#[derive(Debug, Clone)]
struct GraphData {
    data: HashMap<String, Vec<(String, i32)>>,
}

impl GraphData {
    fn new() -> Self {
        GraphData {
            data: HashMap::new(),
        }
    }

    fn get(&self) -> &HashMap<String, Vec<(String, i32)>> {
        &self.data
    }

    fn get_vertices(&self) -> Vec<String> {
        self.data.keys().cloned().collect()
    }

    fn get_edges(&self) -> Vec<(String, String, i32)> {
        let mut edges = HashSet::new();
        for (vertex, neighbors) in &self.data {
            for (neighbor, weight) in neighbors {
                let mut edge_vertices = vec![vertex.clone(), neighbor.clone()];
                edge_vertices.sort();
                edges.insert((edge_vertices[0].clone(), edge_vertices[1].clone(), *weight));
            }
        }
        edges.into_iter().collect()
    }

    fn get_neighbors(&self, vertex: &str) -> Option<&Vec<(String, i32)>> {
        self.data.get(vertex)
    }

    fn get_edge_weight(&self, vertex1: &str, vertex2: &str) -> Option<i32> {
        if let Some(neighbors) = self.data.get(vertex1) {
            neighbors
                .iter()
                .find(|(neighbor, _)| neighbor == vertex2)
                .map(|(_, weight)| *weight)
        } else {
            None
        }
    }

    fn get_vertice(&self, vertex: &str) -> Option<&Vec<(String, i32)>> {
        match self.data.get(vertex) {
            Some(neighbors) => Some(neighbors),
            None => {
                println!("ERROR: {} is out of range", vertex);
                None
            }
        }
    }

    fn get_edge(&self, vertex1: &str, vertex2: &str) -> bool {
        if let Some(neighbors) = self.data.get(vertex1) {
            neighbors.iter().any(|(neighbor, _)| neighbor == vertex2)
        } else {
            false
        }
    }

    fn add_vertex(&mut self, vertex: String) -> bool {
        self.data.entry(vertex).or_insert(Vec::new());
        true
    }

    fn add_edge(&mut self, vertex1: String, vertex2: String, weight: i32) -> bool {
        self.data.entry(vertex1.clone()).or_insert(Vec::new());
        self.data.entry(vertex2.clone()).or_insert(Vec::new());

        // Add or update edge for vertex1 -> vertex2
        if let Some(neighbors) = self.data.get_mut(&vertex1) {
            if let Some(edge) = neighbors.iter_mut().find(|(v, _)| *v == vertex2) {
                edge.1 = weight;
            } else {
                neighbors.push((vertex2.clone(), weight));
            }
        }

        // Add or update edge for vertex2 -> vertex1
        if let Some(neighbors) = self.data.get_mut(&vertex2) {
            if let Some(edge) = neighbors.iter_mut().find(|(v, _)| *v == vertex1) {
                edge.1 = weight;
            } else {
                neighbors.push((vertex1, weight));
            }
        }

        true
    }

    fn remove_vertex(&mut self, vertex: &str) -> bool {
        if self.data.contains_key(vertex) {
            // Remove references to this vertex from other vertices
            for (_, neighbors) in self.data.iter_mut() {
                neighbors.retain(|(v, _)| v != vertex);
            }
            
            // Remove the vertex itself
            self.data.remove(vertex);
            true
        } else {
            println!("ERROR: {} is out of range", vertex);
            false
        }
    }

    fn remove_edge(&mut self, vertex1: &str, vertex2: &str) -> bool {
        let mut removed = false;

        // Remove edge from vertex1's neighbors
        if let Some(neighbors) = self.data.get_mut(vertex1) {
            let original_len = neighbors.len();
            neighbors.retain(|(v, _)| v != vertex2);
            if neighbors.len() < original_len {
                removed = true;
            }
        }

        // Remove edge from vertex2's neighbors
        if let Some(neighbors) = self.data.get_mut(vertex2) {
            let original_len = neighbors.len();
            neighbors.retain(|(v, _)| v != vertex1);
            if neighbors.len() < original_len {
                removed = true;
            }
        }

        if !removed {
            println!("ERROR: {} or {} is out of range", vertex1, vertex2);
        }

        removed
    }

    fn is_empty(&self) -> bool {
        self.data.is_empty()
    }

    fn size(&self) -> usize {
        self.data.len()
    }

    fn clear(&mut self) -> bool {
        self.data.clear();
        true
    }
}

fn main() {
    println!("Graph TEST -----> start");

    println!("\nnew");
    let mut graph_data = GraphData::new();
    println!("  現在のデータ: {:?}", graph_data.get());

    println!("\nis_empty");
    let output = graph_data.is_empty();
    println!("  出力値: {}", output);

    println!("\nsize");
    let output = graph_data.size();
    println!("  出力値: {}", output);

    let input_list = vec!["A", "B", "C"];
    for input in input_list {
        println!("\nadd_vertex");
        println!("  入力値: {}", input);
        let output = graph_data.add_vertex(input.to_string());
        println!("  出力値: {}", output);
    }
    println!("  現在のデータ: {:?}", graph_data.get());

    println!("\nget_vertices");
    let output = graph_data.get_vertices();
    println!("  出力値: {:?}", output);

    println!("\nsize");
    let output = graph_data.size();
    println!("  出力値: {}", output);

    println!("\nadd_edge");
    let input_list = vec![("A", "B", 4), ("B", "C", 2), ("C", "A", 3)];
    for &(v1, v2, w) in &input_list {
        println!("  入力値: ({}, {}, {})", v1, v2, w);
        let output = graph_data.add_edge(v1.to_string(), v2.to_string(), w);
        println!("  出力値: {}", output);
    }
    println!("  現在のデータ: {:?}", graph_data.get());

    println!("\nget_vertices");
    let output = graph_data.get_vertices();
    println!("  出力値: {:?}", output);

    println!("\nget_edges");
    let output = graph_data.get_edges();
    println!("  出力値: {:?}", output);

    println!("\nsize");
    let output = graph_data.size();
    println!("  出力値: {}", output);

    println!("\nget_vertice");
    let input = "B";
    println!("  入力値: '{}'", input);
    let output = graph_data.get_vertice(input);
    println!("  出力値: {:?}", output);

    println!("\nget_vertice");
    let input = "E";
    println!("  入力値: '{}'", input);
    let output = graph_data.get_vertice(input);
    println!("  出力値: {:?}", output);

    println!("\nremove_edge");
    let input = ("A", "B");
    println!("  入力値: {:?}", input);
    let output = graph_data.remove_edge(input.0, input.1);
    println!("  出力値: {}", output);
    println!("  現在のデータ: {:?}", graph_data.get());

    println!("\nremove_edge");
    let input = ("A", "C");
    println!("  入力値: {:?}", input);
    let output = graph_data.remove_edge(input.0, input.1);
    println!("  出力値: {}", output);
    println!("  現在のデータ: {:?}", graph_data.get());

    println!("\nget_edges");
    let output = graph_data.get_edges();
    println!("  出力値: {:?}", output);

    println!("\nremove_vertex");
    let input = "B";
    println!("  入力値: {}", input);
    let output = graph_data.remove_vertex(input);
    println!("  出力値: {}", output);
    println!("  現在のデータ: {:?}", graph_data.get());

    println!("\nremove_vertex");
    let input = "Z";
    println!("  入力値: {}", input);
    let output = graph_data.remove_vertex(input);
    println!("  出力値: {}", output);
    println!("  現在のデータ: {:?}", graph_data.get());

    println!("\nget_vertices");
    let output = graph_data.get_vertices();
    println!("  出力値: {:?}", output);

    println!("\nget_edges");
    let output = graph_data.get_edges();
    println!("  出力値: {:?}", output);

    println!("\nsize");
    let output = graph_data.size();
    println!("  出力値: {}", output);

    println!("\nget_vertice");
    let input = "B";
    println!("  入力値: {}", input);
    let output = graph_data.get_vertice(input);
    println!("  出力値: {:?}", output);

    println!("\nclear");
    let output = graph_data.clear();
    println!("  出力値: {}", output);

    println!("\nis_empty");
    let output = graph_data.is_empty();
    println!("  出力値: {}", output);

    println!("\nsize");
    let output = graph_data.size();
    println!("  出力値: {}", output);

    println!("\nget_vertices");
    let output = graph_data.get_vertices();
    println!("  出力値: {:?}", output);

    println!("\nget_edges");
    let output = graph_data.get_edges();
    println!("  出力値: {:?}", output);

    println!("\nGraph TEST <----- end");
}
