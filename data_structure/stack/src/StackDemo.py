# Python
# データ構造: スタック (Stack)

class StackData:
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
    
    def push(self, item):
        # スタックの一番上に要素を追加
        self._data.append(item)
        return True
    
    def pop(self):
        # スタックが空でなければ、一番上の要素を取り出して返す
        if not self.is_empty():
            self._data.pop()
            return True
        else:
            print(f"ERROR: 空です")
            return False

    def peek(self):
        # スタックが空でなければ、一番上の要素を参照して返す
        if not self.is_empty():
            return self._data[-1]
        else:
            return None
    
    def is_empty(self):
        # スタックが空かどうか
        return len(self._data) == 0
    
    def size(self):
        # スタックのサイズ（要素数）を返す
        return len(self._data)
    
    def clear(self):
        # スタックの全要素を削除する
        self._data = []
        return True

def main():
    print("Stack TEST -----> start")

    print("\nnew")
    stack_data = StackData()
    print(f"  現在のデータ: {stack_data.get()}")

    print("\nis_empty")
    output = stack_data.is_empty()
    print(f"  出力値: {output}")

    print("\nsize")
    output = stack_data.size()
    print(f"  出力値: {output}")

    print("\npush")
    items_to_push = [10, 20, 30, 40]
    for item in items_to_push:
        print(f"  入力値: {item}")
        output = stack_data.push(item)
        print(f"  出力値: {output}")
        print(f"  現在のデータ: {stack_data.get()}")

    print("\nsize")
    output = stack_data.size()
    print(f"  出力値: {output}")

    print("\nis_empty")
    output = stack_data.is_empty()
    print(f"  出力値: {output}")

    print("\npeek")
    output = stack_data.peek()
    print(f"  出力値: {output}")

    print("\nget_index")
    input = 30
    print(f"  入力値: {input}")
    output = stack_data.get_index(input)
    print(f"  出力値: {output}")

    print("\nget_index")
    input = 50
    print(f"  入力値: {input}")
    output = stack_data.get_index(input)
    print(f"  出力値: {output}")

    print("\npop")
    while not stack_data.is_empty():
        output = stack_data.pop()
        print(f"  出力値: {output}")
        print(f"  現在のデータ: {stack_data.get()}")

    print("\nis_empty")
    output = stack_data.is_empty()
    print(f"  出力値: {output}")

    print("\nsize")
    output = stack_data.size()
    print(f"  出力値: {output}")

    print("\npop")
    output = stack_data.pop()
    print(f"  出力値: {output}")

    print("\npeek")
    output = stack_data.peek()
    print(f"  出力値: {output}")

    print("\nStack TEST <----- end")

if __name__ == "__main__":
    main()