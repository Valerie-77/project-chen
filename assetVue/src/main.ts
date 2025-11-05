import './styles/cyber-common.scss'
import '@/styles/cyber-common.scss'
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import router from './router'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const globalStyles = document.createElement('style')
globalStyles.textContent = `
  *:focus {
    outline: none !important;
    box-shadow: none !important;
  }
  button:focus {
    outline: none !important;
    box-shadow: none !important;
  }
`
document.head.appendChild(globalStyles)


const app = createApp(App)

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

app.use(ElementPlus)
app.use(createPinia())
app.use(router)

app.mount('#app')
