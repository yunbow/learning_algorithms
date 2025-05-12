// JavaScript
// データ構造: キュー (Queue)

class QueueData {
    constructor() {
        this._data = [];
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
            console.log(`Error: インデックス ${index} は範囲外です`);
            return null;
        }
    }

    enqueue(item) {
        this._data.push(item);
        return true;
    }

    dequeue() {
        if (!this.isEmpty()) {
            this._data.shift();
            return true;
        } else {
            console.log("ERROR: キューが空です");
            return false;
        }
    }

    peek() {
        if (!this.isEmpty()) {
            return this._data[0];
        } else {
            console.log("ERROR: キューが空です");
            return null;
        }
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
    console.log("Queue TEST -----> start");

    console.log("\nnew");
    const queueData = new QueueData();
    console.log(`  現在のデータ: ${queueData.get()}`);

    console.log("\nis_empty");
    const output1 = queueData.isEmpty();
    console.log(`  出力値: ${output1}`);

    console.log("\nenqueue");
    const input = [10, 20, 30];
    for (const item of input) {
        console.log(`  入力値: ${item}`);
        const output = queueData.enqueue(item);
        console.log(`  出力値: ${output}`);
        console.log(`  現在のデータ: ${queueData.get()}`);
    }

    console.log("\nsize");
    const output2 = queueData.size();
    console.log(`  出力値: ${output2}`);

    console.log("\npeek");
    const output3 = queueData.peek();
    console.log(`  出力値: ${output3}`);

    console.log("\nget_index");
    const input1 = 20;
    console.log(`  入力値: ${input1}`);
    const output4 = queueData.getIndex(input1);
    console.log(`  出力値: ${output4}`);

    console.log("\nget_index");
    const input2 = 50;
    console.log(`  入力値: ${input2}`);
    const output5 = queueData.getIndex(input2);
    console.log(`  出力値: ${output5}`);

    console.log("\ndequeue");
    const output6 = queueData.dequeue();
    console.log(`  出力値: ${output6}`);
    console.log(`  現在のデータ: ${queueData.get()}`);

    console.log("\ndequeue");
    const output7 = queueData.dequeue();
    console.log(`  出力値: ${output7}`);
    console.log(`  現在のデータ: ${queueData.get()}`);

    console.log("\nsize");
    const output8 = queueData.size();
    console.log(`  出力値: ${output8}`);

    console.log("\ndequeue");
    const output9 = queueData.dequeue();
    console.log(`  出力値: ${output9}`);
    console.log(`  現在のデータ: ${queueData.get()}`);

    console.log("\ndequeue");
    const output10 = queueData.dequeue();
    console.log(`  出力値: ${output10}`);
    console.log(`  現在のデータ: ${queueData.get()}`);

    console.log("\nis_empty");
    const output11 = queueData.isEmpty();
    console.log(`  出力値: ${output11}`);

    console.log("\nclear");
    const output12 = queueData.clear();
    console.log(`  出力値: ${output12}`);
    console.log(`  現在のデータ: ${queueData.get()}`);

    console.log("\nsize");
    const output13 = queueData.size();
    console.log(`  出力値: ${output13}`);

    console.log("\nQueue TEST <----- end");
}

main();