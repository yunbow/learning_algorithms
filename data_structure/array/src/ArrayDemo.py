# Python
# データ構造: 配列 (Array)

class ArrayData:
    def __init__(self):
        self._data = []
    
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

    def add(self, item):
        # 配列の末尾に要素を追加する
        self._data.append(item)
        return True
    
    def remove(self, index):
        # 指定されたインデックスの要素を削除する
        if 0 <= index < len(self._data):
            self._data.pop(index)
            return True
        else:
            print(f"ERROR: {index} は範囲外です")
            return False
    
    def update(self, index, new_value):
        # 指定されたインデックスの要素を新しい値に更新する
        if 0 <= index < len(self._data):
            self._data[index] = new_value
            return True
        else:
            print(f"ERROR: {index} は範囲外です")
            return False

    def reverse(self):
        # 配列の要素を逆順にする
        self._data.reverse()
        return self._data
    
    def sort(self, descending=False):
        # 配列の要素をソートする
        self._data.sort(reverse=descending)
        return self._data
        
    def is_empty(self):
        # 配列が空かどうか
        return len(self._data) == 0
    
    def size(self):
        # 配列のサイズ（要素数）を返す
        return len(self._data)
    
    def clear(self):
        # 配列の全要素を削除する
        self._data = []
        return True

def main():
    print("Array TEST -----> start")

    print("\nnew")
    array_data = ArrayData()
    print(f"  現在のデータ: {array_data.get()}")

    print("\nadd")
    input = [10, 20, 30, 10, 40]
    for item in input:
        print(f"  入力値: {item}")
        output = array_data.add(item)
        print(f"  出力値: {output}")
        print(f"  現在のデータ: {array_data.get()}")

    print("\nsize")
    output = array_data.size()
    print(f"  出力値: {output}")

    print("\nis_empty")
    output = array_data.is_empty()
    print(f"  出力値: {output}")

    print("\nget_value")
    input = 2
    print(f"  入力値: {input}")
    output = array_data.get_value(input)
    print(f"  出力値: {output}")

    print("\nget_value")
    input = 10
    print(f"  入力値: {input}")
    output = array_data.get_value(input)
    print(f"  出力値: {output}")

    print("\nupdate")
    input = (1, 25)
    print(f"  入力値: {input}")
    output = array_data.update(*input)
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {array_data.get()}")

    print("\nupdate")
    input = (15, 25)
    print(f"  入力値: {input}")
    output = array_data.update(*input)
    print(f"  出力値: {output}") 
    print(f"  現在のデータ: {array_data.get()}") 

    print("\nget_index")
    input = 10
    print(f"  入力値: {input}")
    output = array_data.get_index(input)
    print(f"  出力値: {output}")

    print("\nget_index")
    input = 99
    print(f"  入力値: {input}")
    output = array_data.get_index(input)
    print(f"  出力値: {output}")

    print("\nremove")
    input = 3
    print(f"  入力値: {input}")
    output = array_data.remove(input)
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {array_data.get()}")

    print("\nremove")
    input = 8
    print(f"  入力値: {input}")
    output = array_data.remove(input)
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {array_data.get()}")

    print("\nreverse")
    output = array_data.reverse()
    print(f"  出力値: {output}")

    print("\nsort")
    print("  入力値: descending=False")
    output = array_data.sort(descending=False)
    print(f"  出力値: {output}")

    print("\nsort")
    print("  入力値: descending=True")
    output = array_data.sort(descending=True)
    print(f"  出力値: {output}")

    print("\nclear")
    output = array_data.clear()
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {array_data.get()}")

    print("\nis_empty")
    output = array_data.is_empty()
    print(f"  出力値: {output}")

    print("\nArray TEST <----- end")

if __name__ == "__main__":
    main()