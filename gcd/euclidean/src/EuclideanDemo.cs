// C#
// 最大公約数: ユークリッドの互除法 (Euclidean)

using System;

class Program
{
    static int Gcd(int a, int b)
    {
        // bが0になるまで繰り返し計算する
        while (b != 0)
        {
            int temp = a;
            a = b;
            b = temp % b;
        }
        return a;
    }

    static void Main(string[] args)
    {
        Console.WriteLine("Euclidean TEST -----> start");
        
        (int a, int b) input = (48, 18);
        Console.WriteLine($"\n  入力値: ({input.a}, {input.b})");
        
        int output = Gcd(input.a, input.b);
        Console.WriteLine($"\n  出力値: {output}");
        
        Console.WriteLine("\nEuclidean TEST <----- end");
    }
}