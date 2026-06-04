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
import com.BiomeLab.Model.Estudo;
import com.BiomeLab.Record.EditarEstudoDTO;
import com.BiomeLab.Repository.AmbienteRepository;
import com.BiomeLab.Repository.EstudoRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/estudo")
public class EstudoController {

    @Autowired
    private EstudoRepository repEstudo;
    
    @Autowired
    private AmbienteRepository repAmbiente;

    @GetMapping(value = "/todos")
    public ResponseEntity<List<Estudo>> retornarTodosEstudos() {

        List<Estudo> estudos = repEstudo.findAll();

        return ResponseEntity.ok(estudos);
    }

    @GetMapping(value = "/{idEstudo}")
    public ResponseEntity<Estudo> retornarEstudoPorId(
            @PathVariable Long idEstudo) {

        Optional<Estudo> op = repEstudo.findById(idEstudo);

        if (op.isPresent()) {
            return ResponseEntity.ok(op.get());
        }

        return ResponseEntity.notFound().build();
    }
    
    
    @GetMapping(value = "/ambiente/{idAmbiente}/usuario/{idUsuario}")
    public ResponseEntity<Estudo> retornarEstudoPorAmbienteEUsuario(
    		@PathVariable("idUsuario") Long idUsuario,
    		@PathVariable("idAmbiente") Long idAmbiente
    		){
    	
    	Optional<Estudo> op = repEstudo.retornaEstudoPorAmbientePorUsuario(idUsuario,idAmbiente);
    	
        if (op.isPresent()) {
        	return ResponseEntity.ok(op.get());
        }
    	return ResponseEntity.notFound().build();
    };
    
    
    // Busca o Estudo do Ambiente Ativo
    @GetMapping("/ativo/usuario/{idUsuario}")
    public ResponseEntity<Estudo> retornarEstudoDoAmbienteAtivoPorUsuario(@PathVariable("idUsuario") Long idUsuario){
    	
    	Optional<Estudo> op = repEstudo.buscarEstudoDoAmbienteAtivo(idUsuario);

        if (op.isPresent()) {
            return ResponseEntity.ok(op.get());
        }
        return ResponseEntity.notFound().build();
    }
    
    
    @GetMapping("/estudo/{idEstudo}/ambiente")
    public ResponseEntity<Long> retornarIdAmbientePorEstudo(Long idEstudo){
    	
    	Optional<Estudo> op_estudo = repEstudo.findById(idEstudo);
    	if (op_estudo.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
    	Estudo estudo = op_estudo.get();
    	return ResponseEntity.ok(estudo.getAmbiente().getIdAmbiente());
    }
    

    @PostMapping(value = "/criar-estudo")
    public ResponseEntity<Void> criarEstudo(
            @RequestBody @Valid Estudo estudo) {

        repEstudo.save(estudo);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    
    @PutMapping("/usuario/{idUsuario}/ambiente/{idAmbiente}/estudo/{idEstudo}")
    public ResponseEntity<Void> editarEstudo(
            @PathVariable Long idUsuario,
            @PathVariable Long idAmbiente,
            @PathVariable Long idEstudo,
            @RequestBody @Valid EditarEstudoDTO estudoDTO) {

        // Valida se o ambiente existe e pertence ao usuário
        Optional<Ambiente> op_ambiente = repAmbiente.findById(idAmbiente);
        if (op_ambiente.isEmpty()) return ResponseEntity.notFound().build();

        Ambiente ambiente = op_ambiente.get();
        if (!ambiente.getUsuario().getIdUsuario().equals(idUsuario)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Busca o estudo
        Optional<Estudo> op_estudo = repEstudo.findById(idEstudo);
        if (op_estudo.isEmpty()) return ResponseEntity.notFound().build();

        Estudo estudo = op_estudo.get();
        estudo.setNomeEstudo(estudoDTO.nomeEstudo());
        estudo.setDescricaoEstudo(estudoDTO.descricaoEstudo());

        repEstudo.save(estudo);

        return ResponseEntity.noContent().build();
    }

    
    @DeleteMapping(value = "/remover-estudo/{idEstudo}")
    public ResponseEntity<Void> removerEstudo(
            @PathVariable Long idEstudo) {

        Optional<Estudo> op = repEstudo.findById(idEstudo);

        if (op.isPresent()) {

            repEstudo.deleteById(idEstudo);

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

}