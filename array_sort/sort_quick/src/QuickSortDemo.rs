// Rust
// 配列の並び替え: クイックソート (Quick Sort)

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

    fn quick_sort(&mut self, target: &[i32]) -> Vec<i32> {
        // 空の配列または要素が1つの場合はそのまま返す（基底条件）
        if target.len() <= 1 {
            return target.to_vec();
        }
        
        // ピボットを選択（この実装では最後の要素を選択）
        let pivot = target[target.len() - 1];
        
        // ピボットより小さい要素と大きい要素に分ける
        let mut left = Vec::new();
        let mut right = Vec::new();
        
        // 最後の要素（ピボット）を除いて配列をスキャン
        for i in 0..target.len() - 1 {
            if target[i] <= pivot {
                left.push(target[i]);
            } else {
                right.push(target[i]);
            }
        }
        
        // 左側の部分配列、ピボット、右側の部分配列を再帰的にソートして結合
        let mut result = self.quick_sort(&left);
        result.push(pivot);
        result.append(&mut self.quick_sort(&right));
        
        result
    }

    fn sort(&mut self) -> bool {
        let sorted = self.quick_sort(&self.data);
        self.data = sorted;
        true
    }
}

fn main() {
    println!("QuickSort TEST -----> start");

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

    println!("\nQuickSort TEST <----- end");
}