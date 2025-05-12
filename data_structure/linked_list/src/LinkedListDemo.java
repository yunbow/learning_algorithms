// Java
// データ構造: 連結リスト (Linked List)

import java.util.ArrayList;
import java.util.List;

class NodeData {
    private Object data;
    private NodeData next;

    public NodeData(Object data) {
        this.data = data;
        this.next = null;
    }

    public Object getData() {
        return data;
    }

    public NodeData getNext() {
        return next;
    }
    
    public void setData(Object data) {
        this.data = data;
    }

    public void setNext(NodeData next) {
        this.next = next;
    }
}

class LinkedListData {
    private NodeData data;
    private int size;

    public LinkedListData() {
        this.data = null;
        this.size = 0;
    }

    public NodeData getData() {
        return this.data;
    }

    public int getPosition(Object searchData) {
        if (isEmpty()) {
            return -1;
        }
        
        NodeData current = this.data;
        int position = 0;
        
        while (current != null) {
            if (current.getData().equals(searchData)) {
                return position;
            }
            current = current.getNext();
            position++;
        }
        
        return -1;
    }

    public Object getValue(int position) {
        if (isEmpty() || position < 0 || position >= size) {
            System.out.printf("ERROR: %d は範囲外です%n", position);
            return null;
        }
        
        NodeData current = this.data;
        for (int i = 0; i < position; i++) {
            current = current.getNext();
        }
        
        return current.getData();
    }

    public boolean add(Object data) {
        return add(data, null);
    }

    public boolean add(Object data, Integer position) {
        NodeData newNode = new NodeData(data);
        
        if (isEmpty()) {
            this.data = newNode;
            size++;
            return true;
        }
        
        if (position == null || position >= size) {
            NodeData current = this.data;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newNode);
            size++;
            return true;
        }
        
        if (position == 0) {
            newNode.setNext(this.data);
            this.data = newNode;
            size++;
            return true;
        }
        
        NodeData current = this.data;
        for (int i = 0; i < position - 1; i++) {
            current = current.getNext();
        }
        
