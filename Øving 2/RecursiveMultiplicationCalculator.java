import java.util.Date;

public class RecursiveMultiplicationCalculator {
    public double multiplicationMethod1(int n, double x) {
        if (n == 1) {
            return x;
        } else {
            return x + multiplicationMethod1(n - 1, x);
        }
    }

    public double multiplicationMethod2(int n, double x) {
        if (n == 1) {                                           // 1 comparison
            return x;
        } else if (n % 2 == 0) { // n is an even number         // 1 operation
            return multiplicationMethod2(n/2,(x+x));         // 1 addition + T(n/2) operations, T is a call to the method
        } else { // n is an odd number
            return x + multiplicationMethod2(((n-1)/2),(x+x));  // 1 addition + 1 addition + T(n/2) operations, T is a call to the method
        }
    }

    /**
     * Finds time-complexity for multiplicationMethod1
     */
    public double findTimeComplexity1(int n, double x) {
        Date start = new Date();
        int runder = 0;
        double tid;
        Date slutt;
        do {
            multiplicationMethod1(n, x);
            slutt = new Date();
            ++runder;
        } while (slutt.getTime()-start.getTime() < 1000);
        tid = (double)
                (slutt.getTime()-start.getTime()) / runder;
        return tid;
    }

    /**
     * Finds time-complexity for multiplicationMethod2
     */
    public double findTimeComplexity2(int n, double x) {
        Date start = new Date();
        int runder = 0;
        double tid;
        Date slutt;
        do {
            multiplicationMethod2(n, x);
            slutt = new Date();
            ++runder;
        } while (slutt.getTime()-start.getTime() < 1000);
        tid = (double)
                (slutt.getTime()-start.getTime()) / runder;
        return tid;
    }

    public static void main(String[] args) {
        RecursiveMultiplicationCalculator main = new RecursiveMultiplicationCalculator();
        double x = 10.1;
        System.out.println("Test 1: 13 * 2.5");
        System.out.println("Method 1: " + main.multiplicationMethod1(13, 2.5));
        System.out.println("Method 2: " + main.multiplicationMethod2(13, 2.5));
        System.out.println("\nTest 2: 14 * 10.1");
        System.out.println("Method 1: " + main.multiplicationMethod1(14, 10.1));
        System.out.println("Method 2: " + main.multiplicationMethod2(14, 10.1) + "\n");

        // Time-complexity for Method 1
        System.out.println("Milliseconds pr. round:" + main.findTimeComplexity1(50, x) + ", when n = 50 for Method 1");
        System.out.println("Milliseconds pr. round:" + main.findTimeComplexity1(500, x) + ", when n = 500 for Method 1");
        System.out.println("Milliseconds pr. round:" + main.findTimeComplexity1(5000, x) + ", when n = 5000 for Method 1\n");

        // Time-complexity for Method 2
        System.out.println("Milliseconds pr. round:" + main.findTimeComplexity2(50, x) + ", when n = 50 for Method 2");
        System.out.println("Milliseconds pr. round:" + main.findTimeComplexity2(500, x) + ", when n = 500 for Method 2");
        System.out.println("Milliseconds pr. round:" + main.findTimeComplexity2(5000, x) + ", when n = 5000 for Method 2");
        System.out.println("Milliseconds pr. round:" + main.findTimeComplexity2(50000, x) + ", when n = 50000 for Method 2");
        System.out.println("Milliseconds pr. round:" + main.findTimeComplexity2(500000, x) + ", when n = 500000 for Method 2");
        System.out.println("Milliseconds pr. round:" + main.findTimeComplexity2(5000000, x) + ", when n = 500000 for Method 2");
    }
}