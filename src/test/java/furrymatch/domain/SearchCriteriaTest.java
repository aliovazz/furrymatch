package furrymatch.domain;

import static org.assertj.core.api.Assertions.assertThat;

import furrymatch.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SearchCriteriaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SearchCriteria.class);
        SearchCriteria searchCriteria1 = new SearchCriteria();
        searchCriteria1.setId(1L);
        SearchCriteria searchCriteria2 = new SearchCriteria();
        searchCriteria2.setId(searchCriteria1.getId());
        assertThat(searchCriteria1).isEqualTo(searchCriteria2);
        searchCriteria2.setId(2L);
        assertThat(searchCriteria1).isNotEqualTo(searchCriteria2);
        searchCriteria1.setId(null);
        assertThat(searchCriteria1).isNotEqualTo(searchCriteria2);
    }
}
