package ru.itsphere.itmoney.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import ru.itsphere.itmoney.controllers.AbstractController;

import java.util.Map;

/**
 * По clientRequest определяет какой контроллер должен обработать запрос
 * <p>
 * http://it-channel.ru/
 *
 * @author Budnikov Aleksandr
 */
@Component
public class ControllerResolver {
    /**
     * Подключили логгер к текущему классу
     */
    private static final Logger logger = LogManager.getLogger(ControllerResolver.class);

    private Map<String, Object> controllers;

    public AbstractController getController(ClientRequest clientRequest) {
        AbstractController controller = (AbstractController) controllers.get(clientRequest.getController());
        if (controller == null) {
            logger.fatal("Controller {} wasn't registered", clientRequest.getController());
            throw new RuntimeException("Controller '" + clientRequest.getController() + "' wasn't registered");
        }
        return controller;
    }

    public void setControllers(Map<String, Object> controllers) {
        this.controllers = controllers;
    }
}
