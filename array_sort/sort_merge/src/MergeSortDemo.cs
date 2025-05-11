// C#
// 配列の並び替え: マージソート (Merge Sort)

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

    private List<int> _MergeSort(List<int> target)
    {
        // 配列の長さが1以下の場合はそのまま返す（基本ケース）
        if (target.Count <= 1)
        {
            return target;
        }

        // 配列を半分に分割
        int mid = target.Count / 2;
        List<int> leftHalf = target.GetRange(0, mid);
        List<int> rightHalf = target.GetRange(mid, target.Count - mid);

        // 左右の半分を再帰的にソート
        leftHalf = _MergeSort(leftHalf);
        rightHalf = _MergeSort(rightHalf);

        // ソート済みの半分同士をマージ
        return _Merge(leftHalf, rightHalf);
    }

    private List<int> _Merge(List<int> left, List<int> right)
    {
        List<int> result = new List<int>();
        int i = 0;
        int j = 0;

        // 左右の配列を比較しながらマージ
        while (i < left.Count && j < right.Count)
        {
            if (left[i] <= right[j])
            {
                result.Add(left[i]);
                i++;
            }
            else
            {
                result.Add(right[j]);
                j++;
            }
        }

        // 残った要素を追加
        while (i < left.Count)
        {
            result.Add(left[i]);
            i++;
        }

        while (j < right.Count)
        {
            result.Add(right[j]);
            j++;
        }

        return result;
    }

    public bool Sort()
    {
        _data = _MergeSort(_data);
        return true;
    }
}

class Program
{
    static void Main(string[] args)
    {
        Console.WriteLine("MergeSort TEST -----> start");

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

        Console.WriteLine("\nMergeSort TEST <----- end");
    }
}