package io.github.alirezamht.authentication.service;

import io.github.alirezamht.authentication.model.Session;
import io.github.alirezamht.authentication.repository.SessionRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component("sessionService")
@Transactional
public class SessionServiceImp implements SessionService {


    private final SessionRepository sessionRepository;

    public SessionServiceImp(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Session getSessionBySS(String secureString) {
        return sessionRepository.findBySessionString(secureString);
    }

    @Override
    public List<Session> getSessionByUserIdentifier(String id) {
        return sessionRepository.findByUserIdentifier(id);
    }

    @Override
    public void save(Session session) {
        sessionRepository.save(session);
    }

    @Override
    public void update(Session session) {
        sessionRepository.update(session.getId(),session.getExpireTime());

    }

    @Override
    public void delete(Session session) {
        sessionRepository.delete(session);
    }
}
