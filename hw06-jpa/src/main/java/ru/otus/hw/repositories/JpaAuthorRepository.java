package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class JpaAuthorRepository implements AuthorRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Author> findAll() {
        return em.createQuery("select a from Author a", Author.class).getResultList();
    }

    @Override
    public Optional<Author> findById(Long id) {
        return Optional.ofNullable(em.find(Author.class, id));
    }

    @Override
    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }

    @Override
    public List<Author> findAllByIds(Set<Long> ids) {
        TypedQuery<Author> query = em.createQuery("SELECT a from Author a WHERE id IN (:ids)",
            Author.class
        );

        query.setParameter("ids", ids);
        return query.getResultList();
    }

    @Override
    public void deleteById(Long id) {
        findById(id)
            .ifPresent(em::remove);
    }

    @Override
    public Author save(Author author) {
        if (author.getId() == null) {
            em.persist(author);
            return author;
        } else {
            return em.merge(author);
        }
    }
}
