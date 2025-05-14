// C#
// 素数判定: フェルマーテスト (Fermat Test)

using System;
using System.Numerics;

class FermatTest
{
    static bool IsPrime(int target, int k = 5)
    {
        if (target == 2 || target == 3)
            return true;

        // 2未満または偶数は素数でない（2を除く）
        if (target < 2 || target % 2 == 0)
            return false;

        Random random = new Random();
        
        for (int i = 0; i < k; i++)
        {
            // 1 < a < target の範囲からランダムに数を選ぶ
            int a = random.Next(2, target - 1);

            // フェルマーの小定理: a^(target-1) ≡ 1 (mod target) が成り立つか検証
            // BigInteger.ModPow は (a^(target-1)) % target を効率的に計算します。
            if (BigInteger.ModPow(a, target - 1, target) != 1)
                return false;
        }

        return true;
    }

    static void Main()
    {
        Console.WriteLine("FermatTest TEST -----> start");

        Console.WriteLine("\nIsPrime");
        int[] inputList = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 97, 100, 101, 997 };
        foreach (int input in inputList)
        {
            bool output = IsPrime(input);
            Console.WriteLine($"  {input}: {output}");
        }

        Console.WriteLine("\nFermatTest TEST <----- end");
    }
}