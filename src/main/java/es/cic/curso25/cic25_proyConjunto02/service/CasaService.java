package es.cic.curso25.cic25_proyConjunto02.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.cic.curso25.cic25_proyConjunto02.model.Casa;
import es.cic.curso25.cic25_proyConjunto02.repository.CasaRepository;

@Service
public class CasaService {

    @Autowired
    private CasaRepository casaRepository;

    public Casa crear(Casa casa){
        return casaRepository.save(casa);
    }
}
