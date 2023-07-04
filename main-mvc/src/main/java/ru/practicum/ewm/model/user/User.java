package ru.practicum.ewm.model.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "email", unique = true, length = 254, nullable = false)
    private String email;
    @Column(name = "name", length = 250, nullable = false)
    private String name;
}
