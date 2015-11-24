package ru.itsphere.itmoney.controllers;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.itsphere.itmoney.domain.User;
import ru.itsphere.itmoney.services.ServiceException;
import ru.itsphere.itmoney.services.UserService;

import java.util.HashMap;
import java.util.Map;

/**
 * Это класс контроллер для работы с пользователями он обрабатывает запросы DispatcherServlet
 * <p>
 * http://it-channel.ru/
 *
 * @author Budnikov Aleksandr
 */
@Controller
public class UserController extends AbstractController {
    /**
     * Подключили логгер к текущему классу
     */
    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    private Map<Actions, Executable> handlers;

    public UserController() {
        handlers = new HashMap<>();
        handlers.put(Actions.GET_BY_ID, getById);
        handlers.put(Actions.SAVE, save);
        handlers.put(Actions.GET_ALL, getAll);
        handlers.put(Actions.DELETE_BY_ID, deleteById);
        handlers.put(Actions.GET_COUNT, getCount);
        handlers.put(Actions.FIND_USERS_BY_QUERY,findUsersByQuery);
    }

    private Executable getById = (params) -> {
        try {
            if (params.get("id") == null) {
                logger.warn("Action {} incoming param id is null", Actions.GET_BY_ID);
                return null;
            }
            int id = Integer.parseInt(params.get("id"));
            User user = userService.getById(id);
            return wrap(user);
        } catch (ServiceException e) {
            throw new ApplicationException(String.format("Action getById with params (%s) has thrown an exception", params), e);
        }
    };

    private Executable save = (params) -> {
        try {
            User newUser = convertMapToUser(params);
            if (params.get("id") == null) {
                userService.save(newUser);
                return wrap(newUser);
            } else {
                userService.update(newUser);
                return wrap(newUser);
            }
        } catch (Exception e) {
            throw new ApplicationException(String.format("Action save with params (%s) has thrown an exception", params), e);
        }
    };

    private Executable getAll = (params) -> {
        try {
            return wrap(userService.getAll());
        } catch (Exception e) {
            throw new ApplicationException(String.format("Action getAll with params (%s) has thrown an exception", params), e);
        }
    };

    private Executable findUsersByQuery = (params) -> {
        try {
            String query = String.valueOf(params.get("query"));
            return wrap(userService.findUsersByQuery(query));
        } catch (Exception e) {
            throw new ApplicationException(String.format("Action getAll with params (%s) has thrown an exception", params), e);
        }
    };

    private Executable deleteById = (params) -> {
        try {
            if (params.get("id") == null) {
                return null;
            }
            int id = Integer.parseInt(params.get("id"));
            userService.deleteById(id);
            return null;
        } catch (Exception e) {
            throw new ApplicationException(String.format("Action deleteBuId with params (%s) has thrown an exception", params), e);
        }
    };

    private Executable getCount = (params) -> {
        try {
            return wrap(userService.getCount());
        } catch (Exception e) {
            throw new ApplicationException(String.format("Action getCount with params (%s) has thrown an exception", params), e);
        }
    };

    private String wrap(Object object) {
        return new Gson().toJson(object);
    }

    private User convertMapToUser(Map<String, String> params) {
        String name = params.get("name");
        if (params.get("id") == null) {
            return new User(name);
        }
        int id = Integer.parseInt(params.get("id"));
        return new User(id, name);
    }

    @Override
    public Map<Actions, Executable> getHandlers() {
        return this.handlers;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
