<template>
  <a-layout-sider :style="{ width: '236px' }">
    <div class="h-screen flex flex-col">
      <div class="flex justify-center text-5xl font-light text-blue py-12">
        Raphael
      </div>
      <a-menu
          :selected-keys="[$route.name]"
          :style="{ padding: '0 12px 0 12px' }"
          class="h-full"
      >
        <router-link to="/">
          <a-menu-item key="home">首页</a-menu-item>
        </router-link>
        <router-link to="/">
          <a-menu-item key="square">知识广场</a-menu-item>
        </router-link>
        <router-link to="/">
          <a-menu-item key="functions">特色功能</a-menu-item>
        </router-link>
        <router-link to="/">
          <a-menu-item key="about">关于我们</a-menu-item>
        </router-link>
        <template v-if="isLogged">
          <a-divider />
          <router-link to="/dashboard">
            <a-menu-item key="dashboard">工作台</a-menu-item>
          </router-link>
        </template>
      </a-menu>
      <template v-if="!isLogged">
        <div class="flex w-full gap-2 px-4 py-12">
          <router-link class="w-1/2" to="/register">
            <a-button long>注册</a-button>
          </router-link>
          <router-link class="w-1/2" to="/login">
            <a-button long type="primary">
              登录
            </a-button>
          </router-link>
        </div>
      </template>
    </div>
  </a-layout-sider>
</template>

<script lang="ts" setup>
import { getUserStore } from "../../store";
import { ref, watch } from "vue";
import { useRoute } from "vue-router";

const isLogged = ref(false)

if (getUserStore().getUser) {
  isLogged.value = true
}
</script>