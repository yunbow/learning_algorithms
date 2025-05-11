// JavaScript
// 配列の並び替え: バブルソート (Bubble Sort)

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
    const n = this._data.length;
    
    // 外側のループ: n-1回の走査が必要
    for (let i = 0; i < n; i++) {
      // 最適化: 一度の走査で交換がなければソート完了
      let swapped = false;
      
      // 内側のループ: まだソートされていない部分を走査
      // 各走査後に最大の要素が末尾に移動するため、i回分を除外
      for (let j = 0; j < n - i - 1; j++) {
        // 隣接する要素を比較し、必要に応じて交換
        if (this._data[j] > this._data[j + 1]) {
          [this._data[j], this._data[j + 1]] = [this._data[j + 1], this._data[j]];
          swapped = true;
        }
      }
      
      // 交換が発生しなければソート完了
      if (!swapped) {
        break;
      }
    }
    return true;
  }
}

function main() {
  console.log("BubbleSort TEST -----> start");

  const arrayData = new ArrayData();

  // ランダムな整数の配列
  console.log("\nsort");
  let input = [64, 34, 25, 12, 22, 11, 90];
  console.log(`  ソート前: ${input}`);
  arrayData.set(input);
  arrayData.sort();
  console.log(`  ソート後: ${arrayData.get()}`);
  
  // 既にソートされている配列
  console.log("\nsort");
  input = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
  console.log(`  ソート前: ${input}`);
  arrayData.set(input);
  arrayData.sort();
  console.log(`  ソート後: ${arrayData.get()}`);
  
  // 逆順の配列
  console.log("\nsort");
  input = [10, 9, 8, 7, 6, 5, 4, 3, 2, 1];
  console.log(`  ソート前: ${input}`);
  arrayData.set(input);
  arrayData.sort();
  console.log(`  ソート後: ${arrayData.get()}`);
  
  // 重複要素を含む配列
  console.log("\nsort");
  input = [10, 9, 8, 7, 6, 10, 9, 8, 7, 6];
  console.log(`  ソート前: ${input}`);
  arrayData.set(input);
  arrayData.sort();
  console.log(`  ソート後: ${arrayData.get()}`);
  
  // 空の配列
  console.log("\nsort");
  input = [];
  console.log(`  ソート前: ${input}`);
  arrayData.set(input);
  arrayData.sort();
  console.log(`  ソート後: ${arrayData.get()}`);

  console.log("\nBubbleSort TEST <----- end");
}

main();