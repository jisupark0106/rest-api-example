package me.didoo.whiteshiprestapi.index;

import me.didoo.whiteshiprestapi.events.EventController;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
public class IndexController {

    @GetMapping("/api")
    public RepresentationModel index(){
        RepresentationModel representationModel = new RepresentationModel();
        representationModel.add(linkTo(EventController.class).withRel("events"));
        return representationModel;
    }

}
