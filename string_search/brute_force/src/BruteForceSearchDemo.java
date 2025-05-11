import java.util.ArrayList;
import java.util.List;

public class BruteForceSearchDemo {
    
    public static List<Integer> search(String text, String pattern) {
        int n = text.length();
        int m = pattern.length();
        List<Integer> positions = new ArrayList<>();
        
        // パターンが空または検索対象より長い場合
        if (m == 0 || m > n) {
            return positions;
        }
        
        // テキスト内の各位置でパターンとの一致を確認
        for (int i = 0; i <= n - m; i++) {
            int j = 0;
            // パターンの各文字を確認
            while (j < m && text.charAt(i + j) == pattern.charAt(j)) {
                j++;
            }
            // パターンが完全に一致した場合
            if (j == m) {
                positions.add(i);
            }
        }
        
        return positions;
    }
    
    public static void main(String[] args) {
        System.out.println("BruteForceSearch TEST -----> start");
        
        System.out.println("\nsearch");
        Object[] input = {"ABABCABCABABABD", "ABABD"};
        System.out.println("  入力値: [" + input[0] + ", " + input[1] + "]");
        List<Integer> output = search((String)input[0], (String)input[1]);
        System.out.println("  出力値: " + output);
        
        System.out.println("\nsearch");
        input = new Object[]{"AAAAAA", "AA"};
        System.out.println("  入力値: [" + input[0] + ", " + input[1] + "]");
        output = search((String)input[0], (String)input[1]);
        System.out.println("  出力値: " + output);
        
        System.out.println("\nsearch");
        input = new Object[]{"ABCDEFG", "XYZ"};
        System.out.println("  入力値: [" + input[0] + ", " + input[1] + "]");
        output = search((String)input[0], (String)input[1]);
        System.out.println("  出力値: " + output);
        
        System.out.println("\nsearch");
        input = new Object[]{"ABCABC", "ABC"};
        System.out.println("  入力値: [" + input[0] + ", " + input[1] + "]");
        output = search((String)input[0], (String)input[1]);
        System.out.println("  出力値: " + output);
        
        System.out.println("\nsearch");
        input = new Object[]{"ABC", ""};
        System.out.println("  入力値: [" + input[0] + ", " + input[1] + "]");
        output = search((String)input[0], (String)input[1]);
        System.out.println("  出力値: " + output);
        
        System.out.println("\nBruteForceSearch TEST <----- end");
    }
}