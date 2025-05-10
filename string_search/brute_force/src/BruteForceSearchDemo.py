# Python
# 文字列の検索: ブルートフォース法 (Brute Force Search)

def search(text, pattern):
    n = len(text)
    m = len(pattern)
    positions = []
    
    # パターンが空または検索対象より長い場合
    if m == 0 or m > n:
        return positions
    
    # テキスト内の各位置でパターンとの一致を確認
    for i in range(n - m + 1):
        j = 0
        # パターンの各文字を確認
        while j < m and text[i + j] == pattern[j]:
            j += 1
        # パターンが完全に一致した場合
        if j == m:
            positions.append(i)
    
    return positions

def main():
    print("BruteForceSearch TEST -----> start")

    print("\nsearch")
    input = ("ABABCABCABABABD", "ABABD")
    print(f"  入力値: {input}")
    output = search(*input)
    print(f"  出力値: {output}")

    print("\nsearch")
    input = ("AAAAAA", "AA")
    print(f"  入力値: {input}")
    output = search(*input)
    print(f"  出力値: {output}")

    print("\nsearch")
    input = ("ABCDEFG", "XYZ")
    print(f"  入力値: {input}")
    output = search(*input)
    print(f"  出力値: {output}")

    print("\nsearch")
    input = ("ABCABC", "ABC")
    print(f"  入力値: {input}")
    output = search(*input)
    print(f"  出力値: {output}")

    print("\nsearch")
    input = ("ABC", "")
    print(f"  入力値: {input}")
    output = search(*input)
    print(f"  出力値: {output}")

    print("\nBruteForceSearch TEST <----- end")

if __name__ == "__main__":
    main()