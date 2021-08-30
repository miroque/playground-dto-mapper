package ru.mirouqe.playground.dto.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.mirouqe.playground.dto.mapper.play.one.Aaa;
import ru.mirouqe.playground.dto.mapper.play.one.Bbb;
import ru.mirouqe.playground.dto.mapper.play.one.RepoAaa;
import ru.mirouqe.playground.dto.mapper.play.one.ServiceAaa;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j

@Testcontainers

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

@SpringBootTest
class DtoMapperApplicationTests {
    @Autowired
    ObjectMapper jsonObjectMapper;

    @Autowired
    RepoAaa repoAaa;

    @Autowired
    ServiceAaa serviceAaa;
//    @Autowired
//    RepoBaz repoBaz;

    @Container
    public static PostgreSQLContainer container = new PostgreSQLContainer("postgres:12.4-alpine")
            .withUsername("foo_user")
            .withPassword("foo_pwd")
            .withDatabaseName("foo");

    @DynamicPropertySource
    static void properites(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    @Test
    @DisplayName("Проверяю создание первого класса")
    @Order(1)
    void testA() {
        Aaa aaa = new Aaa();
        aaa.setName("name of A");
        assertNull(aaa.getId());
        aaa = repoAaa.save(aaa);
        assertNotNull(aaa.getId());
        assertEquals("name of A", aaa.getName());


        List<Bbb> bbbs = new ArrayList<>();

        Bbb bbb = new Bbb();
        bbb.setName("I'm a B!");
        bbb.setAaa(aaa);

        Bbb bbb2 = new Bbb();
        bbb2.setName("I'm a B - too!");
        bbb2.setAaa(aaa);

        bbbs.add(bbb);
        bbbs.add(bbb2);

        aaa.setBbbs(bbbs);
        repoAaa.save(aaa);
    }


    @Test
    @DisplayName("Проверяю взять через сервис А")
    @Order(2)
    void testAAddNew() throws JsonProcessingException {
        var a = serviceAaa.get(1);
//        new ObjectMapper().readTree(answer.getBody()).toPrettyString()
        log.info("\ndto A:\n{}", jsonObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(a));
    }

    /*
    @Test
    @DisplayName("Добавление Bbb с Aaa")
    @Order(3)
//    @Transactional
    void testBazAddNew() {
        Bbb baz = new Bbb();
        baz.setName("name of Bbb -01");
        baz.setAgeo(repoAaa.findById(2).get());

        baz = repoBaz.save(baz);
        Bbb baz2 = new Bbb();
        baz2.setName("name of Bbb -02");
        baz2.setAgeo(repoAaa.findById(2).get());

        baz2 = repoBaz.save(baz2);
        assertNotNull(baz2.getId());
        repoBaz.findAll().stream().forEach(i -> log.info("{}",i));
    }

    @Test
    @DisplayName("Добавление Bbb с Aaa и Ccc")
    @Order(4)
//    @Transactional
    void testBazAddNewCitr() {
        Bbb baz = new Bbb();
        baz.setName("name of Bbb -03");
        baz.setAgeo(repoAaa.findById(2).get());
        List<Ccc> citrs = new ArrayList<>();
        Ccc citr_01 = new Ccc();
        citr_01.setName("c 01");
        citrs.add(citr_01);
        baz.setCitrs(citrs);

        repoBaz.save(baz);

//        repoBaz.findAll().stream().forEach(i -> log.info("{}",i));
    }

    @Test
    @DisplayName("Обновить Ccc у Bbb")
    @Order(5)
    @Transactional
    void testUpdateCitrFromBaz() {
        Bbb baz = repoBaz.findById(3).get();

        log.warn("{}", baz);

        var citr_01 = baz.getCitrs().get(0);
        log.warn("{}", citr_01);
        citr_01.setName("re *** c 01");
        log.warn("{}", citr_01);

        repoBaz.save(baz);
        log.warn("{}", baz);

        repoBaz.findAll().stream().forEach(i -> log.info("{}",i));
    }
*/

}
