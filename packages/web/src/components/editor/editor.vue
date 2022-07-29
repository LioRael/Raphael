<template>
  <div :class="readOnly ? 'layout' : 'layout-editing'">
    <editor-menu :editor="editor" v-if="!props.readOnly" />
    <textarea v-if="!props.readOnly" placeholder="请输入标题" class="title-textarea" v-model="title" />
    <div v-else class="title">{{ props.title }}</div>
    <editor-content class="editor-wrapper" :editor="editor" />
    <div class="pb-24"/>
  </div>
</template>

<script lang="ts" setup>
import { EditorContent, useEditor } from '@tiptap/vue-3'
import StarterKit from "@tiptap/starter-kit";
import EditorMenu from "./menu/editor-menu.vue";
import { Placeholder } from "@tiptap/extension-placeholder";
import UidExtension from "./extensions/uid";
import { ref } from "vue";

const props = defineProps({
  title: {
    type: String,
    default: ""
  },
  content: {
    type: String,
    default: ""
  },
  readOnly: {
    type: Boolean,
    default: false
  },
  padding: {
    type: Boolean,
    default: false
  }
})

const title = ref(props.title)

const editor = useEditor({
  extensions: [
    StarterKit,
    Placeholder.configure({
      placeholder: ({ node }) => {
        return '输入 / 唤醒菜单'
      },
    }),
    UidExtension,
  ],
  content: props.content,
  editable: !props.readOnly,
});

const getHTML = () => {
  return editor.value!!.getHTML()
}

const getTitle = () => {
  return title.value
}

defineExpose({
  getHTML,
  getTitle,
})
</script>

<style lang="less">
.title {
  font-family: Chinese Quote, Segoe UI, Roboto, PingFang SC, Hiragino Sans GB, Microsoft YaHei, Helvetica Neue, Helvetica, Arial, sans-serif, Apple Color Emoji;
  font-size: 36px;
  line-height: 1.389;
  font-weight: 700;
  color: #262626;
  display: inline;
  margin-right: 20px;
  -webkit-font-variant-ligatures: none;
  font-variant-ligatures: none;
  word-break: break-word;
  padding-left: 16px;
  padding-right: 16px;
  margin-top: 19px;
  margin-bottom: 19px;
}

.title-textarea {
  resize: none;
  max-height: 9.0072e+15px;
  height: 50px !important;
  line-height: 1.389;
  font-variant-ligatures: none;
  -webkit-font-variant-ligatures: none;
  border: none;
  outline: none;
  box-shadow: none;
  padding-left: 16px;
  padding-right: 16px;
  max-width: 100%;
  vertical-align: bottom;
  transition: all .3s, height 0s;
  color: #262626;
  font-weight: 700;
  font-size: 36px;
  font-variant: tabular-nums;
  list-style: none;
  font-feature-settings: "tnum";
  position: relative;
  display: inline-block;
  width: 100%;
  min-width: 0;
  background-color: #fff;
  background-image: none;
  overflow: auto;
  margin-top: 19px;
  margin-bottom: 19px;
}

.layout {
  display: flex;
  flex-direction: column;
  width: 100%;
  padding-left: 256px;
  padding-right: 256px;
}

.layout-editing {
  display: flex;
  flex-direction: column;
  width: 100%;
  padding-left: 512px;
  padding-right: 512px;
}

.editor-wrapper {
  padding-left: 16px;
  padding-right: 16px;
}

.ProseMirror {
  font-family: Roboto, RobotoNum, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "Helvetica Neue", Helvetica, Arial, sans-serif, "Segoe UI";
  color: #262626;
  letter-spacing: initial;
  line-height: 1.74;
  font-size: 15px;
  position: relative;
  z-index: 1;
  outline: none;
  white-space: break-spaces;
  word-break: break-word;
  word-wrap: break-word;
}

.ProseMirror > p {
  margin-top: 0 !important;
  margin-bottom: 9px !important;
}

.ProseMirror ul {
  margin-top: 0 !important;
  margin-bottom: 9px !important;
}

.ProseMirror h1 {
  font-size: 28px;
  line-height: 38px;
  font-weight: bold;
  margin-top: 0 !important;
  margin-bottom: 19px !important;
}

.ProseMirror h2 {
  font-size: 24px;
  line-height: 34px;
  font-weight: bold;
  margin-top: 0 !important;
  margin-bottom: 16px;
}

.ProseMirror h3 {
  font-size: 20px;
  line-height: 30px;
  font-weight: bold;
  margin-top: 0 !important;
  margin-bottom: 14px;
}

.ProseMirror h4 {
  font-size: 16px;
  line-height: 26px;
  font-weight: bold;
  margin-top: 0 !important;
  margin-bottom: 12px;
}

.ProseMirror strong {
  font-weight: bold;
  color: #262626;
}

.ProseMirror blockquote {
  background: #f9f9f9;
  border-left: 3px solid #ccc;
  border-radius: 4px;
  margin: 1em 0 1em 0;
  padding: 0.5em 8px;
}

.ProseMirror ul {
  list-style: disc;
  margin-left: 1.5em;
  padding-left: 0.5em;
}

.ProseMirror li {
  margin-left: 0.2em;
}

.ProseMirror ol {
  list-style: decimal;
  margin-left: 1.5em;
  padding-left: 0.5em;
}

.ProseMirror .is-empty::before {
  content: attr(data-placeholder);
  float: left;
  color: #ced4da;
  pointer-events: none;
  height: 0;
}
</style>