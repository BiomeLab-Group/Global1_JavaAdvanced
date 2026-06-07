package com.BiomeLab.Control;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BiomeLab.Model.Localidade;
import com.BiomeLab.Repository.LocalidadeRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/localidade")
@Tag(
		name = "Localidade",
	    description = "Operações relacionadas as localidades de ambientes públicos"
	    )
public class LocalidadeController {

    @Autowired
    private LocalidadeRepository repLocalidade;

    @Operation(
            summary = "Retorna todas as localidades",
            description = """
                Retorna a lista completa de localidades cadastradas.

                As localidades são utilizadas para classificação
                geográfica dos ambientes do sistema.
                """
        )
        @ApiResponses({
            @ApiResponse(
                responseCode = "200",
                description = "Lista de localidades retornada com sucesso"
            )
        })
    @GetMapping(value = "/todos")
    public ResponseEntity<List<Localidade>> retornarTodasLocalidades() {

        List<Localidade> localidades = repLocalidade.findAll();

        return ResponseEntity.ok(localidades);
    }

//    @GetMapping(value = "/{idLocalidade}")
//    public ResponseEntity<Localidade> retornarLocalidadePorId(
//            @PathVariable Long idLocalidade) {
//
//        Optional<Localidade> op = repLocalidade.findById(idLocalidade);
//
//        if (op.isPresent()) {
//            return ResponseEntity.ok(op.get());
//        }
//
//        return ResponseEntity.notFound().build();
//    }

//    @PostMapping(value = "/criar-localidade")
//    public ResponseEntity<Void> criarLocalidade(
//            @RequestBody @Valid Localidade localidade) {
//
//        repLocalidade.save(localidade);
//
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }

//    @PutMapping(value = "/editar-localidade/{idLocalidade}")
//    public ResponseEntity<Void> editarLocalidade(
//            @PathVariable Long idLocalidade,
//            @RequestBody @Valid Localidade localidadeAtualizada) {
//
//        Optional<Localidade> op = repLocalidade.findById(idLocalidade);
//
//        if (op.isPresent()) {
//
//            Localidade localidade = op.get();
//
//            localidade.transferirLocalidade(localidadeAtualizada);
//
//            repLocalidade.save(localidade);
//
//            return ResponseEntity.noContent().build();
//        }
//
//        return ResponseEntity.notFound().build();
//    }

//    @DeleteMapping(value = "/remover-localidade/{idLocalidade}")
//    public ResponseEntity<Void> removerLocalidade(
//            @PathVariable Long idLocalidade) {
//
//        Optional<Localidade> op = repLocalidade.findById(idLocalidade);
//
//        if (op.isPresent()) {
//
//            repLocalidade.deleteById(idLocalidade);
//
//            return ResponseEntity.noContent().build();
//        }
//
//        return ResponseEntity.notFound().build();
//    }
}
