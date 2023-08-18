package furrymatch.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Breed.
 */
@Entity
@Table(name = "breed")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Breed implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "breed", nullable = false)
    private String breed;

    @NotNull
    @Column(name = "breed_type", nullable = false)
    private String breedType;

    @OneToMany(cascade = CascadeType.MERGE, orphanRemoval = true, mappedBy = "breed")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "photos", "firstLikees", "secondLikees", "owner", "breed", "searchCriteria" }, allowSetters = true)
    private Set<Pet> pets = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Breed id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBreed() {
        return this.breed;
    }

    public Breed breed(String breed) {
        this.setBreed(breed);
        return this;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getBreedType() {
        return this.breedType;
    }

    public Breed breedType(String breedType) {
        this.setBreedType(breedType);
        return this;
    }

    public void setBreedType(String breedType) {
        this.breedType = breedType;
    }

    public Set<Pet> getPets() {
        return this.pets;
    }

    public void setPets(Set<Pet> pets) {
        if (this.pets != null) {
            this.pets.forEach(i -> i.setBreed(null));
        }
        if (pets != null) {
            pets.forEach(i -> i.setBreed(this));
        }
        this.pets = pets;
    }

    public Breed pets(Set<Pet> pets) {
        this.setPets(pets);
        return this;
    }

    public Breed addPet(Pet pet) {
        this.pets.add(pet);
        pet.setBreed(this);
        return this;
    }

    public Breed removePet(Pet pet) {
        this.pets.remove(pet);
        pet.setBreed(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Breed)) {
            return false;
        }
        return id != null && id.equals(((Breed) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Breed{" +
            "id=" + getId() +
            ", breed='" + getBreed() + "'" +
            ", breedType='" + getBreedType() + "'" +
            "}";
    }
}
