package lee.library_back.controller;

import lee.library_back.LibraryManagementSystemImpl;
import lee.library_back.queries.BookQueryConditions;
import lee.library_back.queries.BookQueryResults;
import lee.library_back.queries.BorrowHistories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class HistoryController {
    private final LibraryManagementSystemImpl libraryManagementSystem;
    @Autowired
    public HistoryController(LibraryManagementSystemImpl libraryManagementSystem) {
        this.libraryManagementSystem = libraryManagementSystem;
    }
    @GetMapping("/history")
    @ResponseBody
    @CrossOrigin
    public BorrowHistories query(@RequestParam(value = "cardId",required = true) int cardId){

        return (BorrowHistories)libraryManagementSystem.showBorrowHistory(cardId).payload;

    }

}