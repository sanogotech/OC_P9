package com.dummy.myerp.model.bean.comptabilite;

import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class LigneEcritureComptableTest {

    @Test
    public void Given_beanEcritureComptable_When_toStringIsUsed_Then_shouldBeEqual() {
        // GIVEN
        LigneEcritureComptable classUndertest = new LigneEcritureComptable();
        classUndertest.setCompteComptable(new CompteComptable(1,"Achats"));
        classUndertest.setLibelle("Achats");
        classUndertest.setCredit(new BigDecimal(100));
        classUndertest.setDebit(new BigDecimal(100));
        String expected = "LigneEcritureComptable{compteComptable=CompteComptable{numero=1, libelle='Achats'}, libelle='Achats', debit=100, credit=100}";
        // WHEN
        final String result = classUndertest.toString();
        // THEN
        assertThat(result).isEqualTo(expected);
    }
}
