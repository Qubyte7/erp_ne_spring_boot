package com.shamiinnocent.erp.Repositories;

import com.shamiinnocent.erp.Models.Deduction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for managing Deduction entities.
 */
@Repository
public interface DeductionRepo extends JpaRepository<Deduction, Long> {
    /**
     * Finds a deduction by its code.
     *
     * @param code the deduction code to search for
     * @return an optional containing the deduction, or empty if not found
     */
    Optional<Deduction> findByCode(String code);

    /**
     * Finds a deduction by its name.
     *
     * @param name the deduction name to search for
     * @return an optional containing the deduction, or empty if not found
     */
    Optional<Deduction> findByName(String name);

    /**
     * Checks if a deduction exists with the given code.
     *
     * @param code the deduction code to check
     * @return true if a deduction exists, false otherwise
     */
    boolean existsByCode(String code);

    /**
     * Checks if a deduction exists with the given name.
     *
     * @param name the deduction name to check
     * @return true if a deduction exists, false otherwise
     */
    boolean existsByName(String name);
}