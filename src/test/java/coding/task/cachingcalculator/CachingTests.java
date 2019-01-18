package coding.task.cachingcalculator;

import coding.task.cachingcalculator.service.CalculatorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

@Profile("cachingtest")
@Configuration
class CachingCalculationServiceTestConfiguration{
    @SuppressWarnings("unchecked")
    @Bean @Primary public CalculatorService<BigDecimal> calculatorService(){ return Mockito.mock(CalculatorService.class); }
}

@RunWith(SpringRunner.class)
@ActiveProfiles("cachingtest")
public class CachingTests extends CalculatorServiceTests{
    @Autowired private CalculatorService<BigDecimal> calculatorService;

    @Test
    @SuppressWarnings("unchecked")
    public void testCachedResultReturnedWhenTheSameNumbersAreAdded(){
        when(calculatorService.add(any(Mono.class), any(Mono.class))).thenReturn(monoTenTimesMaxDouble);
        WebTestClient.ResponseSpec responseSpec=getSpec.uri(String.format("/add/%s/1/0", tenTimesMaxDoubleMinusOne)).accept(APPLICATION_JSON_UTF8).exchange();
        WebTestClient.BodyContentSpec json=responseSpec.expectStatus().isOk().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.result").isEqualTo(tenTimesMaxDouble);
        verify(calculatorService, times(2)).add(any(Mono.class), any(Mono.class));

        reset(calculatorService);
        responseSpec=getSpec.uri(String.format("/add/%s/1/0", tenTimesMaxDoubleMinusOne)).accept(APPLICATION_JSON_UTF8).exchange();
        json=responseSpec.expectStatus().isOk().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.result").isEqualTo(tenTimesMaxDouble);
        verify(calculatorService, never()).add(any(Mono.class), any(Mono.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCachedResultReturnedWhenTheSameNumbersAreSubtracted(){
        when(calculatorService.subtract(any(Mono.class), any(Mono.class))).thenReturn(monoTenTimesMaxDoubleMinusOne);
        WebTestClient.ResponseSpec responseSpec=getSpec.uri(String.format("/subtract/%s/1/0", tenTimesMaxDouble)).accept(APPLICATION_JSON_UTF8).exchange();
        WebTestClient.BodyContentSpec json=responseSpec.expectStatus().isOk().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.result").isEqualTo(tenTimesMaxDoubleMinusOne);
        verify(calculatorService, times(2)).subtract(any(Mono.class), any(Mono.class));

        reset(calculatorService);
        responseSpec=getSpec.uri(String.format("/subtract/%s/1/0", tenTimesMaxDouble)).accept(APPLICATION_JSON_UTF8).exchange();
        json=responseSpec.expectStatus().isOk().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.result").isEqualTo(tenTimesMaxDoubleMinusOne);
        verify(calculatorService, never()).subtract(any(Mono.class), any(Mono.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCachedResultReturnedWhenTheSameNumbersAreMultiplied(){
        when(calculatorService.multiply(any(Mono.class), any(Mono.class))).thenReturn(monoTenTimesMaxDouble);
        WebTestClient.ResponseSpec responseSpec=getSpec.uri(String.format("/multiply/%s/10/1", maxDouble)).accept(APPLICATION_JSON_UTF8).exchange();
        WebTestClient.BodyContentSpec json=responseSpec.expectStatus().isOk().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.result").isEqualTo(tenTimesMaxDouble);
        verify(calculatorService, times(2)).multiply(any(Mono.class), any(Mono.class));

        reset(calculatorService);
        responseSpec=getSpec.uri(String.format("/multiply/%s/10/1", maxDouble)).accept(APPLICATION_JSON_UTF8).exchange();
        json=responseSpec.expectStatus().isOk().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.result").isEqualTo(tenTimesMaxDouble);
        verify(calculatorService, never()).multiply(any(Mono.class), any(Mono.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCachedResultReturnedWhenTheSameNumbersAreDivided(){
        when(calculatorService.divide(any(Mono.class), any(Mono.class))).thenReturn(monoMaxDouble);
        WebTestClient.ResponseSpec responseSpec=getSpec.uri(String.format("/divide/%s/10", tenTimesMaxDouble)).accept(APPLICATION_JSON_UTF8).exchange();
        WebTestClient.BodyContentSpec json=responseSpec.expectStatus().isOk().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.result").isEqualTo(maxDouble);
        verify(calculatorService).divide(any(Mono.class), any(Mono.class));

        reset(calculatorService);
        responseSpec=getSpec.uri(String.format("/divide/%s/10", tenTimesMaxDouble)).accept(APPLICATION_JSON_UTF8).exchange();
        json=responseSpec.expectStatus().isOk().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.result").isEqualTo(maxDouble);
        verify(calculatorService, never()).divide(any(Mono.class), any(Mono.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCachedResultReturnedWhenTheSameNumberIsRaisedToTheSamePower(){
        when(calculatorService.pow(any(Mono.class), any(Mono.class))).thenReturn(monoMaxDoubleTo10);
        WebTestClient.ResponseSpec responseSpec = getSpec.uri(String.format("/pow/%s/10", maxDouble)).accept(APPLICATION_JSON_UTF8).exchange();
        WebTestClient.BodyContentSpec json = responseSpec.expectStatus().isOk().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.result").isEqualTo(maxDoubleTo10);
        verify(calculatorService).pow(any(Mono.class), any(Mono.class));

        reset(calculatorService);
        responseSpec = getSpec.uri(String.format("/pow/%s/10", maxDouble)).accept(APPLICATION_JSON_UTF8).exchange();
        json = responseSpec.expectStatus().isOk().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.result").isEqualTo(maxDoubleTo10);
        verify(calculatorService, never()).pow(monoMaxDouble, monoTen);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testAnticipatedErroneousResultsAreCachedTo(){
        when(calculatorService.divide(any(Mono.class), any(Mono.class))).thenReturn(Mono.error(new ArithmeticException("Division undefined")));
        WebTestClient.ResponseSpec responseSpec = getSpec.uri("/divide/0/0").accept(APPLICATION_JSON_UTF8).exchange();
        WebTestClient.BodyContentSpec json = responseSpec.expectStatus().isBadRequest().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.errors").isEqualTo("Division undefined");
        verify(calculatorService).divide(any(Mono.class), any(Mono.class));

        reset(calculatorService);
        responseSpec = getSpec.uri("/divide/0/0").accept(APPLICATION_JSON_UTF8).exchange();
        json = responseSpec.expectStatus().isBadRequest().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.errors").isEqualTo("Division undefined");
        verify(calculatorService, never()).divide(monoZero, monoZero);

        when(calculatorService.divide(any(Mono.class), any(Mono.class))).thenReturn(Mono.error(new ArithmeticException("Division by zero")));
        responseSpec = getSpec.uri("/divide/1/0").accept(APPLICATION_JSON_UTF8).exchange();
        json = responseSpec.expectStatus().isBadRequest().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.errors").isEqualTo("Division by zero");
        verify(calculatorService).divide(any(Mono.class), any(Mono.class));

        reset(calculatorService);
        responseSpec = getSpec.uri("/divide/1/0").accept(APPLICATION_JSON_UTF8).exchange();
        json = responseSpec.expectStatus().isBadRequest().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.errors").isEqualTo("Division by zero");
        verify(calculatorService, never()).divide(any(Mono.class), any(Mono.class));

        when(calculatorService.divide(any(Mono.class), any(Mono.class))).thenReturn(Mono.error(new ArithmeticException("Non-terminating decimal expansion; no exact representable decimal result.")));
        responseSpec = getSpec.uri("/divide/2/3").accept(APPLICATION_JSON_UTF8).exchange();
        json = responseSpec.expectStatus().isBadRequest().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.errors").isEqualTo("Non-terminating decimal expansion; no exact representable decimal result.");
        verify(calculatorService).divide(any(Mono.class), any(Mono.class));

        reset(calculatorService);
        responseSpec = getSpec.uri("/divide/2/3").accept(APPLICATION_JSON_UTF8).exchange();
        json = responseSpec.expectStatus().isBadRequest().expectHeader().contentType(APPLICATION_JSON_UTF8).expectBody();
        json.jsonPath("$.errors").isEqualTo("Non-terminating decimal expansion; no exact representable decimal result.");
        verify(calculatorService, never()).divide(monoTwo, monoThree);
    }
}
