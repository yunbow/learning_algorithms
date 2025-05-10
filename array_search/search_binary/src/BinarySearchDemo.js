// JavaScript
// 配列の検索: 二分探索 (Binary Search)

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

    search(target) {
        let left = 0;
        let right = this._data.length - 1;
        
        while (left <= right) {
            const mid = Math.floor((left + right) / 2);
            
            // 中央の要素が目標値と一致
            if (this._data[mid] === target) {
                return mid;
            }
            
            // 中央の要素が目標値より小さい場合、右半分を探索
            else if (this._data[mid] < target) {
                left = mid + 1;
            }
            
            // 中央の要素が目標値より大きい場合、左半分を探索
            else {
                right = mid - 1;
            }
        }
        
        // 目標値が見つからない場合
        return -1;
    }
}

function main() {
    console.log("BinarySearch TEST -----> start");

    console.log("\nnew");
    const arrayData = new ArrayData();
    const input = [1, 3, 5, 7, 9, 11, 13, 15, 17, 19];
    arrayData.set(input);
    console.log(`  現在のデータ: ${arrayData.get()}`);
    
    console.log("\nsearch");
    const searchValue1 = 7;
    console.log(`  入力値: ${searchValue1}`);
    const output1 = arrayData.search(searchValue1);
    console.log(`  出力値: ${output1}`);

    console.log("\nsearch");
    const searchValue2 = 30;
    console.log(`  入力値: ${searchValue2}`);
    const output2 = arrayData.search(searchValue2);
    console.log(`  出力値: ${output2}`);

    console.log("\nBinarySearch TEST <----- end");
}

main();