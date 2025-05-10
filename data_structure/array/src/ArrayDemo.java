// Java
// データ構造: 配列 (Array)

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ArrayData {
    private List<Integer> _data;

    public ArrayData() {
        this._data = new ArrayList<>();
    }

    public List<Integer> get() {
        // 要素を取得
        return this._data;
    }

    public int getIndex(Integer item) {
        // 配列内で指定された値を検索し、最初に見つかったインデックスを返す
        int index = this._data.indexOf(item);
        if (index == -1) {
            System.out.println("ERROR: " + item + " は範囲外です");
        }
        return index;
    }

    public Integer getValue(int index) {
        // 指定されたインデックスの要素を取得する
        if (0 <= index && index < this._data.size()) {
            return this._data.get(index);
        } else {
            System.out.println("ERROR: " + index + " は範囲外です");
            return null;
        }
    }

    public boolean add(Integer item) {
        // 配列の末尾に要素を追加する
        return this._data.add(item);
    }

    public boolean remove(int index) {
        // 指定されたインデックスの要素を削除する
        if (0 <= index && index < this._data.size()) {
            this._data.remove(index);
            return true;
        } else {
            System.out.println("ERROR: " + index + " は範囲外です");
            return false;
        }
    }

    public boolean update(int index, Integer newValue) {
        // 指定されたインデックスの要素を新しい値に更新する
        if (0 <= index && index < this._data.size()) {
            this._data.set(index, newValue);
            return true;
        } else {
            System.out.println("ERROR: " + index + " は範囲外です");
            return false;
        }
    }

    public List<Integer> reverse() {
        // 配列の要素を逆順にする
        Collections.reverse(this._data);
        return this._data;
    }

    public List<Integer> sort(boolean descending) {
        // 配列の要素をソートする
        if (descending) {
            Collections.sort(this._data, Collections.reverseOrder());
        } else {
            Collections.sort(this._data);
        }
        return this._data;
    }

    public boolean isEmpty() {
        // 配列が空かどうか
        return this._data.isEmpty();
    }

    public int size() {
        // 配列のサイズ（要素数）を返す
        return this._data.size();
    }

    public boolean clear() {
        // 配列の全要素を削除する
        this._data.clear();
        return true;
    }
}

public class ArrayDemo {
    public static void main(String[] args) {
        System.out.println("Array TEST -----> start");

        System.out.println("\nnew");
        ArrayData arrayData = new ArrayData();
        System.out.println("  現在のデータ: " + arrayData.get());

        System.out.println("\nadd");
        int[] input = {10, 20, 30, 10, 40};
        for (int item : input) {
            System.out.println("  入力値: " + item);
            boolean output = arrayData.add(item);
            System.out.println("  出力値: " + output);
            System.out.println("  現在のデータ: " + arrayData.get());
        }

        System.out.println("\nsize");
        int outputSize = arrayData.size();
        System.out.println("  出力値: " + outputSize);

        System.out.println("\nis_empty");
        boolean outputEmpty = arrayData.isEmpty();
        System.out.println("  出力値: " + outputEmpty);

        System.out.println("\nget_value");
        int inputIndex = 2;
        System.out.println("  入力値: " + inputIndex);
        Integer outputValue = arrayData.getValue(inputIndex);
        System.out.println("  出力値: " + outputValue);

        System.out.println("\nget_value");
        inputIndex = 10;
        System.out.println("  入力値: " + inputIndex);
        outputValue = arrayData.getValue(inputIndex);
        System.out.println("  出力値: " + outputValue);

        System.out.println("\nupdate");
        int updateIndex = 1;
        int updateValue = 25;
        System.out.println("  入力値: (" + updateIndex + ", " + updateValue + ")");
        boolean outputUpdate = arrayData.update(updateIndex, updateValue);
        System.out.println("  出力値: " + outputUpdate);
        System.out.println("  現在のデータ: " + arrayData.get());

        System.out.println("\nupdate");
        updateIndex = 15;
        updateValue = 25;
        System.out.println("  入力値: (" + updateIndex + ", " + updateValue + ")");
        outputUpdate = arrayData.update(updateIndex, updateValue);
        System.out.println("  出力値: " + outputUpdate);
        System.out.println("  現在のデータ: " + arrayData.get());

        System.out.println("\nget_index");
        Integer findItem = 10;
        System.out.println("  入力値: " + findItem);
        int outputIndex = arrayData.getIndex(findItem);
        System.out.println("  出力値: " + outputIndex);

        System.out.println("\nget_index");
        findItem = 99;
        System.out.println("  入力値: " + findItem);
        outputIndex = arrayData.getIndex(findItem);
        System.out.println("  出力値: " + outputIndex);

        System.out.println("\nremove");
        int removeIndex = 3;
        System.out.println("  入力値: " + removeIndex);
        boolean outputRemove = arrayData.remove(removeIndex);
        System.out.println("  出力値: " + outputRemove);
        System.out.println("  現在のデータ: " + arrayData.get());

        System.out.println("\nremove");
        removeIndex = 8;
        System.out.println("  入力値: " + removeIndex);
        outputRemove = arrayData.remove(removeIndex);
        System.out.println("  出力値: " + outputRemove);
        System.out.println("  現在のデータ: " + arrayData.get());

        System.out.println("\nreverse");
        List<Integer> outputReverse = arrayData.reverse();
        System.out.println("  出力値: " + outputReverse);

        System.out.println("\nsort");
        System.out.println("  入力値: descending=false");
        List<Integer> outputSort = arrayData.sort(false);
        System.out.println("  出力値: " + outputSort);

        System.out.println("\nsort");
        System.out.println("  入力値: descending=true");
        outputSort = arrayData.sort(true);
        System.out.println("  出力値: " + outputSort);

        System.out.println("\nclear");
        boolean outputClear = arrayData.clear();
        System.out.println("  出力値: " + outputClear);
        System.out.println("  現在のデータ: " + arrayData.get());

        System.out.println("\nis_empty");
        outputEmpty = arrayData.isEmpty();
        System.out.println("  出力値: " + outputEmpty);

        System.out.println("\nArray TEST <----- end");
    }
}