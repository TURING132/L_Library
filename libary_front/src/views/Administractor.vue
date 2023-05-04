<template>
  <Cheader></Cheader>
  <div>
    <button @click="operation = 'search'">查询书籍</button>
    <button @click="operation = 'add'">添加图书</button>
    <button @click="operation = 'del'">删除图书</button>
    <button @click="operation = 'modify'">修改图书信息</button>
    <button @click="operation = 'inc'">修改库存</button>
    <button @click="operation = 'his'">查询借书记录</button>
    <button @click="operation = 'card'">查询所有卡</button>
    <button @click="operation = 'imp'">批量导入书</button>
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


    <div v-if="operation === 'add'">
      <div class="form">
        <h3>添加图书</h3>
        <div>
          <label for="book-name">书籍名：</label>
          <input type="text" id="book-name" v-model="newBookName">
        </div>
        <div>
          <label for="book-author">作者：</label>
          <input type="text" id="book-author" v-model="newBookAuthor">
        </div>
        <div>
          <label for="book-author">类别：</label>
          <input type="text" id="book-category" v-model="newBookCategory">
        </div>
        <div>
          <label for="book-publisher">出版社：</label>
          <input type="text" id="book-publisher" v-model="newBookPublisher">
        </div>
        <div>
          <label for="book-pubdate">出版年份：</label>
          <input type="number" id="book-pubdate" v-model="newBookYear">
        </div>
        <div>
          <label for="book-price">价格：</label>
          <input type="text" id="book-price" v-model="newBookPrice">
        </div>
        <div>
          <label for="book-price">库存：</label>
          <input type="number" step="1" id="book-price"  v-model="newBookStock">
        </div>
        <button @click="addBook">添加</button>
      </div>

    </div>
    <div v-if="operation === 'del'">
      <div class="form">
        <h3>删除图书</h3>
        <div>
          <label for="book-id">书籍编号：</label>
          <input type="number" id="book-id" v-model="deleteBookId">
        </div>
        <button @click="deleteBook">删除</button>
      </div>
    </div>

    <div v-if="operation === 'modify'">
      <div class="form">
        <h3>修改图书信息</h3>
        <div>
          <label for="book-id">书籍编号：</label>
          <input type="number" id="book-id" v-model="updateBookId">
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
          <label for="update-author">类别：</label>
          <input type="text" id="update-category" v-model="updateBookCategory">
        </div>
        <div>
          <label for="update-publisher">出版社：</label>
          <input type="text" id="update-publisher" v-model="updateBookPublisher">
        </div>
        <div>
          <label for="update-pubdate">出版年份：</label>
          <input type="number" id="update-pubdate" v-model="updateBookPublishYear">
        </div>
        <div>
          <label for="update-price">价格：</label>
          <input type="text" id="update-price" v-model="updateBookPrice">
        </div>
        <button @click="updateBook">修改</button>
      </div>
    </div>

    <div v-if="operation === 'inc'">
      <div class="form">
        <h3>修改库存</h3>
        <div>
          <label for="book-id">书籍编号：</label>
          <input type="number" id="book-id" v-model="stockBookId">
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
    <div v-if="operation === 'imp'">
      <div class="form">
        <input type="file" id="file-input" />
        <button @click="parseCSV">批量导入</button>
      </div>
    </div>
    <div v-if="operation === 'card'">
      <div class="form">
        <button @click="getAllCard">查看所有卡</button>
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
  <div v-if="allCard.length">
    <div class="floating-box">
      <h3>所有借书卡</h3><button class="close" @click="clearCard">关闭</button>
      <ul><li>
        <span>借书卡编号</span>
        <span>用户名</span>
        <span>院系</span>
        <span>类型</span>


      </li></ul>

      <ul>
        <li v-for="card in allCard" :key="card.cardId">
          <span>{{ card.cardId }}</span>
          <span>{{card.name}}</span>
          <span>{{ card.department }}</span>
          <span>{{card.type}}</span>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import Cheader from "@/components/Cheader.vue";
import axios from "axios";
import Papa from 'papaparse';

