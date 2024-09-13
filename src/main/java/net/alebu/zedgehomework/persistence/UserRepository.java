package net.alebu.zedgehomework.persistence;

import net.alebu.zedgehomework.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    default UserEntity requireById(Long userId) {
        return findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User with ID " + userId + " not found")
        );
    }

    boolean existsByEmail(String email);
}
