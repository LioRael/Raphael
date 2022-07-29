<template>
  <a-menu v-if="props.editor" class="editor-menu">
    <a-menu-item
        key="0"
        @click="props.editor.chain().focus().setParagraph().run()"
        :class="'editor-menu-content' +(props.editor.isActive('paragraph') ? ' editor-menu-action-active' : '')"
    >
      <span class="editor-menu-paragraph">正文</span>
      <span class="editor-menu-command">Alt Ctrl 0</span>
    </a-menu-item>
    <a-menu-item
        key="1"
        @click="props.editor.chain().focus().toggleHeading({ level: 1 }).run()"
        :class="'editor-menu-content' +(props.editor.isActive('heading', { level: 1 }) ? ' editor-menu-action-active' : '')"
    >
      <span class="editor-menu-heading1">标题1</span>
      <span class="editor-menu-command">Alt Ctrl 1</span>
    </a-menu-item>
    <a-menu-item
        key="2"
        @click="props.editor.chain().focus().toggleHeading({ level: 2 }).run()"
        :class="'editor-menu-content' +(props.editor.isActive('heading', { level: 2 }) ? ' editor-menu-action-active' : '')"
    >
    <span class="editor-menu-heading2">标题2</span>
    <span class="editor-menu-command">Alt Ctrl 2</span>
    </a-menu-item>
    <a-menu-item
        key="3"
        @click="props.editor.chain().focus().toggleHeading({ level: 3 }).run()"
        :class="'editor-menu-content' +(props.editor.isActive('heading', { level: 3 }) ? ' editor-menu-action-active' : '')"
    >
    <span class="editor-menu-heading3">标题3</span>
    <span class="editor-menu-command">Alt Ctrl 3</span>
    </a-menu-item>
    <a-menu-item
        key="4"
        @click="props.editor.chain().focus().toggleHeading({ level: 4 }).run()"
        :class="'editor-menu-content' +(props.editor.isActive('heading', { level: 4 }) ? ' editor-menu-action-active' : '')"
    >
    <span class="editor-menu-heading4">标题4</span>
    <span class="editor-menu-command">Alt Ctrl 4</span>
    </a-menu-item>
  </a-menu>
</template>

<script lang="ts" setup>
const props = defineProps({
  editor: {
    type: Object,
    default: null
  }
})
</script>

<style lang="less" scoped>

</style>