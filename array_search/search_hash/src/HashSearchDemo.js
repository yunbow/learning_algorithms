// JavaScript
// 配列の検索: ハッシュ探索 (Hash Search)

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
    // ハッシュテーブルの作成
    const hashTable = {};
    
    // 配列の要素をハッシュテーブルに格納
    // キーを要素の値、値をインデックスとする
    this._data.forEach((value, index) => {
      hashTable[value] = index;
    });
    
    // ハッシュテーブルを使って検索
    if (target in hashTable) {
      return hashTable[target];
    } else {
      return -1;
    }
  }
}

function main() {
  console.log("HashSearch TEST -----> start");

  console.log("\nnew");
  const arrayData = new ArrayData();
  let input = [1, 3, 5, 7, 9, 11, 13, 15, 17, 19];
  arrayData.set(input);
  console.log(`  現在のデータ: ${arrayData.get()}`);    

  console.log("\nsearch");
  input = 7;
  console.log(`  入力値: ${input}`);
  let output = arrayData.search(input);
  console.log(`  出力値: ${output}`);

  console.log("\nsearch");
  input = 30;
  console.log(`  入力値: ${input}`);
  output = arrayData.search(input);
  console.log(`  出力値: ${output}`);

  console.log("\nHashSearch TEST <----- end");
}

main();