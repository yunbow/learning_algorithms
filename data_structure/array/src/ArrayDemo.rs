// Rust
// データ構造: 配列 (Array)

struct ArrayData {
    data: Vec<i32>,
}

impl ArrayData {
    fn new() -> Self {
        ArrayData { data: Vec::new() }
    }

    fn get(&self) -> &Vec<i32> {
        // 要素を取得
        &self.data
    }

    fn get_index(&self, item: i32) -> i32 {
        // 配列内で指定された値を検索し、最初に見つかったインデックスを返す
        match self.data.iter().position(|&x| x == item) {
            Some(index) => index as i32,
            None => {
                println!("ERROR: {} は範囲外です", item);
                -1
            }
        }
    }

    fn get_value(&self, index: usize) -> Option<i32> {
        // 指定されたインデックスの要素を取得する
        if index < self.data.len() {
            Some(self.data[index])
        } else {
            println!("ERROR: {} は範囲外です", index);
            None
        }
    }

    fn add(&mut self, item: i32) -> bool {
        // 配列の末尾に要素を追加する
        self.data.push(item);
        true
    }

    fn remove(&mut self, index: usize) -> bool {
        // 指定されたインデックスの要素を削除する
        if index < self.data.len() {
            self.data.remove(index);
            true
        } else {
            println!("ERROR: {} は範囲外です", index);
            false
        }
    }

    fn update(&mut self, index: usize, new_value: i32) -> bool {
        // 指定されたインデックスの要素を新しい値に更新する
        if index < self.data.len() {
            self.data[index] = new_value;
            true
        } else {
            println!("ERROR: {} は範囲外です", index);
            false
        }
    }

    fn reverse(&mut self) -> &Vec<i32> {
        // 配列の要素を逆順にする
        self.data.reverse();
        &self.data
    }

    fn sort(&mut self, descending: bool) -> &Vec<i32> {
        // 配列の要素をソートする
        if descending {
            self.data.sort_by(|a, b| b.cmp(a));
        } else {
            self.data.sort();
        }
        &self.data
    }

    fn is_empty(&self) -> bool {
        // 配列が空かどうか
        self.data.is_empty()
    }

    fn size(&self) -> usize {
        // 配列のサイズ（要素数）を返す
        self.data.len()
    }

    fn clear(&mut self) -> bool {
        // 配列の全要素を削除する
        self.data.clear();
        true
    }
}

fn main() {
    println!("Array TEST -----> start");

    println!("\nnew");
    let mut array_data = ArrayData::new();
    println!("  現在のデータ: {:?}", array_data.get());

    println!("\nadd");
    let input = [10, 20, 30, 10, 40];
    for &item in &input {
        println!("  入力値: {}", item);
        let output = array_data.add(item);
        println!("  出力値: {}", output);
        println!("  現在のデータ: {:?}", array_data.get());
    }

    println!("\nsize");
    let output = array_data.size();
    println!("  出力値: {}", output);

    println!("\nis_empty");
    let output = array_data.is_empty();
    println!("  出力値: {}", output);

    println!("\nget_value");
    let input = 2;
    println!("  入力値: {}", input);
    let output = array_data.get_value(input);
    println!("  出力値: {:?}", output);

    println!("\nget_value");
    let input = 10;
    println!("  入力値: {}", input);
    let output = array_data.get_value(input);
    println!("  出力値: {:?}", output);

    println!("\nupdate");
    let index = 1;
    let value = 25;
    println!("  入力値: ({}, {})", index, value);
    let output = array_data.update(index, value);
    println!("  出力値: {}", output);
    println!("  現在のデータ: {:?}", array_data.get());

    println!("\nupdate");
    let index = 15;
    let value = 25;
    println!("  入力値: ({}, {})", index, value);
    let output = array_data.update(index, value);
    println!("  出力値: {}", output);
    println!("  現在のデータ: {:?}", array_data.get());

    println!("\nget_index");
    let input = 10;
    println!("  入力値: {}", input);
    let output = array_data.get_index(input);
    println!("  出力値: {}", output);

    println!("\nget_index");
    let input = 99;
    println!("  入力値: {}", input);
    let output = array_data.get_index(input);
    println!("  出力値: {}", output);

    println!("\nremove");
    let input = 3;
    println!("  入力値: {}", input);
    let output = array_data.remove(input);
    println!("  出力値: {}", output);
    println!("  現在のデータ: {:?}", array_data.get());

    println!("\nremove");
    let input = 8;
    println!("  入力値: {}", input);
    let output = array_data.remove(input);
    println!("  出力値: {}", output);
    println!("  現在のデータ: {:?}", array_data.get());

    println!("\nreverse");
    let output = array_data.reverse();
    println!("  出力値: {:?}", output);

    println!("\nsort");
    println!("  入力値: descending=false");
    let output = array_data.sort(false);
    println!("  出力値: {:?}", output);

    println!("\nsort");
    println!("  入力値: descending=true");
    let output = array_data.sort(true);
    println!("  出力値: {:?}", output);

    println!("\nclear");
    let output = array_data.clear();
    println!("  出力値: {}", output);
    println!("  現在のデータ: {:?}", array_data.get());

    println!("\nis_empty");
    let output = array_data.is_empty();
    println!("  出力値: {}", output);

    println!("\nArray TEST <----- end");
}