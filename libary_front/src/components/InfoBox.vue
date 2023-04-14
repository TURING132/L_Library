<template>
  <div class="info-box">
    <h2>请输入信息</h2>
    <label>Card ID:</label>
    <input type="text" v-model="cardId">
    <div>
      <label>
        <input type="radio" value="teacher" v-model="identity">
<!--        动态绑定值到identity-->
        老师
      </label>
      <label>
        <input type="radio" value="student" v-model="identity">
        学生
      </label>
    </div>
    <button class="submit-btn" @click="submit">提交</button>
  </div>
</template>

<script>
export default {
  name: "InfoBox",
  data() {
    return {
      cardId: "",
      identity: "",
    };
  },
  methods: {
    submit() {
      // 在这里可以提交表单信息
      this.$router.push({
        name: "Customer",
        params: {
          cardId: this.cardId,
          identity: this.identity
        }
      });
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