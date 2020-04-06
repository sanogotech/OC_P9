package com.dummy.myerp.testconsumer.consumer;

import com.dummy.myerp.consumer.dao.impl.db.dao.ComptabiliteDaoImpl;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


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
    /*             getListEcritureComptable Integration tests                   */
    /*==========================================================================*/

    @Test
    public void Given__numberOfEcritureComptableRecordedWhen_getListEcritureComptableIsUsed_Then_shouldReturnCorrectNumberOfRecords() throws NotFoundException {
        // GIVEN
        List<EcritureComptable> initialList = classUnderTest.getListEcritureComptable();
        classUnderTest.insertEcritureComptable(vEcritureComptable);
        // WHEN
        List<EcritureComptable> result = classUnderTest.getListEcritureComptable();
        // THEN
        EcritureComptable ecritureAdded= classUnderTest.getEcritureComptableByRef("AC-2020/00001");
        classUnderTest.deleteEcritureComptable(ecritureAdded.getId());
        assertThat(result.size()).isEqualTo(initialList.size() +1);

    }

    @Test
    public void Given__When_getListEcritureComptableIsUsed_Then_shouldReturnAList() {
        // GIVEN

        // WHEN
        List<EcritureComptable> result = classUnderTest.getListEcritureComptable();
        // THEN
        assertThat(result).isInstanceOf(List.class);

    }

    @Test
    public void Given__When_getListEcritureComptableIsUsed_Then_shouldReturnAListOfEcritureComptable() {
        // GIVEN

        // WHEN
        List<EcritureComptable> result = classUnderTest.getListEcritureComptable();
        // THEN
        assertThat(result.get(0)).isInstanceOf(EcritureComptable.class);

    }

    /*==========================================================================*/
    /*              getEcritureComptable Integration tests                      */
    /*==========================================================================*/

    @Test
    public void Given_idInDataBase_When_getEcritureComptableIsUsed_Then_shouldReturnMatchingBean() throws NotFoundException {
        // GIVEN
        classUnderTest.insertEcritureComptable(vEcritureComptable);
        EcritureComptable ecritureAdded = classUnderTest.getEcritureComptableByRef(vEcritureComptable.getReference());
        // WHEN
        EcritureComptable result = classUnderTest.getEcritureComptable(ecritureAdded.getId());
        // THEN
        classUnderTest.deleteEcritureComptable(ecritureAdded.getId());
        String ecritureAddedToString = ecritureAdded.toString();
        String resultToString = result.toString();
        assertThat(resultToString).isEqualTo(ecritureAddedToString);
    }

    @Test(expected = NotFoundException.class)
    public void Given_wrongId_When_getEcritureComptableIsUsed_Then_shouldThrowsNotFoundException() throws NotFoundException {

        // WHEN
        classUnderTest.getEcritureComptable(100);
    }

    @Test
    public void Given_wrongId_When_getEcritureComptableIsUsed_Then_shouldReturnTheCorrectExceptionMessage() {
        // GIVEN
        int wrongId = 100;
        String messageExpected = "EcritureComptable non trouvée : id=" + wrongId;
        String result = "";
        // WHEN
        try {
            classUnderTest.getEcritureComptable(wrongId);
        } catch (NotFoundException e) {
            result = e.getLocalizedMessage();
        }
        // THEN
        assertThat(result).isEqualTo(messageExpected);
    }

    /*==========================================================================*/
    /*           getEcritureComptableByRef Integration tests                    */
    /*==========================================================================*/

    @Test(expected = NotFoundException.class)
    public void Given_wrongReference_When_getEcritureComptableByRefIsUsed_Then_ThrowNotFoundException() throws NotFoundException {
        // WHEN
        classUnderTest.getEcritureComptableByRef(vEcritureComptable.getReference());
    }

    @Test
    public void Given_wrongReference_When_getEcritureComptableByRefIsUsed_Then_shouldReturnCorrectExceptionMessage() {
        // GIVEN
        String messageExpected = "EcritureComptable non trouvée : reference=" + vEcritureComptable.getReference();
        String result = "";
        // WHEN
        try {
            classUnderTest.getEcritureComptableByRef(vEcritureComptable.getReference());
        } catch (NotFoundException e) {
            result = e.getLocalizedMessage();
        }
        // THEN
        assertThat(result).isEqualTo(messageExpected);
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

    /*==========================================================================*/
    /*             UpdateEcritureComptable Integration tests                    */
    /*==========================================================================*/

    @Test
    public void Given__When__Then() throws NotFoundException {
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
}
