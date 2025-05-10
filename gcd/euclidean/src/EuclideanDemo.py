# Python
# 最大公約数: ユークリッドの互除法 (Euclidean)

def gcd(a, b):
    # bが0になるまで繰り返し計算する
    while b:
        a, b = b, a % b
    return a

def main():
    print("Euclidean TEST -----> start")
    input = (48, 18)
    print(f"\n  入力値: {input}")
    output = gcd(*input)
    print(f"\n  出力値: {output}")
    print("\nEuclidean TEST <----- end")

if __name__ == "__main__":
    main()
