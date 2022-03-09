package com.generation.blogpessoal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.blogpessoal.model.Usuario;



public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	/** 
	 * Método criado para checar se o usuário já existe no banco de dados
	 */ 
	
	public Optional<Usuario> findByUsuario(String usuario);
	
	public List<Usuario> findAllByNomeContainingIgnoreCase(String nome);


	/** 
	 * Método criado para a Sessão de testes
	 */ 
}
