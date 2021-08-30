package ru.mirouqe.playground.dto.mapper.play.one;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoAaa {
    Integer id;
    String name;
    List<DtoBbb> bbbs;
}
