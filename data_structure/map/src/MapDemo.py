# Python
# データ構造: マップ (Map)

class MapData:
    def __init__(self):
        self._data = {}

    def get(self):
        # すべてのキーと値のペアのリストを返す
        return list(self._data.items())
    
    def get_keys(self):
        # すべてのキーのリストを返す
        return list(self._data.keys())
    
    def get_values(self):
        # すべての値のリストを返す
        return list(self._data.values())
    
    def get_key(self, value):
        # 指定された値に対応するキーを見つける（最初に見つかったものを返す）
        for k, v in self._data.items():
            if v == value:
                return k
        print(f"ERROR: {value} は範囲外です")
        return None  # 値が見つからない場合
    
    def get_value(self, key):
        # 指定された値に対応する値を見つける（最初に見つかったものを返す）
        for k, v in self._data.items():
            if k == key:
                return v
        print(f"ERROR: {key} は範囲外です")
        return None  # 値が見つからない場合

    def add(self, key, value):
        # キーと値のペアを追加する
        if key in self._data:
            print(f"ERROR: {key} は重複です")
            return False  # キーが既に存在する場合
        else:
            self._data[key] = value
            return True
    
    def remove(self, key):
        # キーと対応する値を削除する
        if key in self._data:
            del self._data[key]
            return True
        else:
            print(f"ERROR: {key} は範囲外です")
            return False  # キーが存在しない場合
    
    def update(self, key, value):
        # 既存のキーの値を更新する
        if key in self._data:
            self._data[key] = value
            return True
        else:
            print(f"ERROR: {key} は範囲外です")
            return False  # キーが存在しない場合
    
    def is_empty(self):
        # マップが空かどうかを返す
        return len(self._data) == 0
    
    def size(self):
        # マップのサイズ（キーと値のペアの数）を返す
        return len(self._data)
    
    def clear(self):
        # マップをクリアする
        self._data.clear()
        return True

def main():
    print("Map TEST -----> start")

    print("\nnew")
    map_data = MapData()
    print(f"  現在のデータ: {map_data.get()}")

    print("\nis_empty")
    output = map_data.is_empty()
    print(f"  出力値: {output}")

    print("\nsize")
    output = map_data.size()
    print(f"  出力値: {output}")

    print("\nadd")
    input = ('apple', 100)
    print(f"  入力値: {input}")
    output = map_data.add(*input)
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {map_data.get()}")

    print("\nadd")
    input = ('banana', 150)
    print(f"  入力値: {input}")
    output = map_data.add(*input)
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {map_data.get()}")

    print("\nadd")
    input = ('apple', 200)
    print(f"  入力値: {input}")
    output = map_data.add(*input)
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {map_data.get()}")

    print("\nsize")
    output = map_data.size()
    print(f"  出力値: {output}")

    print("\nget")
    input = 'apple'
    print(f"  入力値: {input}")
    output = map_data.get_value(input)
    print(f"  出力値: {output}")

    print("\nget")
    input = 'orange'
    print(f"  入力値: {input}")
    output = map_data.get_value(input)
    print(f"  出力値: {output}")

    print("\nupdate")
    input = ('banana', 180)
    print(f"  入力値: {input}")
    output = map_data.update(*input)
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {map_data.get()}")

    print("\nupdate")
    input = ('orange', 250)
    print(f"  入力値: {input}")
    output = map_data.update(*input)
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {map_data.get()}")

    print("\nget")
    input = 'banana'
    output = map_data.get_value(input)
    print(f"  出力値: {output}")

    print("\nget_keys")
    output = map_data.get_keys()
    print(f"  出力値: {output}")

    print("\nvalues")
    output = map_data.get_values()
    print(f"  出力値: {output}")

    print("\nget_key")
    input = 180
    print(f"  入力値: {input}")
    output = map_data.get_key(input)
    print(f"  出力値: {output}")

    print("\nget_key")
    input = 500
    print(f"  入力値: {input}")
    output = map_data.get_key(input)
    print(f"  出力値: {output}")

    print("\nremove")
    input = 'apple'
    print(f"  入力値: {input}")
    output = map_data.remove(input)
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {map_data.get()}")

    print("\nremove")
    input = 'orange'
    print(f"  入力値: {input}")
    output = map_data.remove(input)
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {map_data.get()}")

    print("\nsize")
    output = map_data.size()
    print(f"  出力値: {output}")

    print("\nget_keys")
    output = map_data.get_keys()
    print(f"  出力値: {output}") 

    print("\nclear")
    output = map_data.clear()
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {map_data.get()}")

    print("\nsize")
    output = map_data.size()
    print(f"  出力値: {output}")

    print("\nis_empty")
    output = map_data.is_empty()
    print(f"  出力値: {output}")

    print("\nMap TEST <----- end")

if __name__ == "__main__":
    main()