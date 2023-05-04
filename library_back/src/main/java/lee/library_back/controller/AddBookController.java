package lee.library_back.controller;

import jdk.nashorn.internal.runtime.regexp.joni.ast.StringNode;
import lee.library_back.LibraryManagementSystemImpl;
import lee.library_back.entities.Book;
import lee.library_back.entities.Borrow;
import lee.library_back.queries.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AddBookController {
    private final LibraryManagementSystemImpl libraryManagementSystem;
    @Autowired
    public AddBookController(LibraryManagementSystemImpl libraryManagementSystem) {
        this.libraryManagementSystem = libraryManagementSystem;
    }

    @PostMapping("/addbook")
    @ResponseBody
    @CrossOrigin
    public ApiResult borrow(@RequestBody Map<String, Object> requestBody) {
        String title = (String)requestBody.get("title");
        String author = (String) requestBody.get("author");
        String category = (String)requestBody.get("category");
        String press = (String)requestBody.get("press");
        String priceStr = (String)requestBody.get("price");
        double price = Double.parseDouble(priceStr);
        int stock = (int)requestBody.get("stock");
        int publishyear = (int)requestBody.get("publishyear");
        Book newbook = new Book();
        newbook.setAuthor(author);
        newbook.setCategory(category);
        newbook.setPress(press);
        newbook.setPrice(price);
        newbook.setTitle(title);
        newbook.setStock(stock);
        newbook.setPublishYear(publishyear);
        return libraryManagementSystem.storeBook(newbook);
    }
}