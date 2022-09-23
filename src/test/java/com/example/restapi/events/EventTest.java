package com.example.restapi.events;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

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
}