package es.cic.curso25.cic25_proyConjunto02.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.cic.curso25.cic25_proyConjunto02.model.Persona;
import es.cic.curso25.cic25_proyConjunto02.service.PersonaService;

@RestController
@RequestMapping("/persona")
public class PersonaController {

     private static final Logger LOGGER = LoggerFactory.getLogger(PersonaController.class);

    @Autowired
    private PersonaService personaService;

    @GetMapping("/{id}")
    public Optional<Persona> get(@PathVariable Long id) {
        LOGGER.info(String.format("Buscando la persona con id %d", id));
        return personaService.get(id);
    }

    @GetMapping
    public List<Persona> get() {
        LOGGER.info("Buscando todas las personas");
        return personaService.get();
    }

    @PostMapping
    public Persona create(@RequestBody Persona persona){
        if(persona.getId() != null){
            throw new ModificacionSecurityException("Intento de modificación en el create");
        }
        LOGGER.info("Creando una persona");
        return personaService.create(persona);
    }

    @PutMapping
    public Persona update(@RequestBody Persona persona){
        LOGGER.info(String.format("Actualizando la perosna con id %d",persona.getId()));
        if(persona.getId() == null) {
            throw new CreacionSecurityException("Intendo de creación en la modificación");
        }
        return personaService.update(persona);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        personaService.delete(id);
    }

    @PostMapping("/vive")
    public Persona createConCasa(@RequestBody Persona persona){
        Persona personaCreada = personaService.create(persona);
        return personaCreada;
    }
}
