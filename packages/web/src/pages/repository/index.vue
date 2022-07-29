<template>
  <a-layout>
    <a-layout className="h-screen max-h-screen">
      <a-layout-sider
          v-if="editing === false"
          :resizeDirections="['right']"
          :style="{minWidth: '236px', maxWidth: '480px',width: '320px'}"
          class="h-screen"
      >
        <repo-sider v-if="repoId != null" :editable="editable" :repo="repoId" />
      </a-layout-sider>
      <a-layout-content class="h-screen overflow-y-auto">
        <a-layout className="h-full flex flex-col">
          <a-layout-header>
            <div class="header-container">
              <div class="w-full">
                <a-breadcrumb>
                  <a-breadcrumb-item>
                    <router-link :to="'/' + workspace">{{ workspace }}</router-link>
                  </a-breadcrumb-item>
                  <a-breadcrumb-item>
                    <router-link :to="'/' + workspace + '/' + repository">{{ repository }}</router-link>
                  </a-breadcrumb-item>
                  <a-breadcrumb-item v-if="document !== undefined">
                    <router-link :to="'/' + workspace + '/' + repository + '/' + documentId">
                      {{ documentTitle }}
                    </router-link>
                  </a-breadcrumb-item>
                </a-breadcrumb>
              </div>
              <a-button>分享</a-button>
              <template v-if="editable">
                <template v-if="document !== null || route.name === 'createDoc'">
                  <div v-if="editing === false" class="flex gap-x-2 ml-auto">
                    <a-button @click="handleDelete">删除</a-button>
                    <router-link :to="'/' + workspace + '/' + repository + '/' + documentId + '/edit'">
                      <a-button type="primary">
                        编辑
                      </a-button>
                    </router-link>
                  </div>
                  <a-button v-else type="primary" @click="submit">
                    提交
                  </a-button>
                </template>
              </template>
            </div>
          </a-layout-header>
          <div v-if="document !== null || route.name === 'createDoc'" class="document-content flex h-full">
            <div v-if="loading" class="w-full h-full flex items-center align-center justify-center">
              <a-spin :loading="loading" tip="加载中" />
            </div>
            <template v-else>
              <editor ref="editor" :key="[documentTitle, editing, route.name, route.params]"
                      v-if="documentContent != null || route.name === 'createDoc'"
                      :content="documentContent" :title="documentTitle" :readOnly="!editing"
                      :padding="editing"
              />
            </template>
          </div>
          <div v-if="document === null && route.name != 'createDoc'"
               class="h-full flex items-center align-center justify-center">
            <icon-empty class="empty-icon" />
            <p class="empty-description">请选择一个文档</p>
          </div>
          <!--          <a-layout-footer>-->
          <!--            <div class="document-footer">Footer</div>-->
          <!--          </a-layout-footer>-->
        </a-layout>
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script lang="ts" setup>
import { useRoute, useRouter } from "vue-router";
import { getRepo } from "../../api/repository";
import RepoSider from "./components/repo-sider.vue";
import { ref, watch } from "vue";
import { createDocument, deleteDocument, getDocument, updateDocument } from "../../api/document";
import Editor from "../../components/editor/editor.vue";
import { Message } from "@arco-design/web-vue";

const route = useRoute()
const router = useRouter()
const editing = ref(route.name === "document-edit")
const workspace = ref(route.params.workspace)
const repository = ref(route.params.repository)
const document = ref(null)
const documentTitle = ref(null)
const documentId = ref(route.params.document)
const documentContent = ref(null)
const repoId = ref(null)
const editor = ref<any>()
const loading = ref(false)
const editable = ref(false)
const readable = ref(false)

const initDoc = () => {
  loading.value = true
  getRepo(workspace.value.toString(), repository.value.toString()).then((response) => {
    repoId.value = response.data.id
    editable.value = response.data.writable
    readable.value = response.data.readable
    if (readable.value === false) {
      Message.error("你没有权限访问该仓库")
      router.push("/")
    }

    if (documentId.value) {
      getDocument(response.data.id, documentId.value.toString()).then((response) => {
        document.value = response.data
        documentTitle.value = response.data.title
        documentContent.value = response.data.content
        loading.value = false
      })
    } else {
      document.value = null
      documentTitle.value = null
      documentContent.value = null
      loading.value = false
    }
  })
}

const submit = () => {
  if (route.name === 'createDoc') {
    const parent = route.params.parent.toString() === "root" ? null : route.params.parent.toString()
    createDocument(repoId.value!!, editor.value.getTitle(), editor.value.getHTML(), route.params.type.toString(), parent ? parent : undefined).then((response) => {
      Message.success("创建成功")
      router.push("/" + workspace.value + "/" + repository.value + "/" + response.data.id)
    })
  } else {
    if (editor.value.getTitle() != null) {
      updateDocument(repoId.value!!, documentId.value.toString(), editor.value.getTitle(), editor.value.getHTML()).then((response) => {
        router.push('/' + workspace.value + '/' + repository.value + '/' + documentId.value)
        document.value = response.data
        documentTitle.value = response.data.title
        documentContent.value = response.data.content
      })
    } else {
      Message.error("请输入文档标题")
    }
  }
}

initDoc()

watch(route, (to, from) => {
  if (to.name === "repository" || to.name === "document" || to.name === "document-edit") {
    workspace.value = to.params.workspace
    repository.value = to.params.repository
    documentId.value = to.params.document
    editing.value = to.name == "document-edit"
    initDoc()
  }
  if (to.name === "createDoc") {
    workspace.value = to.params.workspace
    repository.value = to.params.repository
    documentId.value = to.params.document
    editing.value = true
    initDoc()
  }
})

const handleDelete = () => {
  if (document.value) {
    deleteDocument(repoId.value!!, documentId.value.toString()).then((response) => {
      Message.success("删除成功")
      router.push("/" + workspace.value + "/" + repository.value)
    })
  }
}
</script>

<style lang="less" scoped>
.header-container {
  display: flex;
  height: 48px;
  align-items: center;
  padding: 0 16px 0 16px;
  gap: 0.5rem;
}

.empty-icon {
  font-size: 3rem;
  color: var(--color-text-2);
}

.empty-description {
  margin-left: 8px;
  font-size: 1.5rem;
  color: var(--color-text-2);
}
</style>