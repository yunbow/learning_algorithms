// Java
// 配列の並び替え: ヒープソート (Heap Sort)

import java.util.Arrays;

class HeapData {
    private int[] data;
    // 最小ヒープか最大ヒープかを設定
    private boolean isMinHeap;

    public HeapData() {
        this(true);
    }

    public HeapData(boolean isMinHeap) {
        this.data = new int[0];
        this.isMinHeap = isMinHeap;
    }

    private int getParentIdx(int idx) {
        // 親ノードのインデックスを計算
        if (idx <= 0) {
            return -1; // 根ノードには親がない
        }
        return (idx - 1) / 2;
    }

    private int getLeftChildIdx(int idx) {
        // 左の子ノードのインデックスを計算
        return 2 * idx + 1;
    }

    private int getRightChildIdx(int idx) {
        // 右の子ノードのインデックスを計算
        return 2 * idx + 2;
    }

    private boolean hasParent(int idx) {
        // 親ノードが存在するか確認
        return getParentIdx(idx) >= 0 && getParentIdx(idx) < data.length;
    }

    private boolean hasLeftChild(int idx) {
        // 左の子ノードが存在するか確認
        return getLeftChildIdx(idx) < data.length;
    }

    private boolean hasRightChild(int idx) {
        // 右の子ノードが存在するか確認
        return getRightChildIdx(idx) < data.length;
    }

    private void swap(int idx1, int idx2) {
        // 2つのノードの値を交換
        if (0 <= idx1 && idx1 < data.length && 0 <= idx2 && idx2 < data.length) {
            int temp = data[idx1];
            data[idx1] = data[idx2];
            data[idx2] = temp;
        } else {
            System.out.println("Warning: Swap indices out of bounds: " + idx1 + ", " + idx2);
        }
    }

    private boolean shouldSwap(int idx1, int idx2) {
        // 最小ヒープでは idx1(親) が idx2(子) より大きい場合に交換
        // 最大ヒープでは idx1(親) が idx2(子) より小さい場合に交換
        if (isMinHeap) {
            return data[idx1] > data[idx2];
        } else { // Max heap
            return data[idx1] < data[idx2];
        }
    }

    private void heapifyDown(int idx) {
        // ノードを下方向に移動させてヒープ条件を満たす (isMinHeap に従う)
        int smallestOrLargest = idx;

        int leftChildIdx = getLeftChildIdx(idx);
        int rightChildIdx = getRightChildIdx(idx);

        // 左の子ノードと比較 (存在する場合)
        if (hasLeftChild(idx)) {
            if (isMinHeap) { // Min-heap: compare with smallest child
                if (data[leftChildIdx] < data[smallestOrLargest]) {
                    smallestOrLargest = leftChildIdx;
                }
            } else { // Max-heap: compare with largest child
                if (data[leftChildIdx] > data[smallestOrLargest]) {
                    smallestOrLargest = leftChildIdx;
                }
            }
        }

        // 右の子ノードと比較 (存在する場合)
        if (hasRightChild(idx)) {
            if (isMinHeap) { // Min-heap: compare with smallest child
                if (data[rightChildIdx] < data[smallestOrLargest]) {
                    smallestOrLargest = rightChildIdx;
                }
            } else { // Max-heap: compare with largest child
                if (data[rightChildIdx] > data[smallestOrLargest]) {
                    smallestOrLargest = rightChildIdx;
                }
            }
        }

        // インデックスが変わっていたら交換して再帰的に処理
        if (smallestOrLargest != idx) {
            swap(idx, smallestOrLargest);
            heapifyDown(smallestOrLargest);
        }
    }

