using System;
using System.Collections.Generic;

namespace BoyerMooreSearch
{
    class Program
    {
        static List<int> Search(string text, string pattern)
        {
            if (string.IsNullOrEmpty(pattern) || string.IsNullOrEmpty(text) || pattern.Length > text.Length)
            {
                return new List<int>();
            }

            // 結果を格納するリスト
            List<int> occurrences = new List<int>();

            // 悪文字ルールのテーブル作成
            Dictionary<char, int> badChar = BadCharacterTable(pattern);

            // 良接尾辞ルールのテーブル作成
            int[] goodSuffix = GoodSuffixTable(pattern);

            // 検索
            int i = pattern.Length - 1; // テキスト内の位置
            while (i < text.Length)
            {
                int j = pattern.Length - 1; // パターン内の位置
                while (j >= 0 && text[i - pattern.Length + 1 + j] == pattern[j])
                {
                    j--;
                }

                if (j < 0) // マッチした場合
                {
                    occurrences.Add(i - pattern.Length + 1);
                    i++;
                }
                else // マッチしなかった場合
                {
                    // 悪文字ルールと良接尾辞ルールのシフト量の大きい方を採用
                    int bcShift = Math.Max(1, j - (badChar.ContainsKey(text[i]) ? badChar[text[i]] : -1));
                    int gsShift = goodSuffix[j];
                    i += Math.Max(bcShift, gsShift);
                }
            }

            return occurrences;
        }

        static Dictionary<char, int> BadCharacterTable(string pattern)
        {
            // 悪文字ルールのテーブルを作成
            Dictionary<char, int> badChar = new Dictionary<char, int>();
            for (int i = 0; i < pattern.Length; i++)
            {
                badChar[pattern[i]] = i;
            }
            return badChar;
        }

        static int[] GoodSuffixTable(string pattern)
        {
            // 良接尾辞ルールのテーブルを作成
            int n = pattern.Length;
            // ボーダー配列の計算
            int[] border = new int[n + 1];
            border[n] = n;

            // 補助関数：Z配列の計算
            int[] ZArray(string pattern)
            {
                int n = pattern.Length;
                int[] z = new int[n];
                int l = 0, r = 0;
                for (int i = 1; i < n; i++)
                {
                    if (i <= r)
                    {
                        z[i] = Math.Min(r - i + 1, z[i - l]);
                    }
                    while (i + z[i] < n && pattern[z[i]] == pattern[i + z[i]])
                    {
                        z[i]++;
                    }
                    if (i + z[i] - 1 > r)
                    {
                        l = i;
                        r = i + z[i] - 1;
                    }
                }
                return z;
            }

            // パターンの逆順に対するZ配列
            char[] patternChars = pattern.ToCharArray();
            Array.Reverse(patternChars);
            string patternRev = new string(patternChars);
            int[] z = ZArray(patternRev);

            for (int i = 0; i < n; i++)
            {
                int j = n - z[i];
                border[j] = i;
            }

            // 良接尾辞ルールのシフト量計算
            int[] shift = new int[n];
            for (int i = 0; i < n; i++)
            {
                shift[i] = n;
            }

            for (int i = 0; i < n; i++)
            {
                int j = n - border[i];
                shift[n - j - 1] = j;
            }

            // ボーダーが存在しない場合の処理
            for (int i = 0; i < n - 1; i++)
            {
                int j = n - 1 - i;
                if (border[j] == j)
                {
                    for (int k = 0; k < n - j; k++)
                    {
                        if (shift[k] == n)
                        {
                            shift[k] = n - j;
                        }
                    }
                }
            }

            return shift;
        }

        static void Main(string[] args)
        {
            Console.WriteLine("BoyerMooreSearch TEST -----> start");

            Console.WriteLine("\nSearch");
            var input1 = ("ABABCABCABABABD", "ABABD");
            Console.WriteLine($"  入力値: ({input1.Item1}, {input1.Item2})");
            var output1 = Search(input1.Item1, input1.Item2);
            Console.WriteLine($"  出力値: [{string.Join(", ", output1)}]");

            Console.WriteLine("\nSearch");
            var input2 = ("AAAAAA", "AA");
            Console.WriteLine($"  入力値: ({input2.Item1}, {input2.Item2})");
            var output2 = Search(input2.Item1, input2.Item2);
            Console.WriteLine($"  出力値: [{string.Join(", ", output2)}]");

            Console.WriteLine("\nSearch");
            var input3 = ("ABCDEFG", "XYZ");
            Console.WriteLine($"  入力値: ({input3.Item1}, {input3.Item2})");
            var output3 = Search(input3.Item1, input3.Item2);
            Console.WriteLine($"  出力値: [{string.Join(", ", output3)}]");

            Console.WriteLine("\nSearch");
            var input4 = ("ABCABC", "ABC");
            Console.WriteLine($"  入力値: ({input4.Item1}, {input4.Item2})");
            var output4 = Search(input4.Item1, input4.Item2);
            Console.WriteLine($"  出力値: [{string.Join(", ", output4)}]");

            Console.WriteLine("\nSearch");
            var input5 = ("ABC", "");
            Console.WriteLine($"  入力値: ({input5.Item1}, {input5.Item2})");
            var output5 = Search(input5.Item1, input5.Item2);
            Console.WriteLine($"  出力値: [{string.Join(", ", output5)}]");

            Console.WriteLine("\nBoyerMooreSearch TEST <----- end");
        }
    }
}