package com.BiomeLab.Control;

import java.time.LocalDate;
import java.util.Optional;

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
    	    summary = "Inicia um novo teste com novas condições ambientais",
    	    description = """
    	        Encerra o teste ativo do ambiente,
    	        atualiza as propriedades atuais do ambiente,
    	        cria um novo teste e registra um snapshot
    	        com as condições informadas.

    	        O ambiente deve pertencer ao usuário autenticado
    	        e estar com status ATIVO.
    	        """
    	)
    	@ApiResponses({
    	    @ApiResponse(responseCode = "204", description = "Novo teste iniciado com sucesso"),
    	    @ApiResponse(responseCode = "400", description = "O ambiente não está ativo"),
    	    @ApiResponse(responseCode = "403", description = "O ambiente não pertence ao usuário autenticado"),
    	    @ApiResponse(responseCode = "404", description = "Ambiente, estudo, teste ativo ou conjunto de propriedades não encontrado")
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
        Optional<Ambiente> opAmbiente = repAmbiente.findById(idAmbiente);

        if(opAmbiente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Ambiente ambiente = opAmbiente.get();
        
        if (!ambiente.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        if (ambiente.getStatusAtivo() != StatusAtivoEnum.ATIVO) {
            return ResponseEntity.badRequest().build();
        }

     // buscar estudo do ambiente
        Optional<Estudo> opEstudo = repEstudo
                .retornaEstudoPorAmbientePorUsuario(
                        usuario.getIdUsuario(),
                        idAmbiente);

        if (opEstudo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Estudo estudo = opEstudo.get();

     // buscar teste ativo
        Optional<Teste> opTeste = repTeste
                .findByEstudo_IdEstudoAndDataTerminoTesteIsNull(estudo.getIdEstudo());

        if (opTeste.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Teste testeAtual = opTeste.get();

        // encerrar teste atual
        testeAtual.setDataTerminoTeste(LocalDate.now());
        repTeste.save(testeAtual);

        // atualizar propriedades atuais
        Optional<ConjuntoPropriedadesAtual> opProps =
                repConjuntoPropriedadesAtual
                        .retornaPropsAtuaisPorAmbiente(idAmbiente);

        if (opProps.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ConjuntoPropriedadesAtual props = opProps.get();

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