# Python
# 配列の並び替え: バブルソート (Bubble Sort)

class ArrayData:
    def __init__(self):
        self._data = []

    def get(self):
        return self._data

    def set(self, data):
        self._data = data
        return True

    def sort(self):
        n = len(self._data)
        
        # 外側のループ: n-1回の走査が必要
        for i in range(n):
            # 最適化: 一度の走査で交換がなければソート完了
            swapped = False
            
            # 内側のループ: まだソートされていない部分を走査
            # 各走査後に最大の要素が末尾に移動するため、i回分を除外
            for j in range(0, n-i-1):
                # 隣接する要素を比較し、必要に応じて交換
                if self._data[j] > self._data[j+1]:
                    self._data[j], self._data[j+1] = self._data[j+1], self._data[j]
                    swapped = True
            
            # 交換が発生しなければソート完了
            if not swapped:
                break
        return True

def main():
    print("BubbleSort TEST -----> start")

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

    print("\nBubbleSort TEST <----- end")

if __name__ == "__main__":
    main()