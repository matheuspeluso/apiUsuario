package br.com.cotiinformatica.entities;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_perfil")

public class Perfil {
	
	@Id
	private UUID id;
	
	@Column(name = "nome", length = 25, nullable = false)
	private String nome;
	
	@OneToMany(mappedBy = "perfil") //nome do atributo que foi mapeado o ManyToOne
	private List<Usuario> usuarios;
	
	@ManyToMany // muitos para muitos
	@JoinTable(
			name = "tb_perfil_permissao",
			joinColumns = @JoinColumn(name = "perfil_id", nullable = false),
			inverseJoinColumns = @JoinColumn(name = "permissao_id", nullable = false)
			) // cria uma tabela associativa para armazenar as chaves estrangeiras do relacionamentro de muitos p/ muitos
	private List<Permissao> permissoes;
}
