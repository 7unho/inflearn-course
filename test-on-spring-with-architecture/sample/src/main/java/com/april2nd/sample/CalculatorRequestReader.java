package com.april2nd.sample;

import java.util.Scanner;

public class CalculatorRequestReader {
    public CalculationRequest read() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter two numbers and an operator (ex] 1 + 2 ): ");
        String result = scanner.nextLine();
        return new CalculationRequest(result.split(" "));
    }
}
