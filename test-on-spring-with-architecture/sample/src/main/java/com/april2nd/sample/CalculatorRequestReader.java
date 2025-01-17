package com.april2nd.sample;

import java.util.Scanner;

public class CalculatorRequestReader {
    public String[] read() {
        Scanner scanner = new Scanner(System.in);
        String result = scanner.nextLine();
        return result.split(" ");
    }
}
