// Java
// 素数判定: ミラーラビン (Miller-Rabin)

import java.util.Arrays;
import java.util.Random;

public class MillerRabinDemo {
    
    public static boolean isPrime(long target, int k) {
        // 特殊なケースを処理
        if (target <= 1) {
            return false;
        }
        if (target <= 3) {
            return true;
        }
        if (target % 2 == 0) {
            return false;
        }
        
        // n-1 = 2^r * d の形に分解する（dは奇数）
        int r = 0;
        long d = target - 1;
        while (d % 2 == 0) {
            r++;
            d /= 2;
        }
        
        // k回の試行を実行
        Random random = new Random();
        for (int i = 0; i < k; i++) {
            long a = 2 + random.nextInt((int)(target - 3));
            long x = modPow(a, d, target);
            
            if (x == 1 || x == target - 1) {
                continue;
            }
            
            boolean isProbablePrime = false;
            for (int j = 0; j < r - 1; j++) {
                x = modPow(x, 2, target);
                if (x == target - 1) {
                    isProbablePrime = true;
                    break;
                }
            }
            
            if (!isProbablePrime) {
                return false; // 合成数と確定
            }
        }
        
        return true; // 素数である可能性が高い
    }
    
    public static boolean isPrime(long target) {
        return isPrime(target, 40);
    }
    
    // a^b mod m を計算する関数
    private static long modPow(long base, long exponent, long modulus) {
        return java.math.BigInteger.valueOf(base)
                .modPow(java.math.BigInteger.valueOf(exponent), java.math.BigInteger.valueOf(modulus))
                .longValue();
    }
    
    public static void main(String[] args) {
        System.out.println("MillerRabin TEST -----> start");
        
        System.out.println("\nis_prime");
        long[] inputList = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 97, 100, 101, 997};
        for (long input : inputList) {
            boolean output = isPrime(input);
            System.out.println("  " + input + ": " + output);
        }
        
        System.out.println("\nMillerRabin TEST <----- end");
    }
}