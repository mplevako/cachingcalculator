package coding.task.cachingcalculator.webflux;

import coding.task.cachingcalculator.service.CalculationServiceImpl;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Data class CalculationResult{ private final BigDecimal result; }
@Data class Error{ private final String error; }

@Slf4j
@RestController
@RequiredArgsConstructor
public class CalculatorServiceController{
    private final CalculationServiceImpl calculationService;

    @GetMapping(path={"/add/{augend}/{addend}", "/add/{augend}/{addend}/{optionalAddend}"}, produces=APPLICATION_JSON_UTF8_VALUE)
    public Mono<CalculationResult> add(@PathVariable("augend")String augendPathVar, @PathVariable("addend")String addendPathVar,
                                       @PathVariable(value="optionalAddend", required=false)Optional<String> optionalAddendPathVar){
        log.info("Processing a call to /add/{}/{}/{}", augendPathVar, addendPathVar, optionalAddendPathVar);
        final BigDecimal augend=new BigDecimal(augendPathVar);
        final BigDecimal addend=new BigDecimal(addendPathVar);
        final Optional<BigDecimal> optionalAddend=optionalAddendPathVar.map(BigDecimal::new);
        return calculationService.add(augend, addend, optionalAddend).map(CalculationResult::new);
    }

    @GetMapping(path={"/subtract/{subtrahend}/{minuend}", "/subtract/{subtrahend}/{minuend}/{optionalMinuend}"}, produces=APPLICATION_JSON_UTF8_VALUE)
    public Mono<CalculationResult> subtract(@PathVariable("subtrahend")String subtrahendPathVar, @PathVariable("minuend")String minuendPathVar,
                                            @PathVariable(value="optionalMinuend", required=false)Optional<String> optionalMinuendPathVar){
        log.info("Processing a call to /subtract/{}/{}/{}", subtrahendPathVar, minuendPathVar, optionalMinuendPathVar);
        final BigDecimal subtrahend=new BigDecimal(subtrahendPathVar);
        final BigDecimal minuend=new BigDecimal(minuendPathVar);
        final Optional<BigDecimal> optionalMinuend=optionalMinuendPathVar.map(BigDecimal::new);
        return calculationService.subtract(subtrahend, minuend, optionalMinuend).map(CalculationResult::new);
    }

    @GetMapping(path={"/multiply/{multiplicand}/{multiplier}", "/multiply/{multiplicand}/{multiplier}/{optionalMultiplier}"}, produces=APPLICATION_JSON_UTF8_VALUE)
    public Mono<CalculationResult> multiply(@PathVariable("multiplicand")String multiplicandPathVar, @PathVariable("multiplier")String multiplierPathVar,
                                            @PathVariable(value="optionalMultiplier", required=false)Optional<String> optionalMultiplierPathVar){
        log.info("Processing a call to /multiply/{}/{}/{}", multiplicandPathVar, multiplierPathVar, optionalMultiplierPathVar);
        final BigDecimal multiplicand=new BigDecimal(multiplicandPathVar);
        final BigDecimal multiplier=new BigDecimal(multiplierPathVar);
        final Optional<BigDecimal> optionalMultiplier=optionalMultiplierPathVar.map(BigDecimal::new);
        return calculationService.multiply(multiplicand, multiplier, optionalMultiplier).map(CalculationResult::new);
    }

    @GetMapping(path="/divide/{dividend}/{divisor}", produces=APPLICATION_JSON_UTF8_VALUE)
    public Mono<CalculationResult> divide(@PathVariable("dividend")String dividendPathVar, @PathVariable("divisor")String divisorPathVar){
        log.info("Processing a call to /divide/{}/{}", dividendPathVar, divisorPathVar);
        final BigDecimal dividend=new BigDecimal(dividendPathVar);
        final BigDecimal divisor=new BigDecimal(divisorPathVar);
        return calculationService.divide(dividend, divisor).map(CalculationResult::new);
    }

    @GetMapping(path="/pow/{base}/{exponent}", produces=APPLICATION_JSON_UTF8_VALUE)
    public Mono<CalculationResult> pow(@PathVariable("base")String basePathVar, @PathVariable("exponent")String exponentPathVar){
        log.info("Processing a call to /pow/{}/{}", basePathVar, exponentPathVar);
        final BigDecimal base=new BigDecimal(basePathVar);
        final int exponent=Integer.parseInt(exponentPathVar);
        return calculationService.pow(base, exponent).map(CalculationResult::new);
    }

    @ExceptionHandler
    public ResponseEntity<Mono<Error>> handleExceptions(Throwable exception){
        log.error("An error occurred: {}", exception.getMessage());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(APPLICATION_JSON_UTF8);
        return new ResponseEntity<>(Mono.just(new Error(exception.getMessage())), httpHeaders, BAD_REQUEST);
    }
}