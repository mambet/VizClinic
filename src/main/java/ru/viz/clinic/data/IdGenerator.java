package ru.viz.clinic.data;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.boot.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;
import java.util.stream.Stream;

public class IdGenerator implements IdentifierGenerator, Configurable {
    private String prefix;

    @Override
    public Serializable generate(
            final SharedSessionContractImplementor session,
            final Object obj
    ) throws HibernateException {
        final String query = String.format("select %s from %s",
                session.getEntityPersister(obj.getClass().getName(), obj)
                        .getIdentifierPropertyName(),
                obj.getClass().getSimpleName());

        final Stream<String> ids = session.createQuery(query).stream();

        final long max = ids.map(o -> o.replace(prefix + "-", ""))
                .mapToLong(Long::parseLong)
                .max()
                .orElse(0L);

        return StringUtils.joinWith("-", prefix, String.format("%04d", (max + 1)));
    }

    @Override
    public void configure(
            final Type type,
            final Properties properties,
            final ServiceRegistry serviceRegistry
    ) throws MappingException {
        prefix = properties.getProperty("prefix");
    }
}
