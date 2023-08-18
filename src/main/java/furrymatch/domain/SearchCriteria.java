package furrymatch.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import furrymatch.domain.enumeration.Sex;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SearchCriteria.
 */
@Entity
@Table(name = "search_criteria")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SearchCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "filter_type", nullable = false)
    private String filterType;

    @Column(name = "breed")
    private String breed;

    @Column(name = "trade_pups")
    private String tradePups;

    @Enumerated(EnumType.STRING)
    @Column(name = "sex")
    private Sex sex;

    @Column(name = "pedigree")
    private String pedigree;

    @Column(name = "trade_money")
    private String tradeMoney;

    @Column(name = "provice")
    private String provice;

    @Column(name = "canton")
    private String canton;

    @Column(name = "district")
    private String district;

    @Column(name = "objective")
    private String objective;

    @ManyToOne
    @JsonIgnoreProperties(value = { "photos", "firstLikees", "secondLikees", "owner", "breed", "searchCriteria" }, allowSetters = true)
    private Pet pet;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SearchCriteria id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilterType() {
        return this.filterType;
    }

    public SearchCriteria filterType(String filterType) {
        this.setFilterType(filterType);
        return this;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public String getBreed() {
        return this.breed;
    }

    public SearchCriteria breed(String breed) {
        this.setBreed(breed);
        return this;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getTradePups() {
        return this.tradePups;
    }

    public SearchCriteria tradePups(String tradePups) {
        this.setTradePups(tradePups);
        return this;
    }

    public void setTradePups(String tradePups) {
        this.tradePups = tradePups;
    }

    public Sex getSex() {
        return this.sex;
    }

    public SearchCriteria sex(Sex sex) {
        this.setSex(sex);
        return this;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getPedigree() {
        return this.pedigree;
    }

    public SearchCriteria pedigree(String pedigree) {
        this.setPedigree(pedigree);
        return this;
    }

    public void setPedigree(String pedigree) {
        this.pedigree = pedigree;
    }

    public String getTradeMoney() {
        return this.tradeMoney;
    }

    public SearchCriteria tradeMoney(String tradeMoney) {
        this.setTradeMoney(tradeMoney);
        return this;
    }

    public void setTradeMoney(String tradeMoney) {
        this.tradeMoney = tradeMoney;
    }

    public String getProvice() {
        return this.provice;
    }

    public SearchCriteria provice(String provice) {
        this.setProvice(provice);
        return this;
    }

    public void setProvice(String provice) {
        this.provice = provice;
    }

    public String getCanton() {
        return this.canton;
    }

    public SearchCriteria canton(String canton) {
        this.setCanton(canton);
        return this;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public String getDistrict() {
        return this.district;
    }

    public SearchCriteria district(String district) {
        this.setDistrict(district);
        return this;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getObjective() {
        return this.objective;
    }

    public SearchCriteria objective(String objective) {
        this.setObjective(objective);
        return this;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public Pet getPet() {
        return this.pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public SearchCriteria pet(Pet pet) {
        this.setPet(pet);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SearchCriteria)) {
            return false;
        }
        return id != null && id.equals(((SearchCriteria) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SearchCriteria{" +
            "id=" + getId() +
            ", filterType='" + getFilterType() + "'" +
            ", breed='" + getBreed() + "'" +
            ", tradePups='" + getTradePups() + "'" +
            ", sex='" + getSex() + "'" +
            ", pedigree='" + getPedigree() + "'" +
            ", tradeMoney='" + getTradeMoney() + "'" +
            ", provice='" + getProvice() + "'" +
            ", canton='" + getCanton() + "'" +
            ", district='" + getDistrict() + "'" +
            ", objective='" + getObjective() + "'" +
            "}";
    }
}
