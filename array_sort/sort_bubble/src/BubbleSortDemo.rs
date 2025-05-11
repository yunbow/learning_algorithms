// Rust
// 配列の並び替え: バブルソート (Bubble Sort)

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
        
        // 外側のループ: n-1回の走査が必要
        for i in 0..n {
            // 最適化: 一度の走査で交換がなければソート完了
            let mut swapped = false;
            
            // 内側のループ: まだソートされていない部分を走査
            // 各走査後に最大の要素が末尾に移動するため、i回分を除外
            for j in 0..n.saturating_sub(i + 1) {
                // 隣接する要素を比較し、必要に応じて交換
                if j + 1 < n && self.data[j] > self.data[j+1] {
                    self.data.swap(j, j+1);
                    swapped = true;
                }
            }
            
            // 交換が発生しなければソート完了
            if !swapped {
                break;
            }
        }
        true
    }
}

fn main() {
    println!("BubbleSort TEST -----> start");

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

    println!("\nBubbleSort TEST <----- end");
}