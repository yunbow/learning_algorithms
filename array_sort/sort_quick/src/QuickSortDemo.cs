// C#
// 配列の並び替え: クイックソート (Quick Sort)

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

    private List<int> _QuickSort(List<int> target)
    {
        // 空の配列または要素が1つの場合はそのまま返す（基底条件）
        if (target.Count <= 1)
            return target;
        
        // ピボットを選択（この実装では最後の要素を選択）
        int pivot = target[target.Count - 1];
        
        // ピボットより小さい要素と大きい要素に分ける
        List<int> left = new List<int>();
        List<int> right = new List<int>();
        
        // 最後の要素（ピボット）を除いて配列をスキャン
        for (int i = 0; i < target.Count - 1; i++)
        {
            if (target[i] <= pivot)
                left.Add(target[i]);
            else
                right.Add(target[i]);
        }
        
        // 左側の部分配列、ピボット、右側の部分配列を再帰的にソートして結合
        List<int> result = new List<int>();
        result.AddRange(_QuickSort(left));
        result.Add(pivot);
        result.AddRange(_QuickSort(right));
        
        return result;
    }

    public bool Sort()
    {
        _data = _QuickSort(_data);
        return true;
    }
}

class Program
{
    static void Main(string[] args)
    {
        Console.WriteLine("QuickSort TEST -----> start");

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
        input = new List<int>();
        Console.WriteLine($"  ソート前: [{string.Join(", ", input)}]");
        arrayData.Set(input);
        arrayData.Sort();
        Console.WriteLine($"  ソート後: [{string.Join(", ", arrayData.Get())}]");

        Console.WriteLine("\nQuickSort TEST <----- end");
    }
}