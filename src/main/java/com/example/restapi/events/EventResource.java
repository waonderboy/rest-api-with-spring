package com.example.restapi.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


public class EventResource extends RepresentationModel {
    @JsonUnwrapped
    private Event event;

    public EventResource(Event event) {
        this.event = event;
        add(linkTo(EventController.class).slash(event.getId()).withSelfRel()); // 타입세잎
    }

    public Event getEvent() {
        return this.event;
    }
}