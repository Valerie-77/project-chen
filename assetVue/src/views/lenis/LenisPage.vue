<template>
    <div ref="container" class="lenis-container"></div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import htmlContent from './index.html?raw'

const container = ref(null)

onMounted(() => {

    container.value.innerHTML = htmlContent

    const cssLink = document.createElement('link')
    cssLink.rel = 'stylesheet'
    cssLink.href = '/src/views/lenis/style.css'
    document.head.appendChild(cssLink)

    const loadScript = (src) => {
        return new Promise((resolve) => {
            const script = document.createElement('script')
            script.src = src
            script.onload = resolve
            document.body.appendChild(script)
        })
    }

    loadScript('/src/views/lenis/lenis.min.js').then(() => {
        loadScript('/src/views/lenis/script.js')
    })
})
</script>

<style scoped>
.lenis-container {
    width: 100%;
    min-height: 100vh;
}
</style>