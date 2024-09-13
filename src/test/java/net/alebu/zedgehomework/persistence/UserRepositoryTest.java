package net.alebu.zedgehomework.persistence;

import net.alebu.zedgehomework.persistence.entities.UserEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldCreateUser() {
        UserEntity user = new UserEntity();
        user.setEmail("test@email.com");
        userRepository.save(user);
        Assertions.assertThat(userRepository.findById(user.getId())).isNotNull();
    }
}