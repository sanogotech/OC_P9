package com.dummy.myerp.testbusiness.business;

import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Test(expected = FunctionalException.class)
    public void Given_invalidEcritureComptable_When_insertEcritureComptableIsUsed_Then_shouldReturnFunctionnalException() throws FunctionalException, NotFoundException, ParseException {
        // GIVEN

        // Change journal code to invalid bean
        vEcritureComptable.getJournal().setCode("VE");
        // WHEN
        classUnderTest.insertEcritureComptable(vEcritureComptable);
    }

    @Test
    public void Given_ecritureComptableWithUnknowJournalCode_When_insertEcritureComptableIsUsed_Then_shouldNotInsert() throws FunctionalException, NotFoundException, ParseException {
        // GIVEN
        List<EcritureComptable> records = classUnderTest.getListEcritureComptable();

        // Change bean to invalid it
        vEcritureComptable.setReference("TOTO-2020/00001");
        vEcritureComptable.getJournal().setCode("TOTO");

        // WHEN
        try {
            classUnderTest.insertEcritureComptable(vEcritureComptable);
        } catch (DataIntegrityViolationException e){}

        // THEN
        List<EcritureComptable> result = classUnderTest.getListEcritureComptable();
        assertThat(result.size()).isEqualTo(records.size());
    }

    /*==========================================================================*/
    /*             updateEcritureComptable Integration tests                    */
    /*==========================================================================*/

    @Test
    public void Given_modifiedBean_When_updateEcritureComptableIsUsed_Then_shouldReturnModifiedBean() throws FunctionalException, NotFoundException, ParseException {
        // GIVEN
        String reference = "AC-2016/00001";
        EcritureComptable beanToUpdate = classUnderTest.getEcritureComptableByRef(reference);

        EcritureComptable modifiedBeanToUpdate = new EcritureComptable();
        modifiedBeanToUpdate.setId(beanToUpdate.getId());
        modifiedBeanToUpdate.setJournal(beanToUpdate.getJournal());
        modifiedBeanToUpdate.setDate(beanToUpdate.getDate());
        modifiedBeanToUpdate.setReference(beanToUpdate.getReference());
        modifiedBeanToUpdate.setLibelle("Ramette papier A4");

        // WHEN
        classUnderTest.updateEcritureComptable(modifiedBeanToUpdate);
        // THEN
        EcritureComptable result = classUnderTest.getEcritureComptableByRef(reference);

        // Restore initial data
        classUnderTest.updateEcritureComptable(beanToUpdate);

        assertThat(result.toString()).isEqualTo(modifiedBeanToUpdate.toString());
    }

    @Test
    public void Given_invalidModifiedBean_When_updateEcritureComptableIsUsed_Then_shouldModifiedNothing() throws FunctionalException, NotFoundException, ParseException {
        // GIVEN
        String reference = "AC-2016/00001";
        EcritureComptable beanToUpdate = classUnderTest.getEcritureComptableByRef(reference);

        EcritureComptable modifiedBeanToUpdate = new EcritureComptable();
        modifiedBeanToUpdate.setId(beanToUpdate.getId());
        modifiedBeanToUpdate.setJournal(new JournalComptable("TOTO","test"));
        modifiedBeanToUpdate.setDate(beanToUpdate.getDate());
        modifiedBeanToUpdate.setReference("TOTO-2020/00001");
        modifiedBeanToUpdate.setLibelle("Ramette papier A4");

        // WHEN
        try {
            classUnderTest.updateEcritureComptable(modifiedBeanToUpdate);
        } catch (DataIntegrityViolationException e){

        }

        // THEN
        EcritureComptable result = classUnderTest.getEcritureComptableByRef(reference);

        assertThat(result.toString()).isEqualTo(beanToUpdate.toString());
    }

    /*==========================================================================*/
    /*             deleteEcritureComptable Integration tests                    */
    /*==========================================================================*/

    @Test(expected = NotFoundException.class)
    public void Given_addedEcritureComptableWithid_When_deleteEcritureComptableIsUsed_Then_beanAddedShouldBeDeleted() throws FunctionalException, NotFoundException {
        // GIVEN
        classUnderTest.insertEcritureComptable(vEcritureComptable);
        EcritureComptable beanAdded = classUnderTest.getEcritureComptableByRef(vEcritureComptable.getReference());
        // WHEN
        classUnderTest.deleteEcritureComptable(beanAdded.getId());
        // THEN
        classUnderTest.getEcritureComptableByRef(vEcritureComptable.getReference());
    }

    @Test
    public void Given_wrongRecordId_When_deleteEcritureComptableIsUsed_Then_shouldDeleteNothing() {
        // GIVEN
        List<EcritureComptable> records = classUnderTest.getListEcritureComptable();
        // WHEN
        classUnderTest.deleteEcritureComptable(100);
        // THEN
        List<EcritureComptable> recordsAfterDelete = classUnderTest.getListEcritureComptable();
        assertThat(records.size()).isEqualTo(recordsAfterDelete.size());
    }

    /*==========================================================================*/
    /*        getSequenceEcritureComptableLastValue Integration tests           */
    /*==========================================================================*/

    @Test
    public void Given_codeJournalAndYear_When_getSequenceEcritureComptableLastValueIsUsed_Then_shouldReturn40() {
        // GIVEN
        String codeJournal ="AC";
        Integer year = 2016;
        // WHEN
        final Integer result = classUnderTest.getSequenceEcritureComptableLastValue(codeJournal, year);
        // THEN
        assertThat(result).isEqualTo(40);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void Given_codeJournalAndInvaliYear_When_getSequenceEcritureComptableLastValueIsUsed_Then_shouldThrowEmptyResultDataAccessException() {
        // GIVEN
        String codeJournal ="AC";
        Integer year = 1900;
        // WHEN
        classUnderTest.getSequenceEcritureComptableLastValue(codeJournal, year);


    }

    /*==========================================================================*/
    /*      insertSequenceEcritureComptableLastValue Integration tests          */
    /*==========================================================================*/

    @Test
    public void Given_validCodeJournalAndSequence_When_insertSequenceEcritureComptableIsUsed_Then_shouldBeanSequenceValue() {
        // GIVEN
        String codeJournal = "AC";
        SequenceEcritureComptable sequenceToAdd = new SequenceEcritureComptable();
        sequenceToAdd.setAnnee(2020);
        sequenceToAdd.setDerniereValeur(1);
        // WHEN
        classUnderTest.insertSequenceEcritureComptable(codeJournal, sequenceToAdd);
        // THEN
        final int result = classUnderTest.getSequenceEcritureComptableLastValue(codeJournal, sequenceToAdd.getAnnee());
        classUnderTest.deleteSequenceEcritureComptable(codeJournal, sequenceToAdd.getAnnee());
        assertThat(result).isEqualTo(sequenceToAdd.getDerniereValeur());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void Given_invalidCodeJournal_When_insertSequenceEcritureComptableIsUsed_Then_shouldThrowDataIntegrityException() {
        // GIVEN
        String codeJournal = "ZZ";
        SequenceEcritureComptable sequenceToAdd = new SequenceEcritureComptable();
        sequenceToAdd.setAnnee(2020);
        sequenceToAdd.setDerniereValeur(1);
        // WHEN
        classUnderTest.insertSequenceEcritureComptable(codeJournal, sequenceToAdd);
    }

    /*==========================================================================*/
    /*      updateSequenceEcritureComptableLastValue Integration tests          */
    /*==========================================================================*/

    @Test
    public void Given_actualSequenceValueIs40_When_updateSequenceEcritureComptableIsUesd_Then_valueShouldBe41() {
        // GIVEN
        String codeJournal = "AC";
        Integer year = 2016;
        int actualValue = classUnderTest.getSequenceEcritureComptableLastValue(codeJournal, year);
        // WHEN
        classUnderTest.updateSequenceEcritureComptable(codeJournal, new SequenceEcritureComptable(year, actualValue + 1));
        // THEN
        final int result = classUnderTest.getSequenceEcritureComptableLastValue(codeJournal, year);
        classUnderTest.updateSequenceEcritureComptable(codeJournal, new SequenceEcritureComptable(year, actualValue));
        assertThat(result).isEqualTo(actualValue+1);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void Given_wrongCodeJournal_When_updateSequenceEcritureComptableIsUsed_Then_shouldThrowEmptyResultDataAccessException() {
        // GIVEN
        String codeJournal = "ZZ";
        Integer year = 2016;
        int actualValue = classUnderTest.getSequenceEcritureComptableLastValue(codeJournal, year);
        // WHEN
        classUnderTest.updateSequenceEcritureComptable(codeJournal, new SequenceEcritureComptable(year, actualValue + 1));
    }

    /*==========================================================================*/
    /*      deleteSequenceEcritureComptableLastValue Integration tests          */
    /*==========================================================================*/

    @Test(expected = EmptyResultDataAccessException.class)
    public void Given_insertNewSequence_When_deleteSequenceEcritureComptableIsUsed_Then_newSequenceShouldBeDelete() {
        // GIVEN
        String codeJournal = "AC";
        Integer year = 2020;
        classUnderTest.insertSequenceEcritureComptable(codeJournal, new SequenceEcritureComptable(year, 1));
        // WHEN
        classUnderTest.deleteSequenceEcritureComptable(codeJournal,year);
        // THEN
        classUnderTest.getSequenceEcritureComptableLastValue(codeJournal, year);
    }

    @Test
    public void Given_wrongSequence_When_deleteSequenceEcritureComptableIsUsed_Then_shouldThrowEmptyResultDataAccessException() {
        // GIVEN
        String codeJournal = "ZZ";
        Integer year = 2020;
        // WHEN
        classUnderTest.deleteSequenceEcritureComptable(codeJournal,year);
    }
}
