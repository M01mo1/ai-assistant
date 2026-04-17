<template>
  <div class="knowledge-add">
    <div class="page-header">
      <el-button @click="router.back()" :icon="ArrowLeft" text>
        返回
      </el-button>
      <h1 class="page-title">添加知识文档</h1>
    </div>

    <div class="form-card">
      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-position="top"
        size="large"
      >
        <el-form-item label="文档标题" prop="title">
          <el-input
            v-model="formData.title"
            placeholder="请输入文档标题"
            maxlength="100"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="文档类型" prop="documentType">
          <el-select v-model="formData.documentType" style="width: 100%">
            <el-option label="手动添加" value="MANUAL" />
            <el-option label="导入文档" value="IMPORTED" />
          </el-select>
        </el-form-item>

        <el-form-item label="文档内容" prop="content">
          <el-input
            v-model="formData.content"
            type="textarea"
            :rows="12"
            placeholder="请输入文档内容，支持Markdown格式"
          />
        </el-form-item>

        <el-form-item label="标签" prop="tags">
          <el-select
            v-model="formData.tags"
            multiple
            filterable
            allow-create
            default-first-option
            placeholder="添加标签（可选）"
            style="width: 100%"
          >
            <el-option
              v-for="tag in suggestedTags"
              :key="tag"
              :label="tag"
              :value="tag"
            />
          </el-select>
        </el-form-item>

        <el-form-item>
          <div class="form-actions">
            <el-button @click="router.back()">取消</el-button>
            <el-button type="primary" @click="handleSubmit" :loading="submitting">
              提交
            </el-button>
          </div>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import * as knowledgeApi from '@/api/knowledge'

const router = useRouter()
const formRef = ref<FormInstance>()
const submitting = ref(false)

const formData = reactive({
  title: '',
  content: '',
  documentType: 'MANUAL',
  tags: [] as string[]
})

const suggestedTags = [
  '退货政策',
  '物流配送',
  '售后服务',
  '订单查询',
  '支付问题',
  '会员权益'
]

const rules: FormRules = {
  title: [
    { required: true, message: '请输入文档标题', trigger: 'blur' },
    { min: 2, max: 100, message: '标题长度在 2 到 100 个字符', trigger: 'blur' }
  ],
  content: [
    { required: true, message: '请输入文档内容', trigger: 'blur' },
    { min: 10, message: '内容至少需要 10 个字符', trigger: 'blur' }
  ]
}

const handleSubmit = async () => {
  const valid = await formRef.value?.validate()
  if (!valid) return

  submitting.value = true
  try {
    await knowledgeApi.addDocument({
      title: formData.title,
      content: formData.content,
      documentType: formData.documentType,
      tags: formData.tags
    })
    ElMessage.success('文档添加成功')
    router.push('/knowledge')
  } catch (error) {
    ElMessage.error('添加文档失败')
  } finally {
    submitting.value = false
  }
}
</script>

<style lang="scss" scoped>
.knowledge-add {
  max-width: 800px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 24px;

  .page-title {
    margin: 0;
    flex: 1;
  }
}

.form-card {
  background: #fff;
  border-radius: 12px;
  padding: 32px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  width: 100%;
}
</style>
