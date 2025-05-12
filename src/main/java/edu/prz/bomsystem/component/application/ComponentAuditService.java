package edu.prz.bomsystem.component.application;

import edu.prz.bomsystem.component.domain.Component;
import edu.prz.bomsystem.component.domain.Component.ComponentId;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ComponentAuditService {

  @PersistenceContext
  private EntityManager entityManager;

  @Transactional
  public List<Component> getComponentVersions(ComponentId componentId) {
    AuditReader auditReader = AuditReaderFactory.get(entityManager);
    AuditQuery queryHistoryOfComponent = auditReader.createQuery()
        .forRevisionsOfEntity(Component.class,true,true)
        .add(AuditEntity.property("id").eq(componentId.getId()));

    return queryHistoryOfComponent.getResultList();
  }

  @Transactional
  public Component getComponentFromVersion(ComponentId componentId, int version){
    List<Component> componentVersionsList = getComponentVersions(componentId);
    return componentVersionsList.get(version);
  }

}
