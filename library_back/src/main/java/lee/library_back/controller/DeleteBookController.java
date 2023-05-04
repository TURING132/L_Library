package lee.library_back.controller;

import lee.library_back.LibraryManagementSystemImpl;
import lee.library_back.entities.Book;
import lee.library_back.queries.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class DeleteBookController {
    private final LibraryManagementSystemImpl libraryManagementSystem;
    @Autowired
    public DeleteBookController(LibraryManagementSystemImpl libraryManagementSystem) {
        this.libraryManagementSystem = libraryManagementSystem;
    }

    @PostMapping("/delbook")
    @ResponseBody
    @CrossOrigin
    public ApiResult borrow(@RequestBody Map<String, Object> requestBody) {
        int bookId = (int)requestBody.get("bookId");
        ApiResult result = libraryManagementSystem.removeBook(bookId);

        return result;
    }
}
