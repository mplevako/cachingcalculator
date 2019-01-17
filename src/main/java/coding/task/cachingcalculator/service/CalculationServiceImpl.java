package coding.task.cachingcalculator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Component
public class CalculationServiceImpl implements CalculationService{
    @Override
    public Mono<BigDecimal> add(BigDecimal augend, BigDecimal addend, Optional<BigDecimal> optionalAddend){
        log.info("Adding {} and {} to {}", addend, optionalAddend, augend);
        BigDecimal result = augend.add(addend).add(optionalAddend.orElse(BigDecimal.ZERO));
        return Mono.just(result);
    }

    @Override
    public Mono<BigDecimal> subtract(BigDecimal subtrahend, BigDecimal minuend, Optional<BigDecimal> optionalMinuend){
        log.info("Subtracting {} and {} from {}", minuend, optionalMinuend, subtrahend);
        BigDecimal result = subtrahend.subtract(minuend).subtract(optionalMinuend.orElse(BigDecimal.ZERO));
        return Mono.just(result);
    }

    @Override
    public Mono<BigDecimal> multiply(BigDecimal multiplicand, BigDecimal multiplier, Optional<BigDecimal> optionalMultiplier){
        log.info("Multiplying {} and {} and {}", multiplicand, multiplier, optionalMultiplier);
        BigDecimal result = multiplicand.multiply(multiplier).multiply(optionalMultiplier.orElse(BigDecimal.ONE));
        return Mono.just(result);
    }

    @Override
    public Mono<BigDecimal> divide(BigDecimal dividend, BigDecimal divisor){
        log.info("Dividing {} by {}", dividend, divisor);
        BigDecimal result = dividend.divide(divisor);
        return Mono.just(result);
    }

    @Override
    public Mono<BigDecimal> pow(BigDecimal base, int exponent){
        log.info("Calculating {} to the power of {}", base, exponent);
        BigDecimal result = base.pow(exponent);
        return Mono.just(result);
    }
}