package edu.prz.bomsystem.module.application;

import edu.prz.bomsystem.module.domain.Module;
import edu.prz.bomsystem.module.domain.Module.ModuleId;
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
public class ModuleAuditService {

  @PersistenceContext
  private EntityManager entityManager;

  @Transactional
  public List<edu.prz.bomsystem.module.domain.Module> getModuleVersions(ModuleId moduleId) {
    AuditReader auditReader = AuditReaderFactory.get(entityManager);
    AuditQuery queryHistoryOfModule = auditReader.createQuery()
        .forRevisionsOfEntity(Module.class,true,true)
        .add(AuditEntity.property("id").eq(moduleId.getId()));

    return queryHistoryOfModule.getResultList();
  }

  @Transactional
  public edu.prz.bomsystem.module.domain.Module getModuleFromVersion(ModuleId moduleId, int version){
    List<edu.prz.bomsystem.module.domain.Module> moduleVersionsList = getModuleVersions(moduleId);
    return moduleVersionsList.get(version);
  }

}
