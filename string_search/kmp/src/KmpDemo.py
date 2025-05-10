# Python
# 文字列の検索: KMP (Knuth Morris Pratt)

def compute_lps(pattern):
    m = len(pattern)
    lps = [0] * m  # LPSテーブルを初期化
    
    # 最初のインデックスのLPS値は常に0
    length = 0
    i = 1
    
    # LPSテーブルの残りの値を計算
    while i < m:
        if pattern[i] == pattern[length]:
            # 文字が一致する場合、lengthをインクリメントしてlps[i]に保存
            length += 1
            lps[i] = length
            i += 1
        else:
            # 文字が一致しない場合
            if length != 0:
                # 一致した部分文字列の前の位置のLPS値を使用
                length = lps[length - 1]
                # iはインクリメントしない
            else:
                # lenght = 0の場合、lps[i] = 0として次に進む
                lps[i] = 0
                i += 1
    
    return lps

def search(text, pattern):
    if not pattern or not text:
        return []
    
    n = len(text)
    m = len(pattern)
    
    # パターン長がテキスト長より大きい場合、マッチングは不可能
    if m > n:
        return []
    
    # LPSテーブルを計算
    lps = compute_lps(pattern)
    
    result = []  # マッチした位置のリスト
    i = 0  # テキストのインデックス
    j = 0  # パターンのインデックス
    
    while i < n:
        # 現在の文字が一致する場合
        if pattern[j] == text[i]:
            i += 1
            j += 1
        
        # パターン全体がマッチした場合
        if j == m:
            # パターンの開始位置をresultに追加
            result.append(i - j)
            # 次の可能性のある一致を探すために、jpをLPS[j-1]に設定
            j = lps[j - 1]
        # 文字が一致しない場合
        elif i < n and pattern[j] != text[i]:
            # jが0でない場合、LPSテーブルを使って次の位置を決定
            if j != 0:
                j = lps[j - 1]
            else:
                # jが0の場合、単純にテキストの次の文字に進む
                i += 1
    
    return result

def main():
    print("Kmp TEST -----> start")

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

    print("\nKmp TEST <----- end")

if __name__ == "__main__":
    main()
