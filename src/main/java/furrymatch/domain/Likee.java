package furrymatch.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import furrymatch.domain.enumeration.LikeType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Likee.
 */
@Entity
@Table(name = "likee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Likee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "like_state", nullable = false)
    private LikeType likeState;

    @OneToMany(cascade = CascadeType.MERGE, orphanRemoval = true, mappedBy = "firstLiked")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contract", "chats", "firstLiked", "secondLiked" }, allowSetters = true)
    private Set<Match> firstMatches = new HashSet<>();

    @OneToMany(cascade = CascadeType.MERGE, orphanRemoval = true, mappedBy = "secondLiked")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contract", "chats", "firstLiked", "secondLiked" }, allowSetters = true)
    private Set<Match> secondMatches = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "photos", "firstLikees", "secondLikees", "owner", "breed", "searchCriteria" }, allowSetters = true)
    private Pet firstPet;

    @ManyToOne
    @JsonIgnoreProperties(value = { "photos", "firstLikees", "secondLikees", "owner", "breed", "searchCriteria" }, allowSetters = true)
    private Pet secondPet;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Likee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LikeType getLikeState() {
        return this.likeState;
    }

    public Likee likeState(LikeType likeState) {
        this.setLikeState(likeState);
        return this;
    }

    public void setLikeState(LikeType likeState) {
        this.likeState = likeState;
    }

    public Set<Match> getFirstMatches() {
        return this.firstMatches;
    }

    public void setFirstMatches(Set<Match> matches) {
        if (this.firstMatches != null) {
            this.firstMatches.forEach(i -> i.setFirstLiked(null));
        }
        if (matches != null) {
            matches.forEach(i -> i.setFirstLiked(this));
        }
        this.firstMatches = matches;
    }

    public Likee firstMatches(Set<Match> matches) {
        this.setFirstMatches(matches);
        return this;
    }

    public Likee addFirstMatch(Match match) {
        this.firstMatches.add(match);
        match.setFirstLiked(this);
        return this;
    }

    public Likee removeFirstMatch(Match match) {
        this.firstMatches.remove(match);
        match.setFirstLiked(null);
        return this;
    }

    public Set<Match> getSecondMatches() {
        return this.secondMatches;
    }

    public void setSecondMatches(Set<Match> matches) {
        if (this.secondMatches != null) {
            this.secondMatches.forEach(i -> i.setSecondLiked(null));
        }
        if (matches != null) {
            matches.forEach(i -> i.setSecondLiked(this));
        }
        this.secondMatches = matches;
    }

    public Likee secondMatches(Set<Match> matches) {
        this.setSecondMatches(matches);
        return this;
    }

    public Likee addSecondMatch(Match match) {
        this.secondMatches.add(match);
        match.setSecondLiked(this);
        return this;
    }

    public Likee removeSecondMatch(Match match) {
        this.secondMatches.remove(match);
        match.setSecondLiked(null);
        return this;
    }

    public Pet getFirstPet() {
        return this.firstPet;
    }

    public void setFirstPet(Pet pet) {
        this.firstPet = pet;
    }

    public Likee firstPet(Pet pet) {
        this.setFirstPet(pet);
        return this;
    }

    public Pet getSecondPet() {
        return this.secondPet;
    }

    public void setSecondPet(Pet pet) {
        this.secondPet = pet;
    }

    public Likee secondPet(Pet pet) {
        this.setSecondPet(pet);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Likee)) {
            return false;
        }
        return id != null && id.equals(((Likee) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Likee{" +
            "id=" + getId() +
            ", likeState='" + getLikeState() + "'" +
            "}";
    }
}
