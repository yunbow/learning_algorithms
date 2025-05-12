// C#
// データ構造: セット (Set)

using System;
using System.Collections.Generic;

class SetData
{
    private List<object> _data;

    public SetData()
    {
        _data = new List<object>();
    }

    public List<object> Get()
    {
        // 要素を取得
        return _data;
    }

    public int GetIndex(object item)
    {
        // 指定された要素がセット内に存在するかどうかをチェックする。
        try
        {
            int index = _data.IndexOf(item);
            if (index == -1)
            {
                Console.WriteLine($"ERROR: {item} は範囲外です");
                return -1;
            }
            return index;
        }
        catch (Exception)
        {
            Console.WriteLine($"ERROR: {item} は範囲外です");
            return -1;
        }
    }

    public object GetValue(int index)
    {
        // 指定されたインデックスの要素を取得する。
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

    public bool Add(object item)
    {
        // 要素をセットに追加する。
        if (!_data.Contains(item))
        {
            _data.Add(item);
            return true;
        }
        else
        {
            Console.WriteLine($"ERROR: {item} は重複です");
            return false;
        }
    }

    public bool Remove(object item)
    {
        // 指定された要素をセットから削除する。
        if (_data.Contains(item))
        {
            _data.Remove(item);
            return true;
        }
        else
        {
            Console.WriteLine($"ERROR: {item} は範囲外です");
            return false;
        }
    }

    public bool IsEmpty()
    {
        // 空かどうかをチェックする
        return _data.Count == 0;
    }

    public int Size()
    {
        // 要素数を返す
        return _data.Count;
    }

    public bool Clear()
    {
        // 空にする
        _data.Clear();
        return true;
    }
}

class Program
{
    static void Main(string[] args)
    {
        Console.WriteLine("Set TEST -----> start");

        Console.WriteLine("\nnew");
        SetData setData = new SetData();
        Console.WriteLine($"  現在のデータ: [{string.Join(", ", setData.Get())}]");

        Console.WriteLine("\nadd");
        object[] input = { 10, 20, 30, 20, 40 };
        foreach (var item in input)
        {
            Console.WriteLine($"  入力値: {item}");
            bool output = setData.Add(item);
            Console.WriteLine($"  出力値: {output}");
            Console.WriteLine($"  現在のデータ: [{string.Join(", ", setData.Get())}]");
        }

        Console.WriteLine("\nsize");
        int sizeOutput = setData.Size();
        Console.WriteLine($"  出力値: {sizeOutput}");

        Console.WriteLine("\nis_empty");
        bool isEmptyOutput = setData.IsEmpty();
        Console.WriteLine($"  出力値: {isEmptyOutput}");

        Console.WriteLine("\nget_value");
        int[] indexInput = { 0, 2, 5 };
        foreach (var index in indexInput)
        {
            Console.WriteLine($"  入力値: {index}");
            object valueOutput = setData.GetValue(index);
            Console.WriteLine($"  出力値: {valueOutput}");
        }

        Console.WriteLine("\nget_index");
        object[] itemInput = { 30, 99 };
        foreach (var item in itemInput)
        {
            Console.WriteLine($"  入力値: {item}");
            int indexOutput = setData.GetIndex(item);
            Console.WriteLine($"  出力値: {indexOutput}");
        }

        Console.WriteLine("\nremove");
        object[] removeInput = { 20, 50, 10 };
        foreach (var item in removeInput)
        {
            Console.WriteLine($"  入力値: {item}");
            bool removeOutput = setData.Remove(item);
            Console.WriteLine($"  出力値: {removeOutput}");
            Console.WriteLine($"  現在のデータ: [{string.Join(", ", setData.Get())}]");
        }

        Console.WriteLine("\nsize");
        sizeOutput = setData.Size();
        Console.WriteLine($"  出力値: {sizeOutput}");

        Console.WriteLine("\nclear");
        bool clearOutput = setData.Clear();
        Console.WriteLine($"  出力値: {clearOutput}");
        Console.WriteLine($"  現在のデータ: [{string.Join(", ", setData.Get())}]");

        Console.WriteLine("\nis_empty");
        isEmptyOutput = setData.IsEmpty();
        Console.WriteLine($"  出力値: {isEmptyOutput}");

        Console.WriteLine("\nSet TEST <----- end");
    }
}