// JavaScript
// 素数判定: ミラーラビン (Miller-Rabin)

function isPrime(target, k = 40) {
  // 特殊なケースを処理
  if (target <= 1) {
    return false;
  }
  if (target <= 3) {
    return true;
  }
  if (target % 2 === 0) {
    return false;
  }
  
  // n-1 = 2^r * d の形に分解する（dは奇数）
  let r = 0;
  let d = target - 1;
  while (d % 2 === 0) {
    r += 1;
    d = Math.floor(d / 2);
  }
  
  // k回の試行を実行
  for (let i = 0; i < k; i++) {
    const a = getRandomInt(2, target - 2);
    let x = modPow(a, d, target);  // a^d mod n を計算
    
    if (x === 1 || x === target - 1) {
      continue;
    }
    
    let j;
    for (j = 0; j < r - 1; j++) {
      x = modPow(x, 2, target);
      if (x === target - 1) {
        break;
      }
    }
    
    if (j === r - 1) {
      return false;  // 合成数と確定
    }
  }
  
  return true;  // 素数である可能性が高い
}

// Math.powはモジュロ計算に対応していないため、独自実装
function modPow(base, exponent, modulus) {
  if (modulus === 1) return 0;
  
  let result = 1;
  base = base % modulus;
  
  while (exponent > 0) {
    if (exponent % 2 === 1) {
      result = (result * base) % modulus;
    }
    exponent = Math.floor(exponent / 2);
    base = (base * base) % modulus;
  }
  
  return result;
}

// min以上max以下の整数の乱数を生成
function getRandomInt(min, max) {
  min = Math.ceil(min);
  max = Math.floor(max);
  return Math.floor(Math.random() * (max - min + 1)) + min;
}

function main() {
  console.log("MillerRabin TEST -----> start");

  console.log("\nisPrime");
  const inputList = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 97, 100, 101, 997];
  for (const input of inputList) {
    const output = isPrime(input);
    console.log(`  ${input}: ${output}`);
  }
  
  console.log("\nMillerRabin TEST <----- end");
}

// Node.jsの場合は直接実行、ブラウザの場合はwindow.onloadなどで呼び出す
if (typeof require !== 'undefined') {
  main();
} else {
  // ブラウザ環境
  window.onload = main;
}