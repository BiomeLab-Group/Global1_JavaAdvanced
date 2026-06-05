package com.BiomeLab.Control;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.BiomeLab.DTO.AmbienteDTO;
import com.BiomeLab.Mapper.AmbienteMapper;
import com.BiomeLab.Model.Ambiente;
import com.BiomeLab.Model.ConjuntoPropriedadesAtual;
import com.BiomeLab.Model.ConjuntoPropriedadesSnapshot;
import com.BiomeLab.Model.Estudo;
import com.BiomeLab.Model.StatusAtivoEnum;
import com.BiomeLab.Model.Teste;
import com.BiomeLab.Model.Usuario;
import com.BiomeLab.Model.VisibilidadeEnum;
import com.BiomeLab.Record.CriarAmbienteDTO;
import com.BiomeLab.Record.EditarAmbienteDTO;
import com.BiomeLab.Repository.AmbienteRepository;
import com.BiomeLab.Repository.ConjuntoPropriedadesAtualRepository;
import com.BiomeLab.Repository.ConjuntoPropriedadesSnapshotRepository;
import com.BiomeLab.Repository.EstudoRepository;
import com.BiomeLab.Repository.TesteRepository;
import com.BiomeLab.Repository.UsuarioRepository;
import com.BiomeLab.Security.UsuarioAutenticado;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;


@Tag(
	    name = "Ambientes",
	    description = "Operações relacionadas aos ambientes"
	)
@RestController
@RequestMapping(value = "/ambiente")
public class AmbienteController {

    @Autowired
    private AmbienteRepository repAmbiente;
    
    @Autowired
    private UsuarioRepository repUsuario;
    
    @Autowired
    private ConjuntoPropriedadesAtualRepository repConjuntoPropsAtual;

    @Autowired
    private EstudoRepository repEstudo;
    
    @Autowired
    private TesteRepository repTeste;
    
    @Autowired
    private ConjuntoPropriedadesSnapshotRepository repSnapshot;
    
    
    @Autowired
    private AmbienteMapper mapper;
    
