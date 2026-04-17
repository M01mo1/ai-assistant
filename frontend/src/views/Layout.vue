<template>
  <el-container class="layout-container">
    <!-- 侧边栏 -->
    <el-aside :width="isCollapsed ? '64px' : '220px'" class="layout-aside">
      <div class="logo-container">
        <el-icon class="logo-icon"><Service /></el-icon>
        <span v-show="!isCollapsed" class="logo-text">AI客服助手</span>
      </div>
      
      <el-menu
        :default-active="currentRoute"
        :collapse="isCollapsed"
        router
        class="layout-menu"
      >
        <el-menu-item index="/chat">
          <el-icon><ChatDotRound /></el-icon>
          <template #title>智能对话</template>
        </el-menu-item>
        
        <el-menu-item index="/sessions">
          <el-icon><List /></el-icon>
          <template #title>会话历史</template>
        </el-menu-item>
        
        <el-menu-item index="/knowledge">
          <el-icon><Document /></el-icon>
          <template #title>知识库管理</template>
        </el-menu-item>
        
        <el-menu-item index="/search">
          <el-icon><Search /></el-icon>
          <template #title>知识检索</template>
        </el-menu-item>
        
        <el-menu-item index="/system">
          <el-icon><Setting /></el-icon>
          <template #title>系统信息</template>
        </el-menu-item>
      </el-menu>
      
      <!-- 折叠按钮 -->
      <div class="collapse-btn" @click="isCollapsed = !isCollapsed">
        <el-icon>
          <Expand v-if="isCollapsed" />
          <Fold v-else />
        </el-icon>
      </div>
    </el-aside>
    
    <!-- 主内容区 -->
    <el-container class="layout-main">
      <!-- 顶部栏 -->
      <el-header class="layout-header">
        <div class="header-title">
          <h1>{{ currentTitle }}</h1>
        </div>
        <div class="header-right">
          <el-dropdown>
            <div class="user-info">
              <el-avatar :size="32" class="user-avatar">
                <el-icon><User /></el-icon>
              </el-avatar>
              <span class="user-name">{{ userStore.userName }}</span>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>
                  <el-icon><User /></el-icon>
                  用户ID: {{ userStore.userId }}
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      
      <!-- 内容区 -->
      <el-main class="layout-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'

const route = useRoute()
const userStore = useUserStore()

const isCollapsed = ref(false)

const currentRoute = computed(() => route.path)
const currentTitle = computed(() => (route.meta.title as string) || '首页')
</script>

<style lang="scss" scoped>
.layout-container {
  height: 100vh;
  background: #f0f2f5;
}

.layout-aside {
  background: linear-gradient(180deg, #1a1a2e 0%, #16213e 100%);
  transition: width 0.3s ease;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.logo-container {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  padding: 0 16px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  
  .logo-icon {
    font-size: 28px;
    color: #409eff;
  }
  
  .logo-text {
    font-size: 16px;
    font-weight: 600;
    color: #fff;
    white-space: nowrap;
  }
}

.layout-menu {
  flex: 1;
  border-right: none;
  background: transparent;
  
  :deep(.el-menu-item) {
    color: rgba(255, 255, 255, 0.7);
    
    &:hover {
      background: rgba(255, 255, 255, 0.1);
      color: #fff;
    }
    
    &.is-active {
      background: linear-gradient(90deg, #409eff 0%, #53a8ff 100%);
      color: #fff;
    }
    
    .el-icon {
      color: inherit;
    }
  }
}

.collapse-btn {
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: rgba(255, 255, 255, 0.7);
  cursor: pointer;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  transition: all 0.3s;
  
  &:hover {
    background: rgba(255, 255, 255, 0.1);
    color: #fff;
  }
  
  .el-icon {
    font-size: 20px;
  }
}

.layout-main {
  flex-direction: column;
}

.layout-header {
  height: 60px;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  
  .header-title h1 {
    font-size: 18px;
    font-weight: 600;
    color: #303133;
    margin: 0;
  }
  
  .header-right {
    display: flex;
    align-items: center;
  }
  
  .user-info {
    display: flex;
    align-items: center;
    gap: 8px;
    cursor: pointer;
    padding: 4px 8px;
    border-radius: 4px;
    transition: background 0.3s;
    
    &:hover {
      background: #f5f7fa;
    }
  }
  
  .user-avatar {
    background: linear-gradient(135deg, #409eff, #53a8ff);
  }
  
  .user-name {
    font-size: 14px;
    color: #606266;
  }
}

.layout-content {
  padding: 20px;
  overflow-y: auto;
  background: #f0f2f5;
}
</style>
