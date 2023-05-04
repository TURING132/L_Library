package lee.library_back.config;
import lee.library_back.LibraryManagementSystemImpl;
import lee.library_back.utils.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileNotFoundException;

@Configuration
public class AppConfig {

    @Bean
    public LibraryManagementSystemImpl libraryManagementSystem() throws FileNotFoundException, ClassNotFoundException {
        ConnectConfig connectConfig = new ConnectConfig();
        DatabaseConnector databaseConnector = new DatabaseConnector(connectConfig);
        databaseConnector.connect();
        return new LibraryManagementSystemImpl(databaseConnector);
    }
}
