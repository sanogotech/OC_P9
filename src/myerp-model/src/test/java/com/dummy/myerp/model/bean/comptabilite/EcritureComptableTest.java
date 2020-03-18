package com.dummy.myerp.model.bean.comptabilite;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EcritureComptableTest {

    @Mock
    EcritureComptable classUnderTest;
    LigneEcritureComptable accountWriting1;
    LigneEcritureComptable accountWriting2;
    List<LigneEcritureComptable> accountWritingList;

    @Before
    public void initBeforeEach() {

        classUnderTest = new EcritureComptable();

        // Declare account writing
        accountWriting1 = new LigneEcritureComptable();
        accountWriting2 = new LigneEcritureComptable();

        // Declare collection of account writing
        accountWritingList = new ArrayList<>();

    }

    @After
    public void undefClassUnderTest() {
        classUnderTest = null;

        // Declare account writing
        accountWriting1 = null;
        accountWriting2 = null;

        // Declare collection of account writing
        accountWritingList = null;
    }

    // Soit 2 lignes d'écriture comptable de valeur de crédit 60 et 40, lorsque fait
    // le total du crédit, alors on obtient 100.
    @Test
    public void Given_accountWriting1CreditIsEqual60AndAccountWriting2CreditIsEqual40_When_getTotalCreditIsUsed_Then_shouldReturn100() {
        // GIVEN

        // Set Credit values
        accountWriting1.setCredit(BigDecimal.valueOf(60));
        accountWriting2.setCredit(BigDecimal.valueOf(40));

        // Add account writing to list
        accountWritingList.add(accountWriting1);
        accountWritingList.add(accountWriting2);

        // Set ListLigneEcriture
        classUnderTest.getListLigneEcriture().add(accountWriting1);
        classUnderTest.getListLigneEcriture().add(accountWriting2);

        // WHEN
        final BigDecimal result = classUnderTest.getTotalCredit();
        // THEN
        assertThat(result).isEqualByComparingTo("100");

    }

    // Soit 2 lignes d'écriture comptable de valeurs de crédit null, lorsque fait le
    // total du crédit, alors on obtient 0.
    @Test
    public void Given_accountWriting1CreditIsNullAndAccountWriting2CreditIsNull_When_getTotalCreditIsUsed_Then_shouldReturn0() {
        // GIVEN

        // Add account writing to list
        accountWritingList.add(accountWriting1);
        accountWritingList.add(accountWriting2);

        // Set ListLigneEcriture
        classUnderTest.getListLigneEcriture().add(accountWriting1);
        classUnderTest.getListLigneEcriture().add(accountWriting2);

        // WHEN
        final BigDecimal result = classUnderTest.getTotalCredit();
        // THEN
        assertThat(result).isEqualByComparingTo("0");

    }

    // Soit 2 lignes d'écriture comptable de valeur de debit 80 et 20, lorsque fait
    // le total du débit, alors on obtient 100.
    @Test
    public void Given_accountWriting1DebitIsEqual80AndAccountWriting2DebitIsEqual20_When_getTotalDebitIsUsed_Then_shouldReturn100() {
        // GIVEN

        // Set Debit values
        accountWriting1.setDebit(BigDecimal.valueOf(80));
        accountWriting2.setDebit(BigDecimal.valueOf(20));

        // Add account writing to list
        accountWritingList.add(accountWriting1);
        accountWritingList.add(accountWriting2);

        // Set ListLigneEcriture
        classUnderTest.getListLigneEcriture().add(accountWriting1);
        classUnderTest.getListLigneEcriture().add(accountWriting2);

        // WHEN
        final BigDecimal result = classUnderTest.getTotalDebit();
        // THEN
        assertThat(result).isEqualByComparingTo("100");

    }

    // Soit 2 lignes d'écriture comptable de valeurs de debit null, lorsque fait le
    // total du débit, alors on obtient 0.
    @Test
    public void Given_accountWriting1DebitIsNullAndAccountWriting2DebitIsNull_When_getTotalDebitIsUsed_Then_shouldReturn0() {
        // GIVEN

        // Add account writing to list
        accountWritingList.add(accountWriting1);
        accountWritingList.add(accountWriting2);

        // Set ListLigneEcriture
        classUnderTest.getListLigneEcriture().add(accountWriting1);
        classUnderTest.getListLigneEcriture().add(accountWriting2);

        // WHEN
        final BigDecimal result = classUnderTest.getTotalDebit();
        // THEN
        assertThat(result).isEqualByComparingTo("0");

    }

    // Soit le crédit total=100 et le débit total=100, lorsque l'on vérifie
    // l'équilibre, alors on obtient true
    @Test
    public void Given_totalCredit100AndTotalDebit100_When_isEquilibreeIsUsed_Then_souldReturnTrue() {
        // GIVEN

        // Init mock
        MockitoAnnotations.initMocks(this);

        // Declare mock behavior
        when(classUnderTest.getTotalCredit()).thenReturn(BigDecimal.valueOf(100));
        when(classUnderTest.getTotalDebit()).thenReturn(BigDecimal.valueOf(100));
        when(classUnderTest.isEquilibree()).thenCallRealMethod();

        // WHEN
        final Boolean result = classUnderTest.isEquilibree();

        // THEN
        assertThat(result).isTrue();
    }

    // Soit le crédit total=200 et le débit total=100, lorsque l'on vérifie
    // l'équilibre, alors on obtient false
    @Test
    public void Given_totalCredit200AndTotalDebit100_When_isEquilibreeIsUsed_Then_souldReturnFalse() {
        // GIVEN

        // Init mock
        MockitoAnnotations.initMocks(this);

        // Declare mock behavior
        when(classUnderTest.getTotalCredit()).thenReturn(BigDecimal.valueOf(200));
        when(classUnderTest.getTotalDebit()).thenReturn(BigDecimal.valueOf(100));
        when(classUnderTest.isEquilibree()).thenCallRealMethod();

        // WHEN
        final Boolean result = classUnderTest.isEquilibree();

        // THEN
        assertThat(result).isFalse();
    }
}
