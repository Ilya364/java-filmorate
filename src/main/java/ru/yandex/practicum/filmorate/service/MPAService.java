package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.MPADao;
import ru.yandex.practicum.filmorate.model.MPA;

@Service
@AllArgsConstructor
public class MPAService extends AbstractService<MPADao, MPA> {
    private final MPADao dao;
}
