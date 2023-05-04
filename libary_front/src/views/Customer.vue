<template>
  <Cheader></Cheader>
  <div>
    <button @click="operation = 'search'">查询书籍</button>
    <button @click="operation = 'borrow'">借书</button>
    <button @click="operation = 'return'">还书</button>
    <button @click="operation = 'view'">查看借书记录</button>
    <button @click="operation = 'logout'" >注销卡</button>
    <button @click="toGuide">返回首页</button>

    <div v-if="operation === 'search'">
      <div class="form">
        <h3>查询书籍</h3>
        <div>
          <label for="book-name">书籍名：</label>
          <input type="text" id="book-name" v-model="name">
        </div>
        <div>
          <label for="category">类别：</label>
          <input type="text" id="category" v-model="category">
        </div>
        <div>
          <label for="book-author">作者：</label>
          <input type="text" id="book-author" v-model="author">
        </div>
        <div>
          <label for="press">出版社：</label>
          <input type="text" id="press" v-model="press">
        </div>
        <div>
          <label for="min-publish">出版年起：</label>
          <input type="number" id="min-publish" v-model="minPublish">
          <label for="max-publish">出版年止：</label>
          <input type="number" id="max-publish" v-model="maxPublish">
        </div>
        <div>
          <label for="min-price">价格起：</label>
          <input type="number" id="min-price" v-model="minPrice">
          <label for="max-price">价格止：</label>
          <input type="number" id="max-price" v-model="maxPrice">
        </div>
        <div>
          <label for="sort-by" >排序方式：</label>
          <select id="sort-by" v-model="sortBy">
            <option value="id">按照ID排序</option>
            <option value="price">按照价格排序</option>
            <option value="publish">按照出版时间排序</option>
          </select>
        </div>

        <div>
          <label for="sort-order" >排序顺序：</label>
          <select id="sort-order" v-model="order">
            <option value="asc">升序</option>
            <option value="desc">降序</option>
          </select>
        </div>
        <button @click="searchBooks">查询</button>
      </div>
    </div>


    <div v-if="operation === 'borrow'">
      <div class="form">
        <h3>借书</h3>
        <div>
          <label for="book-id">书籍编号：</label>
          <input type="number" id="book-id" v-model="borrowBookId">
        </div>
        <button @click="borrowBook">借书</button>
      </div>
    </div>

    <div v-if="operation === 'return'">
      <div class="form">
        <h3>还书</h3>
        <div>
          <label for="book-id">书籍编号：</label>
          <input type="number" id="book-id" v-model="returnBookId">
        </div>
        <button @click="returnBook">还书</button>
      </div>
    </div>

    <div v-if="operation === 'view'">
      <div class="form">
        <h3>查看借书记录</h3>
        <button @click="viewBorrowRecords">查看</button>
      </div>
    </div>

    <div v-if="operation === 'logout'">
      <div class="form">
        <h3>注销卡</h3>
        <button @click="logoutCard">注销</button>
      </div>
    </div>
  </div>
  <div v-if="queryResult.length">
    <div class="floating-box">
      <h3>查询结果</h3><button class="close" @click="clearResult">关闭</button>
      <ul><li>
        <span>书籍名</span>
        <span>作者</span>
        <span>bookId</span>
        <span>类别</span>
        <span>出版社</span>
        <span>价格</span>
        <span>出版年份</span>
        <span>库存</span>
      </li></ul>

      <ul>
        <li v-for="result in queryResult" :key="result.bookId">
          <span>{{ result.title }}</span>
          <span>{{ result.author }}</span>
          <span>{{ result.bookId }}</span>
          <span>{{ result.category }}</span>
          <span>{{ result.press }}</span>
          <span>{{ result.price }}</span>
          <span>{{ result.publishYear }}</span>
          <span>{{ result.stock }}</span>
        </li>
      </ul>
    </div>
  </div>
  <div v-if="borrowHistory.length">
    <div class="floating-box">
      <h3>借书记录</h3><button class="close" @click="clearHistory">关闭</button>
      <ul><li>
        <span>是否归还</span>
        <span>书籍名</span>
        <span>借书时间</span>
        <span>作者</span>
        <span>bookId</span>
        <span>类别</span>
        <span>出版社</span>
        <span>价格</span>
        <span>出版年份</span>

      </li></ul>

      <ul>
        <li v-for="record in borrowHistory" :key="record.borrowTime" :class="{'redLi':record.borrowTime>record.returnTime}">
          <span v-if="record.borrowTime<record.returnTime">已归还</span>
          <span v-else>未归还</span>
          <span>{{ record.title }}</span>
          <span>{{formatDate(record.borrowTime)}}</span>
          <span>{{ record.author }}</span>
          <span>{{ record.bookId }}</span>
          <span>{{ record.category }}</span>
          <span>{{ record.press }}</span>
          <span>{{ record.price }}</span>
          <span>{{ record.publishYear }}</span>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import Cheader from "@/components/Cheader.vue";
