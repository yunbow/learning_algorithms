# Python
# 配列の並び替え: 挿入ソート (Insertion Sort)

class ArrayData:
    def __init__(self):
        self._data = []

    def get(self):
        return self._data

    def set(self, data):
        self._data = data
        return True

    def sort(self):
        # 配列の長さを取得
        n = len(self._data)
        
        # 2番目の要素から始める（最初の要素は既にソート済みと見なす）
        for i in range(1, n):
            # 現在の要素を取得
            key = self._data[i]
            
            # ソート済み部分の最後の要素のインデックス
            j = i - 1
            
            # keyより大きい要素をすべて右にシフト
            while j >= 0 and self._data[j] > key:
                self._data[j + 1] = self._data[j]
                j -= 1
                
            # 適切な位置にkeyを挿入
            self._data[j + 1] = key
        
        return True

def main():
    print("InsertionSort TEST -----> start")

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

    print("\nInsertionSort TEST <----- end")

if __name__ == "__main__":
    main()