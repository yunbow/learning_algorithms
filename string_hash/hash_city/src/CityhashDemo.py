# Python implementation of CityHash algorithm

def uint32(x):
    return x & 0xFFFFFFFF

def uint64(x):
    return x & 0xFFFFFFFFFFFFFFFF

def rotate(val, shift):
    return uint64((val >> shift) | (val << (64 - shift)))

def hash_len_16(u, v, mul=0x9ddfea08eb382d69):
    a = uint64((u ^ v) * mul)
    a ^= (a >> 47)
    b = uint64((v ^ a) * mul)
    b ^= (b >> 47)
    b = uint64(b * mul)
    return b

def shift_mix(val):
    return uint64(val ^ (val >> 47))

def fetch64(s, pos):
    return int.from_bytes(s[pos:pos+8], byteorder='little')

def fetch32(s, pos):
    return int.from_bytes(s[pos:pos+4], byteorder='little')

def city_murmur(s, len_s):
    a = fetch64(s, 0)
    b = fetch64(s, 8)
    c = fetch64(s, len_s - 8)
    d = fetch64(s, len_s - 16)
    e = fetch64(s, (len_s - 16) // 2)
    
    a = uint64(a * 0x9ddfea08eb382d69)
    b = uint64(b * 0x9ddfea08eb382d69)
    c = uint64(c * 0x9ddfea08eb382d69)
    
    a = uint64(a ^ (rotate(b ^ c, 43) + rotate(b, 30) + d))
    a = uint64(a * 0x9ddfea08eb382d69)
    a = uint64(a + e)
    a = uint64(a ^ (rotate(a, 43) + rotate(a, 15) + c))
    
    return a

def city_hash_64(s):
    len_s = len(s)
    
    if len_s <= 32:
        if len_s <= 16:
            if len_s >= 8:
                a = fetch64(s, 0)
                b = fetch64(s, len_s - 8)
                return hash_len_16(a, uint64(b + len_s), 0x9ddfea08eb382d69)
            elif len_s >= 4:
                a = fetch32(s, 0)
                b = fetch32(s, len_s - 4)
                return hash_len_16(uint64(len_s + (a << 3)), uint64(b << 3), 0x9ddfea08eb382d69)
            elif len_s > 0:
                a = s[0]
                b = s[len_s >> 1]
                c = s[len_s - 1]
                y = uint32(a + (b << 8))
                z = uint32(len_s + (c << 2))
                return shift_mix(uint64(y * 0x9ddfea08eb382d69) ^ uint64(z * 0x9ddfea08eb382d69)) * 0x9ddfea08eb382d69
            return 0x9ddfea08eb382d69
        
        # For 17-32 bytes
        a = fetch64(s, 0)
        b = fetch64(s, 8)
        c = fetch64(s, len_s - 8)
        d = fetch64(s, len_s - 16)
        return hash_len_16(
            uint64(a ^ c), uint64(b ^ d), 0x9ddfea08eb382d69
        )
    
    # For 33+ bytes
    a = fetch64(s, 0)
    b = fetch64(s, 8)
    c = fetch64(s, len_s - 8)
    d = fetch64(s, len_s - 16)
    e = fetch64(s, 16)
    f = fetch64(s, 24)
    h = uint64(len_s)
    
    g = c
    
    a = uint64(a + (a << 52))
    a = uint64(a ^ (a >> 41))
    a = uint64(a + (a << 35))
    a = uint64(a ^ (a >> 4))
    
    b = uint64(b + (b << 52))
    b = uint64(b ^ (b >> 41))
    b = uint64(b + (b << 35))
    b = uint64(b ^ (b >> 4))
    
    g = uint64((g ^ a) * 0x9ddfea08eb382d69)
    g = uint64((g ^ b) * 0x9ddfea08eb382d69)
    h = uint64((h ^ g) * 0x9ddfea08eb382d69)
    
    if len_s <= 64:
        return h
    
    # For strings longer than 64 bytes, we compute the hash of chunks and combine them
    v = uint64(len_s)
    for i in range(32, len_s, 64):
        a = fetch64(s, i)
        b = fetch64(s, i + 8)
        c = fetch64(s, i + 16)
        d = fetch64(s, i + 24)
        h = uint64(h * 0x9ddfea08eb382d69)
        h = uint64(h ^ (a * 0x9ddfea08eb382d69))
        h = uint64(h ^ (b * 0x9ddfea08eb382d69))
        h = uint64(h ^ (c * 0x9ddfea08eb382d69))
        h = uint64(h ^ (d * 0x9ddfea08eb382d69))
    
    h = uint64(h * 0x9ddfea08eb382d69)
    
    return h

# Python implementation of CityHash algorithm

def uint32(x):
    return x & 0xFFFFFFFF

def uint64(x):
    return x & 0xFFFFFFFFFFFFFFFF

def rotate(val, shift):
    return uint64((val >> shift) | (val << (64 - shift)))

def hash_len_16(u, v, mul=0x9ddfea08eb382d69):
    a = uint64((u ^ v) * mul)
    a ^= (a >> 47)
    b = uint64((v ^ a) * mul)
    b ^= (b >> 47)
    b = uint64(b * mul)
    return b

def shift_mix(val):
    return uint64(val ^ (val >> 47))

def fetch64(s, pos):
    return int.from_bytes(s[pos:pos+8], byteorder='little')

def fetch32(s, pos):
    return int.from_bytes(s[pos:pos+4], byteorder='little')

def city_murmur(s, len_s):
    a = fetch64(s, 0)
    b = fetch64(s, 8)
    c = fetch64(s, len_s - 8)
    d = fetch64(s, len_s - 16)
    e = fetch64(s, (len_s - 16) // 2)
    
    a = uint64(a * 0x9ddfea08eb382d69)
    b = uint64(b * 0x9ddfea08eb382d69)
    c = uint64(c * 0x9ddfea08eb382d69)
    
    a = uint64(a ^ (rotate(b ^ c, 43) + rotate(b, 30) + d))
    a = uint64(a * 0x9ddfea08eb382d69)
    a = uint64(a + e)
    a = uint64(a ^ (rotate(a, 43) + rotate(a, 15) + c))
    
    return a

def city_hash_64(s):
    len_s = len(s)
    
    if len_s <= 32:
        if len_s <= 16:
            if len_s >= 8:
                a = fetch64(s, 0)
                b = fetch64(s, len_s - 8)
                return hash_len_16(a, uint64(b + len_s), 0x9ddfea08eb382d69)
            elif len_s >= 4:
                a = fetch32(s, 0)
                b = fetch32(s, len_s - 4)
                return hash_len_16(uint64(len_s + (a << 3)), uint64(b << 3), 0x9ddfea08eb382d69)
            elif len_s > 0:
                a = s[0]
                b = s[len_s >> 1]
                c = s[len_s - 1]
                y = uint32(a + (b << 8))
                z = uint32(len_s + (c << 2))
                return shift_mix(uint64(y * 0x9ddfea08eb382d69) ^ uint64(z * 0x9ddfea08eb382d69)) * 0x9ddfea08eb382d69
            return 0x9ddfea08eb382d69
        
        # For 17-32 bytes
        a = fetch64(s, 0)
        b = fetch64(s, 8)
        c = fetch64(s, len_s - 8)
        d = fetch64(s, len_s - 16)
        return hash_len_16(
            uint64(a ^ c), uint64(b ^ d), 0x9ddfea08eb382d69
        )
    
    # For 33+ bytes
    a = fetch64(s, 0)
    b = fetch64(s, 8)
    c = fetch64(s, len_s - 8)
    d = fetch64(s, len_s - 16)
    e = fetch64(s, 16)
    f = fetch64(s, 24)
    h = uint64(len_s)
    
    g = c
    
    a = uint64(a + (a << 52))
    a = uint64(a ^ (a >> 41))
    a = uint64(a + (a << 35))
    a = uint64(a ^ (a >> 4))
    
    b = uint64(b + (b << 52))
    b = uint64(b ^ (b >> 41))
    b = uint64(b + (b << 35))
    b = uint64(b ^ (b >> 4))
    
    g = uint64((g ^ a) * 0x9ddfea08eb382d69)
    g = uint64((g ^ b) * 0x9ddfea08eb382d69)
    h = uint64((h ^ g) * 0x9ddfea08eb382d69)
    
    if len_s <= 64:
        return h
    
    # For strings longer than 64 bytes, we compute the hash of chunks and combine them
    v = uint64(len_s)
    for i in range(32, len_s, 64):
        a = fetch64(s, i)
        b = fetch64(s, i + 8)
        c = fetch64(s, i + 16)
        d = fetch64(s, i + 24)
        h = uint64(h * 0x9ddfea08eb382d69)
        h = uint64(h ^ (a * 0x9ddfea08eb382d69))
        h = uint64(h ^ (b * 0x9ddfea08eb382d69))
        h = uint64(h ^ (c * 0x9ddfea08eb382d69))
        h = uint64(h ^ (d * 0x9ddfea08eb382d69))
    
    h = uint64(h * 0x9ddfea08eb382d69)
    
    return h

def main():
    print("Cityhash TEST -----> start")

    print("\ncity_hash_64")
    input = b"hello world!"
    output = city_hash_64(input)
    print(f"  {input}: {output}")

    print("\nCityhash TEST <----- end")

if __name__ == "__main__":
    main()