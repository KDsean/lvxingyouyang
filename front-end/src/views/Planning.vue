<template>
  <main class="max-w-[1440px] mx-auto px-4 py-8">
    <div class="max-w-4xl mx-auto">
      <!-- AI 对话区域 -->
      <div class="bg-slate-900 rounded-3xl p-8 text-white mb-8">
        <div class="flex items-center justify-between mb-6">
          <div>
            <h1 class="text-3xl font-bold mb-2">AI 行程规划助手</h1>
            <p class="text-gray-400">告诉我你的旅行想法，我来帮你规划完美行程</p>
          </div>
          <div class="px-4 py-2 rounded-full bg-green-500/20 border border-green-500 text-green-400 text-sm flex items-center">
            <div class="w-2 h-2 rounded-full bg-green-500 animate-pulse mr-2"></div>
            在线
          </div>
        </div>

        <!-- 聊天消息 -->
        <div class="bg-white/5 rounded-2xl p-6 mb-6 max-h-[500px] overflow-y-auto" ref="chatContainer">
          <div v-if="messages.length === 0" class="text-center py-12 text-gray-400">
            <span class="iconify text-6xl mb-4" data-icon="material-symbols:chat"></span>
            <p>开始对话，让AI帮你规划行程</p>
            <div class="mt-6 flex flex-wrap gap-3 justify-center">
              <el-tag
                v-for="example in exampleQuestions"
                :key="example"
                class="cursor-pointer"
                @click="sendMessage(example)"
              >
                {{ example }}
              </el-tag>
            </div>
          </div>

          <div v-else class="space-y-4">
            <div
              v-for="(message, index) in messages"
              :key="index"
              class="flex"
              :class="message.role === 'user' ? 'justify-end' : 'justify-start'"
            >
              <div
                class="max-w-[80%] rounded-2xl px-4 py-3"
                :class="message.role === 'user' ? 'bg-blue-600 text-white' : 'bg-white/10 text-white'"
              >
                <div class="text-sm mb-1 opacity-70">
                  {{ message.role === 'user' ? '你' : 'AI 助手' }}
                </div>
                <!-- 用户消息：纯文本；AI消息：渲染 Markdown -->
                <div v-if="message.role === 'user'" class="whitespace-pre-wrap">{{ message.content }}</div>
                <div v-else class="markdown-body" v-html="renderMarkdown(message.content)"></div>
                <div class="text-xs opacity-50 mt-2">{{ formatTime(message.timestamp) }}</div>
              </div>
            </div>

            <div v-if="isLoading" class="flex justify-start">
              <div class="bg-white/10 rounded-2xl px-4 py-3">
                <div class="flex items-center space-x-2">
                  <div class="w-2 h-2 bg-blue-400 rounded-full animate-bounce"></div>
                  <div class="w-2 h-2 bg-blue-400 rounded-full animate-bounce" style="animation-delay: 0.1s"></div>
                  <div class="w-2 h-2 bg-blue-400 rounded-full animate-bounce" style="animation-delay: 0.2s"></div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 输入框 -->
        <div class="flex gap-3">
          <el-input
            v-model="inputMessage"
            placeholder="输入你的旅行需求，例如：我想3月去大理玩5天，预算5000元..."
            size="large"
            @keyup.enter="handleSend"
            :disabled="isLoading"
          >
            <template #prefix>
              <span class="iconify" data-icon="material-symbols:chat"></span>
            </template>
          </el-input>
          <el-button
            type="primary"
            size="large"
            @click="handleSend"
            :loading="isLoading"
            class="bg-blue-600"
          >
            <span class="iconify" data-icon="material-symbols:send"></span>
          </el-button>
        </div>
      </div>

      <!-- 快速规划表单 -->
      <div class="bg-white rounded-2xl p-8 shadow-sm border border-gray-100">
        <h2 class="text-2xl font-bold mb-6">快速规划</h2>

        <el-form :model="planForm" label-width="100px">
          <el-form-item label="出发地">
            <el-input v-model="planForm.origin" placeholder="例如：杭州"></el-input>
          </el-form-item>

          <el-form-item label="目的地">
            <el-input v-model="planForm.destination" placeholder="例如：大理"></el-input>
          </el-form-item>

          <el-form-item label="出行天数">
            <el-input-number v-model="planForm.days" :min="1" :max="30"></el-input-number>
          </el-form-item>

          <el-form-item label="预算">
            <el-input v-model="planForm.budget" placeholder="例如：5000">
              <template #append>元</template>
            </el-input>
          </el-form-item>

          <el-form-item label="出发日期">
            <el-date-picker
              v-model="planForm.startDate"
              type="date"
              placeholder="选择日期"
              value-format="YYYY-MM-DD"
              style="width: 100%"
            ></el-date-picker>
          </el-form-item>

          <el-form-item label="兴趣偏好">
            <el-checkbox-group v-model="planForm.interests">
              <el-checkbox label="美食"></el-checkbox>
              <el-checkbox label="摄影"></el-checkbox>
              <el-checkbox label="历史文化"></el-checkbox>
              <el-checkbox label="自然风光"></el-checkbox>
              <el-checkbox label="购物"></el-checkbox>
              <el-checkbox label="休闲放松"></el-checkbox>
            </el-checkbox-group>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="generatePlan" :loading="isGenerating" class="w-full">
              <span class="iconify mr-2" data-icon="material-symbols:auto-awesome"></span>
              AI 生成行程
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </main>
</template>

