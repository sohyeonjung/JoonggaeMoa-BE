package org.silsagusi.batch.infrastructure.repository;

import java.util.Optional;
import java.util.Collection;
import java.util.Set;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.silsagusi.core.domain.article.Complex;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplexRepository extends JpaRepository<Complex, Long> {

	Optional<Complex> findFirstByComplexCode(String complexCode);

	@Query("select c.complexCode from Complex c where c.complexCode in :codes")
	Set<String> findExistingCodes(@Param("codes") Collection<String> codes);
}
