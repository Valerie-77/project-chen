import './styles/cyber-common.scss'
import '@/styles/cyber-common.scss'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import router from './router'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

// ====================== 加在这里！5 秒搞定 ======================
import { gsap } from 'gsap'
import { ScrollTrigger } from 'gsap/ScrollTrigger'
import Lenis from '@studio-freight/lenis'

// 必须这三行！让你原来的 script.js 里能直接用 window.gsap / ScrollTrigger / new Lenis()
gsap.registerPlugin(ScrollTrigger)
  ; (window as any).gsap = gsap
  ; (window as any).ScrollTrigger = ScrollTrigger
  ; (window as any).Lenis = Lenis
// =============================================================

const app = createApp(App)

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(createPinia())
app.use(router)
app.use(ElementPlus)

app.mount('#app')

// 你的全局去 outline 样式
const globalStyles = document.createElement('style')
globalStyles.textContent = `
  *, *::before, *::after, button, input, textarea {
    outline: none !important;
    box-shadow: none !important;
  }
`
document.head.appendChild(globalStyles)