public class FizzBuzz {
    public static void main(String[] args) {
        for(int i = 0; i < 100; i++) {
//<<<<<<< HEAD
            if( i % 3 == 0)
            System.out.println(i);
//=======
            else if( i % 5 == 0)
                System.out.println("Buzz");
//>>>>>>> bfb5368fdbe09f881a45fb89ade87e9dd6b6d7d6
        }
    }
}