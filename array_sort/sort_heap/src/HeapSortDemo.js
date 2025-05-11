// JavaScript
// 配列の並び替え: ヒープソート (Heap Sort)

class HeapData {
    constructor(isMinHeap = true) {
        this._data = [];
        // 最小ヒープか最大ヒープかを設定
        this.isMinHeap = isMinHeap;
    }

    _getParentIdx(idx) {
        // 親ノードのインデックスを計算
        if (idx <= 0) {
            return -1; // 根ノードには親がない
        }
        return Math.floor((idx - 1) / 2);
    }

    _getLeftChildIdx(idx) {
        // 左の子ノードのインデックスを計算
        return 2 * idx + 1;
    }

    _getRightChildIdx(idx) {
        // 右の子ノードのインデックスを計算
        return 2 * idx + 2;
    }

    _hasParent(idx) {
        // 親ノードが存在するか確認
        return this._getParentIdx(idx) >= 0 && this._getParentIdx(idx) < this._data.length;
    }

    _hasLeftChild(idx) {
        // 左の子ノードが存在するか確認
        return this._getLeftChildIdx(idx) < this._data.length;
    }

    _hasRightChild(idx) {
        // 右の子ノードが存在するか確認
        return this._getRightChildIdx(idx) < this._data.length;
    }

    _swap(idx1, idx2) {
        // 2つのノードの値を交換
        if (0 <= idx1 && idx1 < this._data.length && 0 <= idx2 && idx2 < this._data.length) {
            [this._data[idx1], this._data[idx2]] = [this._data[idx2], this._data[idx1]];
        } else {
            console.log(`Warning: Swap indices out of bounds: ${idx1}, ${idx2}`);
        }
    }

    _shouldSwap(idx1, idx2) {
        // 最小ヒープでは idx1(親) が idx2(子) より大きい場合に交換
        // 最大ヒープでは idx1(親) が idx2(子) より小さい場合に交換
        if (this.isMinHeap) {
            return this._data[idx1] > this._data[idx2];
        } else { // Max heap
            return this._data[idx1] < this._data[idx2];
        }
    }

    _heapifyDown(idx) {
        // ノードを下方向に移動させてヒープ条件を満たす (isMinHeap に従う)
        let smallestOrLargest = idx;

        const leftChildIdx = this._getLeftChildIdx(idx);
        const rightChildIdx = this._getRightChildIdx(idx);

        // 左の子ノードと比較 (存在する場合)
        if (this._hasLeftChild(idx)) {
            if (this.isMinHeap) { // Min-heap: compare with smallest child
                if (this._data[leftChildIdx] < this._data[smallestOrLargest]) {
                    smallestOrLargest = leftChildIdx;
                }
            } else { // Max-heap: compare with largest child
                if (this._data[leftChildIdx] > this._data[smallestOrLargest]) {
                    smallestOrLargest = leftChildIdx;
                }
            }
        }

        // 右の子ノードと比較 (存在する場合)
        if (this._hasRightChild(idx)) {
            if (this.isMinHeap) { // Min-heap: compare with smallest child
                if (this._data[rightChildIdx] < this._data[smallestOrLargest]) {
                    smallestOrLargest = rightChildIdx;
                }
            } else { // Max-heap: compare with largest child
                if (this._data[rightChildIdx] > this._data[smallestOrLargest]) {
                    smallestOrLargest = rightChildIdx;
                }
            }
        }

        // インデックスが変わっていたら交換して再帰的に処理
        if (smallestOrLargest !== idx) {
            this._swap(idx, smallestOrLargest);
            this._heapifyDown(smallestOrLargest);
        }
    }

    _maxHeapifyDown(idx, heapSize) {
        // 指定されたヒープサイズ内で、idx から下方向に最大ヒープ条件を満たすように調整する。
        let largest = idx; // 最大ヒープでは、親と子の間で最大のものを探す

        const leftChildIdx = this._getLeftChildIdx(idx);
        const rightChildIdx = this._getRightChildIdx(idx);

        // 左の子ノードと比較 (ヒープサイズ内に存在する場合)
        if (leftChildIdx < heapSize && this._data[leftChildIdx] > this._data[largest]) {
            largest = leftChildIdx;
        }

        // 右の子ノードと比較 (ヒープサイズ内に存在する場合)
        if (rightChildIdx < heapSize && this._data[rightChildIdx] > this._data[largest]) {
            largest = rightChildIdx;
        }

        // largest が現在のノード idx と異なる場合、交換して再帰的に処理
        if (largest !== idx) {
            this._swap(idx, largest);
            this._maxHeapifyDown(largest, heapSize);
        }
    }

    _heapifyUp(idx) {
        // ノードを上方向に移動させてヒープ条件を満たす (isMinHeap に従う)
        while (this._hasParent(idx)) {
            const parentIdx = this._getParentIdx(idx);
            if (this._shouldSwap(parentIdx, idx)) {
                this._swap(parentIdx, idx);
                idx = parentIdx;
            } else {
                break; // Heap property satisfied
            }
        }
    }

    get() {
        // 要素を取得 (現在の internal data を返す)
        return this._data;
    }

    heapify(array) {
        this._data = [...array]; // Create a copy to avoid modifying the original array
        const n = array.length;
        // 最後の非葉ノードから根に向かって、各部分木をヒープ化
        for (let i = Math.floor(n / 2) - 1; i >= 0; i--) {
            this._heapifyDown(i); // Use the regular heapify_down
        }
        return true;
    }

    sort() {
        const n = this._data.length;

        // 配列を最大ヒープに変換する
        for (let i = Math.floor(n / 2) - 1; i >= 0; i--) {
            this._maxHeapifyDown(i, n);
        }

        // 最大ヒープから要素を一つずつ取り出してソート済みの位置に移動
        for (let i = n - 1; i > 0; i--) {
            // 現在の根 (最大値) をヒープの最後の要素と交換
            this._swap(0, i);

            // 交換された要素 (元の最大値) は正しい位置に置かれたので、
            // 次のヒープ調整からは除外する。ヒープサイズは i に減少。
            // 新しい根 (元のヒープの最後の要素だったもの) から
            // _maxHeapifyDown を使って残りの要素を最大ヒープに調整
            this._maxHeapifyDown(0, i);
        }
        
        return true;
    }
}

function main() {
    console.log("HeapSort TEST -----> start");

    const heapData = new HeapData();

    // ランダムな整数の配列
    console.log("\nsort");
    let input = [64, 34, 25, 12, 22, 11, 90];
    console.log(`  ソート前: ${input}`);
    heapData.heapify(input);
    heapData.sort();
    console.log(`  ソート後: ${heapData.get()}`);
    
    // 既にソートされている配列
    console.log("\nsort");
    input = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
    console.log(`  ソート前: ${input}`);
    heapData.heapify(input);
    heapData.sort();
    console.log(`  ソート後: ${heapData.get()}`);
    
    // 逆順の配列
    console.log("\nsort");
    input = [10, 9, 8, 7, 6, 5, 4, 3, 2, 1];
    console.log(`  ソート前: ${input}`);
    heapData.heapify(input);
    heapData.sort();
    console.log(`  ソート後: ${heapData.get()}`);
    
    // 重複要素を含む配列
    console.log("\nsort");
    input = [10, 9, 8, 7, 6, 10, 9, 8, 7, 6];
    console.log(`  ソート前: ${input}`);
    heapData.heapify(input);
    heapData.sort();
    console.log(`  ソート後: ${heapData.get()}`);
    
    // 空の配列
    console.log("\nsort");
    input = [];
    console.log(`  ソート前: ${input}`);
    heapData.heapify(input);
    heapData.sort();
    console.log(`  ソート後: ${heapData.get()}`);

    console.log("\nHeapSort TEST <----- end");
}

// JavaScript では main 関数を直接実行する
main();