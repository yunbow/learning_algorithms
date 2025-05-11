// C#
// 配列の並び替え: 選択ソート (Selection Sort)

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

    private List<int> _selection_sort(List<int> target)
    {
        // 配列の長さを取得
        int n = target.Count;
        
        // 配列を順番に走査
        for (int i = 0; i < n; i++)
        {
            // 未ソート部分の最小値のインデックスを見つける
            int min_index = i;
            for (int j = i + 1; j < n; j++)
            {
                if (target[j] < target[min_index])
                {
                    min_index = j;
                }
            }
            
            // 見つかった最小値と現在の位置を交換
            int temp = target[i];
            target[i] = target[min_index];
            target[min_index] = temp;
        }
        
        return target;
    }

    public bool Sort()
    {
        _selection_sort(_data);
        return true;
    }
}

class Program
{
    static void Main(string[] args)
    {
        Console.WriteLine("SelectionSort TEST -----> start");

        ArrayData array_data = new ArrayData();

        // ランダムな整数の配列
        Console.WriteLine("\nsort");
        List<int> input = new List<int> { 64, 34, 25, 12, 22, 11, 90 };
        Console.WriteLine($"  ソート前: [{string.Join(", ", input)}]");
        array_data.Set(input);
        array_data.Sort();
        Console.WriteLine($"  ソート後: [{string.Join(", ", array_data.Get())}]");
        
        // 既にソートされている配列
        Console.WriteLine("\nsort");
        input = new List<int> { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        Console.WriteLine($"  ソート前: [{string.Join(", ", input)}]");
        array_data.Set(input);
        array_data.Sort();
        Console.WriteLine($"  ソート後: [{string.Join(", ", array_data.Get())}]");
        
        // 逆順の配列
        Console.WriteLine("\nsort");
        input = new List<int> { 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 };
        Console.WriteLine($"  ソート前: [{string.Join(", ", input)}]");
        array_data.Set(input);
        array_data.Sort();
        Console.WriteLine($"  ソート後: [{string.Join(", ", array_data.Get())}]");
        
        // 重複要素を含む配列
        Console.WriteLine("\nsort");
        input = new List<int> { 10, 9, 8, 7, 6, 10, 9, 8, 7, 6 };
        Console.WriteLine($"  ソート前: [{string.Join(", ", input)}]");
        array_data.Set(input);
        array_data.Sort();
        Console.WriteLine($"  ソート後: [{string.Join(", ", array_data.Get())}]");
        
        // 空の配列
        Console.WriteLine("\nsort");
        input = new List<int> { };
        Console.WriteLine($"  ソート前: [{string.Join(", ", input)}]");
        array_data.Set(input);
        array_data.Sort();
        Console.WriteLine($"  ソート後: [{string.Join(", ", array_data.Get())}]");

        Console.WriteLine("\nSelectionSort TEST <----- end");
    }
}