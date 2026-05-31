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

import com.BiomeLab.Model.ConjuntoPropriedadesSnapshot;
import com.BiomeLab.Repository.ConjuntoPropriedadesSnapshotRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/conjunto-propriedades-snapshot")
public class ConjuntoPropriedadesSnapshotController {

    @Autowired
    private ConjuntoPropriedadesSnapshotRepository repConjuntoPropriedadesSnapshot;

    @GetMapping(value = "/todos")
    public ResponseEntity<List<ConjuntoPropriedadesSnapshot>> retornarTodos() {

        List<ConjuntoPropriedadesSnapshot> conjuntos =
                repConjuntoPropriedadesSnapshot.findAll();

        return ResponseEntity.ok(conjuntos);
    }

    @GetMapping(value = "/{idConjunto}")
    public ResponseEntity<ConjuntoPropriedadesSnapshot> retornarPorId(
            @PathVariable Long idConjunto) {

        Optional<ConjuntoPropriedadesSnapshot> op =
                repConjuntoPropriedadesSnapshot.findById(idConjunto);

        if (op.isPresent()) {
            return ResponseEntity.ok(op.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping(value = "/criar")
    public ResponseEntity<Void> criar(
            @RequestBody @Valid ConjuntoPropriedadesSnapshot conjunto) {

        repConjuntoPropriedadesSnapshot.save(conjunto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(value = "/editar/{idConjunto}")
    public ResponseEntity<Void> editar(
            @PathVariable Long idConjunto,
            @RequestBody @Valid ConjuntoPropriedadesSnapshot conjuntoAtualizado) {

        Optional<ConjuntoPropriedadesSnapshot> op =
                repConjuntoPropriedadesSnapshot.findById(idConjunto);

        if (op.isPresent()) {

            ConjuntoPropriedadesSnapshot conjunto = op.get();

            conjunto.transferirConjuntoPropriedadesSnapshot(conjuntoAtualizado);

            repConjuntoPropriedadesSnapshot.save(conjunto);

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping(value = "/remover/{idConjunto}")
    public ResponseEntity<Void> remover(@PathVariable Long idConjunto) {

        Optional<ConjuntoPropriedadesSnapshot> op =
                repConjuntoPropriedadesSnapshot.findById(idConjunto);

        if (op.isPresent()) {

            repConjuntoPropriedadesSnapshot.deleteById(idConjunto);

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

}