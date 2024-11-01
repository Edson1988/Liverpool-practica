package com.liverpool.prueba.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.ToString;

@Entity
@ToString
public class Direccion {
	
	   @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @JsonProperty("id")
	    private Long id;

	    @JsonProperty("asentamiento")
	    private String colonia;  // Cambiado de "colonia" a "asentamiento"

	    @JsonProperty("cp")
	    private String codigoPostal;

	    @JsonProperty("ciudad")  // Agregado para almacenar la ciudad
	    private String ciudad;

	    @JsonProperty("estado")  // Agregado para almacenar el estado
	    private String estado;

	    @JsonProperty("pais")  // Agregado para almacenar el país
	    private String pais;

	    // Getters y Setters
	    public Long getId() {
	        return id;
	    }
	    
	    public void setId(Long id) {
	        this.id = id;
	    }

	    public String getColonia() {
	        return colonia;
	    }

	    public void setColonia(String colonia) {
	        this.colonia = colonia;
	    }

	    public String getCodigoPostal() {
	        return codigoPostal;
	    }

	    public void setCodigoPostal(String codigoPostal) {
	        this.codigoPostal = codigoPostal;
	    }

	    public String getCiudad() {
	        return ciudad;
	    }

	    public void setCiudad(String ciudad) {
	        this.ciudad = ciudad;
	    }

	    public String getEstado() {
	        return estado;
	    }

	    public void setEstado(String estado) {
	        this.estado = estado;
	    }

	    public String getPais() {
	        return pais;
	    }

	    public void setPais(String pais) {
	        this.pais = pais;
	    }
}
