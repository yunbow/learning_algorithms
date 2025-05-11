// Rust
// 配列の並び替え: 挿入ソート (Insertion Sort)

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

    fn sort(&mut self) -> bool {
        let n = self.data.len();

        // 2番目の要素から始める（最初の要素は既にソート済みと見なす）
        for i in 1..n {
            // 現在の要素を取得
            let key = self.data[i];
            
            // ソート済み部分の最後の要素のインデックス
            let mut j = i as isize - 1;
            
            // keyより大きい要素をすべて右にシフト
            while j >= 0 && self.data[j as usize] > key {
                self.data[(j + 1) as usize] = self.data[j as usize];
                j -= 1;
            }
            
            // 適切な位置にkeyを挿入
            self.data[(j + 1) as usize] = key;
        }
        
        true
    }
}

fn main() {
    println!("InsertionSort TEST -----> start");

    let mut array_data = ArrayData::new();

    // ランダムな整数の配列
    println!("\nsort");
    let input = vec![64, 34, 25, 12, 22, 11, 90];
    println!("  ソート前: {:?}", input);
    array_data.set(input);
    array_data.sort();
    println!("  ソート後: {:?}", array_data.get());
    
    // 既にソートされている配列
    println!("\nsort");
    let input = vec![1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
    println!("  ソート前: {:?}", input);
    array_data.set(input);
    array_data.sort();
    println!("  ソート後: {:?}", array_data.get());
    
    // 逆順の配列
    println!("\nsort");
    let input = vec![10, 9, 8, 7, 6, 5, 4, 3, 2, 1];
    println!("  ソート前: {:?}", input);
    array_data.set(input);
    array_data.sort();
    println!("  ソート後: {:?}", array_data.get());
    
    // 重複要素を含む配列
    println!("\nsort");
    let input = vec![10, 9, 8, 7, 6, 10, 9, 8, 7, 6];
    println!("  ソート前: {:?}", input);
    array_data.set(input);
    array_data.sort();
    println!("  ソート後: {:?}", array_data.get());
    
    // 空の配列
    println!("\nsort");
    let input: Vec<i32> = vec![];
    println!("  ソート前: {:?}", input);
    array_data.set(input);
    array_data.sort();
    println!("  ソート後: {:?}", array_data.get());

    println!("\nInsertionSort TEST <----- end");
}