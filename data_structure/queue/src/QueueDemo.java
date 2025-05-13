// Java
// データ構造: キュー (Queue)

import java.util.LinkedList;
import java.util.Queue;

class QueueData {
    private Queue<Integer> data;

    public QueueData() {
        data = new LinkedList<>();
    }

    public Queue<Integer> get() {
        return data;
    }

    public int getIndex(Integer item) {
        Integer[] array = data.toArray(new Integer[0]);
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(item)) {
                return i;
            }
        }
        System.out.println("ERROR: " + item + " は範囲外です");
        return -1;
    }

    public Integer getValue(int index) {
        Integer[] array = data.toArray(new Integer[0]);
        if (0 <= index && index < array.length) {
            return array[index];
        } else {
            System.out.println("Error: インデックス " + index + " は範囲外です");
            return null;
        }
    }

    public boolean enqueue(Integer item) {
        return data.offer(item);
    }

    public boolean dequeue() {
        if (!isEmpty()) {
            data.poll();
            return true;
        } else {
            System.out.println("ERROR: キューが空です");
            return false;
        }
    }

    public Integer peek() {
        if (!isEmpty()) {
            return data.peek();
        } else {
            System.out.println("ERROR: キューが空です");
            return null;
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

public class QueueDemo {
    public static void main(String[] args) {
        System.out.println("Queue TEST -----> start");

        System.out.println("\nnew");
        QueueData queueData = new QueueData();
        System.out.println("  現在のデータ: " + queueData.get());

        System.out.println("\nis_empty");
        boolean output = queueData.isEmpty();
        System.out.println("  出力値: " + output);

        System.out.println("\nenqueue");
        Integer[] input = {10, 20, 30};
        for (Integer item : input) {
            System.out.println("  入力値: " + item);
            output = queueData.enqueue(item);
            System.out.println("  出力値: " + output);
            System.out.println("  現在のデータ: " + queueData.get());
        }

        System.out.println("\nsize");
        int outputSize = queueData.size();
        System.out.println("  出力値: " + outputSize);

        System.out.println("\npeek");
        Integer outputPeek = queueData.peek();
        System.out.println("  出力値: " + outputPeek);

        System.out.println("\nget_index");
        int inputIndex = 20;
        System.out.println("  入力値: " + inputIndex);
        int outputIndex = queueData.getIndex(inputIndex);
        System.out.println("  出力値: " + outputIndex);

        System.out.println("\nget_index");
        inputIndex = 50;
        System.out.println("  入力値: " + inputIndex);
        outputIndex = queueData.getIndex(inputIndex);
        System.out.println("  出力値: " + outputIndex);

        System.out.println("\ndequeue");
        output = queueData.dequeue();
        System.out.println("  出力値: " + output);
        System.out.println("  現在のデータ: " + queueData.get());

        System.out.println("\ndequeue");
        output = queueData.dequeue();
        System.out.println("  出力値: " + output);
        System.out.println("  現在のデータ: " + queueData.get());

        System.out.println("\nsize");
        outputSize = queueData.size();
        System.out.println("  出力値: " + outputSize);

        System.out.println("\ndequeue");
        output = queueData.dequeue();
        System.out.println("  出力値: " + output);
        System.out.println("  現在のデータ: " + queueData.get());

        System.out.println("\ndequeue");
        output = queueData.dequeue();
        System.out.println("  出力値: " + output);
        System.out.println("  現在のデータ: " + queueData.get());

        System.out.println("\nis_empty");
        output = queueData.isEmpty();
        System.out.println("  出力値: " + output);

        System.out.println("\nclear");
        output = queueData.clear();
        System.out.println("  出力値: " + output);
        System.out.println("  現在のデータ: " + queueData.get());

        System.out.println("\nsize");
        outputSize = queueData.size();
        System.out.println("  出力値: " + outputSize);

        System.out.println("\nQueue TEST <----- end");
    }
}