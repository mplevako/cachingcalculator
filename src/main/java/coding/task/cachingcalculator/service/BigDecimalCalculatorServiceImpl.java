package coding.task.cachingcalculator.service;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Component
public class BigDecimalCalculatorServiceImpl implements CalculatorService<BigDecimal>{
    @Override
    public Mono<BigDecimal> add(Mono<BigDecimal> augend, Mono<BigDecimal> addend){
        return op(augend, addend, BigDecimal::add);
    }

    @Override
    public Mono<BigDecimal> subtract(Mono<BigDecimal> subtrahend, Mono<BigDecimal> minuend){
        return op(subtrahend, minuend, BigDecimal::subtract);
    }

    @Override
    public Mono<BigDecimal> multiply(Mono<BigDecimal> multiplicand, Mono<BigDecimal> multiplier){
        return op(multiplicand, multiplier, BigDecimal::multiply);
    }

    @Override
    public Mono<BigDecimal> divide(Mono<BigDecimal> dividend, Mono<BigDecimal> divisor){
        return op(dividend, divisor, BigDecimal::divide);
    }

    @Override
    public Mono<BigDecimal> pow(Mono<BigDecimal> base, Mono<Integer> exponent){
        return op(base, exponent, BigDecimal::pow);
    }
}