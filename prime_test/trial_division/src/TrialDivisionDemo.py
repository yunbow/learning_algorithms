# Python
# 素数判定: 試し割り法 (Trial Division)
import math

def is_prime(target):
    # 2未満の数は素数ではない
    if target < 2:
        return False
    
    # 2は素数
    if target == 2:
        return True
    
    # 偶数は2以外素数ではない
    if target % 2 == 0:
        return False
    
    # 3から√nまでの奇数で割り切れるかチェック
    # √nまでで十分な理由：nが合成数なら、n = a×b と表せる
    # a,b≧√n とすると a×b≧n となり矛盾するので、a,bの少なくとも一方は√n以下
    sqrt_n = int(math.sqrt(target)) + 1
    
    for i in range(3, sqrt_n, 2):
        if target % i == 0:
            return False
    
    return True

def main():
    print("TrialDivision TEST -----> start")

    print(f"\nis_prime")
    input_list = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 97, 100, 101, 997]
    for input in input_list:
        output = is_prime(input)
        print(f"  {input}: {output}")
    
    print("\nTrialDivision TEST <----- end")

if __name__ == "__main__":
    main()