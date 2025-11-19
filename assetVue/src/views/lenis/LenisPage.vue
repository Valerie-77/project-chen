<template>
    <div ref="container" class="lenis-container">
        <div v-if="loading" class="loading">加载中...</div>
    </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import htmlContent from './index.html?raw'

const container = ref(null)
const loading = ref(true)

onMounted(async () => {
    try {
        // 先插入 HTML 内容
        container.value.innerHTML = htmlContent

        // 等待 DOM 更新
        await nextTick()

        // 添加 CSS
        const cssLink = document.createElement('link')
        cssLink.rel = 'stylesheet'
        cssLink.href = '/lenis/style.css'
        document.head.appendChild(cssLink)

        // 按正确顺序加载脚本
        await loadScript('/lenis/gsap.min.js')
        await loadScript('/lenis/ScrollTrigger.min.js')
        await loadScript('/lenis/lenis.min.js')

        // 等待一小段时间确保 Lenis 完全加载
        await new Promise(resolve => setTimeout(resolve, 100))

        // 最后加载你的自定义脚本
        await loadScript('/lenis/script.js')

        loading.value = false
        console.log('所有资源加载完成')
    } catch (error) {
        console.error('加载失败:', error)
        loading.value = false
    }
})

function loadScript(src) {
    return new Promise((resolve, reject) => {
        const script = document.createElement('script')
        script.src = src
        script.onload = () => {
            console.log(`成功加载: ${src}`)
            resolve()
        }
        script.onerror = (err) => {
            console.error(`加载失败: ${src}`, err)
            reject(new Error(`Failed to load: ${src}`))
        }
        document.body.appendChild(script)
    })
}

onUnmounted(() => {
    if (container.value) {
        container.value.innerHTML = ''
    }
})
</script>

<style scoped>
.lenis-container {
    width: 100%;
    min-height: 100vh;
    position: relative;
}

.loading {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    font-size: 18px;
    color: #666;
}
</style>