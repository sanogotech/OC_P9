package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.dummy.myerp.model.bean.comptabilite.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.TransactionStatus;
import com.dummy.myerp.business.contrat.manager.ComptabiliteManager;
import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;


/**
 * Comptabilite manager implementation.
 */
public class ComptabiliteManagerImpl extends AbstractBusinessManager implements ComptabiliteManager {

    // ==================== Attributs ====================


    // ==================== Constructeurs ====================

    /**
     * Instantiates a new Comptabilite manager.
     */
    public ComptabiliteManagerImpl() {
    }


    // ==================== Getters/Setters ====================
    @Override
    public List<CompteComptable> getListCompteComptable() {
        return getDaoProxy().getComptabiliteDao().getListCompteComptable();
    }


    @Override
    public List<JournalComptable> getListJournalComptable() {
        return getDaoProxy().getComptabiliteDao().getListJournalComptable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EcritureComptable> getListEcritureComptable() {
        return getDaoProxy().getComptabiliteDao().getListEcritureComptable();
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    // TODO à tester
    @Override
    public synchronized int addReference(EcritureComptable pEcritureComptable) {
        // TODO à implémenter

        // Set attributes
        String codeJournal = pEcritureComptable.getJournal().getCode();
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        String yearInString = String.valueOf(year);


        // Get SequenceEcritureComptable last value according to parameters
        int sequenceEcritureComptableLastValue = getSequenceEcritureComptableLastValue(codeJournal, yearInString);

        // Set sequenceEcritureComptableUpdatedValue
        int sequenceEcritureComptableUpdatedValue = setSequenceEcritureComptableUpdatedValue(sequenceEcritureComptableLastValue);

        // Update reference
        pEcritureComptable.setReference(createEcritureComptableReference(codeJournal, yearInString, sequenceEcritureComptableUpdatedValue));

        // Persist updated sequence
        return persistSequenceEcritureComptableValue(sequenceEcritureComptableLastValue, new Integer(sequenceEcritureComptableUpdatedValue), codeJournal, new Integer(year));

    }

    /**
     * Check if a sequence already exist then set sequenceEcritureComptableUpdatedValue
     *
     * @param actualSequenceValue
     * @return
     */
    protected int setSequenceEcritureComptableUpdatedValue(int actualSequenceValue) {
        if (actualSequenceValue > 0) {
            return actualSequenceValue + 1;
        } else {
            return 1;
        }
    }

    /**
     * Create Ecriture Comptable reference according to parameters
     * @param codeJournal
     * @param yearInString
     * @param sequenceEcritureComptableUpdatedValue
     * @return
     */
    protected String createEcritureComptableReference(String codeJournal, String yearInString, int sequenceEcritureComptableUpdatedValue) {
        return codeJournal + "-" + yearInString + "/" + String.format("%05d", sequenceEcritureComptableUpdatedValue);
    }

    /**
     * Persist updated sequence in database
     * @param actualSequenceValue
     * @param updatedSequenceValue
     * @param codeJournal
     * @param year
     * @return
     */
    protected int persistSequenceEcritureComptableValue(int actualSequenceValue, Integer updatedSequenceValue, String codeJournal, Integer year) {
        int row=0;
        if (actualSequenceValue > 0) {
            // Sequence already exist then update the sequence
            row = this.updateSequenceEcritureComptable(codeJournal, new SequenceEcritureComptable(year, updatedSequenceValue));
            return row;
        } else {
            // Sequence doesn't exist then insert a new sequence
            row =  this.insertSequenceEcritureComptable(codeJournal, new SequenceEcritureComptable(year, updatedSequenceValue));
            return row;
        }
    }

    /**
     * {@inheritDoc}
     */
    // TODO à tester
    @Override
    public void checkEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        this.checkEcritureComptableUnit(pEcritureComptable);
        this.checkEcritureComptableContext(pEcritureComptable);
    }


    /**
     * Vérifie que l'Ecriture comptable respecte les règles de gestion unitaires,
     * c'est à dire indépendemment du contexte (unicité de la référence, exercie comptable non cloturé...)
     *
     * @param pEcritureComptable -
     * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les règles de gestion
     */
    // TODO tests à compléter
    protected Boolean checkEcritureComptableUnit(EcritureComptable pEcritureComptable) throws FunctionalException {
        // ===== Vérification des contraintes unitaires sur les attributs de l'écriture
        checkEcritureComptableValidation(pEcritureComptable);

        // ===== RG_Compta_2 : Pour qu'une écriture comptable soit valide, elle doit être équilibrée
        checkEcritureComptableEquilibre((pEcritureComptable));

        // ===== RG_Compta_3 : une écriture comptable doit avoir au moins 2 lignes d'écriture (1 au débit, 1 au crédit)
        checkEcritureComptableLines(pEcritureComptable);

        // TODO ===== RG_Compta_5 : Format et contenu de la référence
        // vérifier que l'année dans la référence correspond bien à la date de l'écriture, idem pour le code journal...
        checkReference(pEcritureComptable);
        return true;
    }

    protected Boolean checkEcritureComptableValidation(EcritureComptable pEcritureComptable) throws FunctionalException {
        // ===== Vérification des contraintes unitaires sur les attributs de l'écriture
        Set<ConstraintViolation<EcritureComptable>> vViolations = getConstraintValidator().validate(pEcritureComptable);
        if (!vViolations.isEmpty()) {
            throw new FunctionalException("L'écriture comptable ne respecte pas les règles de gestion.",
                    new ConstraintViolationException(
                            "L'écriture comptable ne respecte pas les contraintes de validation",
                            vViolations));
        }
        return true;
    }

    protected Boolean checkEcritureComptableEquilibre(EcritureComptable pEcritureComptable) throws FunctionalException {
        // ===== RG_Compta_2 : Pour qu'une écriture comptable soit valide, elle doit être équilibrée
        if (!pEcritureComptable.isEquilibree()) {
            throw new FunctionalException("L'écriture comptable n'est pas équilibrée.");
        }
        return true;
    }

    protected Boolean checkEcritureComptableLines(EcritureComptable pEcritureComptable) throws FunctionalException {
        // ===== RG_Compta_3 : une écriture comptable doit avoir au moins 2 lignes d'écriture (1 au débit, 1 au crédit)
        int vNbrCredit = 0;
        int vNbrDebit = 0;
        for (LigneEcritureComptable vLigneEcritureComptable : pEcritureComptable.getListLigneEcriture()) {
            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getCredit(),
                    BigDecimal.ZERO)) != 0) {
                vNbrCredit++;
            }
            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getDebit(),
                    BigDecimal.ZERO)) != 0) {
                vNbrDebit++;
            }
        }
        // On test le nombre de lignes car si l'écriture à une seule ligne
        //      avec un montant au débit et un montant au crédit ce n'est pas valable
        if (pEcritureComptable.getListLigneEcriture().size() < 2
                || vNbrCredit < 1
                || vNbrDebit < 1) {
            throw new FunctionalException(
                    "L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit.");
        }
        return true;
    }

    // vérifier que l'année dans la référence correspond bien à la date de l'écriture, idem pour le code journal...
    protected Boolean checkReference(EcritureComptable pEcritureComptable) throws FunctionalException {

        String[] refSplit = pEcritureComptable.getReference().split("-|/");
        String extractedCode = refSplit[0];
        String extractedYear = refSplit[1];

        String code = pEcritureComptable.getJournal().getCode();
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        String yearInString = String.valueOf(year);

        if (!code.equals(extractedCode)) {
            throw new FunctionalException(
                    "La référence de l'écriture comptable doit contenir le code correspondant au journal associé.");
        }

        if (!yearInString.equals(extractedYear)) {
            throw new FunctionalException(
                    "La référence de l'écriture comptable doit contenir l'année courante.");
        }

        return true;
    }


    /**
     * Vérifie que l'Ecriture comptable respecte les règles de gestion liées au contexte
     * (unicité de la référence, année comptable non cloturé...)
     *
     * @param pEcritureComptable -
     * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les règles de gestion
     */
    protected void checkEcritureComptableContext(EcritureComptable pEcritureComptable) throws FunctionalException {
        // ===== RG_Compta_6 : La référence d'une écriture comptable doit être unique
        if (StringUtils.isNoneEmpty(pEcritureComptable.getReference())) {
            try {
                // Recherche d'une écriture ayant la même référence
                EcritureComptable vECRef = getEcritureComptableByRef(pEcritureComptable);

                // Si l'écriture à vérifier est une nouvelle écriture (id == null),
                // ou si elle ne correspond pas à l'écriture trouvée (id != idECRef),
                // c'est qu'il y a déjà une autre écriture avec la même référence
                if (pEcritureComptable.getId() == null
                        || !pEcritureComptable.getId().equals(vECRef.getId())) {
                    throw new FunctionalException("Une autre écriture comptable existe déjà avec la même référence.");
                }
            } catch (NotFoundException vEx) {
                // Dans ce cas, c'est bon, ça veut dire qu'on n'a aucune autre écriture avec la même référence.
            }
        }
    }

    public EcritureComptable getEcritureComptableByRef(EcritureComptable pEcritureComptable) throws NotFoundException{
        // Recherche d'une écriture ayant la même référence
        return getDaoProxy().getComptabiliteDao().getEcritureComptableByRef(
                pEcritureComptable.getReference());
        }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        this.checkEcritureComptable(pEcritureComptable);
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().insertEcritureComptable(pEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().updateEcritureComptable(pEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteEcritureComptable(Integer pId) {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().deleteEcritureComptable(pId);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    @Override
    public int getSequenceEcritureComptableLastValue(String codeJournal, String year) {
        return getDaoProxy().getComptabiliteDao().getSequenceEcritureComptableLastValue(codeJournal, year);
    }

    @Override
    public int insertSequenceEcritureComptable(String codeJournal, SequenceEcritureComptable pSequenceEcritureComptable) {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        int row = 0;
        try {
            row = getDaoProxy().getComptabiliteDao().insertSequenceEcritureComptable(codeJournal, pSequenceEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
        return row;
    }

    @Override
    public int updateSequenceEcritureComptable(String codeJournal, SequenceEcritureComptable pSequenceEcritureComptable) {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        int row = 0;
        try {
            row = getDaoProxy().getComptabiliteDao().updateSequenceEcritureComptable(codeJournal, pSequenceEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
        return row;
    }
}
