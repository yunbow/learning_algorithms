# Python
# 配列の検索: 線形探索 (Linear Search)

class ArrayData:
    def __init__(self):
        self._data = []

    def get(self):
        return self._data

    def set(self, data):
        self._data = data
        return True

    def search(self, target):
        # 配列の要素を順番に確認
        for i in range(len(self._data)):
            # 目的の値が見つかった場合、そのインデックスを返す
            if self._data[i] == target:
                return i
        
        # 見つからなかった場合は -1 を返す
        return -1

def main():
    print("LinearSearch TEST -----> start")

    print("\nnew")
    array_data = ArrayData()
    input = [1, 3, 5, 7, 9, 11, 13, 15, 17, 19]
    array_data.set(input)
    print(f"  現在のデータ: {array_data.get()}")
    
    print("\nsearch")
    input = 7
    print(f"  入力値: {input}")
    output = array_data.search(input)
    print(f"  出力値: {output}")

    print("\nsearch")
    input = 30
    print(f"  入力値: {input}")
    output = array_data.search(input)
    print(f"  出力値: {output}")

    print("\nLinearSearch TEST <----- end")

if __name__ == "__main__":
    main()