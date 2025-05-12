// Java
// データ構造: スタック (Stack)

import java.util.ArrayList;

class StackData {
    private ArrayList<Integer> _data;

    public StackData() {
        this._data = new ArrayList<>();
    }

    public ArrayList<Integer> get() {
        return this._data;
    }

    public int getIndex(Integer item) {
        try {
            int index = this._data.indexOf(item);
            if (index == -1) {
                System.out.println("ERROR: " + item + " は範囲外です");
            }
            return index;
        } catch (Exception e) {
            System.out.println("ERROR: " + item + " は範囲外です");
            return -1;
        }
    }

    public Integer getValue(int index) {
        if (0 <= index && index < this._data.size()) {
            return this._data.get(index);
        } else {
            System.out.println("ERROR: " + index + " は範囲外です");
            return null;
        }
    }

    public boolean push(Integer item) {
        this._data.add(item);
        return true;
    }

    public boolean pop() {
        if (!this.isEmpty()) {
            this._data.remove(this._data.size() - 1);
            return true;
        } else {
            System.out.println("ERROR: 空です");
            return false;
        }
    }

    public Integer peek() {
        if (!this.isEmpty()) {
            return this._data.get(this._data.size() - 1);
        } else {
            return null;
        }
    }

    public boolean isEmpty() {
        return this._data.size() == 0;
    }

    public int size() {
        return this._data.size();
    }

    public boolean clear() {
        this._data.clear();
        return true;
    }
}

public class StackDemo {
    public static void main(String[] args) {
        System.out.println("Stack TEST -----> start");

        System.out.println("\nnew");
        StackData stackData = new StackData();
        System.out.println("  現在のデータ: " + stackData.get());

        System.out.println("\nis_empty");
        boolean output = stackData.isEmpty();
        System.out.println("  出力値: " + output);

        System.out.println("\nsize");
        int sizeOutput = stackData.size();
        System.out.println("  出力値: " + sizeOutput);

        System.out.println("\npush");
        int[] itemsToPush = {10, 20, 30, 40};
        for (int item : itemsToPush) {
            System.out.println("  入力値: " + item);
            boolean pushOutput = stackData.push(item);
            System.out.println("  出力値: " + pushOutput);
            System.out.println("  現在のデータ: " + stackData.get());
        }

        System.out.println("\nsize");
        sizeOutput = stackData.size();
        System.out.println("  出力値: " + sizeOutput);

        System.out.println("\nis_empty");
        output = stackData.isEmpty();
        System.out.println("  出力値: " + output);

        System.out.println("\npeek");
        Integer peekOutput = stackData.peek();
        System.out.println("  出力値: " + peekOutput);

        System.out.println("\nget_index");
        int input = 30;
        System.out.println("  入力値: " + input);
        int indexOutput = stackData.getIndex(input);
        System.out.println("  出力値: " + indexOutput);

        System.out.println("\nget_index");
        input = 50;
        System.out.println("  入力値: " + input);
        indexOutput = stackData.getIndex(input);
        System.out.println("  出力値: " + indexOutput);

        System.out.println("\npop");
        while (!stackData.isEmpty()) {
            boolean popOutput = stackData.pop();
            System.out.println("  出力値: " + popOutput);
            System.out.println("  現在のデータ: " + stackData.get());
        }

        System.out.println("\nis_empty");
        output = stackData.isEmpty();
        System.out.println("  出力値: " + output);

        System.out.println("\nsize");
        sizeOutput = stackData.size();
        System.out.println("  出力値: " + sizeOutput);

        System.out.println("\npop");
        boolean popOutput = stackData.pop();
        System.out.println("  出力値: " + popOutput);

        System.out.println("\npeek");
        peekOutput = stackData.peek();
        System.out.println("  出力値: " + peekOutput);

        System.out.println("\nStack TEST <----- end");
    }
}