<script setup lang="ts">
import { ref, reactive, nextTick } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import MarkdownIt from 'markdown-it'
import type { AIMessage } from '@/types'
import { chatStream, generatePlan as generatePlanApi } from '@/api/modules/planning'
import { createTrip } from '@/api/modules/user'
import { useUserStore } from '@/stores/user'

// Markdown 渲染器
const md = new MarkdownIt({
  html: false,
  linkify: true,
  typographer: true,
  breaks: true,
})

const userStore = useUserStore()

const renderMarkdown = (content: string): string => {
  if (!content) return ''
  return md.render(content)
}

const router = useRouter()
const route = useRoute()

const inputMessage = ref('')
const messages = ref<AIMessage[]>([])
const isLoading = ref(false)
const isGenerating = ref(false)
const chatContainer = ref<HTMLElement>()

let currentAbortController: AbortController | null = null

const exampleQuestions = [
  '我想3月去大理玩5天',
  '推荐适合亲子游的目的地',
  '预算3000元的周末游',
  '想去看海，有什么推荐'
]

const planForm = reactive({
  origin: '',
  destination: '',
  days: 3,
  budget: '',
  startDate: '',
  interests: [] as string[]
})

if (route.query.q) {
  inputMessage.value = route.query.q as string
  handleSend()
}

// 从历史记录「编辑」跳回时，预填表单
if (route.query.edit) {
  if (route.query.origin) planForm.origin = route.query.origin as string
  if (route.query.destination) planForm.destination = route.query.destination as string
  if (route.query.days) planForm.days = Number(route.query.days)
  if (route.query.budget) planForm.budget = route.query.budget as string
  if (route.query.startDate) planForm.startDate = route.query.startDate as string
  if (route.query.interests) planForm.interests = (route.query.interests as string).split(',').filter(Boolean)
}

async function handleSend() {
  if (!inputMessage.value.trim()) {
    ElMessage.warning('请输入消息')
    return
  }
  await sendMessage(inputMessage.value)
  inputMessage.value = ''
}

async function sendMessage(content: string) {
  if (currentAbortController) {
    currentAbortController.abort()
  }

  messages.value.push({
    role: 'user',
    content,
    timestamp: new Date().toISOString()
  })
  isLoading.value = true

  const aiMessage: AIMessage = {
    role: 'assistant',
    content: '',
    timestamp: new Date().toISOString()
  }
  messages.value.push(aiMessage)
  const aiIndex = messages.value.length - 1

  await nextTick()
  chatContainer.value?.scrollTo({ top: chatContainer.value.scrollHeight, behavior: 'smooth' })

  const history = messages.value
    .slice(0, -2)
    .slice(-10)
    .map(m => ({
      role: m.role,
      // 确保 content 是纯文本字符串，移除任何 HTML 或其他非字符串内容
      content: typeof m.content === 'string' ? m.content : String(m.content || '')
    }))

  currentAbortController = chatStream(
    { message: content, history },
    (chunk) => {
      messages.value[aiIndex].content += chunk
      nextTick(() => {
        chatContainer.value?.scrollTo({ top: chatContainer.value.scrollHeight, behavior: 'smooth' })
      })
    },
    (_fullText) => {
      isLoading.value = false
      currentAbortController = null
    },
    (error) => {
      console.error('AI 流式请求失败:', error)
      messages.value[aiIndex].content = '抱歉，AI 服务暂时不可用，请稍后重试。\n\n错误信息：' + error.message
      isLoading.value = false
      currentAbortController = null
      ElMessage.error('AI 服务连接失败')
    }
  )
}

