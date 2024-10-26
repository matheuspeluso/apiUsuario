package br.com.cotiinformatica.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cotiinformatica.components.JwtTokenComponent;
import br.com.cotiinformatica.components.SHA256Component;
import br.com.cotiinformatica.dtos.AutenticarUsuarioRequestDto;
import br.com.cotiinformatica.dtos.CriarUsuarioRequestDto;
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
	
	public String criarUsuario(CriarUsuarioRequestDto dto) {
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
			
		return "Usuário cadastrado com sucesso!";
	}
	
	/*Método para autenticar um usuário no banco de dados*/
	public String autenticarUsuario(AutenticarUsuarioRequestDto dto) {
		
		//vericando se o usuario existe no banco de dados
		var usuario = usuarioRepository.findByEmailAndSenha(dto.getEmail(),sha256Component.hash(dto.getSenha()));
		
		if(usuario == null) {
			throw new IllegalArgumentException("Usuário inválido.");
		}
		
		var token = jwtTokenComponet.generateToken(usuario.getId());
		return token;
		
	}
}
