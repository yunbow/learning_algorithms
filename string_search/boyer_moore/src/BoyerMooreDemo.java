import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoyerMooreDemo {
    
    public static List<Integer> search(String text, String pattern) {
        List<Integer> occurrences = new ArrayList<>();
        
        if (pattern == null || text == null || pattern.isEmpty() || pattern.length() > text.length()) {
            return occurrences;
        }
        
        // 悪文字ルールのテーブル作成
        Map<Character, Integer> badChar = badCharacterTable(pattern);
        
        // 良接尾辞ルールのテーブル作成
        int[] goodSuffix = goodSuffixTable(pattern);
        
        // 検索
        int i = pattern.length() - 1;  // テキスト内の位置
        while (i < text.length()) {
            int j = pattern.length() - 1;  // パターン内の位置
            while (j >= 0 && text.charAt(i - pattern.length() + 1 + j) == pattern.charAt(j)) {
                j--;
            }
            
            if (j < 0) {  // マッチした場合
                occurrences.add(i - pattern.length() + 1);
                i++;
            } else {  // マッチしなかった場合
                // 悪文字ルールと良接尾辞ルールのシフト量の大きい方を採用
                int bcShift = Math.max(1, j - badChar.getOrDefault(text.charAt(i), -1));
                int gsShift = goodSuffix[j];
                i += Math.max(bcShift, gsShift);
            }
        }
        
        return occurrences;
    }
    
    private static Map<Character, Integer> badCharacterTable(String pattern) {
        // 悪文字ルールのテーブルを作成
        Map<Character, Integer> badChar = new HashMap<>();
        for (int i = 0; i < pattern.length(); i++) {
            badChar.put(pattern.charAt(i), i);
        }
        return badChar;
    }
    
    private static int[] goodSuffixTable(String pattern) {
        // 良接尾辞ルールのテーブルを作成
        int n = pattern.length();
        // ボーダー配列の計算
        int[] border = new int[n + 1];
        border[n] = n;
        
        // パターンの逆順に対するZ配列を計算
        String patternRev = new StringBuilder(pattern).reverse().toString();
        int[] z = zArray(patternRev);
        
        for (int i = 0; i < n; i++) {
            int j = n - z[i];
            border[j] = i;
        }
        
        // 良接尾辞ルールのシフト量計算
        int[] shift = new int[n];
        for (int i = 0; i < n; i++) {
            shift[i] = n;
        }
        
        for (int i = 0; i < n; i++) {
            int j = n - border[i];
            shift[n - j - 1] = j;
        }
        
        // ボーダーが存在しない場合の処理
        for (int i = 0; i < n - 1; i++) {
            int j = n - 1 - i;
            if (border[j] == j) {
                for (int k = 0; k < n - j; k++) {
                    if (shift[k] == n) {
                        shift[k] = n - j;
                    }
                }
            }
        }
        
        return shift;
    }
    
    private static int[] zArray(String pattern) {
        int n = pattern.length();
        int[] z = new int[n];
        int l = 0, r = 0;
        for (int i = 1; i < n; i++) {
            if (i <= r) {
                z[i] = Math.min(r - i + 1, z[i - l]);
            }
            while (i + z[i] < n && pattern.charAt(z[i]) == pattern.charAt(i + z[i])) {
                z[i]++;
            }
            if (i + z[i] - 1 > r) {
                l = i;
                r = i + z[i] - 1;
            }
        }
        return z;
    }
    
    public static void main(String[] args) {
        System.out.println("BoyerMooreSearch TEST -----> start");
        
        System.out.println("\nsearch");
        String text = "ABABCABCABABABD";
        String pattern = "ABABD";
        System.out.println("  入力値: (" + text + ", " + pattern + ")");
        List<Integer> output = search(text, pattern);
        System.out.println("  出力値: " + output);
        
        System.out.println("\nsearch");
        text = "AAAAAA";
        pattern = "AA";
        System.out.println("  入力値: (" + text + ", " + pattern + ")");
        output = search(text, pattern);
        System.out.println("  出力値: " + output);
        
        System.out.println("\nsearch");
        text = "ABCDEFG";
        pattern = "XYZ";
        System.out.println("  入力値: (" + text + ", " + pattern + ")");
        output = search(text, pattern);
        System.out.println("  出力値: " + output);
        
        System.out.println("\nsearch");
        text = "ABCABC";
        pattern = "ABC";
        System.out.println("  入力値: (" + text + ", " + pattern + ")");
        output = search(text, pattern);
        System.out.println("  出力値: " + output);
        
        System.out.println("\nsearch");
        text = "ABC";
        pattern = "";
        System.out.println("  入力値: (" + text + ", " + pattern + ")");
        output = search(text, pattern);
        System.out.println("  出力値: " + output);
        
        System.out.println("\nBoyerMooreSearch TEST <----- end");
    }
}