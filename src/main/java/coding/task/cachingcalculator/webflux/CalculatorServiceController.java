package coding.task.cachingcalculator.webflux;

import coding.task.cachingcalculator.service.CalculatorService;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data class CalculationResult{
    private final BigDecimal result; private final String[] errors;

    static CalculationResult from(BigDecimal decimal) { return new CalculationResult(decimal, null); }

    static CalculationResult from(Throwable throwable) {
        final String[] errors =
                throwable.getSuppressed().length == 0 ? new String[]{ throwable.getMessage() }:
                Arrays.stream(throwable.getSuppressed()).map(Throwable::getMessage).toArray(String[]::new);
        return new CalculationResult(null, errors);
    }
}

@Slf4j
@RestController
@RequiredArgsConstructor
public class CalculatorServiceController{
    private final CalculatorService<BigDecimal> calculationService;

    private static final HttpHeaders commonHeaders=new HttpHeaders(CollectionUtils.toMultiValueMap(
            Stream.of(
                    new SimpleEntry<>(HttpHeaders.CONTENT_TYPE, Collections.singletonList(APPLICATION_JSON_UTF8_VALUE)),
                    new SimpleEntry<>(HttpHeaders.ETAG, Collections.singletonList("permanent"))
            ).collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue)))
    );

    @Cacheable(cacheNames="sums", key="#augend+':'+#addend+':'+#optionalAddend")
    @GetMapping({"/add/{augend}/{addend}", "/add/{augend}/{addend}/{optionalAddend}"})
    public Mono<ResponseEntity<CalculationResult>> add(
            @PathVariable("augend") String augend,
            @PathVariable("addend") String addend,
            @PathVariable(value="optionalAddend", required=false)Optional<String> optionalAddend){
        log.info("Processing a call to /add/{}/{}/{}", augend, addend, optionalAddend);

        Mono<BigDecimal> sum=calculationService.add(parseDecimal(augend), parseDecimal(addend));
        sum = calculationService.add(sum, parseOptionalDecimal(optionalAddend, ZERO));
        return renderOperationResult(sum);
    }

    @Cacheable(cacheNames="differences", key="#subtrahend+':'+#minuend+':'+#optionalMinuend")
    @GetMapping({"/subtract/{subtrahend}/{minuend}", "/subtract/{subtrahend}/{minuend}/{optionalMinuend}"})
      public Mono<ResponseEntity<CalculationResult>> subtract(
              @PathVariable("subtrahend")String subtrahend,
              @PathVariable("minuend")String minuend,
              @PathVariable(value="optionalMinuend", required=false)Optional<String> optionalMinuend){
        log.info("Processing a call to /subtract/{}/{}/{}", subtrahend, minuend, optionalMinuend);

        Mono<BigDecimal> difference=calculationService.subtract(parseDecimal(subtrahend), parseDecimal(minuend));
        difference=calculationService.subtract(difference, parseOptionalDecimal(optionalMinuend, ZERO));
        return renderOperationResult(difference);
    }

    @Cacheable(cacheNames="products", key="#multiplicand+':'+#multiplier+':'+#optionalMultiplier")
    @GetMapping({"/multiply/{multiplicand}/{multiplier}", "/multiply/{multiplicand}/{multiplier}/{optionalMultiplier}"})
      public Mono<ResponseEntity<CalculationResult>> multiply(
              @PathVariable("multiplicand")String multiplicand,
              @PathVariable("multiplier")String multiplier,
              @PathVariable(value="optionalMultiplier", required=false)Optional<String> optionalMultiplier){
        log.info("Processing a call to /multiply/{}/{}/{}", multiplicand, multiplier, optionalMultiplier);

        Mono<BigDecimal> product=calculationService.multiply(parseDecimal(multiplicand), parseDecimal(multiplier));
        product=calculationService.multiply(product, parseOptionalDecimal(optionalMultiplier, ONE));
        return renderOperationResult(product);
    }

    @Cacheable(cacheNames="quotients", key="#dividend+':'+#divisor")
    @GetMapping("/divide/{dividend}/{divisor}")
      public Mono<ResponseEntity<CalculationResult>> divide(
              @PathVariable("dividend")String dividend,
              @PathVariable("divisor")String divisor){
        log.info("Processing a call to /divide/{}/{}", dividend, divisor);

        Mono<BigDecimal> quotient=calculationService.divide(parseDecimal(dividend), parseDecimal(divisor));
        return renderOperationResult(quotient);
    }

    @Cacheable(cacheNames="powers", key="#base+':'+#exponent")
    @GetMapping("/pow/{base}/{exponent}")
      public Mono<ResponseEntity<CalculationResult>> pow(
              @PathVariable("base")String base,
              @PathVariable("exponent")String exponent){
        log.info("Processing a call to /pow/{}/{}", base, exponent);

        Mono<BigDecimal> power=calculationService.pow(parseDecimal(base),
                                                      parseInteger(exponent, e -> e >= 0 && e <= 999999999, "Exponent must fit into [0,999999999]"));
        return renderOperationResult(power);
    }

    private <T,R> Mono<R> parsePathVariable(T pathVariable, Function<T, R> pathVariableParser,
                                            Optional<String> validationMessage){
        try{
            return Mono.just(pathVariableParser.apply(pathVariable));
        }catch(Throwable t){
            String validationError = validationMessage.orElse(String.format("Could not parse %s", pathVariable));
            log.error(validationError);
            return Mono.error(new IllegalArgumentException(validationError, t));
        }
    }

    private Mono<Integer> parseInteger(String pathVariable,
                                       Predicate<Integer> isValidInteger, String validationMessage){
        return parsePathVariable(pathVariable, Integer::valueOf, Optional.of(validationMessage)).
               filter(isValidInteger).switchIfEmpty(Mono.error(new IllegalArgumentException(validationMessage)));
    }

    private Mono<BigDecimal> parseDecimal(String pathVariable){
        return parsePathVariable(pathVariable, BigDecimal::new, Optional.empty());
    }

    private Mono<BigDecimal> parseOptionalDecimal(Optional<String> pathVariable, BigDecimal identity){
        return parsePathVariable(pathVariable, opt -> opt.map(BigDecimal::new).orElse(identity), Optional.empty());
    }

    private Mono<ResponseEntity<CalculationResult>> renderOperationResult(Mono<BigDecimal> calculationResult){
        return calculationResult.map(CalculationResult::from).
                                 map(result -> ResponseEntity.ok().headers(commonHeaders).body(result)).
                                 onErrorResume(error -> Mono.just(ResponseEntity.badRequest().headers(commonHeaders).
                                                                                body(CalculationResult.from(error))));
    }
}