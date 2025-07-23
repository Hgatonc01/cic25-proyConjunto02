package es.cic.curso25.cic25_proyConjunto02.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.List;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.core.type.TypeReference;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.cic.curso25.cic25_proyConjunto02.model.Perro;

@SpringBootTest
@AutoConfigureMockMvc
public class PerroControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreate() throws Exception {

        Perro perroTest = new Perro();
        perroTest.setNombre("Firulais");
        perroTest.setPeso(10);
        perroTest.setRaza("Galgo");

        String perroJson = objectMapper.writeValueAsString(perroTest);

        mockMvc.perform(post("/perro")
                .contentType("application/json")
                .content(perroJson))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String respuesta = result.getResponse().getContentAsString();
                    Perro perroCreado = objectMapper.readValue(respuesta, Perro.class);
                    assertTrue(perroCreado.getId() > 0, "El valor debe ser superior a 0");
                });

    }

    @Test
    void testCreateException() throws Exception {

        Perro perroTest = new Perro();
        perroTest.setId(1L);
        perroTest.setNombre("Firulais");
        perroTest.setPeso(10);
        perroTest.setRaza("Galgo");

        String perroJson = objectMapper.writeValueAsString(perroTest);

        mockMvc.perform(post("/perro")
                .contentType("application/json")
                .content(perroJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDelete() throws Exception  {

        Perro perroTest = new Perro();
        perroTest.setNombre("Firulais");
        perroTest.setPeso(10);
        perroTest.setRaza("Galgo");

        String perroJson = objectMapper.writeValueAsString(perroTest);

        MvcResult mvcResult = mockMvc.perform(post("/perro")
                .contentType("application/json")
                .content(perroJson))
                .andExpect(status().isOk())
                .andReturn();

        Long id = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Perro.class).getId();

        mockMvc.perform(delete("/perro/" + id))
                .andExpect(status().isOk());

    }

    @Test
    void testGet() throws Exception {

        Perro perroTest = new Perro();
        perroTest.setNombre("Firulais");
        perroTest.setPeso(10);
        perroTest.setRaza("Galgo");

        String perroJson = objectMapper.writeValueAsString(perroTest);

        MvcResult mvcResult = mockMvc.perform(post("/perro")
                .contentType("application/json")
                .content(perroJson))
                .andExpect(status().isOk())
                .andReturn();

        Long id = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Perro.class).getId();

        mockMvc.perform(get("/perro/" + id))
                .andDo(print())// Imprime los valores en consola
                .andExpect(status().isOk())
                .andExpect(result -> {
                    assertEquals(objectMapper.readValue(result.getResponse().getContentAsString(), Perro.class).getId(),
                            id);
                });

    }

    @Test
    void testGetLista() throws Exception {

        Perro perroTest1 = new Perro();
        perroTest1.setNombre("Firulais");
        perroTest1.setPeso(20);
        perroTest1.setRaza("Galgo");

        Perro perroTest2 = new Perro();
        perroTest2.setNombre("Croqueta");
        perroTest2.setPeso(15);
        perroTest2.setRaza("Bulldog");

        Perro perroTest3 = new Perro();
        perroTest3.setNombre("Pepe");
        perroTest3.setPeso(6);
        perroTest3.setRaza("Pomerania");

        String perroJson = objectMapper.writeValueAsString(perroTest1);
        mockMvc.perform(post("/perro")
                .contentType("application/json")
                .content(perroJson));

        perroJson = objectMapper.writeValueAsString(perroTest2);
        mockMvc.perform(post("/perro")
                .contentType("application/json")
                .content(perroJson));

        perroJson = objectMapper.writeValueAsString(perroTest3);
        mockMvc.perform(post("/perro")
                .contentType("application/json")
                .content(perroJson));

        mockMvc.perform(get("/perro"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    List<Perro> perros = objectMapper.readValue(response, new TypeReference<List<Perro>>() {
                    });
                    assertTrue(perros.size() >= 3);
                });

    }

    @Test
    void testUpdate() throws Exception {

        Perro perroTest = new Perro();
        perroTest.setNombre("Firulais");
        perroTest.setPeso(10);
        perroTest.setRaza("Galgo");

        String perroJson = objectMapper.writeValueAsString(perroTest);

        MvcResult mvcResult = mockMvc.perform(post("/perro")
                .contentType("application/json")
                .content(perroJson))
                .andExpect(status().isOk())
                .andReturn();

        Long id = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Perro.class).getId();

        perroTest.setId(id);
        perroTest.setPeso(15);

        perroJson = objectMapper.writeValueAsString(perroTest);

        mockMvc.perform(put("/perro")
                .contentType("application/json")
                .content(perroJson))
                .andExpect(status().isOk());

    }

    @Test
    void testUpdateException() throws Exception {

        Perro perroTest = new Perro();
        perroTest.setNombre("Firulais");
        perroTest.setPeso(10);
        perroTest.setRaza("Galgo");

        String perroJson = objectMapper.writeValueAsString(perroTest);

        mockMvc.perform(put("/perro")
                .contentType("application/json")
                .content(perroJson))
                .andExpect(status().isBadRequest());

    }

}
