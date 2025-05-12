// Rust
// データ構造: マップ (Map)

use std::collections::HashMap;

struct MapData {
    data: HashMap<String, i32>,
}

impl MapData {
    fn new() -> Self {
        MapData {
            data: HashMap::new(),
        }
    }

    fn get(&self) -> Vec<(String, i32)> {
        self.data.clone().into_iter().collect()
    }
    
    fn get_keys(&self) -> Vec<String> {
        self.data.keys().cloned().collect()
    }
    
    fn get_values(&self) -> Vec<i32> {
        self.data.values().cloned().collect()
    }
    
    fn get_key(&self, value: i32) -> Option<String> {
        self.data.iter()
            .find(|&(_, &v)| v == value)
            .map(|(k, _)| k.clone())
    }
    
    fn get_value(&self, key: &str) -> Option<i32> {
        self.data.get(key).cloned()
    }

    fn add(&mut self, key: String, value: i32) -> bool {
        if self.data.contains_key(&key) {
            println!("ERROR: {} は重複です", key);
            false
        } else {
            self.data.insert(key, value);
            true
        }
    }
    
    fn remove(&mut self, key: &str) -> bool {
        match self.data.remove(key) {
            Some(_) => true,
            None => {
                println!("ERROR: {} は範囲外です", key);
                false
            }
        }
    }
    
    fn update(&mut self, key: String, value: i32) -> bool {
        if self.data.contains_key(&key) {
            self.data.insert(key, value);
            true
        } else {
            println!("ERROR: {} は範囲外です", key);
            false
        }
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
    println!("Map TEST -----> start");

    println!("\nnew");
    let mut map_data = MapData::new();
    println!("  現在のデータ: {:?}", map_data.get());

    println!("\nis_empty");
    let output = map_data.is_empty();
    println!("  出力値: {}", output);

    println!("\nsize");
    let output = map_data.size();
    println!("  出力値: {}", output);

    println!("\nadd");
    let input = ("apple".to_string(), 100);
    println!("  入力値: {:?}", input);
    let output = map_data.add(input.0, input.1);
    println!("  出力値: {}", output);
    println!("  現在のデータ: {:?}", map_data.get());

    println!("\nadd");
    let input = ("banana".to_string(), 150);
    println!("  入力値: {:?}", input);
    let output = map_data.add(input.0, input.1);
    println!("  出力値: {}", output);
    println!("  現在のデータ: {:?}", map_data.get());

    println!("\nadd");
    let input = ("apple".to_string(), 200);
    println!("  入力値: {:?}", input);
    let output = map_data.add(input.0, input.1);
    println!("  出力値: {}", output);
    println!("  現在のデータ: {:?}", map_data.get());

    println!("\nsize");
    let output = map_data.size();
    println!("  出力値: {}", output);

    println!("\nget");
    let input = "apple";
    println!("  入力値: {}", input);
    let output = map_data.get_value(input);
    println!("  出力値: {:?}", output);

    println!("\nget");
    let input = "orange";
    println!("  入力値: {}", input);
    let output = map_data.get_value(input);
    println!("  出力値: {:?}", output);

    println!("\nupdate");
    let input = ("banana".to_string(), 180);
    println!("  入力値: {:?}", input);
    let output = map_data.update(input.0, input.1);
    println!("  出力値: {}", output);
    println!("  現在のデータ: {:?}", map_data.get());

    println!("\nupdate");
    let input = ("orange".to_string(), 250);
    println!("  入力値: {:?}", input);
    let output = map_data.update(input.0, input.1);
    println!("  出力値: {}", output);
    println!("  現在のデータ: {:?}", map_data.get());

    println!("\nget");
    let input = "banana";
    let output = map_data.get_value(input);
    println!("  出力値: {:?}", output);

    println!("\nget_keys");
    let output = map_data.get_keys();
    println!("  出力値: {:?}", output);

    println!("\nvalues");
    let output = map_data.get_values();
    println!("  出力値: {:?}", output);

    println!("\nget_key");
    let input = 180;
    println!("  入力値: {}", input);
    let output = map_data.get_key(input);
    println!("  出力値: {:?}", output);

    println!("\nget_key");
    let input = 500;
    println!("  入力値: {}", input);
    let output = map_data.get_key(input);
    println!("  出力値: {:?}", output);

    println!("\nremove");
    let input = "apple";
    println!("  入力値: {}", input);
    let output = map_data.remove(input);
    println!("  出力値: {}", output);
    println!("  現在のデータ: {:?}", map_data.get());

    println!("\nremove");
    let input = "orange";
    println!("  入力値: {}", input);
    let output = map_data.remove(input);
    println!("  出力値: {}", output);
    println!("  現在のデータ: {:?}", map_data.get());

    println!("\nsize");
    let output = map_data.size();
    println!("  出力値: {}", output);

    println!("\nget_keys");
    let output = map_data.get_keys();
    println!("  出力値: {:?}", output);

    println!("\nclear");
    let output = map_data.clear();
    println!("  出力値: {}", output);
    println!("  現在のデータ: {:?}", map_data.get());

    println!("\nsize");
    let output = map_data.size();
    println!("  出力値: {}", output);

    println!("\nis_empty");
    let output = map_data.is_empty();
    println!("  出力値: {}", output);

    println!("\nMap TEST <----- end");
}
