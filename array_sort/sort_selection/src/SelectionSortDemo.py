# Python
# 配列の並び替え: 選択ソート (Selection Sort)

class ArrayData:
    def __init__(self):
        self._data = []

    def get(self):
        return self._data

    def set(self, data):
        self._data = data
        return True

    def _selection_sort(self, target):
        # 配列の長さを取得
        n = len(target)
        
        # 配列を順番に走査
        for i in range(n):
            # 未ソート部分の最小値のインデックスを見つける
            min_index = i
            for j in range(i + 1, n):
                if target[j] < target[min_index]:
                    min_index = j
            
            # 見つかった最小値と現在の位置を交換
            target[i], target[min_index] = target[min_index], target[i]
        
        return target

    def sort(self):
        self._selection_sort(self._data)
        return True

def main():
    print("SelectionSort TEST -----> start")

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

    print("\nSelectionSort TEST <----- end")

if __name__ == "__main__":
    main()