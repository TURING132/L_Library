package lee.library_back.controller;

import lee.library_back.LibraryManagementSystemImpl;
import lee.library_back.entities.Book;
import lee.library_back.queries.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ImpController {
    private final LibraryManagementSystemImpl libraryManagementSystem;
    @Autowired
    public ImpController(LibraryManagementSystemImpl libraryManagementSystem) {
        this.libraryManagementSystem = libraryManagementSystem;
    }

    @PostMapping("/imp")
    @ResponseBody
    @CrossOrigin
    public ApiResult imp(@RequestBody List<Book> books) {
        return libraryManagementSystem.storeBook(books);
    }
}
