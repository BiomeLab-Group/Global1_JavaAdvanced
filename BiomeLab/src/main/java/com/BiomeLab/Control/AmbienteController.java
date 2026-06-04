package com.BiomeLab.Control;

import java.time.LocalDate;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    
    // usado para apenas teste
    @Operation(
    	    summary = "Retorna todos os ambientes",
    	    description = "Endpoint auxiliar utilizado apenas para testes e validações internas.",
    	    tags = {"Admin/Testes"}
    	)
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
    	        """,
    	    tags = {"Ambiente"}
    	)
    	@ApiResponses(value = {
    	    @ApiResponse(responseCode = "200", description = "Ambiente encontrado"),
    	    @ApiResponse(responseCode = "403", description = "O ambiente privado não pertence ao usuário informado"),
    	    @ApiResponse(responseCode = "404", description = "Usuário ou ambiente não encontrado")
    	})
    @GetMapping(value = "/usuario/{idUsuario}/ambiente/{idAmbiente}")
    public ResponseEntity<Ambiente> retornarAmbientePorId(
    		@Parameter(description = "Identificador do usuário", example = "1") @PathVariable Long idUsuario,
    		@Parameter(description = "Identificador do ambiente", example = "5") @PathVariable Long idAmbiente) {

        Optional<Ambiente> op_ambiente = repAmbiente.findById(idAmbiente);

        if (op_ambiente.isEmpty()) { 
        	return ResponseEntity.notFound().build();
        	// Ambiente não encontrado 
        }
        Ambiente ambiente = op_ambiente.get();
        
        
        Optional<Usuario> op_usuario = repUsuario.findById(idUsuario);
        if(op_usuario.isEmpty()) {
        	return ResponseEntity.notFound().build();
        	// Usuario não encontrado
        }
        Usuario usuario = op_usuario.get();
        
        if (ambiente.getVisibilidade() == VisibilidadeEnum.R) {
        	
        	if (!ambiente.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                //Ambiente não pertence ao usuario
            }
        	
		}else if(ambiente.getVisibilidade() == VisibilidadeEnum.P) {
			
			
		}


        return ResponseEntity.ok(ambiente);
    }
    
    
    // No filtro de Ambientes Privados
    @Operation(
    	    summary = "Pesquisa ambientes privados do usuário",
    	    description = """
    	        Retorna a lista de ambientes privados pertencentes ao usuário.
    	        
    	        É possível informar um texto para filtrar os ambientes pelo nome.
    	        Utilizado pela tela de listagem de ambientes do aplicativo.
    	        """,
    	    tags = {"Mobile API"}
    	)
    	@ApiResponses(value = {
    	    @ApiResponse(responseCode = "200", description = "Lista de ambientes retornada com sucesso")
    	})
    @GetMapping(value = "/usuario/{idUsuario}/ambientes/pesquisa")
    public ResponseEntity<List<Ambiente>> retornarAmbientePrivadosPorUsuario(
    		@RequestParam(name = "substring",required = false,defaultValue = "") String substring,
    		@PathVariable Long idUsuario
    		)
    {
    	
    	List<Ambiente> ambientes = repAmbiente.buscarAmbientesPrivadosPorSubstring(substring,idUsuario);
    	
    	return ResponseEntity.ok(ambientes);
    }
    
    
    // Busca o ambiente ativo - HOME
    @Operation(
    	    summary = "Retorna o ambiente ativo do usuário",
    	    description = "Utilizado pela tela Home do aplicativo",
    	    tags = {"Mobile API"}
    	)
    @ApiResponses(value = {
    	    @ApiResponse(responseCode = "200", description = "Ambiente ativo encontrado"),
    	    @ApiResponse(responseCode = "404", description = "Usuário não possui ambiente ativo")
    	})
    @GetMapping("/ativo/usuario/{idUsuario}")
    public ResponseEntity<Ambiente> buscarAmbienteAtivo(
            @Parameter(description = "Identificador do usuário",example = "1") @PathVariable Long idUsuario) {

        Optional<Ambiente> op = repAmbiente.buscarAmbienteAtivoHome(idUsuario);

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
    	        """,
    	    tags = {"Mobile API"}
    	)
    	@ApiResponses(value = {
    	    @ApiResponse(responseCode = "200", description = "Ambiente ativado com sucesso"),
    	    @ApiResponse(responseCode = "404", description = "Ambiente não encontrado ou não pertence ao usuário")
    	})
    @Transactional
    @PutMapping("/usuario/{idUsuario}/ambiente/{idAmbiente}/ativarAmbiente")
    public ResponseEntity<Void> ativarAmbiente(
            @PathVariable Long idUsuario,
            @PathVariable Long idAmbiente){
    	
    	Optional<Ambiente> op = repAmbiente.retornaAmbienteAtivo(idUsuario);
    	
    	if(op.isPresent()) {
    		Ambiente ambienteInativado = op.get();
    	
    		ambienteInativado.setStatusAtivo(StatusAtivoEnum.INATIVO);
    		repAmbiente.save(ambienteInativado);
    	}
    	
    	//---------- ATIVAR NOVO AMBIENTE
    	
    	Optional<Ambiente> op2 =
    	        repAmbiente.ativarAmbiente(idUsuario, idAmbiente);
    	
    	//ATIVANDO O NOVO
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
    	        """,
    	    tags = {"Mobile API"}
    	)
    	@ApiResponses(value = {
    	    @ApiResponse(responseCode = "200", description = "Ambiente desativado com sucesso"),
    	    @ApiResponse(responseCode = "404", description = "Ambiente ativo não encontrado ou não pertence ao usuário")
    	})
    @Transactional
    @PutMapping(value = "/usuario/{idUsuario}/ambiente/{idAmbiente}/desativarAmbiente")
    public ResponseEntity<Void> desativarAmbienteAtivo(
            @PathVariable Long idUsuario,
            @PathVariable Long idAmbiente){

        Optional<Ambiente> op =
                repAmbiente.buscarAmbienteAtivo(idUsuario,idAmbiente);

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
    	        """,
    	    tags = {"Mobile API"}
    	)
    	@ApiResponses(value = {
    	    @ApiResponse(responseCode = "201", description = "Ambiente criado com sucesso"),
    	    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    	})
    @Transactional
    @PostMapping("/usuario/{idUsuario}/criar-ambiente")
    public ResponseEntity<Long> criarAmbienteValido(
    		@Parameter(description = "Identificador do usuário proprietário do ambiente",example = "1") @PathVariable Long idUsuario,
            @RequestBody @Valid CriarAmbienteDTO ambienteDTO) {
    	
    	Optional<Usuario> op_usuario = repUsuario.findById(idUsuario);
    	
    	if (op_usuario.isEmpty()) {
    		return ResponseEntity.notFound().build();    	
    	}
    	Usuario usuario = op_usuario.get();

    	//-------------- CRIAÇÃO/DEFINIÇÃO DOS CAMPOS DE AMBIENTE
    	Ambiente ambiente = Ambiente.builder()
    			.nomeAmbiente(ambienteDTO.nomeAmbiente())
    			.statusAtivo(StatusAtivoEnum.INATIVO)
    			.visibilidade(VisibilidadeEnum.R)
    			.usuario(usuario) // problema aqui, vou ter que receber de alguma forma o id do usuario para transofrmalo em um objeto
    			.build();

        Ambiente ambienteSalvo = repAmbiente.save(ambiente);
        
    	//-------------- CRIAÇÃO/DEFINIÇÃO DOS CAMPOS DE PROPRIEDADES DO AMBIENTE
        ConjuntoPropriedadesAtual conjuntoPropriedadesAtual = ConjuntoPropriedadesAtual.builder()
        		.temperatura(ambienteDTO.temperatura())
        		.umidade(ambienteDTO.umidade())
        		.luminosidade(ambienteDTO.luminosidade())
        		.gravidade(ambienteDTO.gravidade())
        		.pressaoAtmosferica(ambienteDTO.pressaoAtmosferica())
        		.ambiente(ambienteSalvo)
        		.build();
        
        repConjuntoPropsAtual.save(conjuntoPropriedadesAtual);
        
    	//-------------- CRIAÇÃO/DEFINIÇÃO DOS CAMPOS DE ESTUDO
        
        Estudo estudoSalvo = Estudo.builder()
        		.nomeEstudo("Estudo de "+ambienteSalvo.getNomeAmbiente())
        		.descricaoEstudo(null)
        		.ambiente(ambienteSalvo)
        		.build();
        
        repEstudo.save(estudoSalvo);
        
    	//-------------- CRIAÇÃO/DEFINIÇÃO DOS CAMPOS DE TESTE
        
        Teste testeSalvo = Teste.builder()
        		.nomeTeste("1° Teste - "+ambienteSalvo.getNomeAmbiente())
        		.dataInicioTeste(LocalDate.now())
        		.dataTerminoTeste(null)
        		.observacoesGerais(null)
        		.conclusao(null)
        		.estudo(estudoSalvo)
        		.build();
        
        repTeste.save(testeSalvo);
    	//-------------- CRIAÇÃO/DEFINIÇÃO DOS CAMPOS DE PROPRIEDADES_SNAPSHOT COPIADAS
        
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
    	        """,
    	    tags = {"Mobile API"}
    	)
    	@ApiResponses(value = {
    	    @ApiResponse(responseCode = "201", description = "Ambiente público copiado com sucesso"),
    	    @ApiResponse(responseCode = "400", description = "O ambiente informado não é público"),
    	    @ApiResponse(responseCode = "404", description = "Usuário, ambiente ou propriedades do ambiente não encontrados")
    	})
    @Transactional
    @PostMapping("/usuario/{idUsuario}/baixar-ambiente-publico/{idAmbiente}")
    public ResponseEntity<Long> baixarAmbientePublico(
    		@Parameter(description = "Identificador do usuário que receberá a cópia", example = "1") @PathVariable Long idUsuario,
    		@Parameter(description = "Identificador do ambiente público a ser copiado",example = "6") @PathVariable Long idAmbiente) {

        Optional<Ambiente> op_ambiente_pub = repAmbiente.findById(idAmbiente);
        if(op_ambiente_pub.isEmpty()) {
        	return ResponseEntity.notFound().build();
        }

        Ambiente publico = op_ambiente_pub.get();	// Ambiente Publico existe

        if (publico.getVisibilidade() != VisibilidadeEnum.P) {
        	return ResponseEntity.badRequest().build();
        	// MENSAGEM : Você só pode baixar ambientes publicos
        }

        Optional<Usuario> op_usuario = repUsuario.findById(idUsuario);
        if (op_usuario.isEmpty()) {
        	 return ResponseEntity.notFound().build();
		}
        Usuario usuario = op_usuario.get();

        
    	//-------------- CRIAÇÃO/DEFINIÇÃO DOS CAMPOS DE AMBIENTE
        
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

        
    	//-------------- COPIANDO OS CAMPOS DE PROPRIEDADES DO AMBIENTE
        
        ConjuntoPropriedadesAtual propsPrivado = ConjuntoPropriedadesAtual.builder()
                .temperatura(propsPublico.getTemperatura())
                .umidade(propsPublico.getUmidade())
                .luminosidade(propsPublico.getLuminosidade())
                .gravidade(propsPublico.getGravidade())
                .pressaoAtmosferica(propsPublico.getPressaoAtmosferica())
                .ambiente(privadoSalvo)
                .build();
        repConjuntoPropsAtual.save(propsPrivado);
        
        
    	//-------------- CRIAÇÃO/DEFINIÇÃO DOS CAMPOS DE ESTUDO

        Estudo estudoSalvo = Estudo.builder()
                .nomeEstudo("Estudo de " + privadoSalvo.getNomeAmbiente())
                .descricaoEstudo(null)
                .ambiente(privadoSalvo)
                .build();
        repEstudo.save(estudoSalvo);
        
        
    	//-------------- CRIAÇÃO/DEFINIÇÃO DOS CAMPOS DE TESTE

        Teste testeSalvo = Teste.builder()
                .nomeTeste("1° Teste - " + privadoSalvo.getNomeAmbiente())
                .dataInicioTeste(LocalDate.now())
                .estudo(estudoSalvo)
                .build();
        repTeste.save(testeSalvo);

        
    	//-------------- CRIAÇÃO/DEFINIÇÃO DOS CAMPOS DE PROPRIEDADES_SNAPSHOT COPIADAS
        
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
    	        """,
    	    tags = {"Mobile API"}
    	)
    	@ApiResponses(value = {
    	    @ApiResponse(responseCode = "204", description = "Nome do ambiente atualizado com sucesso"),
    	    @ApiResponse(responseCode = "400", description = "O ambiente não é privado ou não está ativo"),
    	    @ApiResponse(responseCode = "403", description = "O ambiente não pertence ao usuário"),
    	    @ApiResponse(responseCode = "404", description = "Ambiente não encontrado")
    	})
    @PutMapping(value = "/usuario/{idUsuario}/editar-ambiente/{idAmbiente}")
    public ResponseEntity<Void> editarAmbiente(
    		 @PathVariable Long idUsuario,
            @PathVariable Long idAmbiente,
            @RequestBody @Valid EditarAmbienteDTO ambienteAtualizadoDto) {

        Optional<Ambiente> op_ambiente_pri = repAmbiente.findById(idAmbiente);
        if (op_ambiente_pri.isEmpty()) {
        	return ResponseEntity.notFound().build();
        	// AMBIENTE PRIVADO NÃO ENCONTRADO
        }
        Ambiente ambiente = op_ambiente_pri.get();
        
        if (!ambiente.getUsuario().getIdUsuario().equals(idUsuario)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            // AMBIENTE NÃO PERTENCE A ESSE USUARIO
        }
        
        if (ambiente.getVisibilidade() != VisibilidadeEnum.R) {
        	return ResponseEntity.badRequest().build();
        	// MENSAGEM : Você só pode editar ambientes privados
        }
        if (ambiente.getStatusAtivo() != StatusAtivoEnum.ATIVO) {
        	return ResponseEntity.badRequest().build();
        	// MENSAGEM : Você só pode editar um ambiente ativo
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
    	        """,
    	    tags = {"Mobile API"}
    	)
    	@ApiResponses(value = {
    	    @ApiResponse(responseCode = "204", description = "Ambiente removido com sucesso"),
    	    @ApiResponse(responseCode = "403", description = "O ambiente não pertence ao usuário"),
    	    @ApiResponse(responseCode = "404", description = "Usuário, ambiente ou estudo não encontrados")
    	})
    @Transactional
    @DeleteMapping(value = "/usuario/{idUsuario}/remover-ambiente/{idAmbiente}")
    public ResponseEntity<Void> removerAmbiente( @PathVariable Long idUsuario,@PathVariable Long idAmbiente) {
    	
    	Optional<Usuario> op_usuario = repUsuario.findById(idUsuario);
        
        if(op_usuario.isEmpty()) {
        	return ResponseEntity.notFound().build();
        	//Usuario não encontrado
        }
        Usuario usuario = op_usuario.get();
    	

    	Long idAmbienteApagar=null;
    	Long idPropsAtualApagar=null;
    	Long idEstudoApagar=null;
    	List<Long> idsTestesApagar = new ArrayList<>();
    	List<Long> idsPropsSnapshotsApagar = new ArrayList<>();
    	
        Optional<Ambiente> op_ambiente = repAmbiente.findById(idAmbiente);
        
        if(op_ambiente.isEmpty()) {
        	return ResponseEntity.notFound().build();
        }
        Ambiente ambiente = op_ambiente.get();  
        idAmbienteApagar = ambiente.getIdAmbiente();

        //---------- Coletar o id do Conjunto de Props. Atual para apagar
        Optional<ConjuntoPropriedadesAtual> op_propsAtual = repConjuntoPropsAtual.retornaPropsAtuaisPorAmbientePorUsuario(usuario.getIdUsuario(),idAmbienteApagar);
        if(op_propsAtual.isEmpty()) {
        	return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        ConjuntoPropriedadesAtual conjuntoPropriedadesAtual = op_propsAtual.get();
        idPropsAtualApagar = conjuntoPropriedadesAtual.getIdConjuntoPropriedadesAtual();
        

      //---------- Coletar o id do estudo para apagar
        Optional<Estudo> op_estudo = repEstudo.retornaEstudoPorAmbientePorUsuario(usuario.getIdUsuario(), idAmbienteApagar);
        if(op_estudo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Estudo estudo = op_estudo.get();
        idEstudoApagar = estudo.getIdEstudo();


        //---------- Coletar o id do teste para apagar
        List<Teste> testes = repTeste.retornarTestesPorEstudoPorAmbientePorUsuario(idEstudoApagar, idAmbienteApagar, idUsuario);


        //---------- Coletar o id do conjunto props snapshot para apagar e deletar em ordem inversa
        for(Teste teste : testes) {
            idsTestesApagar.add(teste.getIdTeste());
            
            Optional<ConjuntoPropriedadesSnapshot> op_snapshot = repSnapshot.retornaPropsSnapshotPorTesteEEstudoEAmbienteEUsuario(
                teste.getIdTeste(), idEstudoApagar, idAmbienteApagar, idUsuario);

            if(op_snapshot.isPresent()) {
                idsPropsSnapshotsApagar.add(op_snapshot.get().getIdConjuntoPropriedadesSnapshot());
            }
        }
        
      //---------- Deletar em ordem inversa
        idsPropsSnapshotsApagar.forEach(id -> repSnapshot.deleteById(id)); //lista
        idsTestesApagar.forEach(id -> repTeste.deleteById(id)); // lista
        repEstudo.deleteById(idEstudoApagar);
        repConjuntoPropsAtual.deleteById(idPropsAtualApagar);
        repAmbiente.deleteById(idAmbienteApagar);

        return ResponseEntity.noContent().build();
    }

} 