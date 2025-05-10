# Python
# 配列の検索: ハッシュ探索 (Hash Search)

class ArrayData:
    def __init__(self):
        self._data = []

    def get(self):
        return self._data

    def set(self, data):
        self._data = data
        return True

    def search(self, target):
        # ハッシュテーブルの作成
        hash_table = {}
        
        # 配列の要素をハッシュテーブルに格納
        # キーを要素の値、値をインデックスとする
        for i, value in enumerate(self._data):
            hash_table[value] = i
        
        # ハッシュテーブルを使って検索
        if target in hash_table:
            return hash_table[target]
        else:
            return -1

def main():
    print("HashSearch TEST -----> start")

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

    print("\nHashSearch TEST <----- end")

if __name__ == "__main__":
    main()
