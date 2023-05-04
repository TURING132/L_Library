package lee.library_back.controller;

import lee.library_back.LibraryManagementSystemImpl;
import lee.library_back.queries.ApiResult;
import lee.library_back.queries.BorrowHistories;
import lee.library_back.queries.CardList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ShowCardController {
    private final LibraryManagementSystemImpl libraryManagementSystem;
    @Autowired
    public ShowCardController(LibraryManagementSystemImpl libraryManagementSystem) {
        this.libraryManagementSystem = libraryManagementSystem;
    }
    @GetMapping("/showCard")
    @ResponseBody
    @CrossOrigin
    public Object query(){
        return libraryManagementSystem.showCards();
    }

}
