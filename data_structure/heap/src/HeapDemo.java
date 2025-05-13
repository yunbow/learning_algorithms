// Java
// データ構造: ヒープ (Heap)

import java.util.ArrayList;
import java.util.List;

class HeapData {
    private List<Integer> data;
    private boolean isMinHeap;

    public HeapData(boolean isMinHeap) {
        this.data = new ArrayList<>();
        this.isMinHeap = isMinHeap;
    }

    private int getParentIdx(int idx) {
        return (idx - 1) / 2;
    }

    private int getLeftChildIdx(int idx) {
        return 2 * idx + 1;
    }

    private int getRightChildIdx(int idx) {
        return 2 * idx + 2;
    }

    private boolean hasParent(int idx) {
        return getParentIdx(idx) >= 0;
    }

    private boolean hasLeftChild(int idx) {
        return getLeftChildIdx(idx) < data.size();
    }

    private boolean hasRightChild(int idx) {
        return getRightChildIdx(idx) < data.size();
    }

    private int getParent(int idx) {
        return data.get(getParentIdx(idx));
    }

    private int getLeftChild(int idx) {
        return data.get(getLeftChildIdx(idx));
    }

    private int getRightChild(int idx) {
        return data.get(getRightChildIdx(idx));
    }

    private void swap(int idx1, int idx2) {
        int temp = data.get(idx1);
        data.set(idx1, data.get(idx2));
        data.set(idx2, temp);
    }

    private boolean shouldSwap(int idx1, int idx2) {
        if (isMinHeap) {
            return data.get(idx1) > data.get(idx2);
        } else {
            return data.get(idx1) < data.get(idx2);
        }
    }

    private void heapifyDown(int idx) {
        int smallestOrLargest = idx;

        if (hasLeftChild(idx) && shouldSwap(smallestOrLargest, getLeftChildIdx(idx))) {
            smallestOrLargest = getLeftChildIdx(idx);
        }

        if (hasRightChild(idx) && shouldSwap(smallestOrLargest, getRightChildIdx(idx))) {
            smallestOrLargest = getRightChildIdx(idx);
        }

        if (smallestOrLargest != idx) {
            swap(idx, smallestOrLargest);
            heapifyDown(smallestOrLargest);
        }
    }

    private void heapifyUp(int idx) {
        while (hasParent(idx) && shouldSwap(getParentIdx(idx), idx)) {
            int parentIdx = getParentIdx(idx);
            swap(parentIdx, idx);
            idx = parentIdx;
        }
    }

    public List<Integer> get() {
        return new ArrayList<>(data);
    }

    public int getIndex(int item) {
        int index = data.indexOf(item);
        if (index == -1) {
            System.out.println("ERROR: " + item + " は範囲外です");
        }
        return index;
    }

    public Integer getValue(int index) {
        if (index >= 0 && index < data.size()) {
            return data.get(index);
        } else {
            System.out.println("ERROR: " + index + " は範囲外です");
            return null;
        }
    }

    public boolean heapify(List<Integer> array) {
        data = new ArrayList<>(array);
        for (int i = data.size() / 2 - 1; i >= 0; i--) {
            heapifyDown(i);
        }
        return true;
    }

    public boolean push(int value) {
        data.add(value);
        heapifyUp(data.size() - 1);
        return true;
    }

    public boolean pop() {
        if (data.isEmpty()) {
            return false;
        }

        int lastElement = data.remove(data.size() - 1);

        if (!data.isEmpty()) {
            data.set(0, lastElement);
            heapifyDown(0);
        }

        return true;
    }