    private void maxHeapifyDown(int idx, int heapSize) {
        // 指定されたヒープサイズ内で、idx から下方向に最大ヒープ条件を満たすように調整する。
        int largest = idx; // 最大ヒープでは、親と子の間で最大のものを探す

        int leftChildIdx = getLeftChildIdx(idx);
        int rightChildIdx = getRightChildIdx(idx);

        // 左の子ノードと比較 (ヒープサイズ内に存在する場合)
        if (leftChildIdx < heapSize && data[leftChildIdx] > data[largest]) {
            largest = leftChildIdx;
        }

        // 右の子ノードと比較 (ヒープサイズ内に存在する場合)
        if (rightChildIdx < heapSize && data[rightChildIdx] > data[largest]) {
            largest = rightChildIdx;
        }

        // largest が現在のノード idx と異なる場合、交換して再帰的に処理
        if (largest != idx) {
            swap(idx, largest);
            maxHeapifyDown(largest, heapSize);
        }
    }

    private void heapifyUp(int idx) {
        // ノードを上方向に移動させてヒープ条件を満たす (isMinHeap に従う)
        while (hasParent(idx)) {
            int parentIdx = getParentIdx(idx);
            if (shouldSwap(parentIdx, idx)) {
                swap(parentIdx, idx);
                idx = parentIdx;
            } else {
                break; // Heap property satisfied
            }
        }
    }

    public int[] get() {
        // 要素を取得 (現在の internal data を返す)
        return data;
    }

    public boolean heapify(int[] array) {
        this.data = array;
        int n = array.length;
        // 最後の非葉ノードから根に向かって、各部分木をヒープ化
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapifyDown(i); // Use the regular heapifyDown
        }
        return true;
    }

    public boolean sort() {
        int n = data.length;

        // 配列を最大ヒープに変換する
        for (int i = n / 2 - 1; i >= 0; i--) {
            maxHeapifyDown(i, n);
        }

        // 最大ヒープから要素を一つずつ取り出してソート済みの位置に移動
        for (int i = n - 1; i > 0; i--) {
            // 現在の根 (最大値) をヒープの最後の要素と交換
            swap(0, i);

            // 交換された要素 (元の最大値) は正しい位置に置かれたので、
            // 次のヒープ調整からは除外する。ヒープサイズは i に減少。
            // 新しい根 (元のヒープの最後の要素だったもの) から
            // maxHeapifyDown を使って残りの要素を最大ヒープに調整
            maxHeapifyDown(0, i);
        }

        return true;
    }
}

public class HeapSortDemo {
    public static void main(String[] args) {
        System.out.println("HeapSort TEST -----> start");

        HeapData heapData = new HeapData();

        // ランダムな整数の配列
        System.out.println("\nsort");
        int[] input1 = {64, 34, 25, 12, 22, 11, 90};
        System.out.println("  ソート前: " + Arrays.toString(input1));
        heapData.heapify(input1);
        heapData.sort();
        System.out.println("  ソート後: " + Arrays.toString(heapData.get()));

        // 既にソートされている配列
        System.out.println("\nsort");
        int[] input2 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        System.out.println("  ソート前: " + Arrays.toString(input2));
        heapData.heapify(input2);
        heapData.sort();
        System.out.println("  ソート後: " + Arrays.toString(heapData.get()));

        // 逆順の配列
        System.out.println("\nsort");
        int[] input3 = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        System.out.println("  ソート前: " + Arrays.toString(input3));
        heapData.heapify(input3);
        heapData.sort();
        System.out.println("  ソート後: " + Arrays.toString(heapData.get()));

        // 重複要素を含む配列
        System.out.println("\nsort");
        int[] input4 = {10, 9, 8, 7, 6, 10, 9, 8, 7, 6};
        System.out.println("  ソート前: " + Arrays.toString(input4));
        heapData.heapify(input4);
        heapData.sort();
        System.out.println("  ソート後: " + Arrays.toString(heapData.get()));

        // 空の配列
        System.out.println("\nsort");
        int[] input5 = {};
        System.out.println("  ソート前: " + Arrays.toString(input5));
        heapData.heapify(input5);
        heapData.sort();
        System.out.println("  ソート後: " + Arrays.toString(heapData.get()));

        System.out.println("\nHeapSort TEST <----- end");
    }
}