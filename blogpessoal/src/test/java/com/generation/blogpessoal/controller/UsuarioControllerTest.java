package com.generation.blogpessoal.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.model.UsuarioLogin;
import com.generation.blogpessoal.service.UsuarioService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioControllerTest {
	
	@Autowired
	private TestRestTemplate  testRestTemplate ;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Test
	@Order(1)
	@DisplayName("Cadastrar um usuários")
	public void deveCriarUmUsuarios() { 
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>
		(new Usuario (0L, "Paulo Antunes", "paulo_antunes@email.com.br", "13465278", "https://i.imgur.com/JR7kUFU.jpg"));
		
		ResponseEntity<Usuario> corpoResposta = testRestTemplate
				.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);
		
		assertEquals(HttpStatus.CREATED, corpoResposta.getStatusCode());
		
		assertEquals(corpoRequisicao.getBody().getNome(), corpoResposta.getBody().getNome());
		
		assertEquals(corpoRequisicao.getBody().getUsuario(), corpoResposta.getBody().getUsuario());		
	}
	
	@Test
	@Order(2)
	@DisplayName("Não deve permitir duplicação do Usuário")
	public void naoDeveDuplicarUsuario() {
		usuarioService.cadastrarUsuario(new Usuario(0L, 
				"Maria da Silva", "maria_silva@email.com.br", "13465278", "https://i.imgur.com/T12NIp9.jpg"));
		
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario(0L, 
				"Maria da Silva", "maria_silva@email.com.br", "13465278", "https://i.imgur.com/T12NIp9.jpg"));
		
		ResponseEntity<Usuario> corpoResposta = testRestTemplate
				.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, corpoResposta.getStatusCode());	
		
	}

	@Test
	@Order(3)
	@DisplayName("Atualizar um Usuário")
	public void deveAtualizarUmUsuario() { 
		Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(new Usuario(0L, 
				"Juliana Andrews", "juliana_andrews@email.com.br", "juliana123", "https://i.imgur.com/yDRVeK7.jpg"));
		
		Usuario usuarioUpdate = new Usuario(usuarioCadastrado.get().getId(), 
				"Juliana Andrews Ramos", "juliana_ramos@email.com.br", "juliana123" , "https://i.imgur.com/yDRVeK7.jpg");
		
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(usuarioUpdate);
		
		ResponseEntity<Usuario> corpoResposta = testRestTemplate
				.withBasicAuth("root", "root")
				.exchange("/usuarios/atualizar", HttpMethod.PUT, corpoRequisicao, Usuario.class);
		
		assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
		
		assertEquals(corpoRequisicao.getBody().getNome(), corpoResposta.getBody().getNome());
		
		assertEquals(corpoRequisicao.getBody().getUsuario(), corpoResposta.getBody().getUsuario());
	}
	
	@Test
	@Order(4)
	@DisplayName("Listar todos os Usuários")
	public void deveMostrarTodosUsuarios() { 
		
		usuarioService.cadastrarUsuario(new Usuario(0L, 
				"Sabrina Sanches", "sabrina_sanches@email.com.br", "sabrina123", "https://i.imgur.com/5M2p5Wb.jpg"));
		
		usuarioService.cadastrarUsuario(new Usuario(0L, 
				"Ricardo Marques", "ricardo_marques@email.com.br", "ricardo123", "https://i.imgur.com/Sk5SjWE.jpg"));
		
		ResponseEntity<String> resposta = testRestTemplate
				.withBasicAuth("root", "root")
				.exchange("/usuarios/all", HttpMethod.GET, null, String.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}
	
	@Test
	@Order(5)
	@DisplayName("Listar Um Usuário Específico")
	public void deveListarApenasUmUsuario() { 
		
		Optional<Usuario> usuarioBusca = usuarioService.cadastrarUsuario(new Usuario(0L, 
				"Larissa Manoela", "hogwarts.lari@email.com.br", "larissa123", "https://linkdafoto.jpg"));
		
		ResponseEntity<String> resposta = testRestTemplate
				.withBasicAuth("root", "root")
				.exchange("/usuarios/" + usuarioBusca.get().getId(), HttpMethod.GET, null, String.class);
		assertEquals(HttpStatus.OK, resposta.getStatusCode());	
	}
	

	@Test
	@Order(6)
	@DisplayName("Deve logar o usuario.")
	public void deveLogarUsuario() {
		
		usuarioService.cadastrarUsuario(new Usuario(0L,
				"Alexandra Monarquia", "alexa_impera@email.com", "alexaa1234","http://linkdafoto.com"));
				
		HttpEntity<UsuarioLogin> loginRequest = new HttpEntity<UsuarioLogin>(new UsuarioLogin(
				"", "alexa_impera@email.com", "alexaa1234", ""));
				
		ResponseEntity<UsuarioLogin> loginReturn = testRestTemplate
				.exchange("/usuarios/logar", HttpMethod.POST, loginRequest, UsuarioLogin.class); 
				
				assertEquals(HttpStatus.OK, loginReturn.getStatusCode());
	}
	
}
