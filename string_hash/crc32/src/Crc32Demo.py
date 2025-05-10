# Python
# ハッシュ文字列: CRC32

def crc32(data):
    # 文字列の場合はバイト列に変換
    if isinstance(data, str):
        data = data.encode('utf-8')
    
    # CRC32の多項式
    polynomial = 0xEDB88320
    
    # CRCの初期値
    crc = 0xFFFFFFFF
    
    # バイトごとに処理
    for byte in data:
        crc ^= byte
        
        # 8ビット分処理
        for _ in range(8):
            # 最下位ビットが1なら多項式でXOR
            if crc & 1:
                crc = (crc >> 1) ^ polynomial
            else:
                crc >>= 1
    
    # 最終的な値を返す（ビット反転して符号なしに）
    return (crc ^ 0xFFFFFFFF) & 0xFFFFFFFF

def main():
    print("crc32 TEST -----> start")

    print("\ncrc32")
    input = "hello world!"
    output = crc32(input)
    print(f"  {input}: {output}")

    print("\ncrc32 TEST <----- end")

if __name__ == "__main__":
    main()
