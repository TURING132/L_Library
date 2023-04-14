<template>
  <Cheader></Cheader>
  <div>
    <button @click="operation = 'search'">查询书籍</button>
    <button @click="operation = 'add'">添加图书</button>
    <button @click="operation = 'del'">删除图书</button>
    <button @click="operation = 'modify'">修改图书信息</button>
    <button @click="operation = 'inc'">修改库存</button>
    <button @click="operation = 'his'">查询借书记录</button>
    <button @click="toGuide">返回首页</button>

    <div v-if="operation === 'search'">
      <div class="form">
        <h3>查询书籍</h3>
        <div>
          <label for="book-name">书籍名：</label>
          <input type="text" id="book-name" v-model="bookName">
        </div>
        <div>
          <label for="book-author">作者：</label>
          <input type="text" id="book-author" v-model="bookAuthor">
        </div>
        <div>
          <label for="book-publisher">出版社：</label>
          <input type="text" id="book-publisher" v-model="bookPublisher">
        </div>
        <div>
          <label for="book-Update">出版时间起：</label>
          <input type="date" id="book-pubdate" v-model="bookUpdate">
          <label for="book-Update">出版时间止：</label>
          <input type="date" id="book-pubdate" v-model="bookUpdate">
        </div>
        <div>
          <label for="book-price">价格起：</label>
          <input type="number" id="book-price" v-model="bookPrice">
          <label for="book-price">价格止：</label>
          <input type="number" id="book-price" v-model="bookPrice">
        </div>
        <button @click="searchBooks">查询</button>
      </div>
    </div>


    <div v-if="operation === 'add'">
      <div class="form">
        <h3>添加图书</h3>
        <div>
          <label for="book-name">书籍名：</label>
          <input type="text" id="book-name" v-model="bookName">
        </div>
        <div>
          <label for="book-author">作者：</label>
          <input type="text" id="book-author" v-model="bookAuthor">
        </div>
        <div>
          <label for="book-publisher">出版社：</label>
          <input type="text" id="book-publisher" v-model="bookPublisher">
        </div>
        <div>
          <label for="book-pubdate">出版时间：</label>
          <input type="date" id="book-pubdate" v-model="bookPubdate">
        </div>
        <div>
          <label for="book-price">价格：</label>
          <input type="number" id="book-price" v-model="bookPrice">
        </div>
        <button @click="addBook">添加</button>
      </div>

    </div>
    <div v-if="operation === 'del'">
      <div class="form">
        <h3>删除图书</h3>
        <div>
          <label for="book-id">书籍编号：</label>
          <input type="text" id="book-id" v-model="deleteBookId">
        </div>
        <button @click="deleteBook">删除</button>
      </div>
    </div>

    <div v-if="operation === 'modify'">
      <div class="form">
        <h3>修改图书信息</h3>
        <div>
          <label for="book-id">书籍编号：</label>
          <input type="text" id="book-id" v-model="updateBookId">
        </div>
        <div>
          <label for="update-name">书籍名：</label>
          <input type="text" id="update-name" v-model="updateBookName">
        </div>
        <div>
          <label for="update-author">作者：</label>
          <input type="text" id="update-author" v-model="updateBookAuthor">
        </div>
        <div>
          <label for="update-publisher">出版社：</label>
          <input type="text" id="update-publisher" v-model="updateBookPublisher">
        </div>
        <div>
          <label for="update-pubdate">出版时间：</label>
          <input type="date" id="update-pubdate" v-model="updateBookPubdate">
        </div>
        <div>
          <label for="update-price">价格：</label>
          <input type="number" id="update-price" v-model="updateBookPrice">
        </div>
        <button @click="updateBook">修改</button>
      </div>
    </div>

    <div v-if="operation === 'inc'">
      <div class="form">
        <h3>修改库存</h3>
        <div>
          <label for="book-id">书籍编号：</label>
          <input type="text" id="book-id" v-model="stockBookId">
        </div>
        <div>
          <label for="delta-stock">库存变化：</label>
          <input type="number" id="delta-stock" v-model="deltaStock">
        </div>
        <button @click="changeStock">修改</button>
      </div>
    </div>

    <div v-if="operation=== 'his'">
      <div class="form">
        <h3>查询借书记录</h3>
        <div>
          <label for="card-id">借书证号：</label>
          <input type="text" id="card-id" v-model="cardId">
        </div>
        <button @click="getBorrowRecords">查询</button>
        <div v-if="borrowRecords">
          <table>
            <thead>
            <tr>
              <th>书名</th>
              <th>作者</th>
              <th>出版社</th>
              <th>借出时间</th>
              <th>归还时间</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="record in borrowRecords" :key="record.book_id">
              <td>{{ record.book_name }}</td>
              <td>{{ record.book_author }}</td>
              <td>{{ record.book_publisher }}</td>
              <td>{{ record.borrow_date }}</td>
              <td>{{ record.return_date }}</td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import Cheader from "@/components/Cheader.vue";

export default {
  name: "Administractor",
  data() {
    return {
      operation:"",
      cardId: '',
      borrowRecords: null,
      stockBookId: "",
      deltaStock: 0
    };
  },
  components:{
    Cheader:Cheader
  }
  ,
  methods: {
    addBook() {
      // 添加图书的逻辑
    },
    deleteBook() {
      // 删除图书的逻辑
    },
    updateBook() {
      // 修改图书信息的逻辑
    },
    changeStock() {
      // 修改库存的逻辑
    },
    toGuide() {
      this.$router.push('/');
    }
    ,
    async getBorrowRecords() {
      try {
        const response = await axios.get(`/api/borrow?card_id=${this.cardId}`);
        this.borrowRecords = response.data;
      } catch (error) {
        console.error(error);
      }
    }
  }
}
</script>

<style scoped>
button {
  background-color: white;
  color: black;
  border: 2px solid black;
  border-radius: 5px;
  padding: 10px 20px;
  font-size: 16px;
  margin-right: 10px;
  margin-top: 20px;
}

button:hover {
  background-color: black;
  color: white;
  transition: all 0.3s ease-in-out;
}

.form {
  background-color: white;
  color: black;
  border: 2px solid black;
  border-radius: 5px;
  padding: 20px;
  margin-top: 20px;
  max-width: 400px;
  margin-left: auto;
  margin-right: auto;
}

.form label {
  display: inline-block;
  width: 120px;
  text-align: right;
  margin-right: 10px;
}

.form input[type="text"],
.form input[type="number"],
.form input[type="date"] {
  width: 200px;
  padding: 5px;
  font-size: 16px;
  border-radius: 5px;
  border: 1px solid #ccc;
  margin-bottom: 10px;
}

.form button {
  display: block;
  width: 100%;
  background-color: black;
  color: white;
  border: none;
  border-radius: 5px;
  padding: 10px;
  font-size: 16px;
  margin-top: 20px;
  cursor: pointer;
}

.form button:hover {
  background-color: #333;
  transition: all 0.3s ease-in-out;
}
</style>