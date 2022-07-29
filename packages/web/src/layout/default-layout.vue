<template>
  <a-layout :hasSider="$screens.current.value === 'md'" class="h-screen w-screen">
    <slot name="nav" />
    <a-layout>
      <a-layout-content>
        <slot name="content" />
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>