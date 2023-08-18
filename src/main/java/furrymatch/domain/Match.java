package furrymatch.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Match.
 */
@Entity
@Table(name = "jhi_match")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Match implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "notify_match")
    private Boolean notifyMatch;

    @Column(name = "date_match")
    private LocalDate dateMatch;

    @JsonIgnoreProperties(value = { "match" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Contract contract;

    @OneToMany(cascade = CascadeType.MERGE, orphanRemoval = true, mappedBy = "match")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "match" }, allowSetters = true)
    private Set<Chat> chats = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "firstMatches", "secondMatches", "firstPet", "secondPet" }, allowSetters = true)
    private Likee firstLiked;

    @ManyToOne
    @JsonIgnoreProperties(value = { "firstMatches", "secondMatches", "firstPet", "secondPet" }, allowSetters = true)
    private Likee secondLiked;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Match id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getNotifyMatch() {
        return this.notifyMatch;
    }

    public Match notifyMatch(Boolean notifyMatch) {
        this.setNotifyMatch(notifyMatch);
        return this;
    }

    public void setNotifyMatch(Boolean notifyMatch) {
        this.notifyMatch = notifyMatch;
    }

    public LocalDate getDateMatch() {
        return this.dateMatch;
    }

    public Match dateMatch(LocalDate dateMatch) {
        this.setDateMatch(dateMatch);
        return this;
    }

    public void setDateMatch(LocalDate dateMatch) {
        this.dateMatch = dateMatch;
    }

    public Contract getContract() {
        return this.contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Match contract(Contract contract) {
        this.setContract(contract);
        return this;
    }

    public Set<Chat> getChats() {
        return this.chats;
    }

    public void setChats(Set<Chat> chats) {
        if (this.chats != null) {
            this.chats.forEach(i -> i.setMatch(null));
        }
        if (chats != null) {
            chats.forEach(i -> i.setMatch(this));
        }
        this.chats = chats;
    }

    public Match chats(Set<Chat> chats) {
        this.setChats(chats);
        return this;
    }

    public Match addChat(Chat chat) {
        this.chats.add(chat);
        chat.setMatch(this);
        return this;
    }

    public Match removeChat(Chat chat) {
        this.chats.remove(chat);
        chat.setMatch(null);
        return this;
    }

    public Likee getFirstLiked() {
        return this.firstLiked;
    }

    public void setFirstLiked(Likee likee) {
        this.firstLiked = likee;
    }

    public Match firstLiked(Likee likee) {
        this.setFirstLiked(likee);
        return this;
    }

    public Likee getSecondLiked() {
        return this.secondLiked;
    }

    public void setSecondLiked(Likee likee) {
        this.secondLiked = likee;
    }

    public Match secondLiked(Likee likee) {
        this.setSecondLiked(likee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Match)) {
            return false;
        }
        return id != null && id.equals(((Match) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Match{" +
            "id=" + getId() +
            ", notifyMatch='" + getNotifyMatch() + "'" +
            ", dateMatch='" + getDateMatch() + "'" +
            "}";
    }
}
