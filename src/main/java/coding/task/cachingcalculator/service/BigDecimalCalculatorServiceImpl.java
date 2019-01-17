package coding.task.cachingcalculator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Slf4j
@Component
public class BigDecimalCalculatorServiceImpl implements CalculatorService<BigDecimal>{
    @Override
    public Mono<BigDecimal> add(Mono<BigDecimal> augend, Mono<BigDecimal> addend){
        log.info("Adding {} to {}", addend, augend);
        return op(augend, addend, BigDecimal::add);
    }

    @Override
    public Mono<BigDecimal> subtract(Mono<BigDecimal> subtrahend, Mono<BigDecimal> minuend){
        log.info("Subtracting {} from {}", minuend, subtrahend);
        return op(subtrahend, minuend, BigDecimal::subtract);
    }

    @Override
    public Mono<BigDecimal> multiply(Mono<BigDecimal> multiplicand, Mono<BigDecimal> multiplier){
        log.info("Multiplying {} by {}", multiplicand, multiplier);
        return op(multiplicand, multiplier, BigDecimal::multiply);
    }

    @Override
    public Mono<BigDecimal> divide(Mono<BigDecimal> dividend, Mono<BigDecimal> divisor){
        log.info("Dividing {} by {}", dividend, divisor);
        return op(dividend, divisor, BigDecimal::divide);
    }

    @Override
    public Mono<BigDecimal> pow(Mono<BigDecimal> base, Mono<Integer> exponent){
        log.info("Calculating {} to the power of {}", base, exponent);
        return op(base, exponent, BigDecimal::pow);
    }
}