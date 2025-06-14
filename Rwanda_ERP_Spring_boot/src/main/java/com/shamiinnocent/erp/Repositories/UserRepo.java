//package com.shamiinnocent.erp.Repositories;
//
//import com.shamiinnocent.erp.Models.User;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
//@Repository
//public interface UserRepo extends JpaRepository<User, Long> {
//    /**
//     * Finds a user by their email address.
//     *
//     * @param email the email address to search for
//     * @return an optional containing the user, or empty if not found
//     */
//    Optional<User> findByEmail(String email);
//
//    /**
//     * Checks if a user exists with the given email address.
//     *
//     * @param email the email address to check
//     * @return true if a user exists, false otherwise
//     */
//    boolean existsByEmail(String email);
//}
