// Java
// 最大公約数: ユークリッドの互除法 (Euclidean)

public class EuclideanDemo {
    
    public static int gcd(int a, int b) {
        // bが0になるまで繰り返し計算する
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
    
    public static void main(String[] args) {
        System.out.println("Euclidean TEST -----> start");
        int[] input = {48, 18};
        System.out.println("\n  入力値: [" + input[0] + ", " + input[1] + "]");
        int output = gcd(input[0], input[1]);
        System.out.println("\n  出力値: " + output);
        System.out.println("\nEuclidean TEST <----- end");
    }
}
