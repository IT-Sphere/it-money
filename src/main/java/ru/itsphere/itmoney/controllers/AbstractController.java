package ru.itsphere.itmoney.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.itsphere.itmoney.servlets.ClientRequest;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Все контроллеры должны расширять этот класс
 * <p>
 * http://it-channel.ru/
 *
 * @author Budnikov Aleksandr
 */
public abstract class AbstractController {
    /**
     * Подключили логгер к текущему классу
     */
    private static final Logger logger = LogManager.getLogger(AbstractController.class);

    /**
     * Находит правильный обработчик и выполняет запрос
     * Затем сериализует результат
     *
     * @param clientRequest запрос клиента
     * @return сериализуемый результат выполнения запроса
     */
    public String handleRequest(ClientRequest clientRequest) {
        // Логируем входные параметры запроса
        logger.entry(clientRequest);
        try {
            // Получили имя метода в контроллере
            String methodName = clientRequest.getAction();
            // this.getClass() - объект-класс текущего контроллера (UserController).
            // Получили метод этого контроллера
            Method methodAction = this.getClass().getDeclaredMethod(methodName, Map.class);
            // Выполнили этот метод и получили результат
            Serializable result = (Serializable) methodAction.invoke(this, clientRequest.getParams());
            // Логируем ответ сервера клиенту
            return logger.exit(ResponseCreator.process(result));
        } catch (ApplicationException e) {
            logger.error("Client request %s has thrown an exception", clientRequest, e);
            // Логируем ответ сервера клиенту
            return logger.exit(ResponseCreator.processError(e));
        } catch (Exception e) {
            logger.fatal("Client request %s has thrown an exception" , clientRequest, e);
            return logger.exit(ResponseCreator.processError(e));
        }
    }

    /**
     * Возвращает все доступные обработчики
     *
     * @return все доступные обработчики
     */
}
