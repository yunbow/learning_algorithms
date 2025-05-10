# Python
# 素数判定: フェルマーテスト (Fermat Test)
import random

def is_prime(target, k=5):
    if target == 2 or target == 3:
        return True

    # 2未満または偶数は素数でない（2を除く）
    if target < 2 or target % 2 == 0:
        return False

    for _ in range(k):
        # 1 < a < target の範囲からランダムに数を選ぶ
        a = random.randint(2, target - 2)

        # フェルマーの小定理: a^(target-1) ≡ 1 (mod target) が成り立つか検証
        # pow(a, target - 1, target) は (a^(target-1)) % target を効率的に計算します。
        if pow(a, target - 1, target) != 1:
            return False

    return True

def main():
    print("FermatTest TEST -----> start")

    print(f"\nis_prime")
    input_list = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 97, 100, 101, 997]
    for input in input_list: 
        output = is_prime(input)
        print(f"  {input}: {output}")

    print("\nFermatTest TEST <----- end")

if __name__ == "__main__":
    main()