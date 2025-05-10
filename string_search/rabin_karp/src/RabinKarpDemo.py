# Python
# 文字列の検索: ラビンカープ法 (Rabin Karp)

def search(text, pattern):
    n = len(text)
    m = len(pattern)
    
    # パターンが文字列より長い場合は検索できない
    if m > n:
        return []
    
    # ハッシュ計算のための定数
    # 大きな素数を使用
    q = 101
    d = 256  # 文字セットのサイズ（ASCIIを想定）
    
    # パターンとテキスト最初のm文字のハッシュ値を計算
    pattern_hash = 0
    text_hash = 0
    h = 1
    
    # h = d^(m-1) mod q の計算
    for i in range(m-1):
        h = (h * d) % q
    
    # パターンと初期ウィンドウのハッシュ値を計算
    for i in range(m):
        pattern_hash = (d * pattern_hash + ord(pattern[i])) % q
        text_hash = (d * text_hash + ord(text[i])) % q
    
    result = []
    
    # テキスト内を順に探索
    for i in range(n - m + 1):
        # ハッシュ値が一致した場合、文字ごとに比較して確認
        if pattern_hash == text_hash:
            # 文字ごとのチェック
            match = True
            for j in range(m):
                if text[i+j] != pattern[j]:
                    match = False
                    break
            
            if match:
                result.append(i)
        
        # 次のウィンドウのハッシュ値を計算
        if i < n - m:
            # 先頭の文字を削除
            text_hash = (d * (text_hash - ord(text[i]) * h) + ord(text[i + m])) % q
            
            # 負の値になる場合は調整
            if text_hash < 0:
                text_hash += q
    
    return result

def main():
    print("RabinKarp TEST -----> start")

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

    print("\nRabinKarp TEST <----- end")

if __name__ == "__main__":
    main()