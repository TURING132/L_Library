<template>
  <div class="info-box">
    <h2>请输入信息</h2>
    <label>Card ID:</label>
    <input type="number" v-model="cardId">
    <button class="submit-btn" @click="submit">提交</button>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "InfoBox",
  data() {
    return {
      cardId: null,
    };
  },
  methods: {
    submit() {
      let params = new URLSearchParams();
      params.append('cardId',this.cardId)
      axios.get("http://localhost:8050/checkCard",{params:params})
          .then(response=>{
            if(response.data.ok){
              this.$router.push({// 在这里可以提交表单信息
                name: "Customer",
                params: {
                  cardId: this.cardId,
                }
              });
            }else{
              alert("不存在的卡号，请先注册")
            }
          })


    },
  },
}
</script>

<style scoped>
.info-box {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100%;
  text-align: center;
}

.info-box h2{
  color: black;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  color: black;
}

input[type="text"] {
  padding: 0.5rem;
  margin-bottom: 1rem;
  color: black;
  border: 1px solid black;
  border-radius: 0.5rem;
  font-size: 1rem;
}

input[type="radio"] {
  margin-right: 0.5rem;
}

.submit-btn {
  padding: 1rem 2rem;
  margin-top: 1rem;
  color: black;
  background-color: white;
  border: 1px solid black;
  border-radius: 0.5rem;
  cursor: pointer;
  font-size: 1rem;
}

</style>