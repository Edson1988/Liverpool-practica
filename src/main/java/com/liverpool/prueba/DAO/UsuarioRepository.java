package com.liverpool.prueba.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import com.liverpool.prueba.DTO.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
