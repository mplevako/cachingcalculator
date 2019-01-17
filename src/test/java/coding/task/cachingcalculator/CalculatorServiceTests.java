package coding.task.cachingcalculator;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static java.math.BigDecimal.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CachingCalculatorApplication.class)
public class CalculatorServiceTests{
    @Autowired private ApplicationContext context;

    WebTestClient.RequestHeadersUriSpec getSpec;

    final BigDecimal maxDouble = new BigDecimal(Double.MAX_VALUE);
    final BigDecimal maxDoubleTo10 = maxDouble.pow(10);
    final BigDecimal tenTimesMaxDouble = maxDouble.multiply(TEN);
    final BigDecimal tenTimesMaxDoubleMinusOne = tenTimesMaxDouble.subtract(ONE);

    final Mono<BigDecimal> monoZero=Mono.just(ZERO);
    final Mono<BigDecimal> monoTwo=Mono.just(new BigDecimal(2));
    final Mono<BigDecimal> monoThree=Mono.just(new BigDecimal(3));
    final Mono<Integer>    monoTen=Mono.just(10);
    final Mono<BigDecimal> monoMaxDouble=Mono.just(maxDouble);
    final Mono<BigDecimal> monoMaxDoubleTo10=monoMaxDouble.map(n -> n.pow(10));
    final Mono<BigDecimal> monoTenTimesMaxDouble=monoMaxDouble.map(n -> n.multiply(TEN));
    final Mono<BigDecimal> monoTenTimesMaxDoubleMinusOne=monoMaxDouble.map(n -> n.multiply(TEN).subtract(ONE));

    @Before
    public void setup() {
        WebTestClient webClient=WebTestClient.bindToApplicationContext(context).configureClient().build();
        getSpec = webClient.get();
    }
}