import axios from "axios";
import router from "@/router";
export default {
  name: "Customer",
  components:{
    Cheader:Cheader
  },
  data() {
    return {
      operation:"",
      name: "",
      category:"",
      author: "",
      press: "",
      minPublish: "",
      maxPublish: "",
      minPrice: "",
      maxPrice:"",
      sortBy:"",
      order:"",
      borrowBookId: "",
      returnBookId: "",
      borrowerId: "",
      books: [],
      borrowHistory: [],
      identity: "",
      cardId: "",//从router参数获得
      queryResult:[]
    };
  },
  created() {//获得router参数
    this.cardId = this.$route.params.cardId;
    this.identity = this.$route.params.identity;
  },
  methods: {
    searchBooks(){
      let params = new URLSearchParams();
      params.append('title', this.name);
      params.append('category', this.category);
      params.append('press', this.press);
      params.append('minPublishYear', this.minPublish);
      params.append('maxPublishYear', this.maxPublish);
      params.append('author', this.author);
      params.append('minPrice', this.minPrice);
      params.append('maxPrice', this.maxPrice);
      axios.get("http://localhost:8050/query",{ params: params })
          .then(response=>{
            this.queryResult = response.data.results
          })
          .then(error=>{
            console.log(error)
          });
    },
    formatDate(timestamp) {
      const date = new Date(timestamp);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const hour = String(date.getHours()).padStart(2, '0');
      const minute = String(date.getMinutes()).padStart(2, '0');
      const second = String(date.getSeconds()).padStart(2, '0');
      return `${year}-${month}-${day} ${hour}:${minute}:${second}`;
    },
    borrowBook() {
      // 发送借书请求
      // 代码待完成
      if(!this.borrowBookId){alert("请填写要借的书的id");return}
      let data = {
        bookId: this.borrowBookId,
        cardId:Number(this.cardId)
      };
      //post要写成data
      axios.post("http://localhost:8050/borrow", data)
          .then(response => {
            if(response.data.ok==true){
              alert("借书成功"+this.formatDate(Number(response.data.message)))
            }else if(response.data.message=="book not return"){
              alert("当前有未归还的书")
            }else if(response.data.message=="no enough stock"){
              alert("库存不足或错误的编号")
            }else{
              alert("接口错误")
            }
          })
          .catch(error => {
            console.error(error);
          });
    },

    returnBook() {
      // 发送还书请求
      // 代码待完
      if(!this.returnBookId){alert("请填写要借的书的id");return}
      let data = {
        bookId: this.returnBookId,
        cardId:Number(this.cardId)
      };
      //post要写成data
      axios.post("http://localhost:8050/return", data)
          .then(response => {
            if(response.data.ok==true){
              alert("还书成功"+this.formatDate(Number(response.data.message)))
            }else if(response.data.message=="incorrect book id or card id or have already return"){
              alert("所有书都已归还或错误的编号")
            }else{
              alert("接口错误")
            }
          })
          .catch(error => {
            console.error(error);
          });
    },

    viewBorrowRecords() {
      // 发送查看借书记录请求，并将结果保存到borrowHistory数组中
      let params = new URLSearchParams();
      params.append('cardId', this.cardId);
      axios.get("http://localhost:8050/history",{ params: params })
          .then(response=>{
            console.log(response)
            this.borrowHistory = response.data.items
          })
          .catch(error=>{
            console.log(error)
          });
    },

    logoutCard() {
      let data = {
        cardId:Number(this.cardId)
      };
      //post要写成data
      axios.post("http://localhost:8050/logout", data)
          .then(response => {
            if(response.data.ok==true){
              alert("注销成功")
              this.$router.push({ name: 'Guide' });
            }else if(response.data.message="fail to remove card, because book not returned"){
              alert("请归还所有书之后再注销")
            }else{
              alert("接口错误")
            }
          })
          .catch(error => {
            console.error(error);
          });
    },
    clearResult(){
      this.queryResult=[]
    }
    ,
    clearHistory(){
      this.borrowHistory=[]
    }
    ,
    toGuide() {
      this.$router.push('/');
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
.form input[type="date"],
select{
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
.floating-box {
  position: absolute;
  top: 100px;
  left: 50%;
  transform: translateX(-50%);
  width: 80%;
  height: 80%;
  border: 1px solid #ccc;
  background-color: #fff;
  overflow-y: scroll;
}

.floating-box h3{
  display: inline-block;
}

.floating-box button{
  width: 70px;
  height: 30px;
  font-size: 14px;
  padding: 3px;
  position: absolute;
  right: 0;
}

.floating-box ul {
  padding: 0;
  margin: 0;
  list-style: none;
}

.floating-box li {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  border-bottom: 1px solid #ccc;
}

.floating-box li:last-child {
  border-bottom: none;
}

.redLi{
  background-color: rgba(139, 0, 0, 0.5);

}
</style>