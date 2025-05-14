# Python
# 素数判定: ミラーラビン (Miller-Rabin)

import random

def is_prime(target, k=40):
    # 特殊なケースを処理
    if target <= 1:
        return False
    if target <= 3:
        return True
    if target % 2 == 0:
        return False
    
    # n-1 = 2^r * d の形に分解する（dは奇数）
    r, d = 0, target - 1
    while d % 2 == 0:
        r += 1
        d //= 2
    
    # k回の試行を実行
    for _ in range(k):
        a = random.randint(2, target - 2)
        x = pow(a, d, target)  # a^d mod n を計算
        
        if x == 1 or x == target - 1:
            continue
        
        for _ in range(r - 1):
            x = pow(x, 2, target)
            if x == target - 1:
                break
        else:
            return False  # 合成数と確定
    
    return True  # 素数である可能性が高い

def main():
    print("MillerRabin TEST -----> start")

    print(f"\nis_prime")
    input_list = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 97, 100, 101, 997]
    for input in input_list:
        output = is_prime(input)
        print(f"  {input}: {output}")
    
    print("\nMillerRabin TEST <----- end")

if __name__ == "__main__":
    main()
