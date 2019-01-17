package coding.task.cachingcalculator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;

import static java.math.BigDecimal.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CachingCalculatorApplication.class)
public class CalculatorServiceControllerTests{
    @Autowired private ApplicationContext context;

    private WebTestClient.RequestHeadersUriSpec getSpec;

    private final BigDecimal maxDouble = new BigDecimal(Double.MAX_VALUE);
    private final BigDecimal maxDoubleTo10 = maxDouble.pow(10);
    private final BigDecimal tenTimesMaxDouble = maxDouble.multiply(TEN);
    private final BigDecimal tenTimesMaxDoubleMinusOne = tenTimesMaxDouble.subtract(ONE);

    @Before
    public void setup() {
        WebTestClient webClient=WebTestClient.bindToApplicationContext(context).configureClient().build();
        getSpec = webClient.get();
    }

    @Test
    public void testDoubleAddition(){
        WebTestClient.ResponseSpec responseSpec = getSpec.uri("/add/0/0").accept(APPLICATION_JSON_UTF8).exchange();
        WebTestClient.BodyContentSpec json = responseSpec.expectStatus().isOk().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.result").isEqualTo("0");

        responseSpec = getSpec.uri(String.format("/add/%s/-1", tenTimesMaxDouble)).accept(APPLICATION_JSON_UTF8).exchange();
        json = responseSpec.expectStatus().isOk().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.result").isEqualTo(tenTimesMaxDoubleMinusOne);

        responseSpec = getSpec.uri(String.format("/add/%s/1", tenTimesMaxDoubleMinusOne)).accept(APPLICATION_JSON_UTF8).exchange();
        json = responseSpec.expectStatus().isOk().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.result").isEqualTo(tenTimesMaxDouble);
    }

    @Test
    public void testTripleAddition(){
        WebTestClient.ResponseSpec responseSpec = getSpec.uri("/add/0/0/0").accept(APPLICATION_JSON_UTF8).exchange();
        WebTestClient.BodyContentSpec json = responseSpec.expectStatus().isOk().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.result").isEqualTo("0");

        responseSpec = getSpec.uri("/add/1/2/3").accept(APPLICATION_JSON_UTF8).exchange();
        json = responseSpec.expectStatus().isOk().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.result").isEqualTo("6");
    }

    @Test
    public void testDoubleSubtraction(){
        WebTestClient.ResponseSpec responseSpec = getSpec.uri("/subtract/0/0").accept(APPLICATION_JSON_UTF8).exchange();
        WebTestClient.BodyContentSpec json = responseSpec.expectStatus().isOk().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.result").isEqualTo("0");

        responseSpec = getSpec.uri(String.format("/subtract/%s/1", tenTimesMaxDouble)).accept(APPLICATION_JSON_UTF8).exchange();
        json = responseSpec.expectStatus().isOk().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.result").isEqualTo(tenTimesMaxDoubleMinusOne);

        responseSpec = getSpec.uri(String.format("/subtract/%s/-1", tenTimesMaxDoubleMinusOne)).accept(APPLICATION_JSON_UTF8).exchange();
        json = responseSpec.expectStatus().isOk().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.result").isEqualTo(tenTimesMaxDouble);
    }

    @Test
    public void testTripleSubtraction(){
        WebTestClient.ResponseSpec responseSpec = getSpec.uri("/subtract/0/0/0").accept(APPLICATION_JSON_UTF8).exchange();
        WebTestClient.BodyContentSpec json = responseSpec.expectStatus().isOk().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.result").isEqualTo("0");

        responseSpec = getSpec.uri("/subtract/1/2/3").accept(APPLICATION_JSON_UTF8).exchange();
        json = responseSpec.expectStatus().isOk().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.result").isEqualTo("-4");
    }

    @Test
    public void testDoubleMultiplication(){
        WebTestClient.ResponseSpec responseSpec = getSpec.uri(String.format("/multiply/0/0")).accept(APPLICATION_JSON_UTF8).exchange();
        WebTestClient.BodyContentSpec json = responseSpec.expectStatus().isOk().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.result").isEqualTo("0");

        responseSpec = getSpec.uri("/multiply/1/1").accept(APPLICATION_JSON_UTF8).exchange();
        json = responseSpec.expectStatus().isOk().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.result").isEqualTo("1");

        responseSpec = getSpec.uri(String.format("/multiply/%s/1", tenTimesMaxDouble)).accept(APPLICATION_JSON_UTF8).exchange();
        json = responseSpec.expectStatus().isOk().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.result").isEqualTo(tenTimesMaxDouble);

        responseSpec = getSpec.uri(String.format("/multiply/%s/10", maxDouble)).accept(APPLICATION_JSON_UTF8).exchange();
        json = responseSpec.expectStatus().isOk().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.result").isEqualTo(tenTimesMaxDouble);
    }

