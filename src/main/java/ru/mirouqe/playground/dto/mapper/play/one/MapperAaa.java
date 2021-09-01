package ru.mirouqe.playground.dto.mapper.play.one;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;

@Component
public class MapperAaa {

    private final ModelMapper mapper;

    public MapperAaa(ModelMapper mapper) {
        this.mapper = mapper;
        this.mapper
                .createTypeMap(DtoAaa.class, Aaa.class)
                .addMapping(source -> source.getBbbs(), (destination, value) -> {
                    List<Bbb> values = (List<Bbb>) value;

                    values.forEach(i -> i.setAaa(destination));
                    destination.setBbbs(values);
                });
    }

    public Aaa toEntity(DtoAaa dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, Aaa.class);
    }

    public DtoAaa toDto(Aaa entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, DtoAaa.class);
    }

}
