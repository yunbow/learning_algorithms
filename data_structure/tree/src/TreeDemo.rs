// Rust
// データ構造: 木 (Tree)

use std::cell::{RefCell, Ref};
use std::rc::{Rc, Weak};
use std::collections::VecDeque;

type NodeRef<T> = Rc<RefCell<Node<T>>>;
type WeakNodeRef<T> = Weak<RefCell<Node<T>>>;

struct Node<T> {
    value: T,
    parent: Option<WeakNodeRef<T>>,
    children: Vec<NodeRef<T>>,
}

impl<T> Node<T> {
    fn new(value: T) -> Self {
        Node {
            value,
            parent: None,
            children: Vec::new(),
        }
    }

    fn get_value(&self) -> &T {
        &self.value
    }

    fn get_parent(&self) -> Option<NodeRef<T>> {
        self.parent.as_ref().and_then(|weak| weak.upgrade())
    }

    fn get_children(&self) -> &Vec<NodeRef<T>> {
        &self.children
    }

    fn set_parent(&mut self, parent: Option<WeakNodeRef<T>>) -> bool {
        self.parent = parent;
        true
    }

    fn add_child(&mut self, child: NodeRef<T>) -> bool {
        let weak_self = Rc::downgrade(&Rc::new(RefCell::new(self)));
        child.borrow_mut().set_parent(Some(weak_self));
        self.children.push(Rc::clone(&child));
        true
    }

    fn remove_child(&mut self, child: &NodeRef<T>) -> bool {
        let position = self.children.iter().position(|c| Rc::ptr_eq(c, child));
        if let Some(pos) = position {
            child.borrow_mut().set_parent(None);
            self.children.remove(pos);
            true
        } else {
            false
        }
    }

    fn is_leaf(&self) -> bool {
        self.children.is_empty()
    }
}

struct Tree<T: PartialEq + Clone> {
    root: Option<NodeRef<T>>,
}

impl<T: PartialEq + Clone> Tree<T> {
    fn new() -> Self {
        Tree { root: None }
    }

    fn get(&self) -> Option<NodeRef<T>> {
        self.root.clone()
    }

    fn get_height(&self, node: Option<NodeRef<T>>) -> usize {
        let node = match node {
            Some(n) => n,
            None => {
                return match &self.root {
                    Some(root) => self.get_height(Some(Rc::clone(root))),
                    None => 0,
                }
            }
        };

        let node_ref = node.borrow();
        if node_ref.is_leaf() {
            return 1;
        }

        1 + node_ref
            .get_children()
            .iter()
            .map(|child| self.get_height(Some(Rc::clone(child))))
            .max()
            .unwrap_or(0)
    }

    fn get_parent(&self, node: &NodeRef<T>) -> Option<NodeRef<T>> {
        node.borrow().get_parent()
    }

    fn get_children(&self, node: &NodeRef<T>) -> Vec<NodeRef<T>> {
        node.borrow()
            .get_children()
            .iter()
            .map(|child| Rc::clone(child))
            .collect()
    }

    fn get_node(&self, value: &T, node: Option<NodeRef<T>>) -> Option<NodeRef<T>> {
        let node = match node {
            Some(n) => n,
            None => {
                return match &self.root {
                    Some(root) => self.get_node(value, Some(Rc::clone(root))),
                    None => None,
                }
            }
        };

        let node_ref = node.borrow();
        if node_ref.get_value() == value {
            return Some(Rc::clone(&node));
        }

        for child in node_ref.get_children() {
            if let Some(result) = self.get_node(value, Some(Rc::clone(child))) {
                return Some(result);
            }
        }

        None
    }

    fn add(&mut self, parent: Option<NodeRef<T>>, value: T) -> bool {
        let new_node = Rc::new(RefCell::new(Node::new(value.clone())));

        if let Some(parent_node) = parent {
            parent_node.borrow_mut().add_child(Rc::clone(&new_node));
            true
        } else {
            if self.root.is_none() {
                self.root = Some(new_node);
                true
            } else {
                println!("ERROR: {:?} 重複です", value);
                false
            }
        }
    }

