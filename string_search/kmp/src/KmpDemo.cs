using System;
using System.Collections.Generic;

class KmpDemo
{
    // LPSテーブル(最長の接頭辞と接尾辞が一致する長さ)を計算する関数
    static int[] ComputeLps(string pattern)
    {
        int m = pattern.Length;
        int[] lps = new int[m]; // LPSテーブルを初期化
        
        // 最初のインデックスのLPS値は常に0
        int length = 0;
        int i = 1;
        
        // LPSテーブルの残りの値を計算
        while (i < m)
        {
            if (pattern[i] == pattern[length])
            {
                // 文字が一致する場合、lengthをインクリメントしてlps[i]に保存
                length++;
                lps[i] = length;
                i++;
            }
            else
            {
                // 文字が一致しない場合
                if (length != 0)
                {
                    // 一致した部分文字列の前の位置のLPS値を使用
                    length = lps[length - 1];
                    // iはインクリメントしない
                }
                else
                {
                    // length = 0の場合、lps[i] = 0として次に進む
                    lps[i] = 0;
                    i++;
                }
            }
        }
        
        return lps;
    }

    // テキスト内のパターンを検索する関数
    static List<int> Search(string text, string pattern)
    {
        if (string.IsNullOrEmpty(pattern) || string.IsNullOrEmpty(text))
        {
            return new List<int>();
        }
        
        int n = text.Length;
        int m = pattern.Length;
        
        // パターン長がテキスト長より大きい場合、マッチングは不可能
        if (m > n)
        {
            return new List<int>();
        }
        
        // LPSテーブルを計算
        int[] lps = ComputeLps(pattern);
        
        List<int> result = new List<int>(); // マッチした位置のリスト
        int i = 0; // テキストのインデックス
        int j = 0; // パターンのインデックス
        
        while (i < n)
        {
            // 現在の文字が一致する場合
            if (pattern[j] == text[i])
            {
                i++;
                j++;
            }
            
            // パターン全体がマッチした場合
            if (j == m)
            {
                // パターンの開始位置をresultに追加
                result.Add(i - j);
                // 次の可能性のある一致を探すために、jをLPS[j-1]に設定
                j = lps[j - 1];
            }
            // 文字が一致しない場合
            else if (i < n && pattern[j] != text[i])
            {
                // jが0でない場合、LPSテーブルを使って次の位置を決定
                if (j != 0)
                {
                    j = lps[j - 1];
                }
                else
                {
                    // jが0の場合、単純にテキストの次の文字に進む
                    i++;
                }
            }
        }
        
        return result;
    }

    static void Main()
    {
        Console.WriteLine("Kmp TEST -----> start");

        Console.WriteLine("\nsearch");
        var input1 = ("ABABCABCABABABD", "ABABD");
        Console.WriteLine($"  入力値: {input1}");
        var output1 = Search(input1.Item1, input1.Item2);
        Console.WriteLine($"  出力値: [{string.Join(", ", output1)}]");

        Console.WriteLine("\nsearch");
        var input2 = ("AAAAAA", "AA");
        Console.WriteLine($"  入力値: {input2}");
        var output2 = Search(input2.Item1, input2.Item2);
        Console.WriteLine($"  出力値: [{string.Join(", ", output2)}]");

        Console.WriteLine("\nsearch");
        var input3 = ("ABCDEFG", "XYZ");
        Console.WriteLine($"  入力値: {input3}");
        var output3 = Search(input3.Item1, input3.Item2);
        Console.WriteLine($"  出力値: [{string.Join(", ", output3)}]");

        Console.WriteLine("\nsearch");
        var input4 = ("ABCABC", "ABC");
        Console.WriteLine($"  入力値: {input4}");
        var output4 = Search(input4.Item1, input4.Item2);
        Console.WriteLine($"  出力値: [{string.Join(", ", output4)}]");

        Console.WriteLine("\nsearch");
        var input5 = ("ABC", "");
        Console.WriteLine($"  入力値: {input5}");
        var output5 = Search(input5.Item1, input5.Item2);
        Console.WriteLine($"  出力値: [{string.Join(", ", output5)}]");

        Console.WriteLine("\nKmp TEST <----- end");
    }
}