package com.dummy.myerp.model.bean.comptabilite;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CompteComptableTest extends CompteComptable{

    @Test
    public void Given_beanEcritureComptable_When_getByNumeroIsUsed_Then_shouldBeEqual() {
        // GIVEN
        CompteComptable compte1 = new CompteComptable(1,"Achats");
        CompteComptable compte2 = new CompteComptable(2,"Ventes");
        List<CompteComptable> compteList = new ArrayList<>();
        compteList.add(compte1);
        compteList.add(compte2);
        String expected = "";
        // WHEN
        final CompteComptable result = getByNumero(compteList,2);
        // THEN
        assertThat(result).isEqualTo(compte2);
    }
}
