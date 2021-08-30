package ru.mirouqe.playground.dto.mapper;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import ru.mirouqe.playground.dto.mapper.play.one.*;
import ru.mirouqe.playground.jpa.cascades.play.one.*;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j

@Testcontainers

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

@SpringBootTest
class DtoMapperApplicationTests {

    @Autowired
    RepoAgeo repoAgeo;
    @Autowired
    RepoBaz repoBaz;

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
        Aaa ageo = new Aaa();
        ageo.setName("name of A");
        assertNull(ageo.getId());
        ageo = repoAgeo.save(ageo);
        assertNotNull(ageo.getId());
        assertEquals("name of A", ageo.getName());
    }

    @Test
    @DisplayName("Проверяю добавление еще А")
    @Order(2)
    void testAAddNew() {
        Aaa ageo = new Aaa();
        ageo.setName("name of FOO");
        assertNull(ageo.getId());
        repoAgeo.save(ageo);
        assertEquals(2, repoAgeo.findAll().size());
    }

    @Test
    @DisplayName("Добавление Bbb с Aaa")
    @Order(3)
//    @Transactional
    void testBazAddNew() {
        Bbb baz = new Bbb();
        baz.setName("name of Bbb -01");
        baz.setAgeo(repoAgeo.findById(2).get());

        baz = repoBaz.save(baz);
        Bbb baz2 = new Bbb();
        baz2.setName("name of Bbb -02");
        baz2.setAgeo(repoAgeo.findById(2).get());

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
        baz.setAgeo(repoAgeo.findById(2).get());
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

}
