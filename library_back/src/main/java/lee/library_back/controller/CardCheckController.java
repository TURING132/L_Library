package lee.library_back.controller;

import lee.library_back.LibraryManagementSystemImpl;
import lee.library_back.queries.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class CardCheckController {
    private final LibraryManagementSystemImpl libraryManagementSystem;
    @Autowired
    public CardCheckController(LibraryManagementSystemImpl libraryManagementSystem) {
        this.libraryManagementSystem = libraryManagementSystem;
    }
    @GetMapping("/checkCard")
    @ResponseBody
    @CrossOrigin
    public ApiResult checkCard(@RequestParam(value = "cardId",required = true) int cardId)  {
        return  libraryManagementSystem.checkCard(cardId);
    }
}
