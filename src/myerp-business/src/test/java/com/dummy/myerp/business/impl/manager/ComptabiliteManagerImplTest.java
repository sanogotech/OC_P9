package com.dummy.myerp.business.impl.manager;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.consumer.dao.impl.DaoProxyImpl;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

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

    /*==========================================================================*/
    /*         setSequenceEcritureComptableUpdatedValue unit tests              */
    /*==========================================================================*/

    @Test
    public void Given_actualSequenceValueEquals0_When_setSequenceEcritureComptableUpdatedValueIsUsed_Then_shouldReturn1() {
        // GIVEN
        int sequenceValue = 0;
        // WHEN
        final int result = classUnderTest.setSequenceEcritureComptableUpdatedValue(sequenceValue);
        // THEN
        assertThat(result).isEqualTo(1);
    }

    @Test
    public void Given_actualSequenceValueEquals50_When_setSequenceEcritureComptableUpdatedValueIsUsed_Then_shouldReturn51() {
        // GIVEN
        int sequenceValue = 50;
        // WHEN
        final int result = classUnderTest.setSequenceEcritureComptableUpdatedValue(sequenceValue);
        // THEN
        assertThat(result).isEqualTo(51);
    }

    /*==========================================================================*/
    /*             createEcritureComptableReference unit tests                  */
    /*==========================================================================*/

    @Test
    public void Given_sequenceValueEquals1_When_createEcritureComptableReferenceIsUsed_Then_shouldReturn00001() {
        // GIVEN
        int sequenceValue = 1;
        String codeJOurnal = "AB";
        String yearInString = "2020";
        // WHEN
        final String result = classUnderTest.createEcritureComptableReference(codeJOurnal,yearInString,sequenceValue);
        // THEN
        assertThat(result).isEqualTo("AB-2020/00001");

    }

    @Test
    public void Given_sequenceValueEquals99999_When_createEcritureComptableReferenceIsUsed_Then_shouldReturn99999() {
        // GIVEN
        int sequenceValue = 99999;
        String codeJOurnal = "AB";
        String yearInString = "2020";
        // WHEN
        final String result = classUnderTest.createEcritureComptableReference(codeJOurnal,yearInString,sequenceValue);
        // THEN
        assertThat(result).isEqualTo("AB-2020/99999");
    }

    /*==========================================================================*/
    /*          persistSequenceEcritureComptableValue unit tests                */
    /*==========================================================================*/

    @Test
    public void Given_actualSequenceValue0_When_persistSequenceEcritureComptableValueIsUsed_Then_shouldReturn1() {
        // GIVEN
        int sequenceValue = 0;
        String codeJournal = "AB";
        int year = 2020;

        MockitoAnnotations.initMocks(this);
        when(classUnderTest.insertSequenceEcritureComptable(anyString(), any(SequenceEcritureComptable.class))).thenReturn(1);
        when(classUnderTest.persistSequenceEcritureComptableValue(sequenceValue,1,codeJournal, year)).thenCallRealMethod();
        // WHEN
        final int result = classUnderTest.persistSequenceEcritureComptableValue(sequenceValue,1,codeJournal, new Integer(year));
        // THEN
        assertThat(result).isEqualTo(1);

    }

    @Test
    public void Given_actualSequenceValue1_When_persistSequenceEcritureComptableValueIsUsed_Then_shouldReturn1() {
        // GIVEN
        int sequenceValue = 1;
        String codeJournal = "AB";
        int year = 2020;

        MockitoAnnotations.initMocks(this);
        when(classUnderTest.updateSequenceEcritureComptable(anyString(), any(SequenceEcritureComptable.class))).thenReturn(1);
        when(classUnderTest.persistSequenceEcritureComptableValue(sequenceValue,1,codeJournal, year)).thenCallRealMethod();
        // WHEN
        final int result = classUnderTest.persistSequenceEcritureComptableValue(sequenceValue,1,codeJournal, new Integer(year));
        // THEN
        assertThat(result).isEqualTo(1);

    }

    /*==========================================================================*/
    /*              checkEcritureComptableContext unit tests                    */
    /*==========================================================================*/



    @Test(expected = FunctionalException.class)
    public void Given_EcritureComptableIDIsNull_When_checkEcritureComptableContextIsUsed_Then_shouldThrowFunctionnalException() throws FunctionalException {
        // GIVEN
        MockitoAnnotations.initMocks(this);
        when(vEcritureComptable.getReference()).thenReturn("test");
        when(vEcritureComptable.getId()).thenReturn(null);
        doCallRealMethod().when(classUnderTest).checkEcritureComptableContext(vEcritureComptable);
        // WHEN
        classUnderTest.checkEcritureComptableContext(vEcritureComptable);

    }

    @Test(expected = FunctionalException.class)
    public void Given_EcritureComptableIDIsTheSameAsTheIdRecordedInDBForTheSameRef_When_checkEcritureComptableContextIsUsed_Then_shouldThrowFunctionnalException() throws FunctionalException, NotFoundException {
        // GIVEN
        MockitoAnnotations.initMocks(this);
        EcritureComptable eCFromDb = new EcritureComptable();
        eCFromDb.setId(1);
        when(vEcritureComptable.getReference()).thenReturn("test");
        when(vEcritureComptable.getId()).thenReturn(2);
        when(classUnderTest.getEcritureComptableByRef(vEcritureComptable)).thenReturn(eCFromDb);
        doCallRealMethod().when(classUnderTest).checkEcritureComptableContext(vEcritureComptable);
        // WHEN
        classUnderTest.checkEcritureComptableContext(vEcritureComptable);

    }

}
