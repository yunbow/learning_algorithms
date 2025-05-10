# Python
# データ構造: セット (Set)

class SetData:
    def __init__(self):
        self._data = []

    def get(self):
        # 要素を取得
        return self._data

    def get_index(self, item):
        # 指定された要素がセット内に存在するかどうかをチェックする。
        try:
            index = self._data.index(item)
            return index
        except ValueError:
            print(f"ERROR: {item} は範囲外です")
            return -1

    def get_value(self, index):
        # 指定されたインデックスの要素を取得する。
        if 0 <= index < len(self._data):
            return self._data[index]
        else:
            print(f"ERROR: {index} は範囲外です")
            return None

    def add(self, item):
        # 要素をセットに追加する。
        if item not in self._data:
            self._data.append(item)
            return True
        else:
            print(f"ERROR: {item} は重複です")
            return False
    
    def remove(self, item):
        # 指定された要素をセットから削除する。
        if item in self._data:
            self._data.remove(item)
            return True
        else:
            print(f"ERROR: {item} は範囲外です")
            return False
    
    def is_empty(self):
        # 空かどうかをチェックする
        return len(self._data) == 0
    
    def size(self):
        # 要素数を返す
        return len(self._data)
    
    def clear(self):
        # 空にする
        self._data.clear()
        return True

def main():
    print("Set TEST -----> start")

    print("\nnew")
    set_data = SetData()
    print(f"  現在のデータ: {set_data.get()}")

    print("\nadd")
    input = [10, 20, 30, 20, 40]
    for item in input:
        print(f"  入力値: {item}")
        output = set_data.add(item)
        print(f"  出力値: {output}")
        print(f"  現在のデータ: {set_data.get()}")

    print("\nsize")
    output = set_data.size()
    print(f"  出力値: {output}")

    print("\nis_empty")
    output = set_data.is_empty()
    print(f"  出力値: {output}")

    print("\nget_value")
    input = [0, 2, 5]
    for index in input:
        print(f"  入力値: {index}")
        output = set_data.get_value(index)
        print(f"  出力値: {output}")

    print("\nget_index")
    input = [30, 99]
    for item in input:
        print(f"  入力値: {item}")
        output = set_data.get_index(item)
        print(f"  出力値: {output}")

    print("\nremove")
    input = [20, 50, 10]
    for item in input:
        print(f"  入力値: {item}")
        output = set_data.remove(item)
        print(f"  出力値: {output}")
        print(f"  現在のデータ: {set_data.get()}")
    
    print("\nsize")
    output = set_data.size()
    print(f"  出力値: {output}")

    print("\nclear")
    output = set_data.clear()
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {set_data.get()}")

    print("\nis_empty")
    output = set_data.is_empty()
    print(f"  出力値: {output}")

    print("\nSet TEST <----- end")

if __name__ == "__main__":
    main()