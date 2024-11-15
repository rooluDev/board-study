import { createRouter, createWebHistory } from 'vue-router';
import List from '@/pages/boards/free/list/ListBoard.vue';
import Write from '@/pages/board/free/write/WriteBoard.vue';
import Modify from '@/pages/board/free/modify/ModifyBoard.vue';
import View from '@/pages/boards/free/view/ViewBoard.vue';
import Error from '@/pages/error/Error.vue';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/boards/free/list',
      name: 'List',
      component: List,
    },
    {
      path: '/board/free/write',
      name: 'Write',
      component: Write,
    },
    {
      path: '/board/free/modify',
      name: 'Modify',
      component: Modify,
    },
    {
      path: '/boards/free/view/:id',
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
