// JavaScript
// データ構造: 連結リスト (Linked List)

class NodeData {
    constructor(data) {
        this._data = data;
        this._next = null;
    }

    get() {
        return this._data;
    }

    getNext() {
        return this._next;
    }

    setData(data) {
        this._data = data;
    }

    setNext(next) {
        this._next = next;
    }
}

class LinkedListData {
    constructor() {
        this._data = null;
        this._size = 0;
    }

    get() {
        return this._data;
    }

    getPosition(data) {
        if (this.isEmpty()) {
            return -1;
        }

        let current = this._data;
        let position = 0;

        while (current) {
            if (current.get() === data) {
                return position;
            }
            current = current.getNext();
            position++;
        }

        return -1;
    }

    getValue(position) {
        if (this.isEmpty() || position < 0 || position >= this._size) {
            console.log(`ERROR: ${position} は範囲外です`);
            return null;
        }

        let current = this._data;
        for (let i = 0; i < position; i++) {
            current = current.getNext();
        }

        return current.get();
    }

    add(data, position = null) {
        const newNode = new NodeData(data);

        if (this.isEmpty()) {
            this._data = newNode;
            this._size++;
            return true;
        }

        if (position === null || position >= this._size) {
            let current = this._data;
            while (current.getNext()) {
                current = current.getNext();
            }
            current.setNext(newNode);
            this._size++;
            return true;
        }

        if (position === 0) {
            newNode.setNext(this._data);
            this._data = newNode;
            this._size++;
            return true;
        }

        let current = this._data;
        for (let i = 0; i < position - 1; i++) {
            current = current.getNext();
        }

        newNode.setNext(current.getNext());
        current.setNext(newNode);
        this._size++;
        return true;
    }

    remove(position = null, data = null) {
        if (this.isEmpty()) {
            console.log("ERROR: リストが空です");
            return false;
        }

        if (data !== null) {
            if (this._data.get() === data) {
                this._data = this._data.getNext();
                this._size--;
                return true;
            }

            let current = this._data;
            while (current.getNext() && current.getNext().get() !== data) {
                current = current.getNext();
            }

            if (current.getNext()) {
                current.setNext(current.getNext().getNext());
                this._size--;
                return true;
            } else {
                console.log(`ERROR: ${data} は範囲外です`);
                return false;
            }
        }

        if (position === null) {
            position = this._size - 1;
        }

        if (position < 0 || position >= this._size) {
            console.log(`ERROR: ${position} は範囲外です`);
            return false;
        }

        if (position === 0) {
            this._data = this._data.getNext();
            this._size--;
            return true;
        }

        let current = this._data;
        for (let i = 0; i < position - 1; i++) {
            current = current.getNext();
        }

        current.setNext(current.getNext().getNext());
        this._size--;
        return true;
    }

    update(position, data) {
        if (this.isEmpty() || position < 0 || position >= this._size) {
            console.log(`ERROR: ${position} は範囲外です`);
            return false;
        }

        let current = this._data;
        for (let i = 0; i < position; i++) {
            current = current.getNext();
        }

        current.setData(data);
        return true;
    }

    isEmpty() {
        return this._data === null;
    }

    size() {
        return this._size;
    }

    clear() {
        this._data = null;
        this._size = 0;
        return true;
    }

    display() {
        const elements = [];
        let current = this._data;
        while (current) {
            elements.push(current.get());
            current = current.getNext();
        }
        return elements;
    }
}

