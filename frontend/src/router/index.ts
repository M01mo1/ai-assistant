import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    component: () => import('@/views/Layout.vue'),
    redirect: '/chat',
    children: [
      {
        path: '/chat',
        name: 'Chat',
        component: () => import('@/views/chat/ChatMain.vue'),
        meta: { title: '智能对话' }
      },
      {
        path: '/sessions',
        name: 'Sessions',
        component: () => import('@/views/chat/SessionHistory.vue'),
        meta: { title: '会话历史' }
      },
      {
        path: '/knowledge',
        name: 'Knowledge',
        component: () => import('@/views/knowledge/KnowledgeList.vue'),
        meta: { title: '知识库管理' }
      },
      {
        path: '/knowledge/add',
        name: 'KnowledgeAdd',
        component: () => import('@/views/knowledge/KnowledgeAdd.vue'),
        meta: { title: '添加知识文档' }
      },
      {
        path: '/knowledge/feishu',
        name: 'FeishuSync',
        component: () => import('@/views/knowledge/FeishuSync.vue'),
        meta: { title: '同步飞书文档' }
      },
      {
        path: '/search',
        name: 'Search',
        component: () => import('@/views/search/KnowledgeSearch.vue'),
        meta: { title: '知识检索' }
      },
      {
        path: '/system',
        name: 'System',
        component: () => import('@/views/system/SystemInfo.vue'),
        meta: { title: '系统信息' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫 - 设置页面标题
router.beforeEach((to, _from, next) => {
  document.title = `${to.meta.title || '首页'} - AI客服助手`
  next()
})

export default router
