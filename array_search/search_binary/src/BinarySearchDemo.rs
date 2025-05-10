// Rust
// 配列の検索: 二分探索 (Binary Search)

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
        let mut left = 0;
        let mut right = self.data.len() as i32 - 1;
        
        while left <= right {
            let mid = (left + right) / 2;
            
            // 中央の要素が目標値と一致
            if self.data[mid as usize] == target {
                return mid;
            }
            
            // 中央の要素が目標値より小さい場合、右半分を探索
            else if self.data[mid as usize] < target {
                left = mid + 1;
            }
            
            // 中央の要素が目標値より大きい場合、左半分を探索
            else {
                right = mid - 1;
            }
        }
        
        // 目標値が見つからない場合
        -1
    }
}

fn main() {
    println!("BinarySearch TEST -----> start");

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

    println!("\nBinarySearch TEST <----- end");
}