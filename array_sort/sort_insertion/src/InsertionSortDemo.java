// Java
// 配列の並び替え: 挿入ソート (Insertion Sort)

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

    public boolean sort() {
        // 配列の長さを取得
        int n = this.data.length;
        
        // 2番目の要素から始める（最初の要素は既にソート済みと見なす）
        for (int i = 1; i < n; i++) {
            // 現在の要素を取得
            int key = this.data[i];
            
            // ソート済み部分の最後の要素のインデックス
            int j = i - 1;
            
            // keyより大きい要素をすべて右にシフト
            while (j >= 0 && this.data[j] > key) {
                this.data[j + 1] = this.data[j];
                j--;
            }
            
            // 適切な位置にkeyを挿入
            this.data[j + 1] = key;
        }
        
        return true;
    }
}

public class InsertionSortDemo {
    public static void main(String[] args) {
        System.out.println("InsertionSort TEST -----> start");

        ArrayData arrayData = new ArrayData();

        // ランダムな整数の配列
        System.out.println("\nsort");
        int[] input1 = {64, 34, 25, 12, 22, 11, 90};
        System.out.println("  ソート前: " + Arrays.toString(input1));
        arrayData.set(input1);
        arrayData.sort();
        System.out.println("  ソート後: " + Arrays.toString(arrayData.get()));
        
        // 既にソートされている配列
        System.out.println("\nsort");
        int[] input2 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        System.out.println("  ソート前: " + Arrays.toString(input2));
        arrayData.set(input2);
        arrayData.sort();
        System.out.println("  ソート後: " + Arrays.toString(arrayData.get()));
        
        // 逆順の配列
        System.out.println("\nsort");
        int[] input3 = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        System.out.println("  ソート前: " + Arrays.toString(input3));
        arrayData.set(input3);
        arrayData.sort();
        System.out.println("  ソート後: " + Arrays.toString(arrayData.get()));
        
        // 重複要素を含む配列
        System.out.println("\nsort");
        int[] input4 = {10, 9, 8, 7, 6, 10, 9, 8, 7, 6};
        System.out.println("  ソート前: " + Arrays.toString(input4));
        arrayData.set(input4);
        arrayData.sort();
        System.out.println("  ソート後: " + Arrays.toString(arrayData.get()));
        
        // 空の配列
        System.out.println("\nsort");
        int[] input5 = {};
        System.out.println("  ソート前: " + Arrays.toString(input5));
        arrayData.set(input5);
        arrayData.sort();
        System.out.println("  ソート後: " + Arrays.toString(arrayData.get()));

        System.out.println("\nInsertionSort TEST <----- end");
    }
}