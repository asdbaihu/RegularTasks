package hello.Model;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Anton on 21.08.2017.
 */
public interface EmailRepository extends PagingAndSortingRepository<Sendemail, Long> {
}
