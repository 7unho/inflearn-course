package april2nd.board.articleread.learning;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class LongToDoubleTest {
    @Test
    void longToDouble() {
        long longValue = 9_007_199_254_740_993L; // double은 52비트까지만 정수부를 정확하게 표현할 수 있어, 오차가 발생
        double doubleValue = (double) longValue;

        System.out.println("Long value: " + longValue);
        System.out.println("Converted double value: " + new BigDecimal(doubleValue).toString());

        long longValue2 = (long) doubleValue;
        System.out.printf("Converted back to long: %d%n", longValue2);
    }
}
