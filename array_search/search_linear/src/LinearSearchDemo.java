// Java
// 配列の検索: 線形探索 (Linear Search)

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
        // 配列の要素を順番に確認
        for (int i = 0; i < this.data.length; i++) {
            // 目的の値が見つかった場合、そのインデックスを返す
            if (this.data[i] == target) {
                return i;
            }
        }
        
        // 見つからなかった場合は -1 を返す
        return -1;
    }
}

public class LinearSearchDemo {
    public static void main(String[] args) {
        System.out.println("LinearSearch TEST -----> start");

        System.out.println("\nnew");
        ArrayData arrayData = new ArrayData();
        int[] input = {1, 3, 5, 7, 9, 11, 13, 15, 17, 19};
        arrayData.set(input);
        System.out.println("  現在のデータ: " + Arrays.toString(arrayData.get()));
        
        System.out.println("\nsearch");
        int searchValue = 7;
        System.out.println("  入力値: " + searchValue);
        int output = arrayData.search(searchValue);
        System.out.println("  出力値: " + output);

        System.out.println("\nsearch");
        searchValue = 30;
        System.out.println("  入力値: " + searchValue);
        output = arrayData.search(searchValue);
        System.out.println("  出力値: " + output);

        System.out.println("\nLinearSearch TEST <----- end");
    }
}