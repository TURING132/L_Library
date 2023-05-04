package lee.library_back.controller;

import lee.library_back.LibraryManagementSystemImpl;
import lee.library_back.entities.Card;
import lee.library_back.queries.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class RegisterContorller {
    private final LibraryManagementSystemImpl libraryManagementSystem;
    @Autowired
    public RegisterContorller(LibraryManagementSystemImpl libraryManagementSystem) {
        this.libraryManagementSystem = libraryManagementSystem;
    }
    @PostMapping("/register")
    @ResponseBody
    @CrossOrigin
    public ApiResult register(@RequestBody Map<String, Object> requestBody) {
        String name = (String) requestBody.get("name");
        String department = (String) requestBody.get("department");
        String type = (String) requestBody.get("type");

        Card newcard = new Card();
        newcard.setName(name);
        newcard.setDepartment(department);
        if("S".equals(type)){
            newcard.setType(Card.CardType.Student);
        }else{
            newcard.setType(Card.CardType.Teacher);
        }
        ApiResult result = libraryManagementSystem.registerCard(newcard);
        return result;
    }
}
