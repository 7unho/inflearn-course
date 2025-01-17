package com.april2nd.sample;

public class SampleApplication {
    public static void main(String[] args) {
        CalculationRequest calculationRequest = new CalculatorRequestReader().read();

        long answer = new Calculator().calculate(
                calculationRequest.getNum1(),
                calculationRequest.getOperator(),
                calculationRequest.getNum2()
        );
        System.out.println(answer);
    }
}
