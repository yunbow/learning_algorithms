# Python
# 素数判定: ソロベイシュトラッセン (Solovay Strassen)
import random

def jacobi_symbol(a, n):
    # ヤコビ記号 (a/n) を計算する
    if a == 0:
        return 0
    if a == 1:
        return 1
    
    if a % 2 == 0:
        return jacobi_symbol(a // 2, n) * pow(-1, (n*n - 1)//8)
    
    if a >= n:
        return jacobi_symbol(a % n, n)
    
    if a % 2 == 1:
        return jacobi_symbol(n, a) * pow(-1, (a-1)*(n-1)//4)

def is_prime(target, k=10):
    # 基本的なケースの処理
    if target == 2 or target == 3:
        return True
    if target <= 1 or target % 2 == 0:
        return False
    
    # 指定した回数だけテストを繰り返す
    for _ in range(k):
        # 1からn-1の範囲でランダムな数aを選ぶ
        a = random.randint(2, target-1)
        
        # GCDが1でなければ、nは合成数
        if pow(a, target-1, target) != 1:
            return False
        
        # ヤコビ記号を計算
        j = jacobi_symbol(a, target)
        if j < 0:
            j += target
            
        # 疑似素数テスト
        if pow(a, (target-1)//2, target) != j % target:
            return False
    
    # すべてのテストをパスすれば、おそらく素数
    return True

def main():
    print("SolovayStrassen TEST -----> start")

    print(f"\nis_prime")
    input_list = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 97, 100, 101, 997]
    for input in input_list:
        output = is_prime(input)
        print(f"  {input}: {output}")
    
    print("\nSolovayStrassen TEST <----- end")

if __name__ == "__main__":
    main()