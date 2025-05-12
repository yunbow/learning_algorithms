// C#
// データ構造: スタック (Stack)

using System;
using System.Collections.Generic;

class StackData
{
    private List<int> _data;

    public StackData()
    {
        _data = new List<int>();
    }

    public List<int> Get()
    {
        return _data;
    }

    public int GetIndex(int item)
    {
        try
        {
            int index = _data.IndexOf(item);
            return index;
        }
        catch (Exception)
        {
            Console.WriteLine($"ERROR: {item} は範囲外です");
            return -1;
        }
    }

    public int? GetValue(int index)
    {
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

    public bool Push(int item)
    {
        _data.Add(item);
        return true;
    }

    public bool Pop()
    {
        if (!IsEmpty())
        {
            _data.RemoveAt(_data.Count - 1);
            return true;
        }
        else
        {
            Console.WriteLine("ERROR: 空です");
            return false;
        }
    }

    public int? Peek()
    {
        if (!IsEmpty())
        {
            return _data[_data.Count - 1];
        }
        else
        {
            return null;
        }
    }

    public bool IsEmpty()
    {
        return _data.Count == 0;
    }

    public int Size()
    {
        return _data.Count;
    }

    public bool Clear()
    {
        _data.Clear();
        return true;
    }
}

class Program
{
    static void Main(string[] args)
    {
        Console.WriteLine("Stack TEST -----> start");

        Console.WriteLine("\nnew");
        StackData stackData = new StackData();
        Console.WriteLine($"  現在のデータ: [{string.Join(", ", stackData.Get())}]");

        Console.WriteLine("\nis_empty");
        bool output = stackData.IsEmpty();
        Console.WriteLine($"  出力値: {output}");

        Console.WriteLine("\nsize");
        int sizeOutput = stackData.Size();
        Console.WriteLine($"  出力値: {sizeOutput}");

        Console.WriteLine("\npush");
        int[] itemsToPush = { 10, 20, 30, 40 };
        foreach (int item in itemsToPush)
        {
            Console.WriteLine($"  入力値: {item}");
            bool pushOutput = stackData.Push(item);
            Console.WriteLine($"  出力値: {pushOutput}");
            Console.WriteLine($"  現在のデータ: [{string.Join(", ", stackData.Get())}]");
        }

        Console.WriteLine("\nsize");
        sizeOutput = stackData.Size();
        Console.WriteLine($"  出力値: {sizeOutput}");

        Console.WriteLine("\nis_empty");
        output = stackData.IsEmpty();
        Console.WriteLine($"  出力値: {output}");

        Console.WriteLine("\npeek");
        int? peekOutput = stackData.Peek();
        Console.WriteLine($"  出力値: {peekOutput}");

        Console.WriteLine("\nget_index");
        int input = 30;
        Console.WriteLine($"  入力値: {input}");
        int indexOutput = stackData.GetIndex(input);
        Console.WriteLine($"  出力値: {indexOutput}");

        Console.WriteLine("\nget_index");
        input = 50;
        Console.WriteLine($"  入力値: {input}");
        indexOutput = stackData.GetIndex(input);
        Console.WriteLine($"  出力値: {indexOutput}");

        Console.WriteLine("\npop");
        while (!stackData.IsEmpty())
        {
            bool popOutput = stackData.Pop();
            Console.WriteLine($"  出力値: {popOutput}");
            Console.WriteLine($"  現在のデータ: [{string.Join(", ", stackData.Get())}]");
        }

        Console.WriteLine("\nis_empty");
        output = stackData.IsEmpty();
        Console.WriteLine($"  出力値: {output}");

        Console.WriteLine("\nsize");
        sizeOutput = stackData.Size();
        Console.WriteLine($"  出力値: {sizeOutput}");

        Console.WriteLine("\npop");
        bool finalPopOutput = stackData.Pop();
        Console.WriteLine($"  出力値: {finalPopOutput}");

        Console.WriteLine("\npeek");
        peekOutput = stackData.Peek();
        Console.WriteLine($"  出力値: {peekOutput}");

        Console.WriteLine("\nStack TEST <----- end");
    }
}