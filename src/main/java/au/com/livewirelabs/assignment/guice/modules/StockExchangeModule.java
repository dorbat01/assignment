package au.com.livewirelabs.assignment.guice.modules;

import au.com.livewirelabs.assignment.guice.dao.StockDao;
import au.com.livewirelabs.assignment.guice.dao.StockDaoImpl;
import au.com.livewirelabs.assignment.service.StockExchangeService;
import au.com.livewirelabs.assignment.service.StockExchangeServiceImpl;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StockExchangeModule  extends AbstractModule {

    private static final ThreadLocal<EntityManager> ENTITY_MANAGER_CACHE = new ThreadLocal<EntityManager>();
    private static final String DB_MANAGER_NAME = "db-manager";

    @Override
    protected void configure() {
        try {
            bind(StockDao.class).to(StockDaoImpl.class);
            bind(StockExchangeService.class).to(StockExchangeServiceImpl.class);
        } catch (SecurityException e) {
            Logger.getLogger(au.com.livewirelabs.assignment.guice.modules.StockExchangeModule.class.getName())
                    .log(Level.SEVERE, null, e);
        }
    }

    @Provides
    @Singleton
    public EntityManagerFactory createEntityManagerFactory() {
        return Persistence.createEntityManagerFactory(DB_MANAGER_NAME);
    }

    @Provides
    public EntityManager createEntityManager(
            EntityManagerFactory entityManagerFactory) {
        EntityManager entityManager = ENTITY_MANAGER_CACHE.get();
        if (entityManager == null) {
            ENTITY_MANAGER_CACHE.set(entityManager = entityManagerFactory
                    .createEntityManager());
        }
        return entityManager;
    }
}
