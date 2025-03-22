public class Calculator {
    long calculate(long num1, char operator, long num2) throws Exception {
        long result = 0;
        switch (operator) {
            case '+':
                result = num1 + num2;
                break;
            case '-':
                result = num1 - num2;
                break;
            case '/':
                if(num1 == 0 || num2 == 0) throw new Exception();
                result = num1 / num2;
                break;
            case '*':
                result = num1 * num2;
                break;

        }
        return result;
    }
}
