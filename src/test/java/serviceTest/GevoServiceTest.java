package serviceTest;

import data.identifier.UserId;
import data.user.User;
import data.user.UserName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.GevoService;
import service.KontoService;
import service.UserService;
import service.serviceexception.ServiceException;

public class GevoServiceTest {
    private final GevoService gevoService;
    private final UserService userService;
    private final KontoService kontoService;

    public GevoServiceTest(GevoService gevoService, UserService userService, KontoService kontoService) {
        this.gevoService = gevoService;
        this.userService = userService;
        this.kontoService = kontoService;
    }
    @Test
    public void KontoAuszugwrapperTest () throws ServiceException {
        User user = new User(new UserId("31df4494-3a25-457f-978b-b1d694"), new UserName("l.sindermann@hsw-stud.de"));
        Assertions.assertNotNull(gevoService.fetchTransaktionsHistorie(user.getUserId()));
    }
}
