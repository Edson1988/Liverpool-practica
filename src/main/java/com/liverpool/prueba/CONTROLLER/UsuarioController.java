package com.liverpool.prueba.CONTROLLER;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.liverpool.prueba.DTO.Direccion;
import com.liverpool.prueba.DTO.Usuario;
import com.liverpool.prueba.EXCEPCION.DireccionNotFoundException;
import com.liverpool.prueba.SERVICE.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
	
	 private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);


	@Autowired
	private UsuarioService usuarioService;

	@GetMapping("/direccion/{cp}")
	public ResponseEntity<Direccion> obtenerDireccion(@PathVariable String cp) {
		logger.info("Solicitud recibida para obtener la dirección del código postal: {}", cp);
		try {
			Direccion direccion = usuarioService.obtenerDireccionDesdeSepomex(cp);
			logger.info("Dirección obtenida exitosamente para el código postal: {}", cp);
			return ResponseEntity.ok(direccion);
		} catch (DireccionNotFoundException e) {
			logger.warn("Dirección no encontrada para el código postal: {}", cp);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} 
		catch (Exception e) {
			 logger.error("Error inesperado al obtener la dirección para el código postal: {}", cp, e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener la dirección");
		}
	}

	@GetMapping
	public ResponseEntity<List<Usuario>> obtenerTodos() {
		try {
			List<Usuario> usuarios = usuarioService.obtenerTodos();
			return ResponseEntity.ok(usuarios);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener usuarios");
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
		try {
			Optional<Usuario> usuario = usuarioService.obtenerPorId(id);
			return usuario.map(ResponseEntity::ok)
					.orElse(ResponseEntity.notFound().build());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener el usuario");
		}
	}

	@PostMapping
	public ResponseEntity<Usuario> guardar( @RequestBody Usuario usuario) {
		try {
			Usuario nuevoUsuario = usuarioService.guardar(usuario);
			return ResponseEntity.created(URI.create("/api/usuarios/" + nuevoUsuario.getId())).body(nuevoUsuario);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar el usuario");
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		try {
			usuarioService.eliminar(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al eliminar el usuario");
		}
	}
}
