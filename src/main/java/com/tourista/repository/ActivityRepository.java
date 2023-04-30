package com.tourista.repository;

import com.tourista.domain.Activity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Activity entity.
 */
@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    default Optional<Activity> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Activity> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Activity> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct activity from Activity activity left join fetch activity.location",
        countQuery = "select count(distinct activity) from Activity activity"
    )
    Page<Activity> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct activity from Activity activity left join fetch activity.location")
    List<Activity> findAllWithToOneRelationships();

    @Query("select activity from Activity activity left join fetch activity.location where activity.id =:id")
    Optional<Activity> findOneWithToOneRelationships(@Param("id") Long id);
}
