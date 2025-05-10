use std::collections::HashMap;

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
        // ハッシュテーブルの作成
        let mut hash_table: HashMap<i32, i32> = HashMap::new();
        
        // 配列の要素をハッシュテーブルに格納
        // キーを要素の値、値をインデックスとする
        for (i, &value) in self.data.iter().enumerate() {
            hash_table.insert(value, i as i32);
        }
        
        // ハッシュテーブルを使って検索
        match hash_table.get(&target) {
            Some(&index) => index,
            None => -1,
        }
    }
}

fn main() {
    println!("HashSearch TEST -----> start");

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

    println!("\nHashSearch TEST <----- end");
}
