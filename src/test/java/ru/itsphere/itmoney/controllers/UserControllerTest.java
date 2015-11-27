package ru.itsphere.itmoney.controllers;


import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.itsphere.itmoney.domain.User;
import ru.itsphere.itmoney.services.UserService;

import java.io.Serializable;
import java.util.*;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
    public static final String INEXISTENT_USER_ID_STRING = null;
    public static final int INEXISTENT_USER_ID = 2;
    public static final String QUERY = "s";
    private String userSashaId = "0";
    private String userSashaName = "Sasha";
    private String userDimaId = "1";
    private String userDimaName = "Dima";
    private User userSasha = new User(0, "Sasha");
    private User userDima = new User(1, "Dima");
    private User newUser = new User(2, "Igor");
    private List<User> allUsers = Arrays.asList(new User[]{userSasha, userDima});
    private List<User> emptyList = new ArrayList<>();
    private Map<String, String> params = new HashMap<>();

    @Mock(answer = Answers.RETURNS_SMART_NULLS)
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private String wrap(Object object) {
        return new Gson().toJson(object);
    }
    private Object unwrap(String str) {return new Gson().fromJson(str, Object.class);}

    @Test
    public void testGetByIdSuccessfully() {
        params.put("id", userDimaId);
        when(userService.getById(userDima.getId())).thenReturn(userDima);
        Serializable userTest = userController.getById(params);
        Serializable user = wrap(userDima);
        Assert.assertEquals(user, userTest);
    }

    @Test
    public void testGetByIdNotExistedUser() {
        params.put("id", INEXISTENT_USER_ID_STRING);
        Serializable testUser = userController.getById(params);
        Assert.assertNull(testUser);
    }

    @Test
    public void testSaveNewUserSuccessfully() {
        params.put("INEXISTENT_USER_ID_STRING", "Igor");
        doAnswer(invocation -> {
            User newUser = (User) invocation.getArguments()[0];
            Assert.assertNotNull(newUser);
            Assert.assertEquals(newUser.getId(), this.newUser.getId());
            Assert.assertEquals(newUser.getName(), this.newUser.getName());
            return null;
        }).when(userService).save(newUser);
        userController.save(params);
    }

    @Test
    public void testSaveUpdateUserSuccefully() {
        params.put("id", userDimaId);
        doAnswer(invocation -> {
            User testUser = (User) invocation.getArguments()[0];
            Assert.assertNotNull(testUser);
            Assert.assertEquals(testUser.getId(), this.userDima.getId());
            Assert.assertEquals(testUser.getName(), this.userDima.getName());
            return null;
        }).when(userService).save(userDima);
        userController.save(params);
    }

    @Test
    public void testGetAllSuccessfully() {
        params.put(userSashaId, userSashaName);
        params.put(userDimaId, userDimaName);
        when(userService.getAll()).thenReturn(allUsers);
        Serializable usresTest = userController.getAll(params);
        Serializable users = wrap(allUsers);
        Assert.assertEquals(users, usresTest);
    }

    @Test
    public void testGetAllEmpty() {
        List<User> tmp = new ArrayList<>();
        when(userService.getAll()).thenReturn(tmp);
        Serializable usresTest = userController.getAll(params);
        Serializable users = wrap(tmp);
        Assert.assertEquals(users, usresTest);
    }

    @Test
    public void testGetCountSuccessfully() {
        params.put(userSashaId, userSashaName);
        params.put(userDimaId, userDimaName);
        when(userService.getCount()).thenReturn(params.size());
        Serializable countTest = userController.getCount(params);
        Serializable count = wrap(params.size());
        Assert.assertEquals(count, countTest);
    }

    @Test
    public void testGetCountEmpty() {
        when(userService.getAll()).thenReturn(emptyList);
        Serializable countTest = userController.getCount(params);
        Serializable count = wrap(emptyList.size());
        Assert.assertEquals(count, countTest);
    }

    @Test
    public void testFindUsersByQuerySuccessfully() {
        params.put("query", QUERY);
        List<User> userForQuery = new ArrayList<>();
        userForQuery.add(userSasha);
        when(userService.findUsersByQuery(QUERY)).thenReturn(userForQuery);
        Serializable usersTest = userController.findUsersByQuery(params);
        Serializable users = wrap(userForQuery);
        Assert.assertEquals(users, usersTest);
    }

    @Test
    public void testFindUsersByQueryEmpty() {
        params.put("query", QUERY);
        List<User> userForQuery = new ArrayList<>();
        when(userService.findUsersByQuery(QUERY)).thenReturn(userForQuery);
        Serializable usersTest = userController.findUsersByQuery(params);
        Serializable users = wrap(userForQuery);
        Assert.assertEquals(users, usersTest);
    }

    @Test
    public void testDeleteByIdSuccessfully() {
        params.put("id", userDimaId);
        List<User> users = new ArrayList<>();
        users.add(userSasha);
        users.add(userDima);
        Assert.assertEquals(allUsers, users);
        doAnswer(invocation -> {
            int id = (int)invocation.getArguments()[0];
            users.remove(id);
            Assert.assertNotEquals(users, allUsers);
            return null;
        }).when(userService).deleteById(userDima.getId());
        userController.deleteById(params);
    }
}
