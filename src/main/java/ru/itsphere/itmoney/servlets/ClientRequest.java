package ru.itsphere.itmoney.servlets;

import ru.itsphere.itmoney.controllers.Actions;
import ru.itsphere.itmoney.controllers.Controllers;

import java.util.Map;

/**
 * Этот класс имитирует пользовательский запрос
 * <p>
 * http://it-channel.ru/
 *
 * @author Budnikov Aleksandr
 */
public class ClientRequest {
    private String action;
    private String controller;
    private Map<String, String> params;

    public ClientRequest(String action, String controller, Map<String, String> params) {
        this.action = action;
        this.controller = controller;
        this.params = params;
    }

    public Actions getAction() {
        return Actions.valueOf(action);
    }

    public Controllers getController() {
        return Controllers.valueOf(controller);
    }

    public Map<String, String> getParams() {
        return params;
    }
}
