package ru.mirouqe.playground.dto.mapper.play.one;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Component
public class MapperBbb {
    private final RepoAaa repoAaa;

    private final ModelMapper mapper;

    public MapperBbb(RepoAaa repoAaa, ModelMapper mapper) {
        this.repoAaa = repoAaa;
        this.mapper = mapper;
    }

//    @Override
    public Bbb toEntity(DtoBbb dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, Bbb.class);
    }

//    @Override
    public DtoBbb toDto(Bbb entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, DtoBbb.class);
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(Bbb.class, DtoBbb.class)
                .addMappings(m -> m.skip(DtoBbb::setAaaId)).setPostConverter(toDtoConverter());
        mapper.createTypeMap(DtoBbb.class, Bbb.class)
                .addMappings(m -> m.skip(Bbb::setAaa)).setPostConverter(toEntityConverter());
    }

    public Converter<DtoBbb, Bbb> toEntityConverter() {
        return context -> {
            DtoBbb source = context.getSource();
            Bbb destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }
    public Converter<Bbb, DtoBbb> toDtoConverter() {
        return context -> {
            Bbb source = context.getSource();
            DtoBbb destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    private void mapSpecificFields(DtoBbb source, Bbb destination) {
        destination.setAaa(repoAaa.findById(source.getAaaId()).orElse(null));
    }
    private void mapSpecificFields(Bbb source, DtoBbb destination) {
        destination.setAaaId(Objects.isNull(source) || Objects.isNull(source.getId()) ? null : source.getAaa().getId());
    }
}
