package com.dummy.myerp.testconsumer.consumer;

import com.dummy.myerp.consumer.dao.impl.db.dao.ComptabiliteDaoImpl;
import com.dummy.myerp.consumer.db.DataSourcesEnum;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.assertj.core.api.ObjectAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import static org.assertj.core.api.Assertions.*;


public class ComptabiliteDaoImplIntegrationTest extends ConsumerTestCase {

    ComptabiliteDaoImpl classUnderTest;
    EcritureComptable vEcritureComptable;

    int year =0;
    String yearInString = null;

    @Before
    public void init() throws ParseException {
        vEcritureComptable = new EcritureComptable();
        classUnderTest = ComptabiliteDaoImpl.getInstance();

        // Set current year
        Calendar now = Calendar.getInstance();
        year = now.get(Calendar.YEAR);
        yearInString = String.valueOf(year);

        // Set a valid EcritureComptable
        vEcritureComptable = createValidEcritureComptable(yearInString);
    }

    @After
    public void undef() {

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
    /*             insertEcritureComptable Integration tests                    */
    /*==========================================================================*/

    @Test
    public void Given_validEcritureComptableToInsert_When_insertecritureComptableIsUsed_Then_shouldReturnTrue() throws NotFoundException, ParseException {
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

            // Check beans equality
            assertThat(resultInString).isEqualTo(vEcritureComptableInString);
    }

}
