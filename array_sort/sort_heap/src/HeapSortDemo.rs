// 配列の並び替え: ヒープソート (Heap Sort)

struct HeapData<T: Ord + Clone> {
    data: Vec<T>,
    is_min_heap: bool,
}

impl<T: Ord + Clone> HeapData<T> {
    fn new(is_min_heap: bool) -> Self {
        HeapData {
            data: Vec::new(),
            is_min_heap,
        }
    }

    fn get_parent_idx(&self, idx: usize) -> Option<usize> {
        // 親ノードのインデックスを計算
        if idx <= 0 {
            None // 根ノードには親がない
        } else {
            Some((idx - 1) / 2)
        }
    }

    fn get_left_child_idx(&self, idx: usize) -> usize {
        // 左の子ノードのインデックスを計算
        2 * idx + 1
    }

    fn get_right_child_idx(&self, idx: usize) -> usize {
        // 右の子ノードのインデックスを計算
        2 * idx + 2
    }

    fn has_parent(&self, idx: usize) -> bool {
        // 親ノードが存在するか確認
        if let Some(parent_idx) = self.get_parent_idx(idx) {
            parent_idx < self.data.len()
        } else {
            false
        }
    }

    fn has_left_child(&self, idx: usize) -> bool {
        // 左の子ノードが存在するか確認
        self.get_left_child_idx(idx) < self.data.len()
    }

    fn has_right_child(&self, idx: usize) -> bool {
        // 右の子ノードが存在するか確認
        self.get_right_child_idx(idx) < self.data.len()
    }

    fn swap(&mut self, idx1: usize, idx2: usize) {
        // 2つのノードの値を交換
        if idx1 < self.data.len() && idx2 < self.data.len() {
            self.data.swap(idx1, idx2);
        } else {
            println!("Warning: Swap indices out of bounds: {}, {}", idx1, idx2);
        }
    }

    fn should_swap(&self, idx1: usize, idx2: usize) -> bool {
        // 最小ヒープでは idx1(親) が idx2(子) より大きい場合に交換
        // 最大ヒープでは idx1(親) が idx2(子) より小さい場合に交換
        if self.is_min_heap {
            self.data[idx1] > self.data[idx2]
        } else {
            self.data[idx1] < self.data[idx2]
        }
    }

    fn heapify_down(&mut self, idx: usize) {
        // ノードを下方向に移動させてヒープ条件を満たす (is_min_heap に従う)
        let mut smallest_or_largest = idx;

        let left_child_idx = self.get_left_child_idx(idx);
        let right_child_idx = self.get_right_child_idx(idx);

        // 左の子ノードと比較 (存在する場合)
        if self.has_left_child(idx) {
            if self.is_min_heap {
                // Min-heap: compare with smallest child
                if self.data[left_child_idx] < self.data[smallest_or_largest] {
                    smallest_or_largest = left_child_idx;
                }
            } else {
                // Max-heap: compare with largest child
                if self.data[left_child_idx] > self.data[smallest_or_largest] {
                    smallest_or_largest = left_child_idx;
                }
            }
        }

        // 右の子ノードと比較 (存在する場合)
        if self.has_right_child(idx) {
            if self.is_min_heap {
                // Min-heap: compare with smallest child
                if self.data[right_child_idx] < self.data[smallest_or_largest] {
                    smallest_or_largest = right_child_idx;
                }
            } else {
                // Max-heap: compare with largest child
                if self.data[right_child_idx] > self.data[smallest_or_largest] {
                    smallest_or_largest = right_child_idx;
                }
            }
        }

        // インデックスが変わっていたら交換して再帰的に処理
        if smallest_or_largest != idx {
            self.swap(idx, smallest_or_largest);
            self.heapify_down(smallest_or_largest);
        }
    }

