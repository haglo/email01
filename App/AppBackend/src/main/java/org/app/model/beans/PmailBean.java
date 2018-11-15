package org.app.model.beans;

import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.app.model.dao.PmailDAO;
import org.app.model.entity.Pmail;
import org.app.model.entity.PmailFolder01;

@Stateless
@Remote(PmailDAO.class)
public class PmailBean implements PmailDAO {

	@PersistenceContext
	private EntityManager em;

	@Override
	public Pmail create(Pmail pmail) {
		em.persist(pmail);
		em.flush();
		return pmail;
	}

	@Override
	public Pmail update(Pmail pmail) {
		pmail = em.merge(pmail);
		em.flush();
		return pmail;
	}

	@Override
	public void remove(Integer id) {
		Pmail toBeDeleted = findByID(id);
		em.remove(toBeDeleted);
		em.flush();
	}

	@Override
	public Pmail findByID(Integer id) {
		return em.find(Pmail.class, id);
	}

	@Override
	public List<Pmail> findAll() {
		return em.createNamedQuery(Pmail.QUERY_FIND_ALL, Pmail.class).getResultList();
	}

	@Override
	public List<Pmail> findByFolderName(PmailFolder01 pmailFolder01) {
		return em.createNamedQuery(Pmail.QUERY_FIND_BY_FOLDERNAME, Pmail.class ).setParameter("pmailFolder01", pmailFolder01).getResultList();
	}

	@Override
	public Pmail findByIampID(Long imapID) {
		return em.createNamedQuery(Pmail.QUERY_FIND_BY_IMAPID, Pmail.class ).setParameter("pimapID", imapID).getSingleResult();
	}

	@Override
	public Pmail findByMessageID(String messageID) {
		return em.createNamedQuery(Pmail.QUERY_FIND_BY_MESSAGEID, Pmail.class ).setParameter("pmessageID", messageID).getSingleResult();
	}

}
