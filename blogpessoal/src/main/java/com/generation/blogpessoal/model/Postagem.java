package com.generation.blogpessoal.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="tb_postagens")
public class Postagem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "O atributo titulo é obrigatório.")
	@Size(min = 5, max = 100, message = "O atributo titulo deve contér no minimo 5 e no maximo 100 caracteres.")
	private String titulo;
	
	@NotBlank(message = "O atributo texto é obrigatório.")
	@Size(min = 10, max = 1000, message = "O atributo titulo deve contér no minimo 10 e no maximo 1000 caracteres.")
	private String texto;
	
	@UpdateTimestamp
	private LocalDateTime data;
	
	
	@ManyToOne
	@JsonIgnoreProperties("postagem")
	private Tema tema;
	
	
	/*Métodos Get and Set*/

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}

	public Tema getTema() {
		return tema;
	}

	public void setTema(Tema tema) {
		this.tema = tema;
	}
	
	
	
	
}
