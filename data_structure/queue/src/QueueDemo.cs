// C#
// データ構造: キュー (Queue)

using System;
using System.Collections.Generic;

class QueueData
{
    private List<object> _data;

    public QueueData()
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
        // キュー内に指定した要素があるか検索
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

    public object GetValue(int index)
    {
        // 指定したインデックスの要素を取得
        if (0 <= index && index < _data.Count)
        {
            return _data[index];
        }
        else
        {
            Console.WriteLine($"Error: インデックス {index} は範囲外です");
            return null;
        }
    }

    public bool Enqueue(object item)
    {
        // キューの末尾に要素を追加
        _data.Add(item);
        return true;
    }

    public bool Dequeue()
    {
        // キューが空でない場合、先頭要素を取り出す
        if (!IsEmpty())
        {
            _data.RemoveAt(0);
            return true;
        }
        else
        {
            Console.WriteLine("ERROR: キューが空です");
            return false;
        }
    }

    public object Peek()
    {
        // キューが空でない場合、先頭要素を参照
        if (!IsEmpty())
        {
            return _data[0];
        }
        else
        {
            Console.WriteLine("ERROR: キューが空です");
            return null;
        }
    }

    public bool IsEmpty()
    {
        // キューが空かどうかを確認
        return _data.Count == 0;
    }

    public int Size()
    {
        // キューの要素数を返す
        return _data.Count;
    }

    public bool Clear()
    {
        // キューをクリア
        _data.Clear();
        return true;
    }
}

class Program
{
    static void Main(string[] args)
    {
        Console.WriteLine("Queue TEST -----> start");

        Console.WriteLine("\nnew");
        QueueData queueData = new QueueData();
        Console.WriteLine($"  現在のデータ: [{string.Join(", ", queueData.Get())}]");

        Console.WriteLine("\nis_empty");
        bool output1 = queueData.IsEmpty();
        Console.WriteLine($"  出力値: {output1}");

        Console.WriteLine("\nenqueue");
        int[] input = { 10, 20, 30 };
        foreach (int item in input)
        {
            Console.WriteLine($"  入力値: {item}");
            bool output2 = queueData.Enqueue(item);
            Console.WriteLine($"  出力値: {output2}");
            Console.WriteLine($"  現在のデータ: [{string.Join(", ", queueData.Get())}]");
        }

        Console.WriteLine("\nsize");
        int output3 = queueData.Size();
        Console.WriteLine($"  出力値: {output3}");

        Console.WriteLine("\npeek");
        object output4 = queueData.Peek();
        Console.WriteLine($"  出力値: {output4}");

        Console.WriteLine("\nget_index");
        int input2 = 20;
        Console.WriteLine($"  入力値: {input2}");
        int output5 = queueData.GetIndex(input2);
        Console.WriteLine($"  出力値: {output5}");

        Console.WriteLine("\nget_index");
        int input3 = 50;
        Console.WriteLine($"  入力値: {input3}");
        int output6 = queueData.GetIndex(input3);
        Console.WriteLine($"  出力値: {output6}");

        Console.WriteLine("\ndequeue");
        bool output7 = queueData.Dequeue();
        Console.WriteLine($"  出力値: {output7}");
        Console.WriteLine($"  現在のデータ: [{string.Join(", ", queueData.Get())}]");

        Console.WriteLine("\ndequeue");
        bool output8 = queueData.Dequeue();
        Console.WriteLine($"  出力値: {output8}");
        Console.WriteLine($"  現在のデータ: [{string.Join(", ", queueData.Get())}]");

        Console.WriteLine("\nsize");
        int output9 = queueData.Size();
        Console.WriteLine($"  出力値: {output9}");

        Console.WriteLine("\ndequeue");
        bool output10 = queueData.Dequeue();
        Console.WriteLine($"  出力値: {output10}");
        Console.WriteLine($"  現在のデータ: [{string.Join(", ", queueData.Get())}]");

        Console.WriteLine("\ndequeue");
        bool output11 = queueData.Dequeue();
        Console.WriteLine($"  出力値: {output11}");
        Console.WriteLine($"  現在のデータ: [{string.Join(", ", queueData.Get())}]");

        Console.WriteLine("\nis_empty");
        bool output12 = queueData.IsEmpty();
        Console.WriteLine($"  出力値: {output12}");

        Console.WriteLine("\nclear");
        bool output13 = queueData.Clear();
        Console.WriteLine($"  出力値: {output13}");
        Console.WriteLine($"  現在のデータ: [{string.Join(", ", queueData.Get())}]");

        Console.WriteLine("\nsize");
        int output14 = queueData.Size();
        Console.WriteLine($"  出力値: {output14}");

        Console.WriteLine("\nQueue TEST <----- end");
    }
}