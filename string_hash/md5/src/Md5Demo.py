# Python
# ハッシュ文字列: MD5

import struct
import math

def md5(message):
    # 初期値（リトルエンディアン形式）
    a0 = 0x67452301
    b0 = 0xEFCDAB89
    c0 = 0x98BADCFE
    d0 = 0x10325476
    
    # シフト量
    s = [
        7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22,
        5,  9, 14, 20, 5,  9, 14, 20, 5,  9, 14, 20, 5,  9, 14, 20,
        4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23,
        6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21
    ]
    
    # 定数テーブル
    K = [int(abs(math.sin(i + 1)) * 2**32) & 0xFFFFFFFF for i in range(64)]
    
    # メッセージの前処理
    # バイト配列に変換
    message_bytes = message.encode('utf-8')
    msg_len_bits = (8 * len(message_bytes)) & 0xffffffffffffffff
    
    # パディング
    message_bytes += b'\x80'
    while len(message_bytes) % 64 != 56:
        message_bytes += b'\x00'
    
    # メッセージ長の追加（64ビット）
    message_bytes += struct.pack('<Q', msg_len_bits)
    
    # 512ビット（64バイト）ブロックごとに処理
    for i in range(0, len(message_bytes), 64):
        block = message_bytes[i:i+64]
        
        # 32ビットワード16個に分解
        M = list(struct.unpack('<16I', block))
        
        # 現在の状態をコピー
        A, B, C, D = a0, b0, c0, d0
        
        # メインループ
        for j in range(64):
            if j <= 15:
                # ラウンド1
                F = (B & C) | ((~B) & D)
                g = j
            elif j <= 31:
                # ラウンド2
                F = (D & B) | ((~D) & C)
                g = (5 * j + 1) % 16
            elif j <= 47:
                # ラウンド3
                F = B ^ C ^ D
                g = (3 * j + 5) % 16
            else:
                # ラウンド4
                F = C ^ (B | (~D))
                g = (7 * j) % 16
            
            # 更新処理
            temp = D
            D = C
            C = B
            B = (B + left_rotate((A + F + K[j] + M[g]) & 0xFFFFFFFF, s[j])) & 0xFFFFFFFF
            A = temp
        
        # 状態の更新
        a0 = (a0 + A) & 0xFFFFFFFF
        b0 = (b0 + B) & 0xFFFFFFFF
        c0 = (c0 + C) & 0xFFFFFFFF
        d0 = (d0 + D) & 0xFFFFFFFF
    
    # リトルエンディアンの16進数文字列に変換して結合
    result = ''.join(format(x, '08x') for x in struct.unpack('<4I', struct.pack('>4I', a0, b0, c0, d0)))
    return result

def left_rotate(x, c):
    return ((x << c) | (x >> (32 - c))) & 0xFFFFFFFF

def main():
    print("md5 TEST -----> start")

    print("\nmd5")
    input = "hello world!"
    output = md5(input)
    print(f"  {input}: {output}")

    print("\nmd5 TEST <----- end")

if __name__ == "__main__":
    main()
