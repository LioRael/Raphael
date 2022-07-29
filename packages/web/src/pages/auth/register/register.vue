<template>
  <auth-container>
    <a-form :model="form" layout="vertical" @Submit="handleSubmit">
      <a-form-item>
        <div
            :style="{ color: '#262626', fontSize: '32px' }"
            class="flex w-full justify-center"
        >
          注册
        </div>
      </a-form-item>
      <a-form-item
          :rules="[
            { required: true, message: '请输入你的邮箱' },
            { type: 'email', message: '请输入合法的邮箱' },]"
          field="email"
          label="邮箱"
      >
        <a-input v-model="form.email" placeholder="请输入你的邮箱..." />
      </a-form-item>
      <a-form-item
          :rules="[
            { type: 'string',minLength: 3, maxLength: 10, message: '用户名长度在3~10之间'},
            { required: true, message: '请输入你的用户名' }]"
          field="username"
          label="用户名"
      >
        <a-input v-model="form.username" placeholder="请输入你的用户名..." />
      </a-form-item>
      <a-form-item
          :rules="[
            { type: 'string', minLength: 8, message: '密码长度不能小于8位'},
            { required: true, message: '请输入你的密码' }]"
          field="password"
          label="密码"
      >
        <a-input-password v-model="form.password" placeholder="请输入你的密码..." />
      </a-form-item>
      <a-form-item>
        <a-checkbox v-model="form.accept">我已经阅读了服务条款</a-checkbox>
      </a-form-item>
      <a-form-item>
        <a-button htmlType="submit" long type="primary">
          注册
        </a-button>
      </a-form-item>
      <div>
        <router-link to="/login">
          <a-button long>
            前往登录
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
  username: '',
  password: '',
  accept: false
});

const handleSubmit = () => {
  const email = form.email
  const username = form.username
  const password = form.password
  const userStore = getUserStore()
  userStore.register(email, username, password, router, reload)
}
</script>