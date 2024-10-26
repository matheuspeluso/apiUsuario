package br.com.cotiinformatica.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cotiinformatica.dtos.AutenticarUsuarioRequestDto;
import br.com.cotiinformatica.dtos.CriarUsuarioRequestDto;
import br.com.cotiinformatica.services.UsuarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/usuarios")
public class UsuariosController {
	
	@Autowired //injeções de dependências
	UsuarioService usuarioService;
	
	@PostMapping("criar") //@Valid = executa a validação dos dados da dto que foram configurados
	public String criar(@RequestBody @Valid CriarUsuarioRequestDto dto) { 
		return usuarioService.criarUsuario(dto);
	}
	
	@PostMapping("autenticar")
	public String autenticar(@RequestBody @Valid AutenticarUsuarioRequestDto dto) {
		return usuarioService.autenticarUsuario(dto);
	}
}
