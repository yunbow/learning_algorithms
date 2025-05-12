// JavaScript
// データ構造: セット (Set)

class SetData {
    constructor() {
        this._data = [];
    }

    get() {
        // 要素を取得
        return this._data;
    }

    getIndex(item) {
        // 指定された要素がセット内に存在するかどうかをチェックする。
        const index = this._data.indexOf(item);
        if (index !== -1) {
            return index;
        } else {
            console.log(`ERROR: ${item} は範囲外です`);
            return -1;
        }
    }

    getValue(index) {
        // 指定されたインデックスの要素を取得する。
        if (index >= 0 && index < this._data.length) {
            return this._data[index];
        } else {
            console.log(`ERROR: ${index} は範囲外です`);
            return null;
        }
    }

    add(item) {
        // 要素をセットに追加する。
        if (!this._data.includes(item)) {
            this._data.push(item);
            return true;
        } else {
            console.log(`ERROR: ${item} は重複です`);
            return false;
        }
    }

    remove(item) {
        // 指定された要素をセットから削除する。
        const index = this._data.indexOf(item);
        if (index !== -1) {
            this._data.splice(index, 1);
            return true;
        } else {
            console.log(`ERROR: ${item} は範囲外です`);
            return false;
        }
    }

    isEmpty() {
        // 空かどうかをチェックする
        return this._data.length === 0;
    }

    size() {
        // 要素数を返す
        return this._data.length;
    }

    clear() {
        // 空にする
        this._data = [];
        return true;
    }
}

function main() {
    console.log("Set TEST -----> start");

    console.log("\nnew");
    const setData = new SetData();
    console.log(`  現在のデータ: ${setData.get()}`);

    console.log("\nadd");
    const input = [10, 20, 30, 20, 40];
    for (const item of input) {
        console.log(`  入力値: ${item}`);
        const output = setData.add(item);
        console.log(`  出力値: ${output}`);
        console.log(`  現在のデータ: ${setData.get()}`);
    }

    console.log("\nsize");
    const sizeOutput = setData.size();
    console.log(`  出力値: ${sizeOutput}`);

    console.log("\nis_empty");
    const isEmptyOutput = setData.isEmpty();
    console.log(`  出力値: ${isEmptyOutput}`);

    console.log("\nget_value");
    const getValueInput = [0, 2, 5];
    for (const index of getValueInput) {
        console.log(`  入力値: ${index}`);
        const output = setData.getValue(index);
        console.log(`  出力値: ${output}`);
    }

    console.log("\nget_index");
    const getIndexInput = [30, 99];
    for (const item of getIndexInput) {
        console.log(`  入力値: ${item}`);
        const output = setData.getIndex(item);
        console.log(`  出力値: ${output}`);
    }

    console.log("\nremove");
    const removeInput = [20, 50, 10];
    for (const item of removeInput) {
        console.log(`  入力値: ${item}`);
        const output = setData.remove(item);
        console.log(`  出力値: ${output}`);
        console.log(`  現在のデータ: ${setData.get()}`);
    }

    console.log("\nsize");
    const finalSizeOutput = setData.size();
    console.log(`  出力値: ${finalSizeOutput}`);

    console.log("\nclear");
    const clearOutput = setData.clear();
    console.log(`  出力値: ${clearOutput}`);
    console.log(`  現在のデータ: ${setData.get()}`);

    console.log("\nis_empty");
    const finalIsEmptyOutput = setData.isEmpty();
    console.log(`  出力値: ${finalIsEmptyOutput}`);

    console.log("\nSet TEST <----- end");
}

main();