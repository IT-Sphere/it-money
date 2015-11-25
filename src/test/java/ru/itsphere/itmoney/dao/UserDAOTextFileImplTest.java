package ru.itsphere.itmoney.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.itsphere.itmoney.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Тесты для UserDAOTextFileImpl
 * <p>
 * http://it-channel.ru/
 *
 * @author Budnikov Aleksandr
 */
public class UserDAOTextFileImplTest {
    public static final String SEPARATOR = UserDAOTextFileImpl.SEPARATOR;

    public static final String USER_ID_TITLE = "userId";
    public static final String USER_NAME_TITLE = "userName";
    public static final int USER_1_ID = 0;
    public static final String USER_1_NAME = "Sasha";
    public static final int USER_2_ID = 1;
    public static final String USER_2_NAME = "Dima";

    public static final int FIRST_USER_ID = USER_1_ID;
    public static final int LAST_USER_ID = USER_2_ID;
    public static final int INEXISTENT_USER_ID = 2;

    private UserDAO userDAO;
    private List<String> lines;

    @Before
    public void setUp() {
        lines = getLines();
        userDAO = createUserDAO(lines);
    }

    private UserDAO createUserDAO(List<String> lines) {
        UserDAOTextFileImpl userDAO = new UserDAOTextFileImpl();
        userDAO.setReaderFactory(new MockDataReaderFactory(lines));
        userDAO.setWriterFactory(new MockDataWriterFactory(lines));
        return userDAO;
    }



    private List<String> getLines() {
        List<String> lines = new ArrayList<>();
        lines.add(USER_ID_TITLE + SEPARATOR + USER_NAME_TITLE);
        lines.add(USER_1_ID + SEPARATOR + USER_1_NAME);
        lines.add(USER_2_ID + SEPARATOR + USER_2_NAME);
        return lines;
    }

    @Test
    public void testGetByIdExistedUser() {
        int id = USER_1_ID;
        String name = USER_1_NAME;
        User user = userDAO.getById(id);
        Assert.assertNotNull("User with id " + id + " is null", user);
        Assert.assertEquals("User id " + user.getId() + " != " + id, user.getId(), id);
        Assert.assertEquals("User name " + user.getName() + " != " + name, user.getName(), name);
    }

    @Test
    public void testGetByIdNotExistedUser() {
        int id = INEXISTENT_USER_ID;
        User user = userDAO.getById(id);
        Assert.assertNull("User with id " + id + " is not null", user);
    }

    @Test
    public void testSaveSuccessfully() {
        String name = "Саша";
        int id = 2;
        userDAO.save(new User(name));
        String expected1 = id + SEPARATOR + name;
        String actual = lines.get(lines.size() - 1);
        Assert.assertEquals(expected1 + "!=" + actual, expected1, actual);
    }

    @Test
    public void testUpdateSuccessfully() {
        User originalUser = new User(USER_1_ID, USER_2_NAME);
        User userWithChangedName = new User(originalUser.getId(), "Alex");
        userDAO.update(userWithChangedName);
        String actual = lines.get(USER_1_ID + 1);
        String unexpected = originalUser.getId() + SEPARATOR + originalUser.getName();
        Assert.assertNotEquals(unexpected + "==" + actual, unexpected, actual);
    }

    @Test(expected = RuntimeException.class)
    public void testUpdateFail() {
        userDAO.update(new User(INEXISTENT_USER_ID, ""));
    }

    @Test
    public void testGetAllSuccessfully() {
        List<User> users = userDAO.getAll();
        Assert.assertNotNull("List is null", users);
        Assert.assertEquals("List hasn't (lines.size() - 1) items", lines.size() - 1, users.size());
        User user = users.get(USER_2_ID);
        String actual = user.getId() + SEPARATOR + user.getName();
        String expected = USER_2_ID + SEPARATOR + USER_2_NAME;
        Assert.assertEquals(expected + "!=" + actual, expected, actual);
    }

    @Test
    public void testGetAllEmpty() {
        lines.remove(1);
        lines.remove(1);
        List<User> users = userDAO.getAll();
        Assert.assertNotNull("List is null", users);
        Assert.assertTrue("List isn't empty", users.isEmpty());
    }

    @Test
    public void testDeleteByIdSuccessfully() {
        int id = USER_1_ID;
        userDAO.deleteById(id);
        String expected = USER_2_ID + SEPARATOR + USER_2_NAME;
        String actual = lines.get(USER_1_ID + 1);
        Assert.assertEquals(expected + "!=" + actual, expected, actual);
    }

    @Test(expected = RuntimeException.class)
    public void testDeleteByIdFail() {
        userDAO.deleteById(INEXISTENT_USER_ID);
    }

    /**
     * Эти тест не удалять и не трогать!
     * Нужен для контроля ваших ошибок
     *
     * @throws Exception
     */
    @Test
    public void testDeleteByIdDeleteFirstDeleteLast() throws Exception {
        // Был баг. После удаления первого пользователя, удаление последнего выбросит исключение.
        userDAO.deleteById(FIRST_USER_ID);
        userDAO.deleteById(LAST_USER_ID);
    }

    /**
     * Эти тест не удалять и не трогать!
     * Нужен для контроля ваших ошибок
     *
     * @throws Exception
     */
    @Test
    public void testDeleteByIdSaveFirstUser() throws Exception {
        // Был баг. После удаления всех пользователей, сохранение нового - выбрасывает исключение.
        // Или когда просто сохраняешь первого пользователя.
        userDAO.deleteById(USER_1_ID);
        userDAO.deleteById(USER_2_ID);
        userDAO.save(new User(USER_1_ID, USER_1_NAME));
    }

    /**
     * Эти тест не удалять и не трогать!
     * Нужен для контроля ваших ошибок
     *
     * @throws Exception
     */
    @Test
    public void testUpdateDeleteFirstUpdateLast() throws Exception {
        // Был баг. После удаления первого пользователя, изменение последнего выбросит исключение.
        userDAO.deleteById(FIRST_USER_ID);
        userDAO.update(new User(LAST_USER_ID, ""));
    }

    @Test
    public void testFindUsersByQuerySuccessfully() {
        List<User> users = userDAO.findUsersByQuery(USER_2_NAME);
        User user = users.get(0);
        String actual = user.getId() + SEPARATOR + user.getName();
        String expected = USER_2_ID + SEPARATOR + USER_2_NAME;
        Assert.assertEquals("findUsersBuQuery isn't working", expected, actual);

    }

    @Test
    public void testFindUsersByQueryFail() {
        List<User> users = userDAO.findUsersByQuery(USER_1_NAME + USER_2_NAME);
        Assert.assertTrue("Users isn't empty", users.isEmpty());
    }
}
