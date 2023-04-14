<template>
  <Cheader></Cheader>
  <div class="head">
  </div>
  <div class="box">
    <h1>请选择您的身份</h1>
    <div class="bbox">
      <Cbutton text="师生" @click="showInfoBox = true"></Cbutton>
      <Cbutton text="管理员" @click="toAdmin"></Cbutton>
    </div>
    <router-link to="/register">注册</router-link>
  </div>
  <div class="mask" v-if="showInfoBox">
    <div class="info-box-wrapper">
      <span class="close" @click="closeInfoBox">×</span>
      <info-box></info-box>
    </div>
  </div>
</template>

<script>
import Cbutton from '../components/Cbutton.vue'
import Cheader from "@/components/Cheader.vue";
import InfoBox from "@/components/InfoBox.vue";
export default {
  name: "Guide",
  data() {
    return {
      showInfoBox: false,
    };
  },
  methods: {
    toAdmin() {
      this.$router.push({ name: 'Admin' });
    },
    toCustomer() {
      this.$router.push({ name: 'Customer' });
    },
    closeInfoBox() {
      this.showInfoBox = false;
    },
  },
  components: {
    Cbutton,
    Cheader,
    InfoBox
  }
}
</script>

<style scoped>
  .box{
    width: 500px;
    margin: 0 auto;
    text-align: center;
  }
  .bbox{
    display: flex;
    justify-content: center;
  }
  .head{
    height: 10vh;
  }
  a {
    color: inherit; /* 改变字体颜色 */
    text-decoration: none; /* 去除下划线 */
    font-size: 1rem;
  }
  .mask {
    position: fixed;
    top: 0;
    left: 0;
    z-index: 999;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .info-box-wrapper {
    position: relative;
    background-color: #fff;
    border-radius: 0.5rem;
    padding: 1rem;
    box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.5);
  }
  .close {
    color: black;
    position: absolute;
    top: 0.5rem;
    right: 0.5rem;
    font-size: 1.5rem;
    cursor: pointer;
  }

</style>