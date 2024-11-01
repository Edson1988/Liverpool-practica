package com.liverpool.prueba.SERVICE;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liverpool.prueba.DAO.UsuarioRepository;
import com.liverpool.prueba.DTO.Direccion;
import com.liverpool.prueba.DTO.Usuario;
import com.liverpool.prueba.EXCEPCION.DireccionNotFoundException;

@Service
public class UsuarioService {

	private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);



	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private RestTemplate restTemplate;


	public Direccion obtenerDireccionDesdeSepomex(String cp) {

		if(cp == null || cp.length() != 5 || !cp.matches("\\d{5}")) {

			logger.warn("Código postal inválido: {}", cp);
			throw new IllegalArgumentException("Código postal inválido: " + cp);

		}



		try {
			logger.info("Consultando dirección para el código postal: {}", cp);
			String token = "a70d9cfc-bfb7-4205-87c3-89a97008f4f2"; // Tu token específico
			String url = "https://api.copomex.com/query/info_cp/" + cp + "?token=" + token;

			// Realiza la solicitud a la API y obtiene la respuesta en formato JSON
			String responseJson = restTemplate.getForObject(url, String.class);

			// Procesa la respuesta JSON
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(responseJson);

			// Verifica si hay un error en la respuesta de la API
			if (jsonNode.path("error").asBoolean()) {
				logger.error("Error al obtener la dirección: {}", jsonNode.path("error_message").asText());
				throw new DireccionNotFoundException("Error al obtener la dirección para el código postal: " + cp);
			}

			// Extrae los datos de la respuesta para el DTO Direccion
			JsonNode responses = jsonNode.path("response");
			if (responses.isArray() && responses.size() > 0) {
				// Asumir que tomamos la primera dirección (puedes modificar esto según tu lógica)
				JsonNode responseNode = responses.get(0); 
				Direccion direccion = new Direccion();
				direccion.setCodigoPostal(responseNode.path("cp").asText());
				direccion.setColonia(responseNode.path("asentamiento").asText());
				direccion.setPais(responseNode.path("pais").asText());
				direccion.setCiudad(responseNode.path("ciudad").asText());
				direccion.setEstado(responseNode.path("estado").asText());

				logger.info("Dirección obtenida para el código postal {}: {}", cp, direccion);
				return direccion;
			} else {
				logger.warn("No se encontraron direcciones para el código postal: {}", cp);
				throw new DireccionNotFoundException("No se encontraron direcciones para el código postal: " + cp);
			}


		} catch (Exception e) {
			logger.error("Error al obtener la dirección desde COPOMEX para el código postal: {}", cp, e);
			throw new RuntimeException("Error al obtener la dirección desde COPOMEX", e);
		}
	}

	// Método para obtener todos los usuarios
	public List<Usuario> obtenerTodos() {
		return usuarioRepository.findAll();
	}

	// Método para obtener un usuario por ID
	public Optional<Usuario> obtenerPorId(Long id) {
		return usuarioRepository.findById(id);
	}

	// Método para guardar un nuevo usuario
	public Usuario guardar(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}

	// Método para eliminar un usuario por ID
	public void eliminar(Long id) {
		usuarioRepository.deleteById(id);
	}
}
