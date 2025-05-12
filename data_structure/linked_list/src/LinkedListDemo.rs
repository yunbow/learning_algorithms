// Rust
// データ構造: 連結リスト (Linked List)

use std::cell::RefCell;
use std::rc::Rc;

#[derive(Clone)]
struct NodeData<T> {
    data: T,
    next: Option<Rc<RefCell<NodeData<T>>>>,
}

impl<T: Clone> NodeData<T> {
    fn new(data: T) -> Self {
        NodeData {
            data,
            next: None,
        }
    }
}

struct LinkedListData<T: Clone> {
    data: Option<Rc<RefCell<NodeData<T>>>>,
    size: usize,
}

impl<T: Clone + PartialEq> LinkedListData<T> {
    fn new() -> Self {
        LinkedListData {
            data: None,
            size: 0,
        }
    }

    fn get_position(&self, data: &T) -> Option<usize> {
        if self.is_empty() {
            return None;
        }

        let mut current = self.data.clone();
        let mut position = 0;

        while let Some(node_ref) = current {
            if node_ref.borrow().data == *data {
                return Some(position);
            }
            current = node_ref.borrow().next.clone();
            position += 1;
        }

        None
    }

    fn get_value(&self, position: usize) -> Option<T> {
        if self.is_empty() || position >= self.size {
            println!("ERROR: {} は範囲外です", position);
            return None;
        }

        let mut current = self.data.clone();
        for _ in 0..position {
            current = current.unwrap().borrow().next.clone();
        }

        Some(current.unwrap().borrow().data.clone())
    }

    fn add(&mut self, data: T, position: Option<usize>) -> bool {
        let new_node = Rc::new(RefCell::new(NodeData::new(data)));

        if self.is_empty() {
            self.data = Some(new_node);
            self.size += 1;
            return true;
        }

        let position = position.unwrap_or(self.size);

        if position >= self.size {
            let mut current = self.data.clone().unwrap();
            while let Some(next) = current.borrow().next.clone() {
                current = next;
            }
            current.borrow_mut().next = Some(new_node);
            self.size += 1;
            return true;
        }

        if position == 0 {
            new_node.borrow_mut().next = self.data.clone();
            self.data = Some(new_node);
            self.size += 1;
            return true;
        }

        let mut current = self.data.clone().unwrap();
        for _ in 0..(position - 1) {
            current = current.borrow().next.clone().unwrap();
        }

        new_node.borrow_mut().next = current.borrow().next.clone();
        current.borrow_mut().next = Some(new_node);
        self.size += 1;
        true
    }

    fn remove(&mut self, position: Option<usize>, data: Option<&T>) -> bool {
        if self.is_empty() {
            println!("ERROR: リストが空です");
            return false;
        }

        if let Some(data_to_remove) = data {
            if self.data.as_ref().unwrap().borrow().data == *data_to_remove {
                self.data = self.data.as_ref().unwrap().borrow().next.clone();
                self.size -= 1;
                return true;
            }

            let mut current = self.data.clone().unwrap();
            while let Some(next) = current.borrow().next.clone() {
                if next.borrow().data == *data_to_remove {
                    current.borrow_mut().next = next.borrow().next.clone();
                    self.size -= 1;
                    return true;
                }
                current = next;
            }

            println!("ERROR: {:?} は範囲外です", data_to_remove);
            return false;
        }

        let position = position.unwrap_or(self.size - 1);

        if position >= self.size {
            println!("ERROR: {} は範囲外です", position);
            return false;
        }

        if position == 0 {
            self.data = self.data.as_ref().unwrap().borrow().next.clone();
            self.size -= 1;
            return true;
        }

        let mut current = self.data.clone().unwrap();
        for _ in 0..(position - 1) {
            current = current.borrow().next.clone().unwrap();
        }

        current.borrow_mut().next = current.borrow().next.as_ref().unwrap().borrow().next.clone();
        self.size -= 1;
        true
    }

    fn update(&mut self, position: usize, data: T) -> bool {
        if self.is_empty() || position >= self.size {
            println!("ERROR: {} は範囲外です", position);
            return false;
        }

        let mut current = self.data.clone().unwrap();
        for _ in 0..position {
            current = current.borrow().next.clone().unwrap();
        }

        current.borrow_mut().data = data;
        true
    }

    fn is_empty(&self) -> bool {
        self.data.is_none()
    }

