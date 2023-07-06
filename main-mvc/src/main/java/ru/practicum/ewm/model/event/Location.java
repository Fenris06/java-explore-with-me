package ru.practicum.ewm.model.event;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Embeddable
public class Location {
    private Float lat;
    private Float lon;
}
