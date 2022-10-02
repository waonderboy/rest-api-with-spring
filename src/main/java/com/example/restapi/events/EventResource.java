package com.example.restapi.events;

import com.example.restapi.index.IndexController;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


public class EventResource extends EntityModel<Event> {
    //public class EventResource extends RepresentationModel {
//    @JsonUnwrapped
//    private Event event;
//
//    public EventResource(Event event) {
//        this.event = event;
//        add(linkTo(EventController.class).slash(event.getId()).withSelfRel()); // 타입세잎
//    }
//
//    public Event getEvent() {
//        return this.event;
//    }

    public static EntityModel<Event> of(Event event) {
        return EntityModel.of(event).add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
    }
}
