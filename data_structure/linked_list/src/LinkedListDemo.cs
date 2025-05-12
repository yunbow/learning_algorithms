// C#
// データ構造: 連結リスト (Linked List)

using System;
using System.Collections.Generic;

public class NodeData
{
    public object Data { get; set; }
    public NodeData Next { get; set; }

    public NodeData(object data)
    {
        Data = data;
        Next = null;
    }
}

public class LinkedListData
{
    private NodeData _data;
    private int _size;

    public LinkedListData()
    {
        _data = null;
        _size = 0;
    }

    public NodeData Get()
    {
        return _data;
    }

    public int GetPosition(object data)
    {
        if (IsEmpty())
            return -1;

        NodeData current = _data;
        int position = 0;

        while (current != null)
        {
            if (Equals(current.Data, data))
                return position;
            current = current.Next;
            position++;
        }

        return -1;
    }

    public object GetValue(int position)
    {
        if (IsEmpty() || position < 0 || position >= _size)
        {
            Console.WriteLine($"ERROR: {position} は範囲外です");
            return null;
        }

        NodeData current = _data;
        for (int i = 0; i < position; i++)
        {
            current = current.Next;
        }

        return current.Data;
    }

    public bool Add(object data, int? position = null)
    {
        NodeData newNode = new NodeData(data);

        if (IsEmpty())
        {
            _data = newNode;
            _size++;
            return true;
        }

        if (position == null || position >= _size)
        {
            NodeData current = _data;
            while (current.Next != null)
            {
                current = current.Next;
            }
            current.Next = newNode;
            _size++;
            return true;
        }

        if (position == 0)
        {
            newNode.Next = _data;
            _data = newNode;
            _size++;
            return true;
        }

        NodeData prev = _data;
        for (int i = 0; i < position - 1; i++)
        {
            prev = prev.Next;
        }

        newNode.Next = prev.Next;
        prev.Next = newNode;
        _size++;
        return true;
    }

    public bool Remove(int? position = null, object data = null)
    {
        if (IsEmpty())
        {
            Console.WriteLine("ERROR: リストが空です");
            return false;
        }

        if (data != null)
        {
            if (Equals(_data.Data, data))
            {
                _data = _data.Next;
                _size--;
                return true;
            }

            NodeData current = _data;
            while (current.Next != null && !Equals(current.Next.Data, data))
            {
                current = current.Next;
            }

            if (current.Next != null)
            {
                current.Next = current.Next.Next;
                _size--;
                return true;
            }
            else
            {
                Console.WriteLine($"ERROR: {data} は範囲外です");
                return false;
            }
        }

        if (position == null)
        {
            position = _size - 1;
        }

        if (position < 0 || position >= _size)
        {
            Console.WriteLine($"ERROR: {position} は範囲外です");
            return false;
        }

        if (position == 0)
        {
            _data = _data.Next;
            _size--;
            return true;
        }

        NodeData prev = _data;
        for (int i = 0; i < position - 1; i++)
        {
            prev = prev.Next;
        }

        prev.Next = prev.Next.Next;
        _size--;
        return true;
    }

    public bool Update(int position, object data)
    {
        if (IsEmpty() || position < 0 || position >= _size)
        {
            Console.WriteLine($"ERROR: {position} は範囲外です");
            return false;
        }

        NodeData current = _data;
        for (int i = 0; i < position; i++)
        {
            current = current.Next;
        }

        current.Data = data;
        return true;
    }

    public bool IsEmpty()
    {
        return _data == null;
    }

    public int Size()
    {
        return _size;
    }

    public bool Clear()
    {
        _data = null;
        _size = 0;
        return true;
    }

    public List<object> Display()
    {
        List<object> elements = new List<object>();
        NodeData current = _data;
        while (current != null)
        {
            elements.Add(current.Data);
            current = current.Next;
        }
        return elements;
    }
}

