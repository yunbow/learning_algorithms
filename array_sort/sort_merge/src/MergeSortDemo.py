# Python
# 配列の並び替え: マージソート (Merge Sort)

class ArrayData:
    def __init__(self):
        self._data = []

    def get(self):
        return self._data

    def set(self, data):
        self._data = data
        return True
    
    def _merge_sort(self, target):
        # 配列の長さが1以下の場合はそのまま返す（基本ケース）
        if len(target) <= 1:
            return target
        
        # 配列を半分に分割
        mid = len(target) // 2
        left_half = target[:mid]
        right_half = target[mid:]
        
        # 左右の半分を再帰的にソート
        left_half = self._merge_sort(left_half)
        right_half = self._merge_sort(right_half)
        
        # ソート済みの半分同士をマージ
        return self._merge(left_half, right_half)

    def _merge(self, left, right):
        result = []
        i = j = 0
        
        # 左右の配列を比較しながらマージ
        while i < len(left) and j < len(right):
            if left[i] <= right[j]:
                result.append(left[i])
                i += 1
            else:
                result.append(right[j])
                j += 1
        
        # 残った要素を追加
        result.extend(left[i:])
        result.extend(right[j:])
        
        return result

    def sort(self):
        self._merge_sort(self._data)
        return True

def main():
    print("MergeSort TEST -----> start")

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

    print("\nMergeSort TEST <----- end")

if __name__ == "__main__":
    main()