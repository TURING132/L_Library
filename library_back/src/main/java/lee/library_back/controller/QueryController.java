package lee.library_back.controller;


import lee.library_back.LibraryManagementSystemImpl;
import lee.library_back.entities.Book;
import lee.library_back.queries.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static lee.library_back.entities.Book.SortColumn.*;


@RestController
public class QueryController {
    private final LibraryManagementSystemImpl libraryManagementSystem;
    @Autowired
    public QueryController(LibraryManagementSystemImpl libraryManagementSystem) {
        this.libraryManagementSystem = libraryManagementSystem;
    }
    @GetMapping("/query")
    @ResponseBody
    @CrossOrigin
    public BookQueryResults  query(  @RequestParam(value = "title",required = false) String title,
                                    @RequestParam(value = "category",required = false) String category,
                                    @RequestParam(value = "press",required = false) String press,
                                    @RequestParam(value = "minPublishYear",required = false) Integer minPublishYear,
                                    @RequestParam(value = "maxPublishYear",required = false) Integer maxPublishYear,
                                    @RequestParam(value = "author",required = false) String author,
                                    @RequestParam(value = "minPrice",required = false) Double minPrice,
                                    @RequestParam(value = "maxPrice",required = false) Double maxPrice,
                                     @RequestParam(value = "sortBy",required = false) String sortBy,
                                     @RequestParam(value = "order",required = false) String order) {
        BookQueryConditions conditions = new BookQueryConditions();
        System.out.println(author.getClass());
        if(!title.isEmpty())conditions.setTitle(title);
        if(!author.isEmpty())conditions.setAuthor(author);
        if(!category.isEmpty())conditions.setCategory(category);
        if(!press.isEmpty())conditions.setPress(press);
        if(maxPrice!=null)conditions.setMaxPrice(maxPrice);if(minPrice!=null)conditions.setMinPrice(minPrice);
        if(maxPublishYear!=null)conditions.setMaxPublishYear(maxPublishYear);if(minPublishYear!=null)conditions.setMinPublishYear(minPublishYear);
        if (sortBy != null) {
            if ("id".equals(sortBy)) {
                conditions.setSortBy(Book.SortColumn.BOOK_ID);
            } else if ("price".equals(sortBy)) {
                conditions.setSortBy(Book.SortColumn.PRICE);
            } else if ("publish".equals(sortBy)) {
                conditions.setSortBy(Book.SortColumn.PUBLISH_YEAR);
            }
        }
        if(order!=null){
            if("asc".equals(order)){
                conditions.setSortOrder(SortOrder.ASC);
            }else{
                conditions.setSortOrder(SortOrder.DESC);
            }
        }
        BookQueryResults results = (BookQueryResults)libraryManagementSystem.queryBook(conditions).payload;
        return results;

    }

}
