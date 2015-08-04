package ru.itsphere.itmoney.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.WebApplicationInitializer;
import ru.itsphere.itmoney.servlets.ControllerResolver;
import ru.itsphere.itmoney.servlets.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Этот класс инициализирует приложение
 * <p>
 * http://it-channel.ru/
 *
 * @author Budnikov Aleksandr
 */
public class ApplicationInitializer implements WebApplicationInitializer {

    public static final String CONTROLLER_RESOLVER = "controllerResolver";
    public static final String CONTEXT_XML = "context.xml";

    @Override
    public void onStartup(ServletContext container) throws ServletException {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(CONTEXT_XML);
        ControllerResolver controllerResolver = (ControllerResolver) applicationContext.getBean(CONTROLLER_RESOLVER);
        registerDispatcherServlet(container, controllerResolver);
    }

    /**
     * Данный метод делает то что обычно делают в web.xml
     * <servlet>
     *   <servlet-name>DispatcherServlet</servlet-name>
     *   <servlet-class>
     *     ru.itsphere.itmoney.servlets.DispatcherServlet
     *   </servlet-class>
     * </servlet>
     *
     * <servlet-mapping>
     *   <servlet-name>DispatcherServlet</servlet-name>
     *   <url-pattern>/store/*</url-pattern>
     * </servlet-mapping>
     *
     * @param container ServletContext
     * @param controllerResolver ControllerResolver
     */
    private void registerDispatcherServlet(ServletContext container, ControllerResolver controllerResolver) {
        ServletRegistration.Dynamic dispatcher = container.addServlet(DispatcherServlet.class.getName(), new DispatcherServlet(controllerResolver));
        dispatcher.addMapping(DispatcherServlet.MAPPING_PATH);
    }
}