    // usado para apenas teste
    @Operation(
    	    summary = "Retorna todos os ambientes",
    	    description = "Endpoint auxiliar utilizado apenas para testes e validações internas.")
    	@ApiResponse(
    	    responseCode = "200",
    	    description = "Lista de ambientes retornada com sucesso"
    	)
    @GetMapping(value = "/todos")
    public ResponseEntity<List<Ambiente>> retornarTodosAmbientes() {

        List<Ambiente> ambientes = repAmbiente.findAll();

        return ResponseEntity.ok(ambientes);
    }

    
    // Ficha Ambiente-> exibição dos campos
    @Operation(
    	    summary = "Retorna um ambiente específico",
    	    description = """
    	        Retorna os dados de um ambiente a partir do seu identificador.
    	        
    	        Para ambientes privados, o ambiente deve pertencer ao usuário informado.
    	        Ambientes públicos podem ser consultados livremente.
    	        """
    	)
    	@ApiResponses(value = {
    	    @ApiResponse(responseCode = "200", description = "Ambiente encontrado"),
    	    @ApiResponse(responseCode = "403", description = "O ambiente privado não pertence ao usuário informado"),
    	    @ApiResponse(responseCode = "404", description = "Usuário ou ambiente não encontrado")
    	})
    @Tag(name = "Teste em Cloud")
    @GetMapping("/ambiente/{idAmbiente}")
    public ResponseEntity<AmbienteDTO> retornarAmbientePorId(
            @Parameter(description = "Identificador do ambiente", example = "5")
            @PathVariable Long idAmbiente) {

        Optional<Ambiente> opAmbiente = repAmbiente.findById(idAmbiente);

        if (opAmbiente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Ambiente ambiente = opAmbiente.get();

        UsuarioAutenticado auth = (UsuarioAutenticado) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

        Usuario usuario = auth.getUsuario();

        if (ambiente.getVisibilidade() == VisibilidadeEnum.R) {

            if (!ambiente.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        return ResponseEntity.ok(mapper.toDTO(ambiente));
    }
    
    
    // No filtro de Ambientes Privados
    @Operation(
    	    summary = "Pesquisa ambientes privados do usuário",
    	    description = """
    	        Retorna a lista de ambientes privados pertencentes ao usuário.
    	        
    	        É possível informar um texto para filtrar os ambientes pelo nome.
    	        Utilizado pela tela de listagem de ambientes do aplicativo.
    	        """
    	)
    	@ApiResponses(value = {
    	    @ApiResponse(responseCode = "200", description = "Lista de ambientes retornada com sucesso")
    	})

    @GetMapping("/privados/pesquisa")
    public ResponseEntity<List<Ambiente>> retornarAmbientesPrivados(
            @RequestParam(name = "substring", defaultValue = "") String substring
    ) {

    	UsuarioAutenticado auth = (UsuarioAutenticado) SecurityContextHolder
    	        .getContext().getAuthentication().getPrincipal();

    	Usuario usuario = auth.getUsuario();
    	
        List<Ambiente> ambientes =
                repAmbiente.buscarAmbientesPrivadosPorSubstring(
                        substring,
                        usuario.getIdUsuario()
                );


        return ResponseEntity.ok(ambientes);
    }
    
    @Operation(
    	    summary = "Retorna o conjunto de propriedades atuais do ambiente ativo",
    	    description = "Utilizado na tela Home"
    	)
    	@ApiResponses({
    	    @ApiResponse(responseCode = "200", description = "Conjunto de propriedades encontrado"),
    	    @ApiResponse(responseCode = "404", description = "Nenhum ambiente ativo ou conjunto de propriedades encontrado")
    	})
    	@GetMapping("/ambiente-ativo")
    	public ResponseEntity<ConjuntoPropriedadesAtual> retornarConjuntoPorAmbienteAtivo() {

    	    UsuarioAutenticado auth = (UsuarioAutenticado) SecurityContextHolder
    	            .getContext().getAuthentication().getPrincipal();
    	    Usuario usuario = auth.getUsuario();

    	    Optional<Ambiente> op_ambiente = repAmbiente.buscarAmbienteAtivoHome(usuario.getIdUsuario());
    	    if (op_ambiente.isEmpty()) return ResponseEntity.notFound().build();

    	    Optional<ConjuntoPropriedadesAtual> op_conjunto = repConjuntoPropsAtual
    	            .retornaPropsAtuaisPorAmbiente(op_ambiente.get().getIdAmbiente());

    	    if (op_conjunto.isPresent()) return ResponseEntity.ok(op_conjunto.get());
    	    return ResponseEntity.notFound().build();
    	}
    
    
    
    // Busca o ambiente ativo - HOME
    @Operation(
    	    summary = "Retorna o ambiente ativo do usuário",
    	    description = "Utilizado pela tela Home do aplicativo"
    	)
    @ApiResponses(value = {
    	    @ApiResponse(responseCode = "200", description = "Ambiente ativo encontrado"),
    	    @ApiResponse(responseCode = "404", description = "Usuário não possui ambiente ativo")
    	})
    @GetMapping("/ativo")
    public ResponseEntity<Ambiente> buscarAmbienteAtivo() {

        UsuarioAutenticado auth = (UsuarioAutenticado) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        Usuario usuario = auth.getUsuario();

        Optional<Ambiente> op = repAmbiente.buscarAmbienteAtivoHome(usuario.getIdUsuario());

        if (op.isPresent()) {
            return ResponseEntity.ok(op.get());
        }
        return ResponseEntity.notFound().build();
    }
    
    // NA ficha de ambiente inativo
    @Operation(
    	    summary = "Ativa um ambiente do usuário",
    	    description = """
    	        Define o ambiente informado como ativo.
    	        
    	        Caso o usuário já possua outro ambiente ativo, ele será automaticamente
    	        desativado antes da ativação do novo ambiente.
    	        
    	        Utilizado na ficha de um ambiente inativo.
    	        """
    	)
    	@ApiResponses(value = {
    	    @ApiResponse(responseCode = "200", description = "Ambiente ativado com sucesso"),
    	    @ApiResponse(responseCode = "404", description = "Ambiente não encontrado ou não pertence ao usuário")
    	})@Transactional
    @PutMapping("/{idAmbiente}/ativar")
    public ResponseEntity<Void> ativarAmbiente(@PathVariable Long idAmbiente) {

        UsuarioAutenticado auth = (UsuarioAutenticado) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        Usuario usuario = auth.getUsuario();

        Optional<Ambiente> op = repAmbiente.retornaAmbienteAtivo(usuario.getIdUsuario());

        if(op.isPresent()) {
            Ambiente ambienteInativado = op.get();
            ambienteInativado.setStatusAtivo(StatusAtivoEnum.INATIVO);
            repAmbiente.save(ambienteInativado);
        }

        Optional<Ambiente> op2 = repAmbiente.ativarAmbiente(usuario.getIdUsuario(), idAmbiente);

        if (op2.isPresent()) {
            Ambiente ambiente_novo = op2.get();
            ambiente_novo.setStatusAtivo(StatusAtivoEnum.ATIVO);
            repAmbiente.save(ambiente_novo);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
    
    // na ficha de ambiente ativo
    @Operation(
    	    summary = "Desativa um ambiente ativo",
    	    description = """
    	        Remove o status de ativo do ambiente informado.
    	        
    	        Apenas ambientes que estejam atualmente ativos podem ser desativados.
    	        
    	        Utilizado na ficha de um ambiente ativo.
    	        """
    	)
    	@ApiResponses(value = {
    	    @ApiResponse(responseCode = "200", description = "Ambiente desativado com sucesso"),
    	    @ApiResponse(responseCode = "404", description = "Ambiente ativo não encontrado ou não pertence ao usuário")
    	})
    @Transactional
    @PutMapping("/{idAmbiente}/desativar")
    public ResponseEntity<Void> desativarAmbienteAtivo(@PathVariable Long idAmbiente) {

        UsuarioAutenticado auth = (UsuarioAutenticado) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        Usuario usuario = auth.getUsuario();

        Optional<Ambiente> op = repAmbiente.buscarAmbienteAtivo(usuario.getIdUsuario(), idAmbiente);

        if (op.isPresent()) {
            Ambiente ambiente = op.get();
            ambiente.setStatusAtivo(StatusAtivoEnum.INATIVO);
            repAmbiente.save(ambiente);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    // Na lista de Ambiente -> (+)
    @Operation(
    	    summary = "Cria um novo ambiente privado",
    	    description = """
    	        Cria um novo ambiente privado para o usuário informado.
    	        
    	        Durante a criação são gerados automaticamente:
    	        - O ambiente;
    	        - O conjunto de propriedades atuais;
    	        - O estudo inicial do ambiente;
    	        - O primeiro teste do estudo;
    	        - O snapshot inicial das propriedades.
    	        
    	        O ambiente é criado inicialmente com status INATIVO.
    	        Retorna o identificador do ambiente criado.
    	        """
    	)
    	@ApiResponses(value = {
    	    @ApiResponse(responseCode = "201", description = "Ambiente criado com sucesso"),
    	    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    	})
    @Tag(name = "Teste em Cloud")
    @Transactional
    @PostMapping("/criar-ambiente")
    public ResponseEntity<Long> criarAmbienteValido(@RequestBody @Valid CriarAmbienteDTO ambienteDTO) {

        UsuarioAutenticado auth = (UsuarioAutenticado) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        Usuario usuario = auth.getUsuario();

        Ambiente ambiente = Ambiente.builder()
                .nomeAmbiente(ambienteDTO.nomeAmbiente())
                .statusAtivo(StatusAtivoEnum.INATIVO)
                .visibilidade(VisibilidadeEnum.R)
                .usuario(usuario)
                .build();

        Ambiente ambienteSalvo = repAmbiente.save(ambiente);

        ConjuntoPropriedadesAtual conjuntoPropriedadesAtual = ConjuntoPropriedadesAtual.builder()
                .temperatura(ambienteDTO.temperatura())
                .umidade(ambienteDTO.umidade())
                .luminosidade(ambienteDTO.luminosidade())
                .gravidade(ambienteDTO.gravidade())
                .pressaoAtmosferica(ambienteDTO.pressaoAtmosferica())
                .ambiente(ambienteSalvo)
                .build();

        repConjuntoPropsAtual.save(conjuntoPropriedadesAtual);

        Estudo estudoSalvo = Estudo.builder()
                .nomeEstudo("Estudo de " + ambienteSalvo.getNomeAmbiente())
                .descricaoEstudo(null)
                .ambiente(ambienteSalvo)
                .build();

        repEstudo.save(estudoSalvo);

        Teste testeSalvo = Teste.builder()
                .nomeTeste("1° Teste - " + ambienteSalvo.getNomeAmbiente())
                .dataInicioTeste(LocalDate.now())
                .estudo(estudoSalvo)
                .build();

        repTeste.save(testeSalvo);

        ConjuntoPropriedadesSnapshot conjuntoPropriedadesSnapshot = ConjuntoPropriedadesSnapshot.builder()
                .temperatura(conjuntoPropriedadesAtual.getTemperatura())
                .umidade(conjuntoPropriedadesAtual.getUmidade())
                .luminosidade(conjuntoPropriedadesAtual.getLuminosidade())
                .gravidade(conjuntoPropriedadesAtual.getGravidade())
                .pressaoAtmosferica(conjuntoPropriedadesAtual.getPressaoAtmosferica())
                .teste(testeSalvo)
                .build();

        repSnapshot.save(conjuntoPropriedadesSnapshot);

        return ResponseEntity.status(HttpStatus.CREATED).body(ambienteSalvo.getIdAmbiente());
    }
    
    
    // Ficha Ambiente Público -> BAIXAR AMBIENTE 
    // Retorna o id do ambiente criado 
    @Operation(
    	    summary = "Baixa um ambiente público",
    	    description = """
    	        Cria uma cópia privada de um ambiente público para o usuário informado.
    	        
    	        Durante a cópia são gerados automaticamente:
    	        - Um novo ambiente privado;
    	        - Um conjunto de propriedades atuais com os mesmos valores do ambiente público;
    	        - Um estudo inicial;
    	        - Um primeiro teste;
    	        - Um snapshot inicial das propriedades.
    	        
    	        O ambiente copiado é criado com status INATIVO.
    	        
    	        Retorna o identificador do novo ambiente criado.
    	        """
    	)
    	@ApiResponses(value = {
    	    @ApiResponse(responseCode = "201", description = "Ambiente público copiado com sucesso"),
    	    @ApiResponse(responseCode = "400", description = "O ambiente informado não é público"),
    	    @ApiResponse(responseCode = "404", description = "Usuário, ambiente ou propriedades do ambiente não encontrados")
    	})
    @Transactional
    @PostMapping("/baixar-ambiente-publico/{idAmbiente}")
    public ResponseEntity<Long> baixarAmbientePublico(@PathVariable Long idAmbiente) {

        UsuarioAutenticado auth = (UsuarioAutenticado) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        Usuario usuario = auth.getUsuario();

        Optional<Ambiente> op_ambiente_pub = repAmbiente.findById(idAmbiente);
        if(op_ambiente_pub.isEmpty()) return ResponseEntity.notFound().build();

        Ambiente publico = op_ambiente_pub.get();

        if (publico.getVisibilidade() != VisibilidadeEnum.P) {
            return ResponseEntity.badRequest().build();
        }

        Ambiente privado = Ambiente.builder()
                .nomeAmbiente(publico.getNomeAmbiente() + " (Copiado)")
                .visibilidade(VisibilidadeEnum.R)
                .statusAtivo(StatusAtivoEnum.INATIVO)
                .usuario(usuario)
                .build();
        Ambiente privadoSalvo = repAmbiente.save(privado);

        Optional<ConjuntoPropriedadesAtual> op_props = repConjuntoPropsAtual.retornaPropsAtuaisPorAmbiente(idAmbiente);
        if (op_props.isEmpty()) return ResponseEntity.notFound().build();
        ConjuntoPropriedadesAtual propsPublico = op_props.get();

        ConjuntoPropriedadesAtual propsPrivado = ConjuntoPropriedadesAtual.builder()
                .temperatura(propsPublico.getTemperatura())
                .umidade(propsPublico.getUmidade())
                .luminosidade(propsPublico.getLuminosidade())
                .gravidade(propsPublico.getGravidade())
                .pressaoAtmosferica(propsPublico.getPressaoAtmosferica())
                .ambiente(privadoSalvo)
                .build();
        repConjuntoPropsAtual.save(propsPrivado);

        Estudo estudoSalvo = Estudo.builder()
                .nomeEstudo("Estudo de " + privadoSalvo.getNomeAmbiente())
                .descricaoEstudo(null)
                .ambiente(privadoSalvo)
                .build();
        repEstudo.save(estudoSalvo);

        Teste testeSalvo = Teste.builder()
                .nomeTeste("1° Teste - " + privadoSalvo.getNomeAmbiente())
                .dataInicioTeste(LocalDate.now())
                .estudo(estudoSalvo)
                .build();
        repTeste.save(testeSalvo);

        ConjuntoPropriedadesSnapshot snapshot = ConjuntoPropriedadesSnapshot.builder()
                .temperatura(propsPrivado.getTemperatura())
                .umidade(propsPrivado.getUmidade())
                .luminosidade(propsPrivado.getLuminosidade())
                .gravidade(propsPrivado.getGravidade())
                .pressaoAtmosferica(propsPrivado.getPressaoAtmosferica())
                .teste(testeSalvo)
                .build();
        repSnapshot.save(snapshot);

        return ResponseEntity.status(HttpStatus.CREATED).body(privadoSalvo.getIdAmbiente());
    }
    
    //Ficha Ambiente Privado -> EDITAR NOME
    // Retorna nada 
    @Operation(
    	    summary = "Edita o nome de um ambiente privado",
    	    description = """
    	        Atualiza o nome de um ambiente privado pertencente ao usuário.
    	        
    	        Regras:
    	        - O ambiente deve existir;
    	        - O ambiente deve pertencer ao usuário informado;
    	        - O ambiente deve ser privado;
    	        - O ambiente deve estar ativo.
    	        """
    	)
    	@ApiResponses(value = {
    	    @ApiResponse(responseCode = "204", description = "Nome do ambiente atualizado com sucesso"),
    	    @ApiResponse(responseCode = "400", description = "O ambiente não é privado ou não está ativo"),
    	    @ApiResponse(responseCode = "403", description = "O ambiente não pertence ao usuário"),
    	    @ApiResponse(responseCode = "404", description = "Ambiente não encontrado")
    	})
    @Tag(name="Teste em Cloud")
    @PutMapping("/editar-ambiente/{idAmbiente}")
    public ResponseEntity<Void> editarAmbiente(
            @PathVariable Long idAmbiente,
            @RequestBody @Valid EditarAmbienteDTO ambienteAtualizadoDto) {

        UsuarioAutenticado auth = (UsuarioAutenticado) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        Usuario usuario = auth.getUsuario();

        Optional<Ambiente> op_ambiente = repAmbiente.findById(idAmbiente);
        if (op_ambiente.isEmpty()) return ResponseEntity.notFound().build();

        Ambiente ambiente = op_ambiente.get();

        if (!ambiente.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (ambiente.getVisibilidade() != VisibilidadeEnum.R) {
            return ResponseEntity.badRequest().build();
        }



        ambiente.setNomeAmbiente(ambienteAtualizadoDto.nomeAmbiente());
        repAmbiente.save(ambiente);

        return ResponseEntity.noContent().build();
    }

    
    @Operation(
    	    summary = "Remove um ambiente",
    	    description = """
    	        Remove permanentemente um ambiente pertencente ao usuário.
    	        
    	        Durante a remoção também são excluídos:
    	        - Conjunto de propriedades atual;
    	        - Estudo associado ao ambiente;
    	        - Todos os testes do estudo;
    	        - Todos os snapshots vinculados aos testes.
    	        
    	        A exclusão é realizada em cascata manualmente para preservar a integridade dos dados.
    	        """
    	)
    	@ApiResponses(value = {
    	    @ApiResponse(responseCode = "204", description = "Ambiente removido com sucesso"),
    	    @ApiResponse(responseCode = "403", description = "O ambiente não pertence ao usuário"),
    	    @ApiResponse(responseCode = "404", description = "Usuário, ambiente ou estudo não encontrados")
    	})
    @Tag(name = "Teste em Cloud")
    @Transactional
    @DeleteMapping("/remover-ambiente/{idAmbiente}")
    public ResponseEntity<Void> removerAmbiente(@PathVariable Long idAmbiente) {

        UsuarioAutenticado auth = (UsuarioAutenticado) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        Usuario usuario = auth.getUsuario();

        Long idAmbienteApagar = null;
        Long idPropsAtualApagar = null;
        Long idEstudoApagar = null;
        List<Long> idsTestesApagar = new ArrayList<>();
        List<Long> idsPropsSnapshotsApagar = new ArrayList<>();

        Optional<Ambiente> op_ambiente = repAmbiente.findById(idAmbiente);
        if(op_ambiente.isEmpty()) return ResponseEntity.notFound().build();

        Ambiente ambiente = op_ambiente.get();
        idAmbienteApagar = ambiente.getIdAmbiente();

        Optional<ConjuntoPropriedadesAtual> op_propsAtual = repConjuntoPropsAtual.retornaPropsAtuaisPorAmbientePorUsuario(usuario.getIdUsuario(), idAmbienteApagar);
        if(op_propsAtual.isEmpty()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        ConjuntoPropriedadesAtual conjuntoPropriedadesAtual = op_propsAtual.get();
        idPropsAtualApagar = conjuntoPropriedadesAtual.getIdConjuntoPropriedadesAtual();

        Optional<Estudo> op_estudo = repEstudo.retornaEstudoPorAmbientePorUsuario(usuario.getIdUsuario(), idAmbienteApagar);
        if(op_estudo.isEmpty()) return ResponseEntity.notFound().build();

        Estudo estudo = op_estudo.get();
        idEstudoApagar = estudo.getIdEstudo();

        List<Teste> testes = repTeste.retornarTestesPorEstudoPorAmbientePorUsuario(idEstudoApagar, idAmbienteApagar, usuario.getIdUsuario());

        for(Teste teste : testes) {
            idsTestesApagar.add(teste.getIdTeste());

            Optional<ConjuntoPropriedadesSnapshot> op_snapshot = repSnapshot.retornaPropsSnapshotPorTesteEEstudoEAmbienteEUsuario(
                teste.getIdTeste(), idEstudoApagar, idAmbienteApagar, usuario.getIdUsuario());

            if(op_snapshot.isPresent()) {
                idsPropsSnapshotsApagar.add(op_snapshot.get().getIdConjuntoPropriedadesSnapshot());
            }
        }

        idsPropsSnapshotsApagar.forEach(id -> repSnapshot.deleteById(id));
        idsTestesApagar.forEach(id -> repTeste.deleteById(id));
        repEstudo.deleteById(idEstudoApagar);
        repConjuntoPropsAtual.deleteById(idPropsAtualApagar);
        repAmbiente.deleteById(idAmbienteApagar);

        return ResponseEntity.noContent().build();
    }

} 