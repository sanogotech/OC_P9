package com.dummy.myerp.business.impl.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Calendar;
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
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ComptabiliteManagerImplTest {

    @Mock
    ComptabiliteManagerImpl classUnderTest;
    @Mock
    EcritureComptable vEcritureComptable;

    @Before
    public void initBeforeEach() {
        classUnderTest = new ComptabiliteManagerImpl();
        vEcritureComptable = new EcritureComptable();

        // Set valid EcritureComptable
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
    public void Given_validEcritureComptable_When_checkEcritureComptableUnitIsUsed_Then_shouldReturnTrue() throws FunctionalException {
        // GIVEN
        MockitoAnnotations.initMocks(this);
        when(classUnderTest.checkEcritureComptableValidation(vEcritureComptable)).thenReturn(true);
        when(classUnderTest.checkEcritureComptableEquilibre(vEcritureComptable)).thenReturn(true);
        when(classUnderTest.checkEcritureComptableLines(vEcritureComptable)).thenReturn(true);
        when(classUnderTest.checkReference(vEcritureComptable)).thenReturn(true);
        when(classUnderTest.checkEcritureComptableUnit(vEcritureComptable)).thenCallRealMethod();
        // WHEN
        final Boolean result = classUnderTest.checkEcritureComptableUnit(vEcritureComptable);
        // THEN
        assertThat(result).isTrue();
    }

    @Test(expected = FunctionalException.class)
    public void Given_invalidEcritureComptable_When_checkEcritureComptableUnitIsUsed_Then_shouldThrowFunctionalException() throws FunctionalException {
        // GIVEN
        MockitoAnnotations.initMocks(this);
        when(classUnderTest.checkEcritureComptableValidation(vEcritureComptable)).thenThrow(FunctionalException.class);
        when(classUnderTest.checkEcritureComptableUnit(vEcritureComptable)).thenCallRealMethod();
        // WHEN
        classUnderTest.checkEcritureComptableUnit(vEcritureComptable);
    }


    /*==========================================================================*/
    /*              checkEcritureComptable Validation unit tests                */
    /*==========================================================================*/


    @Test
    public void Given_validEcritureComptable_When_checkEcritureComptableUnitValidationIsUsed_Then_shouldReturnTrue()
            throws FunctionalException {

        // WHEN
        Boolean result = classUnderTest.checkEcritureComptableValidation(vEcritureComptable);

        // THEN
        assertThat(result).isTrue();
    }

    @Test(expected = FunctionalException.class)
    public void Given_ecritureComptableWithJournalComptableEqualNull_When_checkEcritureComptableUnitValidationIsUsed_Then_shouldThrowsFunctionalException()
            throws FunctionalException {
        // GIVEN
        vEcritureComptable.setJournal(null);
        // WHEN
        classUnderTest.checkEcritureComptableValidation(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void Given_ecritureComptableWithReferenceEqual0_When_checkEcritureComptableUnitValidationIsUsed_Then_shouldThrowsFunctionalException()
            throws FunctionalException {
        // GIVEN
        vEcritureComptable.setReference("0");
        // WHEN
        classUnderTest.checkEcritureComptableValidation(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void Given_ecritureComptableWithDateEqualNull_When_checkEcritureComptableUnitValidationIsUsed_Then_shouldThrowsFunctionalException()
            throws FunctionalException {
        // GIVEN
        vEcritureComptable.setDate(null);
        // WHEN
        classUnderTest.checkEcritureComptableValidation(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void Given_ecritureComptableWithLibelleEqualNull_When_checkEcritureComptableUnitValidationIsUsed_Then_shouldThrowsFunctionalException()
            throws FunctionalException {
        // GIVEN
        vEcritureComptable.setLibelle(null);
        ;
        // WHEN
        classUnderTest.checkEcritureComptableValidation(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void Given_ecritureComptableWithLibelleEqualEmpty_When_checkEcritureComptableUnitValidationIsUsed_Then_shouldThrowsFunctionalException()
            throws FunctionalException {
        // GIVEN
        vEcritureComptable.setLibelle(
                "");
        // WHEN
        classUnderTest.checkEcritureComptableValidation(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void Given_ecritureComptableWithLibelleSizeIsGreaterThan200Empty_When_checkEcritureComptableUnitValidationIsUsed_Then_shouldThrowsFunctionalException()
            throws FunctionalException {
        // GIVEN
        vEcritureComptable.setLibelle(
                "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        // WHEN
        classUnderTest.checkEcritureComptableValidation(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void Given_ecritureComptableWithOneLigneComptable_When_checkEcritureComptableUnitValidationIsUsed_Then_shouldThrowsFunctionalException()
            throws FunctionalException {
        // GIVEN
        vEcritureComptable.getListLigneEcriture().remove(1);
        // WHEN
        classUnderTest.checkEcritureComptableValidation(vEcritureComptable);
    }

    @Test
    public void Given_ecritureComptableWithOneLigneComptable_When_checkEcritureComptableUnitValidationIsUsed_Then_shouldReturnCorrectFunctionnalExceptionMessage() {
        // GIVEN
        vEcritureComptable.getListLigneEcriture().remove(1);

        String expectedMessage = "L'écriture comptable ne respecte pas les règles de gestion.";
        String thrownMessage = null;
        // WHEN
        try {
            classUnderTest.checkEcritureComptableValidation(vEcritureComptable);
        } catch (FunctionalException e) {
            thrownMessage = e.getLocalizedMessage();
        }
        // THEN
        assertThat(thrownMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void Given_ecritureComptableWithOneLigneComptable_When_checkEcritureComptableUnitValidationIsUsed_Then_shouldReturnCorrectConstraintViolationExceptionMessage() {
        // GIVEN
        vEcritureComptable.getListLigneEcriture().remove(1);

        String expectedMessage = "L'écriture comptable ne respecte pas les contraintes de validation";
        String thrownMessage = null;
        // WHEN
        try {
            classUnderTest.checkEcritureComptableValidation(vEcritureComptable);
        } catch (FunctionalException e) {
            thrownMessage = e.getCause().getLocalizedMessage();
        }
        // THEN
        assertThat(thrownMessage).isEqualTo(expectedMessage);
    }

    /*==========================================================================*/
    /*               checkEcritureComptableEquilibre unit tests                 */
    /*==========================================================================*/

    @Test
    public void Given_BalancedEcritureComptable_When_isEquilibreeIsUsed_Then_ShouldReturnTrue() throws FunctionalException {
        // GIVEN
        MockitoAnnotations.initMocks(this);
        when(vEcritureComptable.isEquilibree()).thenReturn(true);
        when(classUnderTest.checkEcritureComptableEquilibre(vEcritureComptable)).thenCallRealMethod();
        // WHEN
        final Boolean result = classUnderTest.checkEcritureComptableEquilibre(vEcritureComptable);
        // THEN
        assertThat(result).isTrue();

    }

    @Test(expected = FunctionalException.class)
    public void Given_UnbalancedEcritureComptable_When_isEquilibreeIsUsed_Then_ShouldThrowFunctionalException() throws FunctionalException {
        // GIVEN
        MockitoAnnotations.initMocks(this);
        when(vEcritureComptable.isEquilibree()).thenReturn(false);
        when(classUnderTest.checkEcritureComptableEquilibre(vEcritureComptable)).thenCallRealMethod();
        // WHEN
        classUnderTest.checkEcritureComptableEquilibre(vEcritureComptable);
    }

    @Test
    public void Given_UnbalancedEcritureComptable_When_isEquilibreeIsUsed_Then_shouldReturnCorrectExceptionMessage() throws FunctionalException {
        // GIVEN
        MockitoAnnotations.initMocks(this);
        when(vEcritureComptable.isEquilibree()).thenReturn(false);
        when(classUnderTest.checkEcritureComptableEquilibre(vEcritureComptable)).thenCallRealMethod();

        String expectedMessage = "L'écriture comptable n'est pas équilibrée.";
        String thrownMessage = null;
        // WHEN
        try {
            classUnderTest.checkEcritureComptableEquilibre(vEcritureComptable);
        } catch (FunctionalException e) {
            thrownMessage = e.getLocalizedMessage();
        }
        // THEN
        assertThat(thrownMessage).isEqualTo(expectedMessage);
    }

    /*==========================================================================*/
    /*                 checkEcritureComptableLines  unit tests                  */
    /*==========================================================================*/

    @Test
    public void Given_validEcritureComptable_When_checkEcritureComptableLinesIsUsed_Then_shouldReturnTrue() throws FunctionalException {
        // WHEN
        final Boolean result = classUnderTest.checkEcritureComptableLines(vEcritureComptable);
        // THEN
        assertThat(result).isTrue();
    }

    @Test(expected = FunctionalException.class)
    public void Given_invalidEcritureComptableWithOnlyOneLine_When_checkEcritureComptableLinesIsUsed_Then_shouldThrowFunctionalException() throws FunctionalException {
        // GIVEN
        vEcritureComptable.getListLigneEcriture().remove(1);
        // WHEN
        classUnderTest.checkEcritureComptableLines(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void Given_invalidEcritureComptableWithTwoCreditLines_When_checkEcritureComptableLinesIsUsed_Then_shouldThrowFunctionalException() throws FunctionalException {
        // GIVEN
        vEcritureComptable.getListLigneEcriture().get(0).setDebit(null);
        vEcritureComptable.getListLigneEcriture().get(0).setCredit(new BigDecimal(100));
        // WHEN
        classUnderTest.checkEcritureComptableLines(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void Given_invalidEcritureComptableWithTwoDebitLines_When_checkEcritureComptableLinesIsUsed_Then_shouldThrowFunctionalException() throws FunctionalException {
        // GIVEN
        vEcritureComptable.getListLigneEcriture().get(1).setCredit(null);
        vEcritureComptable.getListLigneEcriture().get(1).setDebit(new BigDecimal(100));
        // WHEN
        classUnderTest.checkEcritureComptableLines(vEcritureComptable);
    }

    @Test
    public void Given_invalidEcritureComptableWithTwoDebitLines_When_checkEcritureComptableLinesIsUsed_Then_shouldReturnCorrectExceptionMessage() {
        // GIVEN
        vEcritureComptable.getListLigneEcriture().get(1).setCredit(null);
        vEcritureComptable.getListLigneEcriture().get(1).setDebit(new BigDecimal(100));

        String expectedMessage = "L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit.";
        String thrownMessage = null;
        // WHEN
        try {
            classUnderTest.checkEcritureComptableLines(vEcritureComptable);
        } catch (FunctionalException e) {
            thrownMessage = e.getLocalizedMessage();
        }
        // THEN
        assertThat(thrownMessage).isEqualTo(expectedMessage);
    }

    /*==========================================================================*/
    /*                        checkReference unit tests                         */
    /*==========================================================================*/

    @Test
    public void Given_validReference_When_checkReferenceIsUsed_Then_shouldReturnTrue() throws FunctionalException {
        // GIVEN
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        String yearInString = String.valueOf(year);
        vEcritureComptable.setReference("AC-" + yearInString + "/00001");
        // WHEN
        final Boolean result = classUnderTest.checkReference(vEcritureComptable);
        // THEN
        assertThat(result).isTrue();

    }

    @Test(expected = FunctionalException.class)
    public void Given_invalidReferenceCodeDoesNotMatch_When_checkReferenceIsUsed_Then_shouldthrowFunctionnalException() throws FunctionalException {
        // GIVEN
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        String yearInString = String.valueOf(year);
        vEcritureComptable.setReference("AC-" + yearInString + "/00001");
        vEcritureComptable.getJournal().setCode("ABC");
        // WHEN
        classUnderTest.checkReference(vEcritureComptable);
    }

    @Test
    public void Given_invalidReferenceCodeDoesNotMatch_When_checkReferenceIsUsed_Then_shouldReturnCorrectExceptionMessage() {
        // GIVEN
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        String yearInString = String.valueOf(year);
        vEcritureComptable.setReference("AC-" + yearInString + "/00001");
        vEcritureComptable.getJournal().setCode("ABC");

        String expectedMessage = "La référence de l'écriture comptable doit contenir le code correspondant au journal associé.";
        String thrownMessage = null;
        // WHEN
        try {
            classUnderTest.checkReference(vEcritureComptable);
        } catch (FunctionalException e) {
            thrownMessage = e.getLocalizedMessage();
        }
        // THEN
        assertThat(thrownMessage).isEqualTo(expectedMessage);
    }

    @Test(expected = FunctionalException.class)
    public void Given_invalidReferenceYearDoesNotMatch_When_checkReferenceIsUsed_Then_shouldthrowFunctionnalException() throws FunctionalException {
        // WHEN
        classUnderTest.checkReference(vEcritureComptable);

    }

    @Test
    public void Given_invalidReferenceYearDoesNotMatch_When_checkReferenceIsUsed_Then_shouldReturnCorrectExceptionMessage() {
        // GIVEN
        String expectedMessage = "La référence de l'écriture comptable doit contenir l'année courante.";
        String thrownMessage = null;
        // WHEN
        try {
            classUnderTest.checkReference(vEcritureComptable);
        } catch (FunctionalException e) {
            thrownMessage = e.getLocalizedMessage();
        }
        // THEN
        assertThat(thrownMessage).isEqualTo(expectedMessage);
    }

    }
