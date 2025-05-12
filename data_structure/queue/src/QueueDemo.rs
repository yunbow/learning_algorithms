// Rust
// データ構造: キュー (Queue)

use std::collections::VecDeque;

struct QueueData<T> {
    data: VecDeque<T>,
}

impl<T: Clone + std::fmt::Debug + PartialEq> QueueData<T> {
    fn new() -> Self {
        QueueData {
            data: VecDeque::new(),
        }
    }

    fn get(&self) -> Vec<T> {
        self.data.iter().cloned().collect()
    }

    fn get_index(&self, item: &T) -> i32 {
        match self.data.iter().position(|x| x == item) {
            Some(index) => index as i32,
            None => {
                println!("ERROR: {:?} is out of range", item);
                -1
            }
        }
    }

    fn get_value(&self, index: usize) -> Option<T> {
        if index < self.data.len() {
            self.data.get(index).cloned()
        } else {
            println!("Error: Index {} is out of range", index);
            None
        }
    }

    fn enqueue(&mut self, item: T) -> bool {
        self.data.push_back(item);
        true
    }

    fn dequeue(&mut self) -> bool {
        if !self.is_empty() {
            self.data.pop_front();
            true
        } else {
            println!("ERROR: Queue is empty");
            false
        }
    }

    fn peek(&self) -> Option<T> {
        if !self.is_empty() {
            self.data.front().cloned()
        } else {
            println!("ERROR: Queue is empty");
            None
        }
    }

    fn is_empty(&self) -> bool {
        self.data.is_empty()
    }

    fn size(&self) -> usize {
        self.data.len()
    }

    fn clear(&mut self) -> bool {
        self.data.clear();
        true
    }
}

fn main() {
    println!("Queue TEST -----> start");

    println!("\nnew");
    let mut queue_data = QueueData::<i32>::new();
    println!("  現在のデータ: {:?}", queue_data.get());

    println!("\nis_empty");
    let output = queue_data.is_empty();
    println!("  出力値: {}", output);

    println!("\nenqueue");
    let input = vec![10, 20, 30];
    for item in input {
        println!("  入力値: {}", item);
        let output = queue_data.enqueue(item);
        println!("  出力値: {}", output);
        println!("  現在のデータ: {:?}", queue_data.get());
    }

    println!("\nsize");
    let output = queue_data.size();
    println!("  出力値: {}", output);

    println!("\npeek");
    let output = queue_data.peek();
    println!("  出力値: {:?}", output);

    println!("\nget_index");
    let input = 20;
    println!("  入力値: {}", input);
    let output = queue_data.get_index(&input);
    println!("  出力値: {}", output);

    println!("\nget_index");
    let input = 50;
    println!("  入力値: {}", input);
    let output = queue_data.get_index(&input);
    println!("  出力値: {}", output);

    println!("\ndequeue");
    let output = queue_data.dequeue();
    println!("  出力値: {}", output);
    println!("  現在のデータ: {:?}", queue_data.get());

    println!("\ndequeue");
    let output = queue_data.dequeue();
    println!("  出力値: {}", output);
    println!("  現在のデータ: {:?}", queue_data.get());

    println!("\nsize");
    let output = queue_data.size();
    println!("  出力値: {}", output);

    println!("\ndequeue");
    let output = queue_data.dequeue();
    println!("  出力値: {}", output);
    println!("  現在のデータ: {:?}", queue_data.get());

    println!("\ndequeue");
    let output = queue_data.dequeue();
    println!("  出力値: {}", output);
    println!("  現在のデータ: {:?}", queue_data.get());

    println!("\nis_empty");
    let output = queue_data.is_empty();
    println!("  出力値: {}", output);

    println!("\nclear");
    let output = queue_data.clear();
    println!("  出力値: {}", output);
    println!("  現在のデータ: {:?}", queue_data.get());

    println!("\nsize");
    let output = queue_data.size();
    println!("  出力値: {}", output);

    println!("\nQueue TEST <----- end");
}
