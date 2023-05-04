package lee.library_back.controller;

import lee.library_back.LibraryManagementSystemImpl;
import lee.library_back.entities.Card;
import lee.library_back.queries.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class LogOutController {
    private final LibraryManagementSystemImpl libraryManagementSystem;
    @Autowired
    public LogOutController(LibraryManagementSystemImpl libraryManagementSystem) {
        this.libraryManagementSystem = libraryManagementSystem;
    }
    @PostMapping("/logout")
    @ResponseBody
    @CrossOrigin
    public ApiResult register(@RequestBody Map<String, Object> requestBody) {
        int cardId = (int) requestBody.get("cardId");
        return libraryManagementSystem.removeCard(cardId);
    }
}
