package ru.mirouqe.playground.dto.mapper.play.one;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Component
public class MapperAaa {

    private final ModelMapper mapper;

    public MapperAaa(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public Aaa toEntity(DtoAaa dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, Aaa.class);
    }

    public DtoAaa toDto(Aaa entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, DtoAaa.class);
    }
}
