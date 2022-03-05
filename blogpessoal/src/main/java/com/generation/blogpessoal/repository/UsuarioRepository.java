package com.generation.blogpessoal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.blogpessoal.model.Usuario;



public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	/** 
	 * Método criado para checar se o usuário já existe no banco de dados
	 */ 
	public Optional<Usuario> findByUsuario(String usuario);

	/** 
	 * Método criado para a Sessão de testes
	 */ 
}
