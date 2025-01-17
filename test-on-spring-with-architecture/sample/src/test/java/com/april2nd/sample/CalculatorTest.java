package com.april2nd.sample;

import org.junit.Assert;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CalculatorTest {
    @Test
    public void 덧셈_연산을_할_수_있다() throws Exception {
        //given
        long num1 = 2;
        String operator = "+";
        long num2 = 3;
        Calculator calculator = new Calculator();

        //when
        long result = calculator.calculate(num1, operator, num2);

        //then
        Assert.assertEquals(5, result);
//        assertThat(result).isEqualTo(6);
    }

    @Test
    public void 곱셈_연산을_할_수_있다() throws Exception {
        //given
        long num1 = 2;
        String operator = "*";
        long num2 = 3;
        Calculator calculator = new Calculator();

        //when
        long result = calculator.calculate(num1, operator, num2);

        //then
        Assert.assertEquals(6, result);
//        assertThat(result).isEqualTo(6);
    }

    @Test
    public void 뺼셈_연산을_할_수_있다() throws Exception {
        //given
        long num1 = 2;
        String operator = "-";
        long num2 = 3;
        Calculator calculator = new Calculator();

        //when
        long result = calculator.calculate(num1, operator, num2);

        //then
        Assert.assertEquals(-1, result);
//        assertThat(result).isEqualTo(6);
    }

    @Test
    public void 나눗셈_연산을_할_수_있다() throws Exception {
        //given
        long num1 = 6;
        String operator = "/";
        long num2 = 3;
        Calculator calculator = new Calculator();

        //when
        long result = calculator.calculate(num1, operator, num2);

        //then
        Assert.assertEquals(2, result);
//        assertThat(result).isEqualTo(6);
    }
}