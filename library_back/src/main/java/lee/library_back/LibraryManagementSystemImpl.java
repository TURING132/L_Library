package lee.library_back;


import lee.library_back.entities.Book;
import lee.library_back.entities.Borrow;
import lee.library_back.entities.Card;
import lee.library_back.queries.*;
import lee.library_back.utils.DBInitializer;
import lee.library_back.utils.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class LibraryManagementSystemImpl implements LibraryManagementSystem {

    private final DatabaseConnector connector;

    public LibraryManagementSystemImpl(DatabaseConnector connector) {
        this.connector = connector;
    }

    @Override//done
    public ApiResult storeBook(Book book) {
        try {
            connector.getConn().setAutoCommit(false);
            String sql_s = "SELECT * FROM book WHERE category = ? AND title = ? AND press = ? AND publish_year = ? AND author = ?";//防止注入
            PreparedStatement statement = connector.getConn().prepareStatement(sql_s);
            statement.setString(1, book.getCategory());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getPress());
            statement.setInt(4, book.getPublishYear());
            statement.setString(5, book.getAuthor());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {//已经有这本书
                return new ApiResult(false, "already a same book");
            } else {
                String sql_i = "INSERT INTO book(category,title,press,publish_year,author,price,stock) VALUES(?,?,?,?,?,?,?)";
                PreparedStatement insertStmt = connector.getConn().prepareStatement(sql_i, Statement.RETURN_GENERATED_KEYS);//返回产生的主键
                insertStmt.setString(1, book.getCategory());
                insertStmt.setString(2, book.getTitle());
                insertStmt.setString(3, book.getPress());
                insertStmt.setInt(4, book.getPublishYear());
                insertStmt.setString(5, book.getAuthor());
                insertStmt.setDouble(6, book.getPrice());
                insertStmt.setInt(7, book.getStock());
                int rows = insertStmt.executeUpdate();
                if (rows > 0) {
                    connector.getConn().commit();
                    ResultSet generatedKeys = insertStmt.getGeneratedKeys();//获得返回的主键
                    int id=-1;
                    if (generatedKeys.next()) {
                        id = generatedKeys.getInt(1);
                        book.setBookId(id); //要修改book_id
                    }
                    return new ApiResult(true, ""+id);
                } else {
                    connector.getConn().rollback();
                    return new ApiResult(false, "store book fail");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connector.getConn().rollback();
            } catch (SQLException ee) {
                // 处理异常
                e.printStackTrace();
            }
            return new ApiResult(false, "store book rollback fail");
        } finally {
            try {
                connector.getConn().setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override//done
    public ApiResult incBookStock(int bookId, int deltaStock) {
        try {
            connector.getConn().setAutoCommit(false);
            String selSql = "SELECT * FROM book WHERE book_id = ?";
            PreparedStatement selStatement = connector.getConn().prepareStatement(selSql);
            selStatement.setInt(1, bookId);
            ResultSet resultSet = selStatement.executeQuery();
            if (resultSet.next()) {
                int stock = resultSet.getInt("stock");
                if (stock + deltaStock < 0) {//库存不够
                    return new ApiResult(false, "no so many book stock");
                } else {
                    stock += deltaStock;
                    String setSql = "UPDATE book SET stock = ? WHERE book_id = ?";
                    PreparedStatement upStatement = connector.getConn().prepareStatement(setSql);
                    upStatement.setInt(1, stock);
                    upStatement.setInt(2, bookId);
                    int rows = upStatement.executeUpdate();
                    if (rows > 0) {
                        return new ApiResult(true, "update book stock success");
                    } else {
                        return new ApiResult(false, "fail to update book stock");
                    }
                }

            } else {
                return new ApiResult(false, "book_id not exist");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connector.getConn().rollback();
            } catch (SQLException ee) {
                ee.printStackTrace();
            }
            return new ApiResult(false, "incBookStock fail");
        } finally {
            try {
                connector.getConn().setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override//done
    public ApiResult storeBook(List<Book> books) {
        try {
            connector.getConn().setAutoCommit(false);
            String insSql = "INSERT INTO book(category,title,press,publish_year,author,price,stock) VALUES(?,?,?,?,?,?,?)";
            PreparedStatement statement = connector.getConn().prepareStatement(insSql, Statement.RETURN_GENERATED_KEYS);
            for (Book book : books) {
                statement.setString(1, book.getCategory());
                statement.setString(2, book.getTitle());
                statement.setString(3, book.getPress());
                statement.setInt(4, book.getPublishYear());
                statement.setString(5, book.getAuthor());
                statement.setDouble(6, book.getPrice());
                statement.setInt(7, book.getStock());
                statement.addBatch();//添加到批处理
            }
            int[] rows = statement.executeBatch();//进行批处理
            for (int i : rows) {
                if (i == 0) {
                    try {
                        connector.getConn().rollback();//有更新失败就回滚并返回错误result
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    return new ApiResult(false, "fail to execute batch (insert list of books)");
                }
            }
            ResultSet keySet = statement.getGeneratedKeys();//获取主键值
            int i = 0;
            while (keySet.next()) {
                books.get(i).setBookId(keySet.getInt(1));
                i++;
            }
            connector.getConn().commit();
            return new ApiResult(true, "success to store list of books");
        } catch (Exception e) {
            try {
                connector.getConn().rollback();
            } catch (SQLException ee) {
                ee.printStackTrace();
            }
            return new ApiResult(false, "fail to insert a list of books");
        } finally {
            try {
                connector.getConn().setAutoCommit(true);
            } catch (SQLException eee) {
                eee.printStackTrace();
            }
        }

    }

    @Override
    public ApiResult removeBook(int bookId) {
        //首先查询book中是否有这本书
            //无，返回false
        //然后查询是borrow中是否有没有还的书
            //有，返回false
        //然后进行删除
        try{
            connector.getConn().setAutoCommit(false);
            String checkBookSql = "SELECT * FROM book WHERE book_id = ?";
            PreparedStatement checkBookStatement = connector.getConn().prepareStatement(checkBookSql);
            checkBookStatement.setInt(1,bookId);
            ResultSet checkBookRes = checkBookStatement.executeQuery();
            if(!checkBookRes.next()){//没有这个id
                return new ApiResult(false,"not exist book id");
            }
            String checkBorSql = "SELECT * FROM borrow WHERE book_id = ? AND (return_time<borrow_time)";
            PreparedStatement checkBorStatement = connector.getConn().prepareStatement(checkBorSql);
            checkBorStatement.setInt(1,bookId);
            ResultSet checkBorRes = checkBorStatement.executeQuery();
            if(checkBorRes.next()){//有没还的书
                return new ApiResult(false,"not return book");
            }
            String delSql = "DELETE FROM book WHERE book_id = ?";
            PreparedStatement delStatement = connector.getConn().prepareStatement(delSql);
            delStatement.setInt(1,bookId);
            int row = delStatement.executeUpdate();
            if(row==1){
                connector.getConn().commit();
                return new ApiResult(true,"remove success");
            }
            return new ApiResult(false,"remove false");
        }catch (Exception e) {
            try {
                connector.getConn().rollback();
            } catch (SQLException ee) {
                ee.printStackTrace();
            }
            return new ApiResult(false, "fail to return a list of books");
        } finally {
            try {
                connector.getConn().setAutoCommit(true);
            } catch (SQLException eee) {
                eee.printStackTrace();
            }
        }
    }

    @Override
    public ApiResult modifyBookInfo(Book book) {
        //通过id查找这本书
            //如果不存在此，返回false
        //设置并返回true
        try{
            connector.getConn().setAutoCommit(false);
            String checkSql = "SELECT * FROM book WHERE book_id = ?";
            PreparedStatement checkStatement = connector.getConn().prepareStatement(checkSql);
            checkStatement.setInt(1,book.getBookId());
            ResultSet checkRes = checkStatement.executeQuery();
            if(!checkRes.next()){
                return new ApiResult(false,"invalid book_id");
            }
            String upSql = "UPDATE book SET ";
            int count = 0;
            if(book.getCategory() != null) {
                upSql += "category = ?";
                count++;
            }
            if(book.getTitle() != null) {
                upSql += (count > 0 ? "," : "") + "title = ?";
                count++;
            }
            if(book.getPress() != null) {
                upSql += (count > 0 ? "," : "") + "press = ?";
                count++;
            }
            if(book.getPublishYear() != -1) {
                upSql += (count > 0 ? "," : "") + "publish_year = ?";
                count++;
            }
            if(book.getAuthor() != null) {
                upSql += (count >0 ? "," : "") + "author = ?";
                count++;
            }
            if(book.getPrice() != -1) {
                upSql += (count > 0 ? "," : "") + "price = ?";
                count++;
            }
            if(count == 0) {
                return new ApiResult(false,"no update is needed");
            }
            upSql += " WHERE book_id = ?";
            PreparedStatement upStatement = connector.getConn().prepareStatement(upSql);
            int parameterIndex = 1;
            if(book.getCategory() != null) {
                upStatement.setString(parameterIndex++, book.getCategory());
            }
            if(book.getTitle() != null) {
                upStatement.setString(parameterIndex++, book.getTitle());
            }
            if(book.getPress() != null) {
                upStatement.setString(parameterIndex++, book.getPress());
            }
            if(book.getPublishYear() != -1) {
                upStatement.setInt(parameterIndex++, book.getPublishYear());
            }
            if(book.getAuthor() != null) {
                upStatement.setString(parameterIndex++, book.getAuthor());
            }
            if(book.getPrice() != -1) {
                upStatement.setDouble(parameterIndex++, book.getPrice());
            }
            upStatement.setInt(parameterIndex, book.getBookId());
            int row = upStatement.executeUpdate();
            if(row==1){
                connector.getConn().commit();
                return new ApiResult(true,"success modify Book");
            }else{
                return new ApiResult(false,"fail modify book");
            }
        }catch (Exception e) {
            try {
                connector.getConn().rollback();
            } catch (SQLException ee) {
                ee.printStackTrace();
            }
            return new ApiResult(false, "fail to return a list of books");
        } finally {
            try {
                connector.getConn().setAutoCommit(true);
            } catch (SQLException eee) {
                eee.printStackTrace();
            }
        }
    }



    @Override//done
    public ApiResult queryBook(BookQueryConditions conditions) {
        try {
            StringBuilder querySql = new StringBuilder();
            querySql.append("SELECT * FROM book WHERE 1=1 ");
            List<Object> params = new ArrayList<>();
            if (conditions.getCategory() != null) {
                querySql.append("AND category = ?");
                params.add(conditions.getCategory());
            }
            if (conditions.getTitle() != null) {
                querySql.append(" AND title LIKE ?");
                params.add("%" + conditions.getTitle() + "%");
            }
            if (conditions.getPress() != null) {
                querySql.append(" AND press LIKE ?");
                params.add("%" + conditions.getPress() + "%");
            }
            if (conditions.getMinPublishYear() != null) {
                querySql.append(" AND publish_year >= ?");
                params.add(conditions.getMinPublishYear());
            }
            if (conditions.getMaxPublishYear() != null) {
                querySql.append(" AND publish_year <= ?");
                params.add(conditions.getMaxPublishYear());
            }
            if (conditions.getAuthor() != null) {
                querySql.append(" AND author LIKE ?");
                params.add("%" + conditions.getAuthor() + "%");
            }
            if (conditions.getMinPrice() != null) {
                querySql.append(" AND price >= ?");
                params.add(conditions.getMinPrice());
            }
            if (conditions.getMaxPrice() != null) {
                querySql.append(" AND price <= ?");
                params.add(conditions.getMaxPrice());
            }
            boolean flag = false;
            if (conditions.getSortBy() != null) {
                flag = true;
                querySql.append(" ORDER BY ").append(conditions.getSortBy());
            }
            if (conditions.getSortBy() != null && conditions.getSortOrder() != null) {
                querySql.append(' ').append(conditions.getSortOrder().getValue());
            }
            if(flag){
                querySql.append(" ,book_id ASC");
            }else{
                querySql.append(" ORDER BY book_id ASC");
            }
            System.out.println(connector.toString());
            PreparedStatement stmt = connector.getConn().prepareStatement(querySql.toString());
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i+1, params.get(i));
            }
            ResultSet resultSet = stmt.executeQuery();
            List<Book> bookList = new ArrayList<>();
            while (resultSet.next()) {
                Book abook = new Book();
                abook.setBookId(resultSet.getInt("book_id"));
                abook.setCategory(resultSet.getString("category"));
                abook.setTitle(resultSet.getString("title"));
                abook.setPress(resultSet.getString("press"));
                abook.setPublishYear(resultSet.getInt("publish_year"));
                abook.setAuthor(resultSet.getString("author"));
                abook.setPrice(resultSet.getDouble("price"));
                abook.setStock(resultSet.getInt("stock"));
                bookList.add(abook);
            }
            //正常的返回结果是一个查询结果对象

            BookQueryResults bookQueryResults = new BookQueryResults(bookList);
            return new ApiResult(true, bookQueryResults);
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connector.getConn().rollback();
            } catch (SQLException ee) {
                // 处理异常
                e.printStackTrace();
            }
            return new ApiResult(false, "query book rollback fail");
        } finally {
            try{
                connector.getConn().setAutoCommit(true);
            }catch (SQLException ee){
                ee.printStackTrace();
            }
        }
    }

    @Override//done
    public ApiResult borrowBook(Borrow borrow) {
        try {//应该使用borrow里面的time！不要修改borrow的信息，不要设置return time
            //borrow
            synchronized (inventoryLock){
                connector.getConn().setAutoCommit(false);
                //如果card_id有没有还的书，返回false
                //如果card_id没有未还的书
                //insert一条borrow
                String selSql = "SELECT * FROM borrow WHERE card_id = ? AND book_id = ? AND return_time <borrow_time";//查找是否有没还的书
                PreparedStatement pStmt = connector.getConn().prepareStatement(selSql);
                pStmt.setInt(1, borrow.getCardId());
                pStmt.setInt(2, borrow.getBookId());
                ResultSet selResult = pStmt.executeQuery();
                if (selResult.next()) {//有没还的书，返回false
                    return new ApiResult(false, "book not return");
                } else {//查看库存是否足够
                    //如果没有没还的书，则可以借书，但是card_id可能不存在,所以要先验证card_id的正确性
                    String checkCardSql = "SELECT * FROM card WHERE card_id = ?";
                    PreparedStatement checkStatement = connector.getConn().prepareStatement(checkCardSql);
                    checkStatement.setInt(1, borrow.getCardId());
                    ResultSet checkResult = checkStatement.executeQuery();
                    if (!checkResult.next()) {
                        return new ApiResult(false, "invalid card_id");
                    }
                    String selStockSql = "SELECT * FROM book WHERE book_id = ? AND stock>0";
                    PreparedStatement selStockStatement = connector.getConn().prepareStatement(selStockSql);
                    selStockStatement.setInt(1, borrow.getBookId());
                    ResultSet sResult = selStockStatement.executeQuery();
                    if (sResult.next()) {
                        // 可以借书，插入一条记录，更新书的库存
                        String upStockSql = "UPDATE book SET stock = stock - 1 WHERE book_id = ?" +
                                " AND NOT EXISTS(SELECT * FROM borrow WHERE card_id = ? AND book_id = ? AND return_time <borrow_time) " +
                                "AND stock>0";
                        PreparedStatement upStockStatement = connector.getConn().prepareStatement(upStockSql);
                        upStockStatement.setInt(1, borrow.getBookId());
                        upStockStatement.setInt(2, borrow.getCardId());
                        upStockStatement.setInt(3, borrow.getBookId());
                        int row = upStockStatement.executeUpdate();
                        if (row != 1) {
                            return new ApiResult(false, "borrow fail");
                        }
                        String insSql = "INSERT INTO borrow(card_id,book_id,borrow_time) VALUES(?,?,?)";
                        PreparedStatement inStatement = connector.getConn().prepareStatement(insSql);
                        inStatement.setInt(1, borrow.getCardId());
                        inStatement.setInt(2, borrow.getBookId());
                        inStatement.setLong(3, borrow.getBorrowTime());
                        int rows = inStatement.executeUpdate();
                        if (rows == 0) {
                            connector.getConn().rollback();
                            return new ApiResult(false, "fail to borrow book");
                        } else {
                            connector.getConn().commit();
                            return new ApiResult(true, ""+borrow.getBorrowTime());
                        }

                    } else {
                        return new ApiResult(false, "no enough stock");
                    }
                }
            }
        } catch (SQLException e) {
            try {
                connector.getConn().rollback();
            } catch (SQLException ee) {
                ee.printStackTrace();
            }
            e.printStackTrace();
            return new ApiResult(false, "fail to borrow book");
        } finally {
            try {
                connector.getConn().setAutoCommit(true);
            } catch (SQLException eee) {
                eee.printStackTrace();
            }
        }
    }

    @Override//done
    public ApiResult returnBook(Borrow borrow) {
        try {//应该使用borrow里面的 return time，使用查询的 borrow time
            connector.getConn().setAutoCommit(false);
            //先查询是否有对应的未还的记录，且此未换的记录可以满足当前的还书操作，也就是此未还的书是在当前还书操作之前借的
            String selSql = "SELECT * FROM borrow WHERE card_id = ? AND book_id = ? AND (return_time < borrow_time||return_time = 0) AND borrow_time < ?";
//            String selSql = "SELECT * FROM borrow WHERE card_id = ? AND book_id = ?";
            PreparedStatement selStatement = connector.getConn().prepareStatement(selSql);
            selStatement.setInt(1, borrow.getCardId());
            selStatement.setInt(2, borrow.getBookId());
            selStatement.setLong(3, borrow.getReturnTime());
            ResultSet selResult = selStatement.executeQuery();
            if (selResult.next()) {//如果此书有未还的
                String upSql = "UPDATE borrow SET return_time = ? WHERE card_id =? AND book_id = ? AND (return_time < borrow_time||return_time = 0)";
                PreparedStatement upStatement = connector.getConn().prepareStatement(upSql);
                upStatement.setLong(1, borrow.getReturnTime());
                upStatement.setInt(2, borrow.getCardId());
                upStatement.setInt(3, borrow.getBookId());
                int rows = upStatement.executeUpdate();
                if (rows == 1) {
                    String upStockSql = "UPDATE book SET stock = stock + 1 WHERE book_id = ?";
                    PreparedStatement upStockStatement = connector.getConn().prepareStatement(upStockSql);
                    upStockStatement.setInt(1, borrow.getBookId());
                    int row = upStockStatement.executeUpdate();
                    if (row == 1) {
                        connector.getConn().commit();
                        return new ApiResult(true, ""+borrow.getReturnTime());
                    } else {
                        connector.getConn().rollback();
                        return new ApiResult(false, "fail to return book");
                    }
                } else {
                    connector.getConn().rollback();
                    return new ApiResult(false, "fail to return book");
                }
            } else {
                return new ApiResult(false, "incorrect book id or card id or have already return");
            }
        } catch (SQLException e) {
            try {
                connector.getConn().rollback();
            } catch (SQLException ee) {
                ee.printStackTrace();
            }
            e.printStackTrace();
            return new ApiResult(false, "fail to return book");
        } finally {
            try {
                connector.getConn().setAutoCommit(true);
            } catch (SQLException eee) {
                eee.printStackTrace();
            }
        }
    }

    @Override//done
    public ApiResult showBorrowHistory(int cardId) {
        try {
            connector.getConn().setAutoCommit(false);
            String b_sql = "SELECT * FROM borrow WHERE card_id = ? ORDER BY borrow_time DESC";
            PreparedStatement bStatement = connector.getConn().prepareStatement(b_sql);
            bStatement.setInt(1,cardId);
            ResultSet bResult  = bStatement.executeQuery();
            List<BorrowHistories.Item> bList = new ArrayList<>();
            while(bResult.next()){
                Borrow aBorrow = new Borrow();
                aBorrow.setCardId(cardId);
                int book_id = bResult.getInt("book_id");
                aBorrow.setBookId(book_id);
                aBorrow.setBorrowTime(bResult.getLong("borrow_time"));
                aBorrow.setReturnTime(bResult.getLong("return_time"));

                Book aBook = new Book();
                String k_sql = "SELECT * FROM book WHERE book_id = ?";
                PreparedStatement kStatement = connector.getConn().prepareStatement(k_sql);
                kStatement.setInt(1,book_id);
                ResultSet kResult = kStatement.executeQuery();
                if(kResult.next()){
                    aBook.setBookId(kResult.getInt("book_id"));
                    aBook.setPrice(kResult.getDouble("price"));
                    aBook.setCategory(kResult.getString("category"));
                    aBook.setAuthor(kResult.getString("author"));
                    aBook.setPress(kResult.getString("press"));
                    aBook.setTitle(kResult.getString("title"));
                    aBook.setPublishYear(kResult.getInt("publish_year"));
                }
                BorrowHistories.Item aItem = new BorrowHistories.Item(cardId,aBook,aBorrow);
                bList.add(aItem);
            }
            return new ApiResult(true,new BorrowHistories(bList));
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connector.getConn().rollback();
            } catch (SQLException ee) {
                // 处理异常
                e.printStackTrace();
            }
            return new ApiResult(false, "store book rollback fail");
        } finally {
            try {
                connector.getConn().setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @Override//done
    public ApiResult registerCard(Card card) {//注意注册时候要存进去card_id
        try{
            connector.getConn().setAutoCommit(false);
            String selSql = "SELECT * FROM card WHERE name = ? AND department = ? AND type = ? ";
            PreparedStatement selStatement = connector.getConn().prepareStatement(selSql);
            selStatement.setString(1,card.getName());
            selStatement.setString(2, card.getDepartment());
            selStatement.setString(3,card.getType().getStr());
            ResultSet selResult = selStatement.executeQuery();
            if(selResult.next()){//已经注册过了
                //dup
                return new ApiResult(false,"dup");
            }else{//插入新的卡
                String inSql = "INSERT INTO card(name,department,type) VALUES(?,?,?)";
                PreparedStatement inStatement = connector.getConn().prepareStatement(inSql,Statement.RETURN_GENERATED_KEYS);
                inStatement.setString(1,card.getName());
                inStatement.setString(2,card.getDepartment());
                inStatement.setString(3,card.getType().getStr());
                int rows = inStatement.executeUpdate();
                if(rows>0){
                    ResultSet keys = inStatement.getGeneratedKeys();
                    keys.next();
                    int id = keys.getInt(1);
                    card.setCardId(id);
                    connector.getConn().commit();
                    return new ApiResult(true,""+id);
                }else{
                    try{
                        connector.getConn().rollback();
                    }catch (SQLException ee){
                        ee.printStackTrace();
                    }
                    return  new ApiResult(false,"fail to register card");
                }
            }

        }catch (SQLException e){
            try{
                connector.getConn().rollback();
            }catch (SQLException ee){
                ee.printStackTrace();
            }
            e.printStackTrace();
            return  new ApiResult(false,"fail to register card");
        }finally {
            try{
                connector.getConn().setAutoCommit(true);
            }catch (SQLException eee){
                eee.printStackTrace();
            }
        }


    }

    @Override//done
    public ApiResult removeCard(int cardId) {//not test
        try{
            connector.getConn().setAutoCommit(false);//查询是否有未还的书
            String selSqlBorrow = "SELECT * FROM borrow WHERE card_id = ? AND (return_time < borrow_time || return_time = 0 )";
            PreparedStatement selStatement = connector.getConn().prepareStatement(selSqlBorrow);
            selStatement.setInt(1,cardId);
            ResultSet selResult = selStatement.executeQuery();
            if(selResult.next()){
                return new ApiResult(false,"fail to remove card, because book not returned");
            }else{
                String selSqlCard = "SELECT * FROM card WHERE card_id = ?";
                PreparedStatement selStatement1 = connector.getConn().prepareStatement(selSqlCard);
                selStatement1.setInt(1,cardId);
                ResultSet selResult1 = selStatement1.executeQuery();
                if(selResult1.next()){
                    String delSql = "DELETE FROM card WHERE card_id = ?";
                    PreparedStatement delStatement = connector.getConn().prepareStatement(delSql);
                    delStatement.setInt(1,cardId);
                    int rows = delStatement.executeUpdate();
                    if(rows==1){
                        connector.getConn().commit();
                        return new ApiResult(true,"success to remove card");
                    }else{
                        return new ApiResult(false,"fail to remove card");
                    }
                }else{//不存在此卡
                    return new ApiResult(false,"fail to remove card, because card not exit");
                }
            }

        }catch (SQLException e){
            try{
                connector.getConn().rollback();
            }catch (SQLException ee){
                ee.printStackTrace();
            }
            e.printStackTrace();
            return  new ApiResult(false,"fail to remove card");
        }finally {
            try{
                connector.getConn().setAutoCommit(true);
            }catch (SQLException eee){
                eee.printStackTrace();
            }
        }
    }

    @Override//done
    public ApiResult showCards() {
        try{
            connector.getConn().setAutoCommit(false);
            String selSql = "SELECT * FROM card ORDER BY card_id";
            PreparedStatement selStatement = connector.getConn().prepareStatement(selSql);
            ResultSet selResult = selStatement.executeQuery();
            List<Card>cardList =new ArrayList<>();
            while (selResult.next()){
                Card aCard = new Card();
                aCard.setCardId(selResult.getInt("card_id"));
                aCard.setName(selResult.getString("name"));
                aCard.setDepartment(selResult.getString("department"));
                aCard.setType(Card.CardType.values(selResult.getString("type")));
                cardList.add(aCard);
            }
            return new ApiResult(true,new CardList(cardList));
        }catch (SQLException e){
            try{
                connector.getConn().rollback();
            }catch (SQLException ee){
                ee.printStackTrace();
            }
            e.printStackTrace();
            return  new ApiResult(false,"fail to show card");
        }finally {
            try{
                connector.getConn().setAutoCommit(true);
            }catch (SQLException eee){
                eee.printStackTrace();
            }
        }
    }

    public ApiResult checkCard(int cardId) {//查询是否存在卡片
        try{
                connector.getConn().setAutoCommit(false);
                String selSql = "SELECT * FROM card WHERE card_id = ?";
                PreparedStatement selStatement = connector.getConn().prepareStatement(selSql);
                selStatement.setInt(1,cardId);
                ResultSet selResult = selStatement.executeQuery();
                if (selResult.next()){
                    return new ApiResult(true,"exist");
                }else{
                    return new ApiResult(false,"not exist");
                }
            }
        catch (SQLException e){
            try{
                connector.getConn().rollback();
            }catch (SQLException ee){
                ee.printStackTrace();
            }
            e.printStackTrace();
            return  new ApiResult(false,"fail to show card");
        }finally {
            try{
                connector.getConn().setAutoCommit(true);
            }catch (SQLException eee){
                eee.printStackTrace();
            }
        }
    }

    @Override//already done by test
    public ApiResult resetDatabase() {
        Connection conn = connector.getConn();
        try {
            Statement stmt = conn.createStatement();
            DBInitializer initializer = connector.getConf().getType().getDbInitializer();
            stmt.addBatch(initializer.sqlDropBorrow());
            stmt.addBatch(initializer.sqlDropBook());
            stmt.addBatch(initializer.sqlDropCard());
            stmt.addBatch(initializer.sqlCreateCard());
            stmt.addBatch(initializer.sqlCreateBook());
            stmt.addBatch(initializer.sqlCreateBorrow());
            stmt.executeBatch();
            commit(conn);
        } catch (Exception e) {
            rollback(conn);
            return new ApiResult(false, e.getMessage());
        }
        return new ApiResult(true, null);
    }

    private void rollback(Connection conn) {
        try {
            conn.rollback();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void commit(Connection conn) {
        try {
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final Object inventoryLock = new Object();
}
