package com.april2nd.sample;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

public class CalculationRequestReaderTest {
    @Test
    public void System_in으로_데이터를_읽어들일_수_있다() {
        // give
        CalculatorRequestReader calculatorRequestReader = new CalculatorRequestReader();

        // when
        System.setIn(new ByteArrayInputStream("2 + 3".getBytes()));
        String[] result = calculatorRequestReader.read();

        // then
        Assert.assertEquals("2", result[0]);
        Assert.assertEquals("+", result[1]);
        Assert.assertEquals("3", result[2]);
    }

    // TODO: 유효하지 않은 길이의 연산자가 들어오면 에러를 던진다.
    // TODO: 유효한_길이의_숫자가_들어오지_않으면_에러를_던진다
    // TODO: 세자리_숫자가_넘어가는_유효한_숫자를_파싱할_수_있다
    // TODO: 유효하지_않은_연산자가_들어오면_에러를_던진다
    // TODO: 유효한_숫자를_파싱할_수_있다
}
