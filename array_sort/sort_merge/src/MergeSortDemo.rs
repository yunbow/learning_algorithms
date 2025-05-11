// Rust
// 配列の並び替え: マージソート (Merge Sort)

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

    fn merge_sort(&mut self) -> bool {
        let sorted = self._merge_sort(self.data.clone());
        self.data = sorted;
        true
    }

    fn _merge_sort(&self, target: Vec<i32>) -> Vec<i32> {
        // 配列の長さが1以下の場合はそのまま返す（基本ケース）
        if target.len() <= 1 {
            return target;
        }
        
        // 配列を半分に分割
        let mid = target.len() / 2;
        let left_half = target[..mid].to_vec();
        let right_half = target[mid..].to_vec();
        
        // 左右の半分を再帰的にソート
        let left_half = self._merge_sort(left_half);
        let right_half = self._merge_sort(right_half);
        
        // ソート済みの半分同士をマージ
        self._merge(left_half, right_half)
    }

    fn _merge(&self, left: Vec<i32>, right: Vec<i32>) -> Vec<i32> {
        let mut result = Vec::new();
        let mut i = 0;
        let mut j = 0;
        
        // 左右の配列を比較しながらマージ
        while i < left.len() && j < right.len() {
            if left[i] <= right[j] {
                result.push(left[i]);
                i += 1;
            } else {
                result.push(right[j]);
                j += 1;
            }
        }
        
        // 残った要素を追加
        result.extend_from_slice(&left[i..]);
        result.extend_from_slice(&right[j..]);
        
        result
    }
}

fn main() {
    println!("MergeSort TEST -----> start");

    let mut array_data = ArrayData::new();

    // ランダムな整数の配列
    println!("\nsort");
    let input = vec![64, 34, 25, 12, 22, 11, 90];
    println!("  ソート前: {:?}", input);
    array_data.set(input);
    array_data.merge_sort();
    println!("  ソート後: {:?}", array_data.get());
    
    // 既にソートされている配列
    println!("\nsort");
    let input = vec![1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
    println!("  ソート前: {:?}", input);
    array_data.set(input);
    array_data.merge_sort();
    println!("  ソート後: {:?}", array_data.get());
    
    // 逆順の配列
    println!("\nsort");
    let input = vec![10, 9, 8, 7, 6, 5, 4, 3, 2, 1];
    println!("  ソート前: {:?}", input);
    array_data.set(input);
    array_data.merge_sort();
    println!("  ソート後: {:?}", array_data.get());
    
    // 重複要素を含む配列
    println!("\nsort");
    let input = vec![10, 9, 8, 7, 6, 10, 9, 8, 7, 6];
    println!("  ソート前: {:?}", input);
    array_data.set(input);
    array_data.merge_sort();
    println!("  ソート後: {:?}", array_data.get());
    
    // 空の配列
    println!("\nsort");
    let input: Vec<i32> = vec![];
    println!("  ソート前: {:?}", input);
    array_data.set(input);
    array_data.merge_sort();
    println!("  ソート後: {:?}", array_data.get());

    println!("\nMergeSort TEST <----- end");
}