// JavaScript
// 配列の検索: 線形探索 (Linear Search)

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
    // 配列の要素を順番に確認
    for (let i = 0; i < this._data.length; i++) {
      // 目的の値が見つかった場合、そのインデックスを返す
      if (this._data[i] === target) {
        return i;
      }
    }
    
    // 見つからなかった場合は -1 を返す
    return -1;
  }
}

function main() {
  console.log("LinearSearch TEST -----> start");

  console.log("\nnew");
  const arrayData = new ArrayData();
  const input = [1, 3, 5, 7, 9, 11, 13, 15, 17, 19];
  arrayData.set(input);
  console.log(`  現在のデータ: ${arrayData.get()}`);
  
  console.log("\nsearch");
  let searchInput = 7;
  console.log(`  入力値: ${searchInput}`);
  let output = arrayData.search(searchInput);
  console.log(`  出力値: ${output}`);

  console.log("\nsearch");
  searchInput = 30;
  console.log(`  入力値: ${searchInput}`);
  output = arrayData.search(searchInput);
  console.log(`  出力値: ${output}`);

  console.log("\nLinearSearch TEST <----- end");
}

// JavaScriptではmain関数を明示的に呼び出す
main();