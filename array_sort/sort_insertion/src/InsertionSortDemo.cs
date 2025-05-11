// C#
// 配列の並び替え: 挿入ソート (Insertion Sort)

using System;
using System.Collections.Generic;

class ArrayData
{
    private List<int> _data = new List<int>();

    public List<int> Get()
    {
        return _data;
    }

    public bool Set(List<int> data)
    {
        _data = data;
        return true;
    }

    public bool Sort()
    {
        // 配列の長さを取得
        int n = _data.Count;
        
        // 2番目の要素から始める（最初の要素は既にソート済みと見なす）
        for (int i = 1; i < n; i++)
        {
            // 現在の要素を取得
            int key = _data[i];
            
            // ソート済み部分の最後の要素のインデックス
            int j = i - 1;
            
            // keyより大きい要素をすべて右にシフト
            while (j >= 0 && _data[j] > key)
            {
                _data[j + 1] = _data[j];
                j--;
            }
                
            // 適切な位置にkeyを挿入
            _data[j + 1] = key;
        }
        
        return true;
    }
}

class Program
{
    static void Main(string[] args)
    {
        Console.WriteLine("InsertionSort TEST -----> start");

        ArrayData arrayData = new ArrayData();

        // ランダムな整数の配列
        Console.WriteLine("\nsort");
        List<int> input = new List<int> { 64, 34, 25, 12, 22, 11, 90 };
        Console.WriteLine($"  ソート前: [{string.Join(", ", input)}]");
        arrayData.Set(input);
        arrayData.Sort();
        Console.WriteLine($"  ソート後: [{string.Join(", ", arrayData.Get())}]");
        
        // 既にソートされている配列
        Console.WriteLine("\nsort");
        input = new List<int> { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        Console.WriteLine($"  ソート前: [{string.Join(", ", input)}]");
        arrayData.Set(input);
        arrayData.Sort();
        Console.WriteLine($"  ソート後: [{string.Join(", ", arrayData.Get())}]");
        
        // 逆順の配列
        Console.WriteLine("\nsort");
        input = new List<int> { 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 };
        Console.WriteLine($"  ソート前: [{string.Join(", ", input)}]");
        arrayData.Set(input);
        arrayData.Sort();
        Console.WriteLine($"  ソート後: [{string.Join(", ", arrayData.Get())}]");
        
        // 重複要素を含む配列
        Console.WriteLine("\nsort");
        input = new List<int> { 10, 9, 8, 7, 6, 10, 9, 8, 7, 6 };
        Console.WriteLine($"  ソート前: [{string.Join(", ", input)}]");
        arrayData.Set(input);
        arrayData.Sort();
        Console.WriteLine($"  ソート後: [{string.Join(", ", arrayData.Get())}]");
        
        // 空の配列
        Console.WriteLine("\nsort");
        input = new List<int> { };
        Console.WriteLine($"  ソート前: [{string.Join(", ", input)}]");
        arrayData.Set(input);
        arrayData.Sort();
        Console.WriteLine($"  ソート後: [{string.Join(", ", arrayData.Get())}]");

        Console.WriteLine("\nInsertionSort TEST <----- end");
    }
}