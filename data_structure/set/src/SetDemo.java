// Java
// データ構造: セット (Set)

import java.util.HashSet;
import java.util.Set;

class SetData {
    private Set<Integer> data;

    public SetData() {
        data = new HashSet<>();
    }

    public Set<Integer> get() {
        return data;
    }

    public int getIndex(Integer item) {
        int index = 0;
        for (Integer element : data) {
            if (element.equals(item)) {
                return index;
            }
            index++;
        }
        System.out.println("ERROR: " + item + " は範囲外です");
        return -1;
    }

    public Integer getValue(int index) {
        if (index >= 0 && index < data.size()) {
            return (Integer) data.toArray()[index];
        } else {
            System.out.println("ERROR: " + index + " は範囲外です");
            return null;
        }
    }

    public boolean add(Integer item) {
        if (!data.contains(item)) {
            data.add(item);
            return true;
        } else {
            System.out.println("ERROR: " + item + " は重複です");
            return false;
        }
    }

    public boolean remove(Integer item) {
        if (data.contains(item)) {
            data.remove(item);
            return true;
        } else {
            System.out.println("ERROR: " + item + " は範囲外です");
            return false;
        }
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public int size() {
        return data.size();
    }

    public boolean clear() {
        data.clear();
        return true;
    }
}

public class SetDemo {
    public static void main(String[] args) {
        System.out.println("Set TEST -----> start");

        System.out.println("\nnew");
        SetData setData = new SetData();
        System.out.println("  現在のデータ: " + setData.get());

        System.out.println("\nadd");
        Integer[] input = {10, 20, 30, 20, 40};
        for (Integer item : input) {
            System.out.println("  入力値: " + item);
            boolean output = setData.add(item);
            System.out.println("  出力値: " + output);
            System.out.println("  現在のデータ: " + setData.get());
        }

        System.out.println("\nsize");
        int output = setData.size();
        System.out.println("  出力値: " + output);

        System.out.println("\nis_empty");
        boolean emptyOutput = setData.isEmpty();
        System.out.println("  出力値: " + emptyOutput);

        System.out.println("\nget_value");
        int[] indexInput = {0, 2, 5};
        for (int index : indexInput) {
            System.out.println("  入力値: " + index);
            Integer valueOutput = setData.getValue(index);
            System.out.println("  出力値: " + valueOutput);
        }

        System.out.println("\nget_index");
        Integer[] itemInput = {30, 99};
        for (Integer item : itemInput) {
            System.out.println("  入力値: " + item);
            int indexOutput = setData.getIndex(item);
            System.out.println("  出力値: " + indexOutput);
        }

        System.out.println("\nremove");
        Integer[] removeInput = {20, 50, 10};
        for (Integer item : removeInput) {
            System.out.println("  入力値: " + item);
            boolean removeOutput = setData.remove(item);
            System.out.println("  出力値: " + removeOutput);
            System.out.println("  現在のデータ: " + setData.get());
        }

        System.out.println("\nsize");
        output = setData.size();
        System.out.println("  出力値: " + output);

        System.out.println("\nclear");
        boolean clearOutput = setData.clear();
        System.out.println("  出力値: " + clearOutput);
        System.out.println("  現在のデータ: " + setData.get());

        System.out.println("\nis_empty");
        emptyOutput = setData.isEmpty();
        System.out.println("  出力値: " + emptyOutput);

        System.out.println("\nSet TEST <----- end");
    }
}