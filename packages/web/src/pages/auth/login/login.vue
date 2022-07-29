<template>
  <auth-container>
    <a-form :model="form" layout="vertical" @submit="handleSubmit">
      <a-form-item>
        <div
            :style="{ color: '#262626', fontSize: '32px' }"
            class="flex w-full justify-center"
        >
          登录
        </div>
      </a-form-item>
      <a-form-item
          :rules="[
            { required: true, message: '请输入你的邮箱' },
            { type: 'email', message: '请输入合法的邮箱' }]"
          field="email"
          label="邮箱"
      >
        <a-input v-model="form.email" placeholder="请输入你的邮箱..." />
      </a-form-item>
      <a-form-item
          :rules="[{ required: true, message: '请输入你的密码' }]"
          field="password"
          label="密码"
      >
        <a-input-password v-model="form.password" placeholder="请输入你的密码..." />
      </a-form-item>
      <a-form-item
          :rules="[{ type: 'boolean', message: '你必须同意服务条款', true: true }]"
          field="accept"
      >
        <a-checkbox v-model="form.accept">我已经阅读了服务条款</a-checkbox>
      </a-form-item>
      <a-form-item>
        <a-button htmlType="submit" long type="primary">
          登录
        </a-button>
      </a-form-item>
      <div>
        <router-link to="/register">
          <a-button long>
            前往注册
          </a-button>
        </router-link>
      </div>
    </a-form>
  </auth-container>
</template>

<script lang="ts" setup>
import { useRouter } from "vue-router";
import { getUserStore } from "../../../store";
import { reactive } from "vue";
import AuthContainer from "../auth-container.vue";
import {  inject } from 'vue'

const router = useRouter();
const reload = inject('reload')

const form = reactive({
  email: '',
  password: '',
  accept: false,
});

const handleSubmit = () => {
  const email = form.email
  const password = form.password
  const userStore = getUserStore()
  userStore.login(email, password, router, reload)
}
</script>