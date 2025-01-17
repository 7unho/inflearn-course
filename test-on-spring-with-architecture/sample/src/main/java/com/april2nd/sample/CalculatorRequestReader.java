package com.april2nd.sample;

import java.util.Scanner;

public class CalculatorRequestReader {
    public CalculationRequest read() {
        Scanner scanner = new Scanner(System.in);
        String result = scanner.nextLine();
        return new CalculationRequest(result.split(" "));
    }
}