    @Test
    public void testTripleMultiplication(){
        WebTestClient.ResponseSpec responseSpec = getSpec.uri("/multiply/0/0/0").accept(APPLICATION_JSON_UTF8).exchange();
        WebTestClient.BodyContentSpec json = responseSpec.expectStatus().isOk().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.result").isEqualTo("0");

        responseSpec = getSpec.uri("/multiply/1/1/1").accept(APPLICATION_JSON_UTF8).exchange();
        json = responseSpec.expectStatus().isOk().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.result").isEqualTo("1");

        responseSpec = getSpec.uri("/multiply/2/3/4").accept(APPLICATION_JSON_UTF8).exchange();
        json = responseSpec.expectStatus().isOk().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.result").isEqualTo("24");
    }

    @Test
    public void testDivision(){
        WebTestClient.ResponseSpec responseSpec = getSpec.uri(String.format("/divide/%s/%s", tenTimesMaxDouble, TEN)).accept(APPLICATION_JSON_UTF8).exchange();
        WebTestClient.BodyContentSpec json = responseSpec.expectStatus().isOk().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.result").isEqualTo(maxDouble);
    }

    @Test
    public void testDivisionByZero(){
        WebTestClient.ResponseSpec responseSpec = getSpec.uri(String.format("/divide/%s/%s", ONE, ZERO)).accept(APPLICATION_JSON_UTF8).exchange();
        WebTestClient.BodyContentSpec json = responseSpec.expectStatus().isBadRequest().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.error").isEqualTo("Division by zero");
    }

    @Test
    public void testNonRepresentableDivisionResult(){
        WebTestClient.ResponseSpec responseSpec = getSpec.uri("/divide/2/3").accept(APPLICATION_JSON_UTF8).exchange();
        WebTestClient.BodyContentSpec json = responseSpec.expectStatus().isBadRequest().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.error").isEqualTo("Non-terminating decimal expansion; no exact representable decimal result.");
    }

    @Test
    public void testPow(){
        WebTestClient.ResponseSpec responseSpec = getSpec.uri("/pow/3.14/0").accept(APPLICATION_JSON_UTF8).exchange();
        WebTestClient.BodyContentSpec json = responseSpec.expectStatus().isOk().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.result").isEqualTo("1");

        responseSpec = getSpec.uri("/pow/2.7/1").accept(APPLICATION_JSON_UTF8).exchange();
        json = responseSpec.expectStatus().isOk().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.result").isEqualTo("2.7");

        responseSpec = getSpec.uri("/pow/0/0").accept(APPLICATION_JSON_UTF8).exchange();
        json = responseSpec.expectStatus().isOk().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.result").isEqualTo("1");

        responseSpec = getSpec.uri(String.format("/pow/%s/%s", maxDouble, TEN)).accept(APPLICATION_JSON_UTF8).exchange();
        json = responseSpec.expectStatus().isOk().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.result").isEqualTo(maxDoubleTo10);
    }

    @Test
    public void testErroneousPow(){
        WebTestClient.ResponseSpec responseSpec = getSpec.uri("/pow/4/0.5").accept(APPLICATION_JSON_UTF8).exchange();
        WebTestClient.BodyContentSpec json = responseSpec.expectStatus().isBadRequest().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.error").isEqualTo("For input string: \"0.5\"");

        responseSpec = getSpec.uri("/pow/1/1000000000").accept(APPLICATION_JSON_UTF8).exchange();
        json = responseSpec.expectStatus().isBadRequest().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.error").isEqualTo("Invalid operation");
    }

    @Test
    public void testDifferentNumberNotations(){
        WebTestClient.ResponseSpec responseSpec = getSpec.uri(String.format("/add/0.1/1e1")).accept(APPLICATION_JSON_UTF8).exchange();
        WebTestClient.BodyContentSpec json = responseSpec.expectStatus().isOk().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.result").isEqualTo("10.1");

        responseSpec = getSpec.uri(String.format("/subtract/11.1/1e1")).accept(APPLICATION_JSON_UTF8).exchange();
        json = responseSpec.expectStatus().isOk().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.result").isEqualTo("1.1");

        responseSpec = getSpec.uri(String.format("/multiply/1e1/1e-1")).accept(APPLICATION_JSON_UTF8).exchange();
        json = responseSpec.expectStatus().isOk().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.result").isEqualTo("1");

        responseSpec = getSpec.uri(String.format("/divide/11.1/1e1")).accept(APPLICATION_JSON_UTF8).exchange();
        json = responseSpec.expectStatus().isOk().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.result").isEqualTo("1.11");

        responseSpec = getSpec.uri(String.format("/pow/3.14/2")).accept(APPLICATION_JSON_UTF8).exchange();
        json = responseSpec.expectStatus().isOk().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.result").isEqualTo("9.8596");
    }
}