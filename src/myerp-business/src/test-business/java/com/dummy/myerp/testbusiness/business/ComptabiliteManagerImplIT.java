package com.dummy.myerp.testbusiness.business;

import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ComptabiliteManagerImplIT {


    ComptabiliteManagerImpl classUndertest;
    @Mock
    EcritureComptable ecritureComptable;

    @Before
    public void init() {
        classUndertest = new ComptabiliteManagerImpl();
        ecritureComptable = new EcritureComptable();
    }

    @After
    public void undef() {
        classUndertest = null;
    }




}
