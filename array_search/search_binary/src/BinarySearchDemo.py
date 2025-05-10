# Python
# 配列の検索: 二分探索 (Binary Search)

class ArrayData:
    def __init__(self):
        self._data = []

    def get(self):
        return self._data

    def set(self, data):
        self._data = data
        return True

    def search(self, target):
        left = 0
        right = len(self._data) - 1
        
        while left <= right:
            mid = (left + right) // 2
            
            # 中央の要素が目標値と一致
            if self._data[mid] == target:
                return mid
            
            # 中央の要素が目標値より小さい場合、右半分を探索
            elif self._data[mid] < target:
                left = mid + 1
            
            # 中央の要素が目標値より大きい場合、左半分を探索
            else:
                right = mid - 1
        
        # 目標値が見つからない場合
        return -1

def main():
    print("BinarySearch TEST -----> start")

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

    print("\nBinarySearch TEST <----- end")

if __name__ == "__main__":
    main()