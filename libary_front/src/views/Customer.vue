<template>
  <Cheader></Cheader>
  <div>
    <button @click="operation = 'search'">查询书籍</button>
    <button @click="operation = 'borrow'">借书</button>
    <button @click="operation = 'return'">还书</button>
    <button @click="operation = 'view'">查看借书记录</button>
    <button @click="operation = 'logout'">注销卡</button>
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


    <div v-if="operation === 'borrow'">
      <div class="form">
        <h3>借书</h3>
        <div>
          <label for="book-id">书籍编号：</label>
          <input type="text" id="book-id" v-model="borrowBookId">
        </div>
        <button @click="borrowBook">借书</button>
      </div>
    </div>

    <div v-if="operation === 'return'">
      <div class="form">
        <h3>还书</h3>
        <div>
          <label for="book-id">书籍编号：</label>
          <input type="text" id="book-id" v-model="returnBookId">
        </div>
        <button @click="returnBook">还书</button>
      </div>
    </div>

    <div v-if="operation === 'view'">
      <div class="form">
        <h3>查看借书记录</h3>
        <div>
          <label for="card-id">用户卡号：</label>
          <input type="text" id="card-id" v-model="borrowerId">
        </div>
        <button @click="viewBorrowRecords">查看</button>
      </div>
    </div>

    <div v-if="operation === 'logout'">
      <div class="form">
        <h3>注销卡</h3>
        <div>
          <label for="card-id">用户卡号：</label>
          <input type="text" id="card-id" v-model="cardId">
        </div>
        <button @click="logoutCard">注销</button>
      </div>
    </div>
  </div>
</template>

<script>
import Cheader from "@/components/Cheader.vue";

export default {
  name: "Customer",
  components:{
    Cheader:Cheader
  },
  data() {
    return {
      operation:"",
      bookName: "",
      bookAuthor: "",
      bookPublisher: "",
      bookUpdate: "",
      bookPrice: "",
      borrowBookId: "",
      returnBookId: "",
      borrowerId: "",
      cardId: "",
      books: [],
      borrowRecords: [],
    };
  },
  methods: {
    searchBooks(){

    },
    borrowBook() {
      // 发送借书请求
      // 代码待完成
    },

    returnBook() {
      // 发送还书请求
      // 代码待完成
    },

    viewBorrowRecords() {
      // 发送查看借书记录请求，并将结果保存到borrowRecords数组中
      // 代码待完成
    },

    logoutCard() {
      // 发送注销卡请求
      // 代码待完成
    },
    toGuide() {
      this.$router.push('/');
    },
  },
  mounted() {
    console.log(this.$route.params);
  },
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