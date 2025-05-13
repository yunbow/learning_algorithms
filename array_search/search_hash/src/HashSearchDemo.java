// Java
// 配列の検索: ハッシュ探索 (Hash Search)

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
    
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
        // ハッシュテーブルの作成
        Map<Integer, Integer> hashTable = new HashMap<>();
        
        // 配列の要素をハッシュテーブルに格納
        // キーを要素の値、値をインデックスとする
        for (int i = 0; i < this.data.length; i++) {
            hashTable.put(this.data[i], i);
        }
        
        // ハッシュテーブルを使って検索
        if (hashTable.containsKey(target)) {
            return hashTable.get(target);
        } else {
            return -1;
        }
    }
}

public class HashSearchDemo {

    public static void main(String[] args) {
        System.out.println("HashSearch TEST -----> start");
        
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
        
        System.out.println("\nHashSearch TEST <----- end");
    }
}