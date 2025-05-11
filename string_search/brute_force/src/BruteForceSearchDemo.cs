// C#
// 文字列の検索: ブルートフォース法 (Brute Force Search)

using System;
using System.Collections.Generic;

class BruteForceSearch
{
    static List<int> Search(string text, string pattern)
    {
        int n = text.Length;
        int m = pattern.Length;
        List<int> positions = new List<int>();
        
        // パターンが空または検索対象より長い場合
        if (m == 0 || m > n)
        {
            return positions;
        }
        
        // テキスト内の各位置でパターンとの一致を確認
        for (int i = 0; i <= n - m; i++)
        {
            int j = 0;
            // パターンの各文字を確認
            while (j < m && text[i + j] == pattern[j])
            {
                j++;
            }
            // パターンが完全に一致した場合
            if (j == m)
            {
                positions.Add(i);
            }
        }
        
        return positions;
    }

    static void Main()
    {
        Console.WriteLine("BruteForceSearch TEST -----> start");

        Console.WriteLine("\nSearch");
        var input1 = ("ABABCABCABABABD", "ABABD");
        Console.WriteLine($"  入力値: {input1}");
        var output1 = Search(input1.Item1, input1.Item2);
        Console.WriteLine($"  出力値: [{string.Join(", ", output1)}]");

        Console.WriteLine("\nSearch");
        var input2 = ("AAAAAA", "AA");
        Console.WriteLine($"  入力値: {input2}");
        var output2 = Search(input2.Item1, input2.Item2);
        Console.WriteLine($"  出力値: [{string.Join(", ", output2)}]");

        Console.WriteLine("\nSearch");
        var input3 = ("ABCDEFG", "XYZ");
        Console.WriteLine($"  入力値: {input3}");
        var output3 = Search(input3.Item1, input3.Item2);
        Console.WriteLine($"  出力値: [{string.Join(", ", output3)}]");

        Console.WriteLine("\nSearch");
        var input4 = ("ABCABC", "ABC");
        Console.WriteLine($"  入力値: {input4}");
        var output4 = Search(input4.Item1, input4.Item2);
        Console.WriteLine($"  出力値: [{string.Join(", ", output4)}]");

        Console.WriteLine("\nSearch");
        var input5 = ("ABC", "");
        Console.WriteLine($"  入力値: {input5}");
        var output5 = Search(input5.Item1, input5.Item2);
        Console.WriteLine($"  出力値: [{string.Join(", ", output5)}]");

        Console.WriteLine("\nBruteForceSearch TEST <----- end");
    }
}