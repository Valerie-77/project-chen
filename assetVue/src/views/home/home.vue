  <script setup>
  import { useRouter } from 'vue-router';
  import { onMounted, reactive, ref } from 'vue';
  const router = useRouter()
  let user = reactive({ avatar: "", username: "" });
  const dropDownHandle = (e) => {
    switch (e) {
      case 'userInfo':
        break;
      case 'setPwd':
        break;
      case 'loginOut':
        sessionStorage.removeItem("user");
        router.push("/login")
        break;
    }
  }
  const link = (index) => {
    router.push("/" + index)
  }
  onMounted(() => {
    const u = JSON.parse(sessionStorage.getItem("user"))
    user.avatar = u.avatar;
    user.userName = u.username;
  })
</script>
  <template>
    <el-container>
      <el-menu active-text-color="#00f0ff" background-color="#0a0a12" class="el-menu-vertical-demo cyber-menu"
        default-active="home" text-color="#8b8ba0" @select="link">

        <div class="menu-header">
          <div class="cyber-logo">神经网络系统</div>
          <div class="scan-line"></div>
        </div>

        <el-menu-item index="home" class="cyber-menu-item">
          <el-icon class="menu-icon">
            <HomeFilled />
          </el-icon>
          <span class="menu-text">// 控制台</span>
          <div class="hacker-line"></div>
        </el-menu-item>

        <el-sub-menu index="1" class="cyber-sub-menu">
          <template #title>
            <el-icon class="menu-icon">
              <Tools />
            </el-icon>
            <span class="menu-text">// 系统管理</span>
            <div class="hacker-line"></div>
          </template>
          <el-menu-item index="home/user" class="cyber-menu-item">
            <el-icon class="menu-icon">
              <UserFilled />
            </el-icon>
            <span class="menu-text">用户管理</span>
          </el-menu-item>
          <el-menu-item index="home/categories" class="cyber-menu-item">
            <el-icon class="menu-icon">
              <TrendCharts />
            </el-icon>
            <span class="menu-text">分类管理</span>
          </el-menu-item>
          <el-menu-item index="home/parent" class="cyber-menu-item">
            <el-icon class="menu-icon">
              <Flag />
            </el-icon>
            <span class="menu-text">部门管理</span>
          </el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="2" class="cyber-sub-menu">
          <template #title>
            <el-icon class="menu-icon">
              <Tools />
            </el-icon>
            <span class="menu-text">// 资产管理</span>
            <div class="hacker-line"></div>
          </template>
          <el-menu-item index="2-1" class="cyber-menu-item">
            <el-icon class="menu-icon">
              <UserFilled />
            </el-icon>
            <span class="menu-text">资产清单</span>
          </el-menu-item>
        </el-sub-menu>
      </el-menu>

      <el-container>
        <el-header class="cyber-header">
          <div class="header-glitch" data-text="神经接口控制台">神经接口控制台</div>

          <el-dropdown @command="dropDownHandle" class="cyber-dropdown">
            <span class="cyber-dropdown-link">
              <div class="avatar-glitch">
                <el-avatar src="/wallhaven-o1526l5_3840x2160.png" />
              </div>
              <div class="user-info">
                <span class="username-star">★</span>
                <span class="username">{{ user.userName || 'admin' }}</span>
                <span class="username-star">★</span>
              </div>
              <el-icon class="cyber-arrow">
                <arrow-down />
              </el-icon>
              <div class="cyber-underline"></div>
            </span>
            <template #dropdown>
              <el-dropdown-menu class="cyber-dropdown-menu">
                <el-dropdown-item command="userInfo" class="cyber-dropdown-item">
                  <span class="command-prefix">></span> 个人信息
                </el-dropdown-item>
                <el-dropdown-item command="setPwd" class="cyber-dropdown-item">
                  <span class="command-prefix">></span> 安全设置
                </el-dropdown-item>
                <el-dropdown-item command="loginOut" class="cyber-dropdown-item logout">
                  <span class="command-prefix">#</span> 退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </el-header>

        <el-main class="cyber-main">
          <div class="grid-lines"></div>
          <div class="scan-effect"></div>
          <RouterView />
        </el-main>
      </el-container>
    </el-container>
  </template>

