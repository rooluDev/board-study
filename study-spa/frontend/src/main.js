import { createApp } from 'vue';
import App from './App.vue';
import router from '@/router';

const app = createApp(App);
app.use(router).mount('#app');

app.config.errorHandler = () => {
  router.push({
    name: 'Error',
  });
};
