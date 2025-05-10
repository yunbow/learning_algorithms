// Java
// 配列の検索: 二分探索 (Binary Search)

import java.util.Arrays;

class ArrayData {
    private int[] data;

    public ArrayData() {
        this.data = new int[0];
    }

    public int[] get() {
        return this.data;
    }

    public boolean set(int[] data) {
        this.data = data;
        return true;
    }

    public int search(int target) {
        int left = 0;
        int right = this.data.length - 1;
        
        while (left <= right) {
            int mid = (left + right) / 2;
            
            // 中央の要素が目標値と一致
            if (this.data[mid] == target) {
                return mid;
            }
            
            // 中央の要素が目標値より小さい場合、右半分を探索
            else if (this.data[mid] < target) {
                left = mid + 1;
            }
            
            // 中央の要素が目標値より大きい場合、左半分を探索
            else {
                right = mid - 1;
            }
        }
        
        // 目標値が見つからない場合
        return -1;
    }
}

public class BinarySearchDemo {
    public static void main(String[] args) {
        System.out.println("BinarySearch TEST -----> start");

        System.out.println("\nnew");
        ArrayData arrayData = new ArrayData();
        int[] input = {1, 3, 5, 7, 9, 11, 13, 15, 17, 19};
        arrayData.set(input);
        System.out.println("  現在のデータ: " + Arrays.toString(arrayData.get()));
        
        System.out.println("\nsearch");
        int searchInput = 7;
        System.out.println("  入力値: " + searchInput);
        int output = arrayData.search(searchInput);
        System.out.println("  出力値: " + output);

        System.out.println("\nsearch");
        searchInput = 30;
        System.out.println("  入力値: " + searchInput);
        output = arrayData.search(searchInput);
        System.out.println("  出力値: " + output);

        System.out.println("\nBinarySearch TEST <----- end");
    }
}