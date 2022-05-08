package au.com.livewirelabs.assignment.guice.dao;

import au.com.livewirelabs.assignment.model.Stock;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

public class StockDaoImpl implements StockDao {

    @Inject
    private EntityManager entityManager;

    @Override
    public void save(Stock stock) {
        entityManager.getTransaction().begin();
        entityManager.persist(stock);
        entityManager.getTransaction().commit();
    }

    @Override
    public Stock getByCode(String stockCode) {
        return (Stock) entityManager
                .createQuery("select e from stock e where e.code=:stockCode")
                .setParameter("stockCode", stockCode)
                .getSingleResult();
    }

    public List<Stock> getAllStocks() {
        return entityManager.createNamedQuery("stock.findAll").getResultList();
    }
}
