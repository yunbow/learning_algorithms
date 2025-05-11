// Rust
// 配列の検索: 線形探索 (Linear Search)

struct ArrayData {
    data: Vec<i32>,
}

impl ArrayData {
    fn new() -> Self {
        ArrayData { data: Vec::new() }
    }

    fn get(&self) -> &Vec<i32> {
        &self.data
    }

    fn set(&mut self, data: Vec<i32>) -> bool {
        self.data = data;
        true
    }

    fn search(&self, target: i32) -> i32 {
        // 配列の要素を順番に確認
        for i in 0..self.data.len() {
            // 目的の値が見つかった場合、そのインデックスを返す
            if self.data[i] == target {
                return i as i32;
            }
        }
        
        // 見つからなかった場合は -1 を返す
        -1
    }
}

fn main() {
    println!("LinearSearch TEST -----> start");

    println!("\nnew");
    let mut array_data = ArrayData::new();
    let input = vec![1, 3, 5, 7, 9, 11, 13, 15, 17, 19];
    array_data.set(input);
    println!("  現在のデータ: {:?}", array_data.get());
    
    println!("\nsearch");
    let input = 7;
    println!("  入力値: {}", input);
    let output = array_data.search(input);
    println!("  出力値: {}", output);

    println!("\nsearch");
    let input = 30;
    println!("  入力値: {}", input);
    let output = array_data.search(input);
    println!("  出力値: {}", output);

    println!("\nLinearSearch TEST <----- end");
}