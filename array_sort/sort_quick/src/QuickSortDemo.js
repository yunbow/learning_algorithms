// JavaScript
// 配列の並び替え: クイックソート (Quick Sort)

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

    _quickSort(target) {
        // 空の配列または要素が1つの場合はそのまま返す（基底条件）
        if (target.length <= 1) {
            return target;
        }
        
        // ピボットを選択（この実装では最後の要素を選択）
        const pivot = target[target.length - 1];
        
        // ピボットより小さい要素と大きい要素に分ける
        const left = [];
        const right = [];
        
        // 最後の要素（ピボット）を除いて配列をスキャン
        for (let i = 0; i < target.length - 1; i++) {
            if (target[i] <= pivot) {
                left.push(target[i]);
            } else {
                right.push(target[i]);
            }
        }
        
        // 左側の部分配列、ピボット、右側の部分配列を再帰的にソートして結合
        return [...this._quickSort(left), pivot, ...this._quickSort(right)];
    }

    sort() {
        this._data = this._quickSort(this._data);
        return true;
    }
}

function main() {
    console.log("QuickSort TEST -----> start");

    const arrayData = new ArrayData();

    // ランダムな整数の配列
    console.log("\nsort");
    let input = [64, 34, 25, 12, 22, 11, 90];
    console.log(`  ソート前: ${JSON.stringify(input)}`);
    arrayData.set(input);
    arrayData.sort();
    console.log(`  ソート後: ${JSON.stringify(arrayData.get())}`);
    
    // 既にソートされている配列
    console.log("\nsort");
    input = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
    console.log(`  ソート前: ${JSON.stringify(input)}`);
    arrayData.set(input);
    arrayData.sort();
    console.log(`  ソート後: ${JSON.stringify(arrayData.get())}`);
    
    // 逆順の配列
    console.log("\nsort");
    input = [10, 9, 8, 7, 6, 5, 4, 3, 2, 1];
    console.log(`  ソート前: ${JSON.stringify(input)}`);
    arrayData.set(input);
    arrayData.sort();
    console.log(`  ソート後: ${JSON.stringify(arrayData.get())}`);
    
    // 重複要素を含む配列
    console.log("\nsort");
    input = [10, 9, 8, 7, 6, 10, 9, 8, 7, 6];
    console.log(`  ソート前: ${JSON.stringify(input)}`);
    arrayData.set(input);
    arrayData.sort();
    console.log(`  ソート後: ${JSON.stringify(arrayData.get())}`);
    
    // 空の配列
    console.log("\nsort");
    input = [];
    console.log(`  ソート前: ${JSON.stringify(input)}`);
    arrayData.set(input);
    arrayData.sort();
    console.log(`  ソート後: ${JSON.stringify(arrayData.get())}`);

    console.log("\nQuickSort TEST <----- end");
}

main();