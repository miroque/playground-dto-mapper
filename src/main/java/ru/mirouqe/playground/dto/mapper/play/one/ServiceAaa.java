package ru.mirouqe.playground.dto.mapper.play.one;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceAaa {
    private final RepoAaa repoAaa;
    private final MapperAaa mapperAaa;

    public ServiceAaa(RepoAaa repoAaa, MapperAaa mapperAaa) {
        this.repoAaa = repoAaa;
        this.mapperAaa = mapperAaa;
    }

//    public List<DtoAaa> getAll() {
//        repoAaa.findAll();
//    }

    public DtoAaa save(DtoAaa dto){
        return mapperAaa.toDto(repoAaa.save(mapperAaa.toEntity(dto)));
    }

    public DtoAaa get(Integer id){
        return mapperAaa.toDto(repoAaa.findById(id).get());
    }
}
