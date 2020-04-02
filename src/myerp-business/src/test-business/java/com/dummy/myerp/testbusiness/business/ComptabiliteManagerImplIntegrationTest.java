package com.dummy.myerp.testbusiness.business;

import com.dummy.myerp.business.impl.TransactionManager;
import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.consumer.db.DataSourcesEnum;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.transaction.TransactionStatus;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ComptabiliteManagerImplIntegrationTest extends BusinessTestCase {


   /* @Spy*/
    ComptabiliteManagerImpl classUnderTest;
    /*@Spy*/
    EcritureComptable vEcritureComptable;
    /*@Spy
    DaoProxy mockDaoProxy;
    @Spy
    TransactionManager mockTransactionManager;
    @Spy
    ComptabiliteDao mockComptabiliteDao;*/

    int year =0;
    String yearInString = null;

    @Before
    public void initBeforeEach() throws ParseException {
        vEcritureComptable = new EcritureComptable();
        classUnderTest = new ComptabiliteManagerImpl();

        /*MockitoAnnotations.initMocks(this);
        classUnderTest.configure(null, mockDaoProxy, mockTransactionManager);
        when(mockDaoProxy.getComptabiliteDao()).thenReturn(mockComptabiliteDao);*/

        // Set current year
        Calendar now = Calendar.getInstance();
        year = now.get(Calendar.YEAR);
        yearInString = String.valueOf(year);

        // Set a valid EcritureComptable
        vEcritureComptable = createValidEcritureComptable(yearInString);
    }

    @After
    public void undefAfterEach() {
        classUnderTest = null;
        vEcritureComptable = null;
    }

    public EcritureComptable createValidEcritureComptable(String yearInString) throws ParseException {
        EcritureComptable ecritureComptable = new EcritureComptable();
        BigDecimal value = new BigDecimal(123);
        value = value.setScale(2);
        // Set valid EcritureComptable
        ecritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        ecritureComptable.setDate(formatDate(new Date()));
        ecritureComptable.setLibelle("Libelle");
        ecritureComptable.getListLigneEcriture()
                .add(new LigneEcritureComptable(new CompteComptable(401, "Fournisseurs"), "toto", value, null));
        ecritureComptable.getListLigneEcriture()
                .add(new LigneEcritureComptable(new CompteComptable(401, "Fournisseurs"), "tutu", null, value));
        ecritureComptable.setReference("AC-" + yearInString + "/00001");

        return ecritureComptable;
    }

    public Date formatDate(Date date) throws ParseException {
        // Format date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateInString = dateFormat.format(date);

        Date formatedDate = dateFormat.parse(dateInString);

        return formatedDate;
    }



    /*==========================================================================*/
    /*            getEcritureComptableByRef Integration tests                   */
    /*==========================================================================*/

    @Test
    public void Given_ExistingReferenceInDataBase_When_getEcritureComptableByRefIsUsed_Then_shouldReturnEcritureComptableWithSameReference() throws NotFoundException {
        // GIVEN
        String reference = "AC-2016/00001";
        // WHEN
        final EcritureComptable result = classUnderTest.getEcritureComptableByRef(reference);
        // THEN
        assertThat(result.getReference()).isEqualTo(reference);
    }

    @Test
    public void Given_EcritureComptableWithExistingReferenceInDataBase_When_getEcritureComptableByRefIsUsed_Then_shouldReturnEcritureComptable() throws NotFoundException {
        // GIVEN
        String reference = "AC-2016/00001";
        // WHEN
        final EcritureComptable result = classUnderTest.getEcritureComptableByRef(reference);
        // THEN
        assertThat(result).isInstanceOf(EcritureComptable.class);
    }


    @Test(expected = NotFoundException.class)
    public void Given_noRecordedInDataBaseEcritureComptable_When_getEcritureComptableByRefIsUsed_Then_shouldReturnNotFoundException() throws NotFoundException {
        // GIVEN
        String reference = "test";
        // WHEN
        classUnderTest.getEcritureComptableByRef(reference);
    }

    /*==========================================================================*/
    /*             insertEcritureComptable Integration tests                    */
    /*==========================================================================*/

    @Test
    public void Given_newValidEcritureComptable_When_insertEcritureComptableIsUsed_Then_recordedBeanTheSameAsTheGivenBean() throws FunctionalException, NotFoundException, ParseException {
        // GIVEN

        // WHEN
        classUnderTest.insertEcritureComptable(vEcritureComptable);
        // THEN

            // Get the recorded bean
            EcritureComptable result = classUnderTest.getEcritureComptableByRef(vEcritureComptable.getReference());

            // Clean DB
            classUnderTest.deleteEcritureComptable(result.getId());

            // Format beans to compare
            vEcritureComptable.setId(result.getId());
            result.setDate(formatDate(result.getDate()));

            // Translate beans to String
            String resultInString = result.toString();
            String vEcritureComptableInString = vEcritureComptable.toString();

            // check beans equality
            assertThat(resultInString).isEqualTo(vEcritureComptableInString);
    }
}
