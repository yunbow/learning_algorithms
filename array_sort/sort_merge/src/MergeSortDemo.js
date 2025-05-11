// JavaScript
// 配列の並び替え: マージソート (Merge Sort)

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

    _merge_sort(target) {
        // 配列の長さが1以下の場合はそのまま返す（基本ケース）
        if (target.length <= 1) {
            return target;
        }
        
        // 配列を半分に分割
        const mid = Math.floor(target.length / 2);
        const left_half = target.slice(0, mid);
        const right_half = target.slice(mid);
        
        // 左右の半分を再帰的にソート
        const sorted_left = this._merge_sort(left_half);
        const sorted_right = this._merge_sort(right_half);
        
        // ソート済みの半分同士をマージ
        return this._merge(sorted_left, sorted_right);
    }

    _merge(left, right) {
        const result = [];
        let i = 0;
        let j = 0;
        
        // 左右の配列を比較しながらマージ
        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) {
                result.push(left[i]);
                i++;
            } else {
                result.push(right[j]);
                j++;
            }
        }
        
        // 残った要素を追加
        return result.concat(left.slice(i)).concat(right.slice(j));
    }

    sort() {
        this._data = this._merge_sort(this._data);
        return true;
    }
}

function main() {
    console.log("MergeSort TEST -----> start");

    const array_data = new ArrayData();

    // ランダムな整数の配列
    console.log("\nsort");
    let input = [64, 34, 25, 12, 22, 11, 90];
    console.log(`  ソート前: ${input}`);
    array_data.set(input);
    array_data.sort();
    console.log(`  ソート後: ${array_data.get()}`);
    
    // 既にソートされている配列
    console.log("\nsort");
    input = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
    console.log(`  ソート前: ${input}`);
    array_data.set(input);
    array_data.sort();
    console.log(`  ソート後: ${array_data.get()}`);
    
    // 逆順の配列
    console.log("\nsort");
    input = [10, 9, 8, 7, 6, 5, 4, 3, 2, 1];
    console.log(`  ソート前: ${input}`);
    array_data.set(input);
    array_data.sort();
    console.log(`  ソート後: ${array_data.get()}`);
    
    // 重複要素を含む配列
    console.log("\nsort");
    input = [10, 9, 8, 7, 6, 10, 9, 8, 7, 6];
    console.log(`  ソート前: ${input}`);
    array_data.set(input);
    array_data.sort();
    console.log(`  ソート後: ${array_data.get()}`);
    
    // 空の配列
    console.log("\nsort");
    input = [];
    console.log(`  ソート前: ${input}`);
    array_data.set(input);
    array_data.sort();
    console.log(`  ソート後: ${array_data.get()}`);

    console.log("\nMergeSort TEST <----- end");
}

main();