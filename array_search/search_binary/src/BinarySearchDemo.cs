// C#
// 配列の検索: 二分探索 (Binary Search)

using System;
using System.Collections.Generic;

namespace BinarySearchDemo
{
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
            int left = 0;
            int right = _data.Count - 1;

            while (left <= right)
            {
                int mid = (left + right) / 2;

                // 中央の要素が目標値と一致
                if (_data[mid] == target)
                {
                    return mid;
                }
                // 中央の要素が目標値より小さい場合、右半分を探索
                else if (_data[mid] < target)
                {
                    left = mid + 1;
                }
                // 中央の要素が目標値より大きい場合、左半分を探索
                else
                {
                    right = mid - 1;
                }
            }

            // 目標値が見つからない場合
            return -1;
        }
    }

    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("BinarySearch TEST -----> start");

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

            Console.WriteLine("\nBinarySearch TEST <----- end");
        }
    }
}