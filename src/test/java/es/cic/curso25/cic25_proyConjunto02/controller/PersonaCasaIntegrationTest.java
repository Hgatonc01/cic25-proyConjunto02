package es.cic.curso25.cic25_proyConjunto02.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.cic.curso25.cic25_proyConjunto02.model.Casa;
import es.cic.curso25.cic25_proyConjunto02.model.Persona;
import es.cic.curso25.cic25_proyConjunto02.repository.CasaRepository;
import es.cic.curso25.cic25_proyConjunto02.repository.PersonaRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonaCasaIntegrationTest {

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private CasaRepository casaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateConCasa() throws Exception{
        Persona persona = new Persona();
        persona.setDni("12345678a");
        persona.setNombre("Javier");
        persona.setApellidos("Martínez Samperio");
        persona.setEdad(30);


        Casa casaTest = new Casa();
        casaTest.setDireccion("C/ inventada, 21");
        casaTest.setValorCatastral(123000L);

        persona.setCasa(casaTest);
        casaTest.setPersona(persona);
        


        //convertimos el objeto de tipo amistad en json con ObjectMapper
        String personaACrear = objectMapper.writeValueAsString(persona);
        
        
        //con MockMvc simulamos la peticion HTTP para crear una persona
        mockMvc.perform(post("/persona/vive")
        .contentType("application/json")
        .content(personaACrear))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect( personaResult ->{
            assertNotNull(
                objectMapper.readValue(
                    personaResult.getResponse().getContentAsString(), Persona.class), 
                "Ya tengo casa");
            })
        .andExpect(jsonPath("$.casa.direccion").value("C/ inventada, 21"));

    }

    @Test
    void updateConCasaTest() throws Exception{
        //creamos la casa
        Casa casaTest = new Casa();
        casaTest.setDireccion("C/ inventada 2, 11");
        casaTest.setValorCatastral(103000L);
        //creamos la persona
        Persona persona = new Persona();
        persona.setDni("12345678a");
        persona.setNombre("Jose");
        persona.setApellidos("Rodríguez Samperio");
        persona.setEdad(30);
        //seteamos la casa a la persona y la persona a la casa
        persona.setCasa(casaTest);
        casaTest.setPersona(persona);
        //hacemos el registro de la casa y recogemos la persona creada
        Persona personaCreada = personaRepository.save(persona);
        //le cambiamos la direccion a casa
        personaCreada.getCasa().setDireccion("Dirección nueva");
        //convertimos esta persona en json
        String personaCreadaJson = objectMapper.writeValueAsString(personaCreada);
        //simulamos la llamada HTTP
        mockMvc.perform(put("/persona/vive")
            .contentType("application/json")
            .content(personaCreadaJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.casa.direccion").value("Dirección nueva"));
        


    }

    @Test
    void testDeleteCasa() throws Exception{
        //creamos la casa
        Casa casaTest = new Casa();
        casaTest.setDireccion("C/ inventada 3, 13");
        casaTest.setValorCatastral(103000L);
        //creamos la persona
        Persona persona = new Persona();
        persona.setDni("12345678a");
        persona.setNombre("Jose");
        persona.setApellidos("Rodríguez Samperio");
        persona.setEdad(30);
        //seteamos la casa a la persona y la persona a la casa
        persona.setCasa(casaTest);
        casaTest.setPersona(persona);
        //hacemos el registro de la casa y recogemos la persona creada
        Persona personaCreada = personaRepository.save(persona);
        //recogemos el id de la persona y de la casa
        Long idPersona = personaCreada.getId();
        Long idCasa = personaCreada.getCasa().getId();
        mockMvc.perform(delete("/persona/"+ idPersona))
        .andExpect(status().isOk());

        //comprobamos si existe la persona
        assertTrue(personaRepository.findById(idCasa).isEmpty());
        //comprobamos si existe la casa (ya que al haber puesto borrado en cascada suponemos que no puede existir por si sola)
        assertTrue(casaRepository.findById(idCasa).isEmpty());
    }
}
