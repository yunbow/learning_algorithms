// Java
// データ構造: 木 (Tree)

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class NodeData {
    private Object value;
    private NodeData parent;
    private List<NodeData> children;
    
    public NodeData(Object value) {
        this.value = value;
        this.parent = null;
        this.children = new ArrayList<>();
    }
    
    public Object getValue() {
        return value;
    }
    
    public NodeData getParent() {
        return parent;
    }
    
    public List<NodeData> getChildren() {
        return children;
    }
    
    public boolean setParent(NodeData parent) {
        this.parent = parent;
        return true;
    }
    
    public boolean addChild(NodeData child) {
        child.setParent(this);
        children.add(child);
        return true;
    }
    
    public boolean removeChild(NodeData child) {
        if (children.contains(child)) {
            child.setParent(null);
            children.remove(child);
            return true;
        } else {
            return false;
        }
    }
    
    public boolean isLeaf() {
        return children.isEmpty();
    }
}

class TreeData {
    private NodeData data;
    
    public TreeData() {
        this.data = null;
    }
    
    public NodeData get() {
        return data;
    }
    
    public int getHeight(NodeData node) {
        if (node == null) {
            node = data;
        }
        if (node == null) {
            return 0;
        }
        if (node.isLeaf()) {
            return 1;
        }
        
        int maxHeight = 0;
        for (NodeData child : node.getChildren()) {
            int height = getHeight(child);
            if (height > maxHeight) {
                maxHeight = height;
            }
        }
        return 1 + maxHeight;
    }
    
    public NodeData getParent(NodeData node) {
        return node.getParent();
    }
    
    public List<NodeData> getChildren(NodeData node) {
        return node.getChildren();
    }
    
    public NodeData getNode(Object value, NodeData node) {
        if (node == null) {
            node = data;
        }
        if (node == null) {
            return null;
        }
        
        if (node.getValue().equals(value)) {
            return node;
        }
        
        for (NodeData child : node.getChildren()) {
            NodeData result = getNode(value, child);
            if (result != null) {
                return result;
            }
        }
        
        return null;
    }
    
    public NodeData getNode(Object value) {
        return getNode(value, null);
    }
    
    public boolean add(NodeData parent, Object value) {
        NodeData newNode = new NodeData(value);
        if (parent == null) {
            if (data == null) {
                data = newNode;
                return true;
            } else {
                System.out.println("ERROR: " + value + " 重複です");
                return false;
            }
        } else {
            return parent.addChild(newNode);
        }
    }
    
    public boolean remove(NodeData node) {
        if (node == null) {
            return false;
        }
        
        if (node == data) {
            data = null;
            return true;
        }
        
        NodeData parent = node.getParent();
        if (parent != null) {
            return parent.removeChild(node);
        }
        
        return false;
    }
    
    public List<Object> traverse(NodeData node, String mode) {
        if (node == null) {
            node = data;
        }
        if (node == null) {
            return new ArrayList<>();
        }
        
        List<Object> result = new ArrayList<>();
        
        if ("pre-order".equals(mode)) {
            result.add(node.getValue());
            for (NodeData child : node.getChildren()) {
                result.addAll(traverse(child, mode));
            }
        } else if ("post-order".equals(mode)) {
            for (NodeData child : node.getChildren()) {
                result.addAll(traverse(child, mode));
            }
            result.add(node.getValue());
        } else if ("level-order".equals(mode)) {
            Queue<NodeData> queue = new LinkedList<>();
            queue.add(node);
            while (!queue.isEmpty()) {
                NodeData current = queue.poll();
                result.add(current.getValue());
                queue.addAll(current.getChildren());
            }
        }
        
        return result;
    }
    
    public List<Object> traverse(String mode) {
        return traverse(null, mode);
    }
    
    public boolean isLeaf(NodeData node) {
        return node != null && node.isLeaf();
    }
    
    public boolean isEmpty() {
        return data == null;
    }
    
    public int size(NodeData node) {
        if (node == null) {
            node = data;
        }
        if (node == null) {
            return 0;
        }
        
        int count = 1;
        for (NodeData child : node.getChildren()) {
            count += size(child);
        }
        
        return count;
    }
    
    public int size() {
        return size(null);
    }
    
    public boolean clear() {
        data = null;
        return true;
    }
    
    public List<Object> display() {
        if (data == null) {
            return new ArrayList<>();
        }
        
        return traverse("level-order");
    }
}

public class TreeDemo {
    public static void main(String[] args) {
        System.out.println("Tree TEST -----> start");
        
        System.out.println("\nnew");
        TreeData treeData = new TreeData();
        System.out.println("  現在のデータ: " + treeData.display());
        
        System.out.println("\nis_empty");
        boolean output1 = treeData.isEmpty();
        System.out.println("  出力値: " + output1);
        
        System.out.println("\nsize");
        int output2 = treeData.size();
        System.out.println("  出力値: " + output2);
        
        System.out.println("\nadd");
        Object[] inputParams1 = {null, "Root"};
        System.out.println("  入力値: " + java.util.Arrays.toString(inputParams1));
        boolean output3 = treeData.add(null, "Root");
        System.out.println("  出力値: " + output3);
        System.out.println("  現在のデータ: " + treeData.display());
        
        NodeData rootNode = treeData.get();
        
        System.out.println("\nadd");
        Object[] inputParams2 = {rootNode, "Child1"};
        System.out.println("  入力値: " + java.util.Arrays.toString(inputParams2));
        boolean output4 = treeData.add(rootNode, "Child1");
        System.out.println("  出力値: " + output4);
        System.out.println("  現在のデータ: " + treeData.display());
        
        System.out.println("\nadd");
        Object[] inputParams3 = {rootNode, "Child2"};
        System.out.println("  入力値: " + java.util.Arrays.toString(inputParams3));
        boolean output5 = treeData.add(rootNode, "Child2");
        System.out.println("  出力値: " + output5);
        System.out.println("  現在のデータ: " + treeData.display());
        
        System.out.println("\nget_node");
        String inputValue = "Child1";
        System.out.println("  入力値: " + inputValue);
        NodeData output6 = treeData.getNode(inputValue);
        System.out.println("  出力値: " + output6);
        System.out.println("  現在のデータ: " + treeData.display());
        
        System.out.println("\ntraverse");
        String inputMode = "pre-order";
        System.out.println("  入力値: " + inputMode);
        List<Object> output7 = treeData.traverse(inputMode);
        System.out.println("  出力値: " + output7);
        System.out.println("  現在のデータ: " + treeData.display());
        
        System.out.println("\nTree TEST <----- end");
    }
}