function main() {
    console.log("LinkedList TEST -----> start");

    console.log("\nnew");
    const linkedListData = new LinkedListData();
    console.log(`  現在のデータ: ${linkedListData.display()}`);

    console.log("\nis_empty");
    const outputEmpty = linkedListData.isEmpty();
    console.log(`  出力値: ${outputEmpty}`);

    console.log("\nsize");
    const outputSize = linkedListData.size();
    console.log(`  出力値: ${outputSize}`);

    console.log("\nadd");
    const input1 = 10;
    console.log(`  入力値: ${input1}`);
    const output1 = linkedListData.add(input1);
    console.log(`  出力値: ${output1}`);
    console.log(`  現在のデータ: ${linkedListData.display()}`);

    console.log("\nadd");
    const input2 = 20;
    console.log(`  入力値: ${input2}`);
    const output2 = linkedListData.add(input2);
    console.log(`  出力値: ${output2}`);
    console.log(`  現在のデータ: ${linkedListData.display()}`);

    console.log("\nadd");
    const input3_0 = 5;
    const input3_1 = 0;
    console.log(`  入力値: (${input3_0}, ${input3_1})`);
    const output3 = linkedListData.add(input3_0, input3_1);
    console.log(`  出力値: ${output3}`);
    console.log(`  現在のデータ: ${linkedListData.display()}`);

    console.log("\nadd");
    const input4_0 = 15;
    const input4_1 = 2;
    console.log(`  入力値: (${input4_0}, ${input4_1})`);
    const output4 = linkedListData.add(input4_0, input4_1);
    console.log(`  出力値: ${output4}`);
    console.log(`  現在のデータ: ${linkedListData.display()}`);

    console.log("\nget_value");
    const input5 = 1;
    console.log(`  入力値: ${input5}`);
    const output5 = linkedListData.getPosition(input5);
    console.log(`  出力値: ${output5}`);

    console.log("\nget_value");
    const input6 = 10;
    console.log(`  入力値: ${input6}`);
    const output6 = linkedListData.getPosition(input6);
    console.log(`  出力値: ${output6}`);

    console.log("\nupdate");
    const input7_0 = 1;
    const input7_1 = 99;
    console.log(`  入力値: (${input7_0}, ${input7_1})`);
    const output7 = linkedListData.update(input7_0, input7_1);
    console.log(`  出力値: ${output7}`);
    console.log(`  現在のデータ: ${linkedListData.display()}`);

    console.log("\nget_value");
    const input8 = 15;
    console.log(`  入力値: ${input8}`);
    const output8 = linkedListData.getValue(input8);
    console.log(`  出力値: ${output8}`);

    console.log("\nget_valuefind");
    const input9 = 100;
    console.log(`  入力値: ${input9}`);
    const output9 = linkedListData.getValue(input9);
    console.log(`  出力値: ${output9}`);

    console.log("\nremove");
    const input10 = 15;
    console.log(`  入力値: data=${input10}`);
    const output10 = linkedListData.remove(null, input10);
    console.log(`  出力値: ${output10}`);
    console.log(`  現在のデータ: ${linkedListData.display()}`);

    console.log("\nremove");
    const input11 = 0;
    console.log(`  入力値: position=${input11}`);
    const output11 = linkedListData.remove(input11);
    console.log(`  出力値: ${output11}`);
    console.log(`  現在のデータ: ${linkedListData.display()}`);

    console.log("\nremove");
    const output12 = linkedListData.remove();
    console.log(`  出力値: ${output12}`);
    console.log(`  現在のデータ: ${linkedListData.display()}`);

    console.log("\nremove");
    const input13 = 5;
    console.log(`  入力値: position=${input13}`);
    const output13 = linkedListData.remove(input13);
    console.log(`  出力値: ${output13}`);
    console.log(`  現在のデータ: ${linkedListData.display()}`);

    console.log("\nclear");
    const output14 = linkedListData.clear();
    console.log(`  出力値: ${output14}`);
    console.log(`  現在のデータ: ${linkedListData.display()}`);

    console.log("\nis_empty");
    const output15 = linkedListData.isEmpty();
    console.log(`  出力値: ${output15}`);

    console.log("\nsize");
    const output16 = linkedListData.size();
    console.log(`出力値: ${output16}`);

    console.log("\nremove");
    const output17 = linkedListData.remove();
    console.log(`  出力値: ${output17}`);
    console.log(`  現在のデータ: ${linkedListData.display()}`);

    console.log("\nLinkedList TEST <----- end");
}

main();