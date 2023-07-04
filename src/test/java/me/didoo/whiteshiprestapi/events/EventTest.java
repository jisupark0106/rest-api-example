package me.didoo.whiteshiprestapi.events;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
class EventTest {

    @Test
    public void builder() {
        Event event = Event.builder()
                .name("Spring rest API")
                .description("rest api with Spring")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBeanCreator() {
        Event event = new Event();

        String name = "javaBean Test";
        String description = "Spring javaBean Create Test";
        event.setName(name);
        event.setDescription(description);
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }
    @Test
    public void testOffLine() {
        Event event = Event.builder()
                .location("강남역")
                .build();
        event.update();

        assertThat(event.isOffline()).isTrue();

        event = Event.builder().build();

        event.update();

        assertThat(event.isOffline()).isFalse();
    }
}