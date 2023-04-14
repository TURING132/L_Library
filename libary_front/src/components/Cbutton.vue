<template>
  <button class="c-button">
    {{ buttonText }}
  </button>
</template>

<script>
export default {
  props: {
    text: {
      type: String,
      default: 'Click me!'
    }
  },
  data() {
    return {
      buttonText: this.text
    }
  },
  methods: {
  },
  name: "Cbutton"
}
</script>

<style scoped>
  .c-button {
    padding: 1rem;
    margin: 1rem;
    color: black;
    border: 1px black solid;
    background-color: white;
    border-radius: 0.5rem;
    cursor: pointer;
    font-size: 2rem;
    font-weight: 200;
  }
</style>