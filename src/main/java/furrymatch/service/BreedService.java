package furrymatch.service;

import furrymatch.domain.Breed;
import furrymatch.repository.BreedRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Breed}.
 */
@Service
@Transactional
public class BreedService {

    private final Logger log = LoggerFactory.getLogger(BreedService.class);

    private final BreedRepository breedRepository;

    public BreedService(BreedRepository breedRepository) {
        this.breedRepository = breedRepository;
    }

    /**
     * Save a breed.
     *
     * @param breed the entity to save.
     * @return the persisted entity.
     */
    public Breed save(Breed breed) {
        log.debug("Request to save Breed : {}", breed);
        return breedRepository.save(breed);
    }

    /**
     * Update a breed.
     *
     * @param breed the entity to save.
     * @return the persisted entity.
     */
    public Breed update(Breed breed) {
        log.debug("Request to update Breed : {}", breed);
        return breedRepository.save(breed);
    }

    /**
     * Partially update a breed.
     *
     * @param breed the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Breed> partialUpdate(Breed breed) {
        log.debug("Request to partially update Breed : {}", breed);

        return breedRepository
            .findById(breed.getId())
            .map(existingBreed -> {
                if (breed.getBreed() != null) {
                    existingBreed.setBreed(breed.getBreed());
                }
                if (breed.getBreedType() != null) {
                    existingBreed.setBreedType(breed.getBreedType());
                }

                return existingBreed;
            })
            .map(breedRepository::save);
    }

    /**
     * Get all the breeds.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Breed> findAll(Pageable pageable) {
        log.debug("Request to get all Breeds");
        return breedRepository.findAll(pageable);
    }

    /**
     * Get one breed by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Breed> findOne(Long id) {
        log.debug("Request to get Breed : {}", id);
        return breedRepository.findById(id);
    }

    /**
     * Delete the breed by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Breed : {}", id);
        breedRepository.deleteById(id);
    }
}
