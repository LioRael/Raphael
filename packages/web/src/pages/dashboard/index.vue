<template>
  <default-layout>
    <template v-slot:nav>
      <nav-bar />
    </template>
    <template v-slot:content>
      <a-layout-header>
        <div class="dashboard-action-bar">
          <div class="justify-self-start justify-between">
            <a-select :default-value="workspace" :style="{width:'128px'}" placeholder="工作空间 ..."
                      @update:modelValue="changeWorkspace">
              <a-option>Leosouthey</a-option>
              <a-option>Faithl</a-option>
            </a-select>
          </div>
          <div class="justify-self-end justify-between">
            <a-dropdown v-if="workspace != null" style="width: 128px" trigger="hover">
              <a-button>新建</a-button>
              <template #content>
                <a-doption @click="handleClick">知识库</a-doption>
              </template>
              <a-modal v-model:visible="create" @ok="handleOk" @cancel="handleCancel" @before-ok="handleBeforeOk">
                <template #title>
                  新建知识库
                </template>
                <div>
                  <a-form ref="formRef" :model="form">
                    <a-form-item field="name" label="知识库名称"
                                 :rules="[{required:true,message:'你必须填写知识库名称'}]">
                      <a-input v-model="form.name" />
                    </a-form-item>
                    <a-form-item field="description" label="知识库描述"
                                 :rules="[{required:true,message:'你必须填写描述'}]">
                      <a-textarea v-model="form.description" />
                    </a-form-item>
                    <a-form-item field="type" label="可见类型" :rules="[{required:true,message:'你必须选择可见类型'}]">
                      <a-select v-model="form.type">
                        <a-option>公开</a-option>
                        <a-option>私有</a-option>
                      </a-select>
                    </a-form-item>
                  </a-form>
                </div>
              </a-modal>
            </a-dropdown>
          </div>
        </div>
      </a-layout-header>
      <div class="dashboard-wrapper">
        <div class="dashboard-title">知识库</div>
        <div class="dashboard-content">
          <a-row>
            <a-col v-for="repo in repos" :lg="4" :md="8" :sm="12" :xs="24">
              <a-card :bordered="false" :title="repo.name" class="dashboard-card"
                      hoverable @click="$router.push('/' + workspace + '/' + repo.name)">
                {{ repo.description }}
              </a-card>
            </a-col>
          </a-row>
        </div>
      </div>
    </template>
  </default-layout>
</template>

<script lang="ts" setup>
import DefaultLayout from "../../layout/default-layout.vue";
import NavBar from "../../components/layouts/nav-bar.vue";
import { getDashboardStore } from "../../store/modules/dashboard";
import { createRepo, getRepoList } from "../../api/repository";
import { reactive, ref } from "vue";
import { Message } from "@arco-design/web-vue";

const store = getDashboardStore();
let workspace = store.getWorkspace;
const repos = ref(null)
const create = ref(false);
const formRef = ref();
const form = reactive({
  name: '',
  description: '',
  type: '公开'
});

const changeWorkspace = (inputValue: any) => {
  store.setWorkspace(inputValue);
  workspace = inputValue;
  query()
}

const query = () => {
  if (workspace) {
    getRepoList(workspace!).then((response) => {
      repos.value = response.data
    })
  }
}
query()

const handleBeforeOk = async (done: any) => {
  const result = await formRef.value.validate()
  if (result!=null) {
    done(false)
  } else {
    createRepo(workspace!!, form.name, form.description, form.type).then((response) => {
      query()
      if (response.code == 400) {
        Message.error(response.message);
      }
    })
    done(true)
  }
}
const handleClick = () => {
  create.value = true;
};
const handleOk = () => {
  create.value = false;
};
const handleCancel = () => {
  create.value = false;
}

</script>

<style lang="less" scoped>
.dashboard-wrapper {
  padding: 0 32px;
}

.dashboard-action-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  align-content: center;
  text-align: center;
  height: 58px;
  box-shadow: 0 2px 5px 0 rgb(0 0 0 / 8%);
  padding: 32px;
}

.dashboard-title {
  padding: 32px 0;
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.dashboard-card {
  box-shadow: 0 2px 5px 0 rgb(0 0 0 / 16%);
  height: 200px;
  transition-property: all;
  cursor: pointer;
  border-radius: 8px;

  &:hover {
    transform: translateY(-4px);
  }
}
</style>