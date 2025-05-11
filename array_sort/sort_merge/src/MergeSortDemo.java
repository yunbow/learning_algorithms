// Java
// 配列の並び替え: マージソート (Merge Sort)

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

class ArrayData {
    private List<Integer> data;

    public ArrayData() {
        this.data = new ArrayList<>();
    }

    public List<Integer> get() {
        return this.data;
    }

    public boolean set(List<Integer> data) {
        this.data = data;
        return true;
    }

    private List<Integer> mergeSort(List<Integer> target) {
        // 配列の長さが1以下の場合はそのまま返す（基本ケース）
        if (target.size() <= 1) {
            return target;
        }
        
        // 配列を半分に分割
        int mid = target.size() / 2;
        List<Integer> leftHalf = new ArrayList<>(target.subList(0, mid));
        List<Integer> rightHalf = new ArrayList<>(target.subList(mid, target.size()));
        
        // 左右の半分を再帰的にソート
        leftHalf = mergeSort(leftHalf);
        rightHalf = mergeSort(rightHalf);
        
        // ソート済みの半分同士をマージ
        return merge(leftHalf, rightHalf);
    }

    private List<Integer> merge(List<Integer> left, List<Integer> right) {
        List<Integer> result = new ArrayList<>();
        int i = 0;
        int j = 0;
        
        // 左右の配列を比較しながらマージ
        while (i < left.size() && j < right.size()) {
            if (left.get(i) <= right.get(j)) {
                result.add(left.get(i));
                i++;
            } else {
                result.add(right.get(j));
                j++;
            }
        }
        
        // 残った要素を追加
        while (i < left.size()) {
            result.add(left.get(i));
            i++;
        }
        
        while (j < right.size()) {
            result.add(right.get(j));
            j++;
        }
        
        return result;
    }

    public boolean sort() {
        this.data = mergeSort(this.data);
        return true;
    }
}

public class MergeSortDemo {
    public static void main(String[] args) {
        System.out.println("MergeSort TEST -----> start");

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

        System.out.println("\nMergeSort TEST <----- end");
    }
}