async function generatePlan() {
  if (!planForm.destination || !planForm.budget) {
    ElMessage.warning('请填写目的地和预算')
    return
  }

  isGenerating.value = true

  try {
    const res = await generatePlanApi({
      origin: planForm.origin,
      destination: planForm.destination,
      days: planForm.days,
      budget: planForm.budget,
      startDate: planForm.startDate,
      interests: planForm.interests
    })

    // res 已经是解包后的 {content: string}
    const content = res?.content ?? res
    const contentStr = typeof content === 'string' ? content : JSON.stringify(content)
    messages.value.push({
      role: 'assistant',
      content: contentStr,
      timestamp: new Date().toISOString()
    })
    ElMessage.success('行程已生成！')
    await nextTick()
    chatContainer.value?.scrollTo({ top: chatContainer.value.scrollHeight, behavior: 'smooth' })

    // 保存到历史记录
    if (userStore.isLoggedIn) {
      try {
        const titleDest = `${planForm.destination}${planForm.days}日游`
        const title = planForm.origin ? `${planForm.origin}-${titleDest}` : titleDest
        // 确保日期为 YYYY-MM-DD 格式
        const fmtDate = (v: any): string | undefined => {
          if (!v) return undefined
          if (typeof v === 'string' && /^\d{4}-\d{2}-\d{2}$/.test(v)) return v
          const d = new Date(v)
          if (isNaN(d.getTime())) return undefined
          return d.toISOString().slice(0, 10)
        }
        await createTrip({
          title,
          origin: planForm.origin || undefined,
          destination: planForm.destination,
          days: planForm.days,
          startDate: fmtDate(planForm.startDate),
          budget: planForm.budget ? Number(planForm.budget) : undefined,
          interests: planForm.interests.length ? planForm.interests.join(',') : undefined,
          planDetails: contentStr,
        })
        ElMessage.success('行程已保存到历史记录')
      } catch (e) {
        console.error('[行程保存失败]', e)
        ElMessage.warning('行程生成成功，但保存到历史失败，请检查登录状态')
      }
    } else {
      ElMessage.info('登录后可将行程自动保存到历史记录')
    }
  } catch (error: any) {
    console.error('生成行程失败:', error)
    ElMessage.error('生成行程失败，请检查 AI 服务是否运行')
  } finally {
    isGenerating.value = false
  }
}

const formatTime = (timestamp: string) => {
  return new Date(timestamp).toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
}
</script>

<style scoped>
/* Markdown 渲染样式 */
.markdown-body :deep(h1),
.markdown-body :deep(h2),
.markdown-body :deep(h3),
.markdown-body :deep(h4) {
  font-weight: bold;
  margin: 0.8em 0 0.4em;
  line-height: 1.4;
}
.markdown-body :deep(h1) { font-size: 1.4em; }
.markdown-body :deep(h2) { font-size: 1.2em; }
.markdown-body :deep(h3) { font-size: 1.05em; }
.markdown-body :deep(p) {
  margin: 0.4em 0;
  line-height: 1.7;
}
.markdown-body :deep(ul),
.markdown-body :deep(ol) {
  padding-left: 1.4em;
  margin: 0.4em 0;
}
.markdown-body :deep(li) {
  margin: 0.2em 0;
  line-height: 1.6;
}
.markdown-body :deep(strong) {
  font-weight: 700;
}
.markdown-body :deep(em) {
  font-style: italic;
  opacity: 0.9;
}
.markdown-body :deep(code) {
  background: rgba(255,255,255,0.15);
  border-radius: 4px;
  padding: 0.1em 0.4em;
  font-size: 0.9em;
  font-family: monospace;
}
.markdown-body :deep(pre) {
  background: rgba(0,0,0,0.3);
  border-radius: 8px;
  padding: 1em;
  overflow-x: auto;
  margin: 0.6em 0;
}
.markdown-body :deep(pre code) {
  background: none;
  padding: 0;
}
.markdown-body :deep(blockquote) {
  border-left: 3px solid rgba(255,255,255,0.4);
  padding-left: 0.8em;
  margin: 0.4em 0;
  opacity: 0.85;
}
.markdown-body :deep(hr) {
  border: none;
  border-top: 1px solid rgba(255,255,255,0.2);
  margin: 0.8em 0;
}
.markdown-body :deep(table) {
  width: 100%;
  border-collapse: collapse;
  margin: 0.8em 0;
  background: rgba(255,255,255,0.05);
  border-radius: 8px;
  overflow: hidden;
}
.markdown-body :deep(table th) {
  background: rgba(59, 130, 246, 0.3);
  color: #93c5fd;
  padding: 0.8em;
  text-align: left;
  font-weight: 600;
  border-bottom: 2px solid rgba(59, 130, 246, 0.5);
}
.markdown-body :deep(table td) {
  padding: 0.8em;
  border-bottom: 1px solid rgba(255,255,255,0.1);
  word-break: break-word;
}
.markdown-body :deep(table tr:last-child td) {
  border-bottom: none;
}
.markdown-body :deep(table tr:hover) {
  background: rgba(255,255,255,0.08);
}
.markdown-body :deep(table tbody tr:nth-child(even)) {
  background: rgba(255,255,255,0.03);
}
</style>