    fn max_heapify_down(&mut self, idx: usize, heap_size: usize) {
        // 指定されたヒープサイズ内で、idx から下方向に最大ヒープ条件を満たすように調整する。
        let mut largest = idx; // 最大ヒープでは、親と子の間で最大のものを探す

        let left_child_idx = self.get_left_child_idx(idx);
        let right_child_idx = self.get_right_child_idx(idx);

        // 左の子ノードと比較 (ヒープサイズ内に存在する場合)
        if left_child_idx < heap_size && self.data[left_child_idx] > self.data[largest] {
            largest = left_child_idx;
        }

        // 右の子ノードと比較 (ヒープサイズ内に存在する場合)
        if right_child_idx < heap_size && self.data[right_child_idx] > self.data[largest] {
            largest = right_child_idx;
        }

        // largest が現在のノード idx と異なる場合、交換して再帰的に処理
        if largest != idx {
            self.swap(idx, largest);
            self.max_heapify_down(largest, heap_size);
        }
    }

    fn heapify_up(&mut self, idx: usize) {
        // ノードを上方向に移動させてヒープ条件を満たす (is_min_heap に従う)
        let mut current_idx = idx;
        while self.has_parent(current_idx) {
            let parent_idx = self.get_parent_idx(current_idx).unwrap();
            if self.should_swap(parent_idx, current_idx) {
                self.swap(parent_idx, current_idx);
                current_idx = parent_idx;
            } else {
                break; // Heap property satisfied
            }
        }
    }

    fn get(&self) -> &Vec<T> {
        // 要素を取得 (現在の internal data を返す)
        &self.data
    }

    fn heapify(&mut self, array: Vec<T>) -> bool {
        self.data = array;
        let n = self.data.len();
        // 最後の非葉ノードから根に向かって、各部分木をヒープ化
        if n > 0 {
            for i in (0..=(n / 2 - 1)).rev() {
                self.heapify_down(i); // Use the regular heapify_down
            }
        }
        true
    }

    fn sort(&mut self) -> bool {
        let n = self.data.len();
        if n <= 1 {
            return true;
        }

        // 配列を最大ヒープに変換する
        for i in (0..=(n / 2 - 1)).rev() {
            self.max_heapify_down(i, n);
        }

        // 最大ヒープから要素を一つずつ取り出してソート済みの位置に移動
        for i in (1..n).rev() {
            // 現在の根 (最大値) をヒープの最後の要素と交換
            self.swap(0, i);

            // 交換された要素 (元の最大値) は正しい位置に置かれたので、
            // 次のヒープ調整からは除外する。ヒープサイズは i に減少。
            // 新しい根 (元のヒープの最後の要素だったもの) から
            // max_heapify_down を使って残りの要素を最大ヒープに調整
            self.max_heapify_down(0, i);
        }

        true
    }
}

fn main() {
    println!("HeapSort TEST -----> start");

    let mut heap_data = HeapData::new(true);

    // ランダムな整数の配列
    println!("\nsort");
    let input = vec![64, 34, 25, 12, 22, 11, 90];
    println!("  ソート前: {:?}", input);
    heap_data.heapify(input);
    heap_data.sort();
    println!("  ソート後: {:?}", heap_data.get());

    // 既にソートされている配列
    println!("\nsort");
    let input = vec![1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
    println!("  ソート前: {:?}", input);
    heap_data.heapify(input);
    heap_data.sort();
    println!("  ソート後: {:?}", heap_data.get());

    // 逆順の配列
    println!("\nsort");
    let input = vec![10, 9, 8, 7, 6, 5, 4, 3, 2, 1];
    println!("  ソート前: {:?}", input);
    heap_data.heapify(input);
    heap_data.sort();
    println!("  ソート後: {:?}", heap_data.get());

    // 重複要素を含む配列
    println!("\nsort");
    let input = vec![10, 9, 8, 7, 6, 10, 9, 8, 7, 6];
    println!("  ソート前: {:?}", input);
    heap_data.heapify(input);
    heap_data.sort();
    println!("  ソート後: {:?}", heap_data.get());

    // 空の配列
    println!("\nsort");
    let input: Vec<i32> = vec![];
    println!("  ソート前: {:?}", input);
    heap_data.heapify(input);
    heap_data.sort();
    println!("  ソート後: {:?}", heap_data.get());

    println!("\nHeapSort TEST <----- end");
}
