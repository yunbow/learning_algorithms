// Java
// 配列の並び替え: バブルソート (Bubble Sort)

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
        int n = this.data.length;
        
        // 外側のループ: n-1回の走査が必要
        for (int i = 0; i < n; i++) {
            // 最適化: 一度の走査で交換がなければソート完了
            boolean swapped = false;
            
            // 内側のループ: まだソートされていない部分を走査
            // 各走査後に最大の要素が末尾に移動するため、i回分を除外
            for (int j = 0; j < n - i - 1; j++) {
                // 隣接する要素を比較し、必要に応じて交換
                if (this.data[j] > this.data[j + 1]) {
                    // 要素の交換
                    int temp = this.data[j];
                    this.data[j] = this.data[j + 1];
                    this.data[j + 1] = temp;
                    swapped = true;
                }
            }
            
            // 交換が発生しなければソート完了
            if (!swapped) {
                break;
            }
        }
        return true;
    }
}

public class BubbleSortDemo {
    public static void main(String[] args) {
        System.out.println("BubbleSort TEST -----> start");
        
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
        
        System.out.println("\nBubbleSort TEST <----- end");
    }
}