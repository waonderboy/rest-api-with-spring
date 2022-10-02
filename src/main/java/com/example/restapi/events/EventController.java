package com.example.restapi.events;

import com.example.restapi.common.ErrorsResource;
import com.example.restapi.index.IndexController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.*;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

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

    @GetMapping("/{id}")
    public ResponseEntity getEvent(@PathVariable Integer id) {
        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Event event = optionalEvent.get(); // 리소스로 만들어서 보내야함
        var eventResource = EventResource.of(event);
        eventResource.add(Link.of("/docs/index.html#resources-events-list", LinkRelation.of("profile")));

        return ResponseEntity.ok(eventResource);
    }

    @GetMapping
    public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler) {
        Page<Event> page = this.eventRepository.findAll(pageable);
        var pagedResources = assembler.toModel(page, e -> EventResource.of(e));
        pagedResources.add(Link.of("/docs/index.html#resources-events-list", LinkRelation.of("profile")));
        return ResponseEntity.ok(pagedResources);
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Validated EventDto eventDto, Errors errors) {
        // ResponseEntity를 사용하는 이유:: 응답 코드, 헤더, 본문 모두 다루기 편한 API
        // ResponseEntity.created() uri location 필요
        // HATEOS가 제공하는 linkTo(), methodOn() 사용 -> uri 생성
        // EventDto -> Event로 바꿔야함
        // @Validated 로 검증을하면 Errors나 BindingResult에 담을 수 있다
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(badResource(errors));
        }

        eventValidator.validate(eventDto, errors);

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(badResource(errors));
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
        EntityModel<Event> eventResource = EventResource.of(event);
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
        eventResource.add(selfLinkBuilder.withRel("update-event"));
        eventResource.add(Link.of("/docs/index.html#resources-events-create", LinkRelation.of("profile")));
        return ResponseEntity.created(createdUri).body(eventResource);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateEvent(@PathVariable Integer id, @RequestBody @Valid EventDto eventDto, Errors errors) {
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(badResource(errors));
        }

        eventValidator.validate(eventDto, errors);

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(badResource(errors));
        }

        Event existingEvent = optionalEvent.get();
        this.modelMapper.map(eventDto, existingEvent); // 기존 데이터에 덮어씀, 트랜잭션이 없어 자동 저장되는게 아님
        Event savedEvent = eventRepository.save(existingEvent);
        var evenResource = EventResource.of(savedEvent);
        evenResource.add(Link.of("/docs/index.html#resources-event-update", LinkRelation.of("profile")));

        return ResponseEntity.ok(evenResource);
    }

    private EntityModel<Errors> badResource(Errors errors) {
        return ErrorsResource.of(errors);
    }
}
