package me.didoo.whiteshiprestapi.events;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

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

        EventEntityModel eventModel = new EventEntityModel(event);
        eventModel.add(linkTo(EventController.class).withRel("query-events"));
        eventModel.add(selfLinktBuilder.withRel("update-event"));
        eventModel.add(Link.of("/docs/index.html#resources-events-create", LinkRelation.of("profile")));

        return ResponseEntity.created(createdUri).body(eventModel);
    }

    @GetMapping
    public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> pagedResourcesAssembler){
        Page<Event> page = this.eventRepository.findAll(pageable);

        var pagedResources = pagedResourcesAssembler.toModel(page, EventEntityModel::new);
        pagedResources.add(Link.of("/docs/index.html#resources-events-list", LinkRelation.of("profile")));
        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity getEvent(@PathVariable Integer id){
        Optional<Event> optional = this.eventRepository.findById(id);
        if(optional.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Event event = optional.get();
        EventEntityModel eventEntityModel = new EventEntityModel(event);
        eventEntityModel.add(Link.of("/docs/index.html#resources-events-get", LinkRelation.of("profile")));
        return ResponseEntity.ok(eventEntityModel);


    }
}
