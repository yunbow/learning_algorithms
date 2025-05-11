// JavaScript
// 配列の並び替え: 選択ソート (Selection Sort)

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

  _selection_sort(target) {
    // 配列の長さを取得
    const n = target.length;
    
    // 配列を順番に走査
    for (let i = 0; i < n; i++) {
      // 未ソート部分の最小値のインデックスを見つける
      let min_index = i;
      for (let j = i + 1; j < n; j++) {
        if (target[j] < target[min_index]) {
          min_index = j;
        }
      }
      
      // 見つかった最小値と現在の位置を交換
      [target[i], target[min_index]] = [target[min_index], target[i]];
    }
    
    return target;
  }

  sort() {
    this._selection_sort(this._data);
    return true;
  }
}

function main() {
  console.log("SelectionSort TEST -----> start");

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

  console.log("\nSelectionSort TEST <----- end");
}

// Node.js環境ではmain関数を直接実行
// ブラウザ環境では、HTMLから呼び出すことを想定
if (typeof require !== 'undefined') {
  main();
}