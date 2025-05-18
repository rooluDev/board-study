import { createRouter, createWebHistory } from 'vue-router';
import List from '@/pages/board/list/ListBoard.vue';
import Post from '@/pages/board/post/PostBoard.vue';
import Edit from '@/pages/board/edit/EditBoard.vue';
import View from '@/pages/board/view/ViewBoard.vue';
import Error from '@/pages/error/Error.vue';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/board/list',
      name: 'List',
      component: List,
    },
    {
      path: '/board/post',
      name: 'Post',
      component: Post,
    },
    {
      path: '/board/edit/:id',
      name: 'Edit',
      component: Edit,
    },
    {
      path: '/board/view/:id',
      name: 'View',
      component: View,
    },
    {
      path: '/error',
      name: 'Error',
      component: Error,
    },
  ],
});

export default router;
