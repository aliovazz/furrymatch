package furrymatch.domain;

import static org.assertj.core.api.Assertions.assertThat;

import furrymatch.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BreedTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Breed.class);
        Breed breed1 = new Breed();
        breed1.setId(1L);
        Breed breed2 = new Breed();
        breed2.setId(breed1.getId());
        assertThat(breed1).isEqualTo(breed2);
        breed2.setId(2L);
        assertThat(breed1).isNotEqualTo(breed2);
        breed1.setId(null);
        assertThat(breed1).isNotEqualTo(breed2);
    }
}
