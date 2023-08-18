package furrymatch.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import furrymatch.domain.enumeration.PetType;
import furrymatch.domain.enumeration.Sex;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Pet.
 */
@Entity
@Table(name = "pet")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Pet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "pet_type", nullable = false)
    private PetType petType;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sex", nullable = false)
    private Sex sex;

    @Column(name = "trade_money")
    private Boolean tradeMoney;

    @Column(name = "trade_pups")
    private Boolean tradePups;

    @Column(name = "pedigree")
    private Boolean pedigree;

    @Column(name = "desire_ammount")
    private Double desireAmmount;

    @OneToMany(cascade = CascadeType.MERGE, orphanRemoval = true, mappedBy = "pet")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pet" }, allowSetters = true)
    private Set<Photo> photos = new HashSet<>();

    @OneToMany(cascade = CascadeType.MERGE, orphanRemoval = true, mappedBy = "firstPet")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "firstMatches", "secondMatches", "firstPet", "secondPet" }, allowSetters = true)
    private Set<Likee> firstLikees = new HashSet<>();

    @OneToMany(cascade = CascadeType.MERGE, orphanRemoval = true, mappedBy = "secondPet")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "firstMatches", "secondMatches", "firstPet", "secondPet" }, allowSetters = true)
    private Set<Likee> secondLikees = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "pets" }, allowSetters = true)
    private Owner owner;

    @ManyToOne
    @JsonIgnoreProperties(value = { "pets" }, allowSetters = true)
    private Breed breed;

    @OneToMany(cascade = CascadeType.MERGE, orphanRemoval = true, mappedBy = "pet")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pet" }, allowSetters = true)
    private Set<SearchCriteria> searchCriteria = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Pet name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PetType getPetType() {
        return this.petType;
    }

    public Pet petType(PetType petType) {
        this.setPetType(petType);
        return this;
    }

    public void setPetType(PetType petType) {
        this.petType = petType;
    }

    public String getDescription() {
        return this.description;
    }

    public Pet description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Sex getSex() {
        return this.sex;
    }

    public Pet sex(Sex sex) {
        this.setSex(sex);
        return this;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public Boolean getTradeMoney() {
        return this.tradeMoney;
    }

    public Pet tradeMoney(Boolean tradeMoney) {
        this.setTradeMoney(tradeMoney);
        return this;
    }

    public void setTradeMoney(Boolean tradeMoney) {
        this.tradeMoney = tradeMoney;
    }

    public Boolean getTradePups() {
        return this.tradePups;
    }

    public Pet tradePups(Boolean tradePups) {
        this.setTradePups(tradePups);
        return this;
    }

    public void setTradePups(Boolean tradePups) {
        this.tradePups = tradePups;
    }

    public Boolean getPedigree() {
        return this.pedigree;
    }

    public Pet pedigree(Boolean pedigree) {
        this.setPedigree(pedigree);
        return this;
    }

    public void setPedigree(Boolean pedigree) {
        this.pedigree = pedigree;
    }

    public Double getDesireAmmount() {
        return this.desireAmmount;
    }

    public Pet desireAmmount(Double desireAmmount) {
        this.setDesireAmmount(desireAmmount);
        return this;
    }

    public void setDesireAmmount(Double desireAmmount) {
        this.desireAmmount = desireAmmount;
    }

    public Set<Photo> getPhotos() {
        return this.photos;
    }

    public void setPhotos(Set<Photo> photos) {
        if (this.photos != null) {
            this.photos.forEach(i -> i.setPet(null));
        }
        if (photos != null) {
            photos.forEach(i -> i.setPet(this));
        }
        this.photos = photos;
    }

    public Pet photos(Set<Photo> photos) {
        this.setPhotos(photos);
        return this;
    }

    public Pet addPhoto(Photo photo) {
        this.photos.add(photo);
        photo.setPet(this);
        return this;
    }

    public Pet removePhoto(Photo photo) {
        this.photos.remove(photo);
        photo.setPet(null);
        return this;
    }

    public Set<Likee> getFirstLikees() {
        return this.firstLikees;
    }

    public void setFirstLikees(Set<Likee> likees) {
        if (this.firstLikees != null) {
            this.firstLikees.forEach(i -> i.setFirstPet(null));
        }
        if (likees != null) {
            likees.forEach(i -> i.setFirstPet(this));
        }
        this.firstLikees = likees;
    }

    public Pet firstLikees(Set<Likee> likees) {
        this.setFirstLikees(likees);
        return this;
    }

    public Pet addFirstLikee(Likee likee) {
        this.firstLikees.add(likee);
        likee.setFirstPet(this);
        return this;
    }

    public Pet removeFirstLikee(Likee likee) {
        this.firstLikees.remove(likee);
        likee.setFirstPet(null);
        return this;
    }

    public Set<Likee> getSecondLikees() {
        return this.secondLikees;
    }

    public void setSecondLikees(Set<Likee> likees) {
        if (this.secondLikees != null) {
            this.secondLikees.forEach(i -> i.setSecondPet(null));
        }
        if (likees != null) {
            likees.forEach(i -> i.setSecondPet(this));
        }
        this.secondLikees = likees;
    }

    public Pet secondLikees(Set<Likee> likees) {
        this.setSecondLikees(likees);
        return this;
    }

    public Pet addSecondLikee(Likee likee) {
        this.secondLikees.add(likee);
        likee.setSecondPet(this);
        return this;
    }

    public Pet removeSecondLikee(Likee likee) {
        this.secondLikees.remove(likee);
        likee.setSecondPet(null);
        return this;
    }

    public Owner getOwner() {
        return this.owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Pet owner(Owner owner) {
        this.setOwner(owner);
        return this;
    }

    public Breed getBreed() {
        return this.breed;
    }

    public void setBreed(Breed breed) {
        this.breed = breed;
    }

    public Pet breed(Breed breed) {
        this.setBreed(breed);
        return this;
    }

    public Set<SearchCriteria> getSearchCriteria() {
        return this.searchCriteria;
    }

    public void setSearchCriteria(Set<SearchCriteria> searchCriteria) {
        if (this.searchCriteria != null) {
            this.searchCriteria.forEach(i -> i.setPet(null));
        }
        if (searchCriteria != null) {
            searchCriteria.forEach(i -> i.setPet(this));
        }
        this.searchCriteria = searchCriteria;
    }

    public Pet searchCriteria(Set<SearchCriteria> searchCriteria) {
        this.setSearchCriteria(searchCriteria);
        return this;
    }

    public Pet addSearchCriteria(SearchCriteria searchCriteria) {
        this.searchCriteria.add(searchCriteria);
        searchCriteria.setPet(this);
        return this;
    }

    public Pet removeSearchCriteria(SearchCriteria searchCriteria) {
        this.searchCriteria.remove(searchCriteria);
        searchCriteria.setPet(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pet)) {
            return false;
        }
        return id != null && id.equals(((Pet) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pet{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", petType='" + getPetType() + "'" +
            ", description='" + getDescription() + "'" +
            ", sex='" + getSex() + "'" +
            ", tradeMoney='" + getTradeMoney() + "'" +
            ", tradePups='" + getTradePups() + "'" +
            ", pedigree='" + getPedigree() + "'" +
            ", desireAmmount=" + getDesireAmmount() +
            "}";
    }
}
