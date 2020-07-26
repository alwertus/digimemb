package ru.alwertus.digimemb.info.pagelist;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.alwertus.digimemb.auth.User;

import java.util.List;
import java.util.Optional;

public interface InfoPagesItemRepo extends CrudRepository<InfoPagesItem, Long> {
//    @Query("select p from InfoPagesItem p where (p.creator = :id and p.access = 'PRIVATE') or p.access = 'ALL'")
//    List<InfoPagesItem> findAllByUserId(@Param("id") Long id);

    @Query("select p from InfoPagesItem p where (p.creator = :u and p.access = 'PRIVATE') or p.access = 'ALL'")
    List<InfoPagesItem> findAllByUserId(@Param("u") User user);


    @Query("select p from InfoPagesItem p where p.access = 'ALL'")
    List<InfoPagesItem> findAll();

    Optional<InfoPagesItem> findById(Long id);
}
