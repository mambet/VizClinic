package ru.viz.clinic.data.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.viz.clinic.data.entity.Personal;
import ru.viz.clinic.data.model.PersonalDTO;
import ru.viz.clinic.data.repository.CommonRepository;

import java.util.List;
import java.util.Optional;

public abstract class AbstractService<E extends Personal, R extends CommonRepository<E>, D extends  PersonalDTO> implements CommonService<E> {
    private final Class<E> eClass;
    protected final R repository;

    private final ModelMapper modelMapper;

    @Autowired
    public AbstractService(
            Class<E> eClass,
            R repository,
            ModelMapper modelMapper
    ) {
        this.eClass = eClass;
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void save(D personalDTO) {
        repository.save(modelMapper.map(personalDTO, eClass));
    }

    public List<E> findByUsername(String name) {
        return repository.findByUsername(name);
    }

    public Optional<E> get(Long id) {
        return repository.findById(id);
    }

    public E update(E personal) {
        return repository.save(personal);
    }


    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<E> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

    public List<E> getAll() {
        return repository.findAll();
    }


    @Override
    public Optional<E> save(E personal) {
        return Optional.of(repository.save(personal));
    }
}
