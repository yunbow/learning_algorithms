# Python
# データ構造: 連結リスト (Linked List)

class NodeData:
    def __init__(self, data):
        self._data = data
        self._next = None

    def get(self):
        # 要素を取得
        return self._data

    def get_next(self):
        return self._next
    
    def set_data(self, data):
        self._data = data

    def set_next(self, next):
        self._next = next

class LinkedListData:
    def __init__(self):
        self._data = None
        self._size = 0

    def get(self):
        # 要素を取得
        return self._data

    def get_position(self, data):
        # 指定したデータの位置を検索
        if self.is_empty():
            return -1
        
        current = self._data
        position = 0
        
        while current:
            if current.get() == data:
                return position
            current = current.get_next()
            position += 1
        
        return -1

    def get_value(self, position):
        # 指定した位置の要素を取得
        if self.is_empty() or position < 0 or position >= self._size:
            print(f"ERROR: {position} は範囲外です")
            return None
        
        current = self._data
        for i in range(position):
            current = current.get_next()
        
        return current.get()

    def add(self, data, position=None):
        # 新しいノードを作成
        new_node = NodeData(data)
        
        # リストが空の場合、新しいノードをヘッドにする
        if self.is_empty():
            self._data = new_node
            self._size += 1
            return True
        
        # 位置が指定されていない場合は末尾に追加
        if position is None or position >= self._size:
            current = self._data
            # 最後のノードまで移動
            while current.get_next():
                current = current.get_next()
            current.set_next(new_node)
            self._size += 1
            return True
        
        # 先頭に追加する場合
        if position == 0:
            new_node.set_next(self._data)
            self._data = new_node
            self._size += 1
            return True
        
        # 指定した位置に挿入
        current = self._data
        for i in range(position - 1):
            current = current.get_next()
        
        new_node.set_next(current.get_next())
        current.set_next(new_node)
        self._size += 1
        return True

    def remove(self, position=None, data=None):
        # リストが空の場合
        if self.is_empty():
            print("ERROR: リストが空です")
            return False
        
        # データ指定の場合
        if data is not None:
            if self._data.get() == data:
                self._data = self._data.get_next()
                self._size -= 1
                return True
            
            current = self._data
            while current.get_next() and current.get_next().get() != data:
                current = current.get_next()
            
            if current.get_next():
                current.set_next(current.get_next().get_next())
                self._size -= 1
                return True
            else:
                print(f"ERROR: {data} は範囲外です")
                return False
        
        # 位置指定の場合
        if position is None:
            # デフォルトでは末尾要素を削除
            position = self._size - 1
        
        if position < 0 or position >= self._size:
            print(f"ERROR: {position} は範囲外です")
            return False
        
        if position == 0:
            self._data = self._data.get_next()
            self._size -= 1
            return True
        
        current = self._data
        for i in range(position - 1):
            current = current.get_next()
        
        current.set_next(current.get_next().get_next())
        self._size -= 1
        return True

    def update(self, position, data):
        # 指定した位置の要素を更新
        if self.is_empty() or position < 0 or position >= self._size:
            print(f"ERROR: {position} は範囲外です")
            return False
        
        current = self._data
        for i in range(position):
            current = current.get_next()
        
        current.set_data(data)
        return True

    def is_empty(self):
        # リストが空かどうかを返す
        return self._data is None

    def size(self):
        # リストのサイズを返す
        return self._size

    def clear(self):
        # リストを空にする
        self._data = None
        self._size = 0
        return True
    
    def display(self):
        elements = []
        current = self._data
        while current:
            elements.append(current.get())
            current = current.get_next()
        return elements

def main():
    print("LinkedList TEST -----> start")

    print("\nnew")
    linked_list_data = LinkedListData()
    print(f"  現在のデータ: {linked_list_data.display()}")

    print("\nis_empty")
    output = linked_list_data.is_empty()
    print(f"  出力値: {output}")

    print("\nsize")
    output = linked_list_data.size()
    print(f"  出力値: {output}")

    print("\nadd")
    input = 10
    print(f"  入力値: {input}")
    output = linked_list_data.add(input)
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {linked_list_data.display()}")

    print("\nadd")
    input = 20
    print(f"  入力値: {input}")
    output = linked_list_data.add(input)
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {linked_list_data.display()}")

    print("\nadd")
    input = (5, 0)
    print(f"  入力値: {input}")
    output = linked_list_data.add(*input)
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {linked_list_data.display()}")

    print("\nadd")
    input = (15, 2)
    print(f"  入力値: {input}")
    output = linked_list_data.add(*input)
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {linked_list_data.display()}")

    print("\nget_value")
    input = 1
    print(f"  入力値: {input}")
    output = linked_list_data.get_position(input)
    print(f"  出力値: {output}")

    print("\nget_value")
    input = 10
    print(f"  入力値: {input}")
    output = linked_list_data.get_position(input)
    print(f"  出力値: {output}")

    print("\nupdate")
    input = (1, 99)
    print(f"  入力値: {input}")
    output = linked_list_data.update(*input)
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {linked_list_data.display()}")

    print("\nget_value")
    input = 15
    print(f"  入力値: {input}")
    output = linked_list_data.get_value(input)
    print(f"  出力値: {output}")

    print("\nget_valuefind")
    input = 100
    print(f"  入力値: {input}")
    output = linked_list_data.get_value(input)
    print(f"  出力値: {output}")

    print("\nremove")
    input = 15
    print(f"  入力値: data={input}")
    output = linked_list_data.remove(data=input)
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {linked_list_data.display()}")

    print("\nremove")
    input = 0
    print(f"  入力値: position={input}")
    output = linked_list_data.remove(position=input)
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {linked_list_data.display()}")

    print("\nremove")
    output = linked_list_data.remove()
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {linked_list_data.display()}")

    print("\nremove")
    input = 5
    print(f"  入力値: position={input}")
    output = linked_list_data.remove(position=input)
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {linked_list_data.display()}")

    print("\nclear")
    output = linked_list_data.clear()
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {linked_list_data.display()}")

    print("\nis_empty")
    output = linked_list_data.is_empty()
    print(f"  出力値: {output}")

    print("\nsize")
    output = linked_list_data.size()
    print(f"出力値: {output}")

    print(f"\nremove")
    output = linked_list_data.remove()
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {linked_list_data.display()}")

    print("\nLinkedList TEST <----- end")

if __name__ == "__main__":
    main()