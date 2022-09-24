package com.example.restapi.events;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class EventController {

    private final EventRepository eventRepository;

    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity createEvent(@RequestBody EventDto eventDto) {
        // ResponseEntity를 사용하는 이유:: 응답 코드, 헤더, 본문 모두 다루기 편한 API
        // ResponseEntity.created() uri location 필요
        // HATEOS가 제공하는 linkTo(), methodOn() 사용 -> uri 생성
        // EventDto -> Event로 바꿔야함
        Event event = modelMapper.map(eventDto, Event.class);

        Event newEvent = eventRepository.save(event);
        URI createdUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();
        return ResponseEntity.created(createdUri).body(event);
    }
}
