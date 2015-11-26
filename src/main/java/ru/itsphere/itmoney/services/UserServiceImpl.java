package ru.itsphere.itmoney.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itsphere.itmoney.dao.DAOException;
import ru.itsphere.itmoney.dao.UserDAO;
import ru.itsphere.itmoney.domain.User;

import java.util.List;

/**
 * Реализация UserService
 * <p>
 * http://it-channel.ru/
 *
 * @author Budnikov Aleksandr
 */
@Service
public class UserServiceImpl implements UserService {
    /**
     * Подключили логгер к текущему классу
     */
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDAO userDAO;

    public User getById(int id) {
        try {
            return userDAO.getById(id);
        } catch (DAOException e) {
            throw new ServiceException(String.format("Getting user by id %s", id), e);
        }
    }

    @Override
    public void save(User user) {
        try {
            userDAO.save(user);
        } catch (Exception e) {
            throw new ServiceException(String.format("Save user %s", user), e);
        }
    }

    @Override
    public void update(User user) {
        try {
            userDAO.update(user);
        } catch (Exception e) {
            throw new ServiceException(String.format("Update user %s", user), e);
        }
    }

    @Override
    public List<User> getAll() {
        try {
            return userDAO.getAll();
        } catch (Exception e) {
            throw new ServiceException("Users aren't getting", e);
        }
    }

    @Override
    public void deleteById(int id) {
        try {
            userDAO.deleteById(id);
        } catch (Exception e) {
            throw new ServiceException(String.format("Deleting user by id %s", id), e);
        }
    }

    @Override
    public int getCount() {
        try {
            return userDAO.getCount();
        } catch (Exception e) {
            throw new ServiceException("Problem in getCount", e);
        }
    }

    @Override
    public List<User> findUsersByQuery(String query) {
        try {
            return userDAO.findUsersByQuery(query);
        } catch (Exception e) {
            throw new ServiceException(String.format("findUsersByQuery user by query %s", query), e);
        }
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
}
