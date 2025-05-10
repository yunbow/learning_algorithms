// C#
// 配列の検索: ハッシュ探索 (Hash Search)

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
        // ハッシュテーブルの作成
        Dictionary<int, int> hashTable = new Dictionary<int, int>();
        
        // 配列の要素をハッシュテーブルに格納
        // キーを要素の値、値をインデックスとする
        for (int i = 0; i < _data.Count; i++)
        {
            hashTable[_data[i]] = i;
        }
        
        // ハッシュテーブルを使って検索
        if (hashTable.ContainsKey(target))
        {
            return hashTable[target];
        }
        else
        {
            return -1;
        }
    }
}

class Program
{
    static void Main(string[] args)
    {
        Console.WriteLine("HashSearch TEST -----> start");

        Console.WriteLine("\nnew");
        ArrayData arrayData = new ArrayData();
        List<int> input = new List<int> { 1, 3, 5, 7, 9, 11, 13, 15, 17, 19 };
        arrayData.Set(input);
        Console.WriteLine($"  現在のデータ: [{string.Join(", ", arrayData.Get())}]");

        Console.WriteLine("\nsearch");
        int searchInput = 7;
        Console.WriteLine($"  入力値: {searchInput}");
        int output = arrayData.Search(searchInput);
        Console.WriteLine($"  出力値: {output}");

        Console.WriteLine("\nsearch");
        searchInput = 30;
        Console.WriteLine($"  入力値: {searchInput}");
        output = arrayData.Search(searchInput);
        Console.WriteLine($"  出力値: {output}");

        Console.WriteLine("\nHashSearch TEST <----- end");
    }
}