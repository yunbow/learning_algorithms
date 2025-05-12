// Rust
// データ構造: ヒープ (Heap)

use std::collections::BinaryHeap;
use std::cmp::Reverse;

pub struct HeapData<T: Ord> {
    data: BinaryHeap<T>,
    is_min_heap: bool,
}

impl<T: Ord + Clone> HeapData<T> {
    pub fn new(is_min_heap: bool) -> Self {
        HeapData {
            data: BinaryHeap::new(),
            is_min_heap,
        }
    }

    pub fn get(&self) -> Vec<T> {
        if self.is_min_heap {
            self.data.clone().into_sorted_vec().into_iter().rev().collect()
        } else {
            self.data.clone().into_sorted_vec()
        }
    }

    pub fn get_index(&self, item: &T) -> Option<usize> 
    where T: PartialEq {
        self.get().iter().position(|x| x == item)
    }

    pub fn get_value(&self, index: usize) -> Option<&T> {
        self.get().get(index)
    }

    pub fn heapify(&mut self, array: Vec<T>) -> bool {
        if self.is_min_heap {
            self.data = array.into_iter().map(Reverse).collect();
        } else {
            self.data = array.into_iter().collect();
        }
        true
    }

    pub fn push(&mut self, value: T) -> bool {
        if self.is_min_heap {
            self.data.push(Reverse(value));
        } else {
            self.data.push(value);
        }
        true
    }

    pub fn pop(&mut self) -> bool {
        if self.is_min_heap {
            self.data.pop();
        } else {
            self.data.pop();
        }
        true
    }

    pub fn peek(&self) -> Option<T> {
        if self.is_min_heap {
            self.data.peek().map(|x| x.0.clone())
        } else {
            self.data.peek().cloned()
        }
    }

    pub fn is_empty(&self) -> bool {
        self.data.is_empty()
    }

    pub fn size(&self) -> usize {
        self.data.len()
    }

    pub fn clear(&mut self) -> bool {
        self.data.clear();
        true
    }
}

fn main() {
    println!("Heap TEST -----> start");

    println!("\nmin heap: new");
    let mut min_heap = HeapData::<i32>::new(true);
    println!("  現在のデータ: {:?}", min_heap.get());

    println!("\nmin heap: heapify");
    let input = vec![4, 10, 3, 5, 1];
    println!("  入力値: {:?}", input);
    let output = min_heap.heapify(input);
    println!("  出力値: {:?}", output);
    println!("  現在のデータ: {:?}", min_heap.get());

    println!("\nmin heap: push");
    let input = 2;
    println!("  入力値: {}", input);
    let output = min_heap.push(input);
    println!("  出力値: {:?}", output);
    println!("  現在のデータ: {:?}", min_heap.get());

    println!("\nmin heap: push");
    let input = 15;
    println!("  入力値: {}", input);
    let output = min_heap.push(input);
    println!("  出力値: {:?}", output);
    println!("  現在のデータ: {:?}", min_heap.get());

    println!("\nmin heap: peek");
    let output = min_heap.peek();
    println!("  出力値: {:?}", output);

    println!("\nmin heap: pop");
    let output = min_heap.pop();
    println!("  出力値: {:?}", output);
    println!("  現在のデータ: {:?}", min_heap.get());

    println!("\nmin heap: pop");
    let output = min_heap.pop();
    println!("  出力値: {:?}", output);
    println!("  現在のデータ: {:?}", min_heap.get());

    println!("\nmin heap: get_index");
    let input = 3;
    println!("  入力値: {}", input);
    let output = min_heap.get_index(&input);
    println!("  出力値: {:?}", output);

    println!("\nmin heap: get_index");
    let input = 100;
    println!("  入力値: {}", input);
    let output = min_heap.get_index(&input);
    println!("  出力値: {:?}", output);

    println!("\nmin heap: is_empty");
    let output = min_heap.is_empty();
    println!("  出力値: {}", output);

    println!("\nmin heap: size");
    let output = min_heap.size();
    println!("  出力値: {}", output);

    println!("\nmin heap: clear");
    let output = min_heap.clear();
    println!("  出力値: {:?}", output);
    println!("  現在のデータ: {:?}", min_heap.get());

    println!("\nmin heap: is_empty");
    let output = min_heap.is_empty();
    println!("  出力値: {}", output);

    println!("\nmax heap: new");
    let mut max_heap = HeapData::<i32>::new(false);
    println!("  現在のデータ: {:?}", max_heap.get());

    println!("\nmax heap: heapify");
    let input = vec![4, 10, 3, 5, 1];
    println!("  入力値: {:?}", input);
    let output = max_heap.heapify(input);
    println!("  出力値: {:?}", output);
    println!("  現在のデータ: {:?}", max_heap.get());

    println!("\nmax heap: push");
    let input = 12;
    println!("  入力値: {}", input);
    let output = max_heap.push(input);
    println!("  出力値: {:?}", output);
    println!("  現在のデータ: {:?}", max_heap.get());

    println!("\nmax heap: push");
    let input = 0;
    println!("  入力値: {}", input);
    let output = max_heap.push(input);
    println!("  出力値: {:?}", output);
    println!("  現在のデータ: {:?}", max_heap.get());

    println!("\nmax heap: peek");
    let output = max_heap.peek();
    println!("  出力値: {:?}", output);

    println!("\nmax heap: pop");
    let output = max_heap.pop();
    println!("  出力値: {:?}", output);
    println!("  現在のデータ: {:?}", max_heap.get());

    println!("\nmax heap: pop");
    let output = max_heap.pop();
    println!("  出力値: {:?}", output);
    println!("  現在のデータ: {:?}", max_heap.get());

    println!("\nmax heap: get_index");
    let input = 5;
    println!("  入力値: {}", input);
    let output = max_heap.get_index(&input);
    println!("  出力値: {:?}", output);

    println!("\nmax heap: get_index");
    let input = -10;
    println!("  入力値: {}", input);
    let output = max_heap.get_index(&input);
    println!("  出力値: {:?}", output);

    println!("\nmax heap: is_empty");
    let output = max_heap.is_empty();
    println!("  出力値: {}", output);

    println!("\nmax heap: size");
    let output = max_heap.size();
    println!("  出力値: {}", output);

    println!("\nmax heap: clear");
    let output = max_heap.clear();
    println!("  出力値: {:?}", output);
    println!("  現在のデータ: {:?}", max_heap.get());

    println!("\nmax heap: is_empty");
    let output = max_heap.is_empty();
    println!("  出力値: {}", output);

    println!("\nHeap TEST <----- end");
}