    fn size(&self) -> usize {
        self.size
    }

    fn clear(&mut self) -> bool {
        self.data = None;
        self.size = 0;
        true
    }

    fn display(&self) -> Vec<T> {
        let mut elements = Vec::new();
        let mut current = self.data.clone();

        while let Some(node_ref) = current {
            elements.push(node_ref.borrow().data.clone());
            current = node_ref.borrow().next.clone();
        }

        elements
    }
}

fn main() {
    println!("LinkedList TEST -----> start");

    println!("\nnew");
    let mut linked_list_data: LinkedListData<i32> = LinkedListData::new();
    println!("  現在のデータ: {:?}", linked_list_data.display());

    println!("\nis_empty");
    let output = linked_list_data.is_empty();
    println!("  出力値: {}", output);

    println!("\nsize");
    let output = linked_list_data.size();
    println!("  出力値: {}", output);

    println!("\nadd");
    let input = 10;
    println!("  入力値: {}", input);
    let output = linked_list_data.add(input, None);
    println!("  出力値: {}", output);
    println!("  現在のデータ: {:?}", linked_list_data.display());

    println!("\nadd");
    let input = 20;
    println!("  入力値: {}", input);
    let output = linked_list_data.add(input, None);
    println!("  出力値: {}", output);
    println!("  現在のデータ: {:?}", linked_list_data.display());

    println!("\nadd");
    let input = 5;
    let position = 0;
    println!("  入力値: ({}, {})", input, position);
    let output = linked_list_data.add(input, Some(position));
    println!("  出力値: {}", output);
    println!("  現在のデータ: {:?}", linked_list_data.display());

    println!("\nadd");
    let input = 15;
    let position = 2;
    println!("  入力値: ({}, {})", input, position);
    let output = linked_list_data.add(input, Some(position));
    println!("  出力値: {}", output);
    println!("  現在のデータ: {:?}", linked_list_data.display());

    println!("\nget_position");
    let input = 1;
    println!("  入力値: {}", input);
    let output = linked_list_data.get_position(&input);
    println!("  出力値: {:?}", output);

    println!("\nget_position");
    let input = 10;
    println!("  入力値: {}", input);
    let output = linked_list_data.get_position(&input);
    println!("  出力値: {:?}", output);

    println!("\nupdate");
    let position = 1;
    let input = 99;
    println!("  入力値: ({}, {})", position, input);
    let output = linked_list_data.update(position, input);
    println!("  出力値: {}", output);
    println!("  現在のデータ: {:?}", linked_list_data.display());

    println!("\nget_value");
    let input = 2;
    println!("  入力値: {}", input);
    let output = linked_list_data.get_value(input);
    println!("  出力値: {:?}", output);

    println!("\nget_value");
    let input = 100;
    println!("  入力値: {}", input);
    let output = linked_list_data.get_value(input);
    println!("  出力値: {:?}", output);

    println!("\nremove");
    let input = 15;
    println!("  入力値: data={}", input);
    let output = linked_list_data.remove(None, Some(&input));
    println!("  出力値: {}", output);
    println!("  現在のデータ: {:?}", linked_list_data.display());

    println!("\nremove");
    let input = 0;
    println!("  入力値: position={}", input);
    let output = linked_list_data.remove(Some(input), None);
    println!("  出力値: {}", output);
    println!("  現在のデータ: {:?}", linked_list_data.display());

    println!("\nremove");
    let output = linked_list_data.remove(None, None);
    println!("  出力値: {}", output);
    println!("  現在のデータ: {:?}", linked_list_data.display());

    println!("\nremove");
    let input = 5;
    println!("  入力値: position={}", input);
    let output = linked_list_data.remove(Some(input), None);
    println!("  出力値: {}", output);
    println!("  現在のデータ: {:?}", linked_list_data.display());

    println!("\nclear");
    let output = linked_list_data.clear();
    println!("  出力値: {}", output);
    println!("  現在のデータ: {:?}", linked_list_data.display());

    println!("\nis_empty");
    let output = linked_list_data.is_empty();
    println!("  出力値: {}", output);

    println!("\nsize");
    let output = linked_list_data.size();
    println!("出力値: {}", output);

    println!("\nremove");
    let output = linked_list_data.remove(None, None);
    println!("  出力値: {}", output);
    println!("  現在のデータ: {:?}", linked_list_data.display());

    println!("\nLinkedList TEST <----- end");
}
