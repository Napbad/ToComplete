<template>
  <div class="memo-detail-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>备忘录详情</span>
          <el-button @click="$router.back()" type="primary">返回</el-button>
        </div>
      </template>
      <div class="memo-detail-content">
        <p>备忘录ID: {{ $route.params.id }}</p>
        <p>备忘录详情页面正在开发中...</p>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { useRoute } from 'vue-router'

const route = useRoute()
</script>

<style scoped>
.memo-detail-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.memo-detail-content {
  min-height: 400px;
  padding: 20px;
}
</style> 