package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Genre extends BaseUnit {
    private final short id;
    private final String name;
}
