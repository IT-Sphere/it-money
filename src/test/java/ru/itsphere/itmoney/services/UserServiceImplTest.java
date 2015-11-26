package ru.itsphere.itmoney.services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls;
import org.mockito.runners.MockitoJUnitRunner;
import ru.itsphere.itmoney.dao.UserDAO;
import ru.itsphere.itmoney.domain.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
    public static final int INEXISTENT_USER_ID = 2;
    public static final String QUERY = "s";
    private User userSasha = new User(0, "Sasha");
    private User userDima = new User(1, "Dima");
    private User newUser = new User(2, "Igor");
    private List<User> allUsers = Arrays.asList(new User[]{userSasha, userDima});

    @Mock(answer = Answers.RETURNS_SMART_NULLS)
    private UserDAO userDAO;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testGetByIdSuccessfully() {
        when(userDAO.getById(userSasha.getId())).thenReturn(userSasha);
        User expectedUser = userSasha;
        User actualUser = userService.getById(expectedUser.getId());
        Assert.assertNotNull(actualUser);
        Assert.assertEquals(expectedUser, actualUser);
    }

    @Test
    public void testGetByIdNotExistedUser() {
        when(userDAO.getById(INEXISTENT_USER_ID)).thenReturn(null);
        User user = userService.getById(INEXISTENT_USER_ID);
        Assert.assertNull(user);

    }

    @Test
    public void testSaveSuccessfully() {
        doAnswer(invocation -> {
            User newUser = (User) invocation.getArguments()[0];
            Assert.assertNotNull(newUser);
            Assert.assertEquals(newUser, this.newUser);
            return null;
        }).when(userDAO).save(newUser);
        userService.save(newUser);
    }

    @Test
    public void testUpdateSuccessfully() {
        doAnswer(invocation -> {
            String name = "Igor";
            User userDima = (User) invocation.getArguments()[0];
            userDima.setName(name);
            Assert.assertEquals(userDima.getName(), this.userDima.getName());
            return null;
        }).when(userDAO).update(userDima);
        userService.update(userDima);
    }

    @Test(expected = RuntimeException.class)
    public void testUpdateFail() {
        User user = new User(INEXISTENT_USER_ID, "Igor");
        doThrow(new RuntimeException()).when(userDAO).update(user);
        userService.update(user);
    }

    @Test
    public void testGetAllSuccessfully() {
        when(userDAO.getAll()).thenReturn(allUsers);
        List<User> users = userService.getAll();
        Assert.assertEquals(allUsers, users);
    }

    @Test
    public void testGetAllEmpty() {
        List<User> users = new ArrayList<>();
        when(userDAO.getAll()).thenReturn(users);
        List<User> usersTest = userService.getAll();
        Assert.assertTrue(users.isEmpty());
    }

    @Test
    public void testGetCountSuccessfully() {
        when(userDAO.getCount()).thenReturn(allUsers.size());
        int count = userService.getCount();
        Assert.assertEquals(allUsers.size(), count);
    }

    public void testGetCountEmpty() {
        List<User> users = new ArrayList<>();
        when(userDAO.getCount()).thenReturn(users.size());
        int count = userService.getCount();
        Assert.assertEquals(allUsers.size(), count);
    }

    @Test
    public void testFindUsersByQuerySuccessfully() {
        List<User> users = new ArrayList<>();
        users.add(userSasha);
        when(userDAO.findUsersByQuery(QUERY)).thenReturn(users);
        List<User> testUsers = userService.findUsersByQuery(QUERY);
        Assert.assertEquals(users,testUsers);
    }

    @Test
    public void testFindUsersByQueryEmpty() {
        List<User> users = new ArrayList<>();
        when(userDAO.findUsersByQuery(QUERY)).thenReturn(users);
        List<User> testUsers = userService.findUsersByQuery(QUERY);
        Assert.assertTrue(testUsers.isEmpty());
    }

    @Test
    public void testDeleteByIdSuccessfully() {
        List<User> users = new ArrayList<>();
        users.add(userSasha);
        users.add(userDima);
        doAnswer(invocation -> {
            int id = (int) invocation.getArguments()[0];
            users.remove(id);
            Assert.assertNotEquals(allUsers.size(), users.size());
            return null;
        }).when(userDAO).deleteById(userDima.getId());
        userService.deleteById(userDima.getId());
    }

    @Test(expected = RuntimeException.class)
    public void testDeleteByIdFail() {
        doThrow(new RuntimeException()).when(userDAO).deleteById(INEXISTENT_USER_ID);
        userService.deleteById(INEXISTENT_USER_ID);
    }
}
