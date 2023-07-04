package me.didoo.whiteshiprestapi.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class EventValidator {

    public void validate(EventDto eventDto, Errors errors) {

        if (eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() > 0) {
            errors.rejectValue("BasePrice", "Invalid Value", "BasePrice is invalid");
            errors.rejectValue("MaxPrice", "Invalid Value", "MaxPrice is invalid");

        }

        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if (endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) ||
                endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
                endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())) {
            errors.rejectValue("endEventDateTime", "Invalid Value", "endEventDateTime is invalid");
        }
        // TODO closeEnrollmentDateTime
        LocalDateTime closeEnrollmentDateTime = eventDto.getEndEventDateTime();
        if(closeEnrollmentDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())){
            errors.rejectValue("closeEnrollmentDateTime", "Invalid Value", "closeEnrollmentDateTime is invalid");
        }
    }
}
