<template>
  <Cheader></Cheader>
  <div class="register-container">
    <div class="register-box" :style="{height: cardId ? '300px' : '250px', transition: 'height 1s'}">
      <div class="options">
        <label for="teacher">
          <input type="radio" id="teacher" name="identity" value="teacher" v-model="identity">老师
        </label>
        <label for="student">
          <input type="radio" id="student" name="identity" value="student" v-model="identity">学生
        </label>
      </div>
      <div class="department">
        <label for="department">部门：</label>
        <input type="text" id="department" v-model="department">
      </div>
      <p class="words"  :style="{height: cardId ? '50px' : '0px', transition: 'height 1s'}">
        <span v-show="cardId">您的id为: <span class="large" >{{ cardId }}</span></span>
      </p>
      <button @click="registerCardId">注册</button>
    </div>
  </div>
</template>

<script>
import Cheader from "@/components/Cheader.vue";
export default {
  name: "Register",
  components:{
    Cheader:Cheader
  },
  data() {
    return {
      cardId: null,
      department:null,
      identity:null
    };
  },
  methods: {
    registerCardId() {
      // 模拟注册操作，可以替换为实际的注册逻辑
      this.cardId = "xxxx";
      console.log(this.cardId,this.department,this.identity)//打印看看
    },
  },

}
</script>

<style scoped>

.register-box {
  margin: 100px auto;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 600px;
  /*height: 400px;*/
  border-radius: 8px;
  border: solid 1px black;
  transition: 0.5s;
}

button {
  width: 400px;
  height: 40px;
  font-size: 16px;
  background-color: #ffffff;
  border: 1px solid black;
  border-radius: 4px;
  margin-bottom: 20px;
  cursor: pointer;
}

.words{
  height: 50px;
  width: 100%;
}

.words .large{
  font-size: 2rem;
}

p {
  text-align: center;
}

.options{
  display: flex;
  justify-content: center;
  margin-top: 20px;
  margin-bottom: 20px;
}

.options label{
  margin: 0 20px;
  font-size: 16px;
}

.department{
  display: flex;
  justify-content: center;
}

.department label{
  margin-right: 20px;
  font-size: 16px;
}

.department input{
  width: 200px;
  height: 30px;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  padding-left: 5px;
  font-size: 16px;
}

</style>