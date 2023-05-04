package lee.library_back.controller;

import lee.library_back.LibraryManagementSystemImpl;
import lee.library_back.entities.Borrow;
import lee.library_back.entities.Card;
import lee.library_back.queries.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ReturnController {
    private final LibraryManagementSystemImpl libraryManagementSystem;
    @Autowired
    public ReturnController(LibraryManagementSystemImpl libraryManagementSystem) {
        this.libraryManagementSystem = libraryManagementSystem;
    }

    @PostMapping("/return")
    @ResponseBody
    @CrossOrigin
    public ApiResult borrow(@RequestBody Map<String, Object> requestBody) {
        int bookId = (int) requestBody.get("bookId");
        int carId = (int) requestBody.get("cardId");
        Borrow borrow = new Borrow();
        borrow.setBookId(bookId);
        borrow.setCardId(carId);
        borrow.resetReturnTime();
        ApiResult result = libraryManagementSystem.returnBook(borrow);
        return result;
    }
}