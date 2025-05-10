# Python
# データ構造: キュー (Queue)

class QueueData:
    def __init__(self):
        self._data = []
    
    def get(self):
        # 要素を取得
        return self._data

    def get_index(self, item):
        # キュー内に指定した要素があるか検索
        try:
            index = self._data.index(item)
            return index
        except ValueError:
            print(f"ERROR: {item} は範囲外です")
            return -1

    def get_value(self, index):
        # 指定したインデックスの要素を取得
        if 0 <= index < len(self._data):
            return self._data[index]
        else:
            print(f"Error: インデックス {index} は範囲外です")
            return None

    def enqueue(self, item):
        # キューの末尾に要素を追加
        self._data.append(item)
        return True
    
    def dequeue(self):
        # キューが空でない場合、先頭要素を取り出す
        if not self.is_empty():
            self._data.pop(0)
            return True
        else:
            print("ERROR: キューが空です")
            return False
    
    def peek(self):
        # キューが空でない場合、先頭要素を参照
        if not self.is_empty():
            return self._data[0]
        else:
            print("ERROR: キューが空です")
            return None
    
    def is_empty(self):
        # キューが空かどうかを確認
        return len(self._data) == 0
    
    def size(self):
        # キューの要素数を返す
        return len(self._data)
    
    def clear(self):
        # キューをクリア
        self._data = []
        return True

def main():
    print("Queue TEST -----> start")

    print("\nnew")
    queue_data = QueueData()
    print(f"  現在のデータ: {queue_data.get()}")

    print("\nis_empty")
    output = queue_data.is_empty()
    print(f"  出力値: {output}")

    print("\nenqueue")
    input = [10, 20, 30]
    for item in input:
        print(f"  入力値: {item}")
        output = queue_data.enqueue(item)
        print(f"  出力値: {output}")
        print(f"  現在のデータ: {queue_data.get()}")

    print("\nsize")
    output = queue_data.size()
    print(f"  出力値: {output}")

    print("\npeek")
    output = queue_data.peek()
    print(f"  出力値: {output}")

    print("\nget_index")
    input = 20
    print(f"  入力値: {input}")
    output = queue_data.get_index(input)
    print(f"  出力値: {output}")

    print("\nget_index")
    input = 50
    print(f"  入力値: {input}")
    output = queue_data.get_index(input)
    print(f"  出力値: {output}")

    print("\ndequeue")
    output = queue_data.dequeue()
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {queue_data.get()}")

    print("\ndequeue")
    output = queue_data.dequeue()
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {queue_data.get()}")

    print("\nsize")
    output = queue_data.size()
    print(f"  出力値: {output}")

    print("\ndequeue")
    output = queue_data.dequeue()
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {queue_data.get()}")

    print("\ndequeue")
    output = queue_data.dequeue()
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {queue_data.get()}")

    print("\nis_empty")
    output = queue_data.is_empty()
    print(f"  出力値: {output}")

    print("\nclear")
    output = queue_data.clear()
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {queue_data.get()}")

    print("\nsize")
    output = queue_data.size()
    print(f"  出力値: {output}")

    print("\nQueue TEST <----- end")

if __name__ == "__main__":
    main()