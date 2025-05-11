// C#
// 配列の並び替え: バブルソート (Bubble Sort)

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
        int n = _data.Count;
        
        // 外側のループ: n-1回の走査が必要
        for (int i = 0; i < n; i++)
        {
            // 最適化: 一度の走査で交換がなければソート完了
            bool swapped = false;
            
            // 内側のループ: まだソートされていない部分を走査
            // 各走査後に最大の要素が末尾に移動するため、i回分を除外
            for (int j = 0; j < n - i - 1; j++)
            {
                // 隣接する要素を比較し、必要に応じて交換
                if (_data[j] > _data[j + 1])
                {
                    int temp = _data[j];
                    _data[j] = _data[j + 1];
                    _data[j + 1] = temp;
                    swapped = true;
                }
            }
            
            // 交換が発生しなければソート完了
            if (!swapped)
                break;
        }
        return true;
    }
}

class Program
{
    static void Main(string[] args)
    {
        Console.WriteLine("BubbleSort TEST -----> start");
        
        ArrayData arrayData = new ArrayData();
        
        // ランダムな整数の配列
        Console.WriteLine("\nsort");
        List<int> input1 = new List<int> { 64, 34, 25, 12, 22, 11, 90 };
        Console.WriteLine($"  ソート前: [{string.Join(", ", input1)}]");
        arrayData.Set(input1);
        arrayData.Sort();
        Console.WriteLine($"  ソート後: [{string.Join(", ", arrayData.Get())}]");
        
        // 既にソートされている配列
        Console.WriteLine("\nsort");
        List<int> input2 = new List<int> { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        Console.WriteLine($"  ソート前: [{string.Join(", ", input2)}]");
        arrayData.Set(input2);
        arrayData.Sort();
        Console.WriteLine($"  ソート後: [{string.Join(", ", arrayData.Get())}]");
        
        // 逆順の配列
        Console.WriteLine("\nsort");
        List<int> input3 = new List<int> { 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 };
        Console.WriteLine($"  ソート前: [{string.Join(", ", input3)}]");
        arrayData.Set(input3);
        arrayData.Sort();
        Console.WriteLine($"  ソート後: [{string.Join(", ", arrayData.Get())}]");
        
        // 重複要素を含む配列
        Console.WriteLine("\nsort");
        List<int> input4 = new List<int> { 10, 9, 8, 7, 6, 10, 9, 8, 7, 6 };
        Console.WriteLine($"  ソート前: [{string.Join(", ", input4)}]");
        arrayData.Set(input4);
        arrayData.Sort();
        Console.WriteLine($"  ソート後: [{string.Join(", ", arrayData.Get())}]");
        
        // 空の配列
        Console.WriteLine("\nsort");
        List<int> input5 = new List<int> { };
        Console.WriteLine($"  ソート前: [{string.Join(", ", input5)}]");
        arrayData.Set(input5);
        arrayData.Sort();
        Console.WriteLine($"  ソート後: [{string.Join(", ", arrayData.Get())}]");
        
        Console.WriteLine("\nBubbleSort TEST <----- end");
    }
}