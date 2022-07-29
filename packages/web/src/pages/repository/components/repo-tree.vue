<template>
  <a-input-search ref='searchBar' v-model="searchKey" class="my-2" placeholder="搜索..."></a-input-search>
  <div v-if="loading" class="w-full h-full flex items-center align-center justify-center">
    <a-spin :loading="loading" tip="加载中" />
  </div>
  <div v-else>
    <a-tree
        :data="treeData"
        :default-selected-keys="document"
        :default-expand-all="false"
        blockNode
        draggable
        @drop="onDrop"
        @select="handleSelect"
    >
      <template v-slot:drag-icon />
    </a-tree>
  </div>
</template>

<script lang="ts" setup>
import { ref, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { getDocuments } from "../../../api/document";

const searchKey = ref('')
const router = useRouter()
const route = useRoute()

const workspace = route.params.workspace
const repository = route.params.repository
const document = [route.params.document]
const loading = ref(true)

const originData = ref<any[]>([])

const props = defineProps({
  repo: {
    type: String,
    default: '',
  }
})

watch(searchKey, (newValue) => {
  treeData.value = searchData(newValue)
})

const handleSelect = (key: any) => {
  router.push("/" + workspace + "/" + repository + "/" + key)
}

getDocuments(props.repo).then((response) => {
  originData.value = response.data
  treeData.value = originData.value
  loading.value = false
})

const treeData = ref()

const searchData = (keyword: string) => {
  const loop = (data: any[]) => {
    const result: any[] = [];
    data.forEach((item: { title: string; children: any; }) => {
      if (item.title.toLowerCase().indexOf(keyword.toLowerCase()) > -1) {
        result.push({ ...item });
      } else if (item.children) {
        const filterData = loop(item.children);
        if (filterData.length) {
          result.push({
            ...item,
            children: filterData
          })
        }
      }
    })
    return result;
  }

  return loop(originData.value);
}

const onDrop = ({ dragNode, dropNode, dropPosition }: any) => {
  console.log(dragNode, dropNode, dropPosition)
  // moveDocument(props.repo, dragNode.key, dropNode.key,dropPosition).then((response) => {
  //   if (response.code === 200) {
  //     Message.success('移动成功')
  //   } else {
  //     Message.error('移动失败')
  //   }
  // })
  const newData = treeData.value;
  const loop = (data: any, key: any, callback: any) => {
    data.some((item: any, index: any, arr: any) => {
      if (item.key === key) {
        callback(item, index, arr);
        return true;
      }
      if (item.children) {
        return loop(item.children, key, callback);
      }
      return false;
    });
  };

  loop(newData, dragNode.key, (_: any, index: any, arr: any) => {
    arr.splice(index, 1);
  });

  if (dropPosition === 0) {
    loop(newData, dropNode.key, (item: any) => {
      item.children = item.children || [];
      item.children.push(dragNode);
    });
  } else {
    loop(newData, dropNode.key, (_: any, index: any, arr: any) => {
      arr.splice(dropPosition < 0 ? index : index + 1, 0, dragNode);
    });
  }
}

function getMatchIndex(title: string) {
  if (!searchKey.value) return -1;
  return title.toLowerCase().indexOf(searchKey.value.toLowerCase());
}
</script>

<style lang="less" scoped>

</style>