// Swift
// データ構造: 木 (Tree)

class NodeData {
    private var _value: String
    private var _parent: NodeData?
    private var _children: [NodeData] = []
    
    init(_ value: String) {
        self._value = value
    }
    
    func getValue() -> String {
        return _value
    }
    
    func getParent() -> NodeData? {
        return _parent
    }
    
    func getChildren() -> [NodeData] {
        return _children
    }
    
    func setParent(_ parent: NodeData?) -> Bool {
        self._parent = parent
        return true
    }
    
    func addChild(_ child: NodeData) -> Bool {
        child.setParent(self)
        _children.append(child)
        return true
    }
    
    func removeChild(_ child: NodeData) -> Bool {
        if let index = _children.firstIndex(where: { $0 === child }) {
            child.setParent(nil)
            _children.remove(at: index)
            return true
        } else {
            return false
        }
    }
    
    func isLeaf() -> Bool {
        return _children.isEmpty
    }
}

class TreeData {
    private var _data: NodeData?
    
    func get() -> NodeData? {
        return _data
    }
    
    func getHeight(node: NodeData? = nil) -> Int {
        let currentNode = node ?? _data
        guard let currentNode = currentNode else {
            return 0
        }
        
        if currentNode.isLeaf() {
            return 1
        }
        
        return 1 + currentNode.getChildren().map { getHeight(node: $0) }.max() ?? 0
    }
    
    func getParent(_ node: NodeData) -> NodeData? {
        return node.getParent()
    }
    
    func getChildren(_ node: NodeData) -> [NodeData] {
        return node.getChildren()
    }
    
    func getNode(_ value: String, node: NodeData? = nil) -> NodeData? {
        let currentNode = node ?? _data
        guard let currentNode = currentNode else {
            return nil
        }
        
        if currentNode.getValue() == value {
            return currentNode
        }
        
        for child in currentNode.getChildren() {
            if let result = getNode(value, node: child) {
                return result
            }
        }
        
        return nil
    }
    
    func add(_ parent: NodeData?, _ value: String) -> Bool {
        let newNode = NodeData(value)
        if parent == nil {
            if _data == nil {
                _data = newNode
                return true
            } else {
                print("ERROR: \(value) 重複です")
                return false
            }
        } else {
            parent!.addChild(newNode)
            return true
        }
    }
    
    func remove(_ node: NodeData?) -> Bool {
        guard let node = node else {
            return false
        }
        
        if node === _data {
            _data = nil
            return true
        }
        
        if let parent = node.getParent() {
            return parent.removeChild(node)
        }
        
        return false
    }
    
    func traverse(node: NodeData? = nil, mode: String = "pre-order") -> [String] {
        let currentNode = node ?? _data
        guard let currentNode = currentNode else {
            return []
        }
        
        var result: [String] = []
        
        switch mode {
        case "pre-order":
            result.append(currentNode.getValue())
            for child in currentNode.getChildren() {
                result.append(contentsOf: traverse(node: child, mode: mode))
            }
        case "post-order":
            for child in currentNode.getChildren() {
                result.append(contentsOf: traverse(node: child, mode: mode))
            }
            result.append(currentNode.getValue())
        case "level-order":
            var queue: [NodeData] = [currentNode]
            while !queue.isEmpty {
                let current = queue.removeFirst()
                result.append(current.getValue())
                queue.append(contentsOf: current.getChildren())
            }
        default:
            break
        }
        
        return result
    }
    
    func isLeaf(_ node: NodeData?) -> Bool {
        return node != nil && node!.isLeaf()
    }
    
    func isEmpty() -> Bool {
        return _data == nil
    }
    
    func size(node: NodeData? = nil) -> Int {
        let currentNode = node ?? _data
        guard let currentNode = currentNode else {
            return 0
        }
        
        var count = 1
        for child in currentNode.getChildren() {
            count += size(node: child)
        }
        
        return count
    }
    
    func clear() -> Bool {
        _data = nil
        return true
    }
    
    func display() -> [String] {
        if _data == nil {
            return []
        }
        
        return traverse(mode: "level-order")
    }
}

func main() {
    print("Tree TEST -----> start")
    
    print("\nnew")
    let treeData = TreeData()
    print("  現在のデータ: \(treeData.display())")
    
    print("\nis_empty")
    let isEmpty = treeData.isEmpty()
    print("  出力値: \(isEmpty)")
    
    print("\nsize")
    let size = treeData.size()
    print("  出力値: \(size)")
    
    print("\nadd")
    let inputParams1 = (nil, "Root")
    print("  入力値: \(inputParams1)")
    let output1 = treeData.add(inputParams1.0, inputParams1.1)
    print("  出力値: \(output1)")
    print("  現在のデータ: \(treeData.display())")
    
    let rootNode = treeData.get()
    
    print("\nadd")
    let inputParams2 = (rootNode, "Child1")
    print("  入力値: \(inputParams2)")
    let output2 = treeData.add(inputParams2.0, inputParams2.1)
    print("  出力値: \(output2)")
    print("  現在のデータ: \(treeData.display())")
    
    print("\nadd")
    let inputParams3 = (rootNode, "Child2")
    print("  入力値: \(inputParams3)")
    let output3 = treeData.add(inputParams3.0, inputParams3.1)
    print("  出力値: \(output3)")
    print("  現在のデータ: \(treeData.display())")
    
    print("\nget_node")
    let inputValue = "Child1"
    print("  入力値: \(inputValue)")
    let output4 = treeData.getNode(inputValue)
    print("  出力値: \(output4 != nil ? "some data" : "nil")")
    print("  現在のデータ: \(treeData.display())")
    
    print("\ntraverse")
    let inputMode = "pre-order"
    print("  入力値: \(inputMode)")
    let output5 = treeData.traverse(mode: inputMode)
    print("  出力値: \(output5)")
    print("  現在のデータ: \(treeData.display())")
    
    print("\nTree TEST <----- end")
}

main()