        newNode.setNext(current.getNext());
        current.setNext(newNode);
        size++;
        return true;
    }

    public boolean remove() {
        return remove(null, null);
    }

    public boolean remove(Integer position) {
        return remove(position, null);
    }

    public boolean remove(Object data) {
        return remove(null, data);
    }

    public boolean remove(Integer position, Object data) {
        if (isEmpty()) {
            System.out.println("ERROR: リストが空です");
            return false;
        }
        
        if (data != null) {
            if (this.data.getData().equals(data)) {
                this.data = this.data.getNext();
                size--;
                return true;
            }
            
            NodeData current = this.data;
            while (current.getNext() != null && !current.getNext().getData().equals(data)) {
                current = current.getNext();
            }
            
            if (current.getNext() != null) {
                current.setNext(current.getNext().getNext());
                size--;
                return true;
            } else {
                System.out.printf("ERROR: %s は範囲外です%n", data);
                return false;
            }
        }
        
        if (position == null) {
            position = size - 1;
        }
        
        if (position < 0 || position >= size) {
            System.out.printf("ERROR: %d は範囲外です%n", position);
            return false;
        }
        
        if (position == 0) {
            this.data = this.data.getNext();
            size--;
            return true;
        }
        
        NodeData current = this.data;
        for (int i = 0; i < position - 1; i++) {
            current = current.getNext();
        }
        
        current.setNext(current.getNext().getNext());
        size--;
        return true;
    }

    public boolean update(int position, Object data) {
        if (isEmpty() || position < 0 || position >= size) {
            System.out.printf("ERROR: %d は範囲外です%n", position);
            return false;
        }
        
        NodeData current = this.data;
        for (int i = 0; i < position; i++) {
            current = current.getNext();
        }
        
        current.setData(data);
        return true;
    }

    public boolean isEmpty() {
        return this.data == null;
    }

    public int size() {
        return this.size;
    }

    public boolean clear() {
        this.data = null;
        this.size = 0;
        return true;
    }
    
    public List<Object> display() {
        List<Object> elements = new ArrayList<>();
        NodeData current = this.data;
        while (current != null) {
            elements.add(current.getData());
            current = current.getNext();
        }
        return elements;
    }

    public static void main(String[] args) {
        System.out.println("LinkedList TEST -----> start");

        System.out.println("\nnew");
        LinkedListData linkedListData = new LinkedListData();
        System.out.println("  現在のデータ: " + linkedListData.display());

        System.out.println("\nis_empty");
        boolean output = linkedListData.isEmpty();
        System.out.println("  出力値: " + output);

        System.out.println("\nsize");
        int sizeOutput = linkedListData.size();
        System.out.println("  出力値: " + sizeOutput);

        System.out.println("\nadd");
        int input = 10;
        System.out.println("  入力値: " + input);
        boolean addOutput = linkedListData.add(input);
        System.out.println("  出力値: " + addOutput);
        System.out.println("  現在のデータ: " + linkedListData.display());

        System.out.println("\nadd");
        input = 20;
        System.out.println("  入力値: " + input);
        addOutput = linkedListData.add(input);
        System.out.println("  出力値: " + addOutput);
        System.out.println("  現在のデータ: " + linkedListData.display());

        System.out.println("\nadd");
        int[] input1 = {5, 0};
        System.out.println("  入力値: " + input1[0] + ", " + input1[1]);
        addOutput = linkedListData.add(input1[0], input1[1]);
        System.out.println("  出力値: " + addOutput);
        System.out.println("  現在のデータ: " + linkedListData.display());

        System.out.println("\nadd");
        int[] input2 = {15, 2};
        System.out.println("  入力値: " + input2[0] + ", " + input2[1]);
        addOutput = linkedListData.add(input2[0], input2[1]);
        System.out.println("  出力値: " + addOutput);
        System.out.println("  現在のデータ: " + linkedListData.display());

        System.out.println("\nget_value");
        int findInput = 1;
        System.out.println("  入力値: " + findInput);
        int positionOutput = linkedListData.getPosition(findInput);
        System.out.println("  出力値: " + positionOutput);

        System.out.println("\nget_value");
        findInput = 10;
        System.out.println("  入力値: " + findInput);
        positionOutput = linkedListData.getPosition(findInput);
        System.out.println("  出力値: " + positionOutput);

        System.out.println("\nupdate");
        int[] updateInput = {1, 99};
        System.out.println("  入力値: " + updateInput[0] + ", " + updateInput[1]);
        boolean updateOutput = linkedListData.update(updateInput[0], updateInput[1]);
        System.out.println("  出力値: " + updateOutput);
        System.out.println("  現在のデータ: " + linkedListData.display());

        System.out.println("\nget_value");
        int valueInput = 15;
        System.out.println("  入力値: " + valueInput);
        Object getValueOutput = linkedListData.getValue(valueInput);
        System.out.println("  出力値: " + getValueOutput);

        System.out.println("\nget_valuefind");
        valueInput = 100;
        System.out.println("  入力値: " + valueInput);
        getValueOutput = linkedListData.getValue(valueInput);
        System.out.println("  出力値: " + getValueOutput);

        System.out.println("\nremove");
        int removeInput = 15;
        System.out.println("  入力値: data=" + removeInput);
        boolean removeOutput = linkedListData.remove(removeInput);
        System.out.println("  出力値: " + removeOutput);
        System.out.println("  現在のデータ: " + linkedListData.display());

        System.out.println("\nremove");
        int removePositionInput = 0;
        System.out.println("  入力値: position=" + removePositionInput);
        removeOutput = linkedListData.remove(removePositionInput);
        System.out.println("  出力値: " + removeOutput);
        System.out.println("  現在のデータ: " + linkedListData.display());

        System.out.println("\nremove");
        removeOutput = linkedListData.remove();
        System.out.println("  出力値: " + removeOutput);
        System.out.println("  現在のデータ: " + linkedListData.display());

        System.out.println("\nremove");
        removePositionInput = 5;
        System.out.println("  入力値: position=" + removePositionInput);
        removeOutput = linkedListData.remove(removePositionInput);
        System.out.println("  出力値: " + removeOutput);
        System.out.println("  現在のデータ: " + linkedListData.display());

        System.out.println("\nclear");
        boolean clearOutput = linkedListData.clear();
        System.out.println("  出力値: " + clearOutput);
        System.out.println("  現在のデータ: " + linkedListData.display());

        System.out.println("\nis_empty");
        output = linkedListData.isEmpty();
        System.out.println("  出力値: " + output);

        System.out.println("\nsize");
        sizeOutput = linkedListData.size();
        System.out.println("出力値: " + sizeOutput);

        System.out.println("\nremove");
        removeOutput = linkedListData.remove();
        System.out.println("  出力値: " + removeOutput);
        System.out.println("  現在のデータ: " + linkedListData.display());

        System.out.println("\nLinkedList TEST <----- end");
    }
}
