package com.tralaleritos.inventario.service;

import com.tralaleritos.inventario.model.Tienda;
import com.tralaleritos.inventario.repository.TiendaRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TiendaService {

    @Autowired
    private TiendaRepository tiendaRepository;

    @Transactional
    public Tienda crearTienda(Tienda tienda) {
        // Validar unicidad de Nombre
        if (tienda.getNombre() != null && tiendaRepository.findByNombre(tienda.getNombre()).isPresent()) {
            throw new EntityExistsException("Ya existe una tienda con el nombre: " + tienda.getNombre());
        }
        // Validar unicidad de Email (si es obligatorio y único)
        if (tienda.getEmail() != null && !tienda.getEmail().isEmpty()) { // Solo validar si no es nulo o vacío
            if (tiendaRepository.findByEmail(tienda.getEmail()).isPresent()) {
                throw new EntityExistsException("Ya existe una tienda con el email: " + tienda.getEmail());
            }
        }

        tienda.setId(null); // Asegurar la creación
        return tiendaRepository.save(tienda);
    }

    @Transactional(readOnly = true)
    public List<Tienda> obtenerTodasLasTiendas() {
        return tiendaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Tienda> obtenerTiendaPorId(Long id) {
        return tiendaRepository.findById(id);
    }

    @Transactional
    public Tienda actualizarTienda(Long id, Tienda tiendaActualizacion) {
        Tienda tiendaExistente = tiendaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tienda no encontrada con id: " + id));

        // Validar unicidad de Nombre si se está cambiando
        if (tiendaActualizacion.getNombre() != null && !tiendaActualizacion.getNombre().equals(tiendaExistente.getNombre())) {
            if (tiendaRepository.findByNombre(tiendaActualizacion.getNombre()).isPresent()) {
                throw new EntityExistsException("Ya existe otra tienda con el nombre: " + tiendaActualizacion.getNombre());
            }
        }
        // Validar unicidad de Email si se está cambiando y no es nulo/vacío
        if (tiendaActualizacion.getEmail() != null && !tiendaActualizacion.getEmail().isEmpty() &&
            !tiendaActualizacion.getEmail().equals(tiendaExistente.getEmail())) {
            if (tiendaRepository.findByEmail(tiendaActualizacion.getEmail()).isPresent()) {
                throw new EntityExistsException("Ya existe otra tienda con el email: " + tiendaActualizacion.getEmail());
            }
        }

        tiendaExistente.setNombre(tiendaActualizacion.getNombre());
        tiendaExistente.setDireccion(tiendaActualizacion.getDireccion());
        tiendaExistente.setTelefono(tiendaActualizacion.getTelefono());
        tiendaExistente.setEmail(tiendaActualizacion.getEmail()); // Permitir actualizar a nulo si el campo lo permite
        tiendaExistente.setHorariosApertura(tiendaActualizacion.getHorariosApertura());
        tiendaExistente.setPersonalAsignado(tiendaActualizacion.getPersonalAsignado());
        tiendaExistente.setPoliticasLocales(tiendaActualizacion.getPoliticasLocales());

        // La lista de inventarios se gestiona a través de la entidad Inventario.
        return tiendaRepository.save(tiendaExistente);
    }

    @Transactional
    public void eliminarTienda(Long id) {
        if (!tiendaRepository.existsById(id)) {
            throw new EntityNotFoundException("Tienda no encontrada con id: " + id);
        }
        // Con CascadeType.ALL y orphanRemoval=true en Tienda.inventarios,
        // los registros de inventario asociados a esta tienda también se eliminarán.
        // Esto suele ser el comportamiento deseado (si se elimina una tienda, su inventario también).
        tiendaRepository.deleteById(id);
    }
}