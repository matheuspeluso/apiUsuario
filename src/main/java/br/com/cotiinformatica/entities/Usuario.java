package br.com.cotiinformatica.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity //anotation para dizer que essa classe vai ser uma entidade que vai ser manipulada pelo spring data
@Table(name = "tb_usuario") //anotação para definir o nome da tabela do bd para qual a classe está sendo mapeada

public class Usuario {
	
	@Id // define o compo com chave primaria na nossa tabela
	@Column(name = "id") // anotação para definir as caractristicas de um campo da tebale.
	private UUID id;
	
	@Column(name= "nome",length = 150, nullable = false)
	private String nome;
	
	@Column(name = "email", length = 50, nullable = false, unique = true)
	private String email;
	
	@Column(name="senha", length = 100, nullable =false)
	private String senha;
	
	//todo vez que tiver um many to one tem que ter a forein key que no caso o joincolumn
	@ManyToOne //anotação que define um mapeamento de relação de muitos para um.
	@JoinColumn(name = "perfil_id", nullable = false) // mepeia um campo chave estrangeira de um relacionamento muitos para um
	private Perfil perfil; // Associação de composição
	
}