export default {
  name: "Administractor",
  data() {
    return {
      name: "",
      operation:"",
      cardId: '',
      borrowRecords: null,
      stockBookId: "",
      deltaStock: 0,
      queryResult:[],
      category:"",
      author: "",
      press: "",
      minPublish: "",
      maxPublish: "",
      minPrice: "",
      maxPrice:"",
      sortBy:"",
      order:"",
      newBookName:"",
      newBookCategory:"",
      newBookPrice:"",
      newBookAuthor:"",
      newBookPublisher:"",
      newBookYear:"",
      newBookStock:"",
      updateBookId:null,
      updateBookName:null,
      updateBookAuthor:null,
      updateBookPublisher:null,
      updateBookPublishYear:null,
      updateBookCategory:null,
      updateBookPrice:null,
      deleteBookId:null,
      borrowHistory:[],
      allCard:[]
    };
  },
  components:{
    Cheader:Cheader
  }
  ,
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
      params.append('sortBy',this.sortBy);
      params.append('order',this.order)
      axios.get("http://localhost:8050/query",{ params: params })
          .then(response=>{
            this.queryResult = response.data.results
          })
          .catch(error=>{
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
    clearResult(){
      this.queryResult=[]
    },
    addBook() {
      if(!this.newBookPrice||!this.newBookName||!this.newBookStock||
      !this.newBookCategory||!this.newBookPublisher||!this.newBookPrice
      ||!this.newBookYear){
        alert("请将信息填写齐全")
        return
      }
      let data = {
        title:this.newBookName,
        author:this.newBookAuthor,
        category:this.newBookCategory,
        press:this.newBookPublisher,
        price:this.newBookPrice,
        stock:this.newBookStock,
        publishyear:this.newBookYear
      };

      //post要写成data
      axios.post("http://localhost:8050/addbook", data)
          .then(response => {
            if(response.data.ok==true){
              alert("添加成功，新书的id为"+response.data.message)
              this.newBookYear="";this.newBookStock="";
              this.newBookPublisher="";this.newBookCategory="";
              this.newBookPrice="";this.newBookName="";
              this.newBookAuthor="";
            }else{
              alert("添加失败，书已存在")
            }
          })
          .catch(error => {
            console.error(error);
          });

    },
    deleteBook() {
      // 删除图书的逻辑
      if(!this.deleteBookId){
        alert("请填写编号")
        return
      }
      let data = {
        bookId:this.deleteBookId
      };

      //post要写成data
      axios.post("http://localhost:8050/delbook", data)
          .then(response => {
            if(response.data.ok==true){
              alert("删除编号为"+response.data.message+"的书籍成功")
              this.deleteBookId=""
            }else if(response.data.message=="not exist book id"){
              alert("不存在的书籍")
            }else if(response.data.message=="not return book"){
              alert("有尚未归还的书")
            }else{
              alert("接口错误")
            }
          })
          .catch(error => {
            console.error(error);
          });
    },
    updateBook() {
      // 修改图书信息的逻辑
      if(this.updateBookId==null){
        alert("必须填写书本编号")
        return
      }
      if(this.updateBookName==null&&this.updateBookAuthor==null
      &&this.updateBookCategory==null&&this.updateBookPrice==null&&this.updateBookPublishYear==null
      &&this.updateBookPublisher==null){
        alert("请至少填写一项信息")
      }
      let data = {
        title:this.updateBookName,
        author:this.updateBookAuthor,
        category:this.updateBookCategory,
        press:this.updateBookPublisher,
        price:this.updateBookPrice,
        bookId:this.updateBookId,
        publishyear:this.updateBookPublishYear
      };
      if(this.updateBookPrice==null)data.price="-1.0";
      if(this.updateBookPublishYear==null)data.publishyear=-1;
      console.log(data)
      //post要写成data
      axios.post("http://localhost:8050/modify", data)
          .then(response => {
            if(response.data.ok==true){
              alert("修改成功")
            }else if(response.data.message="invalid book_id"){
              alert("无效的编号")
            }else{
              alert("接口错误")
            }
          })
          .catch(error => {
            console.error(error);
          });

    },
    changeStock() {
      // 修改库存的逻辑
      if(!this.stockBookId||!this.deltaStock){
        alert("请将信息填写齐全")
        return
      }
      let data = {
        bookId:this.stockBookId,
        delta:this.deltaStock
      };

      //post要写成data
      axios.post("http://localhost:8050/stock", data)
          .then(response => {
                if(response.data.ok==true){
                  alert("修改成功")
                }else if(response.data.message=="no so many book stock"){
                  alert("库存不足")
                }else if(response.data.message=="book_id not exist"){
                  alert("不存在的编号")
                }else{
                  alert("接口错误");
                }
          })
          .catch(error => {
            console.error(error);
          });
    },
    toGuide() {
      this.$router.push('/');
    }
    ,
    async getBorrowRecords() {
      // 发送查看借书记录请求，并将结果保存到borrowHistory数组中
      let params = new URLSearchParams();
      params.append('cardId', this.cardId);
      axios.get("http://localhost:8050/history",{ params: params })
          .then(response=>{
            if(response.data.count==0){
              alert("没有借书记录")
              return
            }
            this.borrowHistory = response.data.items
          })
          .catch(error=>{
            console.log(error)
          });
    },
    clearHistory(){
      this.borrowHistory=[]
    },
    clearCard(){
      this.allCard=[]
    },
    parseCSV() {
      const fileInput = document.getElementById("file-input");
      const file = fileInput.files[0];
      const reader = new FileReader();
      const data = []
      reader.onload = () => {
        const fileContent = reader.result;
        const regex = /\((\d+),\s*([\u4e00-\u9fa5]+),\s*([\w\s]+),\s*([\u4e00-\u9fa5]+\w?),\s*(\d{4}),\s*([\w\s]+),\s*(\d+\.\d+),\s*(\d+)\)/g;
        let match;
        while ((match = regex.exec(fileContent)) !== null) {
          const bookId = match[1]; // 书号
          const category = match[2]; // 类别
          const title = match[3]; // 书名
          const press = match[4]; // 出版社
          const publishYear = match[5]; // 年份
          const author = match[6]; // 作者
          const price = match[7]; // 价格
          const stock = match[8]; // 数量
          const book = {
            bookId,
            category,
            title,
            press,
            publishYear,
            author,
            price,
            stock,
          };
          data.push(book); // 把对象添加到数组中
        }
        axios.post("http://localhost:8050/imp",JSON.stringify(data),{
          headers: {
            'Content-Type': 'application/json'
          }
        })
            .then(response => {
              if (response.data.ok == true) {
                alert("批量导入成功")
              }else {
                alert("接口错误 请查看是否有重复的书")
              }
            })
            .catch(error => {
              console.error(error);
            });
        console.log(data);
      };

      reader.readAsText(file);
    },
    getAllCard(){
      axios.get("http://localhost:8050/showCard")
          .then(response=>{
            if(response.data.ok==true){
              this.allCard = response.data.payload.cards

            }
          })
          .catch(error=>{
            console.log(error)
          });
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