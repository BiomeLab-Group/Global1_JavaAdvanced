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

import com.BiomeLab.Model.Teste;
import com.BiomeLab.Repository.TesteRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/teste")
public class TesteController {

    @Autowired
    private TesteRepository repTeste;

    @GetMapping(value = "/todos")
    public ResponseEntity<List<Teste>> retornarTodosTestes() {

        List<Teste> testes = repTeste.findAll();

        return ResponseEntity.ok(testes);
    }

    @GetMapping(value = "/{idTeste}")
    public ResponseEntity<Teste> retornarTestePorId(
            @PathVariable Long idTeste) {

        Optional<Teste> op = repTeste.findById(idTeste);

        if (op.isPresent()) {
            return ResponseEntity.ok(op.get());
        }

        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/estudo/{idEstudo}/ambiente/{idAmbiente}/usuario/{idUsuario}")
    public ResponseEntity<List<Teste>> retornarTestesPorEstudoPorAmbientePorUsuario(
            @PathVariable Long idEstudo,
            @PathVariable Long idAmbiente,
            @PathVariable Long idUsuario
            ){

        List<Teste> testes = repTeste.retornarTestesPorEstudoPorAmbientePorUsuario(idEstudo, idAmbiente, idUsuario);

        if (testes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(testes);
    }
    

    @PostMapping(value = "/criar-teste")
    public ResponseEntity<Void> criarTeste(
            @RequestBody @Valid Teste teste) {

        repTeste.save(teste);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(value = "/editar-teste/{idTeste}")
    public ResponseEntity<Void> editarTeste(
            @PathVariable Long idTeste,
            @RequestBody @Valid Teste testeAtualizado) {

        Optional<Teste> op = repTeste.findById(idTeste);

        if (op.isPresent()) {

            Teste teste = op.get();

            teste.transferirTeste(testeAtualizado);

            repTeste.save(teste);

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping(value = "/remover-teste/{idTeste}")
    public ResponseEntity<Void> removerTeste(
            @PathVariable Long idTeste) {

        Optional<Teste> op = repTeste.findById(idTeste);

        if (op.isPresent()) {

            repTeste.deleteById(idTeste);

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

}