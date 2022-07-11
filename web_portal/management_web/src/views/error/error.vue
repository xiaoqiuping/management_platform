<template>
  <div>
    <div class="app-container">
      <el-col :span="12">
        <img :src="img_404" alt="404" class="img-style">
      </el-col>
      <el-col :span="12">
        <div style="margin-left: 100px;margin-top: 60px">
          <h2 style="color: #606266">很抱歉，系统出错了！</h2>
        </div>
      </el-col>
    </div>
  </div>
</template>
<script>
  import img_404 from '@/assets/gif/gif_404.gif';

  export default {
    name: '404',
    data() {
      return {
        img_404
      }
    },
    methods: {
      handleGoMain() {
        this.$router.push({path: '/'})
      }
    },
  }
</script>

<style scoped>
  .app-container {
    width: 80%;
    margin: 120px auto;
  }

  .img-style {
    width: auto;
    height: auto;
    max-width: 100%;
    max-height: 100%;
  }
</style>
