package es.cic.curso25.cic25_proyConjunto02.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.cic.curso25.cic25_proyConjunto02.model.Casa;

//creamos el interfaz solo para comprobar el delete
public interface CasaRepository extends JpaRepository<Casa, Long>{

}
