package com.example.restapi.common;

import com.example.restapi.events.Event;
import com.example.restapi.events.EventController;
import com.example.restapi.index.IndexController;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ErrorsResource extends EntityModel<Errors> {

    public static EntityModel<Errors> of(Errors errors) {
        return EntityModel.of(errors).add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
    }

}

