package mytest;

public class MysuperTest {
    private static MySuperler newSuperler() {
        return (() -> null);
    }
    public  static void  MySuperlerTwo(MySuperler superler){
        superler.get();
    }

    public static void main(String[] args) {
       // String[] array = { "abc", "ab", "abcd" };
   // System.out.println(Arrays.toString(array)); Arrays.sort(array, newComparator()); System.out.println(Arrays.toString(array));
    }
}
