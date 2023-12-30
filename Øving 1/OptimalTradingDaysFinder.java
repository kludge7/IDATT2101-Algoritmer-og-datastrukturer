import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
public class OptimalTradingDaysFinder {
    public String optimalTradingDaysAlgorithm(Map<Integer, Integer> stockChange) {
        int currentProfit;                                                                                          // 1 tilordning
        int bestProfit = 0;                                                                                         // 1 tilordning
        int bestPurchaseDay = 1;                                                                                    // 1 tilordning
        int bestSellDay = 1;                                                                                        // 1 tilordning
        for (int i = 1; i < stockChange.size(); i++) {                                                              // 1 tilordning, n sammenligninger, n inkrementer
            currentProfit = 0;                                                                                      // 1 tilordning
            for (int j = i+1; j < stockChange.size(); j++) {                                                        // n tilordning, n addisjon, n^2 sammenligninger, n^2 inkrementer
                currentProfit += stockChange.get(j);                                                                // n^2 addisjoner, n^2 tabelloppslag
                if(currentProfit > bestProfit) {                                                                    // n^2 sammenligninger
                    bestProfit = currentProfit;                                                                     // n^2 tilordning
                    bestPurchaseDay = i;                                                                            // n^2 tilordning
                    bestSellDay = j;                                                                                // n^2 tilordning
                }
            }
        }
        return "Buy day: " + bestPurchaseDay + ". Sale day: " + bestSellDay + ". Best profit: " + bestProfit + "."; // 1 return
    }


    /**
     * Metode som lager Map med tilfeldig innhold.
     * Brukes for å teste tidsmåling til optimalTradingDaysAlgorithm
     */
    public Map<Integer,Integer> generateIntegerMap(int amount) {
        Map<Integer, Integer> map = new HashMap<>();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for(int i = 0; i <= amount; i++) {
            map.put(i, random.nextInt(-10, 10));
        }
        return map;
    }

    public static void main(String[] args) {
        OptimalTradingDaysFinder main = new OptimalTradingDaysFinder();

        // Test på at optimalTradingDaysAlgorithm funker
        Map<Integer, Integer> testMap = new HashMap<>();
        testMap.put(1,-1);
        testMap.put(2, 3);
        testMap.put(3, -9);
        testMap.put(4, 2);
        testMap.put(5, 2);
        testMap.put(6, -1);
        testMap.put(7, 2);
        testMap.put(8, -1);
        testMap.put(9, -5);
        System.out.println(main.optimalTradingDaysAlgorithm(testMap));

        // Tidsmålinger med n = 1000
        Date start = new Date();
        int runder = 0;
        double tid;
        Date slutt;
        do {
            main.optimalTradingDaysAlgorithm(main.generateIntegerMap(1000));
            slutt = new Date();
            ++runder;
        } while (slutt.getTime()-start.getTime() < 1000);
        tid = (double)
                (slutt.getTime()-start.getTime()) / runder;
        System.out.println("Millisekund pr. runde:" + tid + ", når n = 1000");

        // Tidsmålinger med n = 10000
        start = new Date();
        runder = 0;
        do {
            main.optimalTradingDaysAlgorithm(main.generateIntegerMap(10000));
            slutt = new Date();
            ++runder;
        } while (slutt.getTime()-start.getTime() < 1000);
        tid = (double)
                (slutt.getTime()-start.getTime()) / runder;
        System.out.println("Millisekund pr. runde:" + tid + ", når n = 10000");

        // Tidsmålinger med n = 100000
        start = new Date();
        runder = 0;
        do {
            main.optimalTradingDaysAlgorithm(main.generateIntegerMap(100000));
            slutt = new Date();
            ++runder;
        } while (slutt.getTime()-start.getTime() < 1000);
        tid = (double)
                (slutt.getTime()-start.getTime()) / runder;
        System.out.println("Millisekund pr. runde:" + tid + ", når n = 100000");

        // Vi ser at det er en økning på 100 når n*10.
        // Dette stemmer med analyse for kompleksiteten av programmet som var O(n^2)
    }
}