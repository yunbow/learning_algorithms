# Python
# 文字列の検索: ボイヤムーア法 (Boyer Moore Search)

def search(text, pattern):
    if not pattern or not text or len(pattern) > len(text):
        return []
    
    # 結果を格納するリスト
    occurrences = []
    
    # 悪文字ルールのテーブル作成
    bad_char = bad_character_table(pattern)
    
    # 良接尾辞ルールのテーブル作成
    good_suffix = good_suffix_table(pattern)
    
    # 検索
    i = len(pattern) - 1  # テキスト内の位置
    while i < len(text):
        j = len(pattern) - 1  # パターン内の位置
        while j >= 0 and text[i - len(pattern) + 1 + j] == pattern[j]:
            j -= 1
        
        if j < 0:  # マッチした場合
            occurrences.append(i - len(pattern) + 1)
            i += 1
        else:  # マッチしなかった場合
            # 悪文字ルールと良接尾辞ルールのシフト量の大きい方を採用
            bc_shift = max(1, j - bad_char.get(text[i], -1))
            gs_shift = good_suffix[j]
            i += max(bc_shift, gs_shift)

    return occurrences

def bad_character_table(pattern):
    # 悪文字ルールのテーブルを作成
    bad_char = {}
    for i in range(len(pattern)):
        bad_char[pattern[i]] = i
    return bad_char

def good_suffix_table(pattern):
    # 良接尾辞ルールのテーブルを作成
    n = len(pattern)
    # ボーダー配列の計算
    border = [0] * (n + 1)
    border[n] = n
    
    # 補助関数：Z配列の計算
    def z_array(pattern):
        n = len(pattern)
        z = [0] * n
        l, r = 0, 0
        for i in range(1, n):
            if i <= r:
                z[i] = min(r - i + 1, z[i - l])
            while i + z[i] < n and pattern[z[i]] == pattern[i + z[i]]:
                z[i] += 1
            if i + z[i] - 1 > r:
                l, r = i, i + z[i] - 1
        return z
    
    # パターンの逆順に対するZ配列
    pattern_rev = pattern[::-1]
    z = z_array(pattern_rev)
    
    for i in range(n):
        j = n - z[i]
        border[j] = i
    
    # 良接尾辞ルールのシフト量計算
    shift = [0] * n
    for i in range(n):
        shift[i] = n
    
    for i in range(n):
        j = n - border[i]
        shift[n - j - 1] = j
    
    # ボーダーが存在しない場合の処理
    for i in range(n - 1):
        j = n - 1 - i
        if border[j] == j:
            for k in range(n - j):
                if shift[k] == n:
                    shift[k] = n - j
    
    return shift

def main():
    print("BoyerMooreSearch TEST -----> start")

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

    print("\nBoyerMooreSearch TEST <----- end")

if __name__ == "__main__":
    main()