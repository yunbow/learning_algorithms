// Java
// データ構造: マップ (Map)

import java.util.HashMap;
import java.util.Map;

class MapData {
    private Map<String, Integer> data;

    public MapData() {
        data = new HashMap<>();
    }

    public Map<String, Integer> get() {
        return new HashMap<>(data);
    }

    public java.util.Set<String> getKeys() {
        return data.keySet();
    }

    public java.util.Collection<Integer> getValues() {
        return data.values();
    }

    public String getKey(Integer value) {
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        System.out.printf("ERROR: %d は範囲外です%n", value);
        return null;
    }

    public Integer getValue(String key) {
        if (data.containsKey(key)) {
            return data.get(key);
        }
        System.out.printf("ERROR: %s は範囲外です%n", key);
        return null;
    }

    public boolean add(String key, Integer value) {
        if (data.containsKey(key)) {
            System.out.printf("ERROR: %s は重複です%n", key);
            return false;
        }
        data.put(key, value);
        return true;
    }

    public boolean remove(String key) {
        if (data.containsKey(key)) {
            data.remove(key);
            return true;
        }
        System.out.printf("ERROR: %s は範囲外です%n", key);
        return false;
    }

    public boolean update(String key, Integer value) {
        if (data.containsKey(key)) {
            data.put(key, value);
            return true;
        }
        System.out.printf("ERROR: %s は範囲外です%n", key);
        return false;
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

    public static void main(String[] args) {
        System.out.println("Map TEST -----> start");

        System.out.println("\nnew");
        MapData mapData = new MapData();
        System.out.printf("  現在のデータ: %s%n", mapData.get());

        System.out.println("\nis_empty");
        boolean output = mapData.isEmpty();
        System.out.printf("  出力値: %b%n", output);

        System.out.println("\nsize");
        int sizeOutput = mapData.size();
        System.out.printf("  出力値: %d%n", sizeOutput);

        System.out.println("\nadd");
        String[] inputs = {"apple", "banana", "apple"};
        int[] values = {100, 150, 200};
        for (int i = 0; i < inputs.length; i++) {
            System.out.printf("  入力値: (%s, %d)%n", inputs[i], values[i]);
            boolean addOutput = mapData.add(inputs[i], values[i]);
            System.out.printf("  出力値: %b%n", addOutput);
            System.out.printf("  現在のデータ: %s%n", mapData.get());
        }

        System.out.println("\nsize");
        sizeOutput = mapData.size();
        System.out.printf("  出力値: %d%n", sizeOutput);

        System.out.println("\nget");
        String[] getInputs = {"apple", "orange"};
        for (String input : getInputs) {
            System.out.printf("  入力値: %s%n", input);
            Integer getOutput = mapData.getValue(input);
            System.out.printf("  出力値: %s%n", getOutput);
        }

        System.out.println("\nupdate");
        String[] updateKeys = {"banana", "orange"};
        int[] updateValues = {180, 250};
        for (int i = 0; i < updateKeys.length; i++) {
            System.out.printf("  入力値: (%s, %d)%n", updateKeys[i], updateValues[i]);
            boolean updateOutput = mapData.update(updateKeys[i], updateValues[i]);
            System.out.printf("  出力値: %b%n", updateOutput);
            System.out.printf("  現在のデータ: %s%n", mapData.get());
        }

        System.out.println("\nget");
        String getValue = "banana";
        Integer getOutput = mapData.getValue(getValue);
        System.out.printf("  出力値: %s%n", getOutput);

        System.out.println("\nget_keys");
        java.util.Set<String> keysOutput = mapData.getKeys();
        System.out.printf("  出力値: %s%n", keysOutput);

        System.out.println("\nvalues");
        java.util.Collection<Integer> valuesOutput = mapData.getValues();
        System.out.printf("  出力値: %s%n", valuesOutput);

        System.out.println("\nget_key");
        int[] keyInputs = {180, 500};
        for (int input : keyInputs) {
            System.out.printf("  入力値: %d%n", input);
            String keyOutput = mapData.getKey(input);
            System.out.printf("  出力値: %s%n", keyOutput);
        }

        System.out.println("\nremove");
        String[] removeInputs = {"apple", "orange"};
        for (String input : removeInputs) {
            System.out.printf("  入力値: %s%n", input);
            boolean removeOutput = mapData.remove(input);
            System.out.printf("  出力値: %b%n", removeOutput);
            System.out.printf("  現在のデータ: %s%n", mapData.get());
        }

        System.out.println("\nsize");
        sizeOutput = mapData.size();
        System.out.printf("  出力値: %d%n", sizeOutput);

        System.out.println("\nget_keys");
        keysOutput = mapData.getKeys();
        System.out.printf("  出力値: %s%n", keysOutput);

        System.out.println("\nclear");
        boolean clearOutput = mapData.clear();
        System.out.printf("  出力値: %b%n", clearOutput);
        System.out.printf("  現在のデータ: %s%n", mapData.get());

        System.out.println("\nsize");
        sizeOutput = mapData.size();
        System.out.printf("  出力値: %d%n", sizeOutput);

        System.out.println("\nis_empty");
        output = mapData.isEmpty();
        System.out.printf("  出力値: %b%n", output);

        System.out.println("\nMap TEST <----- end");
    }
}