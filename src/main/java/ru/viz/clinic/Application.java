package ru.viz.clinic;

import com.vaadin.flow.component.page.*;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.shared.communication.PushMode;
import com.vaadin.flow.shared.ui.Transport;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@PageTitle("Клиника")
@BodySize(height = "100vh", width = "100vw")
@Meta(name = "author", content = "bunny")

//TODO was ist das?
//@Inline(wrapping = Inline.Wrapping.AUTOMATIC,
//        position = Inline.Position.APPEND,
//        target = TargetElement.BODY,
//        value = "./custom.html")
//@PWA(name = "Клиника", shortName = "Клиника")
//@Push(value = PushMode.MANUAL, transport = Transport.WEBSOCKET)
@Theme(value = "vizclinic", variant =  Lumo.DARK)
public class Application implements AppShellConfigurator {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

//    @Bean
//    SqlDataSourceScriptDatabaseInitializer dataSourceScriptDatabaseInitializer(DataSource dataSource,
//            SqlInitializationProperties properties, UserRepository repository) {
//        // This bean ensures the database is only initialized when empty
//        return new SqlDataSourceScriptDatabaseInitializer(dataSource, properties) {
//            @Override
//            public boolean initializeDatabase() {
//                if (repository.count() == 0L) {
//                    return super.initializeDatabase();
//                }
//                return false;
//            }
//        };
//    }
}
