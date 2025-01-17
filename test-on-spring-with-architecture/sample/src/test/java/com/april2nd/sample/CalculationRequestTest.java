package com.april2nd.sample;

import org.junit.Assert;
import org.junit.Test;

public class CalculationRequestTest {
    // TODO: 유효하지_않은_길이의_연산자가_들어오면_에러를_던진다
    @Test
    public void 유효하지_않은_길이의_연산자가_들어오면_에러를_던진다() {
        //given
        String[] parts = new String[]{"2", "+-", "3"};
        //when
        //then
        Assert.assertThrows(InvalidOperatorException.class, () -> {
            new CalculationRequest(parts);
        });
    }

    // TODO: 유효한_길이의_숫자가_들어오지_않으면_에러를_던진다
    @Test
    public void 유효한_길이의_숫자가_들어오지_않으면_에러를_던진다() {
        //given
        String[] parts = new String[]{"232", "+"};
        //when
        //then
        Assert.assertThrows(BadRequestException.class, () -> {
            new CalculationRequest(parts);
        });
    }

    // TODO: 세자리_숫자가_넘어가는_유효한_숫자를_파싱할_수_있다
    @Test
    public void 세자리_숫자가_넘어가는_유효한_숫자를_파싱할_수_있다() {
        //given
        String[] parts = new String[]{"212", "+", "312321"};
        //when
        CalculationRequest calculationRequest = new CalculationRequest(parts);
        //then
        Assert.assertEquals(212, calculationRequest.getNum1());
        Assert.assertEquals("+", calculationRequest.getOperator());
        Assert.assertEquals(312321, calculationRequest.getNum2());
    }

    @Test
    public void 유효하지_않은_연산자가_들어오면_에러를_던진다() {
        //given
        String[] parts = new String[]{"2", "%", "3"};
        //when
        //then
        Assert.assertThrows(InvalidOperatorException.class, () -> {
            new CalculationRequest(parts);
        });
    }

    @Test
    public void 유효한_숫자를_파싱할_수_있다() {
        //given
        String[] parts = new String[]{"2", "+", "3"};
        //when
        CalculationRequest calculationRequest = new CalculationRequest(parts);
        //then
        Assert.assertEquals(2, calculationRequest.getNum1());
        Assert.assertEquals("+", calculationRequest.getOperator());
        Assert.assertEquals(3, calculationRequest.getNum2());
    }
}
