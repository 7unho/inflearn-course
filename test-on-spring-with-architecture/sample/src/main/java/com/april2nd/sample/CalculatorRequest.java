package com.april2nd.sample;

public class CalculatorRequest {
    private final long num1;
    private final String operator;
    private final long num2;

    public CalculatorRequest(String[] parts) {
        this.num1 = Long.parseLong(parts[0]);
        this.operator = parts[1];
        this.num2 = Long.parseLong(parts[2]);
    }

    public long getNum1() {
        return num1;
    }

    public String getOperator() {
        return operator;
    }

    public long getNum2() {
        return num2;
    }
}
