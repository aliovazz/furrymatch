package furrymatch.domain;

import static org.assertj.core.api.Assertions.assertThat;

import furrymatch.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LikeeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Likee.class);
        Likee likee1 = new Likee();
        likee1.setId(1L);
        Likee likee2 = new Likee();
        likee2.setId(likee1.getId());
        assertThat(likee1).isEqualTo(likee2);
        likee2.setId(2L);
        assertThat(likee1).isNotEqualTo(likee2);
        likee1.setId(null);
        assertThat(likee1).isNotEqualTo(likee2);
    }
}
