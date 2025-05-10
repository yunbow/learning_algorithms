// C#
// データ構造: ヒープ (Heap)

using System;
using System.Collections.Generic;

class HeapData
{
    private List<int> _data;
    public bool IsMinHeap { get; }

    public HeapData(bool isMinHeap = true)
    {
        _data = new List<int>();
        IsMinHeap = isMinHeap;
    }

    private int GetParentIdx(int idx)
    {
        // 親ノードのインデックスを計算
        return (idx - 1) / 2;
    }

    private int GetLeftChildIdx(int idx)
    {
        // 左の子ノードのインデックスを計算
        return 2 * idx + 1;
    }

    private int GetRightChildIdx(int idx)
    {
        // 右の子ノードのインデックスを計算
        return 2 * idx + 2;
    }

    private bool HasParent(int idx)
    {
        // 親ノードが存在するか確認
        return GetParentIdx(idx) >= 0;
    }

    private bool HasLeftChild(int idx)
    {
        // 左の子ノードが存在するか確認
        return GetLeftChildIdx(idx) < _data.Count;
    }

    private bool HasRightChild(int idx)
    {
        // 右の子ノードが存在するか確認
        return GetRightChildIdx(idx) < _data.Count;
    }

    private int GetParent(int idx)
    {
        // 親ノードの値を取得
        return _data[GetParentIdx(idx)];
    }

    private int GetLeftChild(int idx)
    {
        // 左の子ノードの値を取得
        return _data[GetLeftChildIdx(idx)];
    }

    private int GetRightChild(int idx)
    {
        // 右の子ノードの値を取得
        return _data[GetRightChildIdx(idx)];
    }

    private void Swap(int idx1, int idx2)
    {
        // 2つのノードの値を交換
        int temp = _data[idx1];
        _data[idx1] = _data[idx2];
        _data[idx2] = temp;
    }

    private bool ShouldSwap(int idx1, int idx2)
    {
        // 最小ヒープでは親が子より大きい場合に交換
        // 最大ヒープでは親が子より小さい場合に交換
        if (IsMinHeap)
        {
            return _data[idx1] > _data[idx2];
        }
        else
        {
            return _data[idx1] < _data[idx2];
        }
    }

    private void HeapifyDown(int idx)
    {
        // ノードを下方向に移動させてヒープ条件を満たす
        int smallestOrLargest = idx;

        // 左の子ノードと比較
        if (HasLeftChild(idx) && ShouldSwap(smallestOrLargest, GetLeftChildIdx(idx)))
        {
            smallestOrLargest = GetLeftChildIdx(idx);
        }

        // 右の子ノードと比較
        if (HasRightChild(idx) && ShouldSwap(smallestOrLargest, GetRightChildIdx(idx)))
        {
            smallestOrLargest = GetRightChildIdx(idx);
        }

        // インデックスが変わっていたら交換して再帰的に処理
        if (smallestOrLargest != idx)
        {
            Swap(idx, smallestOrLargest);
            HeapifyDown(smallestOrLargest);
        }
    }

    private void HeapifyUp(int idx)
    {
        // ノードを上方向に移動させてヒープ条件を満たす
        // 親がある限り、親と比較して必要なら交換
        while (HasParent(idx) && ShouldSwap(GetParentIdx(idx), idx))
        {
            int parentIdx = GetParentIdx(idx);
            Swap(parentIdx, idx);
            idx = parentIdx;
        }
    }

    public List<int> Get()
    {
        // 要素を取得
        return _data;
    }

