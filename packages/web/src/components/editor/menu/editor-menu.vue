<template>
  <div v-if="props.editor" class="editor-menu-bar">
    <editor-menu-item isIcon description="插入...">
      <icon-plus />
    </editor-menu-item>
    <editor-menu-item description="标题">
      <a-dropdown :popup-max-height="false" trigger="click" position="bl">
        <a-button type="text">
          标题
          <icon-down />
        </a-button>
        <template #content>
          <editor-menu-item-title :editor="props.editor" />
        </template>
      </a-dropdown>
    </editor-menu-item>
    <editor-menu-item
        isIcon
        description="粗体"
        command="Ctrl B"
        :class="props.editor.isActive('bold') ?'editor-action-active' : ''"
    >
      <icon-bold @click="props.editor.chain().focus().toggleBold().run()" />
    </editor-menu-item>
    <editor-menu-item
        isIcon
        description="斜体"
        command="Ctrl I"
        :class="props.editor.isActive('italic') ? 'editor-action-active': ''"
    >
      <icon-italic @click="props.editor.chain().focus().toggleItalic().run()" />
    </editor-menu-item>
    <editor-menu-item
        isIcon
        description="添加引用"
        command="Ctrl U"
        :class="props.editor.isActive('blockquote') ? 'editor-action-active': ''"
    >
      <icon-quote
          @click="props.editor.chain().focus().toggleBlockquote().run()"
      />
    </editor-menu-item>
    <editor-menu-item
        isIcon
        description="无序列表"
        command="Ctrl Shift 8"
        :class="props.editor.isActive('bulletList') ? 'editor-action-active': ''"
    >
      <icon-list
          @click="props.editor.chain().focus().toggleBulletList().run()"
      />
    </editor-menu-item>
    <editor-menu-item
        isIcon
        description="有序列表"
        command="Ctrl Shift 7"
        :class="props.editor.isActive('orderList') ? 'editor-action-active': ''"
    >
      <icon-ordered-list
          @click="props.editor.chain().focus().toggleOrderedList().run()"
      />
    </editor-menu-item>
    <editor-menu-item isIcon description="增加缩进" command="Tab">
      <icon-menu-unfold
          @click="props.editor.chain().focus().sinkListItem('listeditor-menu-item').run()"
      />
    </editor-menu-item>
    <editor-menu-item isIcon description="减少缩进" command="Shift Tab">
      <icon-menu-fold
          @click="props.editor.chain().focus().liftListItem('listeditor-menu-item').run()"
      />
    </editor-menu-item>
  </div>
</template>

<script lang="ts" setup>
import EditorMenuItem from "./editor-menu-item.vue";
import EditorMenuItemTitle from "./editor-menu-item-title.vue";

const props = defineProps({
  editor: {
    type: Object,
    default: null
  }
})
</script>

<style lang="less">
.editor-menu-bar {
  padding: 8px;
  display: flex;
  align-items: center;
  box-shadow: 0 4px 6px  rgba(0,0,0,0.08);
}

.editor-menu-action {
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: #585A5A;
  margin-right: 0.5rem;
  font-size: 1.25rem;
  padding: 0.2rem;
  transition: color 0.4s ease-in-out 0s, background-color 0.4s ease-in-out 0s;
  border-radius: 4px;
  height: 1.35em;
}

.editor-menu-action button {
  color: #585A5A !important;
}

.editor-menu-action button:hover {
  background-color: transparent !important;
}

.editor-menu-action-icon {
  width: 1.35em;
}

.editor-menu-action:hover {
  background: #f5f5f5;
}

.editor-menu-action-active {
  background: #f5f5f5;
  color: rgb(var(--primary-6));
}

.editor-action-active {
  background: #E8E9E8 !important;
}

.editor-menu-action-tooltip {
  font-size: 1.2rem;
}

.editor-menu-action .arco-btn-size-default {
  padding: 0 0.3rem !important;
  border-radius: 4px !important;
}

.editor-menu-action .arco-btn-size-default:hover {
  background: none !important;
}

.editor-menu-paragraph {
  font-size: 16px;
  font-weight: bold;
}

.editor-menu-heading1 {
  font-size: 28px;
  font-weight: bold;
  line-height: 1.6;
}

.editor-menu-heading2 {
  font-size: 24px;
  font-weight: bold;
  line-height: 1.6;
}

.editor-menu-heading3 {
  font-size: 20px;
  font-weight: bold;
  line-height: 1.6;
}

.editor-menu-heading4 {
  font-size: 16px;
  font-weight: bold;
  line-height: 1.6;
}

.editor-menu-content {
  height: fit-content;
  position: relative;
  padding: 4px 15px 4px 15px;
  line-height: 30px;
  font-size: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  transition: all .3s;
  font-weight: 400;
}

.editor-menu-content:hover {
  color: rgb(var(--primary-6));
}

.editor-menu-command {
  color: #BEC0BF;
  margin-left: 65px;
}

.editor-menu {
  border-radius: 5px;
  padding: 4px 0;
  min-width: 128px;
  height: fit-content !important;
  border: none !important;
  box-shadow: 0 3px 6px -4px rgba(0, 0, 0, .12), 0 6px 16px 0 rgba(0, 0, 0, .08), 0 9px 28px 8px rgba(0, 0, 0, .05) !important;
}
</style>