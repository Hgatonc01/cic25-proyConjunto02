package es.cic.curso25.cic25_proyConjunto02.uc;

import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.cic.curso25.cic25_proyConjunto02.model.Perro;
import es.cic.curso25.cic25_proyConjunto02.model.Persona;

@SpringBootTest
@AutoConfigureMockMvc
public class SeHacenAmigosPerroPersonaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testEstablecerAmistad() throws Exception {
        /**
         * Creo primero una persona
         */

        Persona persona = new Persona();
        persona.setDni("12345678a");
        persona.setNombre("Javier");
        persona.setApellidos("Martínez Samperio");
        persona.setEdad(30);

        Perro perroTest = new Perro();
        perroTest.setNombre("Firulais");
        perroTest.setPeso(10);
        perroTest.setRaza("Galgo");

        //convertimos el objeto de tipo persona en json con ObjectMapper
        String personaJson = objectMapper.writeValueAsString(persona);
        //con MockMvc simulamos la peticion HTTP para crear una persona
        mockMvc.perform(post("/persona")
        .contentType("application/json")
        .content(personaJson))
        .andExpect(status().isOk())
        .andExpect( personaResult ->{
            assertTrue(
                objectMapper.readValue(
                    personaResult.getResponse().getContentAsString(), Persona.class).getId()
                    > 0, "Si el id es 0 significa que no se ha creado el registro");
            });

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

}
