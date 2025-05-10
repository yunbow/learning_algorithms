// C#
// データ構造: 配列 (Array)

using System;
using System.Collections.Generic;

public class ArrayData
{
    private List<int> _data;

    public ArrayData()
    {
        _data = new List<int>();
    }

    public List<int> Get()
    {
        // 要素を取得
        return _data;
    }

    public int GetIndex(int item)
    {
        // 配列内で指定された値を検索し、最初に見つかったインデックスを返す
        try
        {
            int index = _data.IndexOf(item);
            if (index != -1)
            {
                return index;
            }
            else
            {
                Console.WriteLine($"ERROR: {item} は範囲外です");
                return -1;
            }
        }
        catch (Exception)
        {
            Console.WriteLine($"ERROR: {item} は範囲外です");
            return -1;
        }
    }

    public int? GetValue(int index)
    {
        // 指定されたインデックスの要素を取得する
        if (0 <= index && index < _data.Count)
        {
            return _data[index];
        }
        else
        {
            Console.WriteLine($"ERROR: {index} は範囲外です");
            return null;
        }
    }

    public bool Add(int item)
    {
        // 配列の末尾に要素を追加する
        _data.Add(item);
        return true;
    }

    public bool Remove(int index)
    {
        // 指定されたインデックスの要素を削除する
        if (0 <= index && index < _data.Count)
        {
            _data.RemoveAt(index);
            return true;
        }
        else
        {
            Console.WriteLine($"ERROR: {index} は範囲外です");
            return false;
        }
    }

    public bool Update(int index, int newValue)
    {
        // 指定されたインデックスの要素を新しい値に更新する
        if (0 <= index && index < _data.Count)
        {
            _data[index] = newValue;
            return true;
        }
        else
        {
            Console.WriteLine($"ERROR: {index} は範囲外です");
            return false;
        }
    }

    public List<int> Reverse()
    {
        // 配列の要素を逆順にする
        _data.Reverse();
        return _data;
    }

    public List<int> Sort(bool descending = false)
    {
        // 配列の要素をソートする
        if (descending)
        {
            _data.Sort((a, b) => b.CompareTo(a));
        }
        else
        {
            _data.Sort();
        }
        return _data;
    }

    public bool IsEmpty()
    {
        // 配列が空かどうか
        return _data.Count == 0;
    }

    public int Size()
    {
        // 配列のサイズ（要素数）を返す
        return _data.Count;
    }

    public bool Clear()
    {
        // 配列の全要素を削除する
        _data.Clear();
        return true;
    }
}

public class Program
{
    public static void Main()
    {
        Console.WriteLine("Array TEST -----> start");

        Console.WriteLine("\nnew");
        ArrayData arrayData = new ArrayData();
        Console.WriteLine($"  現在のデータ: [{string.Join(", ", arrayData.Get())}]");

        Console.WriteLine("\nadd");
        int[] input = { 10, 20, 30, 10, 40 };
        foreach (int item in input)
        {
            Console.WriteLine($"  入力値: {item}");
            bool output = arrayData.Add(item);
            Console.WriteLine($"  出力値: {output}");
            Console.WriteLine($"  現在のデータ: [{string.Join(", ", arrayData.Get())}]");
        }

        Console.WriteLine("\nsize");
        int sizeOutput = arrayData.Size();
        Console.WriteLine($"  出力値: {sizeOutput}");

        Console.WriteLine("\nis_empty");
        bool isEmptyOutput = arrayData.IsEmpty();
        Console.WriteLine($"  出力値: {isEmptyOutput}");

        Console.WriteLine("\nget_value");
        int indexInput = 2;
        Console.WriteLine($"  入力値: {indexInput}");
        int? valueOutput = arrayData.GetValue(indexInput);
        Console.WriteLine($"  出力値: {valueOutput}");

        Console.WriteLine("\nget_value");
        indexInput = 10;
        Console.WriteLine($"  入力値: {indexInput}");
        valueOutput = arrayData.GetValue(indexInput);
        Console.WriteLine($"  出力値: {valueOutput}");

        Console.WriteLine("\nupdate");
        Console.WriteLine($"  入力値: (1, 25)");
        bool updateOutput = arrayData.Update(1, 25);
        Console.WriteLine($"  出力値: {updateOutput}");
        Console.WriteLine($"  現在のデータ: [{string.Join(", ", arrayData.Get())}]");

        Console.WriteLine("\nupdate");
        Console.WriteLine($"  入力値: (15, 25)");
        updateOutput = arrayData.Update(15, 25);
        Console.WriteLine($"  出力値: {updateOutput}");
        Console.WriteLine($"  現在のデータ: [{string.Join(", ", arrayData.Get())}]");

        Console.WriteLine("\nget_index");
        int itemInput = 10;
        Console.WriteLine($"  入力値: {itemInput}");
        int indexOutput = arrayData.GetIndex(itemInput);
        Console.WriteLine($"  出力値: {indexOutput}");

        Console.WriteLine("\nget_index");
        itemInput = 99;
        Console.WriteLine($"  入力値: {itemInput}");
        indexOutput = arrayData.GetIndex(itemInput);
        Console.WriteLine($"  出力値: {indexOutput}");

        Console.WriteLine("\nremove");
        indexInput = 3;
        Console.WriteLine($"  入力値: {indexInput}");
        bool removeOutput = arrayData.Remove(indexInput);
        Console.WriteLine($"  出力値: {removeOutput}");
        Console.WriteLine($"  現在のデータ: [{string.Join(", ", arrayData.Get())}]");

        Console.WriteLine("\nremove");
        indexInput = 8;
        Console.WriteLine($"  入力値: {indexInput}");
        removeOutput = arrayData.Remove(indexInput);
        Console.WriteLine($"  出力値: {removeOutput}");
        Console.WriteLine($"  現在のデータ: [{string.Join(", ", arrayData.Get())}]");

        Console.WriteLine("\nreverse");
        List<int> reverseOutput = arrayData.Reverse();
        Console.WriteLine($"  出力値: [{string.Join(", ", reverseOutput)}]");

        Console.WriteLine("\nsort");
        Console.WriteLine("  入力値: descending=False");
        List<int> sortOutput = arrayData.Sort(descending: false);
        Console.WriteLine($"  出力値: [{string.Join(", ", sortOutput)}]");

        Console.WriteLine("\nsort");
        Console.WriteLine("  入力値: descending=True");
        sortOutput = arrayData.Sort(descending: true);
        Console.WriteLine($"  出力値: [{string.Join(", ", sortOutput)}]");

        Console.WriteLine("\nclear");
        bool clearOutput = arrayData.Clear();
        Console.WriteLine($"  出力値: {clearOutput}");
        Console.WriteLine($"  現在のデータ: [{string.Join(", ", arrayData.Get())}]");

        Console.WriteLine("\nis_empty");
        isEmptyOutput = arrayData.IsEmpty();
        Console.WriteLine($"  出力値: {isEmptyOutput}");

        Console.WriteLine("\nArray TEST <----- end");
    }
}