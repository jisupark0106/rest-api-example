package me.didoo.whiteshiprestapi.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@AllArgsConstructor
public class EventRepresentationModel extends RepresentationModel<Event> {

    @Getter
    @JsonUnwrapped
    private Event event;

}
