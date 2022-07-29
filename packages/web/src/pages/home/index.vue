<template>
  <default-layout>
    <template v-slot:nav>
      <nav-bar />
    </template>
    <template v-slot:content>
      <div class="p-4">
        <editor :content="Introduce" read-only />
      </div>
    </template>
  </default-layout>
</template>

<script lang="ts" setup>
const Introduce = `
<h1><span>Raphael</span> 是什么</h1>
<p>它是一个实用的个人(团队)知识库系统</p>
<p>值得一提的是: 这里的介绍是使用自研编辑器渲染的.</p>
<p></p>
<p>前端技术栈: React React-Router Arco-Design Tailwindcss Vite</p>
<p>后端技术栈: SpringBoot SaToken Exposed</p>
<h2>(首页尚未完善)</h2>
`;
import DefaultLayout from "../../layout/default-layout.vue";
import NavBar from "../../components/layouts/nav-bar.vue";
import Editor from "../../components/editor/editor.vue";</script>

<style lang="less" scoped>

</style>