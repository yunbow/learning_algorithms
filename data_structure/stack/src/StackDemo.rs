// Rust
// データ構造: スタック (Stack)

struct StackData<T> {
    data: Vec<T>,
}

impl<T: PartialEq + std::fmt::Debug + Clone> StackData<T> {
    fn new() -> Self {
        StackData { data: Vec::new() }
    }

    fn get(&self) -> &Vec<T> {
        &self.data
    }

    fn get_index(&self, item: T) -> i32 {
        match self.data.iter().position(|x| *x == item) {
            Some(index) => index as i32,
            None => {
                println!("ERROR: {:?} は範囲外です", item);
                -1
            }
        }
    }

    fn get_value(&self, index: usize) -> Option<T> {
        if index < self.data.len() {
            Some(self.data[index].clone())
        } else {
            println!("ERROR: {} は範囲外です", index);
            None
        }
    }

    fn push(&mut self, item: T) -> bool {
        self.data.push(item);
        true
    }

    fn pop(&mut self) -> bool {
        if !self.is_empty() {
            self.data.pop();
            true
        } else {
            println!("ERROR: 空です");
            false
        }
    }

    fn peek(&self) -> Option<&T> {
        self.data.last()
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
    println!("Stack TEST -----> start");

    println!("\nnew");
    let mut stack_data = StackData::<i32>::new();
    println!("  現在のデータ: {:?}", stack_data.get());

    println!("\nis_empty");
    let output = stack_data.is_empty();
    println!("  出力値: {}", output);

    println!("\nsize");
    let output = stack_data.size();
    println!("  出力値: {}", output);

    println!("\npush");
    let items_to_push = [10, 20, 30, 40];
    for item in items_to_push.iter() {
        println!("  入力値: {}", item);
        let output = stack_data.push(*item);
        println!("  出力値: {}", output);
        println!("  現在のデータ: {:?}", stack_data.get());
    }

    println!("\nsize");
    let output = stack_data.size();
    println!("  出力値: {}", output);

    println!("\nis_empty");
    let output = stack_data.is_empty();
    println!("  出力値: {}", output);

    println!("\npeek");
    let output = stack_data.peek();
    println!("  出力値: {:?}", output);

    println!("\nget_index");
    let input = 30;
    println!("  入力値: {}", input);
    let output = stack_data.get_index(input);
    println!("  出力値: {}", output);

    println!("\nget_index");
    let input = 50;
    println!("  入力値: {}", input);
    let output = stack_data.get_index(input);
    println!("  出力値: {}", output);

    println!("\npop");
    while !stack_data.is_empty() {
        let output = stack_data.pop();
        println!("  出力値: {}", output);
        println!("  現在のデータ: {:?}", stack_data.get());
    }

    println!("\nis_empty");
    let output = stack_data.is_empty();
    println!("  出力値: {}", output);

    println!("\nsize");
    let output = stack_data.size();
    println!("  出力値: {}", output);

    println!("\npop");
    let output = stack_data.pop();
    println!("  出力値: {}", output);

    println!("\npeek");
    let output = stack_data.peek();
    println!("  出力値: {:?}", output);

    println!("\nStack TEST <----- end");
}