// JavaScript
// データ構造: ヒープ (Heap)

class HeapData {
    constructor(isMinHeap = true) {
        this._data = [];
        this.isMinHeap = isMinHeap;
    }

    _getParentIdx(idx) {
        return Math.floor((idx - 1) / 2);
    }

    _getLeftChildIdx(idx) {
        return 2 * idx + 1;
    }

    _getRightChildIdx(idx) {
        return 2 * idx + 2;
    }

    _hasParent(idx) {
        return this._getParentIdx(idx) >= 0;
    }

    _hasLeftChild(idx) {
        return this._getLeftChildIdx(idx) < this._data.length;
    }

    _hasRightChild(idx) {
        return this._getRightChildIdx(idx) < this._data.length;
    }

    _getParent(idx) {
        return this._data[this._getParentIdx(idx)];
    }

    _getLeftChild(idx) {
        return this._data[this._getLeftChildIdx(idx)];
    }

    _getRightChild(idx) {
        return this._data[this._getRightChildIdx(idx)];
    }

    _swap(idx1, idx2) {
        [this._data[idx1], this._data[idx2]] = [this._data[idx2], this._data[idx1]];
    }

    _shouldSwap(idx1, idx2) {
        if (this.isMinHeap) {
            return this._data[idx1] > this._data[idx2];
        } else {
            return this._data[idx1] < this._data[idx2];
        }
    }

    _heapifyDown(idx) {
        let smallestOrLargest = idx;

        if (this._hasLeftChild(idx) && 
            this._shouldSwap(smallestOrLargest, this._getLeftChildIdx(idx))) {
            smallestOrLargest = this._getLeftChildIdx(idx);
        }

        if (this._hasRightChild(idx) && 
            this._shouldSwap(smallestOrLargest, this._getRightChildIdx(idx))) {
            smallestOrLargest = this._getRightChildIdx(idx);
        }

        if (smallestOrLargest !== idx) {
            this._swap(idx, smallestOrLargest);
            this._heapifyDown(smallestOrLargest);
        }
    }

    _heapifyUp(idx) {
        while (this._hasParent(idx) && 
               this._shouldSwap(this._getParentIdx(idx), idx)) {
            const parentIdx = this._getParentIdx(idx);
            this._swap(parentIdx, idx);
            idx = parentIdx;
        }
    }

    get() {
        return this._data;
    }

    getIndex(item) {
        const index = this._data.indexOf(item);
        if (index === -1) {
            console.log(`ERROR: ${item} は範囲外です`);
        }
        return index;
    }

    getValue(index) {
        if (index >= 0 && index < this._data.length) {
            return this._data[index];
        } else {
            console.log(`ERROR: ${index} は範囲外です`);
            return null;
        }
    }

    heapify(array) {
        this._data = [...array];
        for (let i = Math.floor(this._data.length / 2) - 1; i >= 0; i--) {
            this._heapifyDown(i);
        }
        return true;
    }

    push(value) {
        this._data.push(value);
        this._heapifyUp(this._data.length - 1);
        return true;
    }

    pop() {
        if (this._data.length === 0) {
            return false;
        }

        const lastElement = this._data.pop();

        if (this._data.length > 0) {
            this._data[0] = lastElement;
            this._heapifyDown(0);
        }

        return true;
    }

    peek() {
        if (this._data.length === 0) {
            return null;
        }
        return this._data[0];
    }

    isEmpty() {
        return this._data.length === 0;
    }

    size() {
        return this._data.length;
    }

    clear() {
        this._data = [];
        return true;
    }
}

