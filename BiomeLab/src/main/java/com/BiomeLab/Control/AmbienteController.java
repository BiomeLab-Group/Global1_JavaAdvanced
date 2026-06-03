package com.BiomeLab.Control;

import java.time.LocalDate;
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
import com.BiomeLab.Repository.AmbienteRepository;
import com.BiomeLab.Repository.ConjuntoPropriedadesAtualRepository;
import com.BiomeLab.Repository.ConjuntoPropriedadesSnapshotRepository;
import com.BiomeLab.Repository.EstudoRepository;
import com.BiomeLab.Repository.TesteRepository;
import com.BiomeLab.Repository.UsuarioRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

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
    @GetMapping(value = "/todos")
    public ResponseEntity<List<Ambiente>> retornarTodosAmbientes() {

        List<Ambiente> ambientes = repAmbiente.findAll();

        return ResponseEntity.ok(ambientes);
    }

    // Ficha Ambiente-> exibição dos campos
    @GetMapping(value = "/usuario/{idUsuario}/ambiente/{idAmbiente}")
    public ResponseEntity<Ambiente> retornarAmbientePorId(
            @PathVariable Long idUsuario,
            @PathVariable Long idAmbiente) {

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
        	
        	if (ambiente.getUsuario().getIdUsuario() != usuario.getIdUsuario()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                //Ambiente não pertence ao usuario
            }
        	
		}else if(ambiente.getVisibilidade() == VisibilidadeEnum.P) {
			
			
		}


        return ResponseEntity.ok(ambiente);
    }
    
    
    // No filtro de Ambientes Privados 
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
    @GetMapping("/ativo/usuario/{idUsuario}")
    public ResponseEntity<Ambiente> buscarAmbienteAtivo(
            @PathVariable Long idUsuario) {

        Optional<Ambiente> op = repAmbiente.buscarAmbienteAtivoHome(idUsuario);

        if (op.isPresent()) {
            return ResponseEntity.ok(op.get());
        }
        return ResponseEntity.notFound().build();
    }
    
    // NA ficha de ambiente inativo
    @PutMapping("/usuario/{idUsuario}/ambiente/{idAmbiente}/ativarAmbiente")
    public ResponseEntity<Ambiente> ativarAmbiente(
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
    @Transactional
    @PostMapping("/usuario/{idUsuario}/criar-ambiente")
    public ResponseEntity<Long> criarAmbienteValido(
            @PathVariable Long idUsuario,
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
    @Transactional
    @PostMapping("/usuario/{idUsuario}/baixar-ambiente-publico/{idAmbiente}")
    public ResponseEntity<Long> baixarAmbientePublico(
            @PathVariable Long idUsuario,
            @PathVariable Long idAmbiente) {

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
        	 ResponseEntity.notFound().build();
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
    @PutMapping(value = "/usuario/{idUsuario}/editar-ambiente/{idAmbiente}")
    public ResponseEntity<Void> editarAmbiente(
            @PathVariable Long idUsuario,
            @PathVariable Long idAmbiente,
            @RequestBody @Valid Ambiente ambienteAtualizado) {

        Optional<Ambiente> op_ambiente_pub = repAmbiente.findById(idAmbiente);
        if (op_ambiente_pub.isEmpty()) {
        	return ResponseEntity.notFound().build();
        	// AMBIENTE PRIVADO NÃO ENCONTRADO
        }
        Ambiente ambiente = op_ambiente_pub.get();
        
        if (ambiente.getUsuario().getIdUsuario() != idUsuario) {
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
        
        
        ambiente.setNomeAmbiente(ambienteAtualizado.getNomeAmbiente());

        repAmbiente.save(ambiente);

        return ResponseEntity.noContent().build();

    }

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
    	Long idTesteApagar=null;
    	Long idPropsSnapshotApagar=null;
    	
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
        
        
        //---------- Coletar o id do teste para apagar
        
        
      //---------- Coletar o id do conjunto props snapshot para apagar
    }

} 