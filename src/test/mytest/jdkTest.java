package mytest;

public class jdkTest {

     public static void  toEatele(Eatable le){
         int eat = le.eat(1, 2);
         System.out.println(eat);
     }
    public static void main(String args[]){
         toEatele((a,b) -> a+b);

    }
}
