package ru.otus.hw.repositories;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Comment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static ru.otus.hw.repositories.EntityGraphNames.COMMENT_BOOK_GRAPH;

@Repository
public class JpaCommentRepository implements CommentRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Comment> findAll() {
        return em.createQuery("select c from Comment c", Comment.class).getResultList();
    }

    @Override
    public Optional<Comment> findById(Long id) {
        EntityGraph<?> entityGraph = em.getEntityGraph(COMMENT_BOOK_GRAPH);
        Map<String, Object> properties = new HashMap<>();
        properties.put("jakarta.persistence.fetchgraph", entityGraph);

        return Optional.ofNullable(em.find(Comment.class, id, properties));
    }

    @Override
    public List<Comment> findAllByIds(Set<Long> ids) {
        TypedQuery<Comment> query = em.createQuery("SELECT c from Comment c WHERE id IN (:ids)",
            Comment.class
        );

        EntityGraph<?> entityGraph = em.getEntityGraph(COMMENT_BOOK_GRAPH);
        query.setParameter("ids", ids);
        query.setHint("jakarta.persistence.fetchgraph", entityGraph);
        return query.getResultList();
    }

    @Override
    public Comment insert(Comment comment) {
        em.persist(comment);
        return comment;
    }

    @Override
    public void deleteById(Long id) {
        findById(id)
            .ifPresent(em::remove);
    }

    @Override
    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == null) {
            return insert(comment);
        } else {
            return update(comment);
        }
    }

    @Override
    public Comment update(Comment comment) {
        return em.merge(comment);
    }

    @Override
    public List<Comment> findAllByBookId(Long bookId) {
        EntityGraph<?> entityGraph = em.getEntityGraph(COMMENT_BOOK_GRAPH);

        TypedQuery<Comment> query = em.createQuery("SELECT c from Comment c WHERE c.book.id = :bookId",
            Comment.class
        );

        query.setParameter("bookId", bookId);
        query.setHint("jakarta.persistence.fetchgraph", entityGraph);

        return query.getResultList();
    }

}
