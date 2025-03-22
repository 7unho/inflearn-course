import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculatorTest {
    private final Calculator calculator = new Calculator();

    @ParameterizedTest
    @MethodSource("provideValidInputs")
    void testValidCalculation(long num1, char operator, long num2, long expected) throws Exception {
        // given

        // when
        long result = calculator.calculate(num1, operator, num2);

        // then
        assertEquals(result, expected);
    }

    private static Stream<Arguments> provideValidInputs() {
        return Stream.of(
                Arguments.of(1, "+", 2, 3),
                Arguments.of(123, "+", 234, 357),
                Arguments.of(111, "+", 222, 333),
                Arguments.of(100, "-", 50, 50),
                Arguments.of(10, "/", 2, 5),
                Arguments.of(6, "*", 50, 300)
        );
    }
}