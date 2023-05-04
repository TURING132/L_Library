package lee.library_back.controller;

import lee.library_back.LibraryManagementSystemImpl;
import lee.library_back.entities.Book;
import lee.library_back.queries.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class StockController {
    private final LibraryManagementSystemImpl libraryManagementSystem;
    @Autowired
    public StockController(LibraryManagementSystemImpl libraryManagementSystem) {
        this.libraryManagementSystem = libraryManagementSystem;
    }

    @PostMapping("/stock")
    @ResponseBody
    @CrossOrigin
    public ApiResult borrow(@RequestBody Map<String, Object> requestBody) {
        int delta = (int)requestBody.get("delta");
        int bookId = (int)requestBody.get("bookId");
        return libraryManagementSystem.incBookStock(bookId,delta);
    }
}

