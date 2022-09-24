package com.example.restapi.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;

@Component
public class EventValidator{
    // errors에 담아줌
    public void validate(EventDto eventDto, Errors errors) {
        if(eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() > 0) {
            errors.rejectValue("basePrice", "WrongValue", "BasePrice is Wrong.");
            errors.rejectValue("maxPrice", "WrongValue", "MaxPrice is Wrong.");
        }

        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        LocalDateTime beginEventDateTime = eventDto.getBeginEventDateTime();
        LocalDateTime beginEnrollDateTime = eventDto.getBeginEnrollDateTime();
        LocalDateTime closeEnrollDateTime = eventDto.getCloseEnrollDateTime();

        if (endEventDateTime.isBefore(beginEventDateTime)
                || endEventDateTime.isBefore(beginEnrollDateTime)
                || endEventDateTime.isBefore(closeEnrollDateTime)) {
            errors.rejectValue("endEventDateTime", "WrongValue", "EndEventDateTime is Wrong.");
        }

        if (beginEventDateTime.isAfter(endEventDateTime)
                || beginEventDateTime.isBefore(beginEnrollDateTime)
                || beginEventDateTime.isBefore(closeEnrollDateTime)) {
            errors.rejectValue("beginEventDateTime", "WrongValue", "beginEventDateTime is Wrong.");
        }

        if (closeEnrollDateTime.isBefore(beginEnrollDateTime)
                || closeEnrollDateTime.isAfter(beginEventDateTime)
                || closeEnrollDateTime.isAfter(endEventDateTime)) {
            errors.rejectValue("closeEnrollDateTime", "WrongValue", "closeEnrollDateTime is Wrong.");
        }
    }
}
