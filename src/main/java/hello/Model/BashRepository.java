package hello.Model;


import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BashRepository extends PagingAndSortingRepository<Bashscript, Long> {


}
