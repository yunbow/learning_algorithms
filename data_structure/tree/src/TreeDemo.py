# Python
# データ構造: 木 (Tree)

class NodeData:
    def __init__(self, value):
        self._value = value
        self._parent = None
        self._children = []
    
    def get_value(self):
        return self._value

    def get_parent(self):
        return self._parent

    def get_children(self):
        return self._children

    def set_parent(self, parent):
        self._parent = parent
        return True

    def add_child(self, child):
        child.set_parent(self)
        self._children.append(child)
        return True
    
    def remove_child(self, child):
        if child in self._children:
            child.set_parent(None)
            self._children.remove(child)
            return True
        else:
            return False
    
    def is_leaf(self):
        return len(self._children) == 0

class TreeData:
    def __init__(self):
        self._data = None

    def get(self):
        return self._data
    
    def get_height(self, node=None):
        if node is None:
            node = self._data
        if node is None:
            return 0
        if node.is_leaf():
            return 1
        return 1 + max(self.get_height(child) for child in node.get_children())
    
    def get_parent(self, node):
        return node.get_parent()
    
    def get_children(self, node):
        return node.get_children()

    def get_node(self, value, node=None):
        if node is None:
            node = self._data
        if node is None:
            return None
        
        if node.get_value() == value:
            return node
        
        for child in node.get_children():
            result = self.get_node(value, child)
            if result is not None:
                return result
        
        return None

    def add(self, parent, value):
        new_node = NodeData(value)
        if parent is None:
            # 親が指定されていない場合は、ルートとして追加
            if self._data is None:
                self._data = new_node
                return True
            else:
                # ルートが既に存在する場合はエラー
                print(f"ERROR: {value} 重複です")
                return False
        else:
            # 親ノードに子として追加
            parent.add_child(new_node)
            return True

    def remove(self, node):
        if node is None:
            return False
        
        if node == self._data:
            self._data = None
            return True
        
        parent = node.get_parent()
        if parent is not None:
            return parent.remove_child(node)
        
        return False
    
    def traverse(self, node=None, mode="pre-order"):
        if node is None:
            node = self._data
        if node is None:
            return []
        
        result = []
        
        if mode == "pre-order":
            # 先に親、次に子
            result.append(node.get_value())
            for child in node.get_children():
                result.extend(self.traverse(child, mode))
        elif mode == "post-order":
            # 先に子、次に親
            for child in node.get_children():
                result.extend(self.traverse(child, mode))
            result.append(node.get_value())
        elif mode == "level-order":
            # 階層順
            queue = [node]
            while queue:
                current = queue.pop(0)
                result.append(current.get_value())
                queue.extend(current.get_children())
        
        return result
    
    def is_leaf(self, node):
        return node is not None and node.is_leaf()
    
    def is_empty(self):
        return self._data is None
    
    def size(self, node=None):
        if node is None:
            node = self._data
        if node is None:
            return 0
        
        count = 1  # 自分自身
        for child in node.get_children():
            count += self.size(child)
        
        return count
    
    def clear(self):
        self._data = None
        return True

    def display(self):
        if self._data is None:
            return []
        
        return self.traverse(mode="level-order")

def main():
    print("Tree TEST -----> start")

    print("\nnew")
    tree_data = TreeData()
    print(f"  現在のデータ: {tree_data.display()}")

    print("\nis_empty")
    output = tree_data.is_empty()
    print(f"  出力値: {output}")

    print("\nsize")
    output = tree_data.size()
    print(f"  出力値: {output}")

    print("\nadd")
    input_params = (None, 'Root')
    print(f"  入力値: {input_params}")
    output = tree_data.add(*input_params)
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {tree_data.display()}")
    
    root_node = tree_data.get()

    print("\nadd")
    input_params = (root_node, 'Child1')
    print(f"  入力値: {input_params}")
    output = tree_data.add(*input_params)
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {tree_data.display()}")

    print("\nadd")
    input_params = (root_node, 'Child2')
    print(f"  入力値: {input_params}")
    output = tree_data.add(*input_params)
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {tree_data.display()}")

    print("\nget_node")
    input_value = 'Child1'
    print(f"  入力値: {input_value}")
    output = tree_data.get_node(input_value)
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {tree_data.display()}")

    print("\ntraverse")
    input_mode = 'pre-order'
    print(f"  入力値: {input_mode}")
    output = tree_data.traverse(mode=input_mode)
    print(f"  出力値: {output}")
    print(f"  現在のデータ: {tree_data.display()}")

    print("\nTree TEST <----- end")

if __name__ == "__main__":
    main()