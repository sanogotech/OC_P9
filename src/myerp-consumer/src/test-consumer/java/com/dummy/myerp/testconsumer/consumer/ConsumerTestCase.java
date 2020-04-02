package com.dummy.myerp.testconsumer.consumer;

import com.dummy.myerp.consumer.dao.contrat.DaoProxy;

import javax.sql.DataSource;


/**
 * Classe mère des classes de test d'intégration de la couche Business
 */
public abstract class ConsumerTestCase {

    static {
        SpringRegistry.init();
    }

    /** {@link DaoProxy} */
    private static final DaoProxy DAO_PROXY = SpringRegistry.getDaoProxy();
    private static final DataSource DATA_SOURCE_MYERP = SpringRegistry.getDataSource();



    // ==================== Constructeurs ====================
    /**
     * Constructeur.
     */
    public ConsumerTestCase() {
    }


    // ==================== Getters/Setters ====================
    public static DaoProxy getDaoProxy() {
        return DAO_PROXY;
    }
    public static DataSource getDataSource() {
        return DATA_SOURCE_MYERP;
    }


}