    public Integer peek() {
        if (data.isEmpty()) {
            return null;
        }
        return data.get(0);
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public int size() {
        return data.size();
    }

    public boolean clear() {
        data.clear();
        return true;
    }
}

public class HeapDemo {
    public static void main(String[] args) {
        System.out.println("Heap TEST -----> start");

        System.out.println("\nmin heap: new");
        HeapData minHeap = new HeapData(true);
        System.out.println("  現在のデータ: " + minHeap.get());

        System.out.println("\nmin heap: heapify");
        List<Integer> input = List.of(4, 10, 3, 5, 1);
        System.out.println("  入力値: " + input);
        boolean output = minHeap.heapify(input);
        System.out.println("  出力値: " + output);
        System.out.println("  現在のデータ: " + minHeap.get());

        System.out.println("\nmin heap: push");
        int input1 = 2;
        System.out.println("  入力値: " + input1);
        output = minHeap.push(input1);
        System.out.println("  出力値: " + output);
        System.out.println("  現在のデータ: " + minHeap.get());

        System.out.println("\nmin heap: push");
        int input2 = 15;
        System.out.println("  入力値: " + input2);
        output = minHeap.push(input2);
        System.out.println("  出力値: " + output);
        System.out.println("  現在のデータ: " + minHeap.get());

        System.out.println("\nmin heap: peek");
        Integer peekOutput = minHeap.peek();
        System.out.println("  出力値: " + peekOutput);

        System.out.println("\nmin heap: pop");
        output = minHeap.pop();
        System.out.println("  出力値: " + output);
        System.out.println("  現在のデータ: " + minHeap.get());

        System.out.println("\nmin heap: pop");
        output = minHeap.pop();
        System.out.println("  出力値: " + output);
        System.out.println("  現在のデータ: " + minHeap.get());

        System.out.println("\nmin heap: get_index");
        int input3 = 3;
        System.out.println("  入力値: " + input3);
        int indexOutput = minHeap.getIndex(input3);
        System.out.println("  出力値: " + indexOutput);

        System.out.println("\nmin heap: get_index");
        int input4 = 100;
        System.out.println("  入力値: " + input4);
        indexOutput = minHeap.getIndex(input4);
        System.out.println("  出力値: " + indexOutput);

        System.out.println("\nmin heap: is_empty");
        boolean isEmptyOutput = minHeap.isEmpty();
        System.out.println("  出力値: " + isEmptyOutput);

        System.out.println("\nmin heap: size");
        int sizeOutput = minHeap.size();
        System.out.println("  出力値: " + sizeOutput);

        System.out.println("\nmin heap: clear");
        output = minHeap.clear();
        System.out.println("  出力値: " + output);
        System.out.println("  現在のデータ: " + minHeap.get());

        System.out.println("\nmin heap: is_empty");
        isEmptyOutput = minHeap.isEmpty();
        System.out.println("  出力値: " + isEmptyOutput);

        System.out.println("\nmax heap: new");
        HeapDemo maxHeap = new HeapDemo(false);
        System.out.println("  現在のデータ: " + maxHeap.get());

        System.out.println("\nmax heap: heapify");
        input = List.of(4, 10, 3, 5, 1);
        System.out.println("  入力値: " + input);
        output = maxHeap.heapify(input);
        System.out.println("  出力値: " + output);
        System.out.println("  現在のデータ: " + maxHeap.get());

        System.out.println("\nmax heap: push");
        int input5 = 12;
        System.out.println("  入力値: " + input5);
        output = maxHeap.push(input5);
        System.out.println("  出力値: " + output);
        System.out.println("  現在のデータ: " + maxHeap.get());

        System.out.println("\nmax heap: push");
        int input6 = 0;
        System.out.println("  入力値: " + input6);
        output = maxHeap.push(input6);
        System.out.println("  出力値: " + output);
        System.out.println("  現在のデータ: " + maxHeap.get());

        System.out.println("\nmax heap: peek");
        peekOutput = maxHeap.peek();
        System.out.println("  出力値: " + peekOutput);

        System.out.println("\nmax heap: pop");
        output = maxHeap.pop();
        System.out.println("  出力値: " + output);
        System.out.println("  現在のデータ: " + maxHeap.get());

        System.out.println("\nmax heap: pop");
        output = maxHeap.pop();
        System.out.println("  出力値: " + output);
        System.out.println("  現在のデータ: " + maxHeap.get());

        System.out.println("\nmax heap: get_index");
        int input7 = 5;
        System.out.println("  入力値: " + input7);
        indexOutput = maxHeap.getIndex(input7);
        System.out.println("  出力値: " + indexOutput);

        System.out.println("\nmax heap: get_index");
        int input8 = -10;
        System.out.println("  入力値: " + input8);
        indexOutput = maxHeap.getIndex(input8);
        System.out.println("  出力値: " + indexOutput);

        System.out.println("\nmax heap: is_empty");
        isEmptyOutput = maxHeap.isEmpty();
        System.out.println("  出力値: " + isEmptyOutput);

        System.out.println("\nmax heap: size");
        sizeOutput = maxHeap.size();
        System.out.println("  出力値: " + sizeOutput);

        System.out.println("\nmax heap: clear");
        output = maxHeap.clear();
        System.out.println("  出力値: " + output);
        System.out.println("  現在のデータ: " + maxHeap.get());

        System.out.println("\nmax heap: is_empty");
        isEmptyOutput = maxHeap.isEmpty();
        System.out.println("  出力値: " + isEmptyOutput);

        System.out.println("\nHeap TEST <----- end");
    }
}
