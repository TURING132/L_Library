package lee.library_back.controller;

import lee.library_back.LibraryManagementSystemImpl;
import lee.library_back.entities.Book;
import lee.library_back.queries.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ModifyController {
    private final LibraryManagementSystemImpl libraryManagementSystem;
    @Autowired
    public ModifyController(LibraryManagementSystemImpl libraryManagementSystem) {
        this.libraryManagementSystem = libraryManagementSystem;
    }

    @PostMapping("/modify")
    @ResponseBody
    @CrossOrigin
    public ApiResult modify(@RequestBody Map<String, Object> requestBody) {
        Book newbook = new Book();
        String title = (String)requestBody.get("title");
        String author = (String) requestBody.get("author");
        String category = (String)requestBody.get("category");
        String press = (String)requestBody.get("press");
        String priceStr = (String)requestBody.get("price");
        double price;
        if(!(priceStr==null)){
            price = Double.parseDouble(priceStr);
            newbook.setPrice(price);
        }
        if(!(requestBody.get("publishyear")==null)){
            int publishyear = (int)requestBody.get("publishyear");
            newbook.setPublishYear(publishyear);
        }
        if(!(requestBody.get("bookId")==null)){
            int bookId = (int)requestBody.get("bookId");
            newbook.setBookId(bookId);
        }
        newbook.setAuthor(author);
        newbook.setCategory(category);
        newbook.setPress(press);
        newbook.setTitle(title);
        return libraryManagementSystem.modifyBookInfo(newbook);
    }
}
