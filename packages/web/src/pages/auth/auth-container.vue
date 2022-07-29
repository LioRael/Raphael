<template>
  <default-layout>
    <template v-slot:nav>
      <nav-bar />
    </template>
    <template v-slot:content>
      <div class="auth-container">
        <div
            :class="'card flex w-full px-6 md:px-0 md:w-3/4 shadow-xl ' + (!isLogin && 'flex-row-reverse')">
          <div v-if="isLogin"
               class="md:block align-middle w-3/4 h-full overflow-hidden hidden"
          >
            <a-image :preview="false" :src="image1" width="100%" />
          </div>
          <div v-else
               class="md:block align-middle w-3/4 h-full overflow-hidden hidden"
          >
            <a-image :preview="false" :src="image2" width="100%" />
          </div>
          <div class="flex flex-col p-4 w-full md:w-1/4 h-full">
            <div class="flex h-full items-center">
              <slot />
            </div>
            <router-link to="/">
              <a-button long type="outline">
                返回首页
              </a-button>
            </router-link>
          </div>
        </div>
      </div>
    </template>
  </default-layout>
</template>

<script lang="ts" setup>
import image1 from "../../assets/image/login.svg";
import image2 from "../../assets/image/register.svg";
import DefaultLayout from "../../layout/default-layout.vue";
import { useRoute, useRouter } from "vue-router";
import NavBar from "../../components/layouts/nav-bar.vue";
import { getUserStore } from "../../store";

const route = useRoute()
const isLogin = route.name === "login"
const router = useRouter();
const userStore = getUserStore()

if (userStore.getUser) {
  router.push("/")
}

</script>
<style lang="less" scoped>
.auth-container {
  display: flex;
  align-items: center;
  justify-items: center;
  justify-content: center;
  height: 100vh;
  width: 100%;
  background: #fafafa;
}

.card {
  display: flex;
  align-items: center;
  border-radius: 12px;
  background: #fff;
  height: 75%;
}
</style>