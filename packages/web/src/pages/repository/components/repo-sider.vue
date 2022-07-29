<template>
  <div class="container">
    <div>
      <div class="flex my-2">
        <router-link class="w-full" to="/">
          <a-button long>返回工作台</a-button>
        </router-link>
      </div>
      <div class="flex w-full justify-between items-center">
        <a-avatar :size="48">R</a-avatar>
        <a-button v-if="editable" @click="handleClick">
          <template #icon>
            <icon-plus />
          </template>
        </a-button>
        <a-modal v-model:visible="visible" @ok="handleOk" @cancel="handleCancel">
          <template #title>
            新建文档
          </template>
          <div>
            <a-form :model="form">
              <a-form-item label="文档类型">
                <a-select v-model="form.type">
                  <a-option>文档</a-option>
                  <a-option>目录</a-option>
                </a-select>
              </a-form-item>
              <a-form-item label="文档路径">
                <a-tree-select
                    v-model="form.parent"
                    :data="treeData"
                ></a-tree-select>
                <template #extra>
                  <div>为空则为根目录</div>
                </template>
              </a-form-item>
            </a-form>
          </div>
        </a-modal>
      </div>
    </div>
    <div class="h-full">
      <div class="document-action-bar">
        <!--        <a-tooltip-->
        <!--            position="bottom"-->
        <!--            mini-->
        <!--            content={folded ? "全部折叠" : "全部展开"}-->
        <!--        >-->
        <!--        <div class="item" onClick={() => setFolded(!folded)}>-->
        <!--        {folded ? <IconMenuFold /> : <IconMenuUnfold />}-->
        <!--      </div>-->
        <!--      </a-tooltip>-->
        <div class="item">
          <!--        <IconMoreVertical />-->
        </div>
      </div>
      <repo-tree :repo="props.repo" />
    </div>
  </div>
</template>

<script lang="ts" setup>
import { useRoute, useRouter } from "vue-router";
import RepoTree from "./repo-tree.vue";
import { reactive, ref, watch } from "vue";
import { getDocuments } from "../../../api/document";

const route = useRoute()
const router = useRouter()
const workspace = route.params.workspace
const repository = route.params.repository
const visible = ref(false);
const treeData = ref([])
const form = reactive({
  parent: 'root',
  type: '文档',
})

const props = defineProps({
  repo: {
    type: String
  },
  editable: {
    type: Boolean
  }
})

getDocuments(props.repo!!).then((response) => {
  treeData.value = response.data
})

const handleClick = () => {
  visible.value = true;
};
const handleOk = () => {
  visible.value = false;
  router.push({
    name: 'createDoc',
    params: {
      workspace,
      repository,
      type: form.type,
      parent: form.parent
    }
  })
};
const handleCancel = () => {
  visible.value = false;
}
</script>

<style lang="less" scoped>
.container {
  padding-left: 12px;
  overflow: hidden;
  height: 100%;
  display: flex;
  flex-direction: column;
}
</style>