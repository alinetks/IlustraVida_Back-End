package com.generation.blogpessoal.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.model.Tema;
import com.generation.blogpessoal.repository.PostagemRepository;
import com.generation.blogpessoal.repository.TemaRepository;



@RestController
@RequestMapping("/postagens") 
@CrossOrigin(origins = "*", allowedHeaders = "*") 
public class PostagemController {
	
	private PostagemRepository postagemRepository;
	private TemaRepository temaRepository;
	
	@Autowired 
	public PostagemController(PostagemRepository postagemRepository, TemaRepository temaRepository){
		this.postagemRepository = postagemRepository;
		this.temaRepository = temaRepository;
	}
	
	@GetMapping
	public ResponseEntity <List<Postagem>> getAll(){
		return ResponseEntity.ok(this.postagemRepository.findAll());
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<Postagem> getById(@PathVariable Long id){
		return postagemRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.notFound().build());
		
	}
	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<List<Postagem>> getByTitulo(@PathVariable String titulo){
		return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
	}
	
	 @PostMapping()
	 public ResponseEntity<Postagem> create(@RequestBody @Valid Postagem postagem) {
		 		Optional<Tema> optionalTema = temaRepository.findById(postagem.getTema().getId());//Star Wars - id = 1 ;"tema" :{id: 1}// null // True Or False
	
	        if (!optionalTema.isPresent()) {
	            return ResponseEntity.badRequest().build();
	        }
	        postagem.setTema(optionalTema.get());
	        Postagem savedPostagem = postagemRepository.save(postagem);
	        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
	            .buildAndExpand(savedPostagem.getId()).toUri();
	        return ResponseEntity.created(location).body(savedPostagem);
	    }
	 @PutMapping
		public ResponseEntity<Postagem> putPostagem (@Valid @RequestBody Postagem postagem){
			return postagemRepository.findById(postagem.getId())
				.map(resposta -> ResponseEntity.ok().body(postagemRepository.save(postagem)))
				.orElse(ResponseEntity.notFound().build());
		}
	@DeleteMapping(path = "/{id}")
	public  ResponseEntity<?> deletePostagem(@PathVariable Long id) {
		return postagemRepository.findById(id).map(record -> {
			postagemRepository.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).build();
		}).orElse(ResponseEntity.notFound().build());
		
	}
	
	
}