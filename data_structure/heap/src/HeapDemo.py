# Python
# データ構造: ヒープ (Heap)

class HeapData:
    def __init__(self, is_min_heap=True):
        self._data = []
        # 最小ヒープか最大ヒープかを設定
        self.is_min_heap = is_min_heap
    
    def _get_parent_idx(self, idx):
        # 親ノードのインデックスを計算
        return (idx - 1) // 2
    
    def _get_left_child_idx(self, idx):
        # 左の子ノードのインデックスを計算
        return 2 * idx + 1
    
    def _get_right_child_idx(self, idx):
        # 右の子ノードのインデックスを計算
        return 2 * idx + 2
    
    def _has_parent(self, idx):
        # 親ノードが存在するか確認
        return self._get_parent_idx(idx) >= 0
    
    def _has_left_child(self, idx):
        # 左の子ノードが存在するか確認
        return self._get_left_child_idx(idx) < len(self._data)
    
    def _has_right_child(self, idx):
        # 右の子ノードが存在するか確認
        return self._get_right_child_idx(idx) < len(self._data)
    
    def _get_parent(self, idx):
        # 親ノードの値を取得
        return self._data[self._get_parent_idx(idx)]
    
    def _get_left_child(self, idx):
        # 左の子ノードの値を取得
        return self._data[self._get_left_child_idx(idx)]
    
    def _get_right_child(self, idx):
        # 右の子ノードの値を取得
        return self._data[self._get_right_child_idx(idx)]
    
    def _swap(self, idx1, idx2):
        # 2つのノードの値を交換
        self._data[idx1], self._data[idx2] = self._data[idx2], self._data[idx1]
    
    def _should_swap(self, idx1, idx2):
        # 最小ヒープでは親が子より大きい場合に交換
        # 最大ヒープでは親が子より小さい場合に交換
        if self.is_min_heap:
            return self._data[idx1] > self._data[idx2]
        else:
            return self._data[idx1] < self._data[idx2]
    
    def _heapify_down(self, idx):
        # ノードを下方向に移動させてヒープ条件を満たす
        smallest_or_largest = idx
        
        # 左の子ノードと比較
        if self._has_left_child(idx) and self._should_swap(smallest_or_largest, self._get_left_child_idx(idx)):
            smallest_or_largest = self._get_left_child_idx(idx)
        
        # 右の子ノードと比較
        if self._has_right_child(idx) and self._should_swap(smallest_or_largest, self._get_right_child_idx(idx)):
            smallest_or_largest = self._get_right_child_idx(idx)
        
        # インデックスが変わっていたら交換して再帰的に処理
        if smallest_or_largest != idx:
            self._swap(idx, smallest_or_largest)
            self._heapify_down(smallest_or_largest)
    
    def _heapify_up(self, idx):
        # ノードを上方向に移動させてヒープ条件を満たす
        # 親がある限り、親と比較して必要なら交換
        while self._has_parent(idx) and self._should_swap(self._get_parent_idx(idx), idx):
            parent_idx = self._get_parent_idx(idx)
            self._swap(parent_idx, idx)
            idx = parent_idx
    
    def get(self):
        # 要素を取得
        return self._data

    def get_index(self, item):
        # 配列内で指定された値を検索し、最初に見つかったインデックスを返す
        try:
            index = self._data.index(item)
            return index
        except ValueError:
            print(f"ERROR: {item} は範囲外です")
            return -1

    def get_value(self, index):
        # 指定されたインデックスの要素を取得する
        if 0 <= index < len(self._data):
            return self._data[index]
        else:
            print(f"ERROR: {index} は範囲外です")
            return None

    def heapify(self, array):
        # 配列をヒープに変換
        self._data = array.copy()
        # 最後の親ノードから根に向かって、各部分木をヒープ化
        for i in range(len(self._data) // 2 - 1, -1, -1):
            self._heapify_down(i)
        return True

    def push(self, value):
        # ヒープに要素を追加
        self._data.append(value)
        # 最後の要素を適切な位置に移動
        self._heapify_up(len(self._data) - 1)
        return True
    
    def pop(self):
        # ヒープが空の場合
        if len(self._data) == 0:
            return False
        
        # 最後の要素をルートに移動
        last_element = self._data.pop()
        
        if len(self._data) > 0:
            self._data[0] = last_element
            # ルートから下方向にヒープ条件を満たすように調整
            self._heapify_down(0)
        
        return True
    
    def peek(self):
        # ヒープが空の場合
        if len(self._data) == 0:
            return None
        # ルート要素を返す（取り出さない）
        return self._data[0]

    def is_empty(self):
        # ヒープが空かどうかを確認
        return len(self._data) == 0
    
    def size(self):
        # ヒープのサイズを返す
        return len(self._data)
    
    def clear(self):
        # ヒープをクリア
        self._data = []
        return True

def main():
    print("Heap TEST -----> start")

    print("\nmin heap: new")
    min_heap = HeapData(is_min_heap=True)
    print(f"  現在のデータ: {min_heap.get()}")

    print(f"\nmin heap: heapify")
    input = [4, 10, 3, 5, 1]
    print(f"  入力値: {input}")
    output = min_heap.heapify(input)
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {min_heap.get()}")

    print(f"\nmin heap: push")
    input = 2
    print(f"  入力値: {input}")
    output = min_heap.push(input)
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {min_heap.get()}")

    print(f"\nmin heap: push")
    input = 15
    print(f"  入力値: {input}")
    output = min_heap.push(input)
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {min_heap.get()}")

    print(f"\nmin heap: peek")
    output = min_heap.peek()
    print(f"  出力値: {output}")

    print(f"\nmin heap: pop")
    output = min_heap.pop()
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {min_heap.get()}")

    print(f"\nmin heap: pop")
    output = min_heap.pop()
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {min_heap.get()}")

    print(f"\nmin heap: get_index")
    input = 3
    print(f"  入力値: {input}")
    output = min_heap.get_index(input)
    print(f"  出力値: {output}")

    print(f"\nmin heap: get_index")
    input = 100
    print(f"  入力値: {input}")
    output = min_heap.get_index(input)
    print(f"  出力値: {output}")

    print(f"\nmin heap: is_empty")
    output = min_heap.is_empty()
    print(f"  出力値: {output}")

    print(f"\nmin heap: size")
    output = min_heap.size()
    print(f"  出力値: {output}")

    print(f"\nmin heap: clear")
    output = min_heap.clear()
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {min_heap.get()}")

    print(f"\nmin heap: is_empty")
    output = min_heap.is_empty()
    print(f"  出力値: {output}")

    print("\nmax heap: new")
    max_heap = HeapData(is_min_heap=False)
    print(f"  現在のデータ: {max_heap.get()}")

    print(f"\nmax heap: heapify")
    input = [4, 10, 3, 5, 1]
    print(f"  入力値: {input}")
    output = max_heap.heapify(input)
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {max_heap.get()}")

    print(f"\nmax heap: push")
    input = 12
    print(f"  入力値: {input}")
    output = max_heap.push(input)
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {max_heap.get()}")

    print(f"\nmax heap: push")
    input = 0
    print(f"  入力値: {input}")
    output = max_heap.push(input)
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {max_heap.get()}")

    print(f"\nmax heap: peek")
    output = max_heap.peek()
    print(f"  出力値: {output}")

    print(f"\nmax heap: pop")
    output = max_heap.pop()
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {max_heap.get()}")

    print(f"\nmax heap: pop")
    output = max_heap.pop()
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {max_heap.get()}")

    print(f"\nmax heap: get_index")
    input = 5
    print(f"  入力値: {input}")
    output = max_heap.get_index(input)
    print(f"  出力値: {output}")

    print(f"\nmax heap: get_index")
    input = -10
    print(f"  入力値: {input}")
    output = max_heap.get_index(input)
    print(f"  出力値: {output}")

    print(f"\nmax heap: is_empty")
    output = max_heap.is_empty()
    print(f"  出力値: {output}")

    print(f"\nmax heap: size")
    output = max_heap.size()
    print(f"  出力値: {output}")

    print(f"\nmax heap: clear")
    output = max_heap.clear()
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {max_heap.get()}")

    print(f"\nmax heap: is_empty")
    output = max_heap.is_empty()
    print(f"  出力値: {output}")

    print("\nHeap TEST <----- end")

if __name__ == "__main__":
    main()