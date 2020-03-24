package com.dummy.myerp.business.impl.manager;

import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigDecimal;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;

public class ComptabiliteManagerImplTest {

    ComptabiliteManagerImpl classUnderTest;
    EcritureComptable vEcritureComptable;

    @Before
    public void initBeforeEach() {
        classUnderTest = new ComptabiliteManagerImpl();
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture()
                .add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
        vEcritureComptable.getListLigneEcriture()
                .add(new LigneEcritureComptable(new CompteComptable(2), null, null, new BigDecimal(123)));
        vEcritureComptable.setReference("AC-2016/00001");
    }

    @After
    public void undefAfterEach() {
        classUnderTest = null;
        vEcritureComptable = null;
    }

    @Test
    @Ignore
    public void checkEcritureComptableUnit() throws Exception {
        classUnderTest.checkEcritureComptableUnit(vEcritureComptable);
    }

    /*
     * @Test(expected = FunctionalException.class)
     * 
     * @Ignore public void checkEcritureComptableUnitViolation() throws Exception {
     * EcritureComptable vEcritureComptable; vEcritureComptable = new
     * EcritureComptable();
     * classUnderTest.checkEcritureComptableUnit(vEcritureComptable); }
     */

    @Test(expected = FunctionalException.class)
    @Ignore
    public void checkEcritureComptableUnitRG2() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture()
                .add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
        vEcritureComptable.getListLigneEcriture()
                .add(new LigneEcritureComptable(new CompteComptable(2), null, null, new BigDecimal(1234)));
        classUnderTest.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    @Ignore
    public void checkEcritureComptableUnitRG3() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture()
                .add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
        vEcritureComptable.getListLigneEcriture()
                .add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
        classUnderTest.checkEcritureComptableUnit(vEcritureComptable);
    }

    /*
     * public void addReferenceTest() { // GIVEN EcritureComptable ecritureComptable
     * = new EcritureComptable(); ecritureComptable.setJournal(new
     * JournalComptable("CC", "Achat fournitures"));
     * ecritureComptable.setDate(Calendar.getInstance().getTime());
     * 
     * ComptabiliteManager comptabiliteManager= new ComptabiliteManagerImpl();
     * 
     * int journalRecordCount = 5; // WHEN
     * 
     * // THEN
     * 
     * }
     */

    @Test
    public void Given_validEcritureComptable_When_checkEcritureComptableUnitViolationsIsUsed_Then_shouldReturnTrue()
            throws FunctionalException {

        // WHEN
        Boolean result = classUnderTest.checkEcritureComptableUnit(vEcritureComptable);

        // THEN
        assertThat(result).isTrue();
    }

    @Test(expected = FunctionalException.class)
    public void Given_ecritureComptableWithJournalComptableEqualNull_When_checkEcritureComptableUnitViolationsIsUsed_Then_shouldThrowsFunctionalException()
            throws FunctionalException {
        // GIVEN
        vEcritureComptable.setJournal(null);
        // WHEN
        classUnderTest.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void Given_ecritureComptableWithReferenceEqual0_When_checkEcritureComptableUnitViolationsIsUsed_Then_shouldThrowsFunctionalException()
            throws FunctionalException {
        // GIVEN
        vEcritureComptable.setReference("0");
        // WHEN
        classUnderTest.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void Given_ecritureComptableWithDateEqualNull_When_checkEcritureComptableUnitViolationsIsUsed_Then_shouldThrowsFunctionalException()
            throws FunctionalException {
        // GIVEN
        vEcritureComptable.setDate(null);
        // WHEN
        classUnderTest.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void Given_ecritureComptableWithLibelleEqualNull_When_checkEcritureComptableUnitViolationsIsUsed_Then_shouldThrowsFunctionalException()
            throws FunctionalException {
        // GIVEN
        vEcritureComptable.setLibelle(null);
        ;
        // WHEN
        classUnderTest.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void Given_ecritureComptableWithLibelleEqualEmpty_When_checkEcritureComptableUnitViolationsIsUsed_Then_shouldThrowsFunctionalException()
            throws FunctionalException {
        // GIVEN
        vEcritureComptable.setLibelle(
                "");
        // WHEN
        classUnderTest.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void Given_ecritureComptableWithLibelleSizeIsGreaterThan200Empty_When_checkEcritureComptableUnitViolationsIsUsed_Then_shouldThrowsFunctionalException()
            throws FunctionalException {
        // GIVEN
        vEcritureComptable.setLibelle(
                "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        // WHEN
        classUnderTest.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void Given_ecritureComptableWithOneLigneComptable_When_checkEcritureComptableUnitViolationsIsUsed_Then_shouldThrowsFunctionalException()
            throws FunctionalException {
        // GIVEN
        vEcritureComptable.getListLigneEcriture().remove(1);
        // WHEN
        classUnderTest.checkEcritureComptableUnit(vEcritureComptable);
    }

}
