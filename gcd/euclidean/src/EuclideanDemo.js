// JavaScript
// 最大公約数: ユークリッドの互除法 (Euclidean)

function gcd(a, b) {
    // bが0になるまで繰り返し計算する
    while (b) {
        [a, b] = [b, a % b];
    }
    return a;
}

function main() {
    console.log("Euclidean TEST -----> start");
    const input = [48, 18];
    console.log(`\n  入力値: [${input}]`);
    const output = gcd(...input);
    console.log(`\n  出力値: ${output}`);
    console.log("\nEuclidean TEST <----- end");
}

main();