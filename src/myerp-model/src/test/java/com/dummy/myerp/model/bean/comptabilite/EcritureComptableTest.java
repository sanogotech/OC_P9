package com.dummy.myerp.model.bean.comptabilite;

import static org.assertj.core.api.Assertions.assertThat;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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

    @Test
    /* @DisplayName("Soit 2 lignes d'écriture comptable de valeur de crédit 60 et 40, lorsque fait le total du crédit, alors on obtient 100.") */
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

    private LigneEcritureComptable createLigne(Integer pCompteComptableNumero, String pDebit, String pCredit) {
        BigDecimal vDebit = pDebit == null ? null : new BigDecimal(pDebit);
        BigDecimal vCredit = pCredit == null ? null : new BigDecimal(pCredit);
        String vLibelle = ObjectUtils.defaultIfNull(vDebit, BigDecimal.ZERO)
                                     .subtract(ObjectUtils.defaultIfNull(vCredit, BigDecimal.ZERO)).toPlainString();
        LigneEcritureComptable vRetour = new LigneEcritureComptable(new CompteComptable(pCompteComptableNumero),
                                                                    vLibelle,
                                                                    vDebit, vCredit);
        return vRetour;
    }

    @Test
    @Ignore
    public void isEquilibree() {
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();

        vEcriture.setLibelle("Equilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "301"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "40", "7"));
        Assert.assertTrue(vEcriture.toString(), vEcriture.isEquilibree());

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Non équilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "20", "1"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "30"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "1", "2"));
        Assert.assertFalse(vEcriture.toString(), vEcriture.isEquilibree());
    }

}
