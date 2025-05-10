using System;
using System.Collections.Generic;

namespace HeapSortDemo
{
    class HeapData
    {
        private List<int> _data;
        public bool IsMinHeap { get; set; }

        public HeapData(bool isMinHeap = true)
        {
            _data = new List<int>();
            IsMinHeap = isMinHeap;
        }

        private int GetParentIdx(int idx)
        {
            if (idx <= 0)
                return -1; // 根ノードには親がない
            return (idx - 1) / 2;
        }

        private int GetLeftChildIdx(int idx)
        {
            return 2 * idx + 1;
        }

        private int GetRightChildIdx(int idx)
        {
            return 2 * idx + 2;
        }

        private bool HasParent(int idx)
        {
            return GetParentIdx(idx) >= 0 && GetParentIdx(idx) < _data.Count;
        }

        private bool HasLeftChild(int idx)
        {
            return GetLeftChildIdx(idx) < _data.Count;
        }

        private bool HasRightChild(int idx)
        {
            return GetRightChildIdx(idx) < _data.Count;
        }

        private void Swap(int idx1, int idx2)
        {
            if (0 <= idx1 && idx1 < _data.Count && 0 <= idx2 && idx2 < _data.Count)
            {
                int temp = _data[idx1];
                _data[idx1] = _data[idx2];
                _data[idx2] = temp;
            }
            else
            {
                Console.WriteLine($"Warning: Swap indices out of bounds: {idx1}, {idx2}");
            }
        }

        private bool ShouldSwap(int idx1, int idx2)
        {
            if (IsMinHeap)
                return _data[idx1] > _data[idx2];
            else
                return _data[idx1] < _data[idx2];
        }

        private void HeapifyDown(int idx)
        {
            int smallestOrLargest = idx;

            int leftChildIdx = GetLeftChildIdx(idx);
            int rightChildIdx = GetRightChildIdx(idx);

            if (HasLeftChild(idx))
            {
                if (IsMinHeap)
                {
                    if (_data[leftChildIdx] < _data[smallestOrLargest])
                        smallestOrLargest = leftChildIdx;
                }
                else
                {
                    if (_data[leftChildIdx] > _data[smallestOrLargest])
                        smallestOrLargest = leftChildIdx;
                }
            }

            if (HasRightChild(idx))
            {
                if (IsMinHeap)
                {
                    if (_data[rightChildIdx] < _data[smallestOrLargest])
                        smallestOrLargest = rightChildIdx;
                }
                else
                {
                    if (_data[rightChildIdx] > _data[smallestOrLargest])
                        smallestOrLargest = rightChildIdx;
                }
            }

            if (smallestOrLargest != idx)
            {
                Swap(idx, smallestOrLargest);
                HeapifyDown(smallestOrLargest);
            }
        }

        private void MaxHeapifyDown(int idx, int heapSize)
        {
            int largest = idx;

            int leftChildIdx = GetLeftChildIdx(idx);
            int rightChildIdx = GetRightChildIdx(idx);

            if (leftChildIdx < heapSize && _data[leftChildIdx] > _data[largest])
                largest = leftChildIdx;

            if (rightChildIdx < heapSize && _data[rightChildIdx] > _data[largest])
                largest = rightChildIdx;

            if (largest != idx)
            {
                Swap(idx, largest);
                MaxHeapifyDown(largest, heapSize);
            }
        }

        private void HeapifyUp(int idx)
        {
            while (HasParent(idx))
            {
                int parentIdx = GetParentIdx(idx);
                if (ShouldSwap(parentIdx, idx))
                {
                    Swap(parentIdx, idx);
                    idx = parentIdx;
                }
                else
                {
                    break; // Heap property satisfied
                }
            }
        }

        public List<int> Get()
        {
            return _data;
        }

        public bool Heapify(List<int> array)
        {
            _data = new List<int>(array);
            int n = _data.Count;
            // 最後の非葉ノードから根に向かって、各部分木をヒープ化
            for (int i = n / 2 - 1; i >= 0; i--)
            {
                HeapifyDown(i);
            }
            return true;
        }

        public bool Sort()
        {
            int n = _data.Count;

            // 配列を最大ヒープに変換する
            for (int i = n / 2 - 1; i >= 0; i--)
            {
                MaxHeapifyDown(i, n);
            }

            // 最大ヒープから要素を一つずつ取り出してソート済みの位置に移動
            for (int i = n - 1; i > 0; i--)
            {
                // 現在の根 (最大値) をヒープの最後の要素と交換
                Swap(0, i);

                // 交換された要素 (元の最大値) は正しい位置に置かれたので、
                // 次のヒープ調整からは除外する。ヒープサイズは i に減少。
                // 新しい根 (元のヒープの最後の要素だったもの) から
                // MaxHeapifyDown を使って残りの要素を最大ヒープに調整
                MaxHeapifyDown(0, i);
            }

            return true;
        }
    }

    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("HeapSort TEST -----> start");

            HeapData heapData = new HeapData();

            // ランダムな整数の配列
            Console.WriteLine("\nsort");
            List<int> input1 = new List<int> { 64, 34, 25, 12, 22, 11, 90 };
            Console.WriteLine($"  ソート前: [{string.Join(", ", input1)}]");
            heapData.Heapify(input1);
            heapData.Sort();
            Console.WriteLine($"  ソート後: [{string.Join(", ", heapData.Get())}]");

            // 既にソートされている配列
            Console.WriteLine("\nsort");
            List<int> input2 = new List<int> { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
            Console.WriteLine($"  ソート前: [{string.Join(", ", input2)}]");
            heapData.Heapify(input2);
            heapData.Sort();
            Console.WriteLine($"  ソート後: [{string.Join(", ", heapData.Get())}]");

            // 逆順の配列
            Console.WriteLine("\nsort");
            List<int> input3 = new List<int> { 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 };
            Console.WriteLine($"  ソート前: [{string.Join(", ", input3)}]");
            heapData.Heapify(input3);
            heapData.Sort();
            Console.WriteLine($"  ソート後: [{string.Join(", ", heapData.Get())}]");

            // 重複要素を含む配列
            Console.WriteLine("\nsort");
            List<int> input4 = new List<int> { 10, 9, 8, 7, 6, 10, 9, 8, 7, 6 };
            Console.WriteLine($"  ソート前: [{string.Join(", ", input4)}]");
            heapData.Heapify(input4);
            heapData.Sort();
            Console.WriteLine($"  ソート後: [{string.Join(", ", heapData.Get())}]");

            // 空の配列
            Console.WriteLine("\nsort");
            List<int> input5 = new List<int>();
            Console.WriteLine($"  ソート前: [{string.Join(", ", input5)}]");
            heapData.Heapify(input5);
            heapData.Sort();
            Console.WriteLine($"  ソート後: [{string.Join(", ", heapData.Get())}]");

            Console.WriteLine("\nHeapSort TEST <----- end");
        }
    }
}