// Java
// 配列の並び替え: クイックソート (Quick Sort)

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ArrayData {
    private List<Integer> data;

    public ArrayData() {
        this.data = new ArrayList<>();
    }

    public List<Integer> get() {
        return this.data;
    }

    public boolean set(List<Integer> data) {
        this.data = new ArrayList<>(data);
        return true;
    }

    private List<Integer> quickSort(List<Integer> target) {
        // 空の配列または要素が1つの場合はそのまま返す（基底条件）
        if (target.size() <= 1) {
            return target;
        }
        
        // ピボットを選択（この実装では最後の要素を選択）
        Integer pivot = target.get(target.size() - 1);
        
        // ピボットより小さい要素と大きい要素に分ける
        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();
        
        // 最後の要素（ピボット）を除いて配列をスキャン
        for (int i = 0; i < target.size() - 1; i++) {
            if (target.get(i) <= pivot) {
                left.add(target.get(i));
            } else {
                right.add(target.get(i));
            }
        }
        
        // 左側の部分配列、ピボット、右側の部分配列を再帰的にソートして結合
        List<Integer> result = new ArrayList<>();
        result.addAll(quickSort(left));
        result.add(pivot);
        result.addAll(quickSort(right));
        
        return result;
    }

    public boolean sort() {
        this.data = quickSort(this.data);
        return true;
    }
}

public class QuickSortDemo {
    public static void main(String[] args) {
        System.out.println("QuickSort TEST -----> start");

        ArrayData arrayData = new ArrayData();

        // ランダムな整数の配列
        System.out.println("\nsort");
        List<Integer> input = Arrays.asList(64, 34, 25, 12, 22, 11, 90);
        System.out.println("  ソート前: " + input);
        arrayData.set(input);
        arrayData.sort();
        System.out.println("  ソート後: " + arrayData.get());
        
        // 既にソートされている配列
        System.out.println("\nsort");
        input = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        System.out.println("  ソート前: " + input);
        arrayData.set(input);
        arrayData.sort();
        System.out.println("  ソート後: " + arrayData.get());
        
        // 逆順の配列
        System.out.println("\nsort");
        input = Arrays.asList(10, 9, 8, 7, 6, 5, 4, 3, 2, 1);
        System.out.println("  ソート前: " + input);
        arrayData.set(input);
        arrayData.sort();
        System.out.println("  ソート後: " + arrayData.get());
        
        // 重複要素を含む配列
        System.out.println("\nsort");
        input = Arrays.asList(10, 9, 8, 7, 6, 10, 9, 8, 7, 6);
        System.out.println("  ソート前: " + input);
        arrayData.set(input);
        arrayData.sort();
        System.out.println("  ソート後: " + arrayData.get());
        
        // 空の配列
        System.out.println("\nsort");
        input = new ArrayList<>();
        System.out.println("  ソート前: " + input);
        arrayData.set(input);
        arrayData.sort();
        System.out.println("  ソート後: " + arrayData.get());

        System.out.println("\nQuickSort TEST <----- end");
    }
}