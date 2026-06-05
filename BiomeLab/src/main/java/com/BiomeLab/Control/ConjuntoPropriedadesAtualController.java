package com.BiomeLab.Control;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BiomeLab.Model.Ambiente;
import com.BiomeLab.Model.ConjuntoPropriedadesAtual;
import com.BiomeLab.Model.ConjuntoPropriedadesSnapshot;
import com.BiomeLab.Model.Estudo;
import com.BiomeLab.Model.StatusAtivoEnum;
import com.BiomeLab.Model.Teste;
import com.BiomeLab.Model.Usuario;
import com.BiomeLab.Record.AlterarCondicoesDTO;
import com.BiomeLab.Repository.AmbienteRepository;
import com.BiomeLab.Repository.ConjuntoPropriedadesAtualRepository;
import com.BiomeLab.Repository.ConjuntoPropriedadesSnapshotRepository;
import com.BiomeLab.Repository.EstudoRepository;
import com.BiomeLab.Repository.TesteRepository;
import com.BiomeLab.Security.UsuarioAutenticado;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/conjunto-propriedades-atual")
public class ConjuntoPropriedadesAtualController {

    @Autowired
    private ConjuntoPropriedadesAtualRepository repConjuntoPropriedadesAtual;
    
    @Autowired
    private AmbienteRepository repAmbiente;
    
    @Autowired
    private ConjuntoPropriedadesSnapshotRepository repConjuntoPropriedadesSnapshot;

    @Autowired
    private TesteRepository repTeste;
    
    @Autowired
    private EstudoRepository repEstudo;
    
//    @GetMapping(value = "/todos")
//    public ResponseEntity<List<ConjuntoPropriedadesAtual>> retornarTodos() {
//
//        List<ConjuntoPropriedadesAtual> conjuntos =
//                repConjuntoPropriedadesAtual.findAll();
//
//        return ResponseEntity.ok(conjuntos);
//    }

    
//    @GetMapping(value = "/{idConjunto}")
//    public ResponseEntity<ConjuntoPropriedadesAtual> retornarPorId(
//            @PathVariable Long idConjunto) {
//
//        Optional<ConjuntoPropriedadesAtual> op =
//                repConjuntoPropriedadesAtual.findById(idConjunto);
//
//        if (op.isPresent()) {
//            return ResponseEntity.ok(op.get());
//        }
//
//        return ResponseEntity.notFound().build();
//    }
    
    
    
//    @PostMapping(value = "/criar")
//    public ResponseEntity<Void> criar(
//            @RequestBody @Valid ConjuntoPropriedadesAtual conjunto) {
//
//        repConjuntoPropriedadesAtual.save(conjunto);
//
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }

    
    @Operation(
    	    summary = "Altera as condições do ambiente ativo",
    	    description = """
    	        Encerra o teste atual, atualiza as propriedades do ambiente
    	        e inicia um novo teste com snapshot das novas condições.
    	        O ambiente deve estar ativo e pertencer ao usuário autenticado.
    	        """
    	)
    	@ApiResponses({
    	    @ApiResponse(responseCode = "204", description = "Condições alteradas com sucesso"),
    	    @ApiResponse(responseCode = "400", description = "Ambiente não está ativo"),
    	    @ApiResponse(responseCode = "403", description = "Ambiente não pertence ao usuário"),
    	    @ApiResponse(responseCode = "404", description = "Ambiente, estudo, teste ou propriedades não encontrados")
    	})
    @PostMapping("/ambiente/{idAmbiente}/alterar-condicoes")
    @Transactional
    public ResponseEntity<Void> alterarCondicoes(
            @PathVariable Long idAmbiente,
            @RequestBody @Valid AlterarCondicoesDTO dto) {

        UsuarioAutenticado auth = (UsuarioAutenticado) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

        Usuario usuario = auth.getUsuario();

        // validar ambiente
        Ambiente ambiente = repAmbiente.findById(idAmbiente)
                .orElseThrow();
        
        if (!ambiente.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        if (ambiente.getStatusAtivo() != StatusAtivoEnum.ATIVO) {
            return ResponseEntity.badRequest().build();
        }

        // buscar estudo do ambiente
        Estudo estudo = repEstudo
                .retornaEstudoPorAmbientePorUsuario(
                        usuario.getIdUsuario(),
                        idAmbiente)
                .orElseThrow();

        // buscar teste ativo
        Teste testeAtual = repTeste
                .findByEstudo_IdEstudoAndDataTerminoTesteIsNull(estudo.getIdEstudo())
                .orElseThrow();

        // encerrar teste atual
        testeAtual.setDataTerminoTeste(LocalDate.now());
        repTeste.save(testeAtual);

        // atualizar propriedades atuais
        ConjuntoPropriedadesAtual props =
                repConjuntoPropriedadesAtual
                        .retornaPropsAtuaisPorAmbiente(idAmbiente)
                        .orElseThrow();

        props.setTemperatura(dto.temperatura());
        props.setUmidade(dto.umidade());
        props.setLuminosidade(dto.luminosidade());
        props.setGravidade(dto.gravidade());
        props.setPressaoAtmosferica(dto.pressaoAtmosferica());

        repConjuntoPropriedadesAtual.save(props);

        // criar novo teste
        Teste novoTeste = Teste.builder()
                .nomeTeste(dto.nomeTeste())
                .dataInicioTeste(LocalDate.now())
                .estudo(estudo)
                .build();

        repTeste.save(novoTeste);

        // criar snapshot
        ConjuntoPropriedadesSnapshot snapshot =
                ConjuntoPropriedadesSnapshot.builder()
                        .temperatura(dto.temperatura())
                        .umidade(dto.umidade())
                        .luminosidade(dto.luminosidade())
                        .gravidade(dto.gravidade())
                        .pressaoAtmosferica(dto.pressaoAtmosferica())
                        .teste(novoTeste)
                        .build();

        repConjuntoPropriedadesSnapshot.save(snapshot);

        return ResponseEntity.noContent().build();
    }

//    @DeleteMapping(value = "/remover/{idConjunto}")
//    public ResponseEntity<Void> remover(@PathVariable Long idConjunto) {
//
//        Optional<ConjuntoPropriedadesAtual> op =
//                repConjuntoPropriedadesAtual.findById(idConjunto);
//
//        if (op.isPresent()) {
//
//            repConjuntoPropriedadesAtual.deleteById(idConjunto);
//
//            return ResponseEntity.noContent().build();
//        }
//
//        return ResponseEntity.notFound().build();
//    }

}