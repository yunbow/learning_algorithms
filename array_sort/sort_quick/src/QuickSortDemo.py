# Python
# 配列の並び替え: クイックソート (Quick Sort)

class ArrayData:
    def __init__(self):
        self._data = []

    def get(self):
        return self._data

    def set(self, data):
        self._data = data
        return True

    def _quick_sort(self, target):
        # 空の配列または要素が1つの場合はそのまま返す（基底条件）
        if len(target) <= 1:
            return target
        
        # ピボットを選択（この実装では最後の要素を選択）
        pivot = target[-1]
        
        # ピボットより小さい要素と大きい要素に分ける
        left = []
        right = []
        
        # 最後の要素（ピボット）を除いて配列をスキャン
        for i in range(len(target) - 1):
            if target[i] <= pivot:
                left.append(target[i])
            else:
                right.append(target[i])
        
        # 左側の部分配列、ピボット、右側の部分配列を再帰的にソートして結合
        return self._quick_sort(left) + [pivot] + self._quick_sort(right)

    def sort(self):
        self._quick_sort(self._data)
        return True

def main():
    print("QuickSort TEST -----> start")

    array_data = ArrayData()

    # ランダムな整数の配列
    print(f"\nsort")
    input = [64, 34, 25, 12, 22, 11, 90]
    print(f"  ソート前: {input}")
    array_data.set(input)
    array_data.sort()
    print(f"  ソート後: {array_data.get()}")
    
    # 既にソートされている配列
    print(f"\nsort")
    input = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    print(f"  ソート前: {input}")
    array_data.set(input)
    array_data.sort()
    print(f"  ソート後: {array_data.get()}")
    
    # 逆順の配列
    print(f"\nsort")
    input = [10, 9, 8, 7, 6, 5, 4, 3, 2, 1]
    print(f"  ソート前: {input}")
    array_data.set(input)
    array_data.sort()
    print(f"  ソート後: {array_data.get()}")
    
    # 重複要素を含む配列
    print(f"\nsort")
    input = [10, 9, 8, 7, 6, 10, 9, 8, 7, 6]
    print(f"  ソート前: {input}")
    array_data.set(input)
    array_data.sort()
    print(f"  ソート後: {array_data.get()}")
    
    # 空の配列
    print(f"\nsort")
    input = []
    print(f"  ソート前: {input}")
    array_data.set(input)
    array_data.sort()
    print(f"  ソート後: {array_data.get()}")

    print("\nQuickSort TEST <----- end")

if __name__ == "__main__":
    main()