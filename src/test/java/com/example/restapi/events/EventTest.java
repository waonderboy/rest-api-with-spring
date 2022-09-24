package com.example.restapi.events;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class EventTest {

    @Test
    public void builder() {
        Event event = Event.builder()
                .name("Test REST API")
                .description("Only Test")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean() {
        // given
        String eventName = "Test REST API";
        String eventDescription = "TEST";

        // when
        Event event = new Event();
        event.setName(eventName);
        event.setDescription(eventDescription);

        // then
        assertThat(event.getName()).isEqualTo(eventName);
        assertThat(event.getDescription()).isEqualTo(eventDescription);
    }

    @ParameterizedTest
    @MethodSource("paramsForTestFree")
    public void testFree(int basePrice, int maxPrice, boolean isFree) {
        // given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();
        // update
        event.update();
        // then
        Assertions.assertThat(event.isFree()).isEqualTo(isFree);
    }

    @ParameterizedTest
    @MethodSource("paramsForTestOffline")
    public void testOffline(String location, boolean offline) {
        // given
        Event event = Event.builder()
                .location(location)
                .build();
        // update
        event.update();
        // then
        Assertions.assertThat(event.isOffline()).isEqualTo(offline);
    }

    private static Stream<Arguments> paramsForTestFree() {
        return Stream.of(
                Arguments.of(0,0, true),
                Arguments.of(100, 0, false),
                Arguments.of(0, 100, false),
                Arguments.of(100, 200, false)
        );
    }

    private static Stream<Arguments> paramsForTestOffline() {
        return Stream.of(
                Arguments.of("강남", true),
                Arguments.of(null, false),
                Arguments.of("        ", false)
        );
    }
}