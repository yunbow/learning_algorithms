
// Go
// データ構造: 木 (Tree)

package main

import (
	"fmt"
)

type NodeData struct {
	value    interface{}
	parent   *NodeData
	children []*NodeData
}

func NewNodeData(value interface{}) *NodeData {
	return &NodeData{
		value:    value,
		parent:   nil,
		children: []*NodeData{},
	}
}

func (n *NodeData) GetValue() interface{} {
	return n.value
}

func (n *NodeData) GetParent() *NodeData {
	return n.parent
}

func (n *NodeData) GetChildren() []*NodeData {
	return n.children
}

func (n *NodeData) SetParent(parent *NodeData) bool {
	n.parent = parent
	return true
}

func (n *NodeData) AddChild(child *NodeData) bool {
	child.SetParent(n)
	n.children = append(n.children, child)
	return true
}

func (n *NodeData) RemoveChild(child *NodeData) bool {
	for i, c := range n.children {
		if c == child {
			child.SetParent(nil)
			n.children = append(n.children[:i], n.children[i+1:]...)
			return true
		}
	}
	return false
}

func (n *NodeData) IsLeaf() bool {
	return len(n.children) == 0
}

type TreeData struct {
	data *NodeData
}

func NewTreeData() *TreeData {
	return &TreeData{
		data: nil,
	}
}

func (t *TreeData) Get() *NodeData {
	return t.data
}

func (t *TreeData) GetHeight(node *NodeData) int {
	if node == nil {
		node = t.data
	}
	if node == nil {
		return 0
	}
	if node.IsLeaf() {
		return 1
	}
	maxHeight := 0
	for _, child := range node.GetChildren() {
		height := t.GetHeight(child)
		if height > maxHeight {
			maxHeight = height
		}
	}
	return 1 + maxHeight
}

func (t *TreeData) GetParent(node *NodeData) *NodeData {
	return node.GetParent()
}

func (t *TreeData) GetChildren(node *NodeData) []*NodeData {
	return node.GetChildren()
}

func (t *TreeData) GetNode(value interface{}, node *NodeData) *NodeData {
	if node == nil {
		node = t.data
	}
	if node == nil {
		return nil
	}

	if node.GetValue() == value {
		return node
	}

	for _, child := range node.GetChildren() {
		result := t.GetNode(value, child)
		if result != nil {
			return result
		}
	}

	return nil
}

func (t *TreeData) Add(parent *NodeData, value interface{}) bool {
	newNode := NewNodeData(value)
	if parent == nil {
		if t.data == nil {
			t.data = newNode
			return true
		} else {
			fmt.Printf("ERROR: %v 重複です\n", value)
			return false
		}
	} else {
		parent.AddChild(newNode)
		return true
	}
}

func (t *TreeData) Remove(node *NodeData) bool {
	if node == nil {
		return false
	}

	if node == t.data {
		t.data = nil
		return true
	}

	parent := node.GetParent()
	if parent != nil {
		return parent.RemoveChild(node)
	}

	return false
}

func (t *TreeData) Traverse(node *NodeData, mode string) []interface{} {
	if node == nil {
		node = t.data
	}
	if node == nil {
		return []interface{}{}
	}

	result := []interface{}{}

	switch mode {
	case "pre-order":
		result = append(result, node.GetValue())
		for _, child := range node.GetChildren() {
			result = append(result, t.Traverse(child, mode)...)
		}
	case "post-order":
		for _, child := range node.GetChildren() {
			result = append(result, t.Traverse(child, mode)...)
		}
		result = append(result, node.GetValue())
	case "level-order":
		queue := []*NodeData{node}
		for len(queue) > 0 {
			current := queue[0]
			queue = queue[1:]
			result = append(result, current.GetValue())
			queue = append(queue, current.GetChildren()...)
		}
	}

	return result
}

func (t *TreeData) IsLeaf(node *NodeData) bool {
	return node != nil && node.IsLeaf()
}

func (t *TreeData) IsEmpty() bool {
	return t.data == nil
}

func (t *TreeData) Size(node *NodeData) int {
	if node == nil {
		node = t.data
	}
	if node == nil {
		return 0
	}

	count := 1
	for _, child := range node.GetChildren() {
		count += t.Size(child)
	}

	return count
}

func (t *TreeData) Clear() bool {
	t.data = nil
	return true
}

func (t *TreeData) Display() []interface{} {
	if t.data == nil {
		return []interface{}{}
	}

	return t.Traverse(nil, "level-order")
}

func main() {
	fmt.Println("Tree TEST -----> start")

	fmt.Println("\nnew")
	treeData := NewTreeData()
	fmt.Printf("  現在のデータ: %v\n", treeData.Display())

	fmt.Println("\nis_empty")
	output := treeData.IsEmpty()
	fmt.Printf("  出力値: %v\n", output)

	fmt.Println("\nsize")
	output2 := treeData.Size(nil)
	fmt.Printf("  出力値: %v\n", output2)

	fmt.Println("\nadd")
	fmt.Printf("  入力値: %v, %v\n", nil, "Root")
	output3 := treeData.Add(nil, "Root")
	fmt.Printf("  出力値: %v\n", output3)
	fmt.Printf("  現在のデータ: %v\n", treeData.Display())

	rootNode := treeData.Get()

	fmt.Println("\nadd")
	fmt.Printf("  入力値: %v, %v\n", rootNode, "Child1")
	output4 := treeData.Add(rootNode, "Child1")
	fmt.Printf("  出力値: %v\n", output4)
	fmt.Printf("  現在のデータ: %v\n", treeData.Display())

	fmt.Println("\nadd")
	fmt.Printf("  入力値: %v, %v\n", rootNode, "Child2")
	output5 := treeData.Add(rootNode, "Child2")
	fmt.Printf("  出力値: %v\n", output5)
	fmt.Printf("  現在のデータ: %v\n", treeData.Display())

	fmt.Println("\nget_node")
	inputValue := "Child1"
	fmt.Printf("  入力値: %v\n", inputValue)
	output6 := treeData.GetNode(inputValue, nil)
	fmt.Printf("  出力値: %v\n", output6)
	fmt.Printf("  現在のデータ: %v\n", treeData.Display())

	fmt.Println("\ntraverse")
	inputMode := "pre-order"
	fmt.Printf("  入力値: %v\n", inputMode)
	output7 := treeData.Traverse(nil, inputMode)
	fmt.Printf("  出力値: %v\n", output7)
	fmt.Printf("  現在のデータ: %v\n", treeData.Display())

	fmt.Println("\nTree TEST <----- end")
}