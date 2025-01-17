package com.april2nd.sample;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;

public class CalculationRequestReaderTest {
    @Test
    public void System_in으로_데이터를_읽어들일_수_있다() {
        // give
        CalculatorRequestReader calculatorRequestReader = new CalculatorRequestReader();

        // when
        System.setIn(new ByteArrayInputStream("2 + 3".getBytes()));
        CalculationRequest result = calculatorRequestReader.read();

        // then
        Assert.assertEquals("2", result.getNum1());
        Assert.assertEquals("+", result.getOperator());
        Assert.assertEquals("3", result.getNum2());
    }
}
