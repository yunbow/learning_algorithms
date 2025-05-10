# Python
# 配列の並び替え: ヒープソート (Heap Sort)

class HeapData:
    def __init__(self, is_min_heap=True):
        self._data = []
        # 最小ヒープか最大ヒープかを設定
        self.is_min_heap = is_min_heap

    def _get_parent_idx(self, idx):
        # 親ノードのインデックスを計算
        if idx <= 0:
            return -1 # 根ノードには親がない
        return (idx - 1) // 2

    def _get_left_child_idx(self, idx):
        # 左の子ノードのインデックスを計算
        return 2 * idx + 1

    def _get_right_child_idx(self, idx):
        # 右の子ノードのインデックスを計算
        return 2 * idx + 2

    def _has_parent(self, idx):
        # 親ノードが存在するか確認
        return self._get_parent_idx(idx) >= 0 and self._get_parent_idx(idx) < len(self._data) # len check added for robustness

    def _has_left_child(self, idx):
        # 左の子ノードが存在するか確認
        return self._get_left_child_idx(idx) < len(self._data)

    def _has_right_child(self, idx):
        # 右の子ノードが存在するか確認
        return self._get_right_child_idx(idx) < len(self._data)

    def _swap(self, idx1, idx2):
        # 2つのノードの値を交換
        if 0 <= idx1 < len(self._data) and 0 <= idx2 < len(self._data):
            self._data[idx1], self._data[idx2] = self._data[idx2], self._data[idx1]
        else:
            print(f"Warning: Swap indices out of bounds: {idx1}, {idx2}") # Optional warning

    def _should_swap(self, idx1, idx2):
        # 最小ヒープでは idx1(親) が idx2(子) より大きい場合に交換
        # 最大ヒープでは idx1(親) が idx2(子) より小さい場合に交換
        if self.is_min_heap:
            return self._data[idx1] > self._data[idx2]
        else: # Max heap
            return self._data[idx1] < self._data[idx2]

    def _heapify_down(self, idx):
        # ノードを下方向に移動させてヒープ条件を満たす (is_min_heap に従う)
        smallest_or_largest = idx

        left_child_idx = self._get_left_child_idx(idx)
        right_child_idx = self._get_right_child_idx(idx)

        # 左の子ノードと比較 (存在する場合)
        if self._has_left_child(idx):
            if self.is_min_heap: # Min-heap: compare with smallest child
                 if self._data[left_child_idx] < self._data[smallest_or_largest]:
                     smallest_or_largest = left_child_idx
            else: # Max-heap: compare with largest child
                 if self._data[left_child_idx] > self._data[smallest_or_largest]:
                     smallest_or_largest = left_child_idx

        # 右の子ノードと比較 (存在する場合)
        if self._has_right_child(idx):
             if self.is_min_heap: # Min-heap: compare with smallest child
                  if self._data[right_child_idx] < self._data[smallest_or_largest]:
                      smallest_or_largest = right_child_idx
             else: # Max-heap: compare with largest child
                  if self._data[right_child_idx] > self._data[smallest_or_largest]:
                      smallest_or_largest = right_child_idx

        # インデックスが変わっていたら交換して再帰的に処理
        if smallest_or_largest != idx:
            self._swap(idx, smallest_or_largest)
            self._heapify_down(smallest_or_largest)

    def _max_heapify_down(self, idx, heap_size):
        # 指定されたヒープサイズ内で、idx から下方向に最大ヒープ条件を満たすように調整する。
        largest = idx # 最大ヒープでは、親と子の間で最大のものを探す

        left_child_idx = self._get_left_child_idx(idx)
        right_child_idx = self._get_right_child_idx(idx)

        # 左の子ノードと比較 (ヒープサイズ内に存在する場合)
        if left_child_idx < heap_size and self._data[left_child_idx] > self._data[largest]:
             largest = left_child_idx

        # 右の子ノードと比較 (ヒープサイズ内に存在する場合)
        if right_child_idx < heap_size and self._data[right_child_idx] > self._data[largest]:
             largest = right_child_idx

        # largest が現在のノード idx と異なる場合、交換して再帰的に処理
        if largest != idx:
            self._swap(idx, largest)
            self._max_heapify_down(largest, heap_size)


    def _heapify_up(self, idx):
        # ノードを上方向に移動させてヒープ条件を満たす (is_min_heap に従う)
        while self._has_parent(idx):
             parent_idx = self._get_parent_idx(idx)
             if self._should_swap(parent_idx, idx):
                 self._swap(parent_idx, idx)
                 idx = parent_idx
             else:
                 break # Heap property satisfied

    def get(self):
        # 要素を取得 (現在の internal data を返す)
        return self._data

    def heapify(self, array):
        self._data = array
        n = len(array)
        # 最後の非葉ノードから根に向かって、各部分木をヒープ化
        for i in range(n // 2 - 1, -1, -1):
             self._heapify_down(i) # Use the regular heapify_down
        return True

    def sort(self):
        n = len(self._data)

        # 配列を最大ヒープに変換する
        for i in range(n // 2 - 1, -1, -1):
            self._max_heapify_down(i, n)

        # 最大ヒープから要素を一つずつ取り出してソート済みの位置に移動
        for i in range(n - 1, 0, -1):
            # 現在の根 (最大値) をヒープの最後の要素と交換
            self._swap(0, i)

            # 交換された要素 (元の最大値) は正しい位置に置かれたので、
            # 次のヒープ調整からは除外する。ヒープサイズは i に減少。
            # 新しい根 (元のヒープの最後の要素だったもの) から
            # _max_heapify_down を使って残りの要素を最大ヒープに調整
            self._max_heapify_down(0, i)
        
        return True

def main():
    print("HeapSort TEST -----> start")

    heap_data = HeapData()

    # ランダムな整数の配列
    print(f"\nsort")
    input = [64, 34, 25, 12, 22, 11, 90]
    print(f"  ソート前: {input}")
    heap_data.heapify(input)
    heap_data.sort()
    print(f"  ソート後: {heap_data.get()}")
    
    # 既にソートされている配列
    print(f"\nsort")
    input = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    print(f"  ソート前: {input}")
    heap_data.heapify(input)
    heap_data.sort()
    print(f"  ソート後: {heap_data.get()}")
    
    # 逆順の配列
    print(f"\nsort")
    input = [10, 9, 8, 7, 6, 5, 4, 3, 2, 1]
    print(f"  ソート前: {input}")
    heap_data.heapify(input)
    heap_data.sort()
    print(f"  ソート後: {heap_data.get()}")
    
    # 重複要素を含む配列
    print(f"\nsort")
    input = [10, 9, 8, 7, 6, 10, 9, 8, 7, 6]
    print(f"  ソート前: {input}")
    heap_data.heapify(input)
    heap_data.sort()
    print(f"  ソート後: {heap_data.get()}")
    
    # 空の配列
    print(f"\nsort")
    input = []
    print(f"  ソート前: {input}")
    heap_data.heapify(input)
    heap_data.sort()
    print(f"  ソート後: {heap_data.get()}")

    print("\nHeapSort TEST <----- end")

if __name__ == "__main__":
    main()