import java.util.*;

public class SortingAlgorithm {
    private static void swap(int[] array, int index1, int index2) {
        int temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    private static int median3sort(int[] array, int leftIndex, int rightIndex) {
        int medianIndex = (leftIndex + rightIndex) / 2;
        if (array[leftIndex] > array[medianIndex]) swap(array, leftIndex, medianIndex);
        if (array[medianIndex] > array[rightIndex]) {
            swap(array, medianIndex, rightIndex);
            if (array[leftIndex] > array[medianIndex]) swap(array, leftIndex, medianIndex);
        }
        return medianIndex;
    }

    public static void quicksort(int[] array, int leftIndex, int rightIndex) {
        if (rightIndex - leftIndex > 2) {
            int pivot = splitt(array, leftIndex, rightIndex);
            quicksort(array, leftIndex, pivot - 1);
            quicksort(array, pivot + 1, rightIndex);
        } else median3sort(array, leftIndex, rightIndex);
    }
    public static void quicksortImproved(int[] array, int leftIndex, int rightIndex) {
        if(leftIndex == 0 && rightIndex == array.length - 1) {
            // Find the index of highest and lowest values in array
            int minIndex = 0;
            int maxIndex = 0;
            for (int i = 1; i < array.length - 1; i++) {
                if (array[minIndex] > array[i]) {
                    minIndex = i;
                }
                if (array[maxIndex] < array[i]) {
                    maxIndex = i;
                }
            }
            // Check if highest value is already sorted
            if (maxIndex != array.length - 1) {
                swap(array, maxIndex, array.length - 1);
            }
            // Checks if lowest value is already sorted
            if (minIndex != 0) {
                swap(array, minIndex, 0);
            }

            leftIndex = 1;
            rightIndex = array.length - 2;
        }

        if(array[leftIndex - 1]  == array[rightIndex + 1]) {
            return;
        }

        if (rightIndex - leftIndex > 2) {
            int pivot = splitt(array, leftIndex, rightIndex);
            quicksortImproved(array, leftIndex, pivot - 1);
            quicksortImproved(array, pivot + 1, rightIndex);
        } else median3sort(array, leftIndex, rightIndex);
    }

    private static int splitt(int[] array, int leftIndex, int rightIndex) {
        int leftPointer, rightPointer;
        int m = median3sort(array, leftIndex, rightIndex);
        int dv = array[m];
        swap(array, m, rightIndex - 1);
        for (leftPointer = leftIndex, rightPointer = rightIndex - 1;;) {
            while (array[++leftPointer] < dv) ;
            while (array[--rightPointer] > dv) ;
            if (leftPointer >= rightPointer) break;
            swap(array, leftPointer, rightPointer);
        }
        swap(array, leftPointer, rightIndex-1);
        return leftPointer;
    }

    /**
     * Find time complexity for the unimproved quicksort method
     */
    public static double findTimeComplexity1(int[] array, int leftIndex, int rightIndex) {
        int[] arrayCopy = Arrays.copyOf(array, array.length);
        Date start = new Date();
        int runder = 0;
        double tid;
        Date slutt;
        do {
            quicksort(arrayCopy, leftIndex, rightIndex);
            arrayCopy = Arrays.copyOf(array, array.length);
            slutt = new Date();
            ++runder;
        } while (slutt.getTime()-start.getTime() < 1000);
        tid = (double)
                (slutt.getTime()-start.getTime()) / runder;
        return tid;
    }

    /**
     * Find time complexity for the improved quicksort method
     */
    public static double findTimeComplexity2(int[] array, int leftIndex, int rightIndex) {
        int[] arrayCopy = Arrays.copyOf(array, array.length);
        Date start = new Date();
        int runder = 0;
        double tid;
        Date slutt;
        do {
            quicksortImproved(arrayCopy, leftIndex, rightIndex);
            arrayCopy = Arrays.copyOf(array, array.length);
            slutt = new Date();
            ++runder;
        } while (slutt.getTime()-start.getTime() < 1000);
        tid = (double)
                (slutt.getTime()-start.getTime()) / runder;
        return tid;
    }

    /**
     * Checks if the size of the two Array´s
     * is the same
     */
    public static boolean isSumOfArraysSimilar(int[] array1, int[] array2) {
        int sum1 = 0;
        for(int i = 0; i < array1.length; i++) {
            sum1 += array1[i];
        }
        int sum2 = 0;
        for(int i = 0; i < array2.length; i++) {
            sum2 += array2[i];
        }
        return (sum1 - sum2 == 0);
    }

    /**
     * Checks if the array is sorted
     */
    public static boolean isArraySorted(int[] array) {
        for(int i = array.length - 1; i > 1; i--) {
            if(array[i] < array[i - 1]) {
                System.out.println("Error in algorithm. " + array[i] + " is lower than " + array[i + 1]);
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        // Testing that the quicksort algorithm works correctly
        Random rand = new Random();
        int length = 5000000;
        int[] array = new int[length];
        for(int i = 0; i < length; i++) {
            array[i] = rand.nextInt(10000);
        }
        int[] arrayUnSorted = Arrays.copyOf(array, length);
        quicksort(array, 0, array.length - 1);
        int[] arraySorted = Arrays.copyOf(array, length);


        if(isSumOfArraysSimilar(arrayUnSorted, arraySorted)) {
            System.out.println("TEST 1 SUCCESS: SUM OF ELEMENTS IS SIMILAR BEFORE AND AFTER BEING SORTED");
        } else {
            System.out.println("TEST 1 FAIL: SUM OF ELEMENTS IS NOT SIMILAR BEFORE AND AFTER BEING SORTED");
        }


        if(isArraySorted(arraySorted)) {
            System.out.println("TEST 2 SUCCESS: THE ARRAY IS SORTED");
        } else {
            System.out.println("TEST 2 FAIL: THE ARRAY IS NOT SORTED");
        }

        if(isSumOfArraysSimilar(arrayUnSorted, arraySorted) && isArraySorted(arraySorted)) {
            System.out.println("THE UN-IMPROVED QUICKSORT ALGORITHM WORKS CORRECTLY\n");
        } else {
            System.out.println("THE UN-IMPROVED QUICKSORT ALGORITHM DOES NOT WORKS CORRECTLY\n");
        }


        // Testing that the quicksort algorithm works correctly
        rand = new Random();
        array = new int[length];
        for(int i = 0; i < length; i++) {
            array[i] = rand.nextInt(10000);
        }
        arrayUnSorted = Arrays.copyOf(array, length);
        quicksortImproved(array, 0, array.length - 1);
        arraySorted = Arrays.copyOf(array, length);

        if(isSumOfArraysSimilar(arrayUnSorted, arraySorted)) {
            System.out.println("TEST 1 SUCCESS: SUM OF ELEMENTS IS SIMILAR BEFORE AND AFTER BEING SORTED");
        } else {
            System.out.println("TEST 1 FAIL: SUM OF ELEMENTS IS NOT SIMILAR BEFORE AND AFTER BEING SORTED");
        }

        if(isArraySorted(arraySorted)) {
            System.out.println("TEST 2 SUCCESS: THE ARRAY IS SORTED");
        } else {
            System.out.println("TEST 2 FAIL: THE ARRAY IS NOT SORTED");
        }
        if(isSumOfArraysSimilar(arrayUnSorted, arraySorted) && isArraySorted(arraySorted)) {
            System.out.println("THE IMPROVED QUICKSORT ALGORITHM WORKS CORRECTLY\n");
        } else {
            System.out.println("THE IMPROVED QUICKSORT ALGORITHM DOES NOT WORKS CORRECTLY\n");
        }

        // Testing the time the un-improved quicksort algorithm
        // takes on an array with many duplicates
        rand = new Random();
        array = new int[length];
        for(int i = 0; i < length; i++) {
            if(i % 2 == 0) {
                array[i] = 42;
            } else {
                array[i] = rand.nextInt(10000);
            }
        }
        arrayUnSorted = Arrays.copyOf(array, length);
        quicksort(array, 0, array.length - 1);
        arraySorted = Arrays.copyOf(array, length);

        if(!isSumOfArraysSimilar(arrayUnSorted, arraySorted)){
            System.out.println("SUM OF ARRAY IS NOT SIMILAR BEFORE AND AFTER BEING SORTED");
        }
        if(!isArraySorted(arraySorted)) {
            System.out.println("THE ARRAY IS NOT SORTED");
        }

        System.out.println("QUICKSORT, DUPLICATES: Milliseconds pr. round: " + findTimeComplexity1(arrayUnSorted, 0, arrayUnSorted.length - 1) + " when there are " + length + " elements");

        // Testing the time the improved quicksort algorithm
        // takes on an array with many duplicates
        arrayUnSorted = Arrays.copyOf(array, length);
        quicksortImproved(array, 0, array.length - 1);
        arraySorted = Arrays.copyOf(array, length);

        if(!isSumOfArraysSimilar(arrayUnSorted, arraySorted)){
            System.out.println("SUM OF ARRAY IS NOT SIMILAR BEFORE AND AFTER BEING SORTED");
        }
        if(!isArraySorted(arraySorted)) {
            System.out.println("THE ARRAY IS NOT SORTED");
        }
        System.out.println("QUICKSORT IMPROVED, DUPLICATES: Milliseconds pr. round: " + findTimeComplexity2(arrayUnSorted, 0, arrayUnSorted.length - 1) + " when there are " + length + " elements\n");

        // Testing the time the improved quicksort algorithm
        // takes on an array with NO duplicates
        Integer[] arrayNoDuplicates = new Integer[length];
        for(int i = 0; i < length; i++) {
            arrayNoDuplicates[i] = i;
        }
        List<Integer> intList = Arrays.asList(arrayNoDuplicates);
        Collections.shuffle(intList);
        for(int i = 0; i < length; i++) {
            array[i] = intList.get(i);
        }
        arrayUnSorted = Arrays.copyOf(array, length);
        quicksortImproved(array, 0, array.length - 1);
        arraySorted = Arrays.copyOf(array, length);

        if(!isSumOfArraysSimilar(arrayUnSorted, arraySorted)){
            System.out.println("SUM OF ARRAY IS NOT SIMILAR BEFORE AND AFTER BEING SORTED");
        }
        if(!isArraySorted(arraySorted)) {
            System.out.println("THE ARRAY IS NOT SORTED");
        }

        System.out.println("QUICKSORT, NO-DUPLICATES: Milliseconds pr. round: " + findTimeComplexity1(arrayUnSorted, 0, arrayUnSorted.length - 1) + " when there are " + length + " elements");

        quicksort(array, 0, array.length - 1);
        arraySorted = Arrays.copyOf(array, length);
        if(!isSumOfArraysSimilar(arrayUnSorted, arraySorted)){
            System.out.println("SUM OF ARRAY IS NOT SIMILAR BEFORE AND AFTER BEING SORTED");
        }
        if(!isArraySorted(arraySorted)) {
            System.out.println("THE ARRAY IS NOT SORTED");
        }

        System.out.println("QUICKSORT IMPROVED, NO-DUPLICATES: Milliseconds pr. round: " + findTimeComplexity2(arrayUnSorted, 0, arrayUnSorted.length - 1) + " when there are " + length + " elements\n");

        System.out.println("QUICKSORT, SORTED: Milliseconds pr. round: " + findTimeComplexity1(arrayUnSorted, 0, arraySorted.length - 1) + " when there are " + length + " elements");
        System.out.println("QUICKSORT IMPROVED, SORTED: Milliseconds pr. round: " + findTimeComplexity2(arrayUnSorted, 0, arraySorted.length - 1) + " when there are " + length + " elements");

    }
}
