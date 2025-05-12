// Kotlin
// データ構造: 木 (Tree)

class NodeData<T>(private val value: T) {
    private var parent: NodeData<T>? = null
    private val children = mutableListOf<NodeData<T>>()
    
    fun getValue(): T {
        return value
    }

    fun getParent(): NodeData<T>? {
        return parent
    }

    fun getChildren(): List<NodeData<T>> {
        return children
    }

    fun setParent(parent: NodeData<T>?): Boolean {
        this.parent = parent
        return true
    }

    fun addChild(child: NodeData<T>): Boolean {
        child.setParent(this)
        children.add(child)
        return true
    }
    
    fun removeChild(child: NodeData<T>): Boolean {
        if (child in children) {
            child.setParent(null)
            children.remove(child)
            return true
        } else {
            return false
        }
    }
    
    fun isLeaf(): Boolean {
        return children.isEmpty()
    }
}

class TreeData<T> {
    private var data: NodeData<T>? = null

    fun get(): NodeData<T>? {
        return data
    }
    
    fun getHeight(node: NodeData<T>? = data): Int {
        if (node == null) {
            return 0
        }
        if (node.isLeaf()) {
            return 1
        }
        return 1 + node.getChildren().maxOfOrNull { getHeight(it) } ?: 0
    }
    
    fun getParent(node: NodeData<T>): NodeData<T>? {
        return node.getParent()
    }
    
    fun getChildren(node: NodeData<T>): List<NodeData<T>> {
        return node.getChildren()
    }

    fun getNode(value: T, node: NodeData<T>? = data): NodeData<T>? {
        if (node == null) {
            return null
        }
        
        if (node.getValue() == value) {
            return node
        }
        
        for (child in node.getChildren()) {
            val result = getNode(value, child)
            if (result != null) {
                return result
            }
        }
        
        return null
    }

    fun add(parent: NodeData<T>?, value: T): Boolean {
        val newNode = NodeData(value)
        if (parent == null) {
            if (data == null) {
                data = newNode
                return true
            } else {
                println("ERROR: $value 重複です")
                return false
            }
        } else {
            return parent.addChild(newNode)
        }
    }

    fun remove(node: NodeData<T>?): Boolean {
        if (node == null) {
            return false
        }
        
        if (node == data) {
            data = null
            return true
        }
        
        val parent = node.getParent()
        return parent?.removeChild(node) ?: false
    }
    
    fun traverse(node: NodeData<T>? = data, mode: String = "pre-order"): List<T> {
        val result = mutableListOf<T>()
        if (node == null) {
            return result
        }
        
        when (mode) {
            "pre-order" -> {
                result.add(node.getValue())
                for (child in node.getChildren()) {
                    result.addAll(traverse(child, mode))
                }
            }
            "post-order" -> {
                for (child in node.getChildren()) {
                    result.addAll(traverse(child, mode))
                }
                result.add(node.getValue())
            }
            "level-order" -> {
                val queue = ArrayDeque<NodeData<T>>()
                queue.add(node)
                while (queue.isNotEmpty()) {
                    val current = queue.removeFirst()
                    result.add(current.getValue())
                    queue.addAll(current.getChildren())
                }
            }
        }
        
        return result
    }
    
    fun isLeaf(node: NodeData<T>?): Boolean {
        return node != null && node.isLeaf()
    }
    
    fun isEmpty(): Boolean {
        return data == null
    }
    
    fun size(node: NodeData<T>? = data): Int {
        if (node == null) {
            return 0
        }
        
        var count = 1
        for (child in node.getChildren()) {
            count += size(child)
        }
        
        return count
    }
    
    fun clear(): Boolean {
        data = null
        return true
    }

    fun display(): List<T> {
        if (data == null) {
            return emptyList()
        }
        
        return traverse(mode = "level-order")
    }
}

fun main() {
    println("Tree TEST -----> start")

    println("\nnew")
    val treeData = TreeData<String>()
    println("  現在のデータ: ${treeData.display()}")

    println("\nis_empty")
    val isEmpty = treeData.isEmpty()
    println("  出力値: $isEmpty")

    println("\nsize")
    val size = treeData.size()
    println("  出力値: $size")

    println("\nadd")
    val inputParams1 = Pair(null, "Root")
    println("  入力値: $inputParams1")
    val output1 = treeData.add(inputParams1.first, inputParams1.second)
    println("  出力値: $output1")
    println("  現在のデータ: ${treeData.display()}")
    
    val rootNode = treeData.get()

    println("\nadd")
    val inputParams2 = Pair(rootNode, "Child1")
    println("  入力値: $inputParams2")
    val output2 = treeData.add(inputParams2.first, inputParams2.second)
    println("  出力値: $output2")
    println("  現在のデータ: ${treeData.display()}")

    println("\nadd")
    val inputParams3 = Pair(rootNode, "Child2")
    println("  入力値: $inputParams3")
    val output3 = treeData.add(inputParams3.first, inputParams3.second)
    println("  出力値: $output3")
    println("  現在のデータ: ${treeData.display()}")

    println("\nget_node")
    val inputValue = "Child1"
    println("  入力値: $inputValue")
    val output4 = treeData.getNode(inputValue)
    println("  出力値: $output4")
    println("  現在のデータ: ${treeData.display()}")

    println("\ntraverse")
    val inputMode = "pre-order"
    println("  入力値: $inputMode")
    val output5 = treeData.traverse(mode = inputMode)
    println("  出力値: $output5")
    println("  現在のデータ: ${treeData.display()}")

    println("\nTree TEST <----- end")
}