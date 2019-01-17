package coding.task.cachingcalculator.service;

import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

public interface CalculatorService<T>{
    Mono<T> add(Mono<T> augend, Mono<T> addend);

    Mono<T> subtract(Mono<T> subtrahend, Mono<T> minuend);

    Mono<T> multiply(Mono<T> multiplicand, Mono<T> multiplier);

    Mono<T> divide(Mono<T> dividend, Mono<T> divisor);

    Mono<T> pow(Mono<T> base, Mono<Integer> exponent);

    default <A, B> Mono<T> op(Mono<A> a, Mono<B> b, BiFunction<A, B, T> biFunction){
        return Mono.zipDelayError(a, b).map(pair -> biFunction.apply(pair.getT1(), pair.getT2()));
    }
}