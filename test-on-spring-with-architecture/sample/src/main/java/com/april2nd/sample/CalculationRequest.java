package com.april2nd.sample;

import java.util.Arrays;

public class CalculationRequest {
    private final long num1;
    private final String operator;
    private final long num2;

    public CalculationRequest(String[] parts) {
        if (parts.length != 3) {
            throw new BadRequestException();
        }

        Long num1 = Long.parseLong(parts[0]);
        String operator = parts[1];
        Long num2 = Long.parseLong(parts[2]);

        if (operator.length() != 1 || isInvalidOperator(operator)) {
            throw new InvalidOperatorException();
        }

        this.num1 = Long.parseLong(parts[0]);
        this.operator = parts[1];
        this.num2 = Long.parseLong(parts[2]);
    }

    private boolean isInvalidOperator(String operator) {
        return !Arrays.asList("+", "-", "*", "/").contains(operator);
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
