# Python
# ハッシュ文字列: SHA

import hashlib

def sha1_hash(data):
    # SHA-1ハッシュ関数
    # 入力が文字列ならUTF-8でエンコード
    if isinstance(data, str):
        data = data.encode('utf-8')
    
    # SHA-1ハッシュオブジェクト作成
    sha1 = hashlib.sha1()
    # データを更新
    sha1.update(data)
    # 16進数文字列で取得
    return sha1.hexdigest()

def sha256_hash(data):
    # SHA-2ファミリーのSHA-256ハッシュ関数
    if isinstance(data, str):
        data = data.encode('utf-8')
    
    sha256 = hashlib.sha256()
    sha256.update(data)
    return sha256.hexdigest()

def sha3_256_hash(data):
    # SHA-3ファミリーのSHA3-256ハッシュ関数
    if isinstance(data, str):
        data = data.encode('utf-8')
    
    sha3 = hashlib.sha3_256()
    sha3.update(data)
    return sha3.hexdigest()

def main():
    print("SHA TEST -----> start")

    print("\nsha1_hash")
    input = "hello world!"
    output = sha1_hash(input)
    print(f"  {input}: {output}")

    print("\nsha256_hash")
    input = "hello world!"
    output = sha256_hash(input)
    print(f"  {input}: {output}")

    print("\nsha3_256_hash")
    input = "hello world!"
    output = sha3_256_hash(input)
    print(f"  {input}: {output}")

    print("\nSHA TEST <----- end")

if __name__ == "__main__":
    main()