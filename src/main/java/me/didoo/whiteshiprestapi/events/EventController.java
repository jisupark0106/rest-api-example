package me.didoo.whiteshiprestapi.events;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final EventValidator eventValidator;

    public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator) {

        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.eventValidator = eventValidator;
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        eventValidator.validate(eventDto, errors);

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        Event event = modelMapper.map(eventDto, Event.class);
        event.update();
        Event newEvent = eventRepository.save(event);

        WebMvcLinkBuilder selfLinktBuilder = linkTo(EventController.class).slash("{id}");
        URI createdUri = selfLinktBuilder.toUri();
        event.setId(1);

        EventRepresentationModel eventModel = new EventRepresentationModel(event);
        eventModel.add(linkTo(EventController.class).withRel("query-events"));
        eventModel.add(selfLinktBuilder.withSelfRel());
        eventModel.add(selfLinktBuilder.withRel("update-event"));
        eventModel.add(Link.of("/docs/index.html#resources-events-create", LinkRelation.of("profile")));

        return ResponseEntity.created(createdUri).body(eventModel);
    }

    @GetMapping
    public ResponseEntity queryEvent(Pageable pageable){
        return ResponseEntity.ok(this.eventRepository.findAll(pageable));
    }
}
