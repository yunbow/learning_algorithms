// JavaScript
// データ構造: 配列 (Array)

class ArrayData {
    constructor() {
        this._data = [];
    }
    
    get() {
        // 要素を取得
        return this._data;
    }

    getIndex(item) {
        // 配列内で指定された値を検索し、最初に見つかったインデックスを返す
        const index = this._data.indexOf(item);
        if (index !== -1) {
            return index;
        } else {
            console.log(`ERROR: ${item} は範囲外です`);
            return -1;
        }
    }

    getValue(index) {
        // 指定されたインデックスの要素を取得する
        if (0 <= index && index < this._data.length) {
            return this._data[index];
        } else {
            console.log(`ERROR: ${index} は範囲外です`);
            return null;
        }
    }

    add(item) {
        // 配列の末尾に要素を追加する
        this._data.push(item);
        return true;
    }
    
    remove(index) {
        // 指定されたインデックスの要素を削除する
        if (0 <= index && index < this._data.length) {
            this._data.splice(index, 1);
            return true;
        } else {
            console.log(`ERROR: ${index} は範囲外です`);
            return false;
        }
    }
    
    update(index, newValue) {
        // 指定されたインデックスの要素を新しい値に更新する
        if (0 <= index && index < this._data.length) {
            this._data[index] = newValue;
            return true;
        } else {
            console.log(`ERROR: ${index} は範囲外です`);
            return false;
        }
    }

    reverse() {
        // 配列の要素を逆順にする
        this._data.reverse();
        return this._data;
    }
    
    sort(descending = false) {
        // 配列の要素をソートする
        if (descending) {
            this._data.sort((a, b) => b - a);
        } else {
            this._data.sort((a, b) => a - b);
        }
        return this._data;
    }
        
    isEmpty() {
        // 配列が空かどうか
        return this._data.length === 0;
    }
    
    size() {
        // 配列のサイズ（要素数）を返す
        return this._data.length;
    }
    
    clear() {
        // 配列の全要素を削除する
        this._data = [];
        return true;
    }
}

function main() {
    console.log("Array TEST -----> start");

    console.log("\nnew");
    const arrayData = new ArrayData();
    console.log(`  現在のデータ: ${JSON.stringify(arrayData.get())}`);

    console.log("\nadd");
    const input = [10, 20, 30, 10, 40];
    for (const item of input) {
        console.log(`  入力値: ${item}`);
        const output = arrayData.add(item);
        console.log(`  出力値: ${output}`);
        console.log(`  現在のデータ: ${JSON.stringify(arrayData.get())}`);
    }

    console.log("\nsize");
    let output = arrayData.size();
    console.log(`  出力値: ${output}`);

    console.log("\nisEmpty");
    output = arrayData.isEmpty();
    console.log(`  出力値: ${output}`);

    console.log("\ngetValue");
    let inputIndex = 2;
    console.log(`  入力値: ${inputIndex}`);
    output = arrayData.getValue(inputIndex);
    console.log(`  出力値: ${output}`);

    console.log("\ngetValue");
    inputIndex = 10;
    console.log(`  入力値: ${inputIndex}`);
    output = arrayData.getValue(inputIndex);
    console.log(`  出力値: ${output}`);

    console.log("\nupdate");
    let updateInput = [1, 25];
    console.log(`  入力値: ${JSON.stringify(updateInput)}`);
    output = arrayData.update(...updateInput);
    console.log(`  出力値: ${output}`);
    console.log(`  現在のデータ: ${JSON.stringify(arrayData.get())}`);

    console.log("\nupdate");
    updateInput = [15, 25];
    console.log(`  入力値: ${JSON.stringify(updateInput)}`);
    output = arrayData.update(...updateInput);
    console.log(`  出力値: ${output}`);
    console.log(`  現在のデータ: ${JSON.stringify(arrayData.get())}`);

    console.log("\ngetIndex");
    let searchItem = 10;
    console.log(`  入力値: ${searchItem}`);
    output = arrayData.getIndex(searchItem);
    console.log(`  出力値: ${output}`);

    console.log("\ngetIndex");
    searchItem = 99;
    console.log(`  入力値: ${searchItem}`);
    output = arrayData.getIndex(searchItem);
    console.log(`  出力値: ${output}`);

    console.log("\nremove");
    inputIndex = 3;
    console.log(`  入力値: ${inputIndex}`);
    output = arrayData.remove(inputIndex);
    console.log(`  出力値: ${output}`);
    console.log(`  現在のデータ: ${JSON.stringify(arrayData.get())}`);

    console.log("\nremove");
    inputIndex = 8;
    console.log(`  入力値: ${inputIndex}`);
    output = arrayData.remove(inputIndex);
    console.log(`  出力値: ${output}`);
    console.log(`  現在のデータ: ${JSON.stringify(arrayData.get())}`);

    console.log("\nreverse");
    output = arrayData.reverse();
    console.log(`  出力値: ${JSON.stringify(output)}`);

    console.log("\nsort");
    console.log("  入力値: descending=false");
    output = arrayData.sort(false);
    console.log(`  出力値: ${JSON.stringify(output)}`);

    console.log("\nsort");
    console.log("  入力値: descending=true");
    output = arrayData.sort(true);
    console.log(`  出力値: ${JSON.stringify(output)}`);

    console.log("\nclear");
    output = arrayData.clear();
    console.log(`  出力値: ${output}`);
    console.log(`  現在のデータ: ${JSON.stringify(arrayData.get())}`);

    console.log("\nisEmpty");
    output = arrayData.isEmpty();
    console.log(`  出力値: ${output}`);

    console.log("\nArray TEST <----- end");
}

// ブラウザ環境ではwindow.onloadで、Node.js環境では直接実行
if (typeof window !== 'undefined') {
    window.onload = main;
} else {
    main();
}