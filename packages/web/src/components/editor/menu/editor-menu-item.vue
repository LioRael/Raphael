<template>
  <a-tooltip class="editor-menu-action-tooltip">
    <template #content>
      <p>{{ description }}</p>
      <p>{{ command }}</p>
    </template>
    <div
        :class="'editor-menu-action' + (props.isIcon ? ' editor-menu-action-icon' : ' ') + props.class">
      <slot></slot>
    </div>
  </a-tooltip>
</template>

<script lang="ts" setup>
const props = defineProps({
  isIcon: {
    type: Boolean,
    default: false
  },
  description: {
    type: String,
    default: null
  },
  command: {
    type: String,
    default: ""
  },
  class: {
    type: String,
    default: ""
  }
})
</script>

<style lang="less" scoped>

</style>