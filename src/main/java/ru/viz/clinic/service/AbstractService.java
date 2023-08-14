package ru.viz.clinic.service;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.viz.clinic.data.entity.Personal;
import ru.viz.clinic.data.repository.CommonRepository;
import ru.viz.clinic.help.Helper;

import java.util.List;
import java.util.Optional;

import static ru.viz.clinic.help.Translator.MSG_ENGINEER_SUCCESS_SAVED;

@Slf4j
public abstract class AbstractService<E extends Personal, R extends CommonRepository<E>> implements CommonService<E> {
    protected final R repository;

    @Autowired
    public AbstractService(
            final R repository
    ) {
        this.repository = repository;
    }

    public Optional<E> findByUsername(final String name) {
        final E byUsername = repository.findByUsername(name);
        if (byUsername != null) {
            return Optional.of(byUsername);
        }
        return Optional.empty();
    }

    public Optional<E> get(final Long id) {
        return repository.findById(id);
    }

    public E update(final E personal) {
        return repository.save(personal);
    }

    public void delete(final Long id) {
        repository.deleteById(id);
    }

    public Page<E> list(final Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

    public List<E> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<E> save(final E personal) {
        return Optional.of(repository.save(personal));
    }
}
