package serviceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.UserRepository;
import service.UserService;

import static org.mockito.Mockito.mock;

public class UserServiceTest {


    private UserService userService;
    private UserRepository userRepository;


    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }


    @Test
    void





}