    fn remove(&mut self, node: Option<NodeRef<T>>) -> bool {
        if let Some(node) = node {
            if self.root.as_ref().map_or(false, |root| Rc::ptr_eq(root, &node)) {
                self.root = None;
                return true;
            }

            if let Some(parent) = node.borrow().get_parent() {
                return parent.borrow_mut().remove_child(&node);
            }
        }
        false
    }

    fn traverse(&self, node: Option<NodeRef<T>>, mode: &str) -> Vec<T> {
        let node = match node {
            Some(n) => n,
            None => {
                return match &self.root {
                    Some(root) => self.traverse(Some(Rc::clone(root)), mode),
                    None => Vec::new(),
                }
            }
        };

        let mut result = Vec::new();
        let node_ref = node.borrow();

        match mode {
            "pre-order" => {
                result.push(node_ref.get_value().clone());
                for child in node_ref.get_children() {
                    result.extend(self.traverse(Some(Rc::clone(child)), mode));
                }
            }
            "post-order" => {
                for child in node_ref.get_children() {
                    result.extend(self.traverse(Some(Rc::clone(child)), mode));
                }
                result.push(node_ref.get_value().clone());
            }
            "level-order" => {
                let mut queue = VecDeque::new();
                queue.push_back(Rc::clone(&node));
                
                while !queue.is_empty() {
                    let current = queue.pop_front().unwrap();
                    let current_ref = current.borrow();
                    result.push(current_ref.get_value().clone());
                    
                    for child in current_ref.get_children() {
                        queue.push_back(Rc::clone(child));
                    }
                }
            }
            _ => {}
        }

        result
    }

    fn is_leaf(&self, node: &NodeRef<T>) -> bool {
        node.borrow().is_leaf()
    }

    fn is_empty(&self) -> bool {
        self.root.is_none()
    }

    fn size(&self, node: Option<NodeRef<T>>) -> usize {
        let node = match node {
            Some(n) => n,
            None => {
                return match &self.root {
                    Some(root) => self.size(Some(Rc::clone(root))),
                    None => 0,
                }
            }
        };

        let node_ref = node.borrow();
        let mut count = 1;
        
        for child in node_ref.get_children() {
            count += self.size(Some(Rc::clone(child)));
        }
        
        count
    }

    fn clear(&mut self) -> bool {
        self.root = None;
        true
    }

    fn display(&self) -> Vec<T> {
        if self.root.is_none() {
            return Vec::new();
        }
        
        self.traverse(None, "level-order")
    }
}

fn main() {
    println!("Tree TEST -----> start");

    println!("\nnew");
    let mut tree_data = Tree::<String>::new();
    println!("  現在のデータ: {:?}", tree_data.display());

    println!("\nis_empty");
    let output = tree_data.is_empty();
    println!("  出力値: {}", output);

    println!("\nsize");
    let output = tree_data.size(None);
    println!("  出力値: {}", output);

    println!("\nadd");
    let input_params = (None, "Root".to_string());
    println!("  入力値: {:?}", input_params);
    let output = tree_data.add(input_params.0, input_params.1);
    println!("  出力値: {}", output);
    println!("  現在のデータ: {:?}", tree_data.display());
    
    let root_node = tree_data.get();

    println!("\nadd");
    let input_params = (root_node.clone(), "Child1".to_string());
    println!("  入力値: ({:?}, {})", "root_node", input_params.1);
    let output = tree_data.add(input_params.0, input_params.1);
    println!("  出力値: {}", output);
    println!("  現在のデータ: {:?}", tree_data.display());

    println!("\nadd");
    let input_params = (root_node.clone(), "Child2".to_string());
    println!("  入力値: ({:?}, {})", "root_node", input_params.1);
    let output = tree_data.add(input_params.0, input_params.1);
    println!("  出力値: {}", output);
    println!("  現在のデータ: {:?}", tree_data.display());

    println!("\nget_node");
    let input_value = "Child1".to_string();
    println!("  入力値: {}", input_value);
    let output = tree_data.get_node(&input_value, None);
    println!("  出力値: {:?}", output);
    println!("  現在のデータ: {:?}", tree_data.display());

    println!("\ntraverse");
    let input_mode = "pre-order";
    println!("  入力値: {}", input_mode);
    let output = tree_data.traverse(None, input_mode);
    println!("  出力値: {:?}", output);
    println!("  現在のデータ: {:?}", tree_data.display());

    println!("\nTree TEST <----- end");
}