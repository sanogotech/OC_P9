package com.dummy.myerp.testbusiness.business;

import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.business.impl.TransactionManager;
import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ComptabiliteManagerImplIT extends BusinessTestCase{


    @Spy
    ComptabiliteManagerImpl classUnderTest;
    @Spy
    EcritureComptable vEcritureComptable;
    @Mock
    DaoProxy mockDaoProxy;
    @Mock
    TransactionManager mockTransactionManager;
    @Mock
    ComptabiliteDao mockComptabiliteDao;

    int year =0;
    String yearInString = null;

    @Before
    public void initBeforeEach() {
        vEcritureComptable = new EcritureComptable();
        classUnderTest = new ComptabiliteManagerImpl();

        MockitoAnnotations.initMocks(this);
        classUnderTest.configure(null, mockDaoProxy, mockTransactionManager);
        when(mockDaoProxy.getComptabiliteDao()).thenReturn(mockComptabiliteDao);


        // Set current year
        Calendar now = Calendar.getInstance();
        year = now.get(Calendar.YEAR);
        yearInString = String.valueOf(year);

        // Set a valid EcritureComptable
        vEcritureComptable = createValidEcritureComptable(yearInString);


    }

    public EcritureComptable createValidEcritureComptable(String yearInString) {
        EcritureComptable ecritureComptable = new EcritureComptable();

        // Set valid EcritureComptable
        ecritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        ecritureComptable.setDate(new Date());
        ecritureComptable.setLibelle("Libelle");
        ecritureComptable.getListLigneEcriture()
                .add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
        ecritureComptable.getListLigneEcriture()
                .add(new LigneEcritureComptable(new CompteComptable(2), null, null, new BigDecimal(123)));
        ecritureComptable.setReference("AC-" + yearInString + "/00001");

        return ecritureComptable;
    }

    @After
    public void undefAfterEach() {
        classUnderTest = null;
        vEcritureComptable = null;
    }

    /*==========================================================================*/
    /*            getEcritureComptableByRef Integration tests                   */
    /*==========================================================================*/

    @Test
    public void Given_EcritureComptableWithExistingReferenceInDataBase_When_getEcritureComptableByRefIsUsed_Then_shouldReturnEcritureComptableWithSameReference() throws NotFoundException {
        // GIVEN
        when(mockComptabiliteDao.getEcritureComptableByRef(anyString())).thenReturn(vEcritureComptable);
        // WHEN
        final EcritureComptable result = classUnderTest.getEcritureComptableByRef(vEcritureComptable);
        // THEN
        assertThat(result.getReference()).isEqualTo(vEcritureComptable.getReference());
    }

    @Test
    public void Given_EcritureComptableWithExistingReferenceInDataBase_When_getEcritureComptableByRefIsUsed_Then_shouldReturnEcritureComptable() throws NotFoundException {
        // GIVEN
        when(mockComptabiliteDao.getEcritureComptableByRef(anyString())).thenReturn(vEcritureComptable);
        // WHEN
        final EcritureComptable result = classUnderTest.getEcritureComptableByRef(vEcritureComptable);
        // THEN
        assertThat(result).isInstanceOf(EcritureComptable.class);
    }

    @Test(expected = NotFoundException.class)
    public void Given__When__Then2() throws NotFoundException {
        // GIVEN
        when(mockComptabiliteDao.getEcritureComptableByRef(anyString())).thenThrow(NotFoundException.class);
        // WHEN
        classUnderTest.getEcritureComptableByRef(vEcritureComptable);
    }


}
