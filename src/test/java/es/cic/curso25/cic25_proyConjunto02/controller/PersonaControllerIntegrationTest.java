package es.cic.curso25.cic25_proyConjunto02.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.cic.curso25.cic25_proyConjunto02.model.Persona;
import es.cic.curso25.cic25_proyConjunto02.repository.PersonaRepository;

import com.fasterxml.jackson.core.type.TypeReference;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonaControllerIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonaRepository personaRepository;
    
    @Test
    void testCreate() throws Exception {
        //creamos un objeto de tipo persona
        Persona persona = new Persona();
        persona.setDni("12345678a");
        persona.setNombre("Javier");
        persona.setApellidos("Martínez Samperio");
        persona.setEdad(30);
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
    }

    @Test
    void testCreateException() throws Exception {
        //creamos un objeto de tipo persona con id para que salte la excepción personalizada
        Persona persona = new Persona();
        persona.setId(3L);
        persona.setDni("12345678a");
        persona.setNombre("Javier");
        persona.setApellidos("Martínez Samperio");
        persona.setEdad(30);
        //convertimos el objeto de tipo persona en json con ObjectMapper
        String personaJson = objectMapper.writeValueAsString(persona);
        //con MockMvc simulamos la peticion HTTP para intentar crear la persona y que nos devuelva un badRequest
        mockMvc.perform(post("/persona")
            .contentType("application/json")
            .content(personaJson))
            .andExpect(status().isBadRequest());
        
    } 
        
    @Test
    void testUpdate() throws Exception {
        //creamos un objeto de tipo persona
        Persona persona = new Persona();
        persona.setDni("12345678a");
        persona.setNombre("Jose Javier");
        persona.setApellidos("Martínez Samperio");
        persona.setEdad(30);
        //persistimos la persona con el método del repositorio y recogemos el id
        Long idGenerado = personaRepository.save(persona).getId();
        //Le pasamos el objeto con los datos modificados y utilizando el id para el método de actualizar
        persona.setId(idGenerado);
        persona.setNombre("NoMeLLamoJoseJavier");
        //generamos el string con objectMapper
        String personaJson = objectMapper.writeValueAsString(persona);
        //llamamos al metodo de update
        mockMvc.perform(put("/persona")
            .contentType("application/json")
            .content(personaJson))
            .andDo(print())
            .andExpect(status().isOk());
        //comprobamos que el registro con el idGenerado tiene el mismo nombre que la modificacion que hemos hecho
        mockMvc.perform(get("/persona/" + idGenerado))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(idGenerado)) 
            .andExpect(jsonPath("$.nombre").value("NoMeLLamoJoseJavier"));
    }

    @Test 
    void testUpdateException() throws Exception {
        //creamos la persona sin id para pasarla en la peticion HTTP
        //no hace falta hacer el post porque no va a llegar a hacer luego la petición del put
        Persona persona = new Persona();
        persona.setDni("12345678a");
        persona.setNombre("Jose Javier");
        persona.setApellidos("Martínez Samperio");
        persona.setEdad(30);

        //convertimos el objeto de tipo persona en json con ObjectMapper
        String personaJson = objectMapper.writeValueAsString(persona);
        //hacemos la petición que debería devolevr un badRequest
        mockMvc.perform(put("/persona")
            .contentType("application/json")
            .content(personaJson))
            .andExpect(status().isBadRequest());

    }

    @Test
    void testGet() throws Exception{
        //primero creamos la persona
        Persona persona = new Persona();
        persona.setDni("87654321b");
        persona.setNombre("Jose");
        persona.setApellidos("Ruiz Rodriguez");
        persona.setEdad(31);
        //convertimos el objeto de tipo persona en json con ObjectMapper
        String personaJson = objectMapper.writeValueAsString(persona);
        //simulamos la llamada HTTP y recogemos el id generado
        MvcResult mvcResult = mockMvc.perform(post("/persona")
            .contentType("application/json")
            .content(personaJson))
            .andExpect(status().isOk())
            .andReturn();
        Long idGenerado = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Persona.class).getId();
        //utilizamos ese id para probar la búsqueda
        //por si acaso (aunque es redundante) comprobamos si el id del objeto que nos devuelve es el mismo que el que hemos usado en la búsqueda
        mockMvc.perform(get("/persona/" + idGenerado))
            .andExpect(status().isOk())
            .andExpect( result ->{
                assertEquals(objectMapper.readValue(result.getResponse().getContentAsString(), Persona.class).getId(), idGenerado);
            });
    }

    @Test
    void testGetLista() throws Exception{
        //creamos los objetos de tipo persona y los introducimos en la base de datos mediante el endpoint de crear
        Persona persona1 = new Persona();
        persona1.setDni("18273645b");
        persona1.setNombre("Cristina");
        persona1.setApellidos("Haya Muiños");
        persona1.setEdad(45);

        String personaJson = objectMapper.writeValueAsString(persona1);

        mockMvc.perform(post("/persona")
            .contentType("application/json")
            .content(personaJson));

        Persona persona2 = new Persona();
        persona2.setDni("54637281v");
        persona2.setNombre("Alvaro");
        persona2.setApellidos("Barragán Cortés");
        persona2.setEdad(12);

        personaJson = objectMapper.writeValueAsString(persona2);

        mockMvc.perform(post("/persona")
            .contentType("application/json")
            .content(personaJson));

        Persona persona3 = new Persona();
        persona3.setDni("12387654h");
        persona3.setNombre("María");
        persona3.setApellidos("García Álvarez");
        persona3.setEdad(21);

        personaJson = objectMapper.writeValueAsString(persona3);

        mockMvc.perform(post("/persona")
            .contentType("application/json")
            .content(personaJson));
        //hacemos la peticion del get y recogemos el resultado para en una lista para ver si el tamaño es 3 o mayor
        mockMvc.perform(get("/persona"))
            .andExpect(status().isOk())
            .andExpect(result -> {
                String response = result.getResponse().getContentAsString();
                List<Persona> personas = objectMapper.readValue(response,new TypeReference<List<Persona>>() {}); 

                assertTrue(personas.size() >= 3);
            });

    }

    @Test
    void testDelete() throws Exception {
        //creamos una persona
        Persona persona1 = new Persona();
        persona1.setDni("18273645b");
        persona1.setNombre("Javier");
        persona1.setApellidos("García Magaldi");
        persona1.setEdad(30);
        //creamos el string con el json para el body
        String personaJson = objectMapper.writeValueAsString(persona1);

        //creamos y recogemos con MockMvc un registro
        MvcResult result = mockMvc.perform(post("/persona")
            .contentType("application/json")
            .content(personaJson))
            .andExpect(status().isOk())
            .andReturn();
        //recogemos el id generado para ese registro
        Long idCreado = objectMapper.readValue(result.getResponse().getContentAsString(), Persona.class).getId();
        //comprobamos el borrado de ese registro
        mockMvc.perform(delete("/persona/" + idCreado))
            .andExpect(status().isOk());
    }
}
