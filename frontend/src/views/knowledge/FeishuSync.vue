<template>
  <div class="feishu-sync">
    <div class="page-header">
      <el-button @click="router.back()" :icon="ArrowLeft" text>
        返回
      </el-button>
      <h1 class="page-title">同步飞书文档</h1>
    </div>

    <div class="form-card">
      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-position="top"
        size="large"
      >
        <el-alert
          type="info"
          :closable="false"
          class="info-alert"
        >
          <template #title>
            <p>请输入飞书文档的Token，您可以从文档URL中获取。</p>
            <p>例如：https://xxx.feishu.cn/docs/<strong>doccnXXXXXX</strong></p>
          </template>
        </el-alert>

        <el-form-item label="文档Token" prop="docToken">
          <el-input
            v-model="formData.docToken"
            placeholder="请输入飞书文档Token"
            clearable
          >
            <template #prefix>
              <el-icon><Link /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item label="文档类型" prop="documentType">
          <el-select v-model="formData.documentType" style="width: 100%">
            <el-option label="飞书文档" value="FEISHU_DOC" />
            <el-option label="飞书云文档" value="FEISHU_DOCX" />
          </el-select>
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
            <el-button type="primary" @click="handleSync" :loading="syncing">
              <el-icon><Refresh /></el-icon>
              开始同步
            </el-button>
          </div>
        </el-form-item>
      </el-form>

      <!-- 同步结果 -->
      <div v-if="syncResult" class="sync-result">
        <el-result
          icon="success"
          title="同步成功"
          :sub-title="`文档「${syncResult.title}」已同步完成`"
        >
          <template #extra>
            <div class="result-info">
              <el-tag type="info">分块数: {{ syncResult.chunkCount }}</el-tag>
              <el-tag type="success">已启用</el-tag>
            </div>
            <el-button type="primary" @click="router.push('/knowledge')">
              查看文档列表
            </el-button>
          </template>
        </el-result>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowLeft, Refresh } from '@element-plus/icons-vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import * as knowledgeApi from '@/api/knowledge'
import type { KnowledgeDocument } from '@/types/knowledge'

const router = useRouter()
const formRef = ref<FormInstance>()
const syncing = ref(false)
const syncResult = ref<KnowledgeDocument | null>(null)

const formData = reactive({
  docToken: '',
  documentType: 'FEISHU_DOC',
  tags: [] as string[]
})

const suggestedTags = [
  '飞书同步',
  '退货政策',
  '物流配送',
  '售后服务',
  '订单查询'
]

const rules: FormRules = {
  docToken: [
    { required: true, message: '请输入飞书文档Token', trigger: 'blur' },
    { min: 5, message: 'Token长度不正确', trigger: 'blur' }
  ]
}

const handleSync = async () => {
  const valid = await formRef.value?.validate()
  if (!valid) return

  syncing.value = true
  syncResult.value = null

  try {
    const result = await knowledgeApi.syncFeishuDocument({
      docToken: formData.docToken,
      documentType: formData.documentType,
      tags: formData.tags
    })
    syncResult.value = result
    ElMessage.success('文档同步成功')
  } catch (error) {
    ElMessage.error('同步文档失败，请检查Token是否正确')
  } finally {
    syncing.value = false
  }
}
</script>

<style lang="scss" scoped>
.feishu-sync {
  max-width: 700px;
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

.info-alert {
  margin-bottom: 24px;

  p {
    margin: 4px 0;
    font-size: 13px;
  }

  strong {
    color: #409eff;
  }
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  width: 100%;
}

.sync-result {
  margin-top: 24px;
  padding-top: 24px;
  border-top: 1px solid #ebeef5;

  .result-info {
    display: flex;
    gap: 8px;
    margin-bottom: 16px;
  }
}
</style>