class Program
{
    static void Main(string[] args)
    {
        Console.WriteLine("LinkedList TEST -----> start");

        Console.WriteLine("\nnew");
        LinkedListData linkedListData = new LinkedListData();
        Console.WriteLine($"  現在のデータ: {string.Join(", ", linkedListData.Display())}");

        Console.WriteLine("\nis_empty");
        bool output = linkedListData.IsEmpty();
        Console.WriteLine($"  出力値: {output}");

        Console.WriteLine("\nsize");
        int sizeOutput = linkedListData.Size();
        Console.WriteLine($"  出力値: {sizeOutput}");

        Console.WriteLine("\nadd");
        int input = 10;
        Console.WriteLine($"  入力値: {input}");
        bool addOutput = linkedListData.Add(input);
        Console.WriteLine($"  出力値: {addOutput}");
        Console.WriteLine($"  現在のデータ: {string.Join(", ", linkedListData.Display())}");

        Console.WriteLine("\nadd");
        input = 20;
        Console.WriteLine($"  入力値: {input}");
        addOutput = linkedListData.Add(input);
        Console.WriteLine($"  出力値: {addOutput}");
        Console.WriteLine($"  現在のデータ: {string.Join(", ", linkedListData.Display())}");

        Console.WriteLine("\nadd");
        object inputTuple = new { Data = 5, Position = 0 };
        Console.WriteLine($"  入力値: Data={((dynamic)inputTuple).Data}, Position={((dynamic)inputTuple).Position}");
        addOutput = linkedListData.Add(((dynamic)inputTuple).Data, ((dynamic)inputTuple).Position);
        Console.WriteLine($"  出力値: {addOutput}");
        Console.WriteLine($"  現在のデータ: {string.Join(", ", linkedListData.Display())}");

        Console.WriteLine("\nadd");
        inputTuple = new { Data = 15, Position = 2 };
        Console.WriteLine($"  入力値: Data={((dynamic)inputTuple).Data}, Position={((dynamic)inputTuple).Position}");
        addOutput = linkedListData.Add(((dynamic)inputTuple).Data, ((dynamic)inputTuple).Position);
        Console.WriteLine($"  出力値: {addOutput}");
        Console.WriteLine($"  現在のデータ: {string.Join(", ", linkedListData.Display())}");

        Console.WriteLine("\nget_value");
        int inputPosition = 1;
        Console.WriteLine($"  入力値: {inputPosition}");
        int positionOutput = linkedListData.GetPosition(inputPosition);
        Console.WriteLine($"  出力値: {positionOutput}");

        Console.WriteLine("\nget_value");
        input = 10;
        Console.WriteLine($"  入力値: {input}");
        positionOutput = linkedListData.GetPosition(input);
        Console.WriteLine($"  出力値: {positionOutput}");

        Console.WriteLine("\nupdate");
        dynamic updateInput = new { Position = 1, Data = 99 };
        Console.WriteLine($"  入力値: Position={updateInput.Position}, Data={updateInput.Data}");
        bool updateOutput = linkedListData.Update(updateInput.Position, updateInput.Data);
        Console.WriteLine($"  出力値: {updateOutput}");
        Console.WriteLine($"  現在のデータ: {string.Join(", ", linkedListData.Display())}");

        Console.WriteLine("\nget_value");
        input = 15;
        Console.WriteLine($"  入力値: {input}");
        object valueOutput = linkedListData.GetValue(input);
        Console.WriteLine($"  出力値: {valueOutput}");

        Console.WriteLine("\nget_value");
        input = 100;
        Console.WriteLine($"  入力値: {input}");
        valueOutput = linkedListData.GetValue(input);
        Console.WriteLine($"  出力値: {valueOutput}");

        Console.WriteLine("\nremove");
        input = 15;
        Console.WriteLine($"  入力値: data={input}");
        bool removeOutput = linkedListData.Remove(data: input);
        Console.WriteLine($"  出力値: {removeOutput}");
        Console.WriteLine($"  現在のデータ: {string.Join(", ", linkedListData.Display())}");

        Console.WriteLine("\nremove");
        int removePosition = 0;
        Console.WriteLine($"  入力値: position={removePosition}");
        removeOutput = linkedListData.Remove(position: removePosition);
        Console.WriteLine($"  出力値: {removeOutput}");
        Console.WriteLine($"  現在のデータ: {string.Join(", ", linkedListData.Display())}");

        Console.WriteLine("\nremove");
        removeOutput = linkedListData.Remove();
        Console.WriteLine($"  出力値: {removeOutput}");
        Console.WriteLine($"  現在のデータ: {string.Join(", ", linkedListData.Display())}");

        Console.WriteLine("\nremove");
        removePosition = 5;
        Console.WriteLine($"  入力値: position={removePosition}");
        removeOutput = linkedListData.Remove(position: removePosition);
        Console.WriteLine($"  出力値: {removeOutput}");
        Console.WriteLine($"  現在のデータ: {string.Join(", ", linkedListData.Display())}");

        Console.WriteLine("\nclear");
        bool clearOutput = linkedListData.Clear();
        Console.WriteLine($"  出力値: {clearOutput}");
        Console.WriteLine($"  現在のデータ: {string.Join(", ", linkedListData.Display())}");

        Console.WriteLine("\nis_empty");
        output = linkedListData.IsEmpty();
        Console.WriteLine($"  出力値: {output}");

        Console.WriteLine("\nsize");
        sizeOutput = linkedListData.Size();
        Console.WriteLine($"出力値: {sizeOutput}");

        Console.WriteLine("\nremove");
        removeOutput = linkedListData.Remove();
        Console.WriteLine($"  出力値: {removeOutput}");
        Console.WriteLine($"  現在のデータ: {string.Join(", ", linkedListData.Display())}");

        Console.WriteLine("\nLinkedList TEST <----- end");
    }
}
