// Rust
// データ構造: セット (Set)

use std::collections::HashSet;

struct SetData {
    data: HashSet<i32>,
}

impl SetData {
    fn new() -> Self {
        SetData {
            data: HashSet::new(),
        }
    }

    fn get(&self) -> Vec<i32> {
        self.data.iter().cloned().collect()
    }

    fn get_index(&self, item: &i32) -> Option<usize> {
        self.data.iter().position(|&x| x == *item)
    }

    fn get_value(&self, index: usize) -> Option<i32> {
        self.get().get(index).cloned()
    }

    fn add(&mut self, item: i32) -> bool {
        self.data.insert(item)
    }
    
    fn remove(&mut self, item: &i32) -> bool {
        self.data.remove(item)
    }
    
    fn is_empty(&self) -> bool {
        self.data.is_empty()
    }
    
    fn size(&self) -> usize {
        self.data.len()
    }
    
    fn clear(&mut self) {
        self.data.clear()
    }
}

fn main() {
    println!("Set TEST -----> start");

    println!("\nnew");
    let mut set_data = SetData::new();
    println!("  現在のデータ: {:?}", set_data.get());

    println!("\nadd");
    let input = [10, 20, 30, 20, 40];
    for &item in &input {
        println!("  入力値: {}", item);
        let output = set_data.add(item);
        println!("  出力値: {}", output);
        println!("  現在のデータ: {:?}", set_data.get());
    }

    println!("\nsize");
    let output = set_data.size();
    println!("  出力値: {}", output);

    println!("\nis_empty");
    let output = set_data.is_empty();
    println!("  出力値: {}", output);

    println!("\nget_value");
    let input = [0, 2, 5];
    for &index in &input {
        println!("  入力値: {}", index);
        let output = set_data.get_value(index);
        println!("  出力値: {:?}", output);
    }

    println!("\nget_index");
    let input = [30, 99];
    for &item in &input {
        println!("  入力値: {}", item);
        let output = set_data.get_index(&item);
        println!("  出力値: {:?}", output);
    }

    println!("\nremove");
    let input = [20, 50, 10];
    for &item in &input {
        println!("  入力値: {}", item);
        let output = set_data.remove(&item);
        println!("  出力値: {}", output);
        println!("  現在のデータ: {:?}", set_data.get());
    }
    
    println!("\nsize");
    let output = set_data.size();
    println!("  出力値: {}", output);

    println!("\nclear");
    set_data.clear();
    println!("  現在のデータ: {:?}", set_data.get());

    println!("\nis_empty");
    let output = set_data.is_empty();
    println!("  出力値: {}", output);

    println!("\nSet TEST <----- end");
}
