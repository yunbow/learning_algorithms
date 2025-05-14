// JavaScript
// 素数判定: フェルマーテスト (Fermat Test)

// a^b mod m を効率的に計算する関数
function modPow(a, b, m) {
  // JavaScriptには組み込みのmodPow関数がないため実装
  if (m === 1) return 0;
  let result = 1;
  a = a % m;
  while (b > 0) {
    if (b % 2 === 1) {
      result = (result * a) % m;
    }
    b = Math.floor(b / 2);
    a = (a * a) % m;
  }
  return result;
}

function isPrime(target, k = 5) {
  if (target === 2 || target === 3) {
    return true;
  }

  // 2未満または偶数は素数でない（2を除く）
  if (target < 2 || target % 2 === 0) {
    return false;
  }

  for (let i = 0; i < k; i++) {
    // 1 < a < target の範囲からランダムに数を選ぶ
    const a = Math.floor(Math.random() * (target - 3)) + 2;

    // フェルマーの小定理: a^(target-1) ≡ 1 (mod target) が成り立つか検証
    if (modPow(a, target - 1, target) !== 1) {
      return false;
    }
  }

  return true;
}

function main() {
  console.log("FermatTest TEST -----> start");

  console.log("\nisPrime");
  const inputList = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 97, 100, 101, 997];
  for (const input of inputList) {
    const output = isPrime(input);
    console.log(`  ${input}: ${output}`);
  }

  console.log("\nFermatTest TEST <----- end");
}

main();