    public int GetIndex(int item)
    {
        // 配列内で指定された値を検索し、最初に見つかったインデックスを返す
        int index = _data.IndexOf(item);
        if (index == -1)
        {
            Console.WriteLine($"ERROR: {item} は範囲外です");
        }
        return index;
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

    public bool Heapify(List<int> array)
    {
        // 配列をヒープに変換
        _data = new List<int>(array);
        // 最後の親ノードから根に向かって、各部分木をヒープ化
        for (int i = _data.Count / 2 - 1; i >= 0; i--)
        {
            HeapifyDown(i);
        }
        return true;
    }

    public bool Push(int value)
    {
        // ヒープに要素を追加
        _data.Add(value);
        // 最後の要素を適切な位置に移動
        HeapifyUp(_data.Count - 1);
        return true;
    }

    public bool Pop()
    {
        // ヒープが空の場合
        if (_data.Count == 0)
        {
            return false;
        }

        // 最後の要素をルートに移動
        int lastElement = _data[_data.Count - 1];
        _data.RemoveAt(_data.Count - 1);

        if (_data.Count > 0)
        {
            _data[0] = lastElement;
            // ルートから下方向にヒープ条件を満たすように調整
            HeapifyDown(0);
        }

        return true;
    }

    public int? Peek()
    {
        // ヒープが空の場合
        if (_data.Count == 0)
        {
            return null;
        }
        // ルート要素を返す（取り出さない）
        return _data[0];
    }

    public bool IsEmpty()
    {
        // ヒープが空かどうかを確認
        return _data.Count == 0;
    }

    public int Size()
    {
        // ヒープのサイズを返す
        return _data.Count;
    }

    public bool Clear()
    {
        // ヒープをクリア
        _data.Clear();
        return true;
    }
}

class Program
{
    static void Main(string[] args)
    {
        Console.WriteLine("Heap TEST -----> start");

        Console.WriteLine("\nmin heap: new");
        HeapData minHeap = new HeapData(isMinHeap: true);
        Console.WriteLine($"  現在のデータ: [{string.Join(", ", minHeap.Get())}]");

        Console.WriteLine("\nmin heap: heapify");
        List<int> input = new List<int> { 4, 10, 3, 5, 1 };
        Console.WriteLine($"  入力値: [{string.Join(", ", input)}]");
        bool output = minHeap.Heapify(input);
        Console.WriteLine($"  出力値: {output}");
        Console.WriteLine($"  現在のデータ: [{string.Join(", ", minHeap.Get())}]");

        Console.WriteLine("\nmin heap: push");
        int pushInput = 2;
        Console.WriteLine($"  入力値: {pushInput}");
        output = minHeap.Push(pushInput);
        Console.WriteLine($"  出力値: {output}");
        Console.WriteLine($"  現在のデータ: [{string.Join(", ", minHeap.Get())}]");

        Console.WriteLine("\nmin heap: push");
        pushInput = 15;
        Console.WriteLine($"  入力値: {pushInput}");
        output = minHeap.Push(pushInput);
        Console.WriteLine($"  出力値: {output}");
        Console.WriteLine($"  現在のデータ: [{string.Join(", ", minHeap.Get())}]");

        Console.WriteLine("\nmin heap: peek");
        int? peekOutput = minHeap.Peek();
        Console.WriteLine($"  出力値: {peekOutput}");

        Console.WriteLine("\nmin heap: pop");
        output = minHeap.Pop();
        Console.WriteLine($"  出力値: {output}");
        Console.WriteLine($"  現在のデータ: [{string.Join(", ", minHeap.Get())}]");

        Console.WriteLine("\nmin heap: pop");
        output = minHeap.Pop();
        Console.WriteLine($"  出力値: {output}");
        Console.WriteLine($"  現在のデータ: [{string.Join(", ", minHeap.Get())}]");

        Console.WriteLine("\nmin heap: get_index");
        int indexInput = 3;
        Console.WriteLine($"  入力値: {indexInput}");
        int indexOutput = minHeap.GetIndex(indexInput);
        Console.WriteLine($"  出力値: {indexOutput}");

        Console.WriteLine("\nmin heap: get_index");
        indexInput = 100;
        Console.WriteLine($"  入力値: {indexInput}");
        indexOutput = minHeap.GetIndex(indexInput);
        Console.WriteLine($"  出力値: {indexOutput}");

        Console.WriteLine("\nmin heap: is_empty");
        bool emptyOutput = minHeap.IsEmpty();
        Console.WriteLine($"  出力値: {emptyOutput}");

        Console.WriteLine("\nmin heap: size");
        int sizeOutput = minHeap.Size();
        Console.WriteLine($"  出力値: {sizeOutput}");

        Console.WriteLine("\nmin heap: clear");
        output = minHeap.Clear();
        Console.WriteLine($"  出力値: {output}");
        Console.WriteLine($"  現在のデータ: [{string.Join(", ", minHeap.Get())}]");

        Console.WriteLine("\nmin heap: is_empty");
        emptyOutput = minHeap.IsEmpty();
        Console.WriteLine($"  出力値: {emptyOutput}");

        Console.WriteLine("\nmax heap: new");
        HeapData maxHeap = new HeapData(isMinHeap: false);
        Console.WriteLine($"  現在のデータ: [{string.Join(", ", maxHeap.Get())}]");

        Console.WriteLine("\nmax heap: heapify");
        input = new List<int> { 4, 10, 3, 5, 1 };
        Console.WriteLine($"  入力値: [{string.Join(", ", input)}]");
        output = maxHeap.Heapify(input);
        Console.WriteLine($"  出力値: {output}");
        Console.WriteLine($"  現在のデータ: [{string.Join(", ", maxHeap.Get())}]");

        Console.WriteLine("\nmax heap: push");
        pushInput = 12;
        Console.WriteLine($"  入力値: {pushInput}");
        output = maxHeap.Push(pushInput);
        Console.WriteLine($"  出力値: {output}");
        Console.WriteLine($"  現在のデータ: [{string.Join(", ", maxHeap.Get())}]");

        Console.WriteLine("\nmax heap: push");
        pushInput = 0;
        Console.WriteLine($"  入力値: {pushInput}");
        output = maxHeap.Push(pushInput);
        Console.WriteLine($"  出力値: {output}");
        Console.WriteLine($"  現在のデータ: [{string.Join(", ", maxHeap.Get())}]");

        Console.WriteLine("\nmax heap: peek");
        peekOutput = maxHeap.Peek();
        Console.WriteLine($"  出力値: {peekOutput}");

        Console.WriteLine("\nmax heap: pop");
        output = maxHeap.Pop();
        Console.WriteLine($"  出力値: {output}");
        Console.WriteLine($"  現在のデータ: [{string.Join(", ", maxHeap.Get())}]");

        Console.WriteLine("\nmax heap: pop");
        output = maxHeap.Pop();
        Console.WriteLine($"  出力値: {output}");
        Console.WriteLine($"  現在のデータ: [{string.Join(", ", maxHeap.Get())}]");

        Console.WriteLine("\nmax heap: get_index");
        indexInput = 5;
        Console.WriteLine($"  入力値: {indexInput}");
        indexOutput = maxHeap.GetIndex(indexInput);
        Console.WriteLine($"  出力値: {indexOutput}");

        Console.WriteLine("\nmax heap: get_index");
        indexInput = -10;
        Console.WriteLine($"  入力値: {indexInput}");
        indexOutput = maxHeap.GetIndex(indexInput);
        Console.WriteLine($"  出力値: {indexOutput}");

        Console.WriteLine("\nmax heap: is_empty");
        emptyOutput = maxHeap.IsEmpty();
        Console.WriteLine($"  出力値: {emptyOutput}");

        Console.WriteLine("\nmax heap: size");
        sizeOutput = maxHeap.Size();
        Console.WriteLine($"  出力値: {sizeOutput}");

        Console.WriteLine("\nmax heap: clear");
        output = maxHeap.Clear();
        Console.WriteLine($"  出力値: {output}");
        Console.WriteLine($"  現在のデータ: [{string.Join(", ", maxHeap.Get())}]");

        Console.WriteLine("\nmax heap: is_empty");
        emptyOutput = maxHeap.IsEmpty();
        Console.WriteLine($"  出力値: {emptyOutput}");

        Console.WriteLine("\nHeap TEST <----- end");
    }
}