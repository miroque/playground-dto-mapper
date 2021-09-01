package ru.mirouqe.playground.dto.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.spi.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.mirouqe.playground.dto.mapper.play.one.*;

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

    @Autowired
    MapperAaa mapperAaa;

    @Autowired
    ModelMapper mm;

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
    void testInitA() {
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

//    @Disabled
    @Test
    @DisplayName("Проверяю взять через сервис А")
    @Order(2)
    void testSendJsonA() throws JsonProcessingException {
        var a = serviceAaa.get(1);
//        new ObjectMapper().readTree(answer.getBody()).toPrettyString()
        log.info("\ndto A:\n{}", jsonObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(a));
    }

    @Test
    @DisplayName("Читаю JSON А в объект")
    @Order(3)
    void testReadJsonA() throws JsonProcessingException {
        String  dtoString = "{\n" +
                "  \"id\" : 1,\n" +
                "  \"name\" : \"name of A\",\n" +
                "  \"bbbs\" : [ {\n" +
                "    \"id\" : 2,\n" +
                "    \"name\" : \"I'm a B!\",\n" +
                "    \"aaaId\" : 1\n" +
                "  }, {\n" +
                "    \"id\" : 3,\n" +
                "    \"name\" : \"I'm a B - too!\",\n" +
                "    \"aaaId\" : 1\n" +
                "  } ]\n" +
                "}";
        DtoAaa dtoAaa = jsonObjectMapper.readValue(dtoString, DtoAaa.class);
        log.info("\ndto A:\n{}",dtoAaa);
        Aaa aaa = mapperAaa.toEntity(dtoAaa);
        log.info("\nA:\n{}", aaa);
//        new ObjectMapper().readTree(answer.getBody()).toPrettyString()
//        log.info("\ndto A:\n{}", jsonObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(a));
    }

    @Disabled
    @Test
    @DisplayName("Сохранить новый объект с зависимостями новыми")
    @Order(4)
    void testGoDeeper() throws JsonProcessingException {
        String  dtoString = "{\n" +
                "  \"id\" : null,\n" +
                "  \"name\" : \"name of A\",\n" +
                "  \"bbbs\" : [ {\n" +
                "    \"id\" : null,\n" +
                "    \"name\" : \"I'm a B!\",\n" +
                "    \"aaaId\" : null\n" +
                "  }, {\n" +
                "    \"id\" : null,\n" +
                "    \"name\" : \"I'm a B - too!\",\n" +
                "    \"aaaId\" : null\n" +
                "  } ]\n" +
                "}";
        DtoAaa dtoAaa = jsonObjectMapper.readValue(dtoString, DtoAaa.class);
        log.info("\ndto A:\n{}",dtoAaa);
        Aaa aaa = mapperAaa.toEntity(dtoAaa);
        log.info("\nA:\n{}", aaa);
        aaa = repoAaa.save(aaa);
        log.info("\nafter save A:\n{}", aaa);
        Aaa aaa2 = repoAaa.findById(1).get();
        log.info("\nfind saved A2:\n{}", aaa2);

    }
}
