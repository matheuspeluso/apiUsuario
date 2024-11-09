package br.com.cotiinformatica.services;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cotiinformatica.components.JwtTokenComponent;
import br.com.cotiinformatica.components.SHA256Component;
import br.com.cotiinformatica.dtos.AutenticarUsuarioRequestDto;
import br.com.cotiinformatica.dtos.AutenticarUsuarioResponseDto;
import br.com.cotiinformatica.dtos.CriarUsuarioRequestDto;
import br.com.cotiinformatica.dtos.CriarUsuarioResponseDto;
import br.com.cotiinformatica.entities.Usuario;
import br.com.cotiinformatica.repositories.PerfilRepository;
import br.com.cotiinformatica.repositories.PermissaoRepository;
import br.com.cotiinformatica.repositories.UsuarioRepository;

@Service/*anotação que define uma classe de serviços, que geralmente será usada para 
implementarmos regras de negocio p/ alguma entidade */ 
public class UsuarioService {
	/*método para criar um usuario no banco de dados*/
	
	@Autowired //injeção de dependências
	UsuarioRepository usuarioRepository;
	
	@Autowired
	PerfilRepository perfilRepository;
	
	@Autowired
	PermissaoRepository permissaoRepository;
	
	@Autowired
	SHA256Component sha256Component;
	
	@Autowired
	JwtTokenComponent jwtTokenComponet;
	
	public CriarUsuarioResponseDto criarUsuario(CriarUsuarioRequestDto dto) {
		//Regra de negócio: não permitir o cadastro de 2 usuario com o mesmo email
		if(usuarioRepository.findByEmail(dto.getEmail()) != null) {
			throw new IllegalArgumentException("O email informado já está cadatrado , tente outro!");
		}
		var usuario = new Usuario();
		usuario.setId(UUID.randomUUID());
		usuario.setNome(dto.getNome());
		usuario.setEmail(dto.getEmail());
		usuario.setSenha(sha256Component.hash(dto.getSenha()));
		usuario.setPerfil(perfilRepository.findByNome("OPERADOR"));
		
		//CADASTRANDO USUARIO NO BANCO
		usuarioRepository.save(usuario);
		
		//retornando o dto de resposta 
		var response = new CriarUsuarioResponseDto();
		response.setId(usuario.getId());
		response.setNome(usuario.getNome());
		response.setEmail(usuario.getEmail());
		response.setPerfil(usuario.getPerfil().getNome());
		response.setMensagem("Usuário cadastrado com sucesso!");
		
		return response;
	}
	
	/*Método para autenticar um usuário no banco de dados*/
	public AutenticarUsuarioResponseDto autenticarUsuario(AutenticarUsuarioRequestDto dto) {
		
		//vericando se o usuario existe no banco de dados
		var usuario = usuarioRepository.findByEmailAndSenha(dto.getEmail(),sha256Component.hash(dto.getSenha()));
		
		if(usuario == null) {
			throw new IllegalArgumentException("Usuário inválido.");
		}
		
		var token = jwtTokenComponet.generateToken(usuario.getId());
		
		//retornando o dto de resposta
				var response = new AutenticarUsuarioResponseDto();
				response.setId(usuario.getId());
				response.setNome(usuario.getNome());
				response.setEmail(usuario.getEmail());
				response.setToken(token);
				response.setDataHoraAcesso(new Date());
				response.setDataHoraExpiracao(new Date(new Date().getTime() + 600000));
				response.setPerfil(usuario.getPerfil().getNome());
				response.setMensagem("Usuário autenticado com sucesso.");
			
				return response;
	}
}
