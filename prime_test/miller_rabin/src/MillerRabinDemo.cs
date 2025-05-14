// C#
// 素数判定: ミラーラビン (Miller-Rabin)

using System;

class MillerRabinDemo
{
    static Random random = new Random();
    
    static bool IsPrime(int target, int k = 40)
    {
        // 特殊なケースを処理
        if (target <= 1)
            return false;
        if (target <= 3)
            return true;
        if (target % 2 == 0)
            return false;
        
        // n-1 = 2^r * d の形に分解する（dは奇数）
        int r = 0;
        int d = target - 1;
        while (d % 2 == 0)
        {
            r++;
            d /= 2;
        }
        
        // k回の試行を実行
        for (int i = 0; i < k; i++)
        {
            int a = random.Next(2, target - 1);
            long x = ModPow(a, d, target);  // a^d mod n を計算
            
            if (x == 1 || x == target - 1)
                continue;
            
            bool isProbablyPrime = false;
            for (int j = 0; j < r - 1; j++)
            {
                x = ModPow(x, 2, target);
                if (x == target - 1)
                {
                    isProbablyPrime = true;
                    break;
                }
            }
            
            if (!isProbablyPrime)
                return false;  // 合成数と確定
        }
        
        return true;  // 素数である可能性が高い
    }
    
    // C#の BigInteger が使えない場合の ModPow の実装
    static long ModPow(long baseNum, int exponent, int modulus)
    {
        long result = 1;
        baseNum = baseNum % modulus;
        
        while (exponent > 0)
        {
            if (exponent % 2 == 1)
                result = (result * baseNum) % modulus;
            
            exponent >>= 1;
            baseNum = (baseNum * baseNum) % modulus;
        }
        
        return result;
    }
    
    static void Main()
    {
        Console.WriteLine("MillerRabin TEST -----> start");
        
        Console.WriteLine("\nIsPrime");
        int[] inputList = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 97, 100, 101, 997 };
        foreach (int input in inputList)
        {
            bool output = IsPrime(input);
            Console.WriteLine($"  {input}: {output}");
        }
        
        Console.WriteLine("\nMillerRabin TEST <----- end");
    }
}