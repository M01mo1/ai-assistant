# AI客服智能助手 - 前端

电商交易链路AI客服工单智能助手系统前端项目

## 技术栈

- Vue 3 + TypeScript
- Vite
- Element Plus
- Pinia
- Vue Router
- Axios

## 开发环境启动

### 安装依赖
```bash
npm install
```

### 启动开发服务器
```bash
npm run dev
```

访问 http://localhost:3000

### 构建生产版本
```bash
npm run build
```

### 代码检查
```bash
npm run lint
```

### 代码格式化
```bash
npm run format
```

## 项目结构

```
src/
├── api/           # API接口
├── assets/        # 静态资源
├── components/    # 公共组件
├── stores/        # 状态管理
├── router/        # 路由配置
├── types/         # 类型定义
├── utils/         # 工具函数
├── views/         # 页面组件
├── styles/        # 全局样式
├── App.vue        # 根组件
└── main.ts        # 入口文件
```

## 功能模块

- 智能对话：与AI客服进行多轮对话
- 会话管理：管理历史对话会话
- 知识库管理：管理系统知识库文档
- 知识检索：语义检索和RAG增强查询
- 系统监控：系统健康状态监控
