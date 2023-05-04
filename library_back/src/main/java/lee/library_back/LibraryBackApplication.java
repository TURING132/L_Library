package lee.library_back;

import lee.library_back.entities.Book;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lee.library_back.utils.ConnectConfig;
import lee.library_back.utils.DatabaseConnector;

import java.util.logging.Logger;

@SpringBootApplication
public class LibraryBackApplication {
	private static final Logger log = Logger.getLogger(LibraryBackApplication.class.getName());
	public static void main(String[] args) {
		SpringApplication.run(LibraryBackApplication.class, args);
	}

}
