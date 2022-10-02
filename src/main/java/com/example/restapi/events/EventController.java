package com.example.restapi.events;

import com.example.restapi.common.ErrorsResource;
import com.example.restapi.index.IndexController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilderDslKt.withRel;

@RestController
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class EventController {

    private final EventRepository eventRepository;

    private final ModelMapper modelMapper;

    private final EventValidator eventValidator;

    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Validated EventDto eventDto, Errors errors) {
        // ResponseEntity를 사용하는 이유:: 응답 코드, 헤더, 본문 모두 다루기 편한 API
        // ResponseEntity.created() uri location 필요
        // HATEOS가 제공하는 linkTo(), methodOn() 사용 -> uri 생성
        // EventDto -> Event로 바꿔야함
        // @Validated 로 검증을하면 Errors나 BindingResult에 담을 수 있다
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(badRequest(errors));
        }

        eventValidator.validate(eventDto, errors);

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(badRequest(errors));
        }

        Event event = modelMapper.map(eventDto, Event.class);
        event.update(); // update free
        Event newEvent = eventRepository.save(event);
        WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());// location header
        URI createdUri = selfLinkBuilder.toUri();
        /**
         * 리팩토링시 링크를 추가하는 코드는 EventResource에 추가하는게 좋다
         * EventResource의 역할이기 때문
         */
        EventResource eventResource = new EventResource(event);
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
        eventResource.add(selfLinkBuilder.withRel("update-event"));
        eventResource.add(Link.of("/docs/index.html#resources-events-create", LinkRelation.of("profile")));
        return ResponseEntity.created(createdUri).body(eventResource);
    }

    private EntityModel<Errors> badRequest(Errors errors) {
        return ErrorsResource.of(errors);
    }
}
