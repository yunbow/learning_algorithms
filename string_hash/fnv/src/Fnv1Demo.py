# Python
# ハッシュ文字列: FNV1

def fnv1_hash(data, bits=32):
    if isinstance(data, str):
        # Unicode文字列をUTF-8でエンコード
        data = data.encode('utf-8')
    
    if bits == 32:
        # 32ビット FNV-1 の値
        FNV_prime = 16777619
        hash_val = 2166136261  # 32ビットオフセットベース
    elif bits == 64:
        # 64ビット FNV-1 の値
        FNV_prime = 1099511628211
        hash_val = 14695981039346656037  # 64ビットオフセットベース
    else:
        raise ValueError("ビット数は32または64のみサポートされています")
    
    # FNV-1 アルゴリズムの実行
    for byte in data:
        hash_val = hash_val * FNV_prime
        hash_val = hash_val ^ byte
        
        # ビット長に合わせてマスク
        if bits == 32:
            hash_val = hash_val & 0xffffffff
    
    return hash_val

def main():
    print("FNV1 TEST -----> start")

    print("\nfnv1_hash 32")
    input = "hello world!"
    output = fnv1_hash(input)
    print(f"  {input}: {output}")

    print("\nfnv1_hash 64")
    input = "hello world!"
    output = fnv1_hash(input, bits=64)
    print(f"  {input}: {output}")

    print("\nFNV1 TEST <----- end")

if __name__ == "__main__":
    main()
