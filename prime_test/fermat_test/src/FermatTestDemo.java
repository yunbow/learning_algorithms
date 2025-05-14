// Java
// 素数判定: フェルマーテスト (Fermat Test)

import java.math.BigInteger;
import java.util.Random;

public class FermatTestDemo {
    
    public static boolean isPrime(int target, int k) {
        if (target == 2 || target == 3) {
            return true;
        }
        
        // 2未満または偶数は素数でない（2を除く）
        if (target < 2 || target % 2 == 0) {
            return false;
        }
        
        Random rand = new Random();
        BigInteger targetBig = BigInteger.valueOf(target);
        BigInteger targetMinusOne = targetBig.subtract(BigInteger.ONE);
        
        for (int i = 0; i < k; i++) {
            // 1 < a < target の範囲からランダムに数を選ぶ
            int a = rand.nextInt(target - 3) + 2; // 2からtarget-2の範囲
            
            // フェルマーの小定理: a^(target-1) ≡ 1 (mod target) が成り立つか検証
            BigInteger aBig = BigInteger.valueOf(a);
            BigInteger result = aBig.modPow(targetMinusOne, targetBig);
            
            if (!result.equals(BigInteger.ONE)) {
                return false;
            }
        }
        
        return true;
    }
    
    public static boolean isPrime(int target) {
        return isPrime(target, 5);
    }
    
    public static void main(String[] args) {
        System.out.println("FermatTest TEST -----> start");
        
        System.out.println("\nis_prime");
        int[] inputList = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 97, 100, 101, 997};
        for (int input : inputList) {
            boolean output = isPrime(input);
            System.out.println("  " + input + ": " + output);
        }
        
        System.out.println("\nFermatTest TEST <----- end");
    }
}