<style scoped>
.el-container {
  width: 100vw;
  height: 100vh;
  background:
    radial-gradient(circle at 20% 50%, #ff00ff0a 0%, transparent 50%),
    radial-gradient(circle at 80% 20%, #00f0ff0a 0%, transparent 50%),
    linear-gradient(135deg, #0a0a12 0%, #1a1a2e 50%, #16213e 100%);
  font-family: 'Microsoft YaHei', 'Segoe UI', sans-serif;
  position: relative;
  overflow: hidden;
}

/* 网格背景 */
.el-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background:
    linear-gradient(90deg, #00f0ff05 1px, transparent 1px),
    linear-gradient(180deg, #00f0ff05 1px, transparent 1px);
  background-size: 20px 20px;
  pointer-events: none;
  z-index: 1;
}

/* 头部样式 */
.cyber-header {
  background: linear-gradient(90deg,
      #0a0a12 0%,
      #ff00ff20 30%,
      #00f0ff20 70%,
      #0a0a12 100%);
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow:
    0 0 30px #ff00ff40,
    inset 0 1px 0 #00f0ff40;
  border-bottom: 1px solid #00f0ff30;
  position: relative;
  z-index: 2;
  padding: 0 20px;
}

.header-glitch {
  color: #00f0ff;
  font-size: 18px;
  font-weight: bold;
  text-shadow:
    0 0 10px #00f0ff,
    0 0 20px #00f0ff,
    2px 2px 0 #ff00ff;
  position: relative;
  animation: text-flicker 3s infinite;
  font-family: 'Microsoft YaHei', sans-serif;
}

.header-glitch::before,
.header-glitch::after {
  content: attr(data-text);
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  font-family: 'Microsoft YaHei', sans-serif;
}

.header-glitch::before {
  animation: glitch-1 5s infinite linear alternate-reverse;
  color: #ff00ff;
  z-index: -1;
}

.header-glitch::after {
  animation: glitch-2 3s infinite linear alternate-reverse;
  color: #00f0ff;
  z-index: -2;
}

/* 主内容区 */
.cyber-main {
  background:
    radial-gradient(circle at 10% 20%, #ff00ff08 0%, transparent 50%),
    radial-gradient(circle at 90% 80%, #00f0ff08 0%, transparent 50%),
    linear-gradient(135deg, #0a0a12 0%, #1a1a2e 100%);
  position: relative;
  overflow: auto;
  padding: 20px;
}

.grid-lines {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background:
    linear-gradient(90deg, #00f0ff03 1px, transparent 1px),
    linear-gradient(180deg, #00f0ff03 1px, transparent 1px);
  background-size: 50px 50px;
  pointer-events: none;
  z-index: 1;
}

.scan-effect {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 2px;
  background: linear-gradient(90deg, transparent, #00f0ff, transparent);
  box-shadow: 0 0 20px #00f0ff;
  animation: scan 4s linear infinite;
  z-index: 2;
}

/* 左侧菜单样式 */
:deep(.cyber-menu) {
  border-right: 1px solid #00f0ff30;
  background: linear-gradient(180deg, #0a0a12 0%, #1a1a2e 100%);
  box-shadow:
    4px 0 20px #ff00ff20,
    inset -1px 0 0 #00f0ff20;
  width: 260px !important;
  z-index: 2;
}

.menu-header {
  padding: 20px 16px;
  border-bottom: 1px solid #00f0ff30;
  position: relative;
  overflow: hidden;
}

.cyber-logo {
  color: #00f0ff;
  font-size: 18px;
  font-weight: bold;
  text-shadow: 0 0 10px #00f0ff;
  letter-spacing: 1px;
  animation: text-flicker 2s infinite;
  font-family: 'Microsoft YaHei', sans-serif;
}

.scan-line {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 1px;
  background: linear-gradient(90deg, transparent, #ff00ff, transparent);
  box-shadow: 0 0 10px #ff00ff;
  animation: scan-horizontal 3s linear infinite;
}

:deep(.cyber-menu-item) {
  background: transparent !important;
  border-left: 3px solid transparent;
  transition: all 0.3s ease;
  margin: 8px 12px;
  border-radius: 0;
  height: 50px;
  position: relative;
  overflow: hidden;
  font-family: 'Microsoft YaHei', sans-serif;
}

:deep(.cyber-menu-item:hover) {
  background: linear-gradient(90deg, #ff00ff10, transparent) !important;
  border-left-color: #ff00ff;
  box-shadow:
    inset 0 0 20px #ff00ff20,
    0 0 10px #ff00ff40;
}

:deep(.cyber-menu-item.is-active) {
  background: linear-gradient(90deg, #00f0ff10, transparent) !important;
  border-left-color: #00f0ff;
  box-shadow:
    inset 0 0 30px #00f0ff20,
    0 0 15px #00f0ff40;
}

.hacker-line {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 1px;
  background: linear-gradient(90deg, transparent, #00f0ff, transparent);
  opacity: 0;
  transition: opacity 0.3s ease;
}

:deep(.cyber-menu-item:hover .hacker-line),
:deep(.cyber-menu-item.is-active .hacker-line) {
  opacity: 1;
}

:deep(.cyber-sub-menu .el-sub-menu__title) {
  background: transparent !important;
  border-left: 3px solid transparent;
  transition: all 0.3s ease;
  margin: 8px 12px;
  border-radius: 0;
  height: 50px;
  font-family: 'Microsoft YaHei', sans-serif;
}

:deep(.cyber-sub-menu .el-sub-menu__title:hover) {
  background: linear-gradient(90deg, #ff00ff10, transparent) !important;
  border-left-color: #ff00ff;
  box-shadow:
    inset 0 0 20px #ff00ff20,
    0 0 10px #ff00ff40;
}

.menu-icon {
  color: #8b8ba0 !important;
  transition: all 0.3s ease;
}

.menu-text {
  font-family: 'Microsoft YaHei', 'Segoe UI', sans-serif;
  font-size: 14px;
  letter-spacing: 0.5px;
  transition: all 0.3s ease;
}

:deep(.cyber-menu-item:hover .menu-icon),
:deep(.cyber-menu-item.is-active .menu-icon),
:deep(.cyber-sub-menu .el-sub-menu__title:hover .menu-icon) {
  color: #00f0ff !important;
  text-shadow: 0 0 10px #00f0ff;
}

:deep(.cyber-menu-item:hover .menu-text),
:deep(.cyber-menu-item.is-active .menu-text),
:deep(.cyber-sub-menu .el-sub-menu__title:hover .menu-text) {
  color: #00f0ff !important;
  text-shadow: 0 0 10px #00f0ff;
}

/* 下拉菜单样式 */
.cyber-dropdown-link {
  display: flex;
  align-items: center;
  color: #00f0ff;
  font-family: 'Microsoft YaHei', sans-serif;
  font-size: 14px;
  letter-spacing: 0.5px;
  padding: 10px 16px;
  background: linear-gradient(90deg, #ff00ff10, #00f0ff10);
  border: 1px solid #00f0ff30;
  position: relative;
  transition: all 0.3s ease;
}

.cyber-dropdown-link:hover {
  background: linear-gradient(90deg, #ff00ff20, #00f0ff20);
  box-shadow:
    0 0 20px #00f0ff40,
    inset 0 0 20px #ff00ff20;
  transform: translateY(-1px);
}

.avatar-glitch {
  position: relative;
  margin-right: 10px;
}

.avatar-glitch::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: #ff00ff;
  opacity: 0;
  mix-blend-mode: overlay;
  animation: glitch-avatar 4s infinite;
}

.cyber-underline {
  position: absolute;
  bottom: 0;
  left: 10%;
  right: 10%;
  height: 1px;
  background: linear-gradient(90deg, transparent, #ff00ff, #00f0ff, transparent);
  box-shadow: 0 0 10px #00f0ff;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.cyber-dropdown-link:hover .cyber-underline {
  opacity: 1;
}

:deep(.cyber-dropdown-menu) {
  background: #0a0a12 !important;
  border: 1px solid #00f0ff30 !important;
  box-shadow:
    0 0 30px #ff00ff40,
    inset 0 0 20px #00f0ff10 !important;
}

:deep(.cyber-dropdown-item) {
  color: #8b8ba0 !important;
  font-family: 'Microsoft YaHei', sans-serif !important;
  font-size: 13px !important;
  letter-spacing: 0.5px;
  background: transparent !important;
  transition: all 0.3s ease;
  padding: 12px 16px !important;
}

:deep(.cyber-dropdown-item:hover) {
  color: #00f0ff !important;
  background: linear-gradient(90deg, #ff00ff20, transparent) !important;
  text-shadow: 0 0 10px #00f0ff;
}

:deep(.cyber-dropdown-item.logout:hover) {
  color: #ff0066 !important;
  background: linear-gradient(90deg, #ff006620, transparent) !important;
  text-shadow: 0 0 10px #ff0066;
}

.command-prefix {
  color: #00f0ff;
  margin-right: 8px;
  font-family: 'Courier New', monospace;
}

:deep(.cyber-dropdown-item.logout .command-prefix) {
  color: #ff0066;
}

/* 动画定义 */
@keyframes scan {
  0% {
    top: 0;
  }

  100% {
    top: 100%;
  }
}

@keyframes scan-horizontal {
  0% {
    transform: translateX(-100%);
  }

  100% {
    transform: translateX(100%);
  }
}

@keyframes text-flicker {

  0%,
  100% {
    opacity: 1;
  }

  50% {
    opacity: 0.8;
  }

  51% {
    opacity: 0.2;
  }

  52% {
    opacity: 0.8;
  }
}

@keyframes glitch-1 {
  0% {
    transform: translate(0);
  }

  20% {
    transform: translate(-2px, 2px);
  }

  40% {
    transform: translate(-2px, -2px);
  }

  60% {
    transform: translate(2px, 2px);
  }

  80% {
    transform: translate(2px, -2px);
  }

  100% {
    transform: translate(0);
  }
}

@keyframes glitch-2 {
  0% {
    transform: translate(0);
  }

  10% {
    transform: translate(2px, 0);
  }

  30% {
    transform: translate(-1px, 1px);
  }

  50% {
    transform: translate(0, -1px);
  }

  70% {
    transform: translate(-2px, 0);
  }

  90% {
    transform: translate(1px, 1px);
  }

  100% {
    transform: translate(0);
  }
}

@keyframes glitch-avatar {

  0%,
  100% {
    opacity: 0;
  }

  50% {
    opacity: 0.3;
  }
}

/* 用户信息容器 */
.user-info {
  display: flex;
  align-items: center;
  justify-content: center;
  /* 添加水平居中 */
  margin: 0 10px;
  position: relative;
}

/* 星星样式 */
.username-star {
  color: #ffd700;
  text-shadow: 0 0 10px #ffd700, 0 0 20px #ff9900;
  font-size: 16px;
  animation: star-glitch 2s infinite;
  flex-shrink: 0;
  /* 防止星星被压缩 */
}

/* 左星星 */
.username-star:first-child {
  margin-right: 8px;
}

/* 右星星 */
.username-star:last-child {
  margin-left: 8px;
}

/* 用户名样式 */
.username {
  color: #00f0ff;
  font-weight: bold;
  text-shadow: 0 0 10px #00f0ff;
  flex-shrink: 0;
  /* 防止用户名被压缩 */
  min-width: fit-content;
  /* 根据内容自适应宽度 */
}

/* 星星闪烁动画 */
@keyframes star-glitch {

  0%,
  100% {
    opacity: 1;
    transform: scale(1);
  }

  50% {
    opacity: 0.7;
    transform: scale(1.1);
  }

  75% {
    opacity: 0.9;
    transform: scale(0.95);
  }

}

.user-info:hover {
  cursor: url('data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMzIiIGhlaWdodD0iMzIiIHZpZXdCb3g9IjAgMCAzMiAzMiIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHBhdGggZD0iTTE2IDZMMTAgMTZMMTYgMjZMMjIgMTZMMTYgNloiIGZpbGw9IiMwMEYwRkYiLz4KPHBhdGggZD0iTTE2IDZMMTAgMTZMMTYgMjZMMjIgMTZMMTYgNloiIGZpbGw9IiMwMEYwRkYiIGZpbGwtb3BhY2l0eT0iMC4zIi8+CjxyZWN0IHg9IjE0IiB5PSIxNCIgd2lkdGg9IjQiIGhlaWdodD0iNCIgZmlsbD0iI0ZGRkZGRiIvPgo8L3N2Zz4K'), auto;
}

.username:hover {
  color: #ff00ff !important;
  text-shadow:
    0 0 15px #ff00ff,
    0 0 30px #ff00ff,
    0 0 45px #ff00ff !important;
  transform: scale(1.05);
  transition: all 0.3s ease;
}
</style>