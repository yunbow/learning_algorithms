// JavaScript
// 配列の並び替え: 挿入ソート (Insertion Sort)

class ArrayData {
    constructor() {
        this._data = [];
    }

    get() {
        return this._data;
    }

    set(data) {
        this._data = data;
        return true;
    }

    sort() {
        // 配列の長さを取得
        const n = this._data.length;
        
        // 2番目の要素から始める（最初の要素は既にソート済みと見なす）
        for (let i = 1; i < n; i++) {
            // 現在の要素を取得
            const key = this._data[i];
            
            // ソート済み部分の最後の要素のインデックス
            let j = i - 1;
            
            // keyより大きい要素をすべて右にシフト
            while (j >= 0 && this._data[j] > key) {
                this._data[j + 1] = this._data[j];
                j--;
            }
            
            // 適切な位置にkeyを挿入
            this._data[j + 1] = key;
        }
        
        return true;
    }
}

function main() {
    console.log("InsertionSort TEST -----> start");

    const array_data = new ArrayData();

    // ランダムな整数の配列
    console.log("\nsort");
    let input = [64, 34, 25, 12, 22, 11, 90];
    console.log(`  ソート前: ${JSON.stringify(input)}`);
    array_data.set(input);
    array_data.sort();
    console.log(`  ソート後: ${JSON.stringify(array_data.get())}`);
    
    // 既にソートされている配列
    console.log("\nsort");
    input = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
    console.log(`  ソート前: ${JSON.stringify(input)}`);
    array_data.set(input);
    array_data.sort();
    console.log(`  ソート後: ${JSON.stringify(array_data.get())}`);
    
    // 逆順の配列
    console.log("\nsort");
    input = [10, 9, 8, 7, 6, 5, 4, 3, 2, 1];
    console.log(`  ソート前: ${JSON.stringify(input)}`);
    array_data.set(input);
    array_data.sort();
    console.log(`  ソート後: ${JSON.stringify(array_data.get())}`);
    
    // 重複要素を含む配列
    console.log("\nsort");
    input = [10, 9, 8, 7, 6, 10, 9, 8, 7, 6];
    console.log(`  ソート前: ${JSON.stringify(input)}`);
    array_data.set(input);
    array_data.sort();
    console.log(`  ソート後: ${JSON.stringify(array_data.get())}`);
    
    // 空の配列
    console.log("\nsort");
    input = [];
    console.log(`  ソート前: ${JSON.stringify(input)}`);
    array_data.set(input);
    array_data.sort();
    console.log(`  ソート後: ${JSON.stringify(array_data.get())}`);

    console.log("\nInsertionSort TEST <----- end");
}

// Node.jsでの実行のために、mainを直接呼び出す
main();