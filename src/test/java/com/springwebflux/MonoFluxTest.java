package com.springwebflux;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class MonoFluxTest {

    @Test
    public void createMono(){
        Mono<String> stringMono = Mono.just("Rambe"); // Proses di dalam just sama aja kayak synchronous
        Mono<String> stringMono2 = Mono.create(monoSink -> {
            monoSink.success("Rambe");
            monoSink.error(new NullPointerException());
        });
        Mono<String> stringMono3 = Mono.from(subscriber -> {
            subscriber.onNext("Rambe");
            subscriber.onComplete();
            subscriber.onError(new NullPointerException());
        });

        StepVerifier.create(stringMono)
                .expectNext("Rambe")
                .verifyComplete();

        StepVerifier.create(stringMono2)
                .expectNext("Rambe")
                .verifyComplete();

        StepVerifier.create(stringMono3)
                .expectNext("Rambe")
                .expectComplete();
    }

    @Test
    public void createFlux(){

        List<String> names = Arrays.asList("apple", "banana", "kiwi");
        Flux<String> flux = Flux.just("apple", "banana", "kiwi");
        Flux<String> flux2 = Flux.create(stringFluxSink -> {
            stringFluxSink.next("apple");
            stringFluxSink.next("banana");
            stringFluxSink.next("kiwi");
            stringFluxSink.complete();
        });
        Flux<String> flux3 = Flux.from(subscriber -> {
            subscriber.onNext("apple");
            subscriber.onNext("banana");
            subscriber.onNext("kiwi");
            subscriber.onComplete();
        });

        StepVerifier.create(flux)
                .expectNext("apple")
                .expectNext("banana")
                .expectNext("kiwi")
                .verifyComplete();

        StepVerifier.create(flux2)
                .expectNext("apple")
                .expectNext("banana")
                .expectNext("kiwi")
                .verifyComplete();

        StepVerifier.create(flux3)
                .assertNext(names::contains)
                .assertNext(names::contains)
                .assertNext(names::contains)
                .expectComplete();
    }

    @Test
    public void mergeFluxExample(){
        Flux<String> names = Flux.just("albert", "budi", "chiki")
                .delayElements(Duration.ofMillis(500));

        Flux<String> fruits = Flux.just("kiwi", "apple", "banana")
                .delayElements(Duration.ofMillis(500))
                .delaySubscription(Duration.ofMillis(250));

        Flux<String> combined = names.mergeWith(fruits);

        StepVerifier.create(combined)
                .expectNext("albert")
                .expectNext("kiwi")
                .expectNext("budi")
                .expectNext("apple")
                .expectNext("chiki")
                .expectNext("banana")
                .verifyComplete();
    }

    @Test
    public void zipFluxExample(){
        Flux<String> names = Flux.just("albert", "budi", "chiki")
                .delayElements(Duration.ofMillis(500));

        Flux<String> fruits = Flux.just("kiwi", "apple", "banana")
                .delayElements(Duration.ofMillis(500))
                .delaySubscription(Duration.ofMillis(250));

        Flux<Tuple2<String, String>> combined = names.zipWith(fruits);

        StepVerifier.create(combined)
                .expectNextMatches(res -> "albert".equals(res.getT1()) && "kiwi".equals(res.getT2()))
                .expectNextMatches(res -> "budi".equals(res.getT1()) && "apple".equals(res.getT2()))
                .expectNextMatches(res -> "chiki".equals(res.getT1()) && "banana".equals(res.getT2()))
                .verifyComplete();
    }

    @Test
    public void concatFluxExample(){
        Flux<String> names = Flux.just("albert", "budi", "chiki")
                .delayElements(Duration.ofMillis(500));

        Flux<String> fruits = Flux.just("kiwi", "apple", "banana")
                .delayElements(Duration.ofMillis(500))
                .delaySubscription(Duration.ofMillis(250));


        Flux<String> concatted = names.concatWith(fruits);

        StepVerifier.create(concatted)
                .expectNext("albert")
                .expectNext("budi")
                .expectNext("chiki")
                .expectNext("kiwi")
                .expectNext("apple")
                .expectNext("banana")
                .verifyComplete();
    }

    @Test
    // Yang diambil flux yang selesai duluan
    public void firstFlux(){
        Flux<String> names = Flux.just("albert", "budi", "chiki")
                .delayElements(Duration.ofMillis(500));

        Flux<String> fruits = Flux.just("kiwi", "apple", "banana")
                .delayElements(Duration.ofMillis(500))
                .delaySubscription(Duration.ofMillis(250));

        Flux<String> first = Flux.firstWithSignal(names, fruits);

        StepVerifier.create(first)
                .expectNext("albert")
                .expectNext("budi")
                .expectNext("chiki")
                .verifyComplete();
    }

    @Test
    public void mapExample(){
        Flux<String> names = Flux.just("albert", "budi", "chiki");
        Flux<String> upperCase = names.map(String::toUpperCase);

        StepVerifier.create(upperCase)
                .expectNext("ALBERT")
                .expectNext("BUDI")
                .expectNext("CHIKI")
                .verifyComplete();
    }

    @Test
    public void mapHasError(){
        Flux<String> names = Flux.just("albert", "budi", "chiki");
        Flux<String> upperCase = names.map(e -> {
            if(e.equals("budi")) throw new NullPointerException();
            return e.toUpperCase();
        });

        StepVerifier.create(upperCase)
                .expectNext("ALBERT")
                .verifyError();
    }

    @Test
    public void mapHasErrorDoOnError(){
        Flux<String> names = Flux.just("albert", "budi", "chiki");
        Flux<String> upperCase = names
                .map(e -> {
                    if(e.equals("budi")) throw new NullPointerException();
                    return e.toUpperCase();
                })
                .doOnError(e-> {
                    System.out.println("error is: " + e.getMessage());
                    // Biasanya untuk revert proses sebelumnya
                });


        StepVerifier.create(upperCase)
                .expectNext("ALBERT")
                .verifyError();
    }

    @Test
    public void flatMapExample(){
        List<String> arrayNames = Arrays.asList(new String[]{"albert", "budi", "chiki"});
        List<String> arrayUpperCased = Arrays.asList(new String[]{"ALBERT", "BUDI", "CHIKI"});
        Flux<String> names = Flux.fromIterable(arrayNames);
        Flux<String> upperCase = names.flatMap(
                name -> Mono.just(name).map(String::toUpperCase).subscribeOn(Schedulers.parallel())
        );

        upperCase.log().subscribe();

        StepVerifier.create(upperCase)
                .assertNext(res -> assertThat(arrayUpperCased).contains(res))
                .assertNext(res -> assertThat(arrayUpperCased).contains(res))
                .assertNext(res -> assertThat(arrayUpperCased).contains(res))
                .verifyComplete();

    }

    @Test
    void takeExample(){
        Flux<String> names = Flux.just("albert", "budi", "chiki");
        Flux<String> taken = names.take(2);

        StepVerifier.create(taken)
                .expectNext("albert")
                .expectNext("budi")
                .verifyComplete();
    }

    @Test
    void skipExample(){
        Flux<String> names = Flux.just("albert", "budi", "chiki");
        Flux<String> skipped = names.skip(2);

        StepVerifier.create(skipped)
                .expectNext("chiki")
                .verifyComplete();
    }

    @Test
    void filter(){
        Flux<String> names = Flux.just("albert", "budi", "chiki");
        Flux<String> filter = names.filter(res -> "budi".equals(res));

        StepVerifier.create(filter)
                .expectNext("budi")
                .verifyComplete();
    }

    @Test
    void any(){
        Flux<String> names = Flux.just("albert", "budi", "chiki");
        Mono<Boolean> any = names.any(res -> res.contains("b"));

        StepVerifier.create(any)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void all(){
        Flux<String> names = Flux.just("albert", "budi", "chiki");
        Mono<Boolean> all = names.all(res -> res.contains("b"));

        StepVerifier.create(all)
                .expectNext(false)
                .verifyComplete();
    }
}