function main() {
    console.log("Heap TEST -----> start");

    console.log("\nmin heap: new");
    const minHeap = new HeapData(true);
    console.log(`  現在のデータ: ${minHeap.get()}`);

    console.log("\nmin heap: heapify");
    const input = [4, 10, 3, 5, 1];
    console.log(`  入力値: ${input}`);
    const output = minHeap.heapify(input);
    console.log(`  出力値: ${output}`);
    console.log(`  現在のデータ: ${minHeap.get()}`);

    console.log("\nmin heap: push");
    const pushInput1 = 2;
    console.log(`  入力値: ${pushInput1}`);
    const pushOutput1 = minHeap.push(pushInput1);
    console.log(`  出力値: ${pushOutput1}`);
    console.log(`  現在のデータ: ${minHeap.get()}`);

    console.log("\nmin heap: push");
    const pushInput2 = 15;
    console.log(`  入力値: ${pushInput2}`);
    const pushOutput2 = minHeap.push(pushInput2);
    console.log(`  出力値: ${pushOutput2}`);
    console.log(`  現在のデータ: ${minHeap.get()}`);

    console.log("\nmin heap: peek");
    const peekOutput = minHeap.peek();
    console.log(`  出力値: ${peekOutput}`);

    console.log("\nmin heap: pop");
    const popOutput1 = minHeap.pop();
    console.log(`  出力値: ${popOutput1}`);
    console.log(`  現在のデータ: ${minHeap.get()}`);

    console.log("\nmin heap: pop");
    const popOutput2 = minHeap.pop();
    console.log(`  出力値: ${popOutput2}`);
    console.log(`  現在のデータ: ${minHeap.get()}`);

    console.log("\nmin heap: get_index");
    const indexInput1 = 3;
    console.log(`  入力値: ${indexInput1}`);
    const indexOutput1 = minHeap.getIndex(indexInput1);
    console.log(`  出力値: ${indexOutput1}`);

    console.log("\nmin heap: get_index");
    const indexInput2 = 100;
    console.log(`  入力値: ${indexInput2}`);
    const indexOutput2 = minHeap.getIndex(indexInput2);
    console.log(`  出力値: ${indexOutput2}`);

    console.log("\nmin heap: is_empty");
    const isEmptyOutput1 = minHeap.isEmpty();
    console.log(`  出力値: ${isEmptyOutput1}`);

    console.log("\nmin heap: size");
    const sizeOutput = minHeap.size();
    console.log(`  出力値: ${sizeOutput}`);

    console.log("\nmin heap: clear");
    const clearOutput = minHeap.clear();
    console.log(`  出力値: ${clearOutput}`);
    console.log(`  現在のデータ: ${minHeap.get()}`);

    console.log("\nmin heap: is_empty");
    const isEmptyOutput2 = minHeap.isEmpty();
    console.log(`  出力値: ${isEmptyOutput2}`);

    console.log("\nmax heap: new");
    const maxHeap = new HeapData(false);
    console.log(`  現在のデータ: ${maxHeap.get()}`);

    console.log("\nmax heap: heapify");
    const maxInput = [4, 10, 3, 5, 1];
    console.log(`  入力値: ${maxInput}`);
    const maxOutput = maxHeap.heapify(maxInput);
    console.log(`  出力値: ${maxOutput}`);
    console.log(`  現在のデータ: ${maxHeap.get()}`);

    console.log("\nmax heap: push");
    const maxPushInput1 = 12;
    console.log(`  入力値: ${maxPushInput1}`);
    const maxPushOutput1 = maxHeap.push(maxPushInput1);
    console.log(`  出力値: ${maxPushOutput1}`);
    console.log(`  現在のデータ: ${maxHeap.get()}`);

    console.log("\nmax heap: push");
    const maxPushInput2 = 0;
    console.log(`  入力値: ${maxPushInput2}`);
    const maxPushOutput2 = maxHeap.push(maxPushInput2);
    console.log(`  出力値: ${maxPushOutput2}`);
    console.log(`  現在のデータ: ${maxHeap.get()}`);

    console.log("\nmax heap: peek");
    const maxPeekOutput = maxHeap.peek();
    console.log(`  出力値: ${maxPeekOutput}`);

    console.log("\nmax heap: pop");
    const maxPopOutput1 = maxHeap.pop();
    console.log(`  出力値: ${maxPopOutput1}`);
    console.log(`  現在のデータ: ${maxHeap.get()}`);

    console.log("\nmax heap: pop");
    const maxPopOutput2 = maxHeap.pop();
    console.log(`  出力値: ${maxPopOutput2}`);
    console.log(`  現在のデータ: ${maxHeap.get()}`);

    console.log("\nmax heap: get_index");
    const maxIndexInput1 = 5;
    console.log(`  入力値: ${maxIndexInput1}`);
    const maxIndexOutput1 = maxHeap.getIndex(maxIndexInput1);
    console.log(`  出力値: ${maxIndexOutput1}`);

    console.log("\nmax heap: get_index");
    const maxIndexInput2 = -10;
    console.log(`  入力値: ${maxIndexInput2}`);
    const maxIndexOutput2 = maxHeap.getIndex(maxIndexInput2);
    console.log(`  出力値: ${maxIndexOutput2}`);

    console.log("\nmax heap: is_empty");
    const maxIsEmptyOutput1 = maxHeap.isEmpty();
    console.log(`  出力値: ${maxIsEmptyOutput1}`);

    console.log("\nmax heap: size");
    const maxSizeOutput = maxHeap.size();
    console.log(`  出力値: ${maxSizeOutput}`);

    console.log("\nmax heap: clear");
    const maxClearOutput = maxHeap.clear();
    console.log(`  出力値: ${maxClearOutput}`);
    console.log(`  現在のデータ: ${maxHeap.get()}`);

    console.log("\nmax heap: is_empty");
    const maxIsEmptyOutput2 = maxHeap.isEmpty();
    console.log(`  出力値: ${maxIsEmptyOutput2}`);

    console.log("\nHeap TEST <----- end");
}

// If running in Node.js environment
if (typeof module !== 'undefined' && module.exports) {
    main();
}
