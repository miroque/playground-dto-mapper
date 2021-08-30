package ru.mirouqe.playground.dto.mapper.play.one;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/aaa")
public class ApiAaa {
    private final ServiceAaa serviceAaa;


    public ApiAaa(ServiceAaa aaa) {
        this.serviceAaa = aaa;
    }

    @GetMapping("/")
    public ResponseEntity<DtoAaa> get() {
        DtoAaa items = serviceAaa.get(1);
        return ResponseEntity.ok(items);
    }
}
