// Java
// 配列の並び替え: 選択ソート (Selection Sort)

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

    private int[] selectionSort(int[] target) {
        // 配列の長さを取得
        int n = target.length;
        
        // 配列を順番に走査
        for (int i = 0; i < n; i++) {
            // 未ソート部分の最小値のインデックスを見つける
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (target[j] < target[minIndex]) {
                    minIndex = j;
                }
            }
            
            // 見つかった最小値と現在の位置を交換
            int temp = target[i];
            target[i] = target[minIndex];
            target[minIndex] = temp;
        }
        
        return target;
    }

    public boolean sort() {
        selectionSort(this.data);
        return true;
    }
}

public class SelectionSortDemo {
    public static void main(String[] args) {
        System.out.println("SelectionSort TEST -----> start");

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

        System.out.println("\nSelectionSort TEST <----- end");
    }
}