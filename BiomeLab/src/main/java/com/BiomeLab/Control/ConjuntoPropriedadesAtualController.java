package com.BiomeLab.Control;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BiomeLab.Model.Ambiente;
import com.BiomeLab.Model.ConjuntoPropriedadesAtual;
import com.BiomeLab.Model.StatusAtivoEnum;
import com.BiomeLab.Repository.AmbienteRepository;
import com.BiomeLab.Repository.ConjuntoPropriedadesAtualRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/conjunto-propriedades-atual")
public class ConjuntoPropriedadesAtualController {

    @Autowired
    private ConjuntoPropriedadesAtualRepository repConjuntoPropriedadesAtual;
    
    @Autowired
    private AmbienteRepository repAmbiente;

    @GetMapping(value = "/todos")
    public ResponseEntity<List<ConjuntoPropriedadesAtual>> retornarTodos() {

        List<ConjuntoPropriedadesAtual> conjuntos =
                repConjuntoPropriedadesAtual.findAll();

        return ResponseEntity.ok(conjuntos);
    }

    
    @GetMapping(value = "/{idConjunto}")
    public ResponseEntity<ConjuntoPropriedadesAtual> retornarPorId(
            @PathVariable Long idConjunto) {

        Optional<ConjuntoPropriedadesAtual> op =
                repConjuntoPropriedadesAtual.findById(idConjunto);

        if (op.isPresent()) {
            return ResponseEntity.ok(op.get());
        }

        return ResponseEntity.notFound().build();
    }
    
    
    @GetMapping(value = "/ambiente/{idAmbiente}/usuario/{idUsuario}")
    public ResponseEntity<ConjuntoPropriedadesAtual> retornarConjuntoPorAmbienteEUsuario(
    		@PathVariable("idUsuario") Long idUsuario,
    		@PathVariable("idAmbiente") Long idAmbiente
    		){
    	
    	Optional<ConjuntoPropriedadesAtual> op = repConjuntoPropriedadesAtual.retornaPropsAtuaisPorAmbientePorUsuario(idUsuario,idAmbiente);
    	
        if (op.isPresent()) {
        	return ResponseEntity.ok(op.get());
        }
    	return ResponseEntity.notFound().build();
    }
    
    
    @PostMapping(value = "/criar")
    public ResponseEntity<Void> criar(
            @RequestBody @Valid ConjuntoPropriedadesAtual conjunto) {

        repConjuntoPropriedadesAtual.save(conjunto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    
    @PutMapping("/usuario/{idUsuario}/ambiente/{idAmbiente}/conjunto-props-atual/{idConjuntoPropsAtual}")
    public ResponseEntity<Void> editarConjuntoPropriedadesAtual(
            @PathVariable Long idUsuario,
            @PathVariable Long idAmbiente,
            @PathVariable Long idConjuntoPropsAtual,
            @RequestBody @Valid ConjuntoPropriedadesAtual conjuntoAtualizado) {

        // Valida se o ambiente existe e pertence ao usuário
        Optional<Ambiente> op_ambiente = repAmbiente.findById(idAmbiente);
        if (op_ambiente.isEmpty()) return ResponseEntity.notFound().build();
        
        Ambiente ambiente = op_ambiente.get();
        if (!ambiente.getUsuario().getIdUsuario().equals(idUsuario)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Valida se o ambiente está ativo
        if (ambiente.getStatusAtivo() != StatusAtivoEnum.ATIVO) {
            return ResponseEntity.badRequest().build();
        }

        // Busca o conjunto de propriedades
        Optional<ConjuntoPropriedadesAtual> op_conjunto = repConjuntoPropriedadesAtual.findById(idConjuntoPropsAtual);
        if (op_conjunto.isEmpty()) return ResponseEntity.notFound().build();

        ConjuntoPropriedadesAtual conjunto = op_conjunto.get();

        conjunto.transferirConjuntoPropriedadesAtual(conjuntoAtualizado);
        repConjuntoPropriedadesAtual.save(conjunto);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/remover/{idConjunto}")
    public ResponseEntity<Void> remover(@PathVariable Long idConjunto) {

        Optional<ConjuntoPropriedadesAtual> op =
                repConjuntoPropriedadesAtual.findById(idConjunto);

        if (op.isPresent()) {

            repConjuntoPropriedadesAtual.deleteById(idConjunto);

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

}