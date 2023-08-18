package furrymatch.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Contract.
 */
@Entity
@Table(name = "contract")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Contract implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "trade_money")
    private String tradeMoney;

    @Column(name = "trade_pups")
    private String tradePups;

    @Column(name = "pedigree")
    private String pedigree;

    @Column(name = "other_notes")
    private String otherNotes;

    @Column(name = "contract_date")
    private LocalDate contractDate;

    @JsonIgnoreProperties(value = { "contract", "chats", "firstLiked", "secondLiked" }, allowSetters = true)
    @OneToOne(cascade = CascadeType.MERGE, orphanRemoval = true, mappedBy = "contract")
    private Match match;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Contract id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTradeMoney() {
        return this.tradeMoney;
    }

    public Contract tradeMoney(String tradeMoney) {
        this.setTradeMoney(tradeMoney);
        return this;
    }

    public void setTradeMoney(String tradeMoney) {
        this.tradeMoney = tradeMoney;
    }

    public String getTradePups() {
        return this.tradePups;
    }

    public Contract tradePups(String tradePups) {
        this.setTradePups(tradePups);
        return this;
    }

    public void setTradePups(String tradePups) {
        this.tradePups = tradePups;
    }

    public String getPedigree() {
        return this.pedigree;
    }

    public Contract pedigree(String pedigree) {
        this.setPedigree(pedigree);
        return this;
    }

    public void setPedigree(String pedigree) {
        this.pedigree = pedigree;
    }

    public String getOtherNotes() {
        return this.otherNotes;
    }

    public Contract otherNotes(String otherNotes) {
        this.setOtherNotes(otherNotes);
        return this;
    }

    public void setOtherNotes(String otherNotes) {
        this.otherNotes = otherNotes;
    }

    public LocalDate getContractDate() {
        return this.contractDate;
    }

    public Contract contractDate(LocalDate contractDate) {
        this.setContractDate(contractDate);
        return this;
    }

    public void setContractDate(LocalDate contractDate) {
        this.contractDate = contractDate;
    }

    public Match getMatch() {
        return this.match;
    }

    public void setMatch(Match match) {
        if (this.match != null) {
            this.match.setContract(null);
        }
        if (match != null) {
            match.setContract(this);
        }
        this.match = match;
    }

    public Contract match(Match match) {
        this.setMatch(match);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contract)) {
            return false;
        }
        return id != null && id.equals(((Contract) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Contract{" +
            "id=" + getId() +
            ", tradeMoney='" + getTradeMoney() + "'" +
            ", tradePups='" + getTradePups() + "'" +
            ", pedigree='" + getPedigree() + "'" +
            ", otherNotes='" + getOtherNotes() + "'" +
            ", contractDate='" + getContractDate() + "'" +
            "}";
    }
}
