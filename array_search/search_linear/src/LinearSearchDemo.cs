// C#
// 配列の検索: 線形探索 (Linear Search)

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

    public int Search(int target)
    {
        // 配列の要素を順番に確認
        for (int i = 0; i < _data.Count; i++)
        {
            // 目的の値が見つかった場合、そのインデックスを返す
            if (_data[i] == target)
            {
                return i;
            }
        }
        
        // 見つからなかった場合は -1 を返す
        return -1;
    }
}

class Program
{
    static void Main(string[] args)
    {
        Console.WriteLine("LinearSearch TEST -----> start");

        Console.WriteLine("\nnew");
        ArrayData arrayData = new ArrayData();
        List<int> input = new List<int> { 1, 3, 5, 7, 9, 11, 13, 15, 17, 19 };
        arrayData.Set(input);
        Console.WriteLine($"  現在のデータ: [{string.Join(", ", arrayData.Get())}]");
        
        Console.WriteLine("\nsearch");
        int searchValue = 7;
        Console.WriteLine($"  入力値: {searchValue}");
        int output = arrayData.Search(searchValue);
        Console.WriteLine($"  出力値: {output}");

        Console.WriteLine("\nsearch");
        searchValue = 30;
        Console.WriteLine($"  入力値: {searchValue}");
        output = arrayData.Search(searchValue);
        Console.WriteLine($"  出力値: {output}");

        Console.WriteLine("\nLinearSearch TEST <----- end");
    }
}