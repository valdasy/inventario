package com.tralaleritos.inventario.service;

import com.tralaleritos.inventario.model.Proveedor;
import com.tralaleritos.inventario.repository.ProveedorRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Transactional
    public Proveedor crearProveedor(Proveedor proveedor) {
        // Validar unicidad de RUT
        if (proveedor.getRut() != null && proveedorRepository.findByRut(proveedor.getRut()).isPresent()) {
            throw new EntityExistsException("Ya existe un proveedor con el RUT: " + proveedor.getRut());
        }
        // Validar unicidad de Email
        if (proveedor.getEmail() != null && proveedorRepository.findByEmail(proveedor.getEmail()).isPresent()) {
            throw new EntityExistsException("Ya existe un proveedor con el email: " + proveedor.getEmail());
        }

        proveedor.setId(null); // Asegurar la creación
        return proveedorRepository.save(proveedor);
    }

    @Transactional(readOnly = true)
    public List<Proveedor> obtenerTodosLosProveedores() {
        return proveedorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Proveedor> obtenerProveedorPorId(Long id) {
        return proveedorRepository.findById(id);
    }

    @Transactional
    public Proveedor actualizarProveedor(Long id, Proveedor proveedorActualizacion) {
        Proveedor proveedorExistente = proveedorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado con id: " + id));

        // Validar unicidad de RUT si se está cambiando
        if (proveedorActualizacion.getRut() != null && !proveedorActualizacion.getRut().equals(proveedorExistente.getRut())) {
            if (proveedorRepository.findByRut(proveedorActualizacion.getRut()).isPresent()) {
                throw new EntityExistsException("Ya existe otro proveedor con el RUT: " + proveedorActualizacion.getRut());
            }
        }
        // Validar unicidad de Email si se está cambiando
        if (proveedorActualizacion.getEmail() != null && !proveedorActualizacion.getEmail().equals(proveedorExistente.getEmail())) {
            if (proveedorRepository.findByEmail(proveedorActualizacion.getEmail()).isPresent()) {
                throw new EntityExistsException("Ya existe otro proveedor con el email: " + proveedorActualizacion.getEmail());
            }
        }

        proveedorExistente.setNombre(proveedorActualizacion.getNombre());
        proveedorExistente.setRut(proveedorActualizacion.getRut());
        proveedorExistente.setDireccion(proveedorActualizacion.getDireccion());
        proveedorExistente.setTelefono(proveedorActualizacion.getTelefono());
        proveedorExistente.setEmail(proveedorActualizacion.getEmail());

        // La lista de productos suministrados se gestiona a través de la entidad Producto,
        // no se actualiza directamente aquí.
        return proveedorRepository.save(proveedorExistente);
    }

    @Transactional
    public void eliminarProveedor(Long id) {
        if (!proveedorRepository.existsById(id)) {
            throw new EntityNotFoundException("Proveedor no encontrado con id: " + id);
        }
        // Considerar: ¿Qué sucede con los productos asociados?
        // Con CascadeType.ALL y orphanRemoval=true en Proveedor.productosSuministrados,
        // los productos asociados también se eliminarán. Esto podría no ser deseado.
        // Si los productos deben permanecer y solo desvincularse, se necesitaría
        // cambiar la cascada y/o manejar la desvinculación manualmente.
        // Si un producto NO PUEDE existir sin proveedor (Foreign Key con ON DELETE CASCADE o similar
        // o si la lógica de negocio lo impide), entonces eliminar el proveedor implicaría eliminar sus productos.
        // ¡Revisa esta lógica según tus requisitos! Por ahora, la cascada los eliminará.
        proveedorRepository.deleteById(id);
    }
}