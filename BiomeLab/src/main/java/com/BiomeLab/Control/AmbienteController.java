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
import com.BiomeLab.Repository.AmbienteRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/ambiente")
public class AmbienteController {

    @Autowired
    private AmbienteRepository repAmbiente;

    @GetMapping(value = "/todos")
    public ResponseEntity<List<Ambiente>> retornarTodosAmbientes() {

        List<Ambiente> ambientes = repAmbiente.findAll();

        return ResponseEntity.ok(ambientes);
    }

    @GetMapping(value = "/{idAmbiente}")
    public ResponseEntity<Ambiente> retornarAmbientePorId(@PathVariable Long idAmbiente) {

        Optional<Ambiente> op = repAmbiente.findById(idAmbiente);

        if (op.isPresent()) {
            return ResponseEntity.ok(op.get());
        }

        return ResponseEntity.notFound().build();
    }
    
    @GetMapping(value = "/{idUsuario}/ambientes")
    public ResponseEntity<List<Ambiente>> retornarAmbientePrivados(@PathVariable Long idUsuario){
    	
    	List<Ambiente> ambientes = repAmbiente.listarAmbientesPrivadosPorUsuario(idUsuario);
    	
    	return ResponseEntity.ok(ambientes);
    }

    @PostMapping(value = "/criar-ambiente")
    public ResponseEntity<Void> criarAmbiente(@RequestBody @Valid Ambiente ambiente) {

        repAmbiente.save(ambiente);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(value = "/editar-ambiente/{idAmbiente}")
    public ResponseEntity<Void> editarAmbiente(
            @PathVariable Long idAmbiente,
            @RequestBody @Valid Ambiente ambienteAtualizado) {

        Optional<Ambiente> op = repAmbiente.findById(idAmbiente);

        if (op.isPresent()) {

            Ambiente ambiente = op.get();

            ambiente.transferirAmbiente(ambienteAtualizado);

            repAmbiente.save(ambiente);

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping(value = "/remover-ambiente/{idAmbiente}")
    public ResponseEntity<Void> removerAmbiente(@PathVariable Long idAmbiente) {

        Optional<Ambiente> op = repAmbiente.findById(idAmbiente);

        if (op.isPresent()) {

            repAmbiente.deleteById(idAmbiente);

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

}