# Python
# 素数判定: エラトステネスのふるい (Sieve of Eratosthenes)
import math

def is_prime(target):
    # 2未満の数は素数ではない
    if target < 2:
        return False
    # 2は唯一の偶数の素数
    if target == 2:
        return True
    # 2より大きい偶数は素数ではない
    if target % 2 == 0:
        return False

    # エラトステネスのふるいを適用するためのbooleanリストを作成
    # リストのインデックスが数値を表す。is_prime[i] が True なら i は素数の可能性がある。
    # num までの判定を行うため、サイズは num + 1 とする。
    is_prime_list = [True] * (target + 1)

    # 0と1は素数ではない
    is_prime_list[0] = is_prime_list[1] = False

    # ふるいを実行
    # 判定の基となる素数 p は、num の平方根まで調べれば十分
    # num = p * q の場合、p または q の少なくとも一方は sqrt(num) 以下になるため
    limit = int(math.sqrt(target)) + 1

    for p in range(2, limit):
        # もし p が素数の可能性があるなら (まだふるい落とされていなければ)
        if is_prime_list[p]:
            # p の倍数をふるい落とす (p*p から開始するのは、それより小さい倍数は
            # より小さい素数によって既にふるい落とされているため)
            # p*p から num までの p の倍数をすべて False に設定
            for multiple in range(p * p, target + 1, p):
                is_prime_list[multiple] = False

    # ふるい落としが完了したら、判定したい数 num の状態を確認
    return is_prime_list[target]

def main():
    print("SieveOfEratosthenes TEST -----> start")

    print(f"\nis_prime")
    input_list = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 97, 100, 101, 997]
    for input in input_list:
        output = is_prime(input)
        print(f"  {input}: {output}")
    
    print("\nSieveOfEratosthenes TEST <----- end")

if __name__ == "__main__":
    main()