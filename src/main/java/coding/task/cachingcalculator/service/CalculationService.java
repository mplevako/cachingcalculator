package coding.task.cachingcalculator.service;

import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Optional;

public interface CalculationService{
    Mono<BigDecimal> add(BigDecimal augend, BigDecimal addend, Optional<BigDecimal> optionalAddend);
    Mono<BigDecimal> subtract(BigDecimal subtrahend, BigDecimal minuend, Optional<BigDecimal> optionalMinuend);
    Mono<BigDecimal> multiply(BigDecimal multiplicand, BigDecimal multiplier, Optional<BigDecimal> optionalMultiplier);
    Mono<BigDecimal> divide(BigDecimal dividend, BigDecimal divisor);
    Mono<BigDecimal> pow(BigDecimal base, int exponent);
}
