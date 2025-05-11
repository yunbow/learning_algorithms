// Rust
// 配列の並び替え: 選択ソート (Selection Sort)

struct ArrayData {
    data: Vec<i32>,
}

impl ArrayData {
    fn new() -> Self {
        ArrayData {
            data: Vec::new(),
        }
    }

    fn get(&self) -> &Vec<i32> {
        &self.data
    }

    fn set(&mut self, data: Vec<i32>) -> bool {
        self.data = data;
        true
    }

    fn selection_sort(&mut self, target: &mut Vec<i32>) -> &Vec<i32> {
        // 配列の長さを取得
        let n = target.len();
        
        // 配列を順番に走査
        for i in 0..n {
            // 未ソート部分の最小値のインデックスを見つける
            let mut min_index = i;
            for j in (i + 1)..n {
                if target[j] < target[min_index] {
                    min_index = j;
                }
            }
            
            // 見つかった最小値と現在の位置を交換
            target.swap(i, min_index);
        }
        
        target
    }

    fn sort(&mut self) -> bool {
        let mut data_clone = self.data.clone();
        self.selection_sort(&mut data_clone);
        self.data = data_clone;
        true
    }
}

fn main() {
    println!("SelectionSort TEST -----> start");

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

    println!("\nSelectionSort TEST <